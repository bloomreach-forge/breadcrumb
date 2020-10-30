package org.onehippo.forge.breadcrumb.components;

import org.hippoecm.hst.core.parameters.Parameter;

public interface BreadcrumbConfigInfo {
	
	@Parameter(name = "separator",
            displayName = "Separator")
	String getSeparator();

	@Parameter(name = "addTrailingDocumentOnly",
            displayName = "addTrailingDocumentOnly")
	boolean getDisplayOption();

}
