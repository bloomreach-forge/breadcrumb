/*
 * Copyright 2009-2013 Hippo B.V. (http://www.onehippo.com)
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
