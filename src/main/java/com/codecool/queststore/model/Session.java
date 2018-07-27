package com.codecool.queststore.model;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {

    public static Map<String, User> data = new HashMap<String, User>();

    public static String getTwigPath() {
        return System.getProperty("user.dir") + "/src/main/resources/templates/";
    }


    public static String getSessionId(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie = null;

        if (cookieStr != null) {
            List<HttpCookie> cookieList = HttpCookie.parse(cookieStr);
            for (HttpCookie c : cookieList) {
                if ( c.getName().equals("sessionId")) {
                    cookie = c;
                }
            }
        }

        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public static User getLoggedUser(HttpExchange httpExchange) {
        String sessionId = Session.getSessionId(httpExchange);
        if (sessionId != null) {
            return Session.data.get(sessionId);
        } else {
            return null;
        }
    }


    public static boolean guard(HttpExchange httpExchange) throws IOException {
        if (Session.getLoggedUser(httpExchange) == null) {
            Session.httpRedirectTo("/login", httpExchange);
            return false;
        } else {
            return true;
        }
    }

    private static void httpRedirectTo(String dest, HttpExchange httpExchange) throws IOException {
        String hostPort = httpExchange.getRequestHeaders().get("host").get(0);
        httpExchange.getResponseHeaders().set("Location", "http://" + hostPort + dest);
        httpExchange.sendResponseHeaders(302, -1);
    }


}
