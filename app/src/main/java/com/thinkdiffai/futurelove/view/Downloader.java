package com.thinkdiffai.futurelove.view;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingBinding;

import java.io.DataInput;

public class Downloader {
    public static void downloadVideo(Context context,String urlVideo,String fileName){
        Uri urlDownload = Uri.parse(urlVideo);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(urlDownload);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        downloadManager.enqueue(request);
    }
}
