/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dlt.study.hadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;


public class WordCount {

  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    @Override
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String pathName = ((FileSplit) context.getInputSplit()).getPath().toString(); // 输入的文件

      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);  //如果有ReducerTask,这儿的write为中间文件输出，由MR框架处理;否则由OutputFormat.RecordWriter来输出。@see MapTask 742行

      }
    }
  }
  
  public static class IntSumReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    args = new String[2];
    args[0] = "/test/input/ddl_owner_rmgz_kf.sql";
    args[1] = "/test/output2";
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length < 2) {
      System.err.println("Usage: wordcount <in> [<in>...] <out>");
      System.exit(2);
    }
    //Job job = new Job(conf, "word count");  旧方法 JobConf job = new JobConf();
    Job  job = Job.getInstance(conf, "word count");
    //job.setInputFormatClass();  // 默认 TextInputFormat
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class); // 这儿的Reducer在Map阶段执行，使用的Output应该与MapTask的一样(与ReduceTask是有差别的)；也可以指定不同的Reducer。
                                               //Combiner的Reducer的要求<KEYIN,VALUEIN>的类型应该与<KEYOUT,VALUEOUT>的类型一致
    job.setReducerClass(IntSumReducer.class);

/*  job.setMaxMapAttempts();;
    job.setMaxReduceAttempts();
    job.setNumReduceTasks(); // 设置 Reduce数量  默认启动一个Reduce
    job.setPartitionerClass(); 设置分区类，默认为HashPartitioner，如果启动的reduce数量小于指定的分区号，会报错*/
/*    job.setSortComparatorClass();*/
    //job.setGroupingComparatorClass();

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    //job.setOutputFormatClass(); // 默认：TextOutputFormat

    //job.setInputFormatClass(); // 默认：TextInputFormat
    for (int i = 0; i < otherArgs.length - 1; ++i) {
      FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
    }
    FileOutputFormat.setOutputPath(job,
      new Path(otherArgs[otherArgs.length - 1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

}
