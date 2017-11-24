package dlt.study.redis.redisson.mapreduce;

import dlt.study.spring.JUnit4Spring;
import org.junit.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.mapreduce.RMapReduce;

import javax.inject.Inject;
import java.util.Map;

public class RedissonMR extends JUnit4Spring {

    @Inject
    RedissonClient redissonClient;

    @Test
    public void wordCount() {
        RMap<String, String> map = redissonClient.getMap("wordsMap");
        map.put("line1", "Alice was beginning to get very tired");
        map.put("line2", "of sitting by her sister on the bank and");
        map.put("line3", "of having nothing to do once or twice she");
        map.put("line4", "had peeped into the book her sister was reading");
        map.put("line5", "but it had no pictures or conversations in it");
        map.put("line6", "and what is the use of a book");
        map.put("line7", "thought Alice without pictures or conversation");

        RMapReduce<String, String, String, Integer> mapReduce
                = map.<String, Integer>mapReduce()
                .mapper(new WordMapper())
                .reducer(new WordReducer());

        // 统计词频
        Map<String, Integer> mapToNumber = mapReduce.execute();
        mapToNumber.forEach((k, v) -> System.out.println(k + "->" + v));


        // 统计字数
        //  Integer totalWordsAmount = mapReduce.execute(new WordCollator());
    }
}
