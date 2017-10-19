package dlt.study.guava.collection;

import com.google.common.collect.*;
import dlt.study.comm.PayStatusEnum;
import org.junit.Test;

import java.util.*;

public class NewCollectDemo {

    @Test
    public void newMap(){
        Map<String, String> a = Maps.newHashMap();
        Map<String, String> b = new HashMap<>();
        Map<String, String> c = Maps.newConcurrentMap(); // new ConcurrentHashMap<>();
        Map<String, String> d = Maps.newIdentityHashMap(); //在IdentityHashMap中，判断两个键值k1和 k2相等的条件是 k1 == k2
        ImmutableMap.Builder<String,String > builder =  ImmutableMap.builder();
        Map<String,String > e = ImmutableMap.of("name","denglt"); // 不可变动map
       // ImmutableMap.copyOf()

         Maps.newTreeMap(new MyComparator());
        EnumMap<PayStatusEnum,String> enumMap =   Maps.newEnumMap(PayStatusEnum.class);
    }

    @Test
    public void newList() {
        Object o = Lists.newArrayList(1,2,"String");

        List<List<Map<String, String>>> listA = Lists.newArrayList(); //new ArrayList<>();
        List<List<Map<String, String>>> listB = Lists.newCopyOnWriteArrayList();  // new CopyOnWriteArrayList<>() //lock on add、set、remove
        List<Integer> listC = Lists.newArrayList(1,2,3);

          //ImmutableList.of("adfdsf","asdfdsf").stream();

    }


    @Test
    public void newSet() {
        Set<Integer> setA = Sets.newHashSet(1,2,3); // new HashSet<>();
        Set<Integer> setB = Sets.newConcurrentHashSet(); // Collections.newSetFromMap(new ConcurrentHashMap<E, Boolean>());
        Set<Integer> setC = Sets.newCopyOnWriteArraySet();
  /*      Sets.union();
        Sets.intersection()
        Sets.difference()*/

        //ImmutableSet
        //ImmutableMultiset
    }

    @Test
    public void enumSet(){
        System.out.println(PayStatusEnum.class);
        System.out.println(PayStatusEnum.PAID.getDeclaringClass());
        System.out.println(PayStatusEnum.PAID.getClass());

        EnumSet<PayStatusEnum> paySet =  EnumSet.of(PayStatusEnum.PAID);
        System.out.println(paySet.size());

    }

    @Test
    public void newArray() {
        Integer[] intArray = ObjectArrays.newArray(Integer.class, 10);
        Integer[] intArray2 = ObjectArrays.newArray(new Integer[0], 10);


        System.out.println(intArray.getClass());
        System.out.println(intArray.getClass().isArray());
        System.out.println(intArray.getClass().getComponentType());
        System.out.println(Integer.class.getComponentType());
    }

    @Test
    public void newQueue(){
        Queues.newArrayBlockingQueue(10);
        Queues.newArrayDeque();
    }


}

class MyComparator implements Comparator<String>{
    @Override
    public int compare(String o1, String Object) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}


