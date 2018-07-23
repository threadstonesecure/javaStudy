# BTrace Tutorial
# This tutorial is written by Jaroslav Bachorik.

## 1. Hello World

Accustoms the learner to 'btrace' command and the way it is used.
Demonstrates the BTrace ability to instrument a class.

### Setup

#### HelloWorld Class

```
package extra;

abstract public class HelloWorld extends HelloWorldBase {
    protected int field = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("ready when you are ...");
        System.in.read();

        callA();
    }

    private static void callA() {
        HelloWorld instance = new HelloWorldExt();
        long x = System.nanoTime();
        instance.callA("hello", 13);
        System.out.println("dur = " + (System.nanoTime() - x));
    }

    private void callA(String a, int b) {
	field++;
        callB(callC(a, b));
	field--;
    }

    private void callB(String s) {
	field++;
        System.out.println("You want " + s);
	field--;
    }

    abstract protected String callC(String a, int b);
}

final class HelloWorldExt extends HelloWorld {
    @Override
    protected String callC(String a, int b) {
	try {
            field++;
            String s = a + "-" + b;
            for(int i=0;i<100;i++) {
                s = callD(s);
            }
            return s;
        } finally {
            field--;
        }
    }
}

abstract class HelloWorldBase {
    final protected String callD(String s) {
        return "# " + s;
    }
}
```

### Steps

1. Run the HelloWorld application
2. Get the HelloWorld application PID via `jps` command
3. Run `btrace <PID> <HelloWorldTrace.java>`
4. Proceed with the HelloWrold application
5. Watch messages being printed

You will repeat these steps while gradually enhancing the used BTrace script

### Lessons

#### Lesson 1 - Launching BTrace

##### Using `btrace` client to attach to a running JVM

`btrace [opts] <pid> <btrace-script> [<args>]`

* **opts** BTrace specific options; use `btrace -h` to obtain the list of all supported options
* **pid** process id of the traced Java program
* **btrace-script** trace program. If it is a ".java", then it is compiled before submission. Or else, it is assumed to be pre-compiled [i.e., it has to be a .class] and submitted.
* **args** trace specific arguments

Once you are attached to the target JVM you can press Ctrl-C in the terminal to show the BTrace console. From there you can either detach and exit or send an event (handled by the __@OnEvent__ annotated methods in the trace program.

##### Starting a Java application with BTrace agent

###### Directly

`java -javaagent:btrace-agent.jar=[<agent-arg>[,<agent-arg>]*]? <launch-args>`

The agent takes a list of comma separated arguments.

* **noServer** - don't start the socket server
* **bootClassPath** - boot classpath to be used
* **systemClassPath** - system classpath to be used
* **debug** - turns on verbose debug messages (true/false)
* **unsafe** - do not check for btrace restrictions violations (true/false)
* **dumpClasses** - dump the transformed bytecode to files (true/false)
* **dumpDir** - specifies the folder where the transformed classes will be dumped to
* **stdout** - redirect the btrace output to stdout instead of writing it to an arbitrary file (true/false)
* **probeDescPath** - the path to search for probe descriptor XMLs
* **scriptdir** - the path to a directory containing scripts to be run at the agent startup
* **scriptOutputFile** - the path to a file the btrace agent will store its output
* **script** - comma separated list of tracing scripts to be run at the agent startup; this argument **MUST** be the last one in the argument list

The scripts to be run must have already been compiled to bytecode (a *.class* file) by __btracec__.

###### Using `btracer'

`btracer [opts] <pre-compiled-btrace.class> <vm-arg> <application-args>`

* **opts** BTrace specific options; use `btracer -h` to obtain the list of all supported options
* **pre-compiled-btrace.class** the trace script compiled to bytecode via __btracec__
* **vm-args** the VM arguments; eg. `-cp app.jar Main.class` or `-jar app.jar`
* **application-args** the application specific arguments

You can use __btracer__ to launch java application from jar (`btracer ... -jar app.jar <application args>`) or a main class (`btracer ... -cp <class_path> <main class> <application args>`)

##### Compiling trace scripts

This needs to be done in order to launch the Java application with BTrace agent.

`btracec [-cp <classpath>] [-d <directory>] <one-or-more-BTrace-.java-files>`

* **classpath** is the classpath used for compiling BTrace program(s). Default is "."
* **directory** is the output directory where compiled .class files are stored. Default is ".".

Rather than regular *javac* the BTrace compiler is used - causing the script to be validated at compile time and prevent reporting verify errors at runtime.

#### Lesson 2 - Tracing methods

This is the main purpose of BTrace - inject a custom code to custom locations to give the insights about the internal state and dynamics of the application.

1. Getting just the information that any method is being executed
```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="/.*/")
    public static void onMethod() {
        println("Hello from method");
    }
}
```

2. Get the method names
```
package helloworld;
import ...;

@BTrace 
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld")
    public static void onMethod(@ProbeMethodName String pmn) {
        println("Hello from method " + pmn);
    }
}
```

3. Intercept only a particular method
```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="callA")
    public static void onMethod(@ProbeMethodName String pmn) {
        println("Hello from method " + pmn);
    }
}
```

4. Intercept only a particular method with name matching the handler name
```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="#")
    public static void callA(@ProbeMethodName String pmn) {
        println("Hello from method " + pmn);
    }
}
```

5. Intercept methods with names matching certain patterns
__Note:__ you can use pattern matching for the class names, too

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="/call.*/")
    public static void onMethod(@ProbeMethodName String pmn) {
        println("Hello from method " + pmn);
    }
}
```

6. Intercept methods with names matching certain patterns and inspect their parameters

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="/call.*/")
    public static void onMethod(@ProbeMethodName String pmn, AnyType[] args) {
        println("Hello from method " + pmn);
        println("Received the following parameters:");
        printArray(args);
    }
}
```

7. Intercept method with names matching certain patterns and discover their signatures
```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="/call.*/")
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn) {
        println("Hello from method " + pmn);
    }
}
```

8. Intercept methods for all subclasses and implementations of a certain class/interface

__Note:__ 'extra.HelloWorldBase.callD()' doesn't show up - it is defined in the superclass of 'extra.HelloWorld' and therefore not intercepted.

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="+extra.HelloWorld", method="/call.*/")
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn) {
        println("Hello from method " + pmn);
    }
}
```

9. Intercept method with a particular name and signature + capture the method arguments (you need to use the information learned in the previous step)

__Note:__ The order of the un-annotated parameters must correspond to the order of the traced method parameters. Annotated parameters may be placed anywhere.

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorldExt", method="callC")
    public static void onMethod(@ProbeMethodName String pmn, String param1, int param2) {
        println("Hello from method " + pmn);
        println("Arguments: param1 = " + str(param1) + ", param2 = " + str(param2));
    }
}
```

10. Intercpet method with a particular name and signature but don't capture the method arguments. Here you will need to decifer the VM method signature to get java like method signature. See the @OnMethod.type() javadoc for the java like signature format.

Eg. having the VM method signature in form of (Ljava/lang/String;I)V will translate to "void (java.lang.String, int)"

__Note:__ We are using overloaded method here and specifying the signature helps BTrace determine which method should be instrumented

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="callA", type="void (java.lang.String, int)")
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn) {
        println("Hello from method " + pmn);
    }
}
```

11. Intercept method with a particular name and capture its return value
`location=@Location(Kind.RETURN)` sets up the instrumentation to be inserted just before the method exits

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="callC", location=@Location(Kind.RETURN))
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn, @Return String ret) {
        println("Hello from method " + pmn + "; returning " + ret);
    }
}
```

12. Inspect the content of an instance variable in the method declaring class

```
package helloworld;
import java.lang.Class;
import java.lang.reflect.Field;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="/call.*/", location=@Location(Kind.RETURN))
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn, @Self Object thiz) {
        Class myClz = classOf(thiz);
        Field fld = field(myClz, "field", false);
        println("Hello from method " + pmn);
        if (fld != null) {
           println("field = " + str(getInt(fld, thiz)));
        }
    }
}
```

13. Get the method execution duration

__Note:__ Need to use @Location(Kind.RETURN) to be able to capture the execution duration

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="/call.*/", location=@Location(Kind.RETURN))
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn, @Duration long dur) {
        println("Hello from method " + pmn);
        println("It took " + str(dur) + "ns to execute this method");
    }
}
```

14. Tracing methods invoked from inside a particular method

__Note:__ 'class', 'method' etc. directly in the @OnMethod annotation will determine where we should look for the invocation of the methods defined by 'class', 'method' etc. parameters in the @Location annotation.
__Note:__ @ProbeMethodName and @ProbeClassName refer to the context method and class; @TargetMethodOrField refers to the traced method invocation
__Note:__ You can use the 'type' annotation parameter in @OnMethod annotation to restrict the context methods and in @Location to restrict the traced method invocations

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="callA",
              location=@Location(
                    value = Kind.CALL,
                    clazz = "extra.HelloWorld",
                    method = "/call.*/",
                    where = Where.BEFORE)
    )
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn, @TargetMethodOrField(fqn = true) String tpmn) {
        println("Hello from method " + pmn);
        println("Going to invoke method " + tpmn);
    }
}
```

15. Measuring the duration of methods invoked from inside a particular method

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="callA",
              location=@Location(
                    value = Kind.CALL,
                    clazz = "extra.HelloWorld",
                    method = "/call.*/",
                    where = Where.AFTER)
    )
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn, @TargetMethodOrField(fqn = true) String tpmn, @Duration long dur) {
        println("Hello from method " + pmn);
        println("Executing " + tpmn + " took " + dur + "ns");
    }
}
```

16. Tracing methods invoked from inside a particular method and capturing their parameters

__Note:__ The captured parameters pertain to the invoked method rather than the context method
__Note:__ The @Self annotated parameter captures the context instance and @TargetInstance annotated parameter captures the instance the method is invoked on

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnMethod(clazz="extra.HelloWorld", method="callA",
              location=@Location(
                    value = Kind.CALL,
                    clazz = "extra.HelloWorld",
                    method = "/call.*/",
                    where = Where.BEFORE)
    )
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn, @TargetMethodOrField(fqn = true) String tpmn,
                                @Self Object thiz, @TargetInstance Object tgt, String a, int b) {
        println("Hello from method " + pmn);
        println("Going to invoke method " + tpmn);
        println("context = " + str(classOf(thiz)) + ", target = " + str(classOf(tgt)));
        println("a = " + a + ", b = " + str(b));
    }
}
```

#### Lesson 3 - Global callbacks

Global callbacks are not directly related to the tracing code injection but they allow us to observe the global state and act correspondingly.

#### __@OnExit__

Called when the traced application is about to exit. Allows to capture the exit code.

__Note:__ The signature of the handler method __MUST__ be 'void (int)'

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnExit
    public static void onexit(int code) {
        println("Application exitting with " + code);
    }
}
```

#### __@OnError__

Called whenever an exception is thrown from anywhere in the BTrace handlers.

__Note:__ The signature of the handler method __MUST__ be 'void (java.lang.Throwable)'

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnError
    public static void onerror(Throwable t) {
        println("Encountered internal error " + str(t));
    }
}
```

#### __@OnTimer__

Allows to register a handler to be invoked periodically at defined intervals.

__Note:__ The annotation parameter takes the interval value in milliseconds
__Note:__ The signature of the handler method __MUST__ be 'void ()'

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnTimer(1000)
    public static void ontimer() {
        println("tick ...");
    }
}
```

#### __@OnEvent__

Used to raise events from external clients (eg. the command line client). The annotation takes a String parameter which is the event name. When not provided the event is considered to be 'unnamed'.

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnEvent
    public static void unnamed() {
        println("Received unnamed event");
    }
}
```
or

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    @OnEvent("myEvent")
    public static void myevent() {
        println("Received my event");
    }
}
```

#### Lesson 4 - Sampling

Tracing many methods being executed frequently can bring a significant overhead to the traced application. And often we are not really interested in the high detail data - an aggregated view would do just fine.

Therefore it is possible to employ statistical sampling to reduce the amount of collected data and related overhead while still providing relevant information about the application behaviour.

The sampling implementation in BTrace guarantees that at least one invocation of a traced method will be recorded, no matter what the sampling settings are.

1. Intercept only each 10th invocation on average

__Note:__ Even though the 'callD' method is executed 100 times we will get only ~10 hits - as dictated by the 'mean' parameter.

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    private static int cntr = 1;
    @Sampled(kind = Sampled.Sampler.Const, mean = 10)
    @OnMethod(clazz="/extra\\.HelloWorld.*/", method="callD")
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn) {
        println("Hello from method " + pmn + " : " + (cntr++));
    }
}
```

2. Let the sampler mean parameter be adjusted dynamically by keeping to the overhead target

__Note:__ In this case the 'mean' parameter actually specify the lowest number of nanoseconds the average interval between interception should be.
__Note:__ Because the adaptive sampling needs to collect timestamps in order to maintain the overhead target the lowest value for the 'mean' parameter is 180 (cca. 60ns for gettint start/stop timestamp pair multiplied by the safety margin of 3) 
__Note:__ The 'callD' method is very short and the number of iteration is rather limited - we will most probably get only one hit here

```
package helloworld;
import ...;

@BTrace
public class HelloWorldTrace {
    private static int cntr = 1;
    @Sampled(kind = Sampled.Sampler.Adaptive, mean = 50)
    @OnMethod(clazz="/extra\\.HelloWorld.*/", method="callD")
    public static void onMethod(@ProbeMethodName(fqn = true) String pmn) {
        println("Hello from method " + pmn + " : " + (cntr++));
    }
}
```