package com.thinkdiffai.futurelove.service.api;

/*
* Usage: Listen any local device ip address when user comment
* */

public interface QueryValueCallback {
    void onQueryValueReceived(String queryValue);
    void onApiCallFailed(Throwable t);
}

