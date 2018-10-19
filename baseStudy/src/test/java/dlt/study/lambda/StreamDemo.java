package dlt.study.lambda;

import com.google.common.collect.Lists;
import dlt.domain.model.User;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 在 Streams API 中，返回流对象的操作都是惰性操作，而返回非流对象的操作（或者无返回值的操作，例如 forEach()）都是急性操作。
 * 在使用这种 数据源—惰性操作—惰性操作—急性操作 流水线时，流水线中的惰性几乎是不可见的，因为计算过程被夹在数据源和最终结果（或副作用操作）之间。这使得API的可用性和性能得到了改善
 */
public class StreamDemo {
    List<User> users = Lists.newArrayList(new User("denglt", 100),
            new User("zyy", 100),
            new User("xxx", 50));

    @Test
    public void sumAge() {
        int sumAge = users.stream().filter(u -> u.getAge() >= 100).mapToInt(u -> u.getAge()).sum();
        System.out.println(sumAge);
        System.out.println(users.stream().filter(u -> u.getAge() >= 50).mapToInt(u -> u.getAge()).sum());
    }

    @Test
    public void sumAgeByParallel() {
        int sumAge = users.parallelStream().mapToInt(User::getAge).sum();
        System.out.println(sumAge);
    }

    @Test
    public void findFirst() {
        Optional<User> user = users.stream().filter(u -> u.getAge() >= 1000).findFirst();
        if (user.isPresent())
            System.out.println(user);
        else
            System.out.println("no find data !");
    }

    @Test
    public void minAge() {
        Optional<User> user = users.stream().sorted(Comparator.comparing(User::getAge)).findFirst();
        if (user.isPresent())
            System.out.println(user);
        else
            System.out.println("no find data !");
    }

    @Test
    public void minAge2() {
        Optional<User> user = users.stream().min(Comparator.comparing(User::getAge));
        if (user.isPresent())
            System.out.println(user);
        else
            System.out.println("no find data !");
    }

    @Test
    public void maxAge() {
        Comparator.comparing(User::getAge);
        Comparator.comparing((User u) -> u.getAge());
        Optional<User> user = users.stream().sorted(Comparator.comparing((User u) -> u.getAge()).reversed()).findFirst();
        //Optional<User> user = users.stream().sorted((u1,u2) -> u2.getAge() - u1.getAge()).findFirst();
        if (user.isPresent())
            System.out.println(user);
        else
            System.out.println("no find data !");
    }

    @Test
    public void maxAge2() {
        Optional<User> user = users.stream().max(Comparator.comparing(u -> u.getAge()));
        if (user.isPresent())
            System.out.println(user);
        else
            System.out.println("no find data !");
    }

    @Test
    public void anyMatch() {
        System.out.println(users.stream().anyMatch(u -> u.getAge() == 50));
    }

    @Test
    public void allMatch() {
        System.out.println(users.stream().allMatch(u -> u.getAge() > 0));
    }

    @Test
    public void noneMatch() {
        System.out.println(users.stream().noneMatch(u -> u.getAge() > 100));
    }

    @Test
    public void forEach() {
        users.stream().forEach((t -> t.setAge(t.getAge() * 2)));
        users.stream().forEach(System.out::println);
    }

    @Test
    public void distinct() {
        Stream.of("denglt", "zyy", "zyy", "xxx").distinct().forEach(System.out::println);
    }

    @Test
    public void flatMap() {
        // Stream.of("denglt", "zyy", "zyy", "xxx").flatMap(t -> Stream.of(t, t)).forEach(System.out::println);
        Map<String, Long> group = Stream.of("denglt", "zyy", "zyy", "xxx", "Denglt").flatMap(t -> Stream.of(t, t)).
                flatMap(t -> Stream.of(t, t)).
                collect(Collectors.groupingBy(t -> t.toLowerCase(), Collectors.counting()));
        group.forEach((k, v) -> System.out.println(k + " -> " + v));
    }

    @Test
    public void peek() {
        users.stream().peek(t -> t.setAge(t.getAge() * 2)).forEach(System.out::println);
    }

    @Test
    public void map() {
        users.stream().map(User::getUserName).forEach(System.out::println);
    }

    @Test
    public void reduce() {
        users = Lists.newArrayList(new User("denglt", 100));
        OptionalInt optionalInt = users.stream().mapToInt(User::getAge).reduce((p1, p2) -> p1 + p2);
        int sumAge = users.stream().mapToInt(User::getAge).reduce(1, (p1, p2) -> p1 + p2);
        System.out.println(sumAge);
        System.out.println(optionalInt.getAsInt());
    }


    /**
     * 无穷stream
     */
    @Test
    public void iterate() {
        Stream<Integer> stream = Stream.iterate(1, t -> ++t).limit(10);
        stream.forEach(System.out::println);

        stream = Stream.iterate(0, t -> t = t + 2).limit(10);
        stream.forEach(System.out::println);
    }

    @Test
    public void generate() {
        MyInteger myInteger = new MyInteger(0);
        Stream<Integer> stream = Stream.generate(() -> {
            myInteger.setValue(myInteger.getValue() + 1);
            return myInteger.getValue();
        }).limit(10);
        stream.forEach(System.out::println);

        Stream<Integer> stream2 = Stream.generate(new Supplier<Integer>() {
            private int start = 0;

            @Override
            public Integer get() {
                return ++start;
            }
        }).limit(10);
        stream2.forEach(System.out::println);
    }


    private class MyInteger {
        private int value;

        MyInteger(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
