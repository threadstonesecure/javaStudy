package dlt.study.runtime;

import jodd.io.StreamGobbler;

import java.util.concurrent.Executors;

public class RuntimeDemo {
    public static void main(String[] args) throws Exception {

        ProcessBuilder pb = new ProcessBuilder("sh","/Users/denglt/myprograme/javaStudy/hs-hdp/hs-hdp-server/target/hs-hdp-server/bin/run.sh", "stop");
/*        Map<String, String> env = pb.environment();
        env.put("VAR1", "myValue");
        env.remove("OTHERVAR");
        env.put("VAR2", env.get("VAR1") + "suffix"); */
        // pb.directory(new File("/Users/denglt/myprograme/javaStudy/hs-hdp/hs-hdp-server/target/hs-hdp-server/bin/"));

        Process p = pb.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(p.getInputStream(), System.out);
        /*Executors.newSingleThreadExecutor().submit(streamGobbler);*/
        streamGobbler.start();
        System.out.println(p);
        int exitCode = p.waitFor();
        assert exitCode == 0;

    }
}
