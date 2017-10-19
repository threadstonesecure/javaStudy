package dlt.study.spring.aop;

import java.lang.annotation.*;

/**
 * Created by denglt on 2016/11/30.
 */

@Target(value={ElementType.METHOD,ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAop {
}
