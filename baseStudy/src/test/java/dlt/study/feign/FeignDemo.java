package dlt.study.feign;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import feign.*;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.codec.StringDecoder;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Package: dlt.study.feign
 * @Author: denglt
 * @Date: 2019/1/16 10:35 AM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class FeignDemo {

    interface GitHub {
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @RequestLine("GET /repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo, @QueryMap Map<String, Object> queryMap);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @RequestLine("POST /repos/{owner}/{repo}/contributors")
        List<Contributor> postContributors(@Param("owner") String owner, @Param("repo") String repo, Map<String, Object> bodyMap);

    }

    public static class Contributor {
        String login;
        int contributions;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public int getContributions() {
            return contributions;
        }

        public void setContributions(int contributions) {
            this.contributions = contributions;
        }
    }

    public static class MyJsonDecoder extends StringDecoder {
        @Override
        public Object decode(Response response, Type type) throws IOException {
            String body = (String) super.decode(response, String.class);
            return JSON.parseObject(body, type);
        }
    }

    public static class MyJsonEncoder implements Encoder {
        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            template.body(JSON.toJSONString(object));
        }
    }

    @Test
    public void feign() {
        GitHub github = Feign.builder()
                .decoder(new MyJsonDecoder())
                .encoder(new MyJsonEncoder())
                //.client()
                // .errorDecoder()
                // .requestInterceptor()
                // .retryer()
                // .options()
                // .contract()
                .logger(new ConsoleLogger())
                .logLevel(Logger.Level.FULL)
                //.queryMapEncoder()
                .target(GitHub.class, "https://api.github.com");

        // Fetch and print a list of the contributors to this library.
        Map<String, Object> queryMap = Maps.newHashMap();
        queryMap.put("nowTime", new Date());
        queryMap.put("name", "denglt");
        queryMap.put("values", Lists.newArrayList("1", "2", "3"));
        List<Contributor> contributors = github.contributors("OpenFeign", "feign", queryMap);
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }

        github.postContributors("OpenFeign", "feign", queryMap); // 仅看日志
    }

    @Test
    public void hystrixFeign() {
        GitHub github = HystrixFeign.builder()
                .decoder(new MyJsonDecoder())
                .encoder(new MyJsonEncoder())
                //.client()
                // .errorDecoder()
                // .requestInterceptor()
                // .retryer()
                // .options()
                // .contract()
                .logger(new ConsoleLogger())
                .logLevel(Logger.Level.FULL)
                //.queryMapEncoder()
                .setterFactory(new MySetterFactory())
                .target(GitHub.class, "https://api.github.com",new MyFallbackFactory());
        List<Contributor> result = github.postContributors("OpenFeign", "feign", Maps.newHashMap());
        System.out.println("result =" + result); // Fallback
    }

    class MySetterFactory implements SetterFactory {

        @Override
        public HystrixCommand.Setter create(Target<?> target, Method method) {
            String groupKey = target.name();
            String commandKey = Feign.configKey(target.type(), method);
            return HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutEnabled(true)
                            .withExecutionTimeoutInMilliseconds(6000)
                            .withExecutionIsolationSemaphoreMaxConcurrentRequests(10) // default 10
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE) //为SEMAPHORE时，在调用线程执行run(),性能更好点
                    );
        }
    }

    class MyFallbackFactory implements FallbackFactory<GitHub> {
        @Override
        public GitHub create(Throwable cause) {
            System.out.println("error ->" + cause);
            return new GitHub() {
                @Override
                public List<Contributor> contributors(String owner, String repo, Map<String, Object> queryMap) {
                    return null;
                }

                @Override
                public List<Contributor> postContributors(String owner, String repo, Map<String, Object> bodyMap) {
                    return null;
                }
            };
        }
    }
}

