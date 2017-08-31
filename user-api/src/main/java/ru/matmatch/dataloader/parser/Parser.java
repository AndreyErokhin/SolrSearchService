package ru.matmatch.dataloader.parser;

import java.io.InputStream;
import java.util.List;

/**
 * Created by erokhin.
 */
public interface Parser<T> {
    public List<T> parseInputStream(InputStream is);
}
