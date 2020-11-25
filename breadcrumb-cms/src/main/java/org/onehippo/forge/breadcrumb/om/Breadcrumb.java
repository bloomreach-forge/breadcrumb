/*
 * Copyright 2009-2018 Hippo B.V. (http://www.onehippo.com)
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
package org.onehippo.forge.breadcrumb.om;

import java.util.List;

import org.onehippo.forge.breadcrumb.components.BreadcrumbProvider;

public class Breadcrumb {

	private List<BreadcrumbItem> items;
	private String separator;
	private String linkNotFoundMode;

	public Breadcrumb(final List<BreadcrumbItem> items, final String separator,
			final BreadcrumbProvider.LinkNotFoundMode linkNotFoundMode) {
		super();
		this.items = items;
		this.separator = separator;
		this.linkNotFoundMode = linkNotFoundMode == null ? null : linkNotFoundMode.name().toLowerCase();
	}

	/**
	 * Returns the list of breadcrumb items.
	 *
	 * @return breadcrumb items
	 */
	public List<BreadcrumbItem> getItems() {
		return items;
	}

	/**
	 * Separator for separating two breadcrumb items.
	 *
	 * @return separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Operation mode for breadcrumb entries that point to 404.
	 *
	 * @return linkNotFoundMode
	 */
	public String getLinkNotFoundMode() {
		return linkNotFoundMode;
	}

	@Override
	public String toString() {
		return super.toString() + "[separator=" + separator + ", items=" + items + ", linkNotFoundMode="
				+ linkNotFoundMode + "]";
	}
}
