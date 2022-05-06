<#include "../include/imports.ftl">

<#-- @ftlvariable name="breadcrumb" type="org.onehippo.forge.breadcrumb.om.Breadcrumb" -->
<#if breadcrumb?? && breadcrumb.items??>
    <#list breadcrumb.items as item>
        <#if item.link.notFound && (breadcrumb.linkNotFoundMode == 'hide' || breadcrumb.linkNotFoundMode == 'unlink')>
            <#if breadcrumb.linkNotFoundMode == 'unlink'>
                <a>${item.title?html}</a><#sep> ${breadcrumb.separator}&nbsp;
            </#if>
        <#else>
            <@hst.link var="link" link=item.link/>
            <a <#sep>href="${link}"</#sep> style="cursor: pointer">${item.title?html}</a><#sep> ${breadcrumb.separator}&nbsp;
        </#if>
    </#list>
</#if>