package dlt.study.serial;

import com.alibaba.fastjson.JSON;
import dlt.study.comm.PayStatusEnum;
import org.junit.Test;

import java.io.*;

public class ObjectOutputStreamDemo {

    @Test
    public void writeObject() throws IOException, ClassNotFoundException {
        P p = new P("denglt", 200);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(p);
        oos.close();
        byte[] ps = bos.toByteArray();

        ByteArrayInputStream bin = new ByteArrayInputStream(ps);
        ObjectInputStream oin = new ObjectInputStream(bin);
        Object cloneObject = oin.readObject();
        System.out.println(cloneObject);
    }

    @Test
    public void writeExternal() throws IOException, ClassNotFoundException {
        ExtP p = new ExtP("denglt", 200);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(p);
        oos.close();
        byte[] ps = bos.toByteArray();

        ByteArrayInputStream bin = new ByteArrayInputStream(ps);
        ObjectInputStream oin = new ObjectInputStream(bin);
        Object cloneObject = oin.readObject();
        System.out.println(cloneObject);
    }

    @Test
    public void writeEnum() throws IOException,ClassNotFoundException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(PayStatusEnum.PAID);
        oos.close();
        byte[] ps = bos.toByteArray();

        ByteArrayInputStream bin = new ByteArrayInputStream(ps);
        ObjectInputStream oin = new ObjectInputStream(bin);
        Object cloneObject = oin.readObject();
        System.out.println(cloneObject.getClass());
        System.out.println(cloneObject == PayStatusEnum.PAID);
        System.out.println(cloneObject);
        System.out.println(PayStatusEnum.PAID);
    }

    @Test
    public void jsonEnum(){
        String  s  = JSON.toJSONString(PayStatusEnum.PAID);
        System.out.println(s);

/*        Object obj = JSON.parseObject(s,PayStatusEnum.class);
        System.out.println(obj);*/

        Pay pay = new Pay("denglt");
        pay.setName("denglt");
        pay.setPayStatus(PayStatusEnum.PAID);
        s  = JSON.toJSONString(pay);
        System.out.println(s);

        Pay pay1 = JSON.parseObject(s,Pay.class);
        System.out.println(pay1);
        System.out.println(pay.getPayStatus() == pay1.getPayStatus());
    }
}

class Pay{

    private String name ;
    private PayStatusEnum payStatus;

    public Pay(String name){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return "Pay{" +
                "name='" + name + '\'' +
                ", payStatus=" + payStatus +
                '}';
    }
}