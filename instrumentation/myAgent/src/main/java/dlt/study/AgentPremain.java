package dlt.study;

import java.lang.instrument.Instrumentation;

/**
 * 在 Java SE 5 当中，开发者只能在premain当中施展想象力，Instrumentation也仅限于 main 函数执行前执行。
 */
public class AgentPremain {

    public static void premain(String agentArgs, Instrumentation inst){ //[1]
        System.out.println("=========premain方法执行========");
        System.out.println(agentArgs);
        // 添加Transformer
      //  inst.addTransformer(new FirstAgent()); // 加载FirstAgent
        inst.addTransformer(new RecordRunInfo());
    }

    //[1] 和 [2] 同时存在时，[2] 被忽略
    public static void premain(String agentArgs){ //[2]

    }
}
