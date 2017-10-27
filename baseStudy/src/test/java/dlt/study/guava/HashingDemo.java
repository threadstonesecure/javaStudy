package dlt.study.guava;

import com.google.common.hash.*;
import dlt.domain.model.User;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class HashingDemo {

    @Test
    public void bitset(){
        BitSet bitSet = new BitSet(100); // BitMap

        bitSet.set(1,true);

        bitSet.set(3,true);

        bitSet.set(6,true);

        bitSet.set(100,true);

        for(int i=0;i<bitSet.size();i++){

            boolean b = bitSet.get(i);

           // if(b){

                System.out.println(i);

           // }

        }
    }

    @Test
    public void hash() {
        HashFunction hf =  Hashing.murmur3_128(64); //Hashing.goodFastHash(64) 随机数;  //Hashing.sha256(); // Hashing.md5();
        HashCode hc = hf.newHasher()
                .putLong(1000)
                .putString("denglt", StandardCharsets.UTF_8)
                .putObject(new User("zyy", 16), new Funnel<User>() {
                    @Override
                    public void funnel(User from, PrimitiveSink into) {
                        into.putInt(from.getAge())
                                .putString(from.getUserName(), StandardCharsets.UTF_8);

                    }
                })
                .hash();
        System.out.println(hc.hashCode());
        System.out.println(hc.asInt());
        System.out.println(hc.asLong());
        System.out.println(hc.toString());

        System.out.println(Hashing.md5().newHasher().putString("denglt",StandardCharsets.UTF_8).hash().toString()); // 98608d4679a28b719815ee03f7c404e0
    }

    // HashingInputStream
    // HashingOutputStream


    /**
     * Bloom Filter（BF）是一种空间效率很高的随机数据结构，它利用位数组很简洁地表示一个集合，并能判断一个元素是否属于这个集合。
     * 它是一个判断元素是否存在集合的快速的概率算法。Bloom Filter有可能会出现错误判断，但不会漏掉判断。
     * 也就是Bloom Filter判断元素不再集合，那肯定不在。如果判断元素存在集合中，有一定的概率判断错误。
     * 因此，Bloom Filter不适合那些“零错误”的应用场合。
     * 而在能容忍低错误率的应用场合下，Bloom Filter比其他常见的算法（如hash，折半查找）极大节省了空间。
      它的优点是空间效率和查询时间都远远超过一般的算法，缺点是有一定的误识别率和删除困难
     *
     *
     * Bloom Filter是一种空间效率很高的随机数据结构，它利用位数组很简洁地表示一个集合，并能判断一个元素是否属于这个集合。
     * Bloom Filter的这种高效是有一定代价的：
     *     在判断一个元素是否属于某个集合时，有可能会把不属于这个集合的元素误认为属于这个集合（false positive）。
     *     但不会把属于这个集合的元素误认为不属于这个集合
     *     因此，Bloom Filter不适合那些“零错误”的应用场合。
     *     而在能容忍低错误率的应用场合下，Bloom Filter通过极少的错误换取了存储空间的极大节省。
     *
     *
     *  Bloom-Filter的应用: http://blog.csdn.net/hguisu/article/details/7866173
     */
    @Test
    public void bloomFilter() {
        BloomFilter<User> friends = BloomFilter.create(new Funnel<User>() {
            @Override
            public void funnel(User from, PrimitiveSink into) {
                into.putInt(from.getAge())
                        .putString(from.getUserName(), StandardCharsets.UTF_8);

            }
           }, 500, 0.01);

        //friends.mightContain()
    }

    /**
     * 问题实例】 给你A,B两个文件，各存放50亿条URL，每条URL占用64字节，内存限制是4G，让你找出A,B文件共同的URL。
     *     如果是三个乃至n个文件呢？
           根据这个问题我们来计算下内存的占用，4G=2^32大概是40亿*8大概是340亿bit，
           n=50亿，如果按出错率0.01算需要的大概是650亿个bit。
           现在可用的是340亿，相差并不多，这样可能会使出错率上升些。
           另外如果这些urlip是一一对应的，就可以转换成ip，则大大简单了。
     */


    /**
     * 斐波那契（Fibonacci）散列法
     * 1，对于16位整数而言，这个乘数是40503
       2，对于32位整数而言，这个乘数是2654435769
       3，对于64位整数而言，这个乘数是11400714819323198485
     */
    @Test
    public  void fibonacci(){
        int value = 353;
        long l = value * 2654435769l;
        int index = (int)l >> 28;
        System.out.println(index);
    }


    public static void main(String[] args) {
/*        int i = 100;
        System.out.println(100 & 2-1);
        System.out.println(100 & 4-1);
        System.out.println(100 & 8-1);
        System.out.println(100 & 16-1);
        System.out.println(100 & 32-1);
        System.out.println(100 & 64-1);*/


        int MAXIMUM_CAPACITY = 1 << 30;
        int number = 10000;
        System.out.println(number);
        int result = number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
        System.out.println(result);

        Map<String,String> map = new HashMap<>(10000);
        map.put("df","fd");
    }
}
