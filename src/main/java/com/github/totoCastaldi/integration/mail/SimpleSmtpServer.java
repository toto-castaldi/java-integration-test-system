package com.github.totoCastaldi.integration.mail;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.github.totoCastaldi.integration.net.Header;
import com.github.totoCastaldi.integration.net.HttpResource;
import com.github.totoCastaldi.integration.net.Rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by toto on 03/03/16.
 */
public class SimpleSmtpServer extends Rest {

    public SimpleSmtpServer(String host, int port) {
        super(host, port);
        setContext("/smtp");
        addHeader(Header.CONTENT_TYPE, "application/json");
        addHeader(Header.ACCEPT, "application/json");
    }

    public void clearEmails() throws IOException {
        delete("/emails");
    }

    public Collection<SimpleSmtpMessage> getReceivedEmail() throws IOException {
        List<SimpleSmtpMessage> result = Lists.newArrayList();
        final HttpResource httpResource = get("/emails");
        final JsonElement jsonResponse = httpResource.getJsonResponse();
        final JsonArray asJsonArray = jsonResponse.getAsJsonArray();
        for (JsonElement jsonElement : asJsonArray) {
            JsonObject jsonObject = (JsonObject) jsonElement;
            result.add(SimpleSmtpMessage.of(jsonObject.get("body").getAsString()));
        }
        return result;
    }
}
