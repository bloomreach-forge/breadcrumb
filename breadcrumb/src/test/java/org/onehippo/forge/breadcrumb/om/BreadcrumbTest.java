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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hippoecm.hst.mock.core.linking.MockHstLink;
import org.onehippo.forge.breadcrumb.components.BreadcrumbProvider.LinkNotFoundMode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BreadcrumbTest {

    @Test
    void getItems_returnsItemsPassedToConstructor() {
        List<BreadcrumbItem> items = Arrays.asList(
                new BreadcrumbItem(new MockHstLink("p1"), "Home"),
                new BreadcrumbItem(new MockHstLink("p2"), "News")
        );
        Breadcrumb crumb = new Breadcrumb(items, ">", LinkNotFoundMode.HIDE);
        assertSame(items, crumb.getItems());
        assertEquals(2, crumb.getItems().size());
    }

    @Test
    void getSeparator_returnsConfiguredSeparator() {
        Breadcrumb crumb = new Breadcrumb(Collections.emptyList(), " | ", null);
        assertEquals(" | ", crumb.getSeparator());
    }

    @Test
    void getLinkNotFoundMode_hideMode_returnsHideLowerCase() {
        Breadcrumb crumb = new Breadcrumb(Collections.emptyList(), ">", LinkNotFoundMode.HIDE);
        assertEquals("hide", crumb.getLinkNotFoundMode());
    }

    @Test
    void getLinkNotFoundMode_unlinkMode_returnsUnlinkLowerCase() {
        Breadcrumb crumb = new Breadcrumb(Collections.emptyList(), ">", LinkNotFoundMode.UNLINK);
        assertEquals("unlink", crumb.getLinkNotFoundMode());
    }

    @Test
    void getLinkNotFoundMode_nullMode_returnsNull() {
        Breadcrumb crumb = new Breadcrumb(Collections.emptyList(), ">", null);
        assertNull(crumb.getLinkNotFoundMode());
    }

    @Test
    void toString_doesNotThrow() {
        Breadcrumb crumb = new Breadcrumb(Collections.emptyList(), ">", LinkNotFoundMode.HIDE);
        String s = crumb.toString();
        assertTrue(s.contains("separator"));
        assertTrue(s.contains("hide"));
    }
}
