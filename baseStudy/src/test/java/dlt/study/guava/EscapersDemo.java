package dlt.study.guava;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.google.common.html.HtmlEscapers;
import org.junit.Test;

public class EscapersDemo {

    @Test
    public void htmlEscaper(){
        Escaper escaper = HtmlEscapers.htmlEscaper();
        System.out.println(escaper.escape("<html>asdfdsf</html>"));
    }

    public void builder(){
        Escapers.builder()
                //.setSafeRange()
                //.setUnsafeReplacement()  // 替换字符串，替换不在SafeRange范围内的char，
                .addEscape('"', "&quot;")
                .addEscape('\'', "&#39;")
                .addEscape('&', "&amp;")
                .addEscape('<', "&lt;")
                .addEscape('>', "&gt;")
                .build();
    }
}
