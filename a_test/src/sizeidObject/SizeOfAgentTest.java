package sizeidObject;

import static sizeidObject.SizeOfAgent.fullSizeOf;
import static sizeidObject.SizeOfAgent.sizeOf;

/**
 * @author Junqiang.Yang
 * @create 2017-08-23 15:00
 **/
public class SizeOfAgentTest {

    public static void main(String[] args) {
        System.out.println("------------------空对象----------------------------");
        // 16 bytes + 0 + 0 = 16  空对象， 只有对象头
        System.out.println("sizeOf(new Object()) = " + sizeOf(new Object()));
        System.out.println("fullSizeOf(new Object()) = " + fullSizeOf(new Object()));

        System.out.println("----------------非空对象含有原始类型、引用类型------------------------------");

        // 16 bytes + 8 + 4 + padding = 32
        System.out.println("sizeOf(new A()) = " + sizeOf(new A()));
        System.out.println("fullSizeOf(new A()) = " + fullSizeOf(new A()));

        // 16 + 4 + padding =24      数据是一个 int
        System.out.println("sizeOf(new Integer(1)) = " + sizeOf(new Integer(1)));

        // (16 + int hash:4 + int hash32:4 + refer char value[]:8 + padding) = 32
        // 静态属性（static）不计算空间，因为所有对象都是共享一块空间的
        // 不同版本JDK可能 String 内部 Field 可能不同，本次测试使用JDK1.7
        System.out.println("sizeOf(new String()) = " + sizeOf(new String()));
        // (16 + 4 + 4 + 8 + padding) + (24 + 0 + padding) = 56
        System.out.println("fullSizeOf(new String()) = " + fullSizeOf(new String()));
        // (16 + 4 + 4 + 8 + padding) = 32
        System.out.println("sizeOf(new String('a')) = " + sizeOf(new String("a")));
        // (16 + 4 + 4 + 8 +padding)  +  (24 + 2 + padding) = 64
        System.out.println("fullSizeOf(new String('a')) = " + fullSizeOf(new String("a")));

        System.out.println("-------------------原始类型数组对象---------------------------");

        // 24 bytes + 0*1 + 0 = 24      数组长度为 0，所以只有对象头的长度
        System.out.println("sizeOf(new byte[0]) = " + sizeOf(new byte[0]));
        System.out.println("fullSizeOf(new byte[0]) = " + fullSizeOf(new byte[0]));

        // 24 + 1*1 + padding = 32
        System.out.println("sizeOf(new byte[1]) = " + sizeOf(new byte[1]));
        System.out.println("fullSizeOf(new byte[1]) = " + fullSizeOf(new byte[1]));

        // 24 + 1*2 + padding = 32
        System.out.println("sizeOf(new char[1]) = " + sizeOf(new char[1]));
        System.out.println("fullSizeOf(new char[1]) = " + fullSizeOf(new char[1]));

        // 24 + 9*1 + padding = 40
        System.out.println("sizeOf(new byte[9]) = " + sizeOf(new byte[9]));
        System.out.println("fullSizeOf(new byte[9]) = " + fullSizeOf(new byte[9]));

        System.out.println("--------------------引用类型数组对象--------------------------");

        // 24 bytes + 0*8 + 0  = 24       数组长度为 0
        System.out.println("sizeOf(new Integer[0]) = " + sizeOf(new Integer[0]));
        System.out.println("fullSizeOf(new Integer[0]) = " + fullSizeOf(new Integer[0]));

        // 24 bytes + 1*8 + 0 = 32    引用对象 64-bit JVM 占用 8 bytes
        System.out.println("sizeOf(new Integer[1]) = " + sizeOf(new Integer[1]));
        System.out.println("fullSizeOf(new Integer[1]) = " + fullSizeOf(new Integer[1]));

        // 24 bytes + 2*8 + padding = 40
        System.out.println("sizeOf(new Integer[1]) = " + sizeOf(new Integer[1]));
        System.out.println("fullSizeOf(new Integer[1]) = " + fullSizeOf(new Integer[1]));

        // 24 + 3*8 + padding = 48
        System.out.println("sizeOf(new Integer[3]) = " + sizeOf(new Integer[3]));
        System.out.println("fullSizeOf(new Integer[3]) = " + fullSizeOf(new Integer[3]));

        System.out.println("-------------------自定义数组对象---------------------------");
        // 16 + (4+8) + padding = 32
        System.out.println("sizeOf(new B()) = " + sizeOf(new B()));
        System.out.println("fullSizeOf(new B()) = " + fullSizeOf(new B()));

        // 24 + 0*8 + padding = 24    引用对象 64-bit JVM 占用 8 bytes,
        // 因为没创建真实的 new B()所以 B类内部数据还未占用空间
        System.out.println("sizeOf(new B[0]) = " + sizeOf(new B[0]));
        System.out.println("fullSizeOf(new B[0]) = " + fullSizeOf(new B[0]));

        // 24 + 1*8 + padding = 32
        System.out.println("sizeOf(new B[1]) = " + sizeOf(new B[1]));
        System.out.println("fullSizeOf(new B[1]) = " + fullSizeOf(new B[1]));

        // 24 + 2*8 + padding = 40
        System.out.println("sizeOf(new B[2]) = " + sizeOf(new B[2]));
        System.out.println("fullSizeOf(new B[2]) = " + fullSizeOf(new B[2]));

        // 24 + 3*8 + padding = 48
        System.out.println("sizeOf(new B[3]) = " + sizeOf(new B[3]));
        System.out.println("fullSizeOf(new B[3]) = " + fullSizeOf(new B[3]));

        System.out.println("-------------------复合对象---------------------------");
        // 16 + (4+8) + padding = 32  sizeOf 只计算单层次占用空间大小
        System.out.println("sizeOf(new C()) = " + sizeOf(new C()));

        // (16 + (4+8) + padding1) + (24 + 2*8 + padding2) + 2*(16 + (4+8) + padding3) = 136
        // 递归计算当前对象占用空间总大小，包括当前类和超类的实例字段大小以及实例字段引用对象大小
        System.out.println("fullSizeOf(new C()) = " + fullSizeOf(new C()));

        System.out.println("-------------------继承关系---------------------------");
        // 涉及继承关系的时候有一个最基本的规则：首先存放父类中的成员，接着才是子类中的成员, 父类也要按照 8 byte 规定
        // 16 + 1 + padding = 24
        System.out.println("sizeOf(new D()) = " + sizeOf(new D()));
        System.out.println("fullSizeOf(new D()) = " + fullSizeOf(new D()));
        // 16 + 父类(1 + padding1) + 1 + padding2 = 32
        System.out.println("sizeOf(new E()) = " + sizeOf(new E()));
        System.out.println("fullSizeOf(new E()) = " + fullSizeOf(new E()));

    }

    public static class A {
        int a;
        Integer b;
    }

    public static class B {
        int a;
        Integer b;
    }

    public static class C{
        int c;
        B[] b = new B[2];

        // 初始化
        C() {
            for (int i = 0; i < b.length; i++) {
                b[i] = new B();
            }
        }
    }

    public static class D {
        byte d1;
    }

    public static class E extends D {
        byte e1;
    }

}