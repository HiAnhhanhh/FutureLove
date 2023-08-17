package com.thinkdiffai.futurelove.presenter.api;

import android.content.Context;

import com.thinkdiffai.futurelove.model.event.UploadingEvent;

public interface ApiEventInteracting {
    void generateImplicitEvent(Context context, UploadingEvent myEvent);
}
