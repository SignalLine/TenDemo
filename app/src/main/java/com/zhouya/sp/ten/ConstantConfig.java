package com.zhouya.sp.ten;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/6/1
 */
public interface ConstantConfig {
    //最外层界面的链接地址
    String MOVIE_URL = "http://api.shigeten.net/api/Critic/GetCriticList";
    String ARTICLE_URL = "http://api.shigeten.net/api/Novel/GetNovelList";
    String PICTURE_URL = "http://api.shigeten.net/api/Diagram/GetDiagramList";

    //详情地址
    String MOVIE_DETATLS_URL = "http://api.shigeten.net/api/Critic/GetCriticContent?id=";
    String ARTICLE_DETATLS_URL = "http://api.shigeten.net/api/Novel/GetNovelContent?id=";
    String PICTURE_DETATLS_URL = "http://api.shigeten.net/api/Diagram/GetDiagramContent?id=";

    //加载图片地址
    String PICTURE_DISPLAY_URL = "http://api.shigeten.net/";

    //favorite收藏数据库
    String MY_FAVORITE = "MyFavorite.db";
    //表明
    String FAVORITES = "Favorites";
}
