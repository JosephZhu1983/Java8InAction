package me.josephzhu.java8inaction;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;

import java.lang.invoke.LambdaMetafactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.function.Function;

import static org.objectweb.asm.Opcodes.*;

public abstract class Lambdafier {
  /**
   * Generate the byte array for a lambda Generator class implementing the Generator interface.
   * @param className The name of the class
   * @param functionalInterface The functional interface the generator should return an instance of
   * @param interfaceMethod The method of the functional interface
   * @param bsmHandle Method Handle for the lambda factory, referencing the method the lambda should invoke
   * @param argumentType If non-null: the type of the target object on which the method handle should be invoked on
   * @return byte array containing the bytes for defining the class
   * @throws Exception
   */
  protected abstract byte[] generateLambdaGeneratorClass(
      String className,
      Class<?> functionalInterface, Method interfaceMethod,
      Handle bsmHandle, Class<?> argumentType) throws Exception;

  /**
   * Create a lambda of the specified functional interface as if it was a method reference to the specified method
   * with the specified object as the target object to invoke on.
   *
   * @param functionalInterface Functional interface to wrap the method invocations as
   * @param method Method to wrap
   * @param object Object to invoke the method on
   * @return A lambda implementation in the for of an instance of the functional interface
   * @throws Exception
   */
  public final <T> T lambdafyVirtual(Class<T> functionalInterface, Method method, Object object) throws Exception {
    Class<?> declaringClass = method.getDeclaringClass();
    int tag = declaringClass.isInterface() ? H_INVOKEINTERFACE : H_INVOKEVIRTUAL;
    Handle handle = new Handle(tag, Type.getInternalName(declaringClass), method.getName(), Type.getMethodDescriptor(method));

    Class<Function<Object, T>> lambdaGeneratorClass = generateLambdaGeneratorClass(functionalInterface, handle, declaringClass, true);
    return lambdaGeneratorClass.newInstance().apply(object);
  }

  /**
   * Create a lambda of the specified functional interface as if it was a method reference to the specified static method
   *
   * @param functionalInterface Functional interface to wrap the method invocations as
   * @param method Method to wrap
   * @return A lambda implementation in the for of an instance of the functional interface
   * @throws Exception
   */
  public final <T> T lambdafyStatic(Class<T> functionalInterface, Method method) throws Exception {
    Class<?> declaringClass = method.getDeclaringClass();
    Handle handle = new Handle(H_INVOKESTATIC, Type.getInternalName(declaringClass), method.getName(), Type.getMethodDescriptor(method));

    Class<Function<Object, T>> lambdaGeneratorClass = generateLambdaGeneratorClass(functionalInterface, handle, declaringClass, false);
    return lambdaGeneratorClass.newInstance().apply(null);
  }

  /**
   * Create a lambda of the specified functional interface as if it was a method reference to the specified constructor
   *
   * @param functionalInterface Functional interface to wrap the method invocations as
   * @param constructor constructor to wrap
   * @return A lambda implementation in the for of an instance of the functional interface
   * @throws Exception
   */
  public final <T> T lambdafyConstructor(Class<T> functionalInterface, Constructor constructor) throws Exception {
    Class<?> declaringClass = constructor.getDeclaringClass();
    Handle handle = new Handle(H_NEWINVOKESPECIAL, Type.getInternalName(declaringClass), "<init>", Type.getConstructorDescriptor(constructor));

    Class<Function<Object, T>> lambdaGeneratorClass = generateLambdaGeneratorClass(functionalInterface, handle, declaringClass, false);
    return lambdaGeneratorClass.newInstance().apply(null);
  }

  private <T> Class<Function<Object, T>> generateLambdaGeneratorClass(Class<T> functionalInterface, Handle handle, Class<?> methodOwner, boolean requiresTarget) throws Exception {
    String className = "LambdaGenerator" + (long) (Math.random() * Long.MAX_VALUE);

    Package pkg = methodOwner.getPackage();
    if (!pkg.isSealed() && !pkg.getName().startsWith("java."))
      className = pkg.getName() + "." + className;

    className = className.replace('.', '/');

    byte[] bytes = generateLambdaGeneratorClass(className, functionalInterface, getInterfaceMethod(functionalInterface), handle, requiresTarget ? methodOwner : null);

    ClassLoader loader = methodOwner.getClassLoader();
    if (loader == null)
      loader = ClassLoader.getSystemClassLoader();

    return (Class<Function<Object, T>>) defineClass0.invoke(null, loader, className, bytes, 0, bytes.length);
  }

  private static final Method defineClass0 = getDefineClass0();
  protected static final Handle metafactoryHandle = findMetaFactoryHandle();

  private static Method getDefineClass0() {
    try {
      Method m = Proxy.class.getDeclaredMethod("defineClass0", ClassLoader.class, String.class, byte[].class, int.class, int.class);
      m.setAccessible(true);
      return m;
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  private static Handle findMetaFactoryHandle() {
    Method metafactory = null;
    for (Method m : LambdaMetafactory.class.getDeclaredMethods()) {
      if (m.getName().equals("metafactory")) {
        metafactory = m;
        break;
      }
    }
    return new Handle(H_INVOKESTATIC, Type.getInternalName(metafactory.getDeclaringClass()), metafactory.getName(), Type.getMethodDescriptor(metafactory));
  }

  /**
   * Functional interfaces can only have one abstract method, this this and return it.
   * If no methods are found, or multiple abstract methods are found, throws an IllegalArgumentException
   * @param iface Interface to find the method for
   * @return The method for the interface
   */
  private static Method getInterfaceMethod(Class<?> iface) {
    if (iface.isInterface()) {
      Method func = null;

      for (Method m : iface.getMethods()) {
        if (Modifier.isStatic(m.getModifiers()) || !Modifier.isAbstract(m.getModifiers())) {
          continue;
        }

        // Functional interfaces can only have one abstract method
        if (func != null) {
          func = null;
          break;
        }
        func = m;
      }
      if (func != null) {
        return func;
      }
    }

    throw new IllegalArgumentException(iface + " is not a functional interface");
  }
}
