package weixin.webapi;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import weixin.AppInfo;
import weixin.access.AccessToken;
import weixin.req.BaseRequest;
import weixin.req.RequestKind;
import weixin.resp.BaseResponse;

/**
 * Created by denglt on 2015/11/10.
 */

@Service
public class WxUtil {
	private static String token = "denglt";

	private static Log log = LogFactory.getLog(WxUtil.class);
	private static AccessToken accessToken;
	private static RestTemplate restTemplate;

	public static RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Resource
	public void setRestTemplate(RestTemplate restTemplate) {
		WxUtil.restTemplate = restTemplate;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	/**
	 * 签名验证
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean signature(String signature, String timestamp,
			String nonce) {
		MessageDigest md = null;
		String strDes = null;
		String[] params = { token, timestamp, nonce };
		Arrays.sort(params);
		StringBuffer sb = new StringBuffer();
		for (String param : params) {
			sb.append(param);
		}
		byte[] bt = sb.toString().getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
			return signature.equals(strDes) ? true : false;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return false;
		}

	}

	/**
	 * 获取访问Token
	 *
	 * @return
	 */
	public static String getAccessToken() {
		if (accessToken == null) {
			synchronized (WxUtil.class) {
				if (accessToken == null
						|| (System.currentTimeMillis() - accessToken
								.getUpdateTime()) / 1000 >= Long
								.parseLong(accessToken.getExpires_in()) - 5 * 60) {
					String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
					url = String.format(url, AppInfo.getAppId(),
							AppInfo.getAppSecret());
					accessToken = restTemplate.getForObject(url,
							AccessToken.class);
					accessToken.setUpdateTime(System.currentTimeMillis());
				}
			}
		}
		return accessToken.getAccess_token();
	}

    /**
     * 转换request为Bean:BaseReqeust
     * @param reqStr
     * @return
     * @throws DocumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
	public static BaseRequest transformRequest(String reqStr)
			throws DocumentException, IllegalAccessException,
			InvocationTargetException {
        System.out.println("WX请求:"+ reqStr);
		Document document = DocumentHelper.parseText(reqStr);
		Element root = document.getRootElement();
		List<Element> elements = root.elements();
		Map<String, String> contents = new HashMap<>();
		for (Element element : elements) {
			String key = element.getName();
			key = StringUtils.uncapitalize(key);
			contents.put(key, element.getText());
		}
		String msgType = contents.get("msgType");
		RequestKind kind = RequestKind.getType(msgType);
		BaseRequest msg = kind.newMessage();
		BeanUtils.populate(msg, contents); // map => bean
		return msg;
	}

    /**
     * 生成BaseResponse,并根据BaseRequest填充BaseResponse的基础信息
     * @param request
     * @param aClass
     * @param <T>
     * @return
     */
	public static <T extends BaseResponse> T createResponse(BaseRequest request,
			Class<T> aClass) {
		try {
			T response = aClass.newInstance();
			response.setCreateTime(System.currentTimeMillis());
			response.setFromUserName(request.getToUserName());
			response.setToUserName(request.getFromUserName());
			return response;
		} catch (Exception ex) {
			log.info(ex);
		}
		return null;
	}

    /**
     * 转换BaseResponse为xml字符串
     * @param response
     * @return
     */
    public static String toXML(BaseResponse response){
        try {
            JAXBContext jc = JAXBContext.newInstance(response.getClass());
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JAXBElement jaxbElement = new JAXBElement(
                    new QName("xml"), response.getClass(), response);
            marshaller.marshal(jaxbElement, outputStream);
            String xml =  outputStream.toString("UTF-8");
            return xml;
        }catch (Exception ex){
            log.info(ex);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

		BaseResponse response = new BaseResponse();
		BeanUtils.setProperty(response, "msgType", "text");
		System.out.println(response);
		String xml = WxUtil.toXML(response);
		System.out.println(xml);
	}
}
