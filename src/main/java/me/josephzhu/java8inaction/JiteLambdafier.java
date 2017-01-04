package me.josephzhu.java8inaction;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.function.Function;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class JiteLambdafier extends Lambdafier {

  @Override
  protected byte[] generateLambdaGeneratorClass(
      final String className,
      final Class<?> functionalInterface, final Method interfaceMethod,
      final Handle bsmHandle, final Class<?> argumentType) throws Exception {

    final Object[] bsmArgs = new Object[] { Type.getType(interfaceMethod), bsmHandle, Type.getType(interfaceMethod) };
    final String bsmDesc = argumentType != null ? sig(functionalInterface, argumentType) : sig(functionalInterface);

    return new JiteClass(className, p(Object.class), new String[] { p(Function.class) }) {{
      defineDefaultConstructor();
      defineMethod("apply", ACC_PUBLIC, sig(Object.class, Object.class), new CodeBlock() {{
        if (argumentType != null) {
          aload(1);
          checkcast(p(argumentType));
        }
        invokedynamic(interfaceMethod.getName(), bsmDesc, metafactoryHandle, bsmArgs);
        areturn();
      }});
    }}.toBytes(JDKVersion.V1_7);
  }
}
