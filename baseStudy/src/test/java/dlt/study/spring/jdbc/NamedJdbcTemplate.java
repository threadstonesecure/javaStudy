package dlt.study.spring.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class NamedJdbcTemplate {  
    static NamedParameterJdbcTemplate named ;// = new NamedParameterJdbcTemplate();  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        User user = new User();  
        user.setMoney(10);  
        user.setId(2);  
        System.out.println(findUser1(user));  
    }  
  
    static void addUser(User user) {  
        String sql = "insert into user(name,birthday, money) values (:name,:birthday,:money) ";  
        SqlParameterSource ps = new BeanPropertySqlParameterSource(user);  
        KeyHolder keyHolder = new GeneratedKeyHolder();  
        named.update(sql, ps, keyHolder);  
        int id = keyHolder.getKey().intValue();  
        user.setId(id);  
          
        Map map = keyHolder.getKeys();  
    }  
  
    static User findUser(User user) {  
        String sql = "select id, name, money, birthday  from user "  
                + "where money > :m and id < :id";  
        Map params = new HashMap();  
        // params.put("n", user.getName());  
        params.put("m", user.getMoney());  
        params.put("id", user.getId());  
        Object u = named.queryForObject(sql, params, new BeanPropertyRowMapper(  
                User.class));  
        return (User) u;  
    }  
  
    static User findUser1(User user) {  
        String sql = "select id, name, money, birthday  from user "  
                + "where money > :money and id < :id";  
        SqlParameterSource ps = new BeanPropertySqlParameterSource(user);  
        Object u = named.queryForObject(sql, ps, new BeanPropertyRowMapper(  
                User.class));  
        return (User) u;  
    }  
  
}  