package com.codecool.queststore.controller;


import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

public class Logout implements HttpHandler {

    public void handle (HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookieStr != null) {

            List<HttpCookie> cookieList = HttpCookie.parse(cookieStr);
            for (HttpCookie cookie : cookieList) {
                if ( cookie.getName().equals("sessionId")) {
                    Session.data.remove(cookie.getValue());
                }
            }
        }
        httpRedirectTo("/login", httpExchange);

    }


    private void httpRedirectTo(String dest, HttpExchange httpExchange) throws IOException {
        String hostPort = httpExchange.getRequestHeaders().get("host").get(0);
        httpExchange.getResponseHeaders().set("Location", "http://" + hostPort + dest);
        httpExchange.sendResponseHeaders(302, -1);
    }
}
