package org.onehippo.forge.breadcrumb.om;

import org.hippoecm.hst.core.linking.HstLink;

public class BreadcrumbItem {

    public String title;
    public HstLink link;

    public BreadcrumbItem(HstLink link, String title) {
        this.link = link;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public HstLink getLink() {
        return link;
    }
}
