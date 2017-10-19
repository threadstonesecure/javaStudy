package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        List<? extends Fruit> flist = new ArrayList<Apple>();
/*        flist.add(new Apple());
        flist.add(new Fruit());
        flist.add(null);*/

        List<? super Fruit> flist2 = new ArrayList<>();
        flist2.add(new Fruit());
        flist2.add(new Apple());
        flist2.add(new RedApple());
    }

}

class Food implements Serializable {
    int i;


}

class Fruit extends Food {
}

class Apple extends Fruit {
}

class RedApple extends Apple {
}