package com.thinkdiffai.futurelove.service.api;

public interface Server {
//    String  DOMAIN1 = "http://14.225.7.221:9090/";
//    String  DOMAIN2 = "http://14.225.7.221:8989/";
//
//
//    String  DOMAIN3 = "http://127.0.0.1:8888/";
    String DOMAIN1 = "https://metatechvn.store/";
    String DOMAIN0 = "http://192.168.50.146:9090/";
    String DOMAIN2 = "https://metatechvn.store/";
    String DOMAIN3 ="https://lhvn.online/";

    String DOMAIN4 ="https://thinkdiff.us/";

    String GET_NETWORK_API_DOMAIN = "http://ip-api.com/";
    String GET_CITY_NAME_FROM_IP = "https://ipinfo.io/";

    String GET_VIDEO = "lovehistory/listvideo/";

    String URI_PAIRING = "getdata";

    String UPLOAD_IMAGE = "upload-gensk/";

    String GET_URL_VIDEO_SWAP = "getdata/genvideo";

    String POST_URL_VIDEO = "getdata/genvideo/swap/imagevid";
    String URI_CREATE_IMPLICIT_DATA = "getdata/skngam";
    String URI_GET_NETWORK_STATUS = "json";

    String URI_LIST_EVENT_HOME = "lovehistory/page/";
    String URI_LIST_EVENT_TIMELINE = "lovehistory/";
    String URI_POST_EVENT_TIMELINE = "lovehistory/add";
    String KEY_HEADER1 = "Link_img1";
    String KEY_HEADER2 = "Link_img2";

    String URI_LIST_COMMENT_BY_EVENT_ID = "lovehistory/comment/";
    String URI_LIST_COMMENT_NEW = "lovehistory/pageComment/";
    String URI_POST_COMMENT = "lovehistory/comment";
    String URI_LINK_WEB_DETAIL = "http://datanomic.online/detail/";
//    String URI_LOG_IN = "login";
    String URI_LOG_IN = "login";
//    String URI_SIGN_UP = "register";
    String URI_SIGN_UP = "register/user";

    // URI profile
    String URI_PROFILE_USER="profile/";
    // uri comments cua tung user
    String URI_COMMENTS_USER="lovehistory/comment/user/";
    // uri events cua tung user
    String URI_EVENTS_USER="lovehistory/user/";
    String URI_DELETE_ACCOUNT = "deleteuser/";
    // uri doi passwork
    String URI_CHANGE_PASSWORD="changepassword/";
}
