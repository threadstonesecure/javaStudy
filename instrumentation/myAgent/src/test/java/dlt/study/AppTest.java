package dlt.study;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 指定agent：(-javaagent 这个参数的个数是不限的，如果指定了多个，则会按指定的先后执行，执行完各个 agent 后，才会执行主程序的 main 方法)
 * -javaagent:/Users/denglt/onGithub/javaStudy/instrumentation/myAgent/target/myAgent-0.0.1-SNAPSHOT.jar=Hello1
 * -javaagent:/Users/denglt/onGithub/javaStudy/instrumentation/myAgent/target/myAgent-0.0.1-SNAPSHOT.jar=Hello2
 * Unit test for simple AgentPremain.
 */


public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
        System.out.println("Agent Test ! ");
    }
}
