package me.josephzhu.java8inaction;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by zhuye on 01/01/2017.
 */
public class ParameterNameTest
{
    public static void main(String[] args) throws NoSuchMethodException
    {
        /*
        {public java.lang.String java.lang.Object.toString()=[], public static void me.josephzhu.java8inaction.ParameterNameTest.main(java.lang.String[]) throws java.lang.NoSuchMethodException=[true:args], public final native void java.lang.Object.notify()=[], public final void java.lang.Object.wait() throws java.lang.InterruptedException=[], public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException=[false:arg0], public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException=[false:arg0, false:arg1], public void me.josephzhu.java8inaction.ParameterNameTest.test2(java.lang.Long)=[true:adxsadasdad], public final native java.lang.Class java.lang.Object.getClass()=[], public native int java.lang.Object.hashCode()=[], public final native void java.lang.Object.notifyAll()=[], public boolean java.lang.Object.equals(java.lang.Object)=[false:arg0], public void me.josephzhu.java8inaction.ParameterNameTest.test(java.lang.String,java.lang.Integer)=[true:aa, true:bb]}
         */
        System.out.println(Arrays.stream(ParameterNameTest.class.getMethods())
                .collect(Collectors.toMap(method -> method, method -> Arrays.stream(method.getParameters())
                        .map(p -> p.isNamePresent() + ":" + p.getName()).collect(Collectors.toList()))));
    }

    public void test(String aa, Integer bb)
    {

    }

    public void test2(Long adxsadasdad)
    {

    }
}
