package watchhdp;

/**
 * @Description: REST接口返回错误码说明
 * @ClassName: ErrorCode.java
 * @Package: com.yuntai.med.support.constant
 * @Author: dingmj@hundsun.com
 * @Date: 2014年12月17日 下午6:23:56
 * @Copyright: 版权归 Hundsun 所有 <ModifyLog>
 * @ModifyContent: 将不规范的异常废弃， 不推荐使用， 后续会统一改掉
 * @Author: Miaoxj
 * @Date: </ModifyLog>
 */
public interface ErrorCode {

	/* 18.1 通用错误码（1XXX） */
	public static final String GENERAL_1001 = "1001"; // 未指定{0}参数
	public static final String GENERAL_1002 = "1002"; // 参数{0}的值{1}无效
	public static final String GENERAL_1003 = "1003"; // 请求正文中不存在{0}对象或属性
	public static final String GENERAL_1004 = "1004"; // 查询字符串中的起始值大于或等于结束值
	public static final String GENERAL_1005 = "1005"; // 参数{0}重复出现
	public static final String GENERAL_1006 = "1006"; // 无效的数据（{0}）
	public static final String GENERAL_1007 = "1007"; // 无效区域识别码{0}

	/* 18.2 身份鉴权（3XXX） */
	public static final String AUTH_3001 = "3001"; // 无效的U-Identity
	public static final String AUTH_3002 = "3002"; // 客户端版本不一致
	public static final String AUTH_3003 = "3003"; // 无效的UID（用户冻结或注销）
	public static final String AUTH_3004 = "3004"; // 用户地理信息不存在
	public static final String AUTH_3005 = "3005"; // 账户发生变更，请重新登录
	public static final String AUTH_3006 = "3006"; // 该账号在其他地方登录
	public static final String AUTH_3007 = "3007"; // 该资源用户无权限访问
	public static final String AUTH_3008 = "3008"; // Session已过期
	public static final String AUTH_3009 = "3009"; // 无效的Token
	public static final String AUTH_3010 = "3010"; // Token已过期
	public static final String AUTH_3011 = "3011"; // 无效的PTOKEN
	public static final String AUTH_3012 = "3012"; // 登录名或密码错误
	public static final String AUTH_3013 = "3013"; // Token即将要过期（12小时前预警）
	/** uid与token不匹配 */
	public static final String AUTH_3014 = "3014"; // uid与token不匹配
	/** 手机号发生变更，请重新登录 */
	public static final String AUTH_3015 = "3015"; // 手机号发生变更，请重新登录
	/** 您访问的频次过高 */
	public static final String AUTH_3016 = "3016"; // 您访问的频次过高
	
	//udb 2.0访问频次过高
	public static final String AUTH_600010000100035 = "600010000100035"; // 触发调用图片验证码接口，busiNo传入2，适用于全局拦截器拦截的功能
	
	public static final String AUTH_600010000100036 = "600010000100036"; // 触发调用图片验证码接口，busiNo传入1；（适用于登录接口的事件）
	
	public static final String AUTH_600010000100037 = "600010000100037"; // 您访问的频次过高，不调用图片验证码
	
	public static final String AUTH_3017 = "3017"; // 内测阶段，暂停服务（不在接口白名单中）

	/* 18.4 网络错误码（5XXX） */
	public static final String NET_5001 = "5001"; // 访问被拒绝
	public static final String NET_5002 = "5002"; // 网络异常或超时
	public static final String NET_5003 = "5003"; // 网络繁忙
	public static final String NET_5004 = "5004"; // 单发消息频率超出最大限制{0}
	public static final String NET_5005 = "5005"; // 批量消息频率超出最大限制{0}

	/* 18.5 系统错误码（6XXX） */
	public static final String SYS_6001 = "6001"; // 服务器当前处于只读模式
	public static final String SYS_6002 = "6002"; // 服务器内部错误
	public static final String SYS_6003 = "6003"; // 数据库发生异常
	public static final String SYS_6004 = "6004"; // 系统繁忙
	public static final String SYS_6005 = "6005"; // 访问资源不存在
	public static final String SYS_6006 = "6006"; // 请求格式错误
	public static final String SYS_6007 = "6007"; // 访问资源不存在

	/*
	 * 患者相关异常
	 */
	public static final String PAT_400401001 = "400401001"; // 患者不存在
	public static final String PAT_400401002 = "400401002"; // 患者名称必须在2-7个汉字
	public static final String PAT_400401003 = "400401003"; // 身份证不能为空
	public static final String PAT_400401004 = "400401004"; // 身份证格式不正确
	public static final String PAT_400401005 = "400401005"; // 手机号码不能为空
	public static final String PAT_400401006 = "400401006"; // 手机号码格式不正确
	public static final String PAT_400401007 = "400401007"; // 邮箱格式不正确
	public static final String PAT_400401009 = "400401009"; // 地址不能超过50个汉字
	public static final String PAT_400401010 = "400401010"; // 工作单位不能超过25个汉字
	public static final String PAT_400401011 = "400401011"; // 婚否设置不正确
	public static final String PAT_400401012 = "400401012"; // 当前用户已添加过该身份证患者
	public static final String PAT_400401013 = "400401013"; // 最多只能添加五个患者
	public static final String PAT_400401014 = "400401014"; // 本人/夫妻只能有一个
	public static final String PAT_400401015 = "400401015"; // 用户患者关系不正确
	public static final String PAT_400401016 = "400401016"; // 用户患者姓名不能空
	public static final String PAT_400401017 = "400401017"; // 患者姓名只允许中文和英文字符！
	public static final String PAT_400401018 = "400401018"; // 该身份证已存在
	public static final String PAT_400401019 = "400401019"; // 用户患者关系不能空
	public static final String PAT_400401020 = "400401020"; // 用户患者监护人姓名不能空
	public static final String PAT_400401021 = "400401021"; // 用户患者监护人身份证不能空
	public static final String PAT_400401022 = "400401022"; // 该患者姓名已存在
	public static final String PAT_400401023 = "400401023"; // 删除就诊人：就诊人ID错误，// 或者就诊人已被删除
	public static final String PAT_400401024 ="400401024";//请输入身份证号或监护人信息
	public static final String PAT_400401025 ="400401025";//出生日期不正确
	public static final String PAT_MSG_400401025 = "出生日期不正确"; // 出生日期不匹配
	public static final String PAT_400401026 ="400401026";//出生日期不正确
	public static final String PAT_MSG_400401026 = "无身份证子女就诊人年龄不可超过18周岁！"; // 出生日期不匹配
	public static final String PAT_400401027 ="400401027";//出生日期不正确
	public static final String PAT_MSG_400401027 = "添加就诊人累计次数已超限制，如有疑问请联系客服"; // 出生日期不匹配
	public static final String PAT_400401028 ="400401028";//出生日期不正确
	public static final String PAT_400401029 ="400401029";//性别设置不正确

	
	public static final String PAT_MSG_400401001 = "患者不存在"; // 患者不存在
	public static final String PAT_MSG_400401002 = "患者名称必须在2-7个汉字"; // 患者名称必须在2-7个汉字
	public static final String PAT_MSG_400401003 = "身份证不能为空 "; // 身份证不能为空
	public static final String PAT_MSG_400401004 = "身份证格式不正确  "; // 身份证格式不正确
	public static final String PAT_MSG_400401005 = "手机号码不能为空  "; // 手机号码不能为空
	public static final String PAT_MSG_400401006 = "手机号码格式不正确  "; // 手机号码格式不正确
	public static final String PAT_MSG_400401007 = "邮箱格式不正确"; // 邮箱格式不正确
	public static final String PAT_MSG_400401009 = "地址不能超过50个汉字"; // 地址不能超过50个汉字
	public static final String PAT_MSG_400401010 = "工作单位不能超过25个汉字  "; // 工作单位不能超过25个汉字
	public static final String PAT_MSG_400401011 = "婚否设置不正确  "; // 婚否设置不正确
	public static final String PAT_MSG_400401012 = "当前用户已添加过该身份证患者"; // 当前用户已添加过该身份证患者
	public static final String PAT_MSG_400401013 = "已达到可添加就诊人数上限，请先删除部分就诊人在进行添加。"; // 最多只能添加五个患者
	public static final String PAT_MSG_400401014 = "本人/夫妻关系就诊人只能添加一个"; // 本人/夫妻只能有一个
	public static final String PAT_MSG_400401015 = "用户患者关系不正确  "; // 用户患者关系不正确
	public static final String PAT_MSG_400401016 = "用户患者姓名不能空 "; // 用户患者姓名不能空
	public static final String PAT_MSG_400401017 = "患者姓名只允许中文和英文字符！"; // 患者姓名只允许中文和英文字符！
	public static final String PAT_MSG_400401018 = "该身份证已存在"; // 该身份证已存在
	public static final String PAT_MSG_400401019 = "用户患者关系不能空 "; // 用户患者关系不能空
	public static final String PAT_MSG_400401020 = "用户患者监护人姓名不能空 "; // 用户患者监护人姓名不能空
	public static final String PAT_MSG_400401021 = "请输入监护人的相关信息"; // 用户患者监护人身份证不能空
	public static final String PAT_MSG_400401022 = "该患者姓名已存在"; // 该患者姓名已存在
	public static final String PAT_MSG_400401022_1 = "该就诊人已存在，请重新添加"; // 该患者姓名已存在
	public static final String PAT_MSG_400401023 = "就诊人ID错误， 或者就诊人已被删除"; // 删除就诊人：就诊人ID错误，
	public static final String PAT_MSG_400401024 = "请输入身份证号或监护人信息"; 																	// 或者就诊人已被删除
	public static final String PAT_MSG_400401029 = "性别设置不正确";
	/*
	 * 患者卡号异常
	 */
	public static final String PAT_400402001 = "400402001"; // 就诊人ID或者就诊卡ID错误
	public static final String PAT_400402020 = "400402020"; // 就诊卡已认证过
	public static final String PAT_400402021 = "400402021"; // 卡号信息中手机号为空
	public static final String PAT_400402022 = "400402022"; // 没有找到相应的卡类型
	public static final String PAT_400402023 = "400402023"; // 患者不存在
	public static final String PAT_400402024 = "400402024"; // 绑卡: 该卡已经被当前就诊人绑定过
	public static final String PAT_400402025 = "400402025"; // 绑卡: 卡号校验功能建设中...
	public static final String PAT_400402026 = "400402026"; // 该就诊人已绑过医院账号，已在医院建档就诊人不可在线建档，如忘记就诊人账号，请持有效身份证件到医院服务窗口查询
	public static final String PAT_400402027 = "400402027"; // 该就诊人已做过在线建档，不可重复建档，如未领卡，请持有效证件去医院服务窗口进行身份核实并领卡
	public static final String PAT_400402028 = "400402028"; // 办理卡号 功能建设中，敬请期待
	public static final String PAT_400402029 = "400402029"; // 卡号查询 功能建设中，敬请期待
	public static final String PAT_400402030 = "400402030"; // 医院返回的手机号码不是标准的手机号码,无法做短信认证
	public static final String PAT_400402031 = "400402031"; // 绑卡数目超过限制
	public static final String PAT_400402033 = "400402033"; // 绑卡: 同一种类型的卡
	public static final String PAT_400402034 = "400402034";	// 该医院只支持绑定1个同类型账号，请先将原绑定账号解绑，再进行在线建档
	public static final String PAT_MSG_400402034="该医院只支持绑定1个同类型账号，请先将原绑定账号解绑，再进行在线建档";
	public static final String PAT_400402222 = "400402222"; // 绑卡：HIS/对接卡号校验出错
	public static final String PAT_400402333 = "400402333"; // 办卡：HIS/对接出错
	public static final String PAT_400402334 = "400402334"; //
	public static final String PAT_MSG_400402334 = "该就诊人在线建档后，尚未到医院办理身份核实及领卡业务，暂不能做认证！";
	public static final String PAT_400402335 = "400402335"; //
	public static final String PAT_MSG_400402335 = "就诊卡号需与身份证号一致，请核对信息后再试！";//卡号校验身份证作为卡输入错误
	public static final String PAT_400402336 = "400402336"; //
	public static final String PAT_MSG_400402336 = "卡号和对接患者ID不能同时为空";	//绑卡时卡号和对接患者ID不能同时为空

	public static final String PAT_400402337 = "400402337"; //
	public static final String PAT_MSG_400402337 = "仅支持证件类型为身份证的就诊人在线建档，请重新选择就诊人";	//仅支持证件类型为身份证的就诊人在线建档，请重新选择就诊人

	public static final String PAT_MSG_400402001 = "就诊人ID或者就诊卡ID错误 "; // 就诊人ID或者就诊卡ID错误
	public static final String PAT_MSG_400402020 = "就诊卡已认证过   "; // 就诊卡已认证过
	public static final String PAT_MSG_400402021 = "医院建档时未留有效手机号码，无法认证。请至医院窗口完善 "; // 卡号信息中手机号为空
	public static final String PAT_MSG_400402022 = "没有找到相应的卡类型 "; // 没有找到相应的卡类型
	public static final String PAT_MSG_400402023 = "患者不存在 "; // 患者不存在
	public static final String PAT_MSG_400402024 ="当前就诊人已绑定过同一卡号或同一ID的就诊卡"; // 绑卡:
																			// 该卡已经被当前就诊人绑定过
	public static final String PAT_MSG_400402025 = "卡号校验功能建设中...  "; // 绑卡:
																			// 卡号校验功能建设中...
	public static final String PAT_MSG_400402026 = "该就诊人已绑过医院账号，已在医院建档就诊人不可在线建档，如忘记就诊人账号，请持有效身份证件到医院服务窗口查询  "; // 该就诊人已绑过医院账号，已在医院建档就诊人不可在线建档，如忘记就诊人账号，请持有效身份证件到医院服务窗口查询
	public static final String PAT_MSG_400402027 = "该就诊人已做过在线建档，不可重复建档，如未领卡，请持有效证件去医院服务窗口进行身份核实并领卡   "; // 该就诊人已做过在线建档，不可重复建档，如未领卡，请持有效证件去医院服务窗口进行身份核实并领卡
	public static final String PAT_MSG_400402028 = "办理卡号 功能建设中，敬请期待 "; // 办理卡号
																		// 功能建设中，敬请期待
	public static final String PAT_MSG_400402029 = "卡号查询 功能建设中，敬请期待 "; // 卡号查询
																		// 功能建设中，敬请期待
	public static final String PAT_MSG_400402030 = "医院返回的手机号码不是标准的手机号码,无法做短信认证 "; // 医院返回的手机号码不是标准的手机号码,无法做短信认证
	public static final String PAT_MSG_400402031 = "绑卡数目超过限制  "; // 绑卡数目超过限制
	public static final String PAT_MSG_400402033 = "当前就诊人已绑定过同一类型的就诊卡"; 
	public static final String PAT_MSG_400402222 = "绑卡：HIS/对接卡号校验出错  "; // 绑卡：HIS/对接卡号校验出错
	public static final String PAT_MSG_400402333 = "办卡：HIS/对接出错 "; // 办卡：HIS/对接出错
    
    public static final String PAT_400402338     = "600160040220002";
    public static final String PAT_MSG_400402338 = "该账号已不存在";

	/*
	 * 排班异常
	 */
	public static final String SCH_400601001 = "400601001"; // 排班：余号功能建设中，敬请期待
	public static final String SCH_400601002 = "400601002"; // 排班：号源功能建设中，敬请期待
	public static final String SCH_400601003 = "400601003"; // 排班：排班无效
	public static final String SCH_MSG_400601001 = "余号功能建设中，敬请期待"; // 排班：余号功能建设中，敬请期待
	public static final String SCH_MSG_400601002 = "号源功能建设中，敬请期待"; // 排班：号源功能建设中，敬请期待
	public static final String SCH_MSG_400601003 = "排班无效 "; // 排班：排班无效

	/*
	 * 预约异常
	 */
	public static final String REG_400701000 = "400701000"; // 排班无效
	public static final String REG_400701001 = "400701001"; // 患者无效
	public static final String REG_400701002 = "400701002"; // 排班已结束
	public static final String REG_400701003 = "400701003"; // 用户当日预约次数超过限制
	public static final String REG_400701004 = "400701004"; // 用户当日取消预约次数超限
	public static final String REG_400701005 = "400701005"; // 患者重复预约
	public static final String REG_400701006 = "400701006"; // 预约功能建设中，敬请期待
	public static final String REG_400701007 = "400701007"; // 预约：错误的卡号ID
	public static final String REG_400701008 = "400701008"; // 预约：该卡未进行短信认证，请认证后再进行操作
	public static final String REG_400701009 = "400701009"; // 当日预约时间限制
	public static final String REG_400701010 = "400701010"; // 无卡预约提示
	public static final String REG_400701011 = "400701011"; // 当前身份证号已经预约过该次排班，不允许再次预约
	public static final String REG_400701012 = "400701012"; // 校验是否可支付功能建设中，敬请期待
	public static final String REG_400701013 = "400701013"; // 校验是否可支付对接未返回结果
	public static final String REG_400701014 = "400701014"; // 预约时间格式有误
	public static final String REG_400701015 = "400701015"; // 该预约记录无法删除
	public static final String REG_400701016 = "400701016"; // 预约支付功能建设中，敬请期待
	
	public static final String REG_MSG_400701000 = "排班无效 "; // 排班无效
	public static final String REG_MSG_400701001 = "患者无效"; // 患者无效
	public static final String REG_MSG_400701002 = "排班已结束"; // 排班已结束
	public static final String REG_MSG_400701003 = "用户当日预约次数超过5次,不允许预约 "; // 用户当日预约次数超过限制
	public static final String REG_MSG_400701003_2 = "用户当日预约次数超过10次,不允许预约 "; // 用户当日预约次数超过限制
	public static final String REG_MSG_400701004 = "用户当日取消预约/爽约超过3次，不允许再次预约"; // 用户当日取消预约次数超限
	public static final String REG_MSG_400701005 = "该患者已经预约当前排班，请到个人中心查看处理"; // 患者重复预约
	public static final String REG_MSG_400701006 = "预约功能建设中，敬请期待"; // 预约功能建设中，敬请期待
	public static final String REG_MSG_400701007 = "预约：错误的卡号ID"; // 预约：错误的卡号ID
	public static final String REG_MSG_400701008 = "该卡未进行短信认证，请认证后再进行操作 "; // 预约：该卡未进行短信认证，请认证后再进行操作
	public static final String REG_MSG_400701009 = "当日预约时间限制"; // 当日预约时间限制
	public static final String REG_MSG_400701010 = "无卡预约提示 "; // 无卡预约提示
	public static final String REG_MSG_400701011 = "当前身份证号已经预约过该次排班，不允许再次预约        "; // 当前身份证号已经预约过该次排班，不允许再次预约
	public static final String REG_MSG_400701012 = "校验是否可支付功能建设中，敬请期待"; // 校验是否可支付功能建设中，敬请期待
	public static final String REG_MSG_400701013 = "校验是否可支付对接未返回结果"; // 校验是否可支付对接未返回结果
	public static final String REG_MSG_400701014 = "预约时间格式有误 "; // 预约时间格式有误
	public static final String REG_MSG_400701015 = "该预约记录无法删除"; // 该预约记录无法删除
	public static final String REG_MSG_400701016 = "预约支付功能建设中，敬请期待 "; // 预约支付功能建设中，敬请期待

	public static final String REG_400701017 = "400701017"; // 用户进入黑名单提示
	public static final String REG_MSG_400701017 = "由于多次取消，您已被列入黑名单，无法继续预约！ "; 
	
	public static final String REG_400701018 = "400701018"; // 用户进入黑名单提示
	public static final String REG_MSG_400701018= "您已多次加入黑名单，不可再预约，请联系400-893-3900电话进行人工解除！ "; 

	public static final String REG_6006007014201 = "6006007014201";
    public static final String REG_MSG_6006007014201 = "预约取消成功，但退款失败，请重试。";
    
    public static final String REG_6006007013101 = "6006007013101"; // 1.0预约老数据
    public static final String REG_MSG_6006007013101 = "取消失败，请前往窗口退号。";
	/*
	 * 取消预约异常
	 */
	public static final String REG_400702000 = "400702000"; // 查不到对应的预约信息
	public static final String REG_400702002 = "400702002"; // 取消预约功能建设中，敬请期待
	public static final String REG_400702003 = "400702003"; // 就诊日期为当天的预约 不能取消
	public static final String REG_400702004 = "400702004"; // 爽约次数超过限制，不允许取消
	public static final String REG_400702005 = "400702005"; // 预约状态:
															// 只允许取消"已预约"状态的预约
	public static final String REG_400702006 = "400702006"; // 支付状态：只允许取消“未支付”和“已支付”的预约
	public static final String REG_400702007 = "400702007"; // 取消预约：错误的卡号ID
	public static final String REG_400702008 = "400702008"; // 取消预约：超时未支付的预约禁止App手动取消
	public static final String REG_400702009 = "400702009"; // 取消预约：该预约已过期不允许取消
	public static final String REG_400702010 = "400702010"; // 取消预约: 取消预约失败
	public static final String REG_400702011 = "400702011"; // 取消预约: 调用支付失败
	public static final String REG_400702012 = "400702012"; // 取消的预约不能重复取消
	public static final String REG_400702013 = "400702013"; // 就诊日期当天不允许取消预约
	public static final String REG_MSG_400702000 = "查不到对应的预约信息"; // 查不到对应的预约信息
	public static final String REG_MSG_400702002 = "取消预约功能建设中，敬请期待 "; // 取消预约功能建设中，敬请期待
	public static final String REG_MSG_400702003 =  "不能取消就诊日期为当日的预约"; // 就诊日期为当天的预约 不能取消
	public static final String REG_MSG_400702004 = "用户当日取消预约/爽约超过3次，不允许再次取消预约"; // 爽约次数超过限制，不允许取消
	public static final String REG_MSG_400702005 = "预约状态不正确"; 
	public static final String REG_MSG_400702006 = "支付状态：只允许取消“未支付”和“已支付”的预约"; // 支付状态：只允许取消“未支付”和“已支付”的预约
	public static final String REG_MSG_400702007 = "取消预约：错误的卡号ID"; // 取消预约：错误的卡号ID
	public static final String REG_MSG_400702008 = "超时未支付的预约不允许手动取消"; // 取消预约：超时未支付的预约禁止App手动取消
	public static final String REG_MSG_400702009 = "取消预约：该预约已过期不允许取消"; // 取消预约：该预约已过期不允许取消
	public static final String REG_MSG_400702010 = "取消预约失败"; // 取消预约: 取消预约失败
	public static final String REG_MSG_400702011 = "取消预约: 调用支付失败 "; // 取消预约: 调用支付失败
	public static final String REG_MSG_400702012 = "该订单已取消/退号，请刷新挂号单详情查看/确认"; // 取消的预约不能重复取消
	public static final String REG_MSG_400702013 = "就诊日期当天不允许取消预约"; // 就诊日期当天不允许取消预约
	/* 线上诊疗 */
	/* 401901 ： 线上诊疗-排班 */
	public static final String OLT_401901001 = "401901001"; // 线上诊疗-排班:余号功能建设中，敬请期待
	public static final String OLT_401901002 = "401901002"; // 线上诊疗-排班:号源功能建设中，敬请期待
	public static final String OLT_401901003 = "401901003"; // 线上诊疗-排班:在线诊疗排班ID有误
	public static final String OLT_MSG_401901001 = "余号功能建设中，敬请期待   "; // 线上诊疗-排班:余号功能建设中，敬请期待
	public static final String OLT_MSG_401901002 = "号源功能建设中，敬请期待   "; // 线上诊疗-排班:号源功能建设中，敬请期待
	public static final String OLT_MSG_401901003 = "在线诊疗排班ID有误"; // 线上诊疗-排班:在线诊疗排班ID有误
	/* 401902 ： 线上诊疗-预约 */
	public static final String OLT_401902001 = "401902001"; // 线上诊疗-预约: 未找到对应的患者
	public static final String OLT_401902002 = "401902002"; // 线上诊疗-预约: 线上诊疗人数已满
	public static final String OLT_401902003 = "401902003"; // 线上诊疗-预约: 预约确认未开启
	public static final String OLT_401902004 = "401902004"; // 线上诊疗-预约: 不在预约时间
	public static final String OLT_401902005 = "401902005"; // 线上诊疗-预约: 未找到对应就诊卡
	public static final String OLT_401902006 = "401902006"; // 线上诊疗-预约: 卡号未认证，不允许预约
	public static final String OLT_401902007 = "401902007"; // 线上诊疗-预约: 请勿重复预约
	public static final String OLT_401902100 = "401902100"; // 线上诊疗-预约: 预约保存失败
	public static final String OLT_401902101 = "401902101"; // 线上诊疗-预约: 预约更新失败
	public static final String OLT_MSG_401902001 = "未找到对应的患者"; // 线上诊疗-预约: 未找到对应的患者
	public static final String OLT_MSG_401902002 = "线上诊疗人数已满"; // 线上诊疗-预约: 线上诊疗人数已满
	public static final String OLT_MSG_401902003 = "预约确认未开启"; // 线上诊疗-预约: 预约确认未开启
	public static final String OLT_MSG_401902004 = "不在预约时间"; // 线上诊疗-预约: 不在预约时间
	public static final String OLT_MSG_401902005 = "未找到对应就诊卡"; // 线上诊疗-预约: 未找到对应就诊卡
	public static final String OLT_MSG_401902006 = "卡号未认证，不允许预约"; // 线上诊疗-预约: 卡号未认证，不允许预约
	public static final String OLT_MSG_401902007 = "请勿重复预约 "; // 线上诊疗-预约: 请勿重复预约
	public static final String OLT_MSG_401902100 = "预约保存失败 "; // 线上诊疗-预约: 预约保存失败
	public static final String OLT_MSG_401902101 = "预约更新失败"; // 线上诊疗-预约: 预约更新失败
	public static final String OLT_401902102 = "401902102";
    public static final String OLT_MSG_401902102 = "未找到相应的在线诊疗预约记录";
    public static final String OLT_401902103 = "401902103";
    public static final String OLT_MSG_401902103 = "无法取消该在线诊疗预约记录";
    
	/* 401907 ： 线上诊疗-评论 */
	public static final String OLT_401907001 = "401907001"; // 线上诊疗-评论：评论失败
	public static final String OLT_MSG_401907001 = "评论失败"; // 线上诊疗-评论：评论失败
	/* 医院模块异常 */
	public static final String HOS_400501001 = "400501001"; // 分享医生失败
	public static final String HOS_400501002 = "400501002"; // 医生不存在
	public static final String HOS_400501003 = "400501003"; // 医院二维码信息有误
	public static final String HOS_400501004 = "400501004"; // 医院信息有误
	public static final String HOS_400501005 = "400501005"; // 分享医生模板不存在
	public static final String HOS_400501006 = "400501006"; // 关注医生失败
	public static final String HOS_400501007 = "400501007"; // 取消关注失败
	public static final String HOS_400501008 = "400501008"; // nextDayTime参数配置有误
	public static final String HOS_MSG_400501001 = "分享医生失败"; // 分享医生失败
	public static final String HOS_MSG_400501002 = "医生不存在"; // 医生不存在
	public static final String HOS_MSG_400501003 = "医院二维码信息有误"; // 医院二维码信息有误
	public static final String HOS_MSG_400501004 = "医院信息有误"; // 医院信息有误
	public static final String HOS_MSG_400501005 = "分享医生模板不存在"; // 分享医生模板不存在
	public static final String HOS_MSG_400501006 = "关注医生失败 "; // 关注医生失败
	public static final String HOS_MSG_400501007 = "取消关注失败"; // 取消关注失败
	public static final String HOS_MSG_400501008 = "nextDayTime参数配置有误"; // nextDayTime参数配置有误
	/* 对接查询 */
	public static final String ACS_400901001 = "400901001"; // 功能建设中，敬请期待
	public static final String ACS_400901002 = "400901002"; // 患者无效
	public static final String ACS_400901003 = "400901003"; // 患者无卡号信息
	public static final String ACS_400901004 = "400901004"; // 卡号未校验通过
	public static final String ACS_400901005 = "400901005"; // 卡号未认证
	public static final String ACS_400901010 = "400901010"; // 费用查询，解析模板出错
	public static final String ACS_MSG_400901001 = "功能建设中，敬请期待 "; // 功能建设中，敬请期待
	public static final String ACS_MSG_400901002 = "患者无效"; // 患者无效
	public static final String ACS_MSG_400901003 = "患者无卡号信息"; // 患者无卡号信息
	public static final String ACS_MSG_400901004 = "卡号未校验通过 "; // 卡号未校验通过
	public static final String ACS_MSG_400901005 = "卡号未认证"; // 卡号未认证
	public static final String ACS_MSG_400901010 = "费用查询，解析模板出错"; // 费用查询，解析模板出错

	/* 网上购药相关异常 */
	public static final String HOS_201001 = "201001"; // 处方单不存在
	public static final String HOS_201002 = "201002"; // 处方单配药详情不存在
	public static final String HOS_201003 = "201003"; // 处方单药品匹配失败
	public static final String HOS_201004 = "201004"; // 药品配送失败
	public static final String HOS_201005 = "201005"; // 购药记录创建失败
	public static final String HOS_201006 = "201006"; // 该处方单类型不支持配药
	public static final String HOS_201007 = "201007";
	public static final String HOS_201008 = "201008";
	public static final String HOS_201009 = "201009";
	public static final String HOS_201010 = "201010";
	public static final String HOS_MSG_201001 = "处方单不存在"; // 处方单不存在
	public static final String HOS_MSG_201002 = "处方单配药详情不存在 "; // 处方单配药详情不存在
	public static final String HOS_MSG_201003 = "处方单药品匹配失败 "; // 处方单药品匹配失败
	public static final String HOS_MSG_201004 = "药品配送失败 "; // 药品配送失败
	public static final String HOS_MSG_201005 = "购药记录创建失败   "; // 购药记录创建失败
	public static final String HOS_MSG_201006 = "该处方单类型不支持配药"; // 该处方单类型不支持配药
	public static final String HOS_MSG_201007 = "201007";
	public static final String HOS_MSG_201008 = "201008";//
	public static final String HOS_MSG_201009 = "201009";
	public static final String HOS_MSG_201010 = "201010";

	/* 密码重置相关异常 */
	public static final String RESET_LIMIT_301001 = "301001"; // 密码重置成功次数受限
	public static final String RESET_LIMIT_301002 = "301002"; // 密码重置失败次数受限
	public static final String RESET_SECUQS_301003 = "301003"; // 安全问题答案错误
	public static final String RESET_PATNAME_301004 = "301004"; // 患者姓名错误

	/* 搜索相关异常 */
	public static final String LUCENE_600101001 = "600101001"; // 搜索字数超长

	/**
	 * 系统设置异常码
	 */
	public static final String STSTEM_400201001 = "400201001";// 分享内容没有找到
	public static final String STSTEM_400201002 = "400201002";// 关于我们的资源没有找到
	public static final String STSTEM_400201003 = "400201003";// 用户意见反馈提交失败
	public static final String STSTEM_400201004 = "400201004";// 用户手机号码不正确
	public static final String STSTEM_400201005 = "400201005";// 对应的版本信息没有找到
	public static final String STSTEM_400201006 = "400201006";// 当前版本已经是最新的版本
	public static final String STSTEM_400201007 = "400201007";// 输入的版本号有误
	public static final String STSTEM_400201008 = "400201008";// 该医院尚未准备好满意度调查模版，请稍候再评
	public static final String STSTEM_400201009 = "400201009";// 亲，您已参与过本次满意度调查活动，谢谢您的配合!
	public static final String STSTEM_400201010 = "400201010";// 输入的升级来源有误
	public static final String STSTEM_400201011 = "400201011";// 用户邮箱格式不正确
	public static final String STSTEM_400201012 = "400201012";// 用户意见提交内容过长
	public static final String STSTEM_400201013 = "400201013";// 满意度意见提交内容过长
	public static final String STSTEM_400201014 = "400201014";// 未配置 APP 首页模板
	public static final String STSTEM_400201015 = "400201015";// 首页模板未配置功能按钮

	public static final String STSTEM_MSG_400201001 = "分享内容没有找到";// 分享内容没有找到
	public static final String STSTEM_MSG_400201002 = "关于我们的资源没有找到";// 关于我们的资源没有找到
	public static final String STSTEM_MSG_400201003 = "用户意见反馈提交失败";// 用户意见反馈提交失败
	public static final String STSTEM_MSG_400201004 = "请输入正确的手机号码";// 用户手机号码不正确
	public static final String STSTEM_MSG_400201005 = "对应的版本信息没有找到 ";// 对应的版本信息没有找到
	public static final String STSTEM_MSG_400201006 = "当前版本已经是最新的版本";// 当前版本已经是最新的版本
	public static final String STSTEM_MSG_400201007 = "输入的版本号有误";// 输入的版本号有误
	public static final String STSTEM_MSG_400201008 = "该医院尚未准备好满意度调查模版，请稍候再评";// 该医院尚未准备好满意度调查模版，请稍候再评
	public static final String STSTEM_MSG_400201009 = "亲，您已参与过本次满意度调查活动，谢谢您的配合!	";// 亲，您已参与过本次满意度调查活动，谢谢您的配合!
	public static final String STSTEM_MSG_400201010 = "输入的升级来源有误 ";// 输入的升级来源有误
	public static final String STSTEM_MSG_400201011 = "用户邮箱格式不正确";// 用户邮箱格式不正确
	public static final String STSTEM_MSG_400201012 = "用户意见提交内容过长 ";// 用户意见提交内容过长
	public static final String STSTEM_MSG_400201013 = "满意度意见提交内容过长 ";// 满意度意见提交内容过长
	public static final String STSTEM_MSG_400201014 = "未配置 APP 首页模板";
	public static final String STSTEM_MSG_400201015 = "首页模板未配置功能按钮";

	/*
	 * 优惠券相关异常
	 */
	public static final String COUPON_402401001 = "402401001";
	public static final String COUPON_MSG_402401001 = "不存在该优惠券，请确认！";
	public static final String COUPON_402401002 = "402401002";
	public static final String COUPON_MSG_402401002 = "该时间点不允许领取优惠券，请在规定时间领取！";
	public static final String COUPON_402401003 = "402401003";
	public static final String COUPON_MSG_402401003 = "该优惠券数量已发放完毕，请下次再来！";
	public static final String COUPON_402401004 = "402401004";
	public static final String COUPON_MSG_402401004 = "今天该优惠券数量已发放完毕，请明天再来！";
	public static final String COUPON_402401005 = "402401005";
	public static final String COUPON_MSG_402401005 = "您已领过该优惠券，不能重复领取！";
	public static final String COUPON_402401006 = "402401006";
	public static final String COUPON_MSG_402401006 = "优惠券暂停领用";
	public static final String COUPON_402401007 = "402401007";
	public static final String COUPON_MSG_402401007 = "优惠券已被使用";
	/**
	 * 分诊导医相关异常
	 */
	public static final String RCMD_402501001 = "402501001"; // 功能暂未开通
	public static final String RCMD_MSG_402501001 = "功能暂未开通";
	/**
	 * 医生评论
	 */
	public static final String COMMENT_401201001 = "401201001";
	public static final String COMMENT_MSG_401201001 = "未找到有效的预约记录。";
	public static final String COMMENT_401201002 = "401201002";
	public static final String COMMENT_MSG_401201002 = "未找到有效的在线诊疗预约记录。";
	public static final String COMMENT_401201003 = "401201003";
	public static final String COMMENT_MSG_401201003 = "未到评价时间，请在就诊日期后进行评价。";
	public static final String COMMENT_401201004 = "401201004";
	public static final String COMMENT_MSG_401201004 = "该在线诊疗记录还未结束，无法评价。";
	public static final String COMMENT_401201005 = "401201005";
	public static final String COMMENT_MSG_401201005 = "该预约记录已取消或未就诊，无法评价。";
	public static final String COMMENT_401201006 = "401201006";
	public static final String COMMENT_MSG_401201006 = "该预约记录已点赞，请勿重复点赞。";

	/**
	 * 医责险相关异常
	 */
	public static final String INS_806201001 = "806201001";
	public static final String INS_MSG_806201001 = "服务端异常，请联系管理员";
	public static final String INS_806201002 = "806201002";
	public static final String INS_MSG_806201002 = "该医生已经领取过医责险，无需重复购买";
	public static final String INS_806201003 = "806201003";
	public static final String INS_MSG_806201003 = "该医生不是在线诊疗医生，没有领取权限";
	public static final String INS_806201004 = "806201004";
	public static final String INS_MSG_806201004 = "投保失败：";

	
	/**
     * 订单
     */
    public static final String ORDER_403100001 = "403100001";
    public static final String ORDER_MSG_403100001 = "订单创建请求失败。";
    
    public static final String ORDER_403100002 = "403100002";    //订单请求result=false

    public static final String ORDER_403100003 = "403100003";
    public static final String ORDER_MSG_403100003 = "创建订单记录有误。";
    
    public static final String ORDER_403100004 = "403100004";
    public static final String ORDER_MSG_403100004 = "该预约记录有误，无法支付。";
    
    public static final String ORDER_403100005 = "403100005";
    public static final String ORDER_MSG_403100005 = "该预约记录已超时。";
    
    public static final String ORDER_403100006 = "403100006";
    public static final String ORDER_MSG_403100006 = "创建订单记录失败(MLZF)。";
	
    
    /*
	 * 头参数拦截器相关异常
	 */
	public static final  String HEAD_100900101="100900101";//头参数clientID为空
	public static final  String HEAD_100900102="100900102";//头参数clientID格式不对
	public static final  String HEAD_100900103="100900103";//unicode为空
	public static final  String HEAD_100900104="100900104";//unicode格式不对
	public static final  String HEAD_100900105="100900105";//找不到公钥
	public static final  String HEAD_100900106="100900106";//数据传输有问题
	
	/**
	 * IM异常
	 */
	public static final String IM_900010001 = "900010002"; // 参数{0}的值{1}无效
	
	public static final  String ACCESS_REMINDER_CODE="400999998";
	public static final  String ACCESS_NOT_OPEN="400999999";
	public static final  String ACCESS_NOT_OPEN_MSG="功能建设中，敬请期待...";

	/**
	 * 检查预约异常
	 */
	public static final String CHECK_6006032010001 = "6006032010001";
    public static final String CHECK_MSG_6006032010001 = "无效的就诊人";
    public static final String CHECK_6006032010002 = "6006032010002";
    public static final String CHECK_MSG_6006032010002 = "该就诊卡无效";
    public static final String CHECK_6006032011001 = "6006032011001";
    public static final String CHECK_MSG_6006032011001 = "该检查项目记录有误";
	public static final String CHECK_6006032012001 = "6006032012001";
	public static final String CHECK_MSG_6006032012001 = "找不到对应的患者信息";
	public static final String CHECK_6006032012002 = "6006032012002"; // 所选排班已结束
	public static final String CHECK_6006032012003 = "6006032012003";
	public static final String CHECK_MSG_6006032012003 = "该患者已经预约过当前排班，不允许再次预约";
	public static final String CHECK_6006032012004 = "6006032012004";
	public static final String CHECK_MSG_6006032012004 = "当前身份证号已经预约过该次排班，不允许再次预约";
	public static final String CHECK_6006032012005 = "6006032012005";
	public static final String CHECK_MSG_6006032012005 = "用户当日预约超过10次，不允许再次预约";
	public static final String CHECK_6006032012006 = "6006032012006";
	public static final String CHECK_MSG_6006032012006 = "用户当日取消预约/爽约超过3次，不允许再次预约";
	public static final String CHECK_6006032012007 = "6006032012007";
	public static final String CHECK_MSG_6006032012007 = "功能建设中，敬请期待";
	public static final String CHECK_6006032012008 = "6006032012008";
	public static final String CHECK_MSG_6006032012008 = "找不到对应的检查预约";
	public static final String CHECK_6006032012009 = "6006032012009";
	public static final String CHECK_MSG_6006032012009 = "找不到对应的预约检查项目";
	
	public static final String PAY_6006035010101 = "6006035010101";
    public static final String PAY_MSG_6006035010101 = "pay_api_type和trade_status不配套。";
    public static final String PAY_6006035010102 = "6006035010102";
    public static final String PAY_MSG_6006035010102 = "支付已超时。";
    public static final String PAY_6006035010103 = "6006035010103";
    public static final String PAY_MSG_6006035010103 = "支付确认失败。";
    public static final String PAY_6006035010104 = "6006035010104";
    public static final String PAY_MSG_6006035010104 = "pay_api_type有误。";
	public static final String PAY_6006035011101 = "6006035011101";
    public static final String PAY_MSG_6006035011101 = "诊间支付订单数据有误。";
    public static final String PAY_6006035011102 = "6006035011102";
    public static final String PAY_MSG_6006035011102 = "未找到有效的自助缴费记录。";
    
    /**
     * 患者自述相关
     */
	public static final String PAT_WORDS_803401000 = "803401000";
	public static final String PAT_WORDS_MSG_803401000 = "新增患者自述失败";
	 /**
	  * 60430
     * 手术查询
     */
	public static final String SURGERY_REQ_EMPTY = "60430100001";
	public static final String SURGERY_RES_NO_ACCESS = "60430100002";
	public static final String SURGERY_RES_ANALYSIS = "60430100003";
	public static final String SURGERY_RES_ACCESS_ERROR = "60430100004";
	/**
	 * "60090  就诊人验证40060090x
     * 二维码扫描报告单
     * 
     */
	public static final String SCANREPORT_REQ_SCAN_EMPTY = "600160090400010";
	public static final String SCANREPORT_RES_ANALYSIS = "600160090400020";
	public static final String SCANREPORT_RES_NO_ACCESS = "600160090400030";
	public static final String SCANREPORT_RES_ACCESS_ERROR = "600160090400040";
	public static final String SCANREPORT_REQ_VIEW_EMPTY = "600160090450010";
	public static final String SCANREPORT_RES_VIEW = "600160090450020";
	public static final String SCANREPORT_VALIDATE_NO_PAT = "600160090400050";
	public static final String SCANREPORT_VALIDATE_NO_CARD = "600160090400060";
	public static final String SCANREPORT_VALIDATE_CARD_IS_PSV = "600160090400070";
	public static final String SCANREPORT_VALIDATE_PSV_TIMEOUT = "600160090400080";
	
	public static final String SCANREPORT_VALIDATE_NO_PAT_MSG = "您尚未在APP添加报告单中的患者为就诊人，无权查看报告单，请先添加就诊人并绑定对应的医院账号";
	public static final String SCANREPORT_VALIDATE_NO_CARD_MSG = "该就诊人尚未绑定并认证该医院账号，无权查看报告单，请先绑定对应的医院账号";
	public static final String SCANREPORT_VALIDATE_CARD_IS_PSV_MSG = "该就诊人尚未认证该医院账号，请先认证对应的医院账号";
	public static final String SCANREPORT_VALIDATE_PSV_TIMEOUT_MSG = "该就诊人账号认证已过期，无权查看，请先认证对应的医院账号";
	/**
	 * "60490  
     * 埋点统计
     * 
     */
	public static final String LOGBP_REQ_EMPTY = "600160490100010";
	public static final String LOGBP_RES_JSON_TRANSF_ERROR = "600160490100020";
	public static final String LOGBP_RES_DB_SAVE_ERROR = "600160490100030";

	/**
	 * 开处方单相关
	 */
    public static final String PRESCRIPTION_SAVE_ERROR = "600390120001001"; //保存处方失败
    public static final String PRESCRIPTION_REQUIRE_PARAM_ERROR = "600390120001002"; //缺少必填的处方单参数
    public static final String PRESCRIPTION_WRONG_VALUE_ERROR = "600390120001003"; //处方单属性值不正确
    public static final String PRESCRIPTION_NOT_FOUND = "600390120001004"; //找不到处方单
	public static final String PRESCRIPTION_ICD_ADD_EXIST_ERROR = "600390120001005";// 诊断已经存在
	public static final String PRESCRIPTION_ICD_ADD_NO_ICDNAME_ERROR = "600390120001006";// 诊断名称不能为空

}
