package ru.matmatch;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * Created by erokhin.
 */
public class TestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
}
