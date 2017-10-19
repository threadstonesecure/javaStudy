package dlt.study.lambda;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import dlt.domain.model.User;
import org.junit.Test;

import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

public class LambdaDemo {

    @Test
    public void lambda() {
        FileFilter fileFilter = (file) -> file.getName().startsWith("denglt");
        System.out.println(fileFilter);
        System.out.println(fileFilter.getClass());
        System.out.println(fileFilter instanceof FileFilter);

    }

    @Test
    public void action() {
        ActionListener<String> al = (s) -> System.out.println(s);
        ActionListener<String> andAl = al.and((s) -> System.out.println("--->" + s));
        andAl.actionPerformed("denglt");

    }

    @Test
    public void stringList() {
        List<String> ss = Lists.newArrayList("denglt", "zyy");
        Stream<String> stream = ss.stream();
        Stream<User> userStream = stream.map(s -> new User(s));
        userStream.forEach((user) -> System.out.println(user));

        ss.stream().<User>map(s -> new User(s)).forEach(user -> System.out.println(user.getUserName()));
    }


    /**
     * Lambda与匿名类比较
     */
    @Test
    public void lambdaAndAnonymousClass() {

        String name = "denglt";
        List<String> aList = Lists.newArrayList();
        Supplier<Runnable> c = () -> () -> {
            System.out.println(this);
            System.out.println(LambdaDemo.this.toString());
            System.out.println("hi");
            System.out.println(name);
           // name = "hei"; is error
            aList.add("hei"); // lambda 表达式对值封闭，对变量开放的原文是：lambda expressions close over values, not variables
        };
        // name = "zyy"; is error
        Supplier<Runnable> cc = () -> new Runnable(){
            @Override
            public void run() {
                System.out.println(this);
                System.out.println(LambdaDemo.this.toString());
                System.out.println("hi");
                System.out.println(name);
                aList.add("hei");
            }
        };

        c.get().run();
        cc.get().run();
    }

    /**
     * 方法引用 Method references
     方法引用有很多种，它们的语法如下：
     静态方法引用：ClassName::methodName
     实例上的实例方法引用：instanceReference::methodName
     超类上的实例方法引用：super::methodName
     类型上的实例方法引用：ClassName::methodName
     构造方法引用：Class::new
     数组构造方法引用：TypeName[]::new
     */
    @Test
    public void methodRef(){
        User[] users = ObjectArrays.newArray(User.class,10);
        Comparator<User> byName = Comparator.comparing( u -> u.getUserName());
        Comparator<User> byName2 = Comparator.comparing(User::getUserName);  // 在这里我们可以用方法引用代替lambda表达式
        Arrays.sort(users,byName);
        Arrays.sort(users,byName2);
    }

    public void methodRef2(){
        Consumer<Integer> b1 = System::exit;    // void exit(int status)
        Consumer<String[]> b2 = Arrays::sort;    // void sort(Object[] a)
        Consumer<String> b3 = LambdaDemo::main;  // void main(String... args)

        Runnable r = new LambdaDemo()::run;        //
        Function<String, String> upperfier = String::toUpperCase;



        IntFunction<int[]> arrayMaker = int[]::new;
        int[] array = arrayMaker.apply(10); // 创建数组 int[10]


    }
    public void test2(){
        // Object o = () -> { System.out.println("hi"); }; 这段代码是非法的
        Object o = (Runnable) () -> { System.out.println("hi"); };
    }

    @Override
    public String toString() {
        return "LambdaDemo{}";
    }


    public void run(){
        System.out.println("run");
    }

    public String toUpperCase(String s) {
        return s.toUpperCase();
    }

    public LambdaDemo(){
        System.out.println("name is null!");
    }

    public  LambdaDemo(String name){
        System.out.println(name);
    }


    public static void main(String... args) {
        System.out.println("ok");
        Runnable r = new LambdaDemo()::run;        //
        r.run();
        Function<String, String> upperfier = String::toUpperCase;
        System.out.println(upperfier.apply("denglt"));
        Function<String, String> upperfier2 = new LambdaDemo()::toUpperCase;
        System.out.println(upperfier2.apply("denglt"));

        Function<String,LambdaDemo> newLambdaDemo = LambdaDemo::new;
        Supplier<LambdaDemo> newLambdaDemo2 = LambdaDemo::new;
        LambdaDemo lambdaDemo = newLambdaDemo.apply("denglt - > zyy");
        newLambdaDemo2.get();


    }
}
