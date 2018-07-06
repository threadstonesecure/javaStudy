package dlt.application;


import java.util.List;

public interface AppUser {

    void add(User user);

    List<User> listUsers();

    String toJson();

    User findUser(String userName);
}
