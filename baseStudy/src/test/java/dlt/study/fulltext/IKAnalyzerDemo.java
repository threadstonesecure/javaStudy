package dlt.study.fulltext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;

/**
 * Created by denglt on 2017/4/13.
 */
public class IKAnalyzerDemo {

    private Analyzer analyzer;

    @Before
    public void init(){
        analyzer = new IKAnalyzer();
    }

    @Test
    public void analyzer() throws IOException{
        TokenStream tokenStream = analyzer.tokenStream("content","邓隆通");
        tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()){
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.end();
        tokenStream.close();
    }

}
