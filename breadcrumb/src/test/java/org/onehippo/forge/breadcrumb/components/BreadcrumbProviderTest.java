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

import java.util.List;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the pure-logic methods of {@link BreadcrumbProvider} that do not
 * require a live HST request context.
 */
@ExtendWith(MockitoExtension.class)
public class BreadcrumbProviderTest {

    @Mock
    private BaseHstComponent component;

    @Mock
    private BreadcrumbParametersInfo info;

    // ------------------------------------------------------------------ getSitemenuNames

    @Test
    void getSitemenuNames_nullMenus_returnsDefaultMainMenu() {
        when(info.getBreadcrumbMenus()).thenReturn(null);
        when(info.getSeparator()).thenReturn(BreadcrumbProvider.DEFAULT_SEPARATOR);
        BreadcrumbProvider provider = new BreadcrumbProvider(component, info);

        List<String> names = provider.getSitemenuNames();

        assertEquals(1, names.size());
        assertEquals(BreadcrumbProvider.DEFAULT_MENU_NAME, names.get(0));
    }

    @Test
    void getSitemenuNames_singleMenu_returnsSingleEntry() {
        when(info.getBreadcrumbMenus()).thenReturn("top");
        when(info.getSeparator()).thenReturn(BreadcrumbProvider.DEFAULT_SEPARATOR);
        BreadcrumbProvider provider = new BreadcrumbProvider(component, info);

        List<String> names = provider.getSitemenuNames();

        assertEquals(1, names.size());
        assertEquals("top", names.get(0));
    }

    @Test
    void getSitemenuNames_commaDelimitedMenus_returnsAllTrimmed() {
        when(info.getBreadcrumbMenus()).thenReturn("main, secondary , top");
        when(info.getSeparator()).thenReturn(BreadcrumbProvider.DEFAULT_SEPARATOR);
        BreadcrumbProvider provider = new BreadcrumbProvider(component, info);

        List<String> names = provider.getSitemenuNames();

        assertEquals(3, names.size());
        assertEquals("main", names.get(0));
        assertEquals("secondary", names.get(1));
        assertEquals("top", names.get(2));
    }

    // ------------------------------------------------------------------ getSeparator

    @Test
    void getSeparator_nonEmptySeparator_returnsConfigured() {
        when(info.getBreadcrumbMenus()).thenReturn(null);
        when(info.getSeparator()).thenReturn(" > ");
        BreadcrumbProvider provider = new BreadcrumbProvider(component, info);

        assertEquals(" > ", provider.getSeparator());
    }

    @Test
    void getSeparator_emptySeparator_returnsDefault() {
        when(info.getBreadcrumbMenus()).thenReturn(null);
        when(info.getSeparator()).thenReturn("");
        BreadcrumbProvider provider = new BreadcrumbProvider(component, info);

        assertEquals(BreadcrumbProvider.DEFAULT_SEPARATOR, provider.getSeparator());
    }

    @Test
    void getSeparator_defaultSeparator_returnsHtmlEntity() {
        when(info.getBreadcrumbMenus()).thenReturn(null);
        when(info.getSeparator()).thenReturn(BreadcrumbProvider.DEFAULT_SEPARATOR);
        BreadcrumbProvider provider = new BreadcrumbProvider(component, info);

        assertEquals("&#187;", provider.getSeparator());
    }

    // ------------------------------------------------------------------ constants

    @Test
    void constants_haveExpectedValues() {
        assertEquals("breadcrumb", BreadcrumbProvider.ATTRIBUTE_NAME);
        assertEquals("breadcrumb-menus", BreadcrumbProvider.PARAMETER_MENUS);
        assertEquals("breadcrumb-separator", BreadcrumbProvider.PARAMETER_SEPARATOR);
        assertEquals("main", BreadcrumbProvider.DEFAULT_MENU_NAME);
        assertEquals("pagenotfound", BreadcrumbProvider.HST_PAGES_PAGENOTFOUND_ID);
    }
}
