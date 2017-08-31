package ru.matmatch.dataloader;

import org.junit.Assert;
import org.junit.Test;
import ru.matmatch.dataloader.parser.UserParser;
import ru.matmatch.model.User;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by erokhin.
 */

public class UserParserTest {

    private UserParser loader = new UserParser();
    private final Path resourceDirectory = Paths.get("src/test/resources");

    @Test
    public void testCnt() throws IOException {
        InputStream stream = new FileInputStream(new File(resourceDirectory.toString(),"cnt1.csv"));
        List<User> result = loader.parseInputStream(stream);
        Assert.assertEquals(8,result.size());
    }

    @Test
    public void contentTest() throws FileNotFoundException {
        InputStream stream = new FileInputStream(new File(resourceDirectory.toString(),"content.csv"));
        List<User> result = loader.parseInputStream(stream);
        Assert.assertEquals(2,result.size());

//        id,first_name,last_name,email,gender,ip_address
//        1,Andrey,Erokhin,lol@kek.com,Male,204.21.163.192
//        4,Kerrill,Derry,kderry3@acquirethisname.com,Female,249.181.187.62

        Assert.assertEquals("First name should parsed correctly","Andrey", result.get(0).getFirstName());
        Assert.assertEquals("Last name should parsed correctly","Erokhin", result.get(0).getLastName());
        Assert.assertEquals("Email should parsed correctly","lol@kek.com", result.get(0).getEmail());
        Assert.assertEquals("Gender name should parsed correctly","male", result.get(0).getGender().toString());
        Assert.assertEquals("Ip address should parsed correctly","204.21.163.192", result.get(0).getIpAddress());

        Assert.assertEquals("First name should parsed correctly","Kerrill", result.get(1).getFirstName());
        Assert.assertEquals("Last name should parsed correctly","Derry", result.get(1).getLastName());
        Assert.assertEquals("Email should parsed correctly","kderry3@acquirethisname.com", result.get(1).getEmail());
        Assert.assertEquals("Gender name should parsed correctly","female", result.get(1).getGender().toString());
        Assert.assertEquals("Ip address should parsed correctly","249.181.187.62", result.get(1).getIpAddress());
    }

}
