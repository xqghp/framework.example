package com.xyzla.ws.stomp.message;

public class Greeting {
    private String username;
    private String body;

    public Greeting(String username, String body) {
        this.username = username;
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
