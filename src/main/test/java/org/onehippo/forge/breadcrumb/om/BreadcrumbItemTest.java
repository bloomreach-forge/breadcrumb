/*
 * Copyright 2017 Hippo B.V. (http://www.onehippo.com)
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
import org.hippoecm.hst.mock.core.linking.MockHstLink;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BreadcrumbItemTest {

    @Test
    public void testEqualsHashCode() {

        final String title1 = "title1";
        final String title2 = "title2";
        final HstLink link1 = new MockHstLink("path1");
        final HstLink link2 = new MockHstLink("path2");

        final BreadcrumbItem item1 = new BreadcrumbItem(link1, title1);
        final BreadcrumbItem dupItem1 = new BreadcrumbItem(link1, title1);
        final BreadcrumbItem nullPathItem = new BreadcrumbItem(null, title1);
        final BreadcrumbItem nullPathItem2 = new BreadcrumbItem(null, title1);
        final BreadcrumbItem nullTitleItem = new BreadcrumbItem(link1, null);
        final BreadcrumbItem nullTitleItem2 = new BreadcrumbItem(link1, null);
        final BreadcrumbItem item2 = new BreadcrumbItem(link2, title2);
        final BreadcrumbItem mixedItem1 = new BreadcrumbItem(link1, title2);
        final BreadcrumbItem mixedItem2 = new BreadcrumbItem(link2, title1);

        assertTrue(item1.equals(dupItem1));
        //noinspection EqualsWithItself
        assertTrue(item1.equals(item1));
        assertTrue(nullPathItem.equals(nullPathItem2));
        assertTrue(nullTitleItem.equals(nullTitleItem2));
        assertFalse(item1.equals(null));
        assertFalse(item1.equals(nullPathItem));
        assertFalse(item1.equals(nullTitleItem));
        assertFalse(nullPathItem.equals(item1));
        assertFalse(nullTitleItem.equals(item1));
        assertFalse(item1.equals(link1));
        assertFalse(item1.equals(item2));
        assertFalse(item1.equals(mixedItem1));
        assertFalse(item1.equals(mixedItem2));
        assertFalse(mixedItem1.equals(mixedItem2));

        assertTrue(item1.hashCode() == dupItem1.hashCode());
        assertTrue(nullPathItem.hashCode() == nullPathItem2.hashCode());
        assertTrue(nullTitleItem.hashCode() == nullTitleItem2.hashCode());
        assertFalse(item1.hashCode() == nullPathItem.hashCode());
        assertFalse(item1.hashCode() == nullTitleItem.hashCode());
        assertFalse(nullPathItem.hashCode() == item1.hashCode());
        assertFalse(nullTitleItem.hashCode() == item1.hashCode());
        assertFalse(item1.hashCode() == link1.hashCode());
        assertFalse(item1.hashCode() == item2.hashCode());
        assertFalse(item1.hashCode() == mixedItem1.hashCode());
        assertFalse(item1.hashCode() == mixedItem2.hashCode());
        assertFalse(mixedItem1.hashCode() == mixedItem2.hashCode());
    }

}
