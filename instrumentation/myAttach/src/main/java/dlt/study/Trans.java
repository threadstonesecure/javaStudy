package dlt.study;

public class Trans {

    public Trans(String name){
        this.name = name;
    }

    public void run( int sleepTime) throws Exception {
        Thread.sleep(1000 * sleepTime);
        System.out.println("Trans.run(int) on " + this);
    }

    public void run() throws Exception {
        Thread.sleep(3000);
        System.out.println("Trans.run() on " + this);
    }
    private String name = null;
    private static String cname ="hello world!";

}
