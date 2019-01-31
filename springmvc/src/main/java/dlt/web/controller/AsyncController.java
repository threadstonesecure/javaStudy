package dlt.web.controller;

import dlt.application.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;

@RestController
@RequestMapping(value = "/async")
public class AsyncController {

    protected static Logger logger = LoggerFactory.getLogger(AsyncController.class);


    @RequestMapping(value = "shortCallable", produces = "application/json;charset=UTF-8")
    public Callable<User> shortCallable() {
        logger.info("Entering controller");
        Callable<User> asyncTask = new UserTimeCaller(10000);
        logger.info("Leaving  controller");
        return asyncTask;
    }

    @RequestMapping("longCallable")
    public Callable<String> longCallable() {
        logger.info("Entering controller");
        Callable<String> asyncTask = new TimeCaller(60000);
        logger.info("Leaving  controller");
        return asyncTask;
    }

    @RequestMapping("longSync")
    @ResponseBody
    public String longSync() {
        logger.info("Entering controller");
        Callable<String> asyncTask = new TimeCaller(60000);
        String result = null;
        try {
            result = asyncTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Leaving  controller");
        return result;
    }

    @RequestMapping("shortSync")
    @ResponseBody
    public String shortSync() {
        logger.info("Entering controller");
        Callable<String> asyncTask = new TimeCaller(10000);
        String result = null;
        try {
            result = asyncTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Leaving  controller");
        return result;
    }

    @RequestMapping(value = "shortDeferred", produces = "application/json;charset=UTF-8")
    public DeferredResult<User> shortDeferredResult() {
        logger.info("Deferred time request -> Entering controller");
        DeferredResult<User> result = new DeferredResult<>();
        result.onTimeout(() -> logger.info("shortDeferredResult -> onTimeout"));
        result.onCompletion(() -> logger.info("shortDeferredResult -> onCompletion"));
        new Thread(new UserDeferred(10000, result), "MyThread").start();
        logger.info("Deferred time request -> Leaving  controller");
        return result;
    }

    @RequestMapping(value = "longDeferred", produces = "application/json;charset=UTF-8")
    public DeferredResult<User> longDeferredResult() {
        logger.info("Deferred time request -> Entering controller");
        DeferredResult<User> result = new DeferredResult<>(20000l, new User("time out"));
        result.onCompletion(() -> logger.info("longDeferredResult -> onCompletion"));
        result.onTimeout(() -> {
            logger.info("longDeferredResult -> onTimeout");
            // result.setErrorResult(new User("time out"));
        });
        new Thread(new UserDeferred(60000, result), "MyThread").start();
        logger.info("Deferred time request -> Leaving  controller");
        return result;
    }

    @RequestMapping(value = "shortWebAsync", produces = "application/json;charset=UTF-8")
    public WebAsyncTask<User> shortWebAsyncTask() {
      //  WebAsyncUtils.getAsyncManager()
     //   WebAsyncManager
        logger.info("Entering controller");
        Callable<User> asyncTask = new UserTimeCaller(10000);
        logger.info("Leaving  controller");
        return new WebAsyncTask<>(asyncTask);
    }

    /**
     * 处理timeoute
     *
     * @return
     */
    @RequestMapping(value = "longWebAsync", produces = "application/json;charset=UTF-8")
    public WebAsyncTask<User> longWebAsyncTask() {
        logger.info("Entering controller");
        Callable<User> asyncTask = new UserTimeCaller(60000);
        logger.info("Leaving  controller");
        WebAsyncTask<User> userWebAsyncTask = new WebAsyncTask<>(20000l, asyncTask);
        userWebAsyncTask.onCompletion(() -> logger.info("longWebAsync -> onCompletion"));
        userWebAsyncTask.onTimeout(() -> {
            logger.info("longWebAsync -> onTimeout");
            return new User("time out");
        });
        return userWebAsyncTask;
    }
}

class TimeCaller implements Callable<String> {

    private long sleepMillis;

    public TimeCaller(long sleepMillis) {
        this.sleepMillis = sleepMillis;
    }

    @Override
    public String call() throws Exception {
        AsyncController.logger.info("begin call() ...");
        Thread.sleep(sleepMillis);
        AsyncController.logger.info("end call() !");
        return "Hello World ! Thread[" + Thread.currentThread() + "]";
    }
}

class UserTimeCaller implements Callable<User> {

    private long sleepMillis;

    public UserTimeCaller(long sleepMillis) {
        this.sleepMillis = sleepMillis;
    }

    @Override
    public User call() throws Exception {
        AsyncController.logger.info("begin call() ...");
        Thread.sleep(sleepMillis);
        AsyncController.logger.info("end call() !");
        return new User("Hello World ! Thread[" + Thread.currentThread() + "]");
    }
}

class UserDeferred implements Runnable {

    private long sleepMillis;
    private DeferredResult deferredResult;

    public UserDeferred(long sleepMillis, DeferredResult deferredResult) {
        this.sleepMillis = sleepMillis;
        this.deferredResult = deferredResult;
    }

    @Override
    public void run() {
        AsyncController.logger.info("begin run() ...");
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AsyncController.logger.info("end run() !");
        deferredResult.setResult(new User("Hello World ! Thread[" + Thread.currentThread() + "]"));
    }
}