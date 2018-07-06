package dlt.application;

import dlt.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class AppUserImpl implements AppUser {

	@Autowired
	//@Autowired(required=false) //缺省为必须有匹配的bean
	//@Qualifier("userService") //如果在配置了多个实例，需要这里要指定qualifier
	//@Resource  //默认首先使用与字段名相同的bean进行注入；如果没有，则使用类型匹配的bean。固@Resource可以替换上面两句话而Spring不会报错
	//@Resource(name="userService")	
	private UserService userService;
	
	
	@PostConstruct  //init-method    
	public void init() {
		System.out.println("Init method after properties are set " );
        User user1 = new User();
        user1.setUserId(1);
        user1.setUserName("邓隆通");
        userService.addUser(user1);
        //创建用户2
        User user2 = new User();
        user2.setUserId(2);
        user2.setUserName("denglt");
        user2.setAge(38);
        userService.addUser(user2);

        User user3 = new User();
        user3.setUserId(3);
        user3.setUserName("zyy");
        user3.setAge(30);
        userService.addUser(user3);
	}

	@PreDestroy  //destroy-method
	public void cleanUp() throws Exception {
	  System.out.println("Spring Container is destroy! User clean up");
	  userService.clear();
	}
	
	@Override
	public void add(User user) {

		userService.addUser(user);
	}


	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public List<User> listUsers() {
		 
		return userService.getUsers();
	}

	
	@Override
	public User findUser(String userName) {
		List<User> users = userService.getUsers();
		for (User user :users){
			if (user.getUserName().equals(userName)){
				return user;
			}
		}
		return null;
	}


	@Override
	public String toJson() {
        TotalJson total = new TotalJson();
        total.setResults(userService.getUserCount());
        total.setItems(userService.getUsers());
       return JsonUtils.toJson(total);
	}



}
