package net.optifine.http;

import lombok.Getter;
import lombok.Setter;

@Getter
public class HttpPipelineRequest {
    private HttpRequest httpRequest = null;
    private HttpListener httpListener = null;
    @Setter
    private boolean closed = false;

    public HttpPipelineRequest(HttpRequest httpRequest, HttpListener httpListener) {
        this.httpRequest = httpRequest;
        this.httpListener = httpListener;
    }

}
