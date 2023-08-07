package com.thinkdiffai.futurelove.service.api;

public interface Server {
    String  DOMAIN1 = "http://14.225.7.221:9090/";
    String  DOMAIN2 = "http://14.225.7.221:8989/";
    String  DOMAIN3 = "http://127.0.0.1:8888/";

    String GET_NETWORK_API_DOMAIN = "http://ip-api.com/";

    String URI_PAIRING = "getdata";

    String URI_GET_NETWORK_STATUS = "json/";

    String URI_LIST_EVENT_HOME = "lovehistory/page/";
    String URI_LIST_EVENT_TIMELINE = "lovehistory/";
    String URI_POST_EVENT_TIMELINE = "lovehistory/add";
    String KEY_HEADER1 = "Link_img1";
    String KEY_HEADER2 = "Link_img2";

    String URI_LIST_COMMENT_BY_EVENT_ID = "lovehistory/comment/";
    String URI_LIST_COMMENT_NEW = "lovehistory/pageComment/";
    String URI_POST_COMMENT = "lovehistory/comment";

    String URI_LINK_WEB_DETAIL = "http://datanomic.online/sukien/";
}
