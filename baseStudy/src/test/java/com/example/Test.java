package com.example;

public class Test {

}


interface Target {
    // Adaptee能提供的电压110V
    void getPower110V() ;
    // 客户需要的电压220V
    void getPower220V() ;
}

class Adaptee {
    public void getPower110V() {
        System.out.println("get power: 110V");
    }
}

class Adapter extends Adaptee implements Target{
    public void getPower220V() {
        System.out.println("get power: 220V");
    }

    public void getPower110V() {
        System.out.println("get power: 110V");
    }
}

