package dlt.study.guava.io;


import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.io.*;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FilesDemo {

    public void toByteArray(){
      //  Files.toByteArray() // like    java.nio.file.Files.readAllBytes()

    }

    public  void write(){
       // Files.write()
      //  java.nio.file.Files.write()
    }


    /**
     * ByteSource and CharSource for reading
     * @throws IOException
     */
    @Test
    public void asByteSource() throws IOException{
        File file = new File("/tmp/temp.log");
        ByteSource byteSource = Files.asByteSource(file);
       // byteSource.sizeIfKnown()
        CharSource charSource = byteSource.asCharSource(StandardCharsets.UTF_8); // Files.asCharSource()
        charSource.readLines().forEach(System.out::println);
    }

    /**
     * ByteSink and CharSink for writing
     */
    @Test
    public void asByteSink() throws IOException{
        File file = new File("/tmp/temp.log");
        ByteSink byteSink = Files.asByteSink(file,FileWriteMode.APPEND);
        CharSink charSink = byteSink.asCharSink(StandardCharsets.UTF_8); // Files.asCharSink()
        charSink.writeLines(Splitter.on(";").split("denglt;邓隆通;denglt"));// 每次都会打开和关闭stream
        charSink.writeLines(Splitter.on(";").split("denglt;邓隆通;denglt"));
        charSink.writeLines(Splitter.on(";").split("denglt;邓隆通;denglt"));

    }

    @Test
    public void fileTreeTraverser() throws Exception{
        File file= new File("/") ;
        Files.fileTreeTraverser().breadthFirstTraversal(file).limit(20).forEach(System.out::println);
        System.out.println("=====children=====");
        Files.fileTreeTraverser().children(file).forEach(System.out::println);
    }
}
