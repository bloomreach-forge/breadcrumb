package org.onehippo.forge.breadcrumb.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.onehippo.forge.breadcrumb.om.Breadcrumb;
import org.onehippo.forge.breadcrumb.om.BreadcrumbItem;

/**
 * HST Breadcrumb component. 
 * 
 * This component creates a basic breadcrumb, but can be easily extended for specific, custom needs. 
 * By default, the first part of the breadcrumb is generated from the expanded sitemenu items, and the 
 * last ("trailing") part of the breadcrumb is generated from the resolved sitemap item belonging to the current request. 
 * However, the trailing items are only derived when the bean belonging to the current resolved sitemap item is
 * a child bean of the bean belonging to the deepest expanded sitemenu item. 
 * 
 */
public class BreadcrumbComponent extends BaseHstComponent {

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response)
			throws HstComponentException {		
	    super.doBeforeRender(request, response);
		request.setAttribute("breadcrumb", getBreadcrumb(request));
		
	}
	
	/**
	 * The default sitemenu is the "main" menu. Override this method if necessary.
	 * @param request
	 * @return
	 */
	protected String getSitemenuName(HstRequest request){
	    return "main";
	}
	
	/**
	 * Returns the separator string that separates two breadcrumb items.
	 * The default separator is "»". This could also be derived from an HST component parameter. 
	 * @param request
	 * @return
	 */
	protected String getSeparator(HstRequest request){
        return "&#187;"; 
    }
	
	/**
	 * Generate the breadcrumb
	 * 
	 * @param request
	 * @return
	 */
	protected Breadcrumb getBreadcrumb(HstRequest request){
	    List<BreadcrumbItem> menuBreadcrumbItems = getMenuBreadcrumbItems(request);
	    List<BreadcrumbItem> trailingBreadcrumbItems = getTrailingBreadcrumbItems(request);
        
	    List<BreadcrumbItem> finalList = new ArrayList<BreadcrumbItem>();
	    finalList.addAll(menuBreadcrumbItems);
        finalList.addAll(trailingBreadcrumbItems);
        
        return new Breadcrumb(finalList,getSeparator(request)); 
	}

	/**
	 * Generate the breadcrumb items which correspond to the expanded menu item tree
	 * 
	 * @param request
	 * @return list of breadcrumb items
	 */
	protected List<BreadcrumbItem> getMenuBreadcrumbItems (HstRequest request){
        List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();

        HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(getSitemenuName(request));
        if (menu != null){
            HstSiteMenuItem menuItem = menu.getDeepestExpandedItem();
            
            while (menuItem != null){
                items.add(getBreadcrumbItem(request, menuItem));
                menuItem = menuItem.getParentItem();
            }
            
            Collections.reverse(items);
        }
        
        return items;
	}
	
	/**
	 * Generate the trailing breadcrumb items. By default, the trailing items are
	 * derived from the bean structure of the resolved sitemap item of the current request. 
	 * 
	 * @param request
	 * @return list of breadcrumb items
	 */
	protected List<BreadcrumbItem> getTrailingBreadcrumbItems (HstRequest request){
	    List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
	    HstRequestContext context = request.getRequestContext();
	    HstSiteMenu menu = context.getHstSiteMenus().getSiteMenu(getSitemenuName(request));
	    if (menu != null){
	        HstSiteMenuItem menuItem = menu.getDeepestExpandedItem();
	        ResolvedSiteMapItem ancestorSmi = menuItem.resolveToSiteMapItem(request);
	        ResolvedSiteMapItem currentSmi = context.getResolvedSiteMapItem();
	        HippoBean ancestorBean = getBeanForResolvedSiteMapItem(request, ancestorSmi);
            HippoBean childBean = getBeanForResolvedSiteMapItem(request, currentSmi);
            
	        if (ancestorBean != null && childBean != null && ancestorBean.isAncestor(childBean)){
	            
	            while (!childBean.isSelf(ancestorBean)){
	                items.add(getBreadcrumbItem(request,childBean));
	                childBean = childBean.getParentBean();
	            }
	            
	            Collections.reverse(items);
	        }
	    }	    
	    
	    return items;
	}
	
	/**
	 * Creates a breadcrumb item belonging to a sitemenu item
	 * 
	 * @param request
	 * @param menuItem
	 * @return breadcrumb item
	 */
	protected BreadcrumbItem getBreadcrumbItem(HstRequest request, HstSiteMenuItem menuItem){
	    return new BreadcrumbItem(menuItem.getHstLink(),menuItem.getName());
	}
    

	/**
	 * Creates a breadcrumb item belonging to a Hippo bean
	 *  
	 * @param request
	 * @param bean
	 * @return breadcrumb item
	 */
	protected BreadcrumbItem getBreadcrumbItem(HstRequest request, HippoBean bean){
	    HstRequestContext context = request.getRequestContext();
        return new BreadcrumbItem(context.getHstLinkCreator().create(bean, context),bean.getName());
    }
	
}
