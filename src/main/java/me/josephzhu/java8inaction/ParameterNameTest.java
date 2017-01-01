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
