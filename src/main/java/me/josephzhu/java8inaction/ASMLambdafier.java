package me.josephzhu.java8inaction;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.lang.reflect.Method;
import java.util.function.Function;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_7;

public class ASMLambdafier extends Lambdafier {

  @Override
  protected byte[] generateLambdaGeneratorClass(
      final String className,
      final Class<?> functionalInterface, final Method interfaceMethod,
      final Handle bsmHandle, final Class<?> argumentType) throws Exception {

    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    cw.visit(V1_7, ACC_PUBLIC, className, null, Type.getInternalName(Object.class), new String[]{Type.getInternalName(Function.class)});

    generateDefaultConstructor(cw);
    generateApplyMethod(cw, functionalInterface, interfaceMethod, bsmHandle, argumentType);

    cw.visitEnd();

    return cw.toByteArray();
  }

  private void generateDefaultConstructor(ClassVisitor cv) {
    String desc = Type.getMethodDescriptor(Type.getType(void.class));
    GeneratorAdapter ga = createMethod(cv, ACC_PUBLIC, "<init>", desc);
    ga.loadThis();
    ga.invokeConstructor(Type.getType(Object.class), new org.objectweb.asm.commons.Method("<init>", desc));
    ga.returnValue();
    ga.endMethod();
  }

  private void generateApplyMethod(ClassVisitor cv, Class<?> functionalInterface, Method interfaceMethod, Handle bsmHandle, Class<?> argumentType) {
    final Object[] bsmArgs = new Object[]{Type.getType(interfaceMethod), bsmHandle, Type.getType(interfaceMethod)};
    final String bsmDesc = argumentType != null ?
        Type.getMethodDescriptor(Type.getType(functionalInterface), Type.getType(argumentType)) :
        Type.getMethodDescriptor(Type.getType(functionalInterface));

    GeneratorAdapter ga = createMethod(cv, ACC_PUBLIC, "apply", Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(Object.class)));
    if (argumentType != null) {
      ga.loadArg(0);
      ga.checkCast(Type.getType(argumentType));
    }
    ga.invokeDynamic(interfaceMethod.getName(), bsmDesc, metafactoryHandle, bsmArgs);
    ga.returnValue();
    ga.endMethod();

  }

  private static GeneratorAdapter createMethod(ClassVisitor cv, int access, String name, String desc) {
    return new GeneratorAdapter(cv.visitMethod(access, name, desc, null, null), access, name, desc);
  }
}
