package dlt.study.hadoop.mr.db;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * InputSplit 会传递到Map Node，并调用InputForamt.createRecordReader来创建RecordReader,
 * 故需要实现Writable接口
 * Created by denglt on 2017/4/24.
 */
public class MyDBInputSplit extends InputSplit implements Writable {

    public MyDBInputSplit() {
    }

    @Override
    public long getLength() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public String[] getLocations() throws IOException, InterruptedException {
        return new String[0];
    }

    @Override
    public void write(DataOutput out) throws IOException {

    }

    @Override
    public void readFields(DataInput in) throws IOException {

    }
}
