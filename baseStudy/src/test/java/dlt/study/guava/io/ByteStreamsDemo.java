package dlt.study.guava.io;

import com.google.common.io.ByteStreams;
import org.junit.Test;

import java.io.InputStream;

public class ByteStreamsDemo {

    public void t(){
       // ByteStreams.copy();
       // ByteStreams.limit()
    }

    public void toByteArray(){
       // ByteStreams.toByteArray()
    }

    /**
     * like DataInput.readFully
     */
    public void readFully(){
      //  ByteStreams.readFully();
    }

    /**
     *
     */
    @Test
    void skipFully(){
        //ByteStreams.skipFully();

    }

    public void nullOutputStream(){
        ByteStreams.nullOutputStream();
    }
}
