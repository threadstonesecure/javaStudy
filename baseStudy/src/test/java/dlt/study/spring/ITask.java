package dlt.study.spring;

import dlt.study.spring.aop.MyAop;

import java.util.concurrent.Future;

/**
 * Created by denglt on 2016/12/12.
 */
public interface ITask {


    //@MyAop
    void doTask();

    Future<String> doTask(String name);
}
