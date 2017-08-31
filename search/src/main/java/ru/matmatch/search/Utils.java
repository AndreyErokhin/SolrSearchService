package ru.matmatch.search;

import ru.matmatch.model.User;
import ru.matmatch.search.model.UserIndex;

import java.util.function.Function;

public class Utils {

    public static final Function<User,UserIndex> userToIndexMapper = new Function<User, UserIndex>() {
        @Override
        public UserIndex apply(User user) {
            return new UserIndex(user.getId(),user.getFirstName(),user.getLastName(),user.getIpAddress());
        }
    };
}
