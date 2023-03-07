package za.net.hanro50;

import java.lang.instrument.Instrumentation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtConstructor;

/**
 * Hello world!
 */

public final class App implements ClassFileTransformer {
    final static String targetClassName = "java.io.File";
    private static List<Patches> patches = new ArrayList<>();

    final static String codeFix = "{System.out.println(\"Rerouting: \"+$1);  if ($1.indexOf(\"minecraft\")>0){$1 = \"/tmp/minecraft\"+ $1.substring($1.indexOf(\"minecraft\") +\"minecraft\".length()); }}";
    static {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("patch.java")));
            String str = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                str += line + "\n";
            }
            System.out.println(str);
            for (String patch : str.split(">FUNCTION")) {
                if (!patch.contains("static public inject"))
                    continue;
                try {
                    patches.add(new Patches(patch));
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) throws ClassNotFoundException {
        System.out.println("[Agent] Loading...");
        Class<?> clazz = Class.forName(targetClassName);
        instrumentation.addTransformer(new App(), true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "[Agent] Transform failed for: [" + clazz.getName() + "]", ex);
        }
    }

    public static String dotToPath(String dot) {
        return dot.replaceAll("\\.", "/");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        byte[] byteCode = classfileBuffer;
        String finalTargetClassName = dotToPath(targetClassName);
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }
        System.out.println("[Agent] Transforming class " + targetClassName);
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.get(targetClassName);
            for (Patches patch : patches) {
                // { cp.get("java/lang/String") };
                CtClass[] cproxy = new CtClass[patch.variables.length];
                for (int i = 0; i < cproxy.length; i++) {
                    cproxy[i] = cp.get(patch.variables[i]);
                }
                if (cproxy[0] == null) {
                    System.out.println("NULL!");
                }
                CtConstructor method = cc.getDeclaredConstructor(cproxy);
                method.insertBefore(patch.body);
            }

            byteCode = cc.toBytecode();
            cc.detach();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return byteCode;
    }

}