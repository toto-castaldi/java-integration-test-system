package com.github.totoCastaldi.integration.net;


import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

/**
 * Created by toto on 16/02/16.
 */
@RequiredArgsConstructor
public class Rest {

    private final String host;
    private final int port;

    private Multimap<String, HeaderContent> headers = LinkedListMultimap.create();
    private String ctx = StringUtils.EMPTY;

    public Rest addHeader(Header contentType, final String content) {
        headers.put(contentType.getHttpKey(), new HeaderContent() {
            @Override
            public String generate(Rest rest) {
                return content;
            }
        });
        return this;
    }

    public Rest addHeader(String headerName, final String content) {
        headers.put(headerName, new HeaderContent() {
            @Override
            public String generate(Rest rest) {
                return content;
            }
        });
        return this;
    }

    public Rest addHeader(Header contentType, HeaderContent headerContent) {
        headers.put(contentType.getHttpKey(), headerContent);
        return this;
    }

    public HttpResource post(String path, InputStream inputStream) throws IOException {
        return HttpResource.post("http://" + this.host + ":" + this.port + this.ctx + path, convertHeaders(), inputStream);
    }

    public HttpResource put(String path, InputStream inputStream) throws IOException {
        return HttpResource.put("http://" + this.host + ":" + this.port + this.ctx + path, convertHeaders(), inputStream);
    }

    public HttpResource delete(String path) throws IOException {
        return HttpResource.delete("http://" + this.host + ":" + this.port + this.ctx + path, convertHeaders());
    }

    public HttpResource get(String path) throws IOException {
        return HttpResource.get("http://" + this.host + ":" + this.port + this.ctx + path, convertHeaders());
    }

    private Map<String, String> convertHeaders() {
        Map<String, String> result = Maps.newHashMap();

        Set<String> headerKeys = this.headers.keySet();
        Joiner joiner = Joiner.on(";").skipNulls();
        for (String header : headerKeys) {
            final Rest rest = this;
            String join = joiner.join(Collections2.transform(headers.get(header), new Function<HeaderContent, String>() {
                @Override
                public String apply(HeaderContent input) {
                    return input.generate(rest);
                }
            }));
            result.put(header, join);
        }

        return result;
    }

    public Rest setContext(String ctx) {
        this.ctx = ctx;
        return this;
    }


    @RequiredArgsConstructor
    public static class AuthorizationBasic implements HeaderContent {
        private final String username;
        private final String password;


        @Override
        public String generate(Rest rest) {
            String authString = username + ":" + password;
            byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
            String authStringEnc = new String(authEncBytes);
            return "Basic " + authStringEnc;
        }
    }

    public interface HeaderContent {

        public String generate(Rest rest);

    }
}
