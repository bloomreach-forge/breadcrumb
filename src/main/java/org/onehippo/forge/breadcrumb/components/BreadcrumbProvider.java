package org.onehippo.forge.breadcrumb.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.onehippo.forge.breadcrumb.om.Breadcrumb;
import org.onehippo.forge.breadcrumb.om.BreadcrumbItem;

/**
 * Provider that can be instantiated within a component to add breadcrumb 
 * functionalities by composition.   
 * 
 * This component creates a basic breadcrumb, but can be easily extended for 
 * specific, custom needs. 
 * 
 * By default, the first part of the breadcrumb is generated from the expanded 
 * sitemenu items, and the last ("trailing") part of the breadcrumb is generated 
 * from the resolved sitemap item belonging to the current request.
 *  
 * However, the trailing items are only derived when the bean belonging to the 
 * current resolved sitemap item is a child bean of the bean belonging to the 
 * deepest expanded sitemenu item. 
 */
public class BreadcrumbProvider {

    public static final String ATTRIBUTE_NAME = "breadcrumb";
    public static final String PARAMETER_MENU = "breadcrumb-menu";
    public static final String PARAMETER_SEPARATOR = "breadcrumb-separator";
    
    private final BaseHstComponent component;

    /** Constructor */
    public BreadcrumbProvider(BaseHstComponent component) {
        super();
        
        this.component = component;
    }
    
     /**
     * Generate the breadcrumb.
     * 
     * @param request
     * @return
     */
    public Breadcrumb getBreadcrumb(HstRequest request){
        
        HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(getSitemenuName(request));

        if (menu == null) {
            throw new HstComponentException("Cannot render breadcrumb: menu named '" 
                    + getSitemenuName(request) + "' not found.");
        }
        
        List<BreadcrumbItem> menuBreadcrumbItems = getMenuBreadcrumbItems(request, menu);
        List<BreadcrumbItem> trailingBreadcrumbItems = getTrailingBreadcrumbItems(request, menu);
        
        List<BreadcrumbItem> finalList = new ArrayList<BreadcrumbItem>();
        finalList.addAll(menuBreadcrumbItems);
        finalList.addAll(trailingBreadcrumbItems);
        
        return new Breadcrumb(finalList, getSeparator(request)); 
    }

    /**
     * The site menu name is configured by configuration parameter 
     * "breadcrumb-menu", defaulting to "main".
     * 
     * @param request
     * @return
     */
    protected String getSitemenuName(HstRequest request){
        
        String menuName = component.getParameter(PARAMETER_MENU, request);
        return (menuName != null) ? menuName : "main";
    }
    
    /**
     * Returns the separator string that separates two breadcrumb items.
     * It is configured by configuration parameter "breadcrumb-separator", 
     * defaulting to "»". 
     *
     * @param request
     * @return
     */
    protected String getSeparator(HstRequest request){

        String separator = component.getParameter(PARAMETER_SEPARATOR, request);
        return (separator != null) ? separator : "&#187;"; 
    }
    
    /**
     * Generate the breadcrumb items which correspond to the expanded menu item tree.
     * 
     * @param request
     * @param menu 
     * @return list of menu breadcrumb items
     */
    protected List<BreadcrumbItem> getMenuBreadcrumbItems (HstRequest request, HstSiteMenu menu){
        List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();

        HstSiteMenuItem menuItem = menu.getDeepestExpandedItem();
        
        while (menuItem != null){
            items.add(getBreadcrumbItem(request, menuItem));
            menuItem = menuItem.getParentItem();
        }
        
        Collections.reverse(items);
        
        return items;
    }
    
    /**
     * Generate the trailing breadcrumb items. By default, the trailing items 
     * are derived from the bean structure of the resolved sitemap item of the 
     * current request. 
     * 
     * @param request
     * @param menu 
     * @return list of trailing breadcrumb items
     */
    protected List<BreadcrumbItem> getTrailingBreadcrumbItems (HstRequest request, HstSiteMenu menu){

        List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();

        HstSiteMenuItem menuItem = menu.getDeepestExpandedItem();
        ResolvedSiteMapItem ancestorSmi = menuItem.resolveToSiteMapItem(request);
        ResolvedSiteMapItem currentSmi = request.getRequestContext().getResolvedSiteMapItem();
        HippoBean ancestorBean = component.getBeanForResolvedSiteMapItem(request, ancestorSmi);
        HippoBean childBean = component.getBeanForResolvedSiteMapItem(request, currentSmi);
        
        if (ancestorBean != null && childBean != null && ancestorBean.isAncestor(childBean)){
            
            while (!childBean.isSelf(ancestorBean)){
                items.add(getBreadcrumbItem(request,childBean));
                childBean = childBean.getParentBean();
            }
            
            Collections.reverse(items);
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
