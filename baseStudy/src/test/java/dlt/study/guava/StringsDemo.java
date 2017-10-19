package dlt.study.guava;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.net.InternetDomainName;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StringsDemo {

    @Test
    public void joiner() {
        Joiner joiner = Joiner.on("; ").skipNulls();
        String str = joiner.join("Harry", null, "Ron", "Hermione");
        System.out.println(str);
        Joiner.on(",").join(Arrays.asList(1, 5, 7)); // returns "1,5,7"
        List<String> ss = Lists.newArrayList();
        System.out.println("".equals(joiner.join(ss)));
    }

    @Test
    public void splitter() {
        Splitter.on(',')
                .trimResults() // 除去value前后空格
                .omitEmptyStrings() // 除去null记录
                .split("foo,bar, ,   qux,").forEach(System.out::println);
        System.out.println("===========");
        Splitter.on(",,")
                .trimResults()
                .omitEmptyStrings()
                .split("foo,,bar,, ,   qux,").forEach(System.out::println);

        System.out.println("=================");
        Splitter.on(',')
                .trimResults(CharMatcher.is('_'))
                .split("_a ,_b_ ,c__")
                .forEach(System.out::println);// returns "a ", "b_ ", "c".

        System.out.println("=================");
        Splitter.fixedLength(3).limit(2)
                .split("1234567890").forEach(System.out::println);
    }


    @Test
    public void charMatcher(){
       System.out.println(CharMatcher.javaIsoControl().removeFrom("denglt邓隆通@##🉐️🉐️"));// 返回不符合条件的字符
       System.out.println(CharMatcher.digit().retainFrom("afdd121sfasd123345dfg")); // 返回符合条件的字符

        System.out.println(CharMatcher.whitespace().trimAndCollapseFrom(" den  glt ", '@'));
        System.out.println(CharMatcher.whitespace().collapseFrom(" den  glt ",'@'));
        System.out.println(CharMatcher.whitespace().replaceFrom(" my name is den glt ",'@'));
        System.out.println(CharMatcher.breakingWhitespace().replaceFrom(" my name is den glt ",'@'));

        System.out.println(CharMatcher.javaDigit().retainFrom("afdd121sfasd123345dfg"));

        System.out.println(CharMatcher.javaDigit().or(CharMatcher.javaLowerCase()).retainFrom("@#Deng445@#"));

        System.out.println(CharMatcher.singleWidth().removeFrom("denglt邓隆通@##"));

        CharMatcher.inRange('a', 'z');

    }

    /**
     *     Format	              Example
         LOWER_CAMEL	        lowerCamel
         LOWER_HYPHEN	        lower-hyphen
         LOWER_UNDERSCORE	    lower_underscore
         UPPER_CAMEL	        UpperCamel
         UPPER_UNDERSCORE	    UPPER_UNDERSCORE
     */
    @Test
    public void caseFormat(){
       System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "CONSTANT_NAME"));

        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));

        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testData"));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "testData"));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));
    }



}
