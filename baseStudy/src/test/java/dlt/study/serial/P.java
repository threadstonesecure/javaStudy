package dlt.study.serial;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by denglt on 2017/6/27.
 */
public class P implements Serializable {

    private static final long serialVersionUID = -4732228038179617839L;

    private   String name;

    private  transient int age;

    public P(){}

    public P(String name , int age){
        this.name = name;
        this.age  = age;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // 默认的序列化进程，就像写（读）所有的non-transient和 non-static字段
        //out.writeObject(name);
      //  out.writeUTF(name);
        out.writeInt(age*2);  // 自己处理transient字段
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // name = (String) in.readObject(); // 执行该句前后观察name
        //name = in.readUTF();
        age  = in.readInt(); // 执行该句前后观察age
    }

    @Override
    public String toString() {
        return "P{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
