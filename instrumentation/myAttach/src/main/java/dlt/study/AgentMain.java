package dlt.study;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Java SE 6 ，开发者可以在 main 函数开始执行以后，再启动自己的 Instrumentation 程序。
 */
public class AgentMain {

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException { //[1]
        System.out.println("=========AgentMain方法执行========");
        System.out.println(agentArgs);
        inst.addTransformer(new RecordRunInfo(), true);
        inst.retransformClasses(Trans.class);
/*        if (inst.isNativeMethodPrefixSupported()) {
            inst.setNativeMethodPrefix();
        }*/
        System.out.println("Agent Main Done");
    }

    //[1] 和 [2] 同时存在时，[2] 被忽略
    public static void agentmain(String agentArgs) { //[2]

    }
}
