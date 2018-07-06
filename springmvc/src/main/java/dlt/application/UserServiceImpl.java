package dlt.application;


import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {


    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static Map<Integer, User> mapUsers = new ConcurrentHashMap<Integer, User>();

    static {
        User user = new User();
        user.setUserId(-1);
        user.setUserName("yuntai");
        mapUsers.put(-1, user);
    }

    @Override
    public void addUser(User user) {
        if (!mapUsers.containsKey(user.getUserId())) {
            mapUsers.put(user.getUserId(), user);
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        // TODO Auto-generated method stub
        mapUsers.remove(userId);
    }

    @Override
    public List<User> getUsers() {
        logger.info("UserServiceImpl -> getUsers()");
        Collection<User> cs = mapUsers.values();
        List<User> users = new ArrayList<User>(cs);
        return users;
    }

    @Override
    public long getUserCount() {
        // TODO Auto-generated method stub
        return mapUsers.size();
    }

    @Override
    public void clear() {
        mapUsers.clear();

    }

}
