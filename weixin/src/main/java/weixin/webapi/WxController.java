package weixin.webapi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.CoreService;
import weixin.req.BaseRequest;
import weixin.resp.BaseResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping(value = "/act")
public class WxController {

	private static Log log = LogFactory.getLog(WxController.class);

    @Resource
    private CoreService actionService;

	@Resource
	private WxUtil wxUtil;

	private boolean isEmpty(String str) {
		return (str == null || str.length() == 0) ? true : false;
	}

	@RequestMapping(method = { RequestMethod.GET })
	@ResponseBody
	public String execute(String signature, String timestamp, String nonce,
			String echostr) {
		log.info("收到验证信息");
		if (isEmpty(signature)) {
			return "parameter error!";
		}
		if (isEmpty(timestamp)) {
			return "parameter error!";
		}
		if (isEmpty(nonce)) {
			return "parameter error!";
		}
		if (isEmpty(echostr)) {
			return "parameter error!";
		}

		if (WxUtil.signature(signature, timestamp, nonce)) {
			return echostr;
		} else
			return "signature fail!";
	}

	@RequestMapping(method = { RequestMethod.POST })
	@ResponseBody
	public String execute(String signature, String timestamp, String nonce,
			String echostr, String noCheckSign, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (noCheckSign == null) {
			if (!WxUtil.signature(signature, timestamp, nonce)) {
				return "验证失败";
			}
		}
		StringBuffer sb = new StringBuffer();
		InputStream inputStream = request.getInputStream();
		InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		BaseRequest reqMessage = WxUtil.transformRequest(sb.toString());
        BaseResponse respMessage = actionService.process(reqMessage);
        System.out.println("请求响应:"+respMessage);
        if (respMessage == null){
            return null;
        }
        return WxUtil.toXML(respMessage);
	}
}
