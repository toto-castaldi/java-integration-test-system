package com.github.totoCastaldi.integration.net;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by toto on 05/02/16.
 */
@NoArgsConstructor (access = AccessLevel.PRIVATE)
@Slf4j
public class HttpResource {

    private final static String USER_AGENT = "Mozilla/5.0";

    @Getter
    private String responseBody = StringUtils.EMPTY;
    @Getter
    private int responseCode = 0;
    private Map<String, List<String>> responseHeaders = Maps.newHashMap();
    private String url;
    private Map<String, String> requestHeaders;
    private String requestBody;
    private Method method;

    public void recall() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        if (StringUtils.isNotBlank(requestBody)) {
            con.setDoOutput(true);
        } else {
            con.setDoOutput(false);
        }
        con.setDoInput(true);
        con.setRequestMethod(method.name());

        if (requestHeaders != null) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }


        if (StringUtils.isNotBlank(requestBody)) {
            //con.setChunkedStreamingMode(5000);
            //con.setFixedLengthStreamingMode(requestBody.getBytes().length);

            OutputStream outputStream = con.getOutputStream();

            IOUtils.copy(new ByteArrayInputStream(requestBody.getBytes()), outputStream);

            outputStream.flush();
            outputStream.close();
        }

        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            if (StringUtils.contains(e.getMessage(), "server returned HTTP response code: 500")) {
                responseCode = 500;
            } else if (!(e instanceof FileNotFoundException)) {
                log.warn(StringUtils.EMPTY, e);
            }
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            responseBody = response.toString();
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                log.warn(StringUtils.EMPTY, e);
            }
        }

        responseHeaders = con.getHeaderFields();
    }

    public static HttpResource post(String url, Map<String, String> headers, InputStream bodyContent) throws IOException {
        HttpResource result = bodyRequest(url, headers, bodyContent);
        result.method = Method.POST;
        result.recall();
        return result;
    }

    public static HttpResource put(String url, Map<String, String> headers, InputStream bodyContent) throws IOException {
        HttpResource result = bodyRequest(url, headers, bodyContent);
        result.method = Method.PUT;
        result.recall();
        return result;
    }

    public static HttpResource get(String url) throws IOException {
        return get(url, null);
    }

    public static HttpResource get(String url, Map<String, String> headers) throws IOException {
        if (headers == null) {
            headers = Collections.emptyMap();
        }
        HttpResource result = bodyRequest(url, headers, null);
        result.method = Method.GET;
        result.recall();
        return result;
    }

    public static HttpResource delete(String url, Map<String, String> headers) throws IOException {
        HttpResource result = bodyRequest(url, headers, null);
        result.method = Method.DELETE;
        result.recall();
        return result;
    }

    public Iterable<String> getResponseHeaders(String headerName) {
        List<String> strings = responseHeaders.get(headerName);
        if (strings == null) {
            return Lists.newArrayList();
        } else {
            return strings;
        }
    }

    public JsonElement getJsonResponse() {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(responseBody);
    }

    private static HttpResource bodyRequest(String url, Map<String, String> headers, InputStream bodyContent) throws IOException {
        HttpResource result = new HttpResource();
        result.url = url;
        result.requestHeaders = headers;
        if (bodyContent != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(bodyContent, baos);
            result.requestBody = baos.toString();
        } else {
            result.requestBody = StringUtils.EMPTY;
        }
        return result;
    }

}
