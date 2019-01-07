package jackson;

import java.lang.reflect.Type;
import java.util.*;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import dlt.utils.JsonUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class JacksonTest {

    private AccountBean bean;

    @Before
    public void init() {
        bean = new AccountBean();
        bean.setAddress("china-Guangzhou");
        bean.setEmail("hoojo_@126.com");
        bean.setId(1L);
        bean.setName("hoojo");
        bean.setBirthday(new Date());
    }

    @After
    public void destory() {

    }

    @Test
    public void writeEntityJSON() {

        String jsonStr = JsonUtils.toJson(bean);
        System.out.println(jsonStr);
    }

    @Test
    public void writeMapJSON() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", bean.getName());
        map.put("account", bean);
        bean = new AccountBean();
        bean.setAddress("china-Beijin");
        bean.setEmail("hoojo@qq.com");
        map.put("account2", bean);
        String jsonStr = JsonUtils.toJson(map);
        System.out.println(jsonStr);
    }

    @Test
    public void writeMap2Json() {
        Map<String, Object> mapLevel1 = new HashMap<>();
        Map<String, Object> mapLevel2 = new HashMap<>();
        Map<String, Object> mapLevel3 = new HashMap<>();
        mapLevel1.put("alibaba_aliqin_fc_tts_num_singlecall_response", mapLevel2);
        mapLevel2.put("result", mapLevel3);
        mapLevel3.put("err_code", 0);
        mapLevel3.put("model", "1234567");
        mapLevel3.put("success", false);
        mapLevel3.put("msg", "成功");
        String jsonStr = JsonUtils.toJson(mapLevel1);
        System.out.println(jsonStr);

        Map<String, Object> mapLevel_1 = JsonUtils.toMap(jsonStr);
        System.out.println(mapLevel_1);

    }

    @Test
    public void writeListJSON() {
        List<AccountBean> beans = new ArrayList<AccountBean>();
        beans.add(bean);
        beans.add(bean);
        String jsonStr = JsonUtils.toJson(beans);
        System.out.println(jsonStr);
    }

    @Test
    public void readJson2Entity() {
        String json = "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";

        AccountBean acc = JsonUtils.toObject(json, AccountBean.class);
        System.out.println(acc.getName());
        System.out.println(acc);

    }


    @Test
    public void readJson2Entity2() {
        String info = "{ \"redirectHosId\" :\"1000\" , \"redirectCmds\":[\"ssdfad\",\"sdfsdf\"]}";
        RedirectHospital redirectHospital = JsonUtils.toObject(info, RedirectHospital.class);
        System.out.println(redirectHospital.getRedirectHosId());
        System.out.println(redirectHospital.redirectCmds);
    }

    @Test
    public void readJson2List() {
        String json = "[{\"id\":1,\"name\":\"hoojo\",\"email\":\"hoojo_@126.com\",\"address\":\"china-Guangzhou\",\"birthday\":\"2014-09-18 17:15:39\"},{\"id\":1,\"name\":\"hoojo\",\"email\":\"hoojo_@126.com\",\"address\":\"china-Guangzhou\",\"birthday\":\"2014-09-18 17:15:39\"}]";
        List<AccountBean> list = JsonUtils.toList(json, AccountBean.class);
        for (AccountBean bean : list) {
            System.out.println(bean);
        }

    }

    public List<AccountBean> abs;

    @Test
    public void readJson2List2() throws Exception {
        String json = "[{\"id\":1,\"name\":\"hoojo\",\"email\":\"hoojo_@126.com\",\"address\":\"china-Guangzhou\",\"birthday\":\"2014-09-18 17:15:39\"},{\"id\":1,\"name\":\"hoojo\",\"email\":\"hoojo_@126.com\",\"address\":\"china-Guangzhou\",\"birthday\":\"2014-09-18 17:15:39\"}]";
        Type type = JacksonTest.class.getDeclaredField("abs").getGenericType();
        System.out.println(type);

        TypeReference<List<AccountBean>> typeReference = new TypeReference<List<AccountBean>>() {
        };

        Type type2 = typeReference.getType();

        System.out.println(type == type2);
        System.out.println(type.getClass());
        System.out.println(type.getTypeName());

        System.out.println("=================");
        if (type instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) type;
            for (Type type1 : parameterizedType.getActualTypeArguments()) {
                System.out.println(type1);
            }
            System.out.println(parameterizedType.getRawType());
            System.out.println(parameterizedType.getOwnerType());
        }
        System.out.println("=============================");
        List<AccountBean> list = (List<AccountBean>) JsonUtils.toObject(json, type, null);
        for (AccountBean bean : list) {
            System.out.println(bean);
        }

    }

    // getClass().getGenericSuperclass();

    @Test
    public void readJson2List3() throws Exception {
        String json = "[{\"id\":1,\"name\":\"hoojo\",\"email\":\"hoojo_@126.com\",\"address\":\"china-Guangzhou\",\"birthday\":\"2014-09-18 17:15:39\"},{\"id\":1,\"name\":\"hoojo\",\"email\":\"hoojo_@126.com\",\"address\":\"china-Guangzhou\",\"birthday\":\"2014-09-18 17:15:39\"}]";

        TypeReference<List<AccountBean>> typeReference = new TypeReference<List<AccountBean>>() {
        };
        List<AccountBean> list = (List<AccountBean>) JsonUtils.toObject(json,
                typeReference);
        for (AccountBean bean : list) {
            System.out.println(bean);
        }

    }

    @Test
    public void readJson2Map() {
        String json = "{\"success\":true,\"A\":{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
                + "\"B\":{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}}";
        Map<String, Object> maps = JsonUtils.toMap(json);
        System.out.println(maps.size());
        Set<String> key = maps.keySet();
        Iterator<String> iter = key.iterator();
        while (iter.hasNext()) {
            String field = iter.next();
            Object o = maps.get(field);
            System.out.println(field + ":" + o + ":" + o.getClass());
        }

    }

    @SuppressWarnings("unchecked")
    @Test
    public void readJson2Map2() {
        bean = new AccountBean();
        bean.setAddress("china-Guangzhou");
        bean.setEmail("hoojo_@126.com");
        bean.setId(1L);
        bean.setName("hoojo");
        bean.setBirthday(new Date());

        AccountBean bean2 = new AccountBean();
        bean2.setAddress("china-Guangzhou");
        bean2.setEmail("hoojo_@126.com");
        bean2.setId(2L);
        bean2.setName("hoojo");
        bean2.setBirthday(new Date());

        Map<String, AccountBean> acs = new HashMap<String, AccountBean>();
        acs.put("" + bean.getId(), bean);
        acs.put("" + bean2.getId(), bean2);
        String json = JsonUtils.toJson(acs);
        TypeReference<Map<String, AccountBean>> typeReference = new TypeReference<Map<String, AccountBean>>() {
        };
        Map<String, AccountBean> maps = (Map<String, AccountBean>) JsonUtils
                .toObject(json, typeReference);

        System.out.println(maps.size());
        Set<String> key = maps.keySet();
        Iterator<String> iter = key.iterator();
        while (iter.hasNext()) {
            String field = iter.next();
            Object o = maps.get(field);
            System.out.println(field + ":" + o + ":" + o.getClass());
        }

    }


    @Test
    public void readJson2MapBean() {
        bean = new AccountBean();
        bean.setAddress("china-Guangzhou");
        bean.setEmail("hoojo_@126.com");
        bean.setId(1L);
        bean.setName("hoojo");
        bean.setBirthday(new Date());

        AccountBean bean2 = new AccountBean();
        bean2.setAddress("china-Guangzhou");
        bean2.setEmail("hoojo_@126.com");
        bean2.setId(2L);
        bean2.setName("hoojo");
        bean2.setBirthday(new Date());

        Map<String, AccountBean> acs = new HashMap<String, AccountBean>();
        acs.put("" + bean.getId(), bean);
        acs.put("" + bean2.getId(), bean2);
        String json = JsonUtils.toJson(acs);

        Map<String, AccountBean> maps = JsonUtils.toMap(json, String.class,
                AccountBean.class);
        Map<String, AccountBean> maps2 = JsonUtils.toMap(json, AccountBean.class);

        System.out.println(maps.size());
        Set<String> key = maps.keySet();
        Iterator<String> iter = key.iterator();
        while (iter.hasNext()) {
            String field = iter.next();
            AccountBean o = maps.get(field);
            System.out.println(field + ":" + o + ":" + o.getClass());
        }

    }

    @Test
    public void typeReference() throws Exception {
        TypeReference<List<AccountBean>> typeReference = new TypeReference<List<AccountBean>>() {
        };
        Type type = typeReference.getType();
        System.out.println(type);
        type = JacksonTest.class.getDeclaredField("abs").getGenericType();
        System.out.println(type);

        TypeReference<AccountBean> typeReference2 = new TypeReference<AccountBean>() {
        };
        type = typeReference2.getType();
        System.out.println(type);

    }

    @Test
    public void mapData() {
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("sheetId", "123412341");
        mapData.put("sheetName", "北京人民医院检查报告单");
        User u = new User();
        u.setAge(10);
        u.setName("denglt");
        mapData.put("user", u);
        String body = JsonUtils.toJson(mapData);
        System.out.println(body);
    }

    @Test
    public void mapData2() {
        Map<User, User> mapData = new HashMap<User, User>();
        User u = new User();
        u.setAge(10);
        u.setName("denglt");
        mapData.put(u, u);
        String body = JsonUtils.toJson(mapData);
        System.out.println(body);
    }


    @Test
    public void test() {
        String jsonStr = "{ \"id\" :\"1000\" }";
        AccountBean accountBean = JSON.parseObject(jsonStr, AccountBean.class);
        System.out.println(accountBean);

        accountBean = JsonUtils.toObject(jsonStr, AccountBean.class);
        System.out.println(accountBean);
    }
}

class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}


class RedirectHospital {
    private String redirectHosId;
    public List<String> redirectCmds;

    public String getRedirectHosId() {
        return redirectHosId;
    }

    public void setRedirectHosId(String redirectHosId) {
        this.redirectHosId = redirectHosId;
    }
}
