package org.b3log.symphony.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Role;
import org.b3log.symphony.model.Tag;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.repository.*;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

@Service
public class AddTestDataService  {



    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AddTestDataService.class);

    /**
     * Default language.
     */
    private static final String DEFAULT_LANG = "en_US";
    /**
     * Tag repository.
     */
    @Inject
    private TagRepository tagRepository;

    @Inject
    private TagTimeRepository tagTimeRepository;

    /**
     * Tag management service.
     */
    @Inject
    private TagMgmtService tagMgmtService;

    /**
     * Article management service.
     */
    @Inject
    private ArticleMgmtService articleMgmtService;

    /**
     * User management service.
     */
    @Inject
    private UserMgmtService userMgmtService;

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;


    public void addData() throws RepositoryException, ServiceException {

        // Init admin
        final JSONObject admin = new JSONObject();
        admin.put(User.USER_EMAIL, "admin" + UserExt.USER_BUILTIN_EMAIL_SUFFIX);
        admin.put(User.USER_NAME, "admin");
        admin.put(User.USER_PASSWORD, DigestUtils.md5Hex("admin"));
        admin.put(UserExt.USER_LANGUAGE, DEFAULT_LANG);
        admin.put(User.USER_ROLE, Role.ROLE_ID_C_ADMIN);
        admin.put(UserExt.USER_STATUS, UserExt.USER_STATUS_C_VALID);
        admin.put(UserExt.USER_GUIDE_STEP, UserExt.USER_GUIDE_STEP_FIN);
        admin.put(UserExt.USER_AVATAR_URL, AvatarQueryService.DEFAULT_AVATAR_URL);
        final String adminId = userMgmtService.addUser(admin);
        admin.put(Keys.OBJECT_ID, adminId);

        // Init community bot
        final JSONObject comBot = new JSONObject();
        comBot.put(User.USER_EMAIL, UserExt.COM_BOT_EMAIL);
        comBot.put(User.USER_NAME, UserExt.COM_BOT_NAME);
        comBot.put(User.USER_PASSWORD, DigestUtils.md5Hex(String.valueOf(new Random().nextInt())));
        comBot.put(UserExt.USER_LANGUAGE, "en_US");
        comBot.put(UserExt.USER_GUIDE_STEP, UserExt.USER_GUIDE_STEP_FIN);
        comBot.put(User.USER_ROLE, Role.ROLE_ID_C_DEFAULT);
        comBot.put(UserExt.USER_STATUS, UserExt.USER_STATUS_C_VALID);
        userMgmtService.addUser(comBot);

        // Init new user
        final JSONObject user = new JSONObject();
        user.put(User.USER_EMAIL, "user1"+UserExt.USER_BUILTIN_EMAIL_SUFFIX);
        user.put(User.USER_NAME, "user1");
        user.put(User.USER_PASSWORD, DigestUtils.md5Hex(String.valueOf(123456)));
        user.put(UserExt.USER_LANGUAGE, "en_US");
        user.put(UserExt.USER_GUIDE_STEP, UserExt.USER_GUIDE_STEP_FIN);
        user.put(User.USER_ROLE, Role.ROLE_ID_C_REGULAR);
        user.put(UserExt.USER_STATUS, UserExt.USER_STATUS_C_VALID);
        final String userId = userMgmtService.addUser(user);

        LOGGER.info("Initialized admin user");

        // Add tags
        String tagTitle = Symphonys.get("systemAnnounce");
        String tagId = tagMgmtService.addTag(adminId, tagTitle);
        JSONObject tag = tagRepository.get(tagId);
        tag.put(Tag.TAG_URI, "announcement");
        tagMgmtService.updateTag(tagId, tag);

        tagTitle = "B3log";
        tagId = tagMgmtService.addTag(adminId, tagTitle);
        tag = tagRepository.get(tagId);
        tag.put(Tag.TAG_URI, "B3log");
        tag.put(Tag.TAG_ICON_PATH, "b3log.png");
        tag.put(Tag.TAG_DESCRIPTION, "[B3log](https://b3log.org) 是一个开源组织，名字来源于“Bulletin Board Blog”缩写，目标是将独立博客与论坛结合，形成一种新的网络社区体验，详细请看 [B3log 构思](https://hacpai.com/b3log)。目前 B3log 已经开源了多款产品： [Solo] 、 [Sym] 、 [Wide] 。");
        tagMgmtService.updateTag(tagId, tag);

        tagTitle = "Sym";
        tagId = tagMgmtService.addTag(adminId, tagTitle);
        tag = tagRepository.get(tagId);
        tag.put(Tag.TAG_URI, "Sym");
        tag.put(Tag.TAG_ICON_PATH, "sym.png");
        tag.put(Tag.TAG_DESCRIPTION, "[Sym](https://github.com/b3log/symphony) 是一个用 [Java] 实现的现代化社区（论坛/社交网络/博客）平台，“下一代的社区系统，为未来而构建”。");
        tagMgmtService.updateTag(tagId, tag);

        tagTitle = "Solo";
        tagId = tagMgmtService.addTag(adminId, tagTitle);
        tag = tagRepository.get(tagId);
        tag.put(Tag.TAG_URI, "Solo");
        tag.put(Tag.TAG_ICON_PATH, "solo.png");
        tag.put(Tag.TAG_DESCRIPTION, "[Solo](https://github.com/b3log/solo) 是目前 GitHub 上关注度最高的 Java 开源博客系统。\n" +
                "\n" +
                "* [项目地址](https://github.com/b3log/solo)\n" +
                "* [用户指南](https://hacpai.com/article/1492881378588)");
        tagMgmtService.updateTag(tagId, tag);

        tagTitle = "Pipe";
        tagId = tagMgmtService.addTag(adminId, tagTitle);
        tag = tagRepository.get(tagId);
        tag.put(Tag.TAG_URI, "Pipe");
        tag.put(Tag.TAG_ICON_PATH, "pipe.png");
        tag.put(Tag.TAG_DESCRIPTION, "[Pipe](https://github.com/b3log/pipe) 是一款小而美的开源博客平台，通过 [黑客派] 账号登录即可使用。如果你不想自己搭建，可以直接使用我们运维的 http://pipe.b3log.org");
        tagMgmtService.updateTag(tagId, tag);

        tagTitle = "Wide";
        tagId = tagMgmtService.addTag(adminId, tagTitle);
        tag = tagRepository.get(tagId);
        tag.put(Tag.TAG_URI, "Wide");
        tag.put(Tag.TAG_ICON_PATH, "wide.png");
        tag.put(Tag.TAG_DESCRIPTION, "[Wide](https://github.com/b3log/wide) 是一个基于 [Web] 的 <a href=\"/tags/golang\">Go</a> 语言团队 IDE。通过浏览器就可以进行 Go 开发，并有代码自动完成、查看表达式、编译反馈、Lint、实时结果输出等功能。");
        tagMgmtService.updateTag(tagId, tag);



        tagTitle = "check";
        tagId = tagMgmtService.addTag(userId, tagTitle);
        tag = tagRepository.get(tagId);
        tag.put(Tag.TAG_URI, "check");
        tag.put(Tag.TAG_ICON_PATH, "wide.png");
        tag.put(Tag.TAG_DESCRIPTION, "[check] is a test tag");
        tagMgmtService.updateTag(tagId, tag);

        // Hello World!
        final JSONObject article = new JSONObject();
        article.put(Article.ARTICLE_TITLE, "Welcome to Sym community :gift_heart:");
        article.put(Article.ARTICLE_TAGS, "Sym,Announcement");
        article.put(Article.ARTICLE_CONTENT, "Hello, everyone!");
        article.put(Article.ARTICLE_EDITOR_TYPE, 0);
        article.put(Article.ARTICLE_AUTHOR_ID, admin.optString(Keys.OBJECT_ID));

        // test article
        final JSONObject articleTest = new JSONObject();
        articleTest.put(Article.ARTICLE_TITLE, "Welcome to test article :gift_heart:");
        articleTest.put(Article.ARTICLE_TAGS, "Sym,Announcement,check");
        articleTest.put(Article.ARTICLE_CONTENT, "Hello, everyone!");
        articleTest.put(Article.ARTICLE_EDITOR_TYPE, 0);
        articleTest.put(Article.ARTICLE_AUTHOR_ID, admin.optString(Keys.OBJECT_ID));        // test article

        final JSONObject articleTest1 = new JSONObject();
        articleTest1.put(Article.ARTICLE_TITLE, "Welcome to test article2 :gift_heart:");
        articleTest1.put(Article.ARTICLE_TAGS, "Sym,Announcement,check");
        articleTest1.put(Article.ARTICLE_CONTENT, "Hello, everyone!");
        articleTest1.put(Article.ARTICLE_EDITOR_TYPE, 0);
        articleTest1.put(Article.ARTICLE_AUTHOR_ID, admin.optString(Keys.OBJECT_ID));

        articleMgmtService.addArticle(articleTest);
        articleMgmtService.addArticle(articleTest1);
        articleMgmtService.addArticle(article);

    }


    public String addUser(String userName) throws RepositoryException, ServiceException {

        // Init new user
        final JSONObject user = new JSONObject();
        user.put(User.USER_EMAIL, userName+UserExt.USER_BUILTIN_EMAIL_SUFFIX);
        user.put(User.USER_NAME, userName);
        user.put(User.USER_PASSWORD, DigestUtils.md5Hex(String.valueOf(123456)));
        user.put(UserExt.USER_LANGUAGE, "en_US");
        user.put(UserExt.USER_GUIDE_STEP, UserExt.USER_GUIDE_STEP_FIN);
        user.put(User.USER_ROLE, Role.ROLE_ID_C_REGULAR);
        user.put(UserExt.USER_STATUS, UserExt.USER_STATUS_C_VALID);
        final String userId = userMgmtService.addUser(user);

        return userId;
    }

    public void addTag(String tagName, String userId) throws RepositoryException, ServiceException {

        if (null == tagRepository.getByTitle(tagName)){

            String tagTitle = tagName;
            String tagId = tagMgmtService.addTag(userId, tagTitle);
            JSONObject tag = tagRepository.get(tagId);
            tag.put(Tag.TAG_URI, "B3log");
            tag.put(Tag.TAG_ICON_PATH, "b3log.png");
            tag.put(Tag.TAG_DESCRIPTION, tagName+" is a new tag"
            );

            tagMgmtService.updateTag(tagId, tag);
        }
    }

    public void addArticle(JSONObject article, String userId) throws  RepositoryException, ServiceException{

        article.put(Article.ARTICLE_EDITOR_TYPE, 0);
        article.put(Article.ARTICLE_AUTHOR_ID, userId);

        articleMgmtService.addArticle(article);
    }
}
