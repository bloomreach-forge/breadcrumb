/*
 * Copyright 2009-2017 Hippo B.V. (http://www.onehippo.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * <p>
 * This component creates a basic breadcrumb, but can be easily extended for
 * specific, custom needs.
 * <p>
 * By default, the first part of the breadcrumb is generated from the expanded
 * site menu items of the menu named 'main' or from multiple menu's as configured
 * by parameter 'breadcrumb-menus'. The last ("trailing") part of the breadcrumb
 * is generated from the resolved sitemap item belonging to the current request.
 * <p>
 * The trailing items are based on the current resolved sitemap item and then
 * moving upwards, until the highest menu item is encountered.
 */
public class BreadcrumbProvider {

    public static final String ATTRIBUTE_NAME = "breadcrumb";
    public static final String PARAMETER_MENUS = "breadcrumb-menus";
    public static final String PARAMETER_SEPARATOR = "breadcrumb-separator";
    public static final String DEFAULT_MENU_NAME = "main";
    public static final String DEFAULT_SEPARATOR = "&#187;";

    private final BaseHstComponent component;
    private boolean addTrailingDocumentOnly = false;
    private final String breadcrumbMenus;
    private final String breadcrumbSeparator;

    /**
     * Constructor
     *
     * @param component component that creates this provider
     */
    public BreadcrumbProvider(final BaseHstComponent component) {
        this.component = component;
        this.breadcrumbMenus = component.getComponentParameter(PARAMETER_MENUS);
        this.breadcrumbSeparator = component.getComponentParameter(PARAMETER_SEPARATOR);
    }

    /**
     * Constructor with an extra flag that determines behaviour for the trailing items
     *
     * @param component               component that creates this provider
     * @param addTrailingDocumentOnly flag determining behaviour whether to add just one trailing document or all.
     */
    @SuppressWarnings("unused")
    public BreadcrumbProvider(final BaseHstComponent component, final boolean addTrailingDocumentOnly) {
        this(component);
        this.addTrailingDocumentOnly = addTrailingDocumentOnly;
    }

    /**
     * Generate the breadcrumb.
     *
     * @param request HST request
     * @return the generated breadcrumb
     */
    public Breadcrumb getBreadcrumb(final HstRequest request) {

        final List<String> siteMenuNames = getSitemenuNames();

        // match deepest menu item for multiple configured menus
        int i = 0;
        HstSiteMenuItem deepestMenuItem = null;
        while (i < siteMenuNames.size() && deepestMenuItem == null) {
            HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(siteMenuNames.get(i));
            deepestMenuItem = menu.getDeepestExpandedItem();
            i++;
        }

        // create items if a menu item is found
        final List<BreadcrumbItem> finalList = new ArrayList<>();
        if (deepestMenuItem != null) {
            final List<BreadcrumbItem> menuBreadcrumbItems = getMenuBreadcrumbItems(request, deepestMenuItem);
            final List<BreadcrumbItem> trailingBreadcrumbItems = getTrailingBreadcrumbItems(request, deepestMenuItem);

            finalList.addAll(menuBreadcrumbItems);
            finalList.addAll(trailingBreadcrumbItems);
        }

        return new Breadcrumb(finalList, getSeparator());
    }

    /**
     * The multiple site menu names are configured by configuration parameter
     * "breadcrumb-menus", defaulting to "main".
     *
     * @param request HST request
     * @return all configured menu names, or "main"
     * @deprecated use {@link #getSitemenuNames()}  instead
     */
    @Deprecated
    protected List<String> getSitemenuNames(final HstRequest request) {
        return getSitemenuNames();
    }

    /**
     * The multiple site menu names are configured by configuration parameter
     * "breadcrumb-menus", defaulting to "main".
     *
     * @return all configured menu names, or "main"
     */
    protected List<String> getSitemenuNames() {

        final List<String> list = new ArrayList<>();
        if (breadcrumbMenus == null) {
            list.add(DEFAULT_MENU_NAME);
        } else {
            final String[] names = breadcrumbMenus.split(",");
            for (String name : names) {
                list.add(name.trim());
            }
        }
        return list;
    }

    /**
     * Get the component as given in the constructor.
     *
     * @return the component as given in the constructor
     */
    protected BaseHstComponent getComponent() {
        return component;
    }

    /**
     * Returns the separator string that separates two breadcrumb items.
     * It is configured by configuration parameter "breadcrumb-separator",
     * defaulting to "»".
     *
     * @param request HST request
     * @return configured or default separator between breadcrumb items
     * @deprecated use {@link #getSeparator()}  instead
     */
    @Deprecated
    protected String getSeparator(HstRequest request) {
        return getSeparator();
    }

    /**
     * Returns the separator string that separates two breadcrumb items.
     * It is configured by configuration parameter "breadcrumb-separator",
     * defaulting to "»".
     *
     * @return configured or default separator between breadcrumb items
     */
    protected String getSeparator() {
        return (breadcrumbSeparator != null) ? breadcrumbSeparator : DEFAULT_SEPARATOR;
    }

    /**
     * Generate the breadcrumb items which correspond to the expanded menu item tree.
     *
     * @param request  HST request
     * @param menuItem HST menu item
     * @return list of menu breadcrumb items
     */
    protected List<BreadcrumbItem> getMenuBreadcrumbItems(final HstRequest request, final HstSiteMenuItem menuItem) {
        final List<BreadcrumbItem> items = new ArrayList<>();

        HstSiteMenuItem traverseUp = menuItem;
        while (traverseUp != null) {
            final BreadcrumbItem item = getBreadcrumbItem(request, traverseUp);
            if (item != null) {
                items.add(item);
            }
            traverseUp = traverseUp.getParentItem();
        }

        Collections.reverse(items);

        return items;
    }

    /**
     * Generate the trailing breadcrumb items. By default, the trailing items
     * are derived from the bean structure of the resolved sitemap item of the
     * current request.
     *
     * @param request                 HST request
     * @param deepestExpandedMenuItem HST menu item
     * @return list of trailing breadcrumb items
     */
    protected List<BreadcrumbItem> getTrailingBreadcrumbItems(final HstRequest request, final HstSiteMenuItem deepestExpandedMenuItem) {

        final List<BreadcrumbItem> items = new ArrayList<>();

        final ResolvedSiteMapItem currentSmi = request.getRequestContext().getResolvedSiteMapItem();
        final HippoBean currentBean = getComponent().getBeanForResolvedSiteMapItem(request, currentSmi);

        if (currentBean != null) {
            final ResolvedSiteMapItem deepestExpandedmenuItemSmi = deepestExpandedMenuItem.resolveToSiteMapItem();
            final HippoBean deepestExpandedMenuItemBean = getComponent().getBeanForResolvedSiteMapItem(request, deepestExpandedmenuItemSmi);

            if (addTrailingDocumentOnly) {
                addTrailingDocument(items, currentBean, deepestExpandedMenuItemBean, request);
            } else {
                if (deepestExpandedMenuItemBean != null) {
                    // deepest menu item bean must be (not same) AND ancestor of the current bean
                    if (!deepestExpandedMenuItemBean.isSelf(currentBean) && deepestExpandedMenuItemBean.isAncestor(currentBean)) {
                        // add trailing items based on content structure
                        addAncestorBasedParentItems(items, currentBean, deepestExpandedMenuItemBean, request);
                    }
                }

                // try to determine parent steps based on path info in case the
                // menuItemBean is not an ancestor, which occurs for instance
                // when faceted navigation is used on the menu item
                else {
                    addURLBasedParentItems(items, currentBean, currentSmi, deepestExpandedmenuItemSmi, request);
                }

                Collections.reverse(items);
            }
        }

        return items;
    }

    /**
     * Add one trailing document since the addTrailingDocumentOnly flag is up.
     *
     * @param items                       list of breadcrumb items
     * @param currentBean                 a bean described by URL that is in the child tree of the ancestor bean
     * @param deepestExpandedMenuItemBean bean corresponding to the deepest expanded site menu item
     * @param request                     HST request
     */
    protected void addTrailingDocument(final List<BreadcrumbItem> items, final HippoBean currentBean,
                                       final HippoBean deepestExpandedMenuItemBean, final HstRequest request) {

        if (currentBean instanceof HippoDocument && !deepestExpandedMenuItemBean.equalCompare(currentBean)) {
            final BreadcrumbItem item = getBreadcrumbItem(request, currentBean);
            if (item != null) {
                items.add(item);
            }
        }

    }

    /**
     * Add breadcrumb items based on an ancestor
     *
     * @param items        list of breadcrumb items
     * @param currentBean  a bean described by URL that is in the child tree of the ancestor bean
     * @param ancestorBean a bean that the ancestor of the current bean
     * @param request      HST request
     */
    protected void addAncestorBasedParentItems(final List<BreadcrumbItem> items, final HippoBean currentBean,
                                               final HippoBean ancestorBean, final HstRequest request) {

        HippoBean currentItemBean = currentBean;
        while (!currentItemBean.isSelf(ancestorBean)) {
            final BreadcrumbItem item = getBreadcrumbItem(request, currentItemBean);
            if (item != null) {
                items.add(item);
            }
            currentItemBean = currentItemBean.getParentBean();
        }

    }

    /**
     * Add breadcrumb items
     *
     * @param items                      list of breadcrumb items
     * @param currentBean                bean described by URL
     * @param currentSmi                 site map item of the current bean
     * @param deepestExpandedmenuItemSmi deepest expanded site map item (its info should be the first part of the
     *                                   currentSmi's info for the implementation to actually add items)
     * @param request                    HST request
     */
    protected void addURLBasedParentItems(final List<BreadcrumbItem> items, final HippoBean currentBean,
                                          final ResolvedSiteMapItem currentSmi,
                                          final ResolvedSiteMapItem deepestExpandedmenuItemSmi,
                                          final HstRequest request) {
        final String ancestorPath = deepestExpandedmenuItemSmi.getPathInfo();
        final String currentPath = currentSmi.getPathInfo();

        if (currentPath.startsWith(ancestorPath)) {
            String trailingPath = currentPath.substring(ancestorPath.length());

            if (trailingPath.startsWith("/")) {
                trailingPath = trailingPath.substring(1);
            }

            int steps = trailingPath.split("/").length;

            HippoBean currentItemBean = currentBean;
            for (int i = 0; i < steps; i++) {
                BreadcrumbItem item = getBreadcrumbItem(request, currentItemBean);
                if (item != null) {
                    items.add(item);
                }
                currentItemBean = currentItemBean.getParentBean();
            }
        }
    }

    /**
     * Creates a breadcrumb item belonging to a sitemenu item
     *
     * @param request  HST request (not used in default implementation)
     * @param menuItem menu item
     * @return breadcrumb item
     */
    @SuppressWarnings("unused")
    protected BreadcrumbItem getBreadcrumbItem(final HstRequest request, final HstSiteMenuItem menuItem) {
        return new BreadcrumbItem(menuItem.getHstLink(), menuItem.getName());
    }

    /**
     * Creates a breadcrumb item from a Hippo bean
     *
     * @param request HST request
     * @param bean    hippo bean from which to create link and name
     * @return breadcrumb item
     */
    protected BreadcrumbItem getBreadcrumbItem(final HstRequest request, final HippoBean bean) {
        return getBreadcrumbItem(request, bean, false/*navigationStateful*/);
    }

    /**
     * Creates a breadcrumb item from a Hippo bean
     *
     * @param request            HST request
     * @param bean               hippo bean from which to create link and name
     * @param navigationStateful is the created link navigation stateful
     * @return breadcrumb item
     */
    protected BreadcrumbItem getBreadcrumbItem(final HstRequest request, final HippoBean bean,
                                               final boolean navigationStateful) {
        final HstRequestContext context = request.getRequestContext();
        if (navigationStateful) {
            return new BreadcrumbItem(
                    context.getHstLinkCreator().create(bean.getNode(), context, null/*preferredItem*/, true/*fallback*/,
                            navigationStateful), bean.getDisplayName());
        } else {
            return new BreadcrumbItem(context.getHstLinkCreator().create(bean, context), bean.getDisplayName());

        }
    }
}
