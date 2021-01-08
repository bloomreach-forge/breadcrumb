/*
 * Copyright 2020 Bloomreach
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
 */package org.onehippo.forge.breadcrumb.components;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface BreadcrumbParametersInfo {

    @Parameter(name = BreadcrumbProvider.PARAMETER_MENUS, defaultValue = BreadcrumbProvider.DEFAULT_MENU_NAME,
            hideInChannelManager = true)
    String getBreadcrumbMenus();

	@Parameter(name = BreadcrumbProvider.PARAMETER_SEPARATOR, defaultValue = BreadcrumbProvider.DEFAULT_SEPARATOR)
	String getSeparator();

	@Parameter(name = BreadcrumbProvider.PARAMETER_LINK_NOT_FOUND_MODE)
	@DropDownList({ "hide", "unlink" })
	String getLinkNotFoundMode();

	@Parameter(name = BreadcrumbProvider.PARAMETER_ADD_TRAILING_DOCUMENT_ONLY, defaultValue = "false")
	boolean getAddTrailingDocumentOnly();

	@Parameter(name = BreadcrumbProvider.PARAMETER_ADD_CONTENT_BASED, defaultValue = "false")
	boolean getAddContentBased();
}
