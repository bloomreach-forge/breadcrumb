package org.onehippo.forge.breadcrumb.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
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
 * sitemenu items of the menu named 'main' or from multiple menu's as configured
 * by parameter 'breadcrumb-menus'. The last ("trailing") part of the breadcrumb 
 * is generated from the resolved sitemap item belonging to the current request.
 *  
 * The trailing items are based on the current resolved sitemap item and then 
 * moving upwards, until the deepest menu item is encountered. 
 */
public class BreadcrumbProvider {

    public static final String ATTRIBUTE_NAME = "breadcrumb";
    public static final String PARAMETER_MENUS = "breadcrumb-menus";
    public static final String PARAMETER_SEPARATOR = "breadcrumb-separator";
    
    private final BaseHstComponent component;
    private boolean addTrailingDocumentOnly = false; 

    /** 
     * Constructor 
     */
    public BreadcrumbProvider(BaseHstComponent component) {
        super();
        
        this.component = component;
    }
    
    /** 
     * Constructor with an extra flag that determines behaviour for the trailing items 
     */
    public BreadcrumbProvider(BaseHstComponent component, boolean addTrailingDocumentOnly) {
        super();
        
        this.component = component;
        this.addTrailingDocumentOnly = addTrailingDocumentOnly;
    }
    
     /**
     * Generate the breadcrumb.
     * 
     * @param request
     * @return
     */
    public Breadcrumb getBreadcrumb(HstRequest request){

        List<String> siteMenuNames = getSitemenuNames(request);

        // match deepest menu item for multiple configured menus
        int i = 0; 
        HstSiteMenuItem deepestMenuItem = null;
        while ((i < siteMenuNames.size()) && (deepestMenuItem == null)) {
            HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(siteMenuNames.get(i));
            deepestMenuItem = menu.getDeepestExpandedItem();
            
            i++;
        }
        
        // create items if a menu item is found
        List<BreadcrumbItem> finalList = new ArrayList<BreadcrumbItem>();
        if (deepestMenuItem != null) {
            List<BreadcrumbItem> menuBreadcrumbItems = getMenuBreadcrumbItems(request, deepestMenuItem);
            List<BreadcrumbItem> trailingBreadcrumbItems = getTrailingBreadcrumbItems(request, deepestMenuItem);
        
            finalList.addAll(menuBreadcrumbItems);
            finalList.addAll(trailingBreadcrumbItems);
        }
        
        return new Breadcrumb(finalList, getSeparator(request)); 
    }

    /**
     * The multiple site menu names are configured by configuration parameter 
     * "breadcrumb-menus", defaulting to "main".
     * 
     * @param request
     * @return
     */
    protected List<String> getSitemenuNames(HstRequest request){
        
        List<String> list = new ArrayList<String>(0);
        String menuNames = component.getParameter(PARAMETER_MENUS, request);
        if (menuNames == null) {
            list.add("main");
        }
        else {
            String[] names = menuNames.split(",");
            for (int i = 0; i < names.length; i++) {
                list.add(names[i].trim());
            }
        }
        return list;
    }
    
    /**
     * Get the component as given in the constructor.
     */
    protected BaseHstComponent getComponent(){
        return component;
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
     * @param deepestMenuItem 
     * @return list of menu breadcrumb items
     */
    protected List<BreadcrumbItem> getMenuBreadcrumbItems(HstRequest request, HstSiteMenuItem menuItem){
        List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();

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
     * @param deepestExpandedMenuItem 
     * @return list of trailing breadcrumb items
     */
    protected List<BreadcrumbItem> getTrailingBreadcrumbItems(HstRequest request, HstSiteMenuItem deepestExpandedMenuItem){

        List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();

        ResolvedSiteMapItem currentSmi = request.getRequestContext().getResolvedSiteMapItem();
        HippoBean currentBean = getComponent().getBeanForResolvedSiteMapItem(request, currentSmi);
        
        if (currentBean != null) {
            ResolvedSiteMapItem deepestExpandedmenuItemSmi = deepestExpandedMenuItem.resolveToSiteMapItem(request);
            HippoBean deepestExpandedMenuItemBean = getComponent().getBeanForResolvedSiteMapItem(request, deepestExpandedmenuItemSmi);
            
            if (addTrailingDocumentOnly) {
                if (currentBean instanceof HippoDocument && !deepestExpandedMenuItemBean.equalCompare(currentBean)) {
                    items.add(getBreadcrumbItem(request, currentBean));
                }
            }
            else {
                // parent steps based on ancestor bean
                if (deepestExpandedMenuItemBean != null && deepestExpandedMenuItemBean.isAncestor(currentBean)){
                    
                    while (!currentBean.isSelf(deepestExpandedMenuItemBean)){
                        items.add(getBreadcrumbItem(request, currentBean));
                        currentBean = currentBean.getParentBean();
                    }
                }
                
                // try to determine parent steps based on path info in case the 
                // menuItemBean is not an ancestor, which occurs for instance  
                // when faceted navigation is used on the menu item
                else {
                    String ancestorPath = deepestExpandedmenuItemSmi.getPathInfo();
                    String currentPath = currentSmi.getPathInfo();
                    
                    if (currentPath.startsWith(ancestorPath)) {
                        String trailingPath = currentPath.substring(ancestorPath.length());
                        
                        if (trailingPath.startsWith("/")) {
                            trailingPath = trailingPath.substring(1);
                        }
                    
                        int steps = trailingPath.split("/").length;
                        
                        for (int i = 0; i < steps; i++) {
                            items.add(getBreadcrumbItem(request, currentBean));
                            currentBean = currentBean.getParentBean();
                        }
                    }
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
