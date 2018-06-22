package dlt.study.spring.controll;



/*
import com.yuntai.med.support.auth.TokenInfo;
import com.yuntai.med.support.util.MedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


*/
/**
 * 功能：方便开发测试，绕过udb
 *
 * @Author:denglt 2017/01/23
 *//*

@Controller
@RequestMapping("/develop")
public class DeveloperController {



    @Autowired(required = false)
    @Qualifier("springSecurityFilterChain")
    private FilterChainProxy filterChainProxy;


    private OnceMattcher onceMattcher = new OnceMattcher();


    @PostConstruct
    public void init() {
        if (filterChainProxy != null)
            try {
                Field f = FilterChainProxy.class.getDeclaredField("filterChains");
                f.setAccessible(true);
                List<SecurityFilterChain> filterChains = (List<SecurityFilterChain>) f.get(filterChainProxy);
                DefaultSecurityFilterChain skipUdb = new DefaultSecurityFilterChain(onceMattcher, new ArrayList<Filter>());
                filterChains.add(0, skipUdb);
            } catch (Exception ex) {
                ex.printStackTrace();
                assert false : "can not insert into OnceMattcher";
            }

    }

    @RequestMapping("/do")
    public String execute(TokenInfo tokenInfo, String targetUrl) {
        MedContext.newInstance().setValue(MedContext.MedKey.UDB_TOKEN_INFO, tokenInfo);
        onceMattcher.setUrl(targetUrl);
        return "forward:" + targetUrl;
    }

    private class OnceMattcher implements RequestMatcher {

        private ThreadLocal<String> skipUdbUrl = new ThreadLocal<>();

        @Override
        public boolean matches(HttpServletRequest httpServletRequest) {
            String currUrl = httpServletRequest.getRequestURI();
            String skipUrl = skipUdbUrl.get();
            skipUdbUrl.remove();
            return skipUrl == null ? false : currUrl.endsWith(skipUrl);
        }

        public void setUrl(String url) {
            skipUdbUrl.set(url);
        }
    }
    
}
*/
