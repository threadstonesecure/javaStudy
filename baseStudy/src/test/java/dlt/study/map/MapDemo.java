package dlt.study.map;

import org.junit.Test;

import java.util.*;

/**
 * Created by denglt on 2016/2/1.
 */
public class MapDemo {

	@Test
	public void treeMap() {
		// 按照自然顺序或自定义顺序遍历键
        Map<String, String> map = new TreeMap<>();
        map.put("d","dd");
        map.put("c","cc");
        map.put("b","bb");
        map.put("a","aa");
        Collection<String> vs = map.values();
        for (String v :vs){
            System.out.println(v);
        }
        System.out.println("====tail===");
        SortedMap<String,String> tail = ((TreeMap)map).tailMap("c",true);
        vs = tail.values();
        for (String v :vs){
            System.out.println(v);
        }
        System.out.println("====firstKey====");
        System.out.println(tail.firstKey());
	}

	@Test
	public void hashMap() {
		// 根据键的hashCode值存储数据，根据键可以直接获取它的值，具有很快的访问速度
		// HashMap最多只允许一条记录的键为NULL，允许多条记录的值为NULL
		// 不支持线程同步
        Map<String, String> map = new HashMap<>();
        map.put("d","dd");
        map.put("c","cc");
        map.put("b","bb");
        map.put("a","aa");
        Collection<String> vs = map.values();
        for (String v :vs){
            System.out.println(v);
        }
	}

	@Test
	public void hashTable() {
		// Hashtable与HashMap类似，不同的是：它不允许记录的键或者值为空；
		// 它支持线程的同步，即任一时刻只有一个线程能写Hashtable，因此也导致了Hashtable在写入时会比较慢
	}

	@Test
	public void linkedHashMap() {
		// 保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的。
		Map<String, String> map = new LinkedHashMap<>();
        map.put("d","dd");
        map.put("c","cc");
        map.put("b","bb");
        map.put("a","aa");
        Collection<String> vs = map.values();
        for (String v :vs){
            System.out.println(v);
        }


	}
}
