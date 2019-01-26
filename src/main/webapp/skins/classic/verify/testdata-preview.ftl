<#--

    Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
    Copyright (C) 2012-2018, b3log.org & hacpai.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

-->
<#include "../macro-head.ftl">
<!DOCTYPE html>
<html>
<head>
<@head title="Test Data Preview- ${symphonyLabel}">
    <meta name="description" content="${registerLabel} ${symphonyLabel}"/>
</@head>
    <link rel="stylesheet" href="${staticServePath}/css/index.css?${staticResourceVersion}" />
    <link rel="canonical" href="${servePath}/register">
</head>
<body>
<#include "../header.ftl">
<div class="main">
            <#list articles as article>
            <div class="form wrapper">
                <input id="amountId${article_index}" type="hidden" value="${article.amount}">
                <div class="module-header">
                    <h2>Test Data article</h2>
                </div>
                <div class="input-wrap">
                    <input id="articleTitle${article_index}" placeholder="" autocomplete="off" value="${article.articleTitle}"/>
                </div>
                <div class="module-header">
                    <h2>Test Data Content</h2>
                </div>
                <div class="input-wrap">
                    <input id="articleContent${article_index}" type="text"  placeholder="" value="${article.articleContent}"/>
                </div>
                <div class="module-header">
                    <h2>Test Data Tag</h2>
                </div>
                <div class="input-wrap">
                    <input id="tag${article_index}" type="text"  placeholder="" value="${article.tag}"/>
                </div>
                <div class="module-header">
                    <h2>Test Data Author</h2>
                </div>
                <div class="input-wrap">
                    <input id="author${article_index}" type="text"  placeholder="" value="${article.author}" />
                </div>
            </div>
            </#list>

                <div class="wrapper" style=" display: flex; align-items: center; justify-content: center;">
                    <button class="green center" onclick="Verify.testdatasubmit()">Go!</button>
                </div>

    </div>
</div>
<#include "../footer.ftl">
<script src="${staticServePath}/js/verify${miniPostfix}.js?${staticResourceVersion}"></script>
</body>
</html>
