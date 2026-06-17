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
package org.onehippo.forge.breadcrumb.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the pure-logic {@link BreadcrumbProvider.LinkNotFoundMode#safeValueOf(String)} factory.
 */
public class LinkNotFoundModeTest {

    @Test
    void safeValueOf_hide_returnsHide() {
        assertEquals(BreadcrumbProvider.LinkNotFoundMode.HIDE,
                BreadcrumbProvider.LinkNotFoundMode.safeValueOf("hide"));
    }

    @Test
    void safeValueOf_HIDE_upperCase_returnsHide() {
        assertEquals(BreadcrumbProvider.LinkNotFoundMode.HIDE,
                BreadcrumbProvider.LinkNotFoundMode.safeValueOf("HIDE"));
    }

    @Test
    void safeValueOf_unlink_returnsUnlink() {
        assertEquals(BreadcrumbProvider.LinkNotFoundMode.UNLINK,
                BreadcrumbProvider.LinkNotFoundMode.safeValueOf("unlink"));
    }

    @Test
    void safeValueOf_UNLINK_upperCase_returnsUnlink() {
        assertEquals(BreadcrumbProvider.LinkNotFoundMode.UNLINK,
                BreadcrumbProvider.LinkNotFoundMode.safeValueOf("UNLINK"));
    }

    @Test
    void safeValueOf_null_returnsNull() {
        assertNull(BreadcrumbProvider.LinkNotFoundMode.safeValueOf(null));
    }

    @Test
    void safeValueOf_emptyString_returnsNull() {
        assertNull(BreadcrumbProvider.LinkNotFoundMode.safeValueOf(""));
    }

    @Test
    void safeValueOf_unknownValue_returnsNull() {
        assertNull(BreadcrumbProvider.LinkNotFoundMode.safeValueOf("bogus"));
    }

    @Test
    void safeValueOf_mixedCase_returnsCorrectMode() {
        assertEquals(BreadcrumbProvider.LinkNotFoundMode.HIDE,
                BreadcrumbProvider.LinkNotFoundMode.safeValueOf("HiDe"));
    }
}
