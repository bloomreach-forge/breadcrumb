package org.bloomreach.forge.breadcrumb.beans;

/*
 * Copyright 2014-2019 Hippo B.V. (http://www.onehippo.com)
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

import java.util.List;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.components.model.AuthorEntry;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "breadcrumbdemo:author")
@Node(jcrType = "breadcrumbdemo:author")
public class Author extends HippoDocument implements AuthorEntry {

    public static final String ROLE = "breadcrumbdemo:role";
    public static final String ACCOUNTS = "breadcrumbdemo:accounts";
    public static final String FULL_NAME = "breadcrumbdemo:fullname";
    public static final String IMAGE = "breadcrumbdemo:image";
    public static final String CONTENT = "breadcrumbdemo:content";

    @HippoEssentialsGenerated(internalName = "breadcrumbdemo:fullname")
    public String getFullName() {
        return getSingleProperty(FULL_NAME);
    }

    @HippoEssentialsGenerated(internalName = "breadcrumbdemo:content")
    public HippoHtml getContent() {
        return getHippoHtml(CONTENT);
    }

    @HippoEssentialsGenerated(internalName = "breadcrumbdemo:role")
    public String getRole() {
        return getSingleProperty(ROLE);
    }

    @HippoEssentialsGenerated(internalName = "breadcrumbdemo:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean(IMAGE, HippoGalleryImageSet.class);
    }

  	@HippoEssentialsGenerated(internalName = "breadcrumbdemo:accounts")
	  public List<Account> getAccounts() {
		    return getChildBeansByName(ACCOUNTS, Account.class);
	  }
}
