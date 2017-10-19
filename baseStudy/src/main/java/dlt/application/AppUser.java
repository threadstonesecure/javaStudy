package dlt.application;

import java.util.List;

import dlt.domain.model.User;

public interface AppUser {

	public void add(User user);

	public List<User> listUsers();

	public String toJson();

	public User findUser(String userName);
}
