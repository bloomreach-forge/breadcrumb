package org.onehippo.forge.breadcrumb.components;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;


public interface BreadcrumbParametersInfo {

	@Parameter(name = "separator", displayName = BreadcrumbProvider.PARAMETER_SEPARATOR)
	String getSeparator();

	@Parameter(name = "linkNotFoundMode", defaultValue = "hide", displayName = BreadcrumbProvider.PARAMETER_LINK_NOT_FOUND_MODE)
	@DropDownList({ "hide", "unlink" })
	String getLinkNotFoundMode();

	@Parameter(name = "addTrailingDocumentOnly", displayName = BreadcrumbProvider.PARAMETER_ADD_TRAILING_DOCUMENT)
	boolean getDisplayOption();

	@Parameter(name = "addContentBased", displayName = BreadcrumbProvider.PARAMETER_ADD_CONTENT_BASED)
	boolean getAddContentBased();
	

	@Parameter(name = "breadcrumbMenus", displayName = BreadcrumbProvider.PARAMETER_MENUS, hideInChannelManager = true)
	boolean getbreadcrumbMenus();

}
