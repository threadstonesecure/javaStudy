package dlt.utils.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denglt on 16/10/24.
 */

public class URLUtils {


    /**
     * 按优先级获取URL里面的参数
     * 优先级:method、reference/service、default
     * 如果URL中存在Constants.METHOD_KEY，调用getMethodParameter
     *
     * @param url
     * @param key
     * @return
     */
    public static String getParameter(URL url, String key) {
        String methodName = url.getParameter(Constants.METHOD_KEY);
        if (StringUtils.isEmpty(methodName))
            return url.getParameter(key);
        else
            return url.getMethodParameter(methodName, key);
    }

    /**
     * 按优先级获取URL里面的参数(带默认参数)
     * 优先级:method、reference/service、default
     * 如果URL中存在Constants.METHOD_KEY，调用getMethodParameter
     *
     * @param url
     * @param key
     * @return
     */
    public static String getParameter(URL url, String key, String defaultValue) {
        String value = getParameter(url, key);
        if (StringUtils.isEmpty(value))
            return defaultValue;
        return value;
    }

    /**
     * 合并 reference/service、default 两个级别的参数
     * @param url
     * @param key
     * @return
     */
    public static List<String> getUnionParameter(URL url, String key){
        List<String> params = new ArrayList<>();
        String defalutValue =  url.getParameters().get(Constants.DEFAULT_KEY_PREFIX + key)   ;
        String serviceValue =  url.getParameters().get(key);
        if (!StringUtils.isEmpty(defalutValue))
            params.add(defalutValue);
        if (!StringUtils.isEmpty(serviceValue))
            params.add(serviceValue);
        return params;
    }

    /**
     * 仅获取Method级别的参数
     * @param url
     * @param key
     * @return
     */
    public static String getMethodParameter(URL url, String key){
        String methodName = url.getParameter(Constants.METHOD_KEY);
        if (StringUtils.isEmpty(methodName))
            return null;
        return url.getParameters().get(methodName+"." + key);
    }


    public static String toKeyStr(URL url) {
        return url.toFullString(Constants.METHOD_KEY,Constants.GROUP_KEY,Constants.VERSION_KEY);
    }

}