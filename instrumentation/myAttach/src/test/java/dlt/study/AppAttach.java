package dlt.study;


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.Optional;

/**
 * Unit test for simple AgentMain.
 */
public class AppAttach {
    public static void main(String[] args)  {
        try {
            String attachJar = "/Users/denglt/onGithub/javaStudy/instrumentation/myAttach/target/myAttach-0.0.1-SNAPSHOT.jar";

        Optional<VirtualMachineDescriptor> optional = VirtualMachine.list().stream()
                .filter(t -> t.displayName().equals("dlt.study.TransRunner")).findFirst();
        if (optional.isPresent()) {
            System.out.println("找到target vm:" + optional.get());
            VirtualMachine targetVM = VirtualMachine.attach(optional.get());
            //System.out.println(targetVM.getClass());
            //targetVM.loadAgent(attachJar,"add");
            targetVM.loadAgent(attachJar,"delete");
            //Thread.sleep(60000);
            targetVM.detach();
        }

       /*     VirtualMachine targetVM = VirtualMachine.attach("80341");
            System.out.println("找到target vm:" + targetVM);
            targetVM.loadAgentPath(attachJar);
            Thread.sleep(60000);
            targetVM.detach();*/
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
