package dlt.study.hadoop.mr.db;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;

import java.io.IOException;
import java.util.List;


/**
 *
 * Created by denglt on 2017/4/24.
 */
public class MyDBInputFormat<MyDBWritable> extends DBInputFormat {

    @Override
    public List<InputSplit> getSplits(JobContext job) throws IOException {

        return super.getSplits(job);
    }

    @Override
    protected RecordReader createDBRecordReader(DBInputSplit split, Configuration conf) throws IOException {
        return super.createDBRecordReader(split, conf);
    }
}




