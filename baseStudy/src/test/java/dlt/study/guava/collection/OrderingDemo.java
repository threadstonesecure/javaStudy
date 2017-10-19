package dlt.study.guava.collection;

import com.google.common.collect.Comparators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import dlt.domain.model.User;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;


public class OrderingDemo {

    List<User> users = Lists.newArrayList(new User("denglt", 100), new User("zyy", 100), new User("xxx", 50));

    /**
     * 自然排序，排序对象实现Comparable接口
     */
    @Test
    public void natural() {
        Ordering<User> c = Ordering.natural();
        c.nullsFirst();
        User u = c.max(users);
        System.out.println(u);
        u = c.min(users);
        System.out.println(u);
        System.out.println("++++++++++++++++++++");
        List<User> orderUser = c.sortedCopy(users);
        orderUser.forEach(System.out::println);
        System.out.println(c.isOrdered(orderUser));

    }

    /**
     * 反向排序
     */
    @Test
    public void reverse() {
        Ordering<User> c = Ordering.natural().reverse();
        List<User> orderUser = c.sortedCopy(users);
        orderUser.forEach(System.out::println);
    }

    /**
     * 指定Comparator，生成Ordering
     */
    @Test
    public void from() {
        Ordering<User> ordering = Ordering.from(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getAge() - o2.getAge();
            }
        });
        users.add(null);
        List<User> orderUser = ordering.nullsFirst().sortedCopy(users);
        orderUser.forEach(System.out::println);
    }

    @Test
    public void lestof() {
        Ordering<User> ordering = Ordering.natural();
        List<User> newUsers = ordering.leastOf(users, 2);
        newUsers.forEach(System.out::println);


    }

    @Test
    public void greatestOf() {
        Ordering<User> ordering = Ordering.natural();
        List<User> newUsers = ordering.greatestOf(users, 2);
        newUsers.forEach(System.out::println);
        User maxUser = ordering.max(users);
    }

    /**
     * 指定用于排序的属性对象，该属性对象必须实现Comparable接口
     */
    @Test
    public void onResultOf() { //
        Ordering<User> ordering = Ordering.natural().nullsFirst().onResultOf(User::getUserName);
        List<User> users = ordering.sortedCopy(this.users);
        users.forEach(System.out::println);

       // Ordering<MyUser> ordering2 =   Ordering.natural().onResultOf( t -> t.getObj());
    }

    class MyUser {
        private String name;
        private int age;

        private Object obj;

        public MyUser(String name) {
            this.name = name;
        }

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

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }
    }
}
