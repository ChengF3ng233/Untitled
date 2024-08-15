package net.optifine.http;

import lombok.Getter;

import java.util.Map;

public class FileUploadThread extends Thread {
    @Getter
    private final String urlString;
    private final Map headers;
    @Getter
    private final byte[] content;
    @Getter
    private final IFileUploadListener listener;

    public FileUploadThread(String urlString, Map headers, byte[] content, IFileUploadListener listener) {
        this.urlString = urlString;
        this.headers = headers;
        this.content = content;
        this.listener = listener;
    }

    public void run() {
        try {
            HttpUtils.post(this.urlString, this.headers, this.content);
            this.listener.fileUploadFinished(this.urlString, this.content, null);
        } catch (Exception exception) {
            this.listener.fileUploadFinished(this.urlString, this.content, exception);
        }
    }

}
