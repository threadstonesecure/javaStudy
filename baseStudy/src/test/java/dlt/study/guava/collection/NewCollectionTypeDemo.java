package dlt.study.guava.collection;

import com.google.common.collect.*;
import dlt.domain.model.User;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.junit.Test;

import java.util.*;

public class NewCollectionTypeDemo {

    @Test
    public void hashMultiset() {
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("denglt");
        multiset.add("denglt");
        multiset.add("zyy");
        System.out.println(multiset.count("denglt")); // 2
        System.out.println("size = " + multiset.size()); // 3
        System.out.println("======multiset.forEach======");
        multiset.forEach(System.out::println);

        Multiset<String> multiset2 = HashMultiset.create();
        multiset2.add("denglt");
        multiset2.add("zyy");
        multiset2.add("denglt");
        System.out.println(multiset.equals(multiset2)); // true

        multiset.add("denglt", 3);
        System.out.println(multiset.count("denglt")); // 5
        System.out.println("size = " + multiset.size()); // 6

        System.out.println("========elementSet==========");
        multiset.elementSet().forEach(System.out::println);

        System.out.println("=======entrySet=========");
        multiset.entrySet().forEach(System.out::println);

        System.out.println("========forEachEntry===============");
        multiset.forEachEntry((v, count) -> System.out.println(v + " -> " + count));

        System.out.println("=========Multisets.sum=============");
        Multiset<String> sumMultiset = Multisets.sum(multiset, multiset2);
        sumMultiset.entrySet().forEach(System.out::println);
        //sumMultiset.setCount("denglt",0); // throws error java.lang.UnsupportedOperationException
        System.out.println("======setCount(\"denglt\",0)======");
        multiset.setCount("denglt", 0);
        multiset2.setCount("denglt", 0);
        sumMultiset.entrySet().forEach(System.out::println);

        System.out.println("===========Multisets.union=====================");
        Multiset<String> unionMultiset = Multisets.union(multiset, multiset2);

        System.out.println("==========");
        Set<String> set = Sets.newHashSet("denglt", "denglt");
        System.out.println(set.size());
        set.forEach(System.out::println);

       // Multisets.containsOccurrences()


    }

    /**
     * hash 顺序
     */
    @Test
    public void hashMultiset2() {
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("denglt");
        multiset.add("denglt");
        multiset.add("zyy");
        multiset.add("dengzy");
        multiset.add("dengwx");
        multiset.add("sdfsdf",2);
        System.out.println(multiset.count("denglt")); // 2
        System.out.println("size = " + multiset.size()); //
        System.out.println("======multiset.forEach======");
        multiset.forEach(System.out::println);
        multiset.forEachEntry((name, count) -> System.out.println(name + " -> " + count));
    }

    /**
     * 排序
     */
    @Test
    public void treeMultiset() {
        Multiset<String> multiset = TreeMultiset.create();
        multiset.add("denglt");
        multiset.add("denglt");
        multiset.add("zyy");
        multiset.add("dengzy");
        multiset.add("dengwx");
        System.out.println(multiset.count("denglt")); // 2
        System.out.println("size = " + multiset.size()); //
        System.out.println("======multiset.forEach======");
        multiset.forEach(System.out::println);
        multiset.forEachEntry((name, count) -> System.out.println(name + " -> " + count));
    }

    /**
     * 按照插入的顺序
     */
    @Test
    public void linkedHashMultiset() {
        Multiset<String> multiset = LinkedHashMultiset.create();
        multiset.add("denglt");
        multiset.add("denglt");
        multiset.add("zyy");
        multiset.add("dengzy");
        multiset.add("dengwx");
        System.out.println(multiset.count("denglt")); // 2
        System.out.println("size = " + multiset.size()); //
        System.out.println("======multiset.forEach======");
        multiset.forEach(System.out::println);
        multiset.forEachEntry((name, count) -> System.out.println(name + " -> " + count));

    }

    List<User> users = Lists.newArrayList(new User("denglt", 100),
            new User("zyy", 100), new User("denglt", 50),
            new User("dengzy", 0), new User("dengwx", 0));

    /**
     * a -> 1 a -> 2 a -> 4 b -> 3 c -> 5
     * or
     * a -> [1, 2, 4] b -> [3] c -> [5]
     * <p>
     * asMap() view, which returns a Map<K, Collection<V>>
     * <p>
     * Multimap
     * |-----SetMultimap
     * |------HashMultimap
     * |-----ListMultimap
     * |-----ArrayListMultimap
     */


    @Test
    public void listMultimap() {
        System.out.println("======ArrayListMultimap (HashMap,ArrayList)=========");
        ListMultimap<String, User> listMultimap = ArrayListMultimap.create();
        users.stream().forEach(u -> listMultimap.put(u.getUserName(), u));
        listMultimap.put(null, null);
        listMultimap.put(null, new User("null", 1000));
        System.out.println(listMultimap.size());
        Map<String, Collection<User>> userMap = listMultimap.asMap();
        userMap.forEach((k, v) -> System.out.println(k + " -> " + v));

        System.out.println("======LinkedListMultimap (LinkedHashMap , LinkedList)=========");
        ListMultimap<String, User> listMultimap2 = LinkedListMultimap.create();
        users.stream().forEach(u -> listMultimap2.put(u.getUserName(), u));
        listMultimap2.put(null, null);
        listMultimap2.put(null, new User("null", 1000));
        System.out.println(listMultimap2.size());
        Map<String, Collection<User>> userMap2 = listMultimap2.asMap();
        userMap2.forEach((k, v) -> System.out.println(k + " -> " + v));

    }

    @Test
    public void setMultimap() {
        System.out.println("======HashMultimap (HashMap,HashSet)=========");
        SetMultimap<String, User> setMultimap = HashMultimap.create();
        users.stream().forEach(u -> setMultimap.put(u.getUserName(), u));
        setMultimap.put(null, null);
        setMultimap.put(null, new User("null", 1000));
        System.out.println(setMultimap.size());
        Map<String, Collection<User>> userMap = setMultimap.asMap();
        userMap.forEach((k, v) -> System.out.println(k + " -> " + v));

        System.out.println("======LinkedHashMultimap (LinkedHashMap,LinkedHashSet)=========");
        SetMultimap<String, User> setMultimap2 = LinkedHashMultimap.create();
        users.stream().forEach(u -> setMultimap2.put(u.getUserName(), u));
        setMultimap2.put(null, null);
        setMultimap2.put(null, new User("null", 1000));
        System.out.println(setMultimap2.size());
        Map<String, Collection<User>> userMap2 = setMultimap2.asMap();
        userMap2.forEach((k, v) -> System.out.println(k + " -> " + v));

        System.out.println("======TreeMultimap (TreeMap,TreeSet)=========");
        SetMultimap<String, User> treeMultimap = TreeMultimap.create();
    }

    @Test
    public void multimapGet(){
        ListMultimap<String, User> listMultimap = ArrayListMultimap.create();
        List<User> denglt = listMultimap.get("denglt");
        List<User> denglt2 = listMultimap.get("denglt");
        System.out.println(denglt == null);
        System.out.println(denglt == denglt2);
        denglt.add(new User("denglt",0));
        denglt2.add(new User("denglt",1));
        System.out.println(listMultimap.size()); // 1
        listMultimap.forEach((k,v) -> System.out.println(k + " -> " + v));
        listMultimap.asMap().forEach((k,v) -> System.out.println(k + " -> " + v));
    }

    /**
     * If you need more customization, use Multimaps.newMultimap(Map, Supplier<Collection>)
     * or the list and set versions to use a custom collection, list, or set implementation to back your multimap.
     */

    @Test
    public void customMultimap() {

/*        Multimaps.newListMultimap();
        Multimaps.newMultimap();
        Multimaps.newSetMultimap();
        Multimaps.newSortedSetMultimap();*/

        Multimap<String, User> customMultimap = Multimaps.newMultimap(new TreeMap<String, Collection<User>>(), ArrayList<User>::new);

    }


    /**
     *  value 唯一
     */
    @Test
    public void biMap() {

        BiMap<String, Integer> biMap = HashBiMap.create();
        biMap.put("denglt", 10);
        biMap.put("denglt", 20);
        System.out.println(biMap.get("denglt"));
        //biMap.put("zyy",20); //java.lang.IllegalArgumentException: value already present: 20
        biMap.forcePut("zyy", 20);
        System.out.println(biMap.get("denglt")); // null;
        System.out.println(biMap.get("zyy")); // 20

        System.out.println("=====inverse=======");
        BiMap<Integer, String> inverse = biMap.inverse();
        inverse.forEach((key, value) -> System.out.println(key + " ->" + value));

        // Maps.unmodifiableBiMap()

    }

    /**
     * table <Row,Column,Value>
     * like  Map<FirstName, Map<LastName, Person>>
     * methods:
     *  1)rowMap(), which views a Table<R, C, V> as a Map<R, Map<C, V>>. Similarly, rowKeySet() returns a Set<R>.
     2)row(r) returns a non-null Map<C, V>. Writes to the Map will write through to the underlying Table.
     3)Analogous column methods are provided: columnMap(), columnKeySet(), and column(c). (Column-based access is somewhat less efficient than row-based access.)
     4)cellSet() returns a view of the Table as a set of Table.Cell<R, C, V>. Cell is much like Map.Entry, but distinguishes the row and column keys.
     */
    /**
     * Several Table implementations are provided, including:
     * 1)HashBasedTable, which is essentially backed by a HashMap<R, HashMap<C, V>> (as of Guava 20.0, it is backed by a LinkedHashMap<R, LinkedHashMap<C, V>>).
     * 2)TreeBasedTable, which is essentially backed by a TreeMap<R, TreeMap<C, V>>. // R 和 C 都排序
     * 3)ImmutableTable, which is essentially backed by an ImmutableMap<R, ImmutableMap<C, V>>. (Note: ImmutableTable has optimized implementations for sparser and denser data sets.)
     * 4)ArrayTable, which requires that the complete universe of rows and columns be specified at construction time, but is backed by a two-dimensional array to improve speed and memory efficiency when the table is dense. ArrayTable works somewhat differently from other implementations; consult the Javadoc for details.
     */
    /**
     * Table 工具类 Tables
         1) Tables.newCustomTable() 自定义Table，自己指定使用的Map
     */
    @Test
    public void table() {
        List<User> users = Lists.newArrayList(new User("denglt", 100), new User("denglt", 100),
                new User("dengzy", 100), new User("dengwx", 100),
                new User("zyy", 100),
                new User("xxx", 10),
                new User("denglt", 10));
        Table<Integer, String, User> tableUsers = HashBasedTable.create();
        users.forEach(u -> tableUsers.put(u.getAge(), u.getUserName(), u));
        System.out.println("===rowMap()====");
        Map<Integer, Map<String, User>> rowMap = tableUsers.rowMap();
        rowMap.forEach((age, usersByName) -> {
            System.out.println(age + "->");
            usersByName.forEach((name, user) -> System.out.println("   " + name + "->" + user));
        });

        System.out.println("===row() with 100 age ====");
        Map<String, User> rowWith100 = tableUsers.row(100);
        rowWith100.forEach((name, user) -> System.out.println(name + "->" + user));

        System.out.println("======columnMap()=======");
        Map<String, Map<Integer, User>> columnMap = tableUsers.columnMap();
        columnMap.forEach((name, userByAge) -> {
            System.out.println(name + "->");
            userByAge.forEach((age, user) -> System.out.println(age + " -> " + user));
        });

        System.out.println("===column() with \"denglt\" ====");
        Map<Integer, User> columnWithDenglt = tableUsers.column("denglt");
        columnWithDenglt.forEach((age, user) -> System.out.println(age + " -> " + user));

        //Tables.newCustomTable()

    }

    /**
     * a primitive type and its corresponding wrapper type may map to different values
     */
    @Test
    public void classToInstanceMap() {
        ClassToInstanceMap<Integer> classToInstanceMap = MutableClassToInstanceMap.create();
        classToInstanceMap.putInstance(Integer.class, 1);
        classToInstanceMap.putInstance(int.class, 2); //a primitive type and its corresponding wrapper type may map to different values
        classToInstanceMap.forEach((c, v) -> System.out.println(c + " -> " + v));

        System.out.println(int.class.equals(Integer.class));
    }

    @Test
    public void rangeSet() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10)); // {[1, 10]}
        rangeSet.add(Range.closedOpen(11, 15)); // disconnected range: {[1, 10], [11, 15)}
        rangeSet.add(Range.closedOpen(15, 20)); // connected range; {[1, 10], [11, 20)}
        rangeSet.add(Range.openClosed(20, 30)); // empty range; {[1, 10], [11, 20),(20,30]}
        rangeSet.remove(Range.open(5, 10)); // splits [1, 10]; {[1, 5], [10, 10], [11, 20),(20,30]}
        rangeSet.add(Range.openClosed(0, 0));
        System.out.println(rangeSet.contains(0));
        System.out.println(rangeSet.contains(4));

        System.out.println("==========asRanges()===============");
        rangeSet.asRanges().forEach(v -> System.out.println(v));

        System.out.println("===========complement()==补集=================");
        rangeSet.complement().asRanges().forEach(v -> System.out.println(v));

        System.out.println("==========asDescendingSetOfRanges()====倒序===========");
        rangeSet.asDescendingSetOfRanges().forEach(v -> System.out.println(v));

        System.out.println("=====subRangeSet()==交集=========");
        RangeSet<Integer> subRangeSet = rangeSet.subRangeSet(Range.closed(10, 50));
        subRangeSet.asRanges().forEach(v -> System.out.println(v));
        System.out.println(rangeSet.intersects(Range.closed(10, 50))); // 检查是否有交集
        System.out.println(rangeSet.intersects(Range.closed(50, 60)));

        System.out.println("========encloses()======");//straightforwardly enough, tests if any Range in the RangeSet encloses the specified range.
        System.out.println(rangeSet.encloses(Range.closed(10, 50))); // false
        System.out.println(rangeSet.encloses(Range.closed(10, 15))); // false ?????
        System.out.println(rangeSet.encloses(Range.closed(11,15))); // true

        System.out.println("========span()==============");
        System.out.println(rangeSet.span()); // [1..30]

        System.out.println("========ImmutableRangeSet===========");
        RangeSet<Integer> rangeSet2 = ImmutableRangeSet.copyOf(rangeSet);
        rangeSet.add(Range.closed(30, 50));
        rangeSet2.asRanges().forEach(v -> System.out.println(v));

        RangeSet<String> strRangeSet = TreeRangeSet.create();
        strRangeSet.add(Range.closed("A", "Z"));
        System.out.println(strRangeSet.contains("aa")); // false;
        System.out.println(strRangeSet.contains("AA"));
    }


    @Test
    public void rangeMap(){
        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closed(1, 10), "foo"); // {[1, 10] => "foo"}
        rangeMap.put(Range.open(3, 6), "bar"); // {[1, 3] => "foo", (3, 6) => "bar", [6, 10] => "foo"}
        rangeMap.put(Range.open(10, 20), "foo"); // {[1, 3] => "foo", (3, 6) => "bar", [6, 10] => "foo", (10, 20) => "foo"}
        rangeMap.remove(Range.closed(5, 11)); // {[1, 3] => "foo", (3, 5) => "bar", (11, 20) => "foo"}


        System.out.println("=====asMapOfRanges()=======");
        rangeMap.asMapOfRanges().forEach((range,v) -> System.out.println(range + " -> "+ v));

        System.out.println("get(3) ->" + rangeMap.get(3));

        System.out.println("======subRangeMap()==交集=====");
        rangeMap.subRangeMap(Range.closed(4,15)).asMapOfRanges().forEach((range,v) -> System.out.println(range + " -> "+ v));
    }

}
