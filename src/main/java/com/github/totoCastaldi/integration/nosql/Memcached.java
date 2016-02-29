package com.github.totoCastaldi.integration.nosql;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by toto on 16/02/16.
 */
@RequiredArgsConstructor
public class Memcached {

    private final String host;
    private final int port;

    public void flushAll() throws IOException {
        Socket sock = new Socket(host, port);
        PrintWriter pw = new PrintWriter(sock.getOutputStream());
        pw.println("flush_all");
        pw.flush();
        pw.println("quit");
        pw.flush();

        pw.close();
        sock.close();
    }
}
