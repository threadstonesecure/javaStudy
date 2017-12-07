package watchhdp;

/**
 * 对接业务常量定义
 * 分为两类: 对接和推送
 * 对接: 云服务请求前置机/HIS, 业务常量命名以 ACCESS打头 (原误写的ACCESSS是错误的, 后续修正)
 * 推送：前置机/HIS请求云服务, 业务常量命名以HOSTOMED打头
 * 注意: 业务名称, 后续不在添加Bussiness作为后缀, 太长 又无用
 */
public interface IAccessTypeConstants {
    /*
     * 前置机提供给服务端查询接口处理日志的相关类
     */
    public static final String ACCESSS_LoggerQueryListBusinessImpl = "LoggerQueryList";       //前置机提供给服务端查询接口处理日志的相关类

    /*
     * 就诊人
     */
    public static final String ACCESSS_PatientCheckBusiness        = "PatientCheck";          //患者卡号校验
    public static final String ACCESSS_CreatPatCardBusiness        = "CreatPatCard";          //办理医院卡
    public static final String ACCESSS_PatCardQueryBusiness        = "PatCardQuery";          //患者卡号查询

    /*
     * 排班
     */
    public static final String ACCESSS_RemainQueryBusiness         = "RemainQuery";           //余号查询
    public static final String ACCESSS_TimeSlotRemainQueryBusiness = "TimeSlotRemainQuery";   //号源信息
    public static final String ACCESSS_RealTimeSchQueryBusiness    = "RealTimeSchQuery";      //获取实时排班数据

    /*
     * 预约
     */
    public static final String ACCESS_RegConfirm                   = "RegConfirm";            //预约确认
    public static final String ACCESS_RegPayConfirm                = "RegPayConfirm";         //预约-支付确认
    public static final String ACCESS_RegCancel                    = "RegCancel";             //取消预约
    public static final String ACCESS_RegCancelRefundConfirm       = "RegCancelRefundConfirm"; //取消预约-退款确认
    public static final String ACCESS_RegPayCheckBussiness         = "RegPayCheck";           //校验是否可支付
    public static final String ACCESS_RegSelectItemBussiness       = "RegSelectItem";         //预约选项
    public static final String ACCESS_RegPreSettlementBussiness    = "RegPreSettlement";      //预约预结算
    public static final String ACCESS_RegPreAlertBussiness         = "RegPreAlert";           //预约前提醒

    /*
     * 住院预约
     */
    public static final String ACCESS_AdmitRegBussiness            = "AdmitReg";              //住院预约-预约
    public static final String ACCESS_AdmitRegSelectItemBussiness  = "AdmitRegSelectItem";    //住院预约-预约选项
    public static final String ACCESS_AdmitCancelRegBussiness      = "AdmitCancelReg";        //住院预约-取消预约
    public static final String HOSTOMED_AdmitRegBussiness          = "HisAdmitRegAudit";      //住院预约-审核 (推送)

    /* 
     * 支付退款
     */
    public static final String ACCESS_RefundApplyBussiness         = "RefundApply";           // 退款申请
    public static final String ACCESS_PayRefundNoticeBussiness     = "PayRefundNotice";       // 退款通知

    /*
     *费用 
     */
    public static final String ACCESSS_FeeListBusiness             = "FeeList";               //费用列表查询
    public static final String ACCESSS_OutFeeViewBusiness          = "OutFeeView";            //门诊费用详情
    public static final String ACCESSS_HosFeeViewBusiness          = "HosFeeView";            //住院费用详情

    /*
     * 诊间支付
     */
    public static final String ACCESSS_UnpaidFeeListBusiness       = "UnpaidFeeList";         //诊间支付-待缴费用列表查询	
    public static final String ACCESSS_UnpaidFeeViewBusiness       = "UnpaidFeeView";         //诊间支付-待缴费用详情查询
    public static final String ACCESS_UnpaidFeePayCheckBusiness    = "UnpaidFeePayCheck";     //诊间支付-支付校验
    public static final String ACCESS_PayClinicConfirmBussiness    = "PayClinicConfirm";      //诊间支付-支付确认
    public static final String ACCESS_PayClinicCancelBussiness     = "PayClinicCancel";       //诊间支付-支付取消
    public static final String ACCESS_PayClinicSelectItemBusiness  = "PayClinicSelectItem";   //诊间支付-获取缴费类型
    public static final String ACCESS_PAYClinicRefundConfirm       = "ClinicRefundConfirm";    //诊间支付-退款确认通知

    /**
     * 业务扫码
     */
    public static final String ACCESS_ScanFeeViewBusiness          = "BizScanUnpaidFeeView";     //业务扫码-扫码费用查询
    public static final String ACCESS_ScanConfirmAccBussness       = "BizScanPayConfirm";        //增加业务

    /*
     * 充值
     */
    public static final String ACCESS_PatCardRecharge              = "PatCardRecharge";       //就诊卡充值
    @Deprecated
    public static final String ACCESS_PayRechargePaymentBussiness  = "PayRechargePayment";    //充值
    @Deprecated
    public static final String ACCESS_PayRechargeConfirmBussiness  = "PayRechargeConfirm";    //充值支付确认
    @Deprecated
    public static final String ACCESS_PayRechargeListBussiness     = "PayRechargeList";       //充值记录
    @Deprecated
    public static final String ACCESS_PayRechargeCheckBussiness    = "PayRechargeCheck";      //充值校验

    /*
     * 报告单
     */
    public static final String ACCESSS_CheckSheetListBusiness      = "CheckSheetList";        //检查单列表
    public static final String ACCESSS_CheckSheetViewBusiness      = "CheckSheetView";        //检查单详情
    public static final String ACCESSS_ExamineSheetListBusiness    = "ExamineSheetList";      //检验单列表
    public static final String ACCESSS_ExamineSheetViewBusiness    = "ExamineSheetView";      //检验单详情

    /*
     * 体检单
     */
    @Deprecated
    public static final String ACCESSS_ExamReportListBusiness      = "ExamReportList";        //体检报告列表
    @Deprecated
    public static final String ACCESSS_ExamReportViewBusiness      = "ExamReportView";        //体检报告详情
    public static final String ACCESSS_TjbgListBusiness            = "TjbgList";              //体检报告列表
    public static final String ACCESSS_TjbgViewBusiness            = "TjbgView";              //体检报告详情
    public static final String ACCESSS_TjbgCheckBusiness           = "TjbgCheck";             //体检报告查询前校验

    /*
     * 住院费用
     */
    public static final String ACCESS_ZyFeeList                    = "ZyFeeList";             //住院费用列表
    public static final String ACCESS_ZyFeeView                    = "ZyFeeView";             //住院费用详情
    public static final String ACCESS_Cyxj                         = "Cyxj";                  //出院小结

    /*
     * 排队叫号
     */
    public static final String ACCESSS_PatientQueueListBusiness    = "PatientQueueList";      //获取患者排队队列列表
    public static final String ACCESSS_PatientQueueViewBusiness    = "PatientQueueView";      //获取患者排队状态详细
    public static final String ACCESS_PushQueueViewBusiness        = "PushQueueView";         //叫号his推送规则
    public static final String ACCESSS_PatientQueuePayBusiness     = "PaySupport";            //获取患者排队状态详细

    /*
     * 在线诊疗
     */
    public static final String ACCESSS_OltSchRemainNoBusiness      = "OltSchRemainNo";        //在线诊疗-排班余号
    public static final String ACCESSS_OltSchNumSrcBusiness        = "OltSchNumSrc";          //在线诊疗-排班号源信息
    public static final String ACCESSS_OltRegConfirmBusiness       = "OltRegConfirm";         //在线诊疗-预约确认
    public static final String ACCESSS_OltCancelRegBusiness        = "OltCancelReg";          //在线诊疗-预约确认
    public static final String ACCESSS_OltPayRegConfirmBusiness    = "OltPayRegConfirm";      //在线诊疗-预约支付确认
    public static final String ACCESSS_OltConfirmBusiness          = "OltConfirm";            //在线诊疗-接诊确认

    /*
     * 第三方预约查询/支付(预约取号)
     */
    public static final String ACCESS_OprRegList                   = "OprRegList";            //第三方预约列表
    public static final String ACCESS_OprRegView                   = "OprRegView";            //第三方预约详情
    public static final String ACCESS_OprRegPayCheck               = "OprRegPayCheck";        //第三方预约支付校验
    public static final String ACCESS_OprRegPayConfirm             = "OprRegPayConfirm";      //第三方预约支付确认

    /*
     * 出生证明预约
     */
    public static final String ACCESS_BcRegSchList                 = "BcRegSchList";          //出生证明预约-排班列表
    public static final String ACCESS_BcRegSchNumSrcList           = "BcRegSchNumSrcList";    //出生证明预约-号源列表
    public static final String ACCESS_BcRegConfirm                 = "BcRegConfirm";          //出生证明预约-预约确认
    public static final String ACCESS_BcRegCancel                  = "BcRegCancel";           //出生证明预约-取消   

    /*
     * 处方单
     */
    public static final String ACCESSS_PrescriptionListBusiness    = "PrescriptionList";      //处方单列表
    public static final String ACCESSS_PrescriptionViewBusiness    = "PrescriptionView";      //处方单详情

    public static final String ACCESSS_MedicalRecordListBusiness   = "MedicalRecordList";     //就诊记录
    public static final String ACCESSS_DrugPayConfirmBusiness      = "DrugPayConfirm";        //药品支付确认
    public static final String ACCESSS_BalanceQueryBusiness        = "BalanceQuery";          //余额查询
    public static final String ACCESSS_AccessQueryBusiness         = "AccessQuery";           //对接通用查询接口  预留的透传接口
    public static final String ACCESS_DocSheetQueryBusiness        = "DocSheetQuery";         //医生开单查询 医生Web端使用
    public static final String ACCESS_EmrList                      = "EmrList";               //电子病历列表
    public static final String ACCESS_EmrView                      = "EmrView";               //电子病历详情
    public static final String ACCESS_ScanCodeQueryBusiness        = "ScanCodeQuery";         //扫码查看报告单/体检单等
    public static final String ACCESS_CurOperationBusiness         = "CurOperation";          //当前手术
    public static final String ACCESS_Comacs                       = "Comacs";                //透传
    public static final String ACCESS_ScanPayConfirm               = "ScanPayConfirm";        //扫码支付-支付确认
    public static final String ACCESS_ScanPayQuery                 = "ScanPayQuery";

    /**
     * 推送接口
     */
    public static final String HOSTOMED_CheckSheetPushBusiness     = "CheckSheetPush";        //检查单推送
    public static final String HOSTOMED_ExamineSheetPushBusiness   = "ExamineSheetPush";      //检验单推送
    public static final String HOSTOMED_PrescriptionPushBusiness   = "PrescriptionPush";      //处方单推送
    public static final String HOSTOMED_UnpaidFeePushBusiness      = "UnpaidFeePush";         //代缴费用推送
    public static final String HOSTOMED_MedicalRecordPushBusiness  = "MedicalRecordPush";     //就诊记录推送
    public static final String HOSTOMED_DiagConfirmPushBusiness    = "DiagConfirmPush";       //就诊确认推送
    public static final String HOSTOMED_WicketRefundBusiness       = "WicketRefund";          //窗口退号
    public static final String HOSTOMED_StopDiagBusiness           = "StopDiag";              //停诊

    public static final String HOSTOMED_CheckOrderQueryBusiness    = "CheckOrderQuery";       //对账查询 严格意义上讲 不属于推送， 虽然是HIS/对接发起的

    public static final String HOSTOMED_ThirdOrderQueryBusiness    = "ThirdOrderQuery";       //第三方账单查询，提供给his使用

    /**
     * 检查项目预约
     */
    public static final String ACCESSS_RegCheckItemListBusiness    = "RegCheckItemList";      //预约检查项目列表
    public static final String ACCESSS_CheckSchRealTimeBusiness    = "CheckSchRealTime";      //实时检查项目排班列表
    public static final String ACCESSS_CheckRegisterBusiness       = "CheckRegister";         //检查项目预约
    public static final String ACCESSS_CancelCheckRegisterBusiness = "CancelCheckRegister";   //检查项目取消预约

    public static final String ACCESSS_MedCateBusiness             = "MedCate";               //分类列表
    public static final String ACCESSS_MedListBusiness             = "MedList";               //医用物品列表
    public static final String ACCESSS_MedBusiness                 = "Med";                   //医用物品详情

    public static final String ACCESSS_SaveNarrateRecord           = "SaveNarrateRecord";     //保存、更新诊前主诉记录

    /**
     * 废弃, 使用 ACCESS_RegConfirm 替代
     * @author Jackie 2017-6-29 15:17:43
     */
    @Deprecated
    public static final String ACCESSS_OrderRegisterBusiness       = "OrderRegister";         //预约挂号
    /**
     * 废弃, 使用 ACCESS_RegPayConfirm 替代
     * @author Jackie 2017-6-29 15:17:49
     */
    @Deprecated
    public static final String ACCESS_PayRegConfirmBussiness       = "PayRegConfirm";         //预约-支付确认
    /**
     * 废弃, 使用 ACCESS_RegCancel 替代
     * @author Jackie 2017-6-29 15:17:56
     */
    @Deprecated
    public static final String ACCESSS_CancelRegisterBusiness      = "CancelRegister";        //取消预约

    /**
     * 废弃, 就诊提醒， 不该用对接规则来配置
     * @author Jackie
     */
    @Deprecated
    public static final String ACCESS_PayRegPushBussiness          = "PayRegPush";            //预约订单推送

    /**
     * 以下是未确定是否不再使用的常量
     */
    @Deprecated
    public static final String Clound_PreverBusiness               = "prever";                //前置机版本更新（监控程序使用）
    @Deprecated
    public static final String Clound_HeartBusiness                = "heart";                 //心跳同步  
    @Deprecated
    public static final String Clound_SecurityBusiness             = "security";              //加密测试

    /**
     * 第三方短信推送
     */
    public static final String ACCESS_ThirdSmsSendPushBussiness    = "ThirdSmsSendPush";      //第三方短信推送

    /**
     * 健康妇幼
     */
    public static final String ACCESS_JkfyIfCreatedRecord          = "JkfyIfCreatedRecord";   //健康妇幼校验是否已建档
    public static final String ACCESS_JkfyCreateRecord             = "JkfyCreateRecord";      //健康妇幼建档
    public static final String ACCESS_JkfyRegSchList               = "JkfyRegSchList";        //健康妇幼可预约排班列表
    public static final String ACCESS_JkfyCanRegHospital           = "JkfyCanRegHospital";    //健康妇幼可预约机构
    public static final String ACCESS_JkfyRegService               = "JkfyRegService";        //健康妇幼服务预约
    public static final String ACCESS_JkfyRegCancel                = "JkfyRegCancel";         //健康妇幼取消预约
    public static final String ACCESS_JkfyReportList               = "JkfyReportList";        //健康妇幼报告单列表
    public static final String ACCESS_JkfyReportDetails            = "JkfyReportDetails";     //健康妇幼报告单详情
    
    /**
     * 体检
     */
    public static final String ACCESS_TiJianOrderConfirm           = "TiJianOrderConfirm";       //体检预约下单
    public static final String ACCESS_TiJianOrderCancel            = "TiJianOrderCancel";        //体检取消订单
    public static final String ACCESS_TiJianStateQuery             = "TiJianStateQuery";         //体检是否到检
    public static final String ACCESS_TiJianReportPdf              = "TiJianReportPdf";          //获取体检报告pdf
    public static final String ACCESS_TiJianReportJson             = "TiJianReportJson";         //获取体检报告json
    public static final String ACCESS_TiJianTimeModify             = "TiJianTimeModify";         //体检预约改期
    public static final String ACCESS_TiJianSch                    = "TiJianSch";                //体检门店列表
    public static final String ACCESS_TiJianTaoCan                 = "TiJianTaoCan";             //查询体检套餐
    public static final String ACCESS_TiJianGroup                  = "TiJianGroup";              //查询组合项目
    public static final String ACCESS_TiJianTaoCanDetail           = "TiJianTaoCanDetail";       //查询套餐明细
    public static final String ACCESS_TiJianCompanyInfo            = "TiJianCompanyInfo";        //查询医院单位信息
}
