package dlt.study.redis.redisson.mapreduce;


import org.redisson.api.mapreduce.RCollector;
import org.redisson.api.mapreduce.RMapper;

import java.util.stream.Stream;
                                   //RCollectionMapper
public class WordMapper implements RMapper<String, String, String, Integer> { // Map

    @Override
    public void map(String key, String value, RCollector<String, Integer> collector) {
        String[] words = value.split("[^a-zA-Z]");
        for (String word : words) {
            collector.emit(word, 1);
        }
    }

    public static void main(String[] args) {
        String[] words = "hello world".split("[^a-zA-Z]");
        Stream.of(words).forEach(System.out::println);
    }

}