package dlt.utils.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

/**
 * Created by denglt on 2017/3/23.
 */

@RequestMapping(value = "/cachemanager")
@Controller
public class WebExCacheManager {

    private Map<String, AbstractCacheFactory> cacheFactorys = new HashedMap();

    public WebExCacheManager() {
        for (String cacheName : Constants.CACHE_NAMES) {
            CacheFactory cacheFactory = ExtensionLoader.getExtensionLoader(CacheFactory.class).getExtension(cacheName);
            if (cacheFactory instanceof AbstractCacheFactory) {
                cacheFactorys.put(cacheName, (AbstractCacheFactory) cacheFactory);
            }
        }
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public void list(HttpServletResponse resp) throws IOException {
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, AbstractCacheFactory> entry : cacheFactorys.entrySet()) {
            String title = entry.getKey();
            List<String> cacheInfo = new ArrayList<>();
            AbstractCacheFactory cacheFactory = entry.getValue();
            for (Map.Entry<String, Cache> cacheEntry : cacheFactory.getCaches().entrySet()) {
                cacheInfo.add(cacheEntry.getKey());
            }
            buf.append(list2String(title+":", cacheInfo));
        }
        PrintWriter out = resp.getWriter();
        out.println(buf.toString());
        out.flush();
        out.close();

    }

    @RequestMapping(value = "/clear")
    @ResponseBody
    public String clear(@RequestParam( value = "cacheKey") String cacheKey) {
        for (Map.Entry<String, AbstractCacheFactory> entry : cacheFactorys.entrySet()) {
            AbstractCacheFactory cacheFactory = entry.getValue();
            for (Map.Entry<String, Cache> cacheEntry : cacheFactory.getCaches().entrySet()) {
                if (cacheEntry.getKey().equals(cacheKey)) {
                    ExCache cache = (ExCache) cacheEntry.getValue();
                    cache.clear();
                    return "clear successfully!";
                }
            }
        }

        return "no found cache!";
    }

    private static String list2String(String title, List<String> contents) {
        StringBuilder buf = new StringBuilder(200);
        String newLine;

        Formatter formatter = new Formatter();
        try {
            newLine = formatter.format("%n").toString();
        } catch (Exception e) {
            // Should not reach here, but just in case.
            newLine = "\n";
        } finally {
            formatter.close();
        }

        buf.append(title + newLine);
        for (String content : contents) {
            buf.append(content + newLine);
        }

        return buf.toString();
    }
}
