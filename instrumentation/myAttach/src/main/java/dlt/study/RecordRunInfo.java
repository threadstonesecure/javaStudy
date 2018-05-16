package dlt.study;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class RecordRunInfo implements ClassFileTransformer {

    private static RecordRunInfo recordRunInfo = new RecordRunInfo();

    private RecordRunInfo(){}

    public static RecordRunInfo get() {
        return recordRunInfo;
    }


    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith("dlt/study")) {
            try {
                CtClass ctclass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classfileBuffer), false);
                for (CtMethod ctMethod : ctclass.getMethods()) {
                    if (isNative(ctMethod)) continue;
                    System.out.println(ctMethod.getName());
                    ctMethod.insertBefore("System.out.println(\" before in RecordRunInfo\");");
                    ctMethod.insertAfter("System.out.println(\" after in RecordRunInfo\");");
                }
                return ctclass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public boolean isNative(CtMethod method) {
        return Modifier.isNative(method.getModifiers());
    }


}
