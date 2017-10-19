package dlt.study.lambda;

/**
 * 匿名类与Lambda 区别
 */
public class Hello {
    private String name;

    /*
    基于词法作用域的理念，lambda 表达式不可以掩盖任何其所在上下文中的局部变量，
    它的行为和那些拥有参数的控制流结构（例如 for 循环和 catch 从句）一致。

    对 this 的引用，以及通过 this 对未限定字段的引用和未限定方法的调用在本质上都属于使用 final 局部变量。
    包含此类引用的 lambda 表达式相当于捕获了 this 实例。在其它情况下，lambda 对象不会保留任何对 this 的引用
    这个特性对内存管理是一件好事：内部类实例会一直保留一个对其外部类实例的强引用，
    而那些没有捕获外部类成员的 lambda 表达式则不会保留对外部类实例的引用。要知道内部类的这个特性往往会造成内存泄露。
     */
    Runnable r1 = () -> {
        System.out.println(this);
        System.out.println(name);
        name = "hei";
    };
    Runnable r2 = new Runnable() {

        @Override
        public void run() {
            System.out.println(this);
            System.out.println(name);
        }
    };

    public Hello(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /*    public String toString() {
        return "Hello, world!";
    }*/

    public static void main(String... args) {
        Hello hello = new Hello("denglt");
        hello.r1.run();
        hello.r2.run();
        Hello hello1 = new Hello("zyy");
        hello.r1 = hello1.r1;
        hello.r2 = hello1.r2;
        hello.r1.run();
        hello.r2.run();
    }
}