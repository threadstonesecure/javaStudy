package dlt.domain.module.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import dlt.domain.model.service.UService;
import dlt.study.log4j.Log;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.stereotype.Service;

import dlt.domain.model.User;
import dlt.domain.model.service.UserService;


@Service
public class UserServiceImpl implements UserService {


    private UService uService ;
	private static Map<Integer, User> mapUsers = new ConcurrentHashMap<Integer, User>();

	static {
		User user = new User();
		user.setUserId(-1);
		user.setUserName("yuntai");
		mapUsers.put(-1, user);
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
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
		Log.info("UserServiceImpl -> getUsers()");
		Collection<User> cs = mapUsers.values();
		List<User> users = new ArrayList<User>(cs);
        if (uService != null){
            uService.getUsers();
        }
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

    public UService getuService() {
        return uService;
    }

    public void setuService(UService uService) {
        this.uService = uService;
    }
}
