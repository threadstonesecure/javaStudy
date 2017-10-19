package dlt.study.hadoop.mr.db;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 如果有Reduce Tasks,MyDBWritable 还需要实现Writable接口
 * Created by denglt on 2017/4/24.
 */
public class MyDBWritable implements DBWritable, Writable {

    private String name;
    private int age;

    @Override
    public void write(PreparedStatement statement) throws SQLException {
        statement.setString(1, name);
        statement.setInt(2, age);
    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        name = resultSet.getString(1);
        age = resultSet.getInt(2);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        name = in.readUTF();
        age = in.readInt();
    }
}
