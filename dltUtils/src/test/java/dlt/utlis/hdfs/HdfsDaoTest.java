package dlt.utlis.hdfs;

import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import java.util.ServiceLoader;

/**
 * Created by denglt on 2017/4/24.
 */
public class HdfsDaoTest {

    @Test
    public void fileSystem() {
       /*		URI uri = URI.create("hdfs://172.16.108.210:8020");
        FileSystem fs = FileSystem.get(uri, new Configuration());
		System.out.println(fs);*/
        ServiceLoader<FileSystem> serviceLoader = ServiceLoader.load(FileSystem.class);
        for (FileSystem fs : serviceLoader) {
            System.out.println(fs.getScheme() + ":" + fs.getClass());
        }
        //DistributedFileSystem fileSystem;

    }
}
