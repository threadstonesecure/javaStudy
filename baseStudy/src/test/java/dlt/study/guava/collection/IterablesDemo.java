package dlt.study.guava.collection;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Iterables 工具类
 * Whenever possible, Guava prefers to provide utilities accepting an Iterable rather than a Collection
 */
public class IterablesDemo {

    @Test
    public void concat(){
        List<String> strs = Lists.newArrayList("denglt","zyy");
        Set<String>  strSet = Sets.newHashSet("denglt","zyy","dengwx");
        Iterables.concat(strs,strSet).forEach(System.out::println);
    }


    @Test
    public void filter(){
        List<String> strs = Lists.newArrayList("denglt","zyy");
        Set<String>  strSet = Sets.newHashSet("denglt","zyy","dengwx");
        Iterable<String> concat = Iterables.concat(strs, strSet);
        Iterables.filter(concat, t -> t == null ? false : t.equals("dengltxx")).forEach(System.out::println);
        Lists.newArrayList(Iterables.filter(concat, t -> t == null ? false : t.equals("dengltxx")));
    }

    /**
     * like Stream.allMatch
     */
    @Test
    public void all(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        System.out.println(Iterables.all(strs, t -> t.compareTo("a") >= 0  ));
    }

    /**
     * like Stream.anyMatch
     */
    @Test
    public void any(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        System.out.println(Iterables.any(strs, t -> t.compareTo("c") >= 0  ));
    }

    @Test
    public void frequency(){
        List<String> strs = Lists.newArrayList("denglt","zyy","denglt");
        System.out.println(Iterables.frequency(strs,"denglt"));
    }

    @Test
    public void partition(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        System.out.println("====Iterables.partition====");
        Iterable<List<String>> partition = Iterables.partition(strs, 2);
        partition.forEach(System.out::println);
        System.out.println("====Lists.partition====");
        List<List<String>> partition1 = Lists.partition(strs, 2);
        partition1.forEach(System.out::println);
    }

    @Test
    public  void getFirstAndLast(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        System.out.println(Iterables.getFirst(strs,"default"));
        System.out.println(Iterables.getLast(strs));

    }

    /**
     * like List.equals
     */
    @Test
    public void elementsEqual(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        List<String> strs2 = Lists.newArrayList("a","b","c","d","e");
        System.out.println(Iterables.elementsEqual(strs,strs2));
    }

    /**
     *  like Collections.unmodifiableMap()
     */
    @Test
    public void unmodifiableIterable(){
      // Iterables.unmodifiableIterable()

    }

    @Test
    public void limit(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        Iterables.limit(strs,3).forEach(System.out::println);
    }

    @Test
    public void getOnlyElement(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        String onlyElement = Iterables.getOnlyElement(strs);
        System.out.println(onlyElement);
    }

    @Test
    public void size(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        System.out.println( Iterables.size(strs));
    }

    /**
     * like Iterators.mergeSorted
     */
    @Test
    public void mergeSorted(){
        //Iterables.mergeSorted()
    }

    /**
     * like Iterators.addAll
     */
    @Test
    public void addAll(){
        //Iterables.addAll()
    }

    /**
     * like Iterators.removeAll
     *   or Collection.removeAll
     */
    @Test
    public void removeAll(){
       // Iterables.removeAll()
    }

    /**
     * 一直循环
     */
    @Test
    public void cycle(){
        Iterables.cycle("a","b","c").forEach(System.out::println);
    }

    @Test
    public void skip(){
        List<String> strs = Lists.newArrayList("a","b","c","d","e");
        Iterables.skip(strs,3).forEach(System.out::println);
    }

}
