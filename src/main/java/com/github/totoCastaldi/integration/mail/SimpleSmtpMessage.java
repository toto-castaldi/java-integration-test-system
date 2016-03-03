package com.github.totoCastaldi.integration.mail;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by toto on 03/03/16.
 */
@Data
@RequiredArgsConstructor (staticName = "of")
public class SimpleSmtpMessage {

    private final String body;
}
