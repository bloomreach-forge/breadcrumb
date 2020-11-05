package org.onehippo.forge.breadcrumb.components;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface BreadcrumbConfigInfo {

	@Parameter(name = "separator", displayName = "separator")
	String getSeparator();

	@Parameter(name = "linkNotFoundMode", defaultValue = "hide")
	@DropDownList({ "hide", "unlink" })
	String getLinkNotFoundMode();

	@Parameter(name = "addTrailingDocumentOnly", displayName = "addTrailingDocumentOnly")
	boolean getDisplayOption();

	@Parameter(name = "addContentBased", displayName = "addContentBased")
	boolean getAddContentBased();

}
