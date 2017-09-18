package sizeidObject;

/**
 * @author Junqiang.Yang
 * @create 2017-08-23 14:58
 **/

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;


/**
 * 使用上述代码必须将上述代码打成 jar 包， 并且 MANIFEST.MF 文件设置参数（
        Premain-Class: sizeof.agent.SizeOfAgent
        Boot-Class-Path:
        Can-Redefine-Classes: false
     ）
 *
 *
 *
 *
 */


public class SizeOfAgent {
    static Instrumentation instrumentation;

    // 第一个参数由 –javaagent， 第二个参数由 JVM 传入
    public static void premain(String agentArgs, Instrumentation instP) {
        instrumentation = instP;
    }

    // 返回没有子类对象大小的大小
    public static long sizeOf(Object o) {
        if (instrumentation == null) {
            throw new IllegalStateException("Can not access instrumentation environment.\n" +
                    "Please check if jar file containing SizeOfAgent class is \n" +
                    "specified in the java's \"-javaagent\" command line argument.");
        }
        return instrumentation.getObjectSize(o);
    }

    /**
     *
     * 计算复合对象
     * @param obj object to calculate size of
     * @return object size
     */
    public static long fullSizeOf(Object obj) {
        Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
        Stack<Object> stack = new Stack<Object>();

        long result = internalSizeOf(obj, stack, visited);
        while (!stack.isEmpty()) {
            result += internalSizeOf(stack.pop(), stack, visited);
        }
        visited.clear();
        return result;
    }

    // 这个算法使每个对象仅被计算一次， 避免循环引用，即死循环计算
    private static boolean skipObject(Object obj, Map<Object, Object> visited) {
        if (obj instanceof String) {
            // String 池里已有的不再计算
            if (obj == ((String) obj).intern()) {
                return true;
            }
        }
        return (obj == null) // 已有对象不再计算
                || visited.containsKey(obj);
    }

    private static long internalSizeOf(Object obj, Stack<Object> stack,  Map<Object, Object> visited) {

        if (skipObject(obj, visited)){
            return 0;
        }
        visited.put(obj, null);

        long result = 0;
        // get size of object + primitive variables + member pointers
        result += SizeOfAgent.sizeOf(obj);

        // 处理所有数组内容
        Class clazz = obj.getClass();
        if (clazz.isArray()) {
            // [I , [F 基本类型名字长度是2
            if(clazz.getName().length() != 2) {// skip primitive type array
                int length =  Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    stack.add(Array.get(obj, i));
                }
            }
            return result;
        }

        // 处理对象的所有字段
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                // 不重复计算静态类型字段
                if (!Modifier.isStatic(fields[i].getModifiers())) {
                    // 不重复计算原始类型字段
                    if (fields[i].getType().isPrimitive()) {
                        continue;
                    } else {
                        // 使 private 属性可访问
                        fields[i].setAccessible(true);
                        try {
                            // objects to be estimated are put to stack
                            Object objectToAdd = fields[i].get(obj);
                            if (objectToAdd != null) {
                                stack.add(objectToAdd);
                            }
                        } catch (IllegalAccessException ex) {
                            assert false;
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

}