package ru.matmatch.dataloader.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.matmatch.model.Gender;
import ru.matmatch.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erokhin.
 */
public class UserParser implements Parser<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserParser.class);
    @Override
    public List<User> parseInputStream(InputStream is) {
        ArrayList<User> result = new ArrayList<>();
        Reader in = new InputStreamReader(is);
        Iterable<CSVRecord> records = null;
        try {
            records = CSVFormat.RFC4180
                    .withHeader("id","firstName","lastName","email","gender","ipAddress")
                    .withSkipHeaderRecord()
                    .withDelimiter(',')
                    .parse(in);
        } catch (IOException e) {
            logger.error("Error while parsing file with the users", e);
        }
        records.forEach(record->result.add(new User(record.get("firstName"),record.get("lastName"),record.get("email"), Gender.valueOf(record.get("gender").toLowerCase()),record.get("ipAddress"))));
        return result;
    }
}
