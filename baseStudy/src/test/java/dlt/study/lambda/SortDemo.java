package dlt.study.lambda;

import com.google.common.collect.Lists;
import dlt.domain.model.User;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

public class SortDemo {
    List<User> users = Lists.newArrayList(new User("denglt", 100), new User("zyy", 100), new User("xxx", 50));

    @Test
    public void sort(){
        users.sort(Comparator.comparing(User::getUserName));
        users.forEach(System.out::println);
    }

    @Test
    public void desc(){
        users.sort(Comparator.comparing(User::getUserName).reversed());
        users.forEach(System.out::println);
    }

    @Test
    public void sort2(){
        Comparator<String> comparator =   Comparator.comparing( (String t) -> t ).reversed();
        users.sort(Comparator.comparing(User::getUserName,comparator));
        users.forEach(System.out::println);
    }
}
