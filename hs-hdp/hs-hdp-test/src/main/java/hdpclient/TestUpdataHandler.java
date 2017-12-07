package hdpclient;

import com.hundsun.med.access.mso.DrugEntity;
import com.hundsun.med.access.pres.PrescriptionInfoBean;
import com.hundsun.med.access.sheet.CheckSheetHTO;
import com.hundsun.med.access.sheet.ExamineSheetHTO;
import com.hundsun.med.access.sheet.ExamineSheetItemInfoBean;
import com.hundsun.med.hostomed.stopdiag.StopDiagEntity;
import com.hundsun.med.hostomed.stopdiag.StopDiagHTO;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;
import com.yuntai.hdp.client.Receiver;
import dlt.utils.JsonUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author denglt
 *
 */
public class TestUpdataHandler {

	//private static String ip =  "121.40.225.50"; // "127.0.0.1"
	private static String ip =  "127.0.0.1"; //
	private static int port = 8088;
	private static String hosId = "226";
	private static HdpClient hdpClient;

	@BeforeClass
	public static void init() {
		hdpClient = new HdpClient().remoteAddress(ip, port).hosId(hosId) // 设置医院ID
				.reconnectDelay(10) // 设置重连接间隔（秒）
				.ssl(true) // 启用ssl安全通讯
				.receiver(new Receiver() {
					@Override
					public void receive(RequestPack data) {
						System.out.println(data);
					}

					@Override
					public void receive(ResultPack data) {
						System.out.println(data);
					}
				}) // 设置业务数据接收器
				.connect(); // 连接HDP Server
	}

	@AfterClass
	public static void destory() throws Exception {
		hdpClient.close();
	}

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 检验单 0100
	 */
	@Test
	public void sendExamineSheet() throws Exception {
        ExamineSheetHTO examineSheet = new ExamineSheetHTO();
		examineSheet.setSheetId("123412341");
		examineSheet.setSheetName("北京人民医院检验报告单");
		examineSheet.setSubName("血常规");
		examineSheet.setCheckDate("2015-01-02");
		examineSheet.setDeptName("门诊部");
		examineSheet.setPatName("张三");
		examineSheet.setAge("20");
		examineSheet.setSex(0);
		examineSheet.setStatus(0);

		examineSheet.setDocName("李四");
		examineSheet.setVerifyDocName("王五");
		examineSheet.setRepDocName("郭六");
		examineSheet.setRepTime("2015-01-02");
		examineSheet.setVerifyTime("2015-01-02");

		List<ExamineSheetItemInfoBean> items = new ArrayList<ExamineSheetItemInfoBean>();
		ExamineSheetItemInfoBean item = new ExamineSheetItemInfoBean();
		item.setSheetId(examineSheet.getSheetId());
		item.setSheetItemId(examineSheet.getSheetId() + "_1");
		item.setItemName("白细胞");
		item.setItemEnName("");
		item.setStatus("↑");
		item.setIndicator(1);
		item.setValue("222");
		item.setUnit("g/ml");
		item.setValueRange("100-200");
		item.setValueMin("100");
		item.setValueMax("200");
		items.add(item);

		item = new ExamineSheetItemInfoBean();
		item.setSheetId(examineSheet.getSheetId());
		item.setSheetItemId(examineSheet.getSheetId() + "_2");
		item.setItemName("红细胞");
		item.setItemEnName("");
		item.setStatus("↓");
		item.setIndicator(1);
		item.setValue("99");
		item.setUnit("g/ml");
		item.setValueRange("100-200");
		item.setValueMin("100");
		item.setValueMax("200");
		items.add(item);
		examineSheet.setItems(items);
		RequestPack pack = new RequestPack();
		pack.setHosId(hosId);
		pack.setSeqno("111111");
		pack.setCmd("0100");
		String body = JsonUtils.toJson(examineSheet);
		System.out.println(body);
		pack.setBody(body);
		pack.setSendTime(System.currentTimeMillis());
		if (hdpClient.synSendData(pack)) {
			System.out.println("推送检验单成功");
		} else {
			System.out.println("推送检验单失败");
		}

		sleep(60000);
	}

	/**
	 * 推送检查单 0200
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendCheckSheet() throws Exception {
		CheckSheetHTO checkBean = new CheckSheetHTO();
		checkBean.setSheetId("123412341");
		checkBean.setSheetName("北京人民医院检查报告单");
		checkBean.setSubName("胸部CT");
		checkBean.setCheckDate("2015-01-02");
		checkBean.setDeptName("门诊部");
		checkBean.setPatName("张三");
		checkBean.setAge("20");
		checkBean.setSex(0);
		checkBean.setStatus(0);

		checkBean.setDocName("李四");
		checkBean.setVerifyDocName("王五");
		checkBean.setRepDocName("郭六");
		checkBean.setRepTime("2015-01-02");
		checkBean.setVerifyTime("2015-01-02");
		checkBean.setImpression("无异常");
		checkBean.setDescription("无异常");
		checkBean
				.setImgUrls(new String[] {
						"http://image.baidu.com/search/detail?z=0&ipn=d&word=%E8%80%81%E8%99%8E%E5%9B%BE%E7%89%87%E5%A4%A7%E5%85%A8&step_word=&pn=0&spn=0&di=0&pi=&tn=baiduimagedetail&istype=&ie=utf-8&oe=utf-8&cs=1197180483%2C3383266959&os=2944698852%2C309310993&adpicid=0&fr=ala&sme=&cg=&bdtype=14&simics=4127602212%2C2381039223&objurl=http%3A%2F%2Fxiaomila.cn%2Fuploadfile%2F2014%2F0412%2F20140412123804844.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bxtw54tsw_z%26e3BvgAzdH3Fv5gpjgp-dnb-9990-8_z%26e3Bip4s&gsm=0&cardserver=1",
						"http://image.baidu.com/search/detail?z=0&ipn=d&word=%E8%80%81%E8%99%8E%E5%9B%BE%E7%89%87%E5%A4%A7%E5%85%A8&step_word=&pn=4&spn=0&di=0&pi=&tn=baiduimagedetail&istype=&ie=utf-8&oe=utf-8&cs=2355778319%2C214660884&os=3077723332%2C2075564113&adpicid=0&fr=ala&sme=&cg=&bdtype=14&simics=1369451712%2C492207454&objurl=http%3A%2F%2Fwww.bjfenghuagg.com%2Fimg%2FaHR0cDovL2YyLmRuLmFucXUuY29tL29yZ2luL1pqVmlNUT09L2FsbGltZy8xMjA2MjQvMzAtMTIwNjI0MTUzNjAyLmpwZw%3D%3D.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bk3ujg2i7w22_z%26e3Bv54AzdH3F89d0l9808AzdH3F&gsm=0&cardserver=1" });
		RequestPack pack = new RequestPack();
		pack.setHosId(hosId);
		pack.setSeqno("111111");
		pack.setCmd("0200");
		String body = JsonUtils.toJson(checkBean);
		System.out.println(body);
		pack.setBody(body);
		pack.setSendTime(System.currentTimeMillis());
		if (hdpClient.synSendData(pack)) {
			System.out.println("推送检查单成功");
		} else {
			System.out.println("推送检查单失败");
		}

		sleep(60000);
	}

	/**
	 * 发送处方单 0300,支持多个处方单
	 */
	@Test
	public void sendPrescription() throws Exception {
		PrescriptionInfoBean prescription = new PrescriptionInfoBean();
		prescription.setAccPresNo("111111");
		prescription.setAccPresItemId("111111_01");
		prescription.setAccessVisitId("2BE45201509241648");
		prescription.setAccessPatId("00008O6S");
		prescription.setPatName("张三");
		prescription.setPresCategCode("1");
		prescription.setPresCategName("处方类别名称");
		prescription.setAccBizId(null);
		prescription.setBizType(null);
		prescription.setPresDate("2015-10-23");
		prescription.setAccCrtPresDocId("1234567890129229");
		prescription.setCrtPresDocName("韩莉");
		prescription.setAccCrtPresDeptId("1003637");
		prescription.setCrtPresDeptName("中医科门诊");
		prescription.setAccDiagDocId("1234567890129229");
		prescription.setDiagDocName("韩莉");
		prescription.setAccDiagDeptId("1003637");
		prescription.setDiagDeptName("中医科门诊");
		prescription.setDiagCode("0000");
		prescription.setDiagName("上呼吸道感染");
		prescription.setDiagResult("上呼吸道感染");
		prescription.setDiagOtherResult("头痛");
		prescription.setSelfCost(100.00);
		prescription.setHealCost(300.00);
		prescription.setTotalCost(400.00);

		List<DrugEntity> drugs = new ArrayList<DrugEntity>();
		DrugEntity drug = new DrugEntity();
		drugs.add(drug);
		drug.setStandCode("0101");
		drug.setMedInsurCode("0101");
		drug.setDrugLocalCode("01010E0302");
		drug.setDrugType("1");
		drug.setDrugChemName("维生素C片");
		drug.setDrugGoodsName("维生素C片");
		drug.setDrugSpecifications("200");
		drug.setDrugDescr("很好吃");

		drug.setDrugUnit("mg");
		drug.setDrugUnitPrice(10.00);
		drug.setDrugAmount("10.0000");
		drug.setDrugSelfCost(10.0);
		drug.setDrugHealCost(20.0);
		drug.setIsHealDurg("Y");
		drug.setUsAge("口服 一次三次 一次两片");

		drug = new DrugEntity();
		drugs.add(drug);
		drug.setStandCode("0102");
		drug.setMedInsurCode("0102");
		drug.setDrugLocalCode("01010E0302");
		drug.setDrugType("1");
		drug.setDrugChemName("阿莫西林分散片(NR)");
		drug.setDrugGoodsName("阿莫西林分散片(NR)");
		drug.setDrugSpecifications("200");
		drug.setDrugDescr("很好吃");

		drug.setDrugUnit("mg");
		drug.setDrugUnitPrice(10.00);
		drug.setDrugAmount("10.0000");
		drug.setDrugSelfCost(10.0);
		drug.setDrugHealCost(20.0);
		drug.setIsHealDurg("Y");
		drug.setUsAge("口服 一次三次 一次两片");

		prescription.setDrugs(drugs);

		List<PrescriptionInfoBean> prescriptions = new ArrayList<PrescriptionInfoBean>();
		prescriptions.add(prescription);

		RequestPack pack = new RequestPack();
		pack.setHosId(hosId);
		pack.setSeqno("111111");
		pack.setCmd("0300");

		String body = JsonUtils.toJson(prescriptions);
		System.out.println(body);
		pack.setBody(body);
		pack.setSendTime(System.currentTimeMillis());
		if (hdpClient.synSendData(pack)) {
			System.out.println("推送处方单成功");
		} else {
			System.out.println("推送处方单失败");
		}
		
		sleep(60000);
	}

    /**
     * 发送诊间支付通知 0400
     */
    @Test
    public void sendUnpaidFee() throws Exception{

        RequestPack pack = new RequestPack();
        pack.setHosId(hosId);
        pack.setSeqno("111111");
        pack.setCmd("0400");
        String body ="[{'accUnpaidFeeId':'1:00008O6S:0000000001','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'胎儿医学科','docName':'肖小敏','healCost':0,'items':[{'accUnpaidFeeItemId':'01010G02000B','itemHealCost':0,'itemName':'口服葡萄糖粉(82.5g)','itemNum':'1','itemPrice':5,'itemSelfCost':5,'itemUintPrice':5,'sortItem':'药品','sortSumFee':100},{'accUnpaidFeeItemId':'01010801030102','itemHealCost':0,'itemName':'米索前列醇片(华典)','itemNum':'3','itemPrice':30,'itemSelfCost':30,'itemUintPrice':10,'sortItem':'药品','sortSumFee':100},{'accUnpaidFeeItemId':'01010D040205','itemHealCost':0,'itemName':'米非司酮片(含珠停)','itemNum':'10','itemPrice':65,'itemSelfCost':65,'itemUintPrice':6.5,'sortItem':'药品','sortSumFee':100}],'pageCurrent':0,'pageLimit':0,'puk':'173da4105ff2401c8b1ffd640ebeed18','selfCost':100,'selfPayRate':1,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'1:00008O6S:0000000002','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'胎儿医学科','docName':'肖小敏','healCost':0,'items':[{'accUnpaidFeeItemId':'250104014','itemHealCost':0,'itemName':'阴道分泌物检查','itemNum':'1','itemPrice':10,'itemSelfCost':10,'itemUintPrice':10,'sortItem':'检验','sortSumFee':100},{'accUnpaidFeeItemId':'250501039','itemHealCost':0,'itemName':'细菌性阴道病唾液酸酶测定','itemNum':'1','itemPrice':60,'itemSelfCost':60,'itemUintPrice':60,'sortItem':'检验','sortSumFee':100},{'accUnpaidFeeItemId':'250101002','itemHealCost':0,'itemName':'红细胞计数(RBC)','itemNum':'1','itemPrice':20,'itemSelfCost':20,'itemUintPrice':20,'sortItem':'检验','sortSumFee':100},{'accUnpaidFeeItemId':'250101009','itemHealCost':0,'itemName':'白细胞计数(WBC)','itemNum':'1','itemPrice':10,'itemSelfCost':10,'itemUintPrice':10,'sortItem':'检验','sortSumFee':100}],'pageCurrent':0,'pageLimit':0,'puk':'6b6e863b099642c0be35231fd7fe72b7','selfCost':100,'selfPayRate':1,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'1:00008O6S:0000000003','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'胎儿医学科','docName':'肖小敏','healCost':0,'items':[{'accUnpaidFeeItemId':'310205008','itemHealCost':0,'itemName':'电脑血糖监测','itemNum':'1','itemPrice':30,'itemSelfCost':30,'itemUintPrice':30,'sortItem':'治疗','sortSumFee':100},{'accUnpaidFeeItemId':'340100017-3','itemHealCost':0,'itemName':'超声波治疗(超声雾化)','itemNum':'4','itemPrice':60,'itemSelfCost':60,'itemUintPrice':15,'sortItem':'治疗','sortSumFee':100},{'accUnpaidFeeItemId':'430000023','itemHealCost':0,'itemName':'穴位贴敷治疗','itemNum':'2','itemPrice':10,'itemSelfCost':10,'itemUintPrice':5,'sortItem':'治疗','sortSumFee':100}],'pageCurrent':0,'pageLimit':0,'puk':'733561a15cf14e6b8ae6faba91dc9654','selfCost':100,'selfPayRate':1,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'1:00008O6S:0000000004','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'胎儿医学科','docName':'肖小敏','healCost':0,'items':[{'accUnpaidFeeItemId':'311201026','itemHealCost':0,'itemName':'胎心监测','itemNum':'1','itemPrice':35,'itemSelfCost':35,'itemUintPrice':35,'sortItem':'检查','sortSumFee':300},{'accUnpaidFeeItemId':'220203004','itemHealCost':0,'itemName':'胎儿生物物理相评分','itemNum':'1','itemPrice':60,'itemSelfCost':60,'itemUintPrice':60,'sortItem':'检查','sortSumFee':300},{'accUnpaidFeeItemId':'311201039','itemHealCost':0,'itemName':'胎盘成熟度检测','itemNum':'1','itemPrice':35,'itemSelfCost':35,'itemUintPrice':35,'sortItem':'检查','sortSumFee':300},{'accUnpaidFeeItemId':'220301001-6','itemHealCost':0,'itemName':'彩色超声检查(产科(含胎儿及宫腔))','itemNum':'1','itemPrice':170,'itemSelfCost':170,'itemUintPrice':170,'sortItem':'检查','sortSumFee':300}],'pageCurrent':0,'pageLimit':0,'puk':'a5254588b88c4993b4e036d36550ec74','selfCost':300,'selfPayRate':1,'totalCost':300,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'1:00008O6S:0000000005','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'胎儿医学科','docName':'肖小敏','healCost':0,'items':[{'accUnpaidFeeItemId':'120400002-1','itemHealCost':0,'itemName':'静脉采血','itemNum':'1','itemPrice':10,'itemSelfCost':10,'itemUintPrice':10,'sortItem':'附加','sortSumFee':100},{'accUnpaidFeeItemId':'910006001','itemHealCost':0,'itemName':'采血针','itemNum':'1','itemPrice':20,'itemSelfCost':20,'itemUintPrice':20,'sortItem':'附加','sortSumFee':100},{'accUnpaidFeeItemId':'950027005','itemHealCost':0,'itemName':'无抗凝管采血器','itemNum':'7','itemPrice':70,'itemSelfCost':70,'itemUintPrice':10,'sortItem':'附加','sortSumFee':100}],'pageCurrent':0,'pageLimit':0,'puk':'4bef8c3481314ad89035953c3cc55322','selfCost':100,'selfPayRate':1,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'2:00008O6S:0000000006','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'骨科','docName':'查振刚','items':[{'accUnpaidFeeItemId':'01010G02000B','itemName':'口服葡萄糖粉(82.5g)','itemNum':'1','itemPrice':5,'itemUintPrice':5,'sortItem':'药品'},{'accUnpaidFeeItemId':'01010801030102','itemName':'米索前列醇片(华典)','itemNum':'3','itemPrice':30,'itemUintPrice':10,'sortItem':'药品'},{'accUnpaidFeeItemId':'01010D040205','itemName':'米非司酮片(含珠停)','itemNum':'10','itemPrice':65,'itemUintPrice':6.5,'sortItem':'药品'}],'pageCurrent':0,'pageLimit':0,'puk':'f69f5e3582b444dab34979883da8406c','selfCost':-9999,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'2:00008O6S:0000000007','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'骨科','docName':'查振刚','items':[{'accUnpaidFeeItemId':'250104014','itemName':'阴道分泌物检查','itemNum':'1','itemPrice':10,'itemUintPrice':10,'sortItem':'检验'},{'accUnpaidFeeItemId':'250501039','itemName':'细菌性阴道病唾液酸酶测定','itemNum':'1','itemPrice':60,'itemUintPrice':60,'sortItem':'检验'},{'accUnpaidFeeItemId':'250101002','itemName':'红细胞计数(RBC)','itemNum':'1','itemPrice':20,'itemUintPrice':20,'sortItem':'检验'},{'accUnpaidFeeItemId':'250101009','itemName':'白细胞计数(WBC)','itemNum':'1','itemPrice':10,'itemUintPrice':10,'sortItem':'检验'}],'pageCurrent':0,'pageLimit':0,'puk':'44d84845975a4f7c912d7198d2f93bfd','selfCost':-9999,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'2:00008O6S:0000000008','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'骨科','docName':'查振刚','items':[{'accUnpaidFeeItemId':'310205008','itemName':'电脑血糖监测','itemNum':'1','itemPrice':30,'itemUintPrice':30,'sortItem':'治疗'},{'accUnpaidFeeItemId':'340100017-3','itemName':'超声波治疗(超声雾化)','itemNum':'4','itemPrice':60,'itemUintPrice':15,'sortItem':'治疗'},{'accUnpaidFeeItemId':'430000023','itemName':'穴位贴敷治疗','itemNum':'2','itemPrice':10,'itemUintPrice':5,'sortItem':'治疗'}],'pageCurrent':0,'pageLimit':0,'puk':'6deda68e19bd4bdb972d5b7c79798554','selfCost':-9999,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'2:00008O6S:0000000009','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'骨科','docName':'查振刚','items':[{'accUnpaidFeeItemId':'311201026','itemName':'胎心监测','itemNum':'1','itemPrice':35,'itemUintPrice':35,'sortItem':'检查'},{'accUnpaidFeeItemId':'220203004','itemName':'胎儿生物物理相评分','itemNum':'1','itemPrice':60,'itemUintPrice':60,'sortItem':'检查'},{'accUnpaidFeeItemId':'311201039','itemName':'胎盘成熟度检测','itemNum':'1','itemPrice':35,'itemUintPrice':35,'sortItem':'检查'},{'accUnpaidFeeItemId':'220301001-6','itemName':'彩色超声检查(产科(含胎儿及宫腔))','itemNum':'1','itemPrice':170,'itemUintPrice':170,'sortItem':'检查'}],'pageCurrent':0,'pageLimit':0,'puk':'4207f39e89cc4463ac47f9732937628f','selfCost':-9999,'totalCost':300,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'},{'accUnpaidFeeId':'2:00008O6S:0000000010','accessPatId':'00008O6S','creatDate':'2015-11-06','deptName':'骨科','docName':'查振刚','items':[{'accUnpaidFeeItemId':'120400002-1','itemName':'静脉采血','itemNum':'1','itemPrice':10,'itemUintPrice':10,'sortItem':'附加'},{'accUnpaidFeeItemId':'910006001','itemName':'采血针','itemNum':'1','itemPrice':20,'itemUintPrice':20,'sortItem':'附加'},{'accUnpaidFeeItemId':'950027005','itemName':'无抗凝管采血器','itemNum':'7','itemPrice':70,'itemUintPrice':10,'sortItem':'附加'}],'pageCurrent':0,'pageLimit':0,'puk':'cf4a6980f53d456b9292a89c109840cf','selfCost':-9999,'totalCost':100,'unpaidFeeType':2,'uuu':'2015-11-06 10:48:16'}]";
        pack.setBody(body);
        pack.setSendTime(System.currentTimeMillis());
        if (hdpClient.synSendData(pack)) {
            System.out.println("推送诊间支付成功");
        } else {
            System.out.println("推送诊间支付诊失败");
        }

        sleep(60000);

    }
    /**
     * 发送停诊通知 0600
     */
    @Test
    public void sendStopDiag() throws Exception{
        StopDiagHTO stopDiagHTO =  new StopDiagHTO();
        stopDiagHTO.setHosId(Long.parseLong(hosId));

        List<StopDiagEntity> entities = new ArrayList<StopDiagEntity>();
        stopDiagHTO.setSdList(entities);
        StopDiagEntity stopDiagEntity = new StopDiagEntity();
        stopDiagEntity.setAccessSchId("1732607");
        stopDiagEntity.setStopDiagReason(null);
        entities.add(stopDiagEntity);
        stopDiagEntity = new StopDiagEntity();
        stopDiagEntity.setAccessSchId("1732608");
        stopDiagEntity.setStopDiagReason(null);
        entities.add(stopDiagEntity);
        stopDiagEntity = new StopDiagEntity();
        stopDiagEntity.setAccessSchId("1732609");
        stopDiagEntity.setStopDiagReason(null);
        entities.add(stopDiagEntity);

        stopDiagEntity = new StopDiagEntity();
        stopDiagEntity.setAccessSchId("1732610");
        stopDiagEntity.setStopDiagReason(null);
        entities.add(stopDiagEntity);

        RequestPack pack = new RequestPack();
        pack.setHosId(hosId);
        pack.setSeqno("111111");
        pack.setCmd("0600");
       // String body ="{hosid : 1111, sdList: [{\"accessSchId\":\"1732607\",\"stopDiagReason\":\"\"},{\"accessSchId\":\"1732608\",\"stopDiagReason\":\"\"},{\"accessSchId\":\"1732609\",\"stopDiagReason\":\"\"},{\"accessSchId\":\"1732610\",\"stopDiagReason\":\"\"}]}";
        String body = JsonUtils.toJson(stopDiagHTO);

        pack.setBody(body);
        System.out.println(body);
        pack.setSendTime(System.currentTimeMillis());
        if (hdpClient.synSendData(pack)) {
            System.out.println("推送处停诊成功");
        } else {
            System.out.println("推送处停诊失败");
        }

        sleep(60000);

    }
   
}
