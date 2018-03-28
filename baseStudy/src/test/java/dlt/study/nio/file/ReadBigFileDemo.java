package dlt.study.nio.file;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Stream;

public class ReadBigFileDemo {

    private String fileName = "/tmp/";

    @Test
    public void bad() throws Exception {
        Files.readLines(new File(fileName), StandardCharsets.UTF_8);
    }

    @Test
    public void scanner() throws Exception {
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(fileName);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // System.out.println(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    @Test
    public void apacheIO() throws Exception {
        File file = new File(fileName);
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    @Test
    public void reader() throws Exception {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            // to do String
        }

        Stream<String> lines = br.lines();// 1.8 stream

    }
}
