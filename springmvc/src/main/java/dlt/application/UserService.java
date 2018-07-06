package dlt.application;

import java.util.List;

public interface UserService {

    void addUser(User user);

    void deleteUser(Integer userId);

    List<User> getUsers();

    long getUserCount();

    void clear();
}
