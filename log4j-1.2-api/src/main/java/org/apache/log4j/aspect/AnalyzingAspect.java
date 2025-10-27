package org.apache.log4j.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect  // ← Плагин ищет эту аннотацию
public class AnalyzingAspect {

    @Before("execution(public * *(..))")
    public void logMethodArguments(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
//        if (joinPoint.getTarget() == null) {
//            className = "";
//        } else {
//            className = joinPoint.getTarget().getClass().getSimpleName();
//        }
        Object[] args = joinPoint.getArgs();

        System.out.println("=== METHOD CALL ===");
        System.out.println("Class: " + className);
        System.out.println("Method: " + methodName);

        if (args.length == 0) {
            System.out.println("Arguments: None");
        } else {
            System.out.println("Arguments (" + args.length + "):");
            String[] parameterNames = signature.getParameterNames();
            for (int i = 0; i < args.length; i++) {
                String paramName = (parameterNames != null && i < parameterNames.length)
                        ? parameterNames[i] : "arg" + i;
                System.out.println("  " + paramName + " = " + formatArgument(args[i]));
            }
        }
        System.out.println("===================");
    }

    private String formatArgument(Object arg) {
        if (arg == null) {
            return "null";
        }

        // Для массивов
        if (arg.getClass().isArray()) {
            return formatArray(arg);
        }

        // Для коллекций
        if (arg instanceof java.util.Collection) {
            return formatCollection((java.util.Collection<?>) arg);
        }

        // Для Map
        if (arg instanceof java.util.Map) {
            return formatMap((java.util.Map<?, ?>) arg);
        }

        // Для строк - ограничиваем длину
        if (arg instanceof String) {
            String str = (String) arg;
            return str.length() > 100 ? str.substring(0, 100) + "..." : str;
        }

        return arg.toString();
    }

    private String formatArray(Object array) {
        if (array instanceof Object[]) {
            return java.util.Arrays.toString((Object[]) array);
        } else if (array instanceof int[]) {
            return java.util.Arrays.toString((int[]) array);
        } else if (array instanceof long[]) {
            return java.util.Arrays.toString((long[]) array);
        } else if (array instanceof double[]) {
            return java.util.Arrays.toString((double[]) array);
        } else if (array instanceof boolean[]) {
            return java.util.Arrays.toString((boolean[]) array);
        } else if (array instanceof char[]) {
            return java.util.Arrays.toString((char[]) array);
        } else if (array instanceof byte[]) {
            return java.util.Arrays.toString((byte[]) array);
        } else if (array instanceof float[]) {
            return java.util.Arrays.toString((float[]) array);
        } else {
            return array.toString();
        }
    }

    private String formatCollection(java.util.Collection<?> collection) {
        return "Collection[size=" + collection.size() + "] " + collection.toString();
    }

    private String formatMap(java.util.Map<?, ?> map) {
        return "Map[size=" + map.size() + "] " + map.toString();
    }
}