package me.micartey.nura.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MailRequests implements Serializable {

    private String host;
    private int port;
    private boolean starttls;
    private Auth auth;
    private Message message;

    @Data
    @AllArgsConstructor
    public static class Message {
        private String recipient;
        private String subject;
        private String text;
    }

    @Data
    @AllArgsConstructor
    public static class Auth {
        private String mail, password;
    }
}
