package dlt.study;

import java.util.Random;

public class TransRunner {

    public static void main(String[] args) throws Exception {
        Trans trans = new Trans("denglt");
        System.out.println(trans);
        Random random = new Random(100);
        while (true) {
            trans.run(random.nextInt(2));
            trans.run();
            Thread.sleep(1000);
        }

    }
}
