package org.onehippo.forge.breadcrumb.om;

import java.util.List;

public class Breadcrumb {

	private List<BreadcrumbItem> items;
	private String separator;
	
	public Breadcrumb(List<BreadcrumbItem> items, String separator) {
		super();
		this.items = items;
		this.separator=separator;    
	}

	/**
	 * Returns the list of breadcrumb items
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
	
}
