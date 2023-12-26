package com.thinkdiffai.futurelove.util;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

public class FileDownloader {
    private final OkHttpClient client = new OkHttpClient();

    public File downloadFile(String url, String destinationPath) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    File destinationFile = new File(destinationPath);

                    // Use Okio to write the response body to the destination file
                    try (BufferedSink sink = Okio.buffer(Okio.sink(destinationFile))) {
                        sink.writeAll(body.source());
                    }

                    return destinationFile;
                }
            }
        }

        return null;
    }
}
