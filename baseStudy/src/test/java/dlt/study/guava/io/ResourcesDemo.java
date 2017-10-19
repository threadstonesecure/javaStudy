package dlt.study.guava.io;

import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.junit.Test;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ResourcesDemo {

    @Test
    public void read() throws Exception{
        URL url = new URL("http://www.baidu.com");
        CharSource charSource = Resources.asByteSource(url).asCharSource(StandardCharsets.UTF_8);
        charSource.readLines().forEach(System.out::println);
    }
}
