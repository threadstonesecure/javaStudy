package dlt.study.hadoop;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

public class HDFSDemo {
	private static final String HADOOP_URL="hdfs://172.16.110.133:9000/user/error.log";

    @Test
	public static void main2() throws IOException {
		
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        final URL url=new URL(HADOOP_URL);
        final InputStream in = url.openStream();
        OutputStream out=new FileOutputStream("hello.txt");
        IOUtils.copyBytes(in, out, 1024,true);
        out.close();
        in.close();		
		/*readFile("/user/error.log");*/
	}

	 public static void main(String[] args) throws IOException {
		readFile("/user/error.log");
	}
	 public static void readFile(String filePath) throws IOException{

         Configuration conf = new Configuration();
         conf.set("fs.defaultFS", "hdfs://172.16.110.134:9000");
         System.out.println("fs.defaultFS="+conf.get("fs.defaultFS"));
         
         System.out.println(conf);
         FileSystem fs = FileSystem.get(conf);
         Path srcPath = new Path(filePath);
         InputStream in = null;
         try {
             in = fs.open(srcPath);
             IOUtils.copyBytes(in, System.out, 4096, false); //复制到标准输出流
         } finally {
             IOUtils.closeStream(in);
         }
     }
}
