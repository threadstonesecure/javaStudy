package dlt.web.controller;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;

/**
 * @Description:
 * @Package: dlt.study.springcloud.restapi
 * @Author: denglt
 * @Date: 2018/11/14 5:00 PM
 * @Copyright: &#x7248;&#x6743;&#x5f52; HSYUNTAI &#x6240;&#x6709;
 */

@RestController
public class EnvController implements EnvironmentAware {

    private Environment environment;  // spring 没有把PropertyPlaceholderConfigurer配置的属性放入到Environment中
                                      // 网上说@PropertySource配置的属性，可以从Environment中获取

    private Properties properties = new Properties();

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        System.out.println("Environment -> " + environment); // StandardServletEnvironment
        MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .forEach(propName -> properties.setProperty(propName,
                        environment.getProperty(propName) == null ? "null" : environment.getProperty(propName)));

        properties.setProperty("active.profiles", Arrays.toString(environment.getActiveProfiles()));

    }

    @RequestMapping(value = "/env", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String env(String name) {
        String property = environment.getProperty(name);
        return name + " ->" + property;
    }

    @RequestMapping(value = "/allenv", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map env() {
        return properties;
    }
}
