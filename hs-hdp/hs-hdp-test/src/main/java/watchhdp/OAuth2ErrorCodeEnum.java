package watchhdp;

public enum OAuth2ErrorCodeEnum {

	CLIENT_NOT_FOUND("610010000100001", "【认证鉴权服务器】找不到clientId"), // 错误信息,APP不用提示
	CLIENT_INVALID("610010000100002", "【认证鉴权服务器】clientId无效"), UNICODE_NOT_FOUND("610010000100003", "【认证鉴权服务器】找不到unicode"), // 触发终端信息上报接口，不跳转
	UNICODE_INVALID("610010000100004", "【认证鉴权服务器】unicode无效"), // 触发终端信息上报接口，并跳转登录页面

	SIGNATURE_ERROR("610010000100005", "【认证鉴权服务器】签名加密异常"), // 错误信息,APP不用提示
	SIGNATURE_PRIVATE_KEY_NOT_FOUND("610010000100006", "【认证鉴权服务器】签名加密私钥不存在"), // 错误信息,APP不用提示
	SIGNATURE_TIME_INVALID("610010000100007", "【认证鉴权服务器】签名加密时间戳非法"), // 错误信息,APP不用提示，触发终端信息上报接口

	SERVER_INNER_ERROR("610010000100008", "【认证鉴权服务器】内部异常"), // 错误信息,APP不用提示

	TOKEN_VALUE_NOT_FOUND("610010000100009", "【认证鉴权服务器】token Value 不能为空"), // 触发跳转登录页面，不用提示

	// openId 不存在
	OPEN_ID_NOT_FOUND("610010000100029", "【授权服务器】openId不能为空"),
	//
	THIRD_BIND_NOT_FOUND("610010000100030", "【授权服务器】 第三方绑定记录不存在"), NO_PRIVILEGE("610010000100031", "【授权服务器】无权限的clientId,请检查配置"),

	INVALIDVERIFYCODE("600010000100032", "请输入正确的登录验证码!"), INVALIDAUTOLOGINCODE("610010000100033", "自动登录失败，请按照正常模式登录！"),

	TERMINALTYPE_NOT_FOUND("610010000100041", "terminalType不存在"),

	COOKIE_INVALID("610010000100035", "您好，信息被篡改，禁止访问！"),

	TOKEN_URL_UPDATE("610010000100017", "token所携带url变动,刷新令牌"),

	TOKEN_SERVICE_NOT_NULL("610010000100044", "tokenServices 不能为null", IllegalStateException.class),

	URL_NOT_FOUND("610010000100045", "获取访问接口url不能为空"),

	AUTHCODE_NOT_FOUND("610010000100060", "授权码信息不存在！"),
	
	INVALID_TOKEN("610010000100017", "【认证鉴权服务器】无效的token"),

	AUTH_PARAME_INVALID("610010000100018", "【认证鉴权服务器】授权参数异常"),

	AUTH_GRANT_FAILED("610010000100018", "【认证鉴权服务器】授权参数失败"),

	USER_NAME_NOT_FOUND("600010000100027", "【认证鉴权服务器】用户名或密码错误"),

	REFRESHTOKEN_NONE("610010000100034", "【认证鉴权服务器】Refresh Token没有记录 tokenId:{0}"),

	TOKEN_KICK("600010000100010", "该账号在其他地方登录"),

	INVALID_GRANT("610010000100012", "【认证鉴权服务器】无效的授权类型"),

	REFRESHTOKEN_EXPIRED("610010000100034", "刷新令牌过期,请重新登录!"),

	NOT_SUPPORT_REFRESH_TOKEN("610010000100072", "【认证鉴权服务器】不支持刷新令牌操作"),

	REFRESH_TOKEN_NOT_NULL("610010000100073", "【认证鉴权服务器】刷新令牌不能为空"),

	DOUBLE_POST("610010000100070", "并发请求刷新令牌，客户端无需处理！"),

	NOT_SUPPORT_LOGIN_TYPE("610010000100074", "不支持登录类型");

	private String code;
	private String message;
	private Class<? extends Throwable> clazz;

	OAuth2ErrorCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	OAuth2ErrorCodeEnum(String code, String message, Class<? extends Throwable> clazz) {
		this.code = code;
		this.message = message;
		this.clazz = clazz;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public static final OAuth2ErrorCodeEnum getOAuth2ErrorCodeEnum(Throwable throwable) {
		OAuth2ErrorCodeEnum[] enums = OAuth2ErrorCodeEnum.values();
		for (OAuth2ErrorCodeEnum codeEnum : enums) {
			Class<? extends Throwable> clazz = codeEnum.clazz;
			if (clazz != null && clazz.isInstance(throwable)) {
				return codeEnum;
			}
		}
		return OAuth2ErrorCodeEnum.SERVER_INNER_ERROR;
	}
}
