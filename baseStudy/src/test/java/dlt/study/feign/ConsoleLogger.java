package dlt.study.feign;

import feign.Logger;

/**
 * @Description:
 * @Package: dlt.study.feign
 * @Author: denglt
 * @Date: 2019/1/17 10:56 AM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class ConsoleLogger extends Logger {

    @Override
    protected void log(String configKey, String format, Object... args) {
        System.out.println(String.format(methodTag(configKey) + format, args));
    }
}
