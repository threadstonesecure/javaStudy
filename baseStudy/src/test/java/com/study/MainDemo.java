package com.study;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;

public class MainDemo {

    public static void main(String[] args) throws Exception {
        Process exec = Runtime.getRuntime().exec("sh /Users/denglt/Dropbox/myprograme/javaStudy/hs-hdp/hs-hdp-server/target/hs-hdp-server/bin/run.sh restart");
        //exec.waitFor();
        System.exit(0);
    }

    public static void main2(String[] args) throws Exception {
        StringBuilder cmd = new StringBuilder();
        cmd.append("cd "+System.getProperty("user.dir")+" ; ");
        //cmd.append("echo denglt >> /tmp/maindemo.log ");
       // cmd.append("ls -l /Users/denglt");
        Process exec = Runtime.getRuntime().exec(cmd.toString());
        PrintStream out = new PrintStream(exec.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(exec.getInputStream()));

       // out.println("ls -l /home");
        while (in.ready()) {
            String s = in.readLine();
            System.out.println(s);
        }

        exec.waitFor();
       // restart();
       // System.in.read();
    }

    public static void restart() throws Exception{
        StringBuilder cmd = new StringBuilder();
        cmd.append("cd "+System.getProperty("user.dir")+" ; ");
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
        cmd.append(System.getProperty("sun.java.command") );
        cmd.append(" >/dev/null 2>&1 &");
        System.out.println(cmd.toString());
        Runtime.getRuntime().exec(cmd.toString());

    }
}
