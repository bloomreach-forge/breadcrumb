/*
 * Copyright 2026 Bloomreach (http://www.bloomreach.com)
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BreadcrumbItemTest {

    // ------------------------------------------------------------------ equals

    @Test
    void equals_sameInstance_returnsTrue() {
        BreadcrumbItem item = new BreadcrumbItem(new MockHstLink("p"), "t");
        assertTrue(item.equals(item));
    }

    @Test
    void equals_equalPathAndTitle_returnsTrue() {
        HstLink link = new MockHstLink("path1");
        BreadcrumbItem a = new BreadcrumbItem(link, "title1");
        BreadcrumbItem b = new BreadcrumbItem(link, "title1");
        assertTrue(a.equals(b));
    }

    @Test
    void equals_differentPath_returnsFalse() {
        BreadcrumbItem a = new BreadcrumbItem(new MockHstLink("path1"), "title");
        BreadcrumbItem b = new BreadcrumbItem(new MockHstLink("path2"), "title");
        assertFalse(a.equals(b));
    }

    @Test
    void equals_differentTitle_returnsFalse() {
        HstLink link = new MockHstLink("path");
        BreadcrumbItem a = new BreadcrumbItem(link, "title1");
        BreadcrumbItem b = new BreadcrumbItem(link, "title2");
        assertFalse(a.equals(b));
    }

    @Test
    void equals_null_returnsFalse() {
        BreadcrumbItem item = new BreadcrumbItem(new MockHstLink("p"), "t");
        assertFalse(item.equals(null));
    }

    @Test
    void equals_differentType_returnsFalse() {
        BreadcrumbItem item = new BreadcrumbItem(new MockHstLink("p"), "t");
        assertFalse(item.equals("a string"));
    }

    @Test
    void equals_bothNullLinks_returnsTrue() {
        BreadcrumbItem a = new BreadcrumbItem(null, "title");
        BreadcrumbItem b = new BreadcrumbItem(null, "title");
        assertTrue(a.equals(b));
    }

    @Test
    void equals_oneNullLink_returnsFalse() {
        BreadcrumbItem withLink = new BreadcrumbItem(new MockHstLink("p"), "title");
        BreadcrumbItem nullLink = new BreadcrumbItem(null, "title");
        assertFalse(withLink.equals(nullLink));
        assertFalse(nullLink.equals(withLink));
    }

    @Test
    void equals_bothNullTitles_returnsTrue() {
        HstLink link = new MockHstLink("path");
        BreadcrumbItem a = new BreadcrumbItem(link, null);
        BreadcrumbItem b = new BreadcrumbItem(link, null);
        assertTrue(a.equals(b));
    }

    @Test
    void equals_oneNullTitle_returnsFalse() {
        HstLink link = new MockHstLink("path");
        BreadcrumbItem withTitle = new BreadcrumbItem(link, "t");
        BreadcrumbItem nullTitle = new BreadcrumbItem(link, null);
        assertFalse(withTitle.equals(nullTitle));
    }

    // ----------------------------------------------------------------- hashCode

    @Test
    void hashCode_equalItems_sameHash() {
        HstLink link = new MockHstLink("path");
        BreadcrumbItem a = new BreadcrumbItem(link, "title");
        BreadcrumbItem b = new BreadcrumbItem(link, "title");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullLinkAndTitle_doesNotThrow() {
        BreadcrumbItem item = new BreadcrumbItem(null, null);
        assertEquals(0, item.hashCode());
    }

    @Test
    void hashCode_differentPaths_differentHashes() {
        BreadcrumbItem a = new BreadcrumbItem(new MockHstLink("path1"), "same");
        BreadcrumbItem b = new BreadcrumbItem(new MockHstLink("path2"), "same");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    // ----------------------------------------------------------------- getters / toString

    @Test
    void getTitle_returnsConstructorValue() {
        BreadcrumbItem item = new BreadcrumbItem(new MockHstLink("p"), "myTitle");
        assertEquals("myTitle", item.getTitle());
    }

    @Test
    void getLink_returnsConstructorValue() {
        MockHstLink link = new MockHstLink("myPath");
        BreadcrumbItem item = new BreadcrumbItem(link, "t");
        assertSame(link, item.getLink());
    }

    @Test
    void toString_nullLink_doesNotThrow() {
        BreadcrumbItem item = new BreadcrumbItem(null, "t");
        assertTrue(item.toString().contains("null"));
    }

    @Test
    void toString_withLink_containsPath() {
        BreadcrumbItem item = new BreadcrumbItem(new MockHstLink("myPath"), "t");
        assertTrue(item.toString().contains("myPath"));
    }
}
