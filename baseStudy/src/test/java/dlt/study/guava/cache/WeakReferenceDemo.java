package dlt.study.guava.cache;

import dlt.study.guava.graph.City;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakReferenceDemo {

    public static void main(String[] args) throws Exception {
        ReferenceQueue<City> referenceQueue = new ReferenceQueue<>();

        WeakReference<City> weakReference = new WeakReference<>(new City("asdfsd", "afdsf"), referenceQueue);

        System.gc();
        System.out.println(weakReference.get());
/*        Reference<? extends City> ref = referenceQueue.remove();
        System.out.println(ref);
        System.out.println(ref.get());*/
        Thread.sleep(100);
        Reference ref = referenceQueue.poll(); //After Object（City） finalize ，WeakReference is moved in ReferenceQueue
        System.out.println(ref);
        System.out.println(ref.get());


    }
}
