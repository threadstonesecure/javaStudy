package dlt.study.guava.collection;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import dlt.domain.model.User;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ConvenienceDemo {

    @Test
    public void toStringHelper() {
        User user = new User();
        user.setUserName("denglt");
        user.setAge(60);
        String s = MoreObjects.toStringHelper(user)
                .add("name", "denglt")
                .add("age", 100).addValue("zyy")
                .add("childs", Lists.newArrayList("dzy","dwx"))
                .toString();
        System.out.println(s);

        List<String> names =  Lists.newArrayList("dzy","dwx");
        Object[] os  = {names};
        System.out.println(os.length);
        System.out.println(Arrays.deepToString(os));
        System.out.println(Arrays.deepToString(names.toArray()));
        System.out.println(Arrays.toString(names.toArray()));
    }

    @Test
    public void strings(){
        String prefix = Strings.commonPrefix("denglt","dengzy");
        System.out.println(prefix);

    }
}
