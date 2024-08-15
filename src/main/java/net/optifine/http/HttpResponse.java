package net.optifine.http;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    @Getter
    private int status = 0;
    @Getter
    private String statusLine = null;
    private Map<String, String> headers = new LinkedHashMap();
    @Getter
    private byte[] body = null;

    public HttpResponse(int status, String statusLine, Map headers, byte[] body) {
        this.status = status;
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public Map getHeaders() {
        return this.headers;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

}
