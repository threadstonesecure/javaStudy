package dlt.study.spring.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcTemplateTest {

	static JdbcTemplate jdbc = new JdbcTemplate();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		User user = findUser("zhangsan");

		// System.out.println("data:" + getData(1));
	}

	static int addUser(final User user) {
		jdbc.execute(new ConnectionCallback() {
			public Object doInConnection(Connection con) throws SQLException,
					DataAccessException {
				String sql = "insert into user(name,birthday, money) values (?,?,?) ";
				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getName());
				ps.setDate(2, new java.sql.Date(user.getBirthday().getTime()));
				ps.setFloat(3, user.getMoney());
				ps.executeUpdate();
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
					user.setId(rs.getInt(1));
				return null;
			}
		});
		return 0;
	}

	static Map getData(int id) {
		String sql = "select id as userId, name, money, birthday  from user where id="
				+ id;
		return jdbc.queryForMap(sql);
	}

	static String getUserName(int id) {
		String sql = "select name from user where id=" + id;
		Object name = jdbc.queryForObject(sql, String.class);
		return (String) name;
	}

	static int getUserCount() {
		String sql = "select count(*) from user";
		return jdbc.queryForObject(sql,int.class);  //jdbc.queryForInt(sql);
	}

	static List findUsers(int id) {
		String sql = "select id, name, money, birthday  from user where id<?";
		Object[] args = new Object[] { id };
		int[] argTypes = new int[] { Types.INTEGER };
		List users = jdbc.query(sql, args, argTypes, new BeanPropertyRowMapper(
				User.class));
		return users;
	}

	static User findUser(String name) {
		String sql = "select id, name, money, birthday  from user where name=?";
		Object[] args = new Object[] { name };
		Object user = jdbc.queryForObject(sql, args, new BeanPropertyRowMapper(
				User.class));
		return (User) user;
	}

	static User findUser1(String name) {
		String sql = "select id, name, money, birthday  from user where name=?";
		Object[] args = new Object[] { name };
		Object user = jdbc.queryForObject(sql, args, new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setMoney(rs.getFloat("money"));
				user.setBirthday(rs.getDate("birthday"));
				return user;
			}
		});
		return (User) user;
	}
}