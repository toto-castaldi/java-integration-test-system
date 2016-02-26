package com.github.totoCastaldi.integration.net;

import lombok.Getter;

/**
 * Created by toto on 16/02/16.
 */
public enum Header {
    ACCEPT("Accept"), AUTHORIZATION("Authorization"), CONTENT_TYPE("Content-Type"), USER_AGENT("User-Agent");

    @Getter
    private final String httpKey;

    private Header(String httpKey) {
        this.httpKey = httpKey;
    }
}
