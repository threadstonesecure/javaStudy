package dlt.domain.model.service;

import java.util.List;

import dlt.domain.model.User;

public interface UserService extends UService{

	public void addUser(User user);
	
	public void deleteUser(Integer userId);
	
	public List<User> getUsers();
	
	public long getUserCount();
	
	public void clear();
}
