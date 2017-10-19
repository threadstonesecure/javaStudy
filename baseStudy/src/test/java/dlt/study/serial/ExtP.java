package dlt.study.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ExtP extends P implements Externalizable {

    private String name;
    private int    age;

    public ExtP(){}

    public ExtP(String name, int age){
        super(name,age);
        System.out.println("super => " + super.getClass());
        System.out.println(super.equals(this) );
        this.name = name;
        this.age  = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println(out.getClass());
        out.writeUTF(name + "-> zyy");
        out.writeInt(age * 3);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = in.readUTF();
        age  = in.readInt();
    }

    @Override
    public String toString() {
        return "ExtP{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}' + "\\\\" + super.toString();
    }
}
