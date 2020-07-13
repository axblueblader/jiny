package com.tuhuynh.httpserver.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.tuhuynh.httpserver.core.RequestBinder.HandlerMetadata;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

@RequiredArgsConstructor
public final class RequestPipeline implements Runnable {
    private final Socket socket;
    private final ArrayList<HandlerMetadata> handlers;
    private BufferedReader in;
    private PrintWriter out;

    @SneakyThrows
    @Override
    public void run() {
        init();
        process();
        clean();
    }

    private void init() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), false);
    }

    private void process() throws IOException {
        val requestStringArr = new ArrayList<String>();
        String inputLine;
        while (!(inputLine = in.readLine()).isEmpty()) {
            requestStringArr.add(inputLine);
        }
        System.out.println(requestStringArr);
        val body = new StringBuilder();
        while (in.ready()) {
            body.append((char) in.read());
        }

        val requestMetadata = RequestUtils.parseRequest(requestStringArr.stream().toArray(String[]::new),
                                                        body.toString());

        val responseObject = new RequestBinder(requestMetadata, handlers).getResponseObject();
        val responseString = RequestUtils.parseResponse(responseObject);

        out.write(responseString);
    }

    public void clean() throws IOException {
        out.flush();
        socket.close();
    }
}