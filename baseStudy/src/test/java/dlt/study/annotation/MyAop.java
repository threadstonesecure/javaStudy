package dlt.study.annotation;

import java.lang.annotation.*;

/**
 * Created by denglt on 2016/11/30.
 */

@Target(value={ElementType.METHOD,ElementType.TYPE})
@Inherited  // 这个继承仅在ElementType.TYPE级别有效，而且如果仅在接口上定义，实现类也看不到
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAop {
}
