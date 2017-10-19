package dlt.study.script;

import org.junit.Before;
import org.junit.Test;

import javax.script.*;

/**
 * Created by denglt on 16/9/27.
 */
public class ScriptDemo {
    ScriptEngine engine = null;
    @Before
    public void init() {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        if (engine == null){
            throw new RuntimeException("找不到JavaScript Engine");
        }
    }

    @Test
    public  void simpleExample () throws ScriptException {
        ScriptContext context = engine.getContext();
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        System.out.println(bindings);
        bindings = context.getBindings(ScriptContext.GLOBAL_SCOPE);
        System.out.println(bindings);
        Bindings bindings2 = engine.createBindings();
        context.setBindings(bindings2, ScriptContext.ENGINE_SCOPE);
        bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        System.out.println(bindings);
        engine.eval("println('hello world!')");
    }

    @Test
    public void  defaultBinding() throws ScriptException {
        engine.put("name","Alex");
        engine.eval(" var message = 'hello, ' + name; println(message)");
        Object obj = engine.get("message");
        System.out.println(obj.getClass());
        System.out.println(obj);
        obj = engine.get("xxxx");
        System.out.println(obj);
    }

    @Test
    public void customBinding() throws ScriptException {
        engine.put("name","Alex");
        Bindings bindings = engine.createBindings();
        bindings.put("name","denglt");
        engine.eval(" var message = 'hello, ' + name; println(message)",bindings);
        Object obj = bindings.get("message");
        System.out.println(obj);
    }

    /**
     * 脚本上下文属性
     * @throws ScriptException
     */
    @Test
    public void scriptContextAttribute() throws ScriptException {
        ScriptContext context = engine.getContext();
        context.setAttribute("name","denglt", ScriptContext.GLOBAL_SCOPE);
        context.setAttribute("name","zyy", ScriptContext.ENGINE_SCOPE);
        Object obj = context.getAttribute("name");
        System.out.println(obj);
        engine.eval(" var message = 'hello, ' + name; println(message)");
        obj = engine.get("message");
        System.out.println(obj);
    }

    /**
     * 脚本上下文绑定
     * @throws ScriptException
     */
    @Test
    public void scriptContextBindings() throws  ScriptException {
        ScriptContext context = engine.getContext();
        Bindings bindings1 = engine.createBindings();
        bindings1.put("name","denglt");
        context.setBindings(bindings1, ScriptContext.GLOBAL_SCOPE);
        Bindings bindings2 = engine.createBindings();
        bindings2.put("name","zyy");
        context.setBindings(bindings2, ScriptContext.ENGINE_SCOPE);
        engine.eval("println(name)");

    }

    /**
     * 脚本编译
     * @throws ScriptException
     */
    @Test
    public void compileAndRun() throws ScriptException {
        String scriptText = " var message = 'hello, ' + name; println(message)";
        Bindings bindings1 = engine.createBindings();

        if (engine instanceof Compilable){
            CompiledScript script = ((Compilable)engine).compile(scriptText);
            for (int i = 0; i < 100; i++) {
                bindings1.put("name","denglt"+i);
                script.eval(bindings1);
            }
        }
    }

    /**
     * 调用脚本里的方法
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    @Test
    public void invokeFunction() throws  ScriptException, NoSuchMethodException {
        String scriptText = "function greet(name){" +
                            "    println('Hello, ' + name); " +
                            "}";
        engine.eval(scriptText);
        if (engine instanceof Invocable){
            Invocable invocable = (Invocable)engine;
            invocable.invokeFunction("greet","denglt");
        }
    }

    /**
     * 调用脚本里的对象方法
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    @Test
    public  void invokeMethod() throws ScriptException, NoSuchMethodException {
        String scriptText = "var obj = {\n" +
                        "\tgetGreeting : function (name) {\n" +
                        "\t\treturn 'Hello, ' + name;\n" +
                        "\t}\n" +
                        "};";
        engine.eval(scriptText);
        Invocable invocable = (Invocable)engine;
        Object obj = engine.get("obj");
        Object result = invocable.invokeMethod(obj, "getGreeting","denglt");
        System.out.println(result);
    }

    /**
     * 脚本中实现java定义的interface Greet
     * @throws ScriptException
     */
    @Test
    public void useInterface() throws ScriptException {
        String scriptText = "function getGreeting(name) {\n" +
                "\treturn 'Hello, ' + name;\n" +
                "};\n";
        engine.eval(scriptText);
        Invocable invocable = (Invocable) engine;
        Greet greet = invocable.getInterface(Greet.class); //getInterface(Object thiz, Class<T> clasz);
        System.out.println(greet.getGreeting("denglt"));
    }
}


