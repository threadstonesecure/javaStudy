package dlt.study.annotation;

/**
 * Created by denglt on 2016/12/14.
 */

@MyAop
public interface IAop {

    @MyAop
    @NoInheritedAop
    void doAop();
}
