package dlt.study.lambda;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dlt.domain.model.User;
import org.junit.Test;

import javax.transaction.NotSupportedException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorDemo {

    List<User> users = Lists.newArrayList(new User("zz",100),new User("denglt", 100), new User("zyy", 100), new User("denglt", 50));

    @Test
    public void toList() {
        List<String> names = Stream.of("denglt", "zyy", "dzy", "dwx").collect(Collectors.toList());
        names.stream().forEach(System.out::println);
    }

    @Test
    public void toMap() {
        Map<String, User> maps = users.stream().collect(Collectors.toMap(User::getUserName, u -> u));
        maps.forEach((name, u) -> System.out.println(name + " -> " + u));
    }

    /**
     * 处理Duplicate key
     * userName重复，返回age小的User
     */
    @Test
    public void toMapWithMerge() {
        Map<String, User> maps = users.stream().collect(Collectors.toMap(User::getUserName,
                u -> u,
                (u1, u2) -> u1.getAge() > u2.getAge() ? u2 : u1,
                TreeMap::new)
        );
        maps.forEach((name, u) -> System.out.println(name + " -> " + u));
    }

    @Test
    public void toSet() {
        Set<User> userSet = users.stream().collect(Collectors.toSet());
        userSet.stream().forEach(System.out::println);
    }

    @Test
    public void counting() {
        Long count = users.stream().collect(Collectors.counting());
        System.out.println(count);
    }


    @Test
    public void grouping() {
        Map<String, List<User>> groups = users.stream().collect(Collectors.groupingBy(User::getUserName));
        groups.forEach((name, users) -> System.out.println(name + " -> " + users.size()));
        groups.forEach((name, users) -> System.out.println(name + " -> " + users.stream().mapToInt(User::getAge).sum() ));

    }

    @Test
    public void grouping2() {
        Map<String, Set<User>> groups = users.stream().collect(Collectors.groupingBy(User::getUserName, Collectors.toSet()));
        groups.forEach((name, users) -> System.out.println(name + " -> " + users.size()));
        groups = users.stream().collect(Collectors.groupingBy(User::getUserName,TreeMap::new,Collectors.toSet()));
        groups.forEach((name, users) -> System.out.println(name + " -> " + users.size()));
        groups = users.stream().collect(Collectors.groupingBy(User::getUserName,TreeMap::new,Collectors.toCollection(TreeSet::new)));
        groups.forEach((name, users) -> System.out.println(name + " -> " + users.size()));
    }

    @Test
    public void partitioning(){ //与groupingBy类似
        Map<Boolean, List<User>> partions = users.stream().collect(Collectors.partitioningBy(t -> t.getAge() > 50));
        partions.forEach((b,users) -> System.out.println(b +"->"+ users));
    }

    @Test
    public  void joining(){
        String s = Stream.of("denglt","zyy").collect(Collectors.joining());
        System.out.println(s);
        s = Stream.of("denglt","zyy").collect(Collectors.joining(","));
        System.out.println(s);

        s = Stream.of("denglt","zyy").collect(Collectors.joining(",","begin->"," :end"));
        System.out.println(s);
    }

    @Test
    public void mapping(){
        List<String> names = users.stream().collect(Collectors.mapping(User::getUserName, Collectors.toList()));
        names.stream().forEach(System.out::println);

        System.out.println("----other----");
        names = users.stream().collect(Collector.of(ArrayList::new,
                                                   List::add,
                                                   (l , r) -> {return null;},
                                                   (List<User> us )->  us.stream().map(User::getUserName).collect(Collectors.toList())
                                                   ));
        names.stream().forEach(System.out::println);

        System.out.println("----other----");
        names = users.stream().collect(Collector.of(ArrayList::new,
                                                    (l , u) -> l.add(u.getUserName()),
                                                    (left, right) -> { left.addAll(right); return left; }
                                            ));
        names.stream().forEach(System.out::println);
    }

    @Test
    public void summarizing(){
        LongSummaryStatistics ageStatis = users.stream().collect(Collectors.summarizingLong(User::getAge));
        System.out.println(ageStatis);

        System.out.println("--------------------");
        Map<String, LongSummaryStatistics> userStatis = users.stream().collect(Collectors.groupingBy(User::getUserName, Collectors.summarizingLong(User::getAge)));
        userStatis.forEach((k,v) -> System.out.println( k + " -> " + v));
    }

    @Test
    public void myCollector() {
        Collector<User, ?, OneString<User>> collector = Collectors.toCollection(() -> new OneString<>(u -> "" + u.getAge()));
        collector = Collector.of( () -> new OneString<>( u -> "" + u.getAge()),
                OneString::add,
                (r1, r2) -> r1.add(r2) ,
                Collector.Characteristics.IDENTITY_FINISH
        );

        OneString<User> oneString = users.stream().collect(collector);
        System.out.println(oneString.toString());

        Map<String, OneString<User>> group = users.stream().collect(Collectors.groupingBy(User::getUserName,collector));
        group.forEach((k , v ) -> System.out.println(k + " -> " + v) );

       System.out.println("---------------------");

        Collector<User, List<User>, Integer> collector2 =  Collector.of(ArrayList::new,
                                                                        List::add,
                                                                        (left, right) -> { return null; }, // parallel 才会用到
                                                                        us -> us.stream().mapToInt(User::getAge).sum()
                                                                      //  ,Collector.Characteristics.IDENTITY_FINISH
                                                                        );
        Map<String, Integer> group2 = users.stream().collect(Collectors.groupingBy(User::getUserName,collector2));
        group2.forEach((k,v) -> System.out.println(k+ " -> " + v));

        Integer sumAge = users.stream().collect(collector2);
        System.out.println(sumAge);

        System.out.println("----------------------");
        group2 = users.stream().collect(Collectors.groupingBy(User::getUserName,
                                                             Collector.of( ArrayList::new,
                                                                           List::add,
                                                                           (left, right) -> { return null; },
                                                                           (List<User> us) -> us.stream().mapToInt(User::getAge).sum()
                                                                         )
                                                           )
                                       );
        group2.forEach((k,v) -> System.out.println(k+ " -> " + v));
    }


    /**
     * 这个== Collectors.toList();
     *
     * @param <T>
     * @return
     */
    public static <T> Collector<T, ?, List<T>> ToList() {
        return Collectors.toCollection(ArrayList::new);
    }

    static class OneString<T> extends AbstractCollection<T> {

        private Function deal;
        private StringBuffer sb = new StringBuffer();

        protected OneString(Function<T, ?> function) {
            super();
            this.deal = function;
        }

        public boolean add(T e) {
            sb.append(deal.apply(e));
            sb.append("～～～");
            return true;
        }

        public OneString add(OneString otherString){
            sb.append(otherString.toString());
            return this;
        }

        @Override
        public Iterator<T> iterator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return 1;
        }

        public String toString() {
            return sb.toString();
        }
    }

}
