package dlt.utils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.hash.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 一致性Hash实现
 * <p>
 * 原实现：https://community.oracle.com/blogs/tomwhite/2007/11/27/consistent-hashing
 * <p>
 * 可以用来平衡数据
 * <p>
 * 下面是结合guava的hash后的改造
 */

public class ConsistentHash<K, T> {

    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();
    private final Funnel<K> kFunnel;
    private final Funnel<T> tFunnel;

    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
        this(hashFunction, numberOfReplicas, nodes, null, null);
    }


    /**
     * @param hashFunction
     * @param numberOfReplicas : 对于一个实际机器节点 node, 对应 numberOfReplicas 个虚拟节点
     * @param nodes
     */
    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas,
                          Collection<T> nodes, Funnel<K> kFunnel, Funnel<T> tFunnel) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
        this.kFunnel = kFunnel;
        this.tFunnel = tFunnel;
        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashNode(node, i), node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashNode(node, i));
        }
    }

    public T get(K key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hashKey(key);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public SortedMap<Integer, T> getCircle() {
        return circle;
    }

    private int hashNode(T node, int number) {
        Hasher hasher = hashFunction.newHasher().putInt(number);
        return (tFunnel == null ? hasher.putString(node.toString(), StandardCharsets.UTF_8)
                : hasher.putObject(node, tFunnel))
                .hash().asInt();
    }

    private int hashKey(K key) {
        Hasher hasher = hashFunction.newHasher();
        return (kFunnel == null ? hasher.putString(key.toString(), StandardCharsets.UTF_8)
                : hasher.putObject(key, kFunnel))
                .hash().asInt();
    }


    /**
     * 测试数据平衡
     *
     * @param args
     */
    public static void main(String[] args) {
        List<String> nodes = ImmutableList.of("192.168.1.100", "192.168.1.101", "192.168.102");
        ConsistentHash<String, String> consistentHash = new ConsistentHash<>(Hashing.murmur3_128(64), 3, nodes);
        System.out.println("=========节点hash分布==============");
        consistentHash.getCircle().forEach((k, v) -> System.out.println(k + "->" + v));

        System.out.println("=========节点数据分布=================");
        Multiset<String> multiset = HashMultiset.create();
        for (int i = 0; i < 100; i++) {
            multiset.add(consistentHash.get(getRandomString(i / 20)));
        }

        multiset.forEachEntry((v, c) -> System.out.println(v + ":" + c));
    }


    private static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}

