package dlt.study.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Created by denglt on 2017/4/24.
 */
public class FileSystemDemo {

    @Test
    public void allFileSystem() {
        ServiceLoader<FileSystem> serviceLoader = ServiceLoader.load(FileSystem.class);
        for (FileSystem fs : serviceLoader) {
            System.out.println(fs.getScheme() + ":" + fs.getClass());
        }

    }


    @Test
    public void getFileSystemClass() throws IOException{
        Class fileSystemClass = FileSystem.getFileSystemClass("hdfs" , new Configuration());
        System.out.println(fileSystemClass);
    }

    @Test
    public void getDefaultFileSystem() throws IOException{
        FileSystem fileSystem = FileSystem.get(new Configuration());
        System.out.println(fileSystem.getClass());
    }

    @Test
    public void loacalPath(){
        Configuration configuration = new Configuration();
       // configuration.getLocalPath()
    }
}
