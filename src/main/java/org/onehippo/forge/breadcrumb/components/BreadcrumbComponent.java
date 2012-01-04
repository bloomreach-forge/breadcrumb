package org.onehippo.forge.breadcrumb.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

/**
 * Standard HST Breadcrumb component.
 */
public class BreadcrumbComponent extends BaseHstComponent {

    private final BreadcrumbProvider breadcrumbProvider = new BreadcrumbProvider(this);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response)
            throws HstComponentException {
        super.doBeforeRender(request, response);

        request.setAttribute(BreadcrumbProvider.ATTRIBUTE_NAME, breadcrumbProvider.getBreadcrumb(request));
    }
}
