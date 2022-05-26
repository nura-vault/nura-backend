package me.micartey.nura.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Stream {

    private static final Logger logger = LoggerFactory.getLogger(Stream.class);

    public static ArrayList<String> getValues(InputStream inputStream) {
        ArrayList<String> values = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.lines().forEach(values::add);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return values;
    }

    public static String getContent(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.lines().forEach(builder::append);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return builder.toString();
    }
}
