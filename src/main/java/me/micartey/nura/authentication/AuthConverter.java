package me.micartey.nura.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class AuthConverter implements Converter<String, AuthConverter.Auth> {

    @Override
    public Auth convert(String data) {
        String decoded = new String(Base64.getDecoder().decode(data));
        String key = decoded.split(":")[0];
        String value = decoded.split(":")[1];
        return new Auth(key, value);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Auth {
        private final String key;
        private final String value;
    }
}
