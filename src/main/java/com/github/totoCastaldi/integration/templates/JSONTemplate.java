package com.github.totoCastaldi.integration.templates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by toto on 26/02/16.
 */
@RequiredArgsConstructor
@Slf4j
public class JSONTemplate {

    private final InputStream sourceInput;
    private final Map<String, String> variables;

    public InputStream asInputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(sourceInput, baos);
        String content = baos.toString();

        for (Map.Entry<String, String> variable : variables.entrySet()) {
            content = StringUtils.replace(content, "${" + variable.getKey() + "}", variable.getValue());
        }

        log.debug("template result {}", content);

        return new ByteArrayInputStream(content.getBytes());
    }
}
