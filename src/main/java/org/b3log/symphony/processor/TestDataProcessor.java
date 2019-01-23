package org.b3log.symphony.processor;


import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.AbstractFreeMarkerRenderer;
import org.b3log.latke.util.Requests;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.processor.advice.PermissionGrant;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.processor.advice.validate.UserRegisterValidation;
import org.b3log.symphony.service.AddTestDataService;
import org.b3log.symphony.service.DataModelService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequestProcessor
public class TestDataProcessor {

    /**
     * Data model service.
     */
    @Inject
    private DataModelService dataModelService;

    @Inject
    private AddTestDataService addTestDataService;

    @RequestProcessing(value = "/testdata", method = HTTPRequestMethod.GET)
    @Before(adviceClass = StopwatchStartAdvice.class)
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void showTestDataForm(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        context.setRenderer(renderer);

        String referer = request.getParameter(Common.GOTO);
        if (StringUtils.isBlank(referer)) {
            referer = request.getHeader("referer");
        }

        if (!StringUtils.startsWith(referer, Latkes.getServePath())) {
            referer = Latkes.getServePath();
        }

        renderer.setTemplateName("verify/testdata.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModel.put(Common.GOTO, referer);

        dataModelService.fillHeaderAndFooter(request, response, dataModel);


    }

    /**
     * Handles test data pre-review receive
     * @param context
     * @param request
     * @throws Exception
     */
    @RequestProcessing(value = "/testdataUpload", method = HTTPRequestMethod.POST)
    @Before(adviceClass = StopwatchStartAdvice.class)
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void testData(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
        throws Exception{
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);

        String temp = request.getParameter("reqJson");


        final JSONObject requestJSONObject = new JSONObject(temp);
        final String articleTitle = requestJSONObject.optString("articleTitle");
        final String articleContent = requestJSONObject.optString("articleContent");
        final String tag = requestJSONObject.optString("tag");
        final String author = requestJSONObject.optString("author");
        final int amount = requestJSONObject.optInt("amount");

        context.setRenderer(renderer);
        renderer.setTemplateName("verify/testdata-preview.ftl");


        String referer = request.getParameter(Common.GOTO);
        if (StringUtils.isBlank(referer)) {
            referer = request.getHeader("referer");
        }

        if (!StringUtils.startsWith(referer, Latkes.getServePath())) {
            referer = Latkes.getServePath();
        }


        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModel.put(Common.GOTO, referer);

        dataModelService.fillHeaderAndFooter(request, response, dataModel);

        List<JSONObject> articles = new ArrayList<>();

        for (int i=0;i< amount;i++){
            JSONObject article = new JSONObject();
            article.put("articleTitle", articleTitle+i);
            article.put("articleContent", articleContent);
            article.put("tag", tag);
            article.put("author", author+i);
            article.put("amount", amount);
            articles.add(article);
        }

        dataModel.put("articles", articles);

    }


    @RequestProcessing(value = "/submitdata", method = HTTPRequestMethod.POST)
    @Before(adviceClass = StopwatchStartAdvice.class)
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void register(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response) throws ServiceException, RepositoryException {

        context.renderJSON();

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        final JSONArray articles = requestJSONObject.getJSONArray("articles");

        for(Object article : articles){

            JSONObject artJSON = (JSONObject) article;

            String articleTitle = artJSON.optString("articleTitle");
            String articleContent = artJSON.optString("articleContent");
            String tags = artJSON.optString("tag");
            String userName = artJSON.optString("author");

            String userId = addTestDataService.addUser(userName);

            addTestDataService.addTag(tags, userId);

            JSONObject newArticle = new JSONObject();
            newArticle.put(Article.ARTICLE_TITLE, articleTitle);
            newArticle.put(Article.ARTICLE_CONTENT, articleContent);
            newArticle.put(Article.ARTICLE_TAGS, tags);

            addTestDataService.addArticle(newArticle, userId);


        }
        context.renderTrueResult();
    }


}
