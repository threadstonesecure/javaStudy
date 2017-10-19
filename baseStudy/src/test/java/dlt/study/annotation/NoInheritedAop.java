package dlt.study.annotation;

import java.lang.annotation.*;

/**
 * Created by denglt on 2016/11/30.
 */

@Target(value={ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoInheritedAop {
}
