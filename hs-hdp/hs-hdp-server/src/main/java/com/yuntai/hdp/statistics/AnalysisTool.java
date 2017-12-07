package com.yuntai.hdp.statistics;

import com.yuntai.hdp.access.ResultPack;
import org.springframework.stereotype.Service;

/**
 * Created by denglt on 16/9/1.
 */
public class AnalysisTool {
    /**
     * add 医院对接结果
     * 该方法thread safe ,不要修改ResultPack的内容
     * @param pack
     */
    public static void addAccessHospitalResult(ResultPack  pack){

    }

    /**
     * 进行对接数据分析,按医院进行汇总
     */
    public void analyzeAccessInfo(){

    }

    /**
     * 对接数据分析汇总结果入库
     */
    public void putAccessInfo2Database(){

    }
}
