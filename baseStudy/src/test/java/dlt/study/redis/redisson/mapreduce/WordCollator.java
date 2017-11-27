package dlt.study.redis.redisson.mapreduce;

import org.redisson.api.mapreduce.RCollator;

import java.util.Map;

public class WordCollator implements RCollator<String, Integer, Integer> {

    @Override
    public Integer collate(Map<String, Integer> resultMap) {
        int result = 0;
        for (Integer count : resultMap.values()) {
            result += count;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(test());
    }

    static String test() {

        try{
            int i =0;
            i = i/i;
            return "try";
        }catch (Exception e){
            e.printStackTrace();
            return "catch";
        }finally {
           // return "finally";
            System.out.println("finally");
        }
    }

}