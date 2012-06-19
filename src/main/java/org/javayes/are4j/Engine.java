package org.javayes.are4j;

import org.javayes.are4j.invoke.Invocation;
import org.javayes.are4j.resolve.AnnotationResolver;
import org.javayes.are4j.resolve.ClassAnnotationResolver;
import org.javayes.are4j.resolve.ConstructorAnnotationResolver;
import org.javayes.are4j.resolve.FieldAnnotationResolver;
import org.javayes.are4j.resolve.MethodAnnotationResolver;
import org.javayes.are4j.resolve.ParameterAnnotationResolver;
import org.javayes.are4j.resolve.ResolverUtils;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Class to generate proxy object.
 * For example:
 * Engine.registerAnnotation(TestMethodAnnotation.class, new TestMethodResolver());
 * TestObject testObject = Engine.newProxyInstance(TestObject.class);
 *
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Engine {

    private static Logger log = Logger.getLogger(Engine.class.getName());

    // Key is Annotation class, value is Resolver object
    private static final Map<Class<? extends Annotation>, AnnotationResolver> resolverMap = new HashMap<Class<? extends Annotation>, AnnotationResolver>();

    private Engine() {
    }

    public static void registerAnnotation(Class<? extends Annotation> annotationClass, AnnotationResolver annotationResolver) {
        Retention retention = annotationClass.getAnnotation(Retention.class);
        if(retention == null) { // no Retention, default is RetentionPolicy.CLASS
            throw new RuntimeException("Annotation " + annotationClass.getName() + " doesn't have Retention");
        }
        RetentionPolicy retentionPolicy = retention.value();
        if(!retentionPolicy.equals(RetentionPolicy.RUNTIME)) { // not RUNTIME annotation
            throw new RuntimeException("Annotation " + annotationClass.getName() + " 's RetentionPolicy is NOT RetentionPolicy.RUNTIME");
        }
        //check if annotationResolverClass matches annotationClass, such as class level,  needed ClassAnnotationResolver interface
        Target target = annotationClass.getAnnotation(Target.class);
        if(target == null) {
            // If a Target meta-annotation is not present on an
            // annotation type declaration, the declared type may be used on any program element.
            if(!(annotationResolver instanceof ClassAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " doesn't have Target(means can be used for any Target), " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + ClassAnnotationResolver.class.getName());
            }
            if(!(annotationResolver instanceof ConstructorAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " doesn't have Target(means can be used for any Target), " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + ConstructorAnnotationResolver.class.getName());
            }
            if(!(annotationResolver instanceof FieldAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " doesn't have Target(means can be used for any Target), " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + FieldAnnotationResolver.class.getName());
            }
            if(!(annotationResolver instanceof MethodAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " doesn't have Target(means can be used for any Target), " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + MethodAnnotationResolver.class.getName());
            }
            if(!(annotationResolver instanceof ParameterAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " doesn't have Target(means can be used for any Target), " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + ParameterAnnotationResolver.class.getName());
            }
        }
        else {
            ElementType[] elementTypes = annotationClass.getAnnotation(Target.class).value();
            if(elementTypes.length== 0) { // invalid annotation
                throw new RuntimeException("Annotation " + annotationClass.getName() + " has empty Target!");
            }
            List<ElementType> elementTypeList = Arrays.asList(elementTypes);
            if(elementTypeList.contains(ElementType.TYPE) && !(annotationResolver instanceof ClassAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " has Target ElementType.TYPE, " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + ClassAnnotationResolver.class.getName());
            }
            if(elementTypeList.contains(ElementType.CONSTRUCTOR) && !(annotationResolver instanceof ConstructorAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " has Target ElementType.CONSTRUCTOR, " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + ConstructorAnnotationResolver.class.getName());
            }
            if(elementTypeList.contains(ElementType.FIELD) && !(annotationResolver instanceof FieldAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " has Target ElementType.FIELD, " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + FieldAnnotationResolver.class.getName());
            }
            if(elementTypeList.contains(ElementType.METHOD) && !(annotationResolver instanceof MethodAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " has Target ElementType.METHOD, " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + MethodAnnotationResolver.class.getName());
            }
            if(elementTypeList.contains(ElementType.PARAMETER) && !(annotationResolver instanceof ParameterAnnotationResolver)) {
                log.warning("Annotation " + annotationClass.getName() + " has Target ElementType.PARAMETER, " + "But AnnotationResolver " + annotationResolver.getClass().getName() + " doesn't implement interface " + ParameterAnnotationResolver.class.getName());
            }
        }

        resolverMap.put(annotationClass, annotationResolver);
    }

    public static AnnotationResolver getAnnotationResolver(Class<? extends Annotation> annotationClass){
        return resolverMap.get(annotationClass);
    }

    public static void unregisterAnnotation(Class<? extends Annotation>... annotationClasses){
        if(annotationClasses != null) {
            for(Class<? extends Annotation> annotationClass : annotationClasses) {
                resolverMap.remove(annotationClass);
            }
        }
    }

    public static void clearAnnotations(){
        resolverMap.clear();
    }


    /**
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newProxyInstance(Class<? extends T> clazz) {
        // generate proxy Object by Javassist
        return newProxyInstance(clazz, new Class[0], new Object[0]);
    }

    public static <T> T newProxyInstance(Class<? extends T> clazz, Class[] constructorParameterTypes, final Object[] constructorParameters) {
        // AnnotationResolverPairs should not be null or empty
        if(resolverMap.isEmpty()) {
            throw new RuntimeException("No any registered annotation, please use Engine.registerAnnotation to register your annotations first.");
        }
        // generate proxy Object by Javassist
        ProxyFactory pf = new ProxyFactory();
        pf.setSuperclass(clazz);
        try {
            final ResolverUtils resolverUtils = new ResolverUtils(clazz);
            //Resolve class annotations before construction
            resolverUtils.resolveClassAnnotationsBeforeConstruct();
            Constructor constructor = clazz.getConstructor(constructorParameterTypes);
            // get resolved constructor parameters
            Object[] resolvedConstructorParameters = resolverUtils.resolveConstructorParameterAnnotationsBeforeConstruct(constructor, constructorParameters);
            resolvedConstructorParameters = resolverUtils.resolveConstructorAnnotationsBeforeConstruct(constructor, resolvedConstructorParameters);
            // create proxy object
            final T proxy = (T)pf.create(constructorParameterTypes, resolvedConstructorParameters, new MethodHandler() {
                public Object invoke(final Object self, final Method thisMethod, final Method proceedMethod, final Object[] args) throws Throwable {
                    Invocation invocation = new Invocation(self, proceedMethod, thisMethod, args, resolverUtils);
                    invocation.invoke();
                    return invocation.getResult();
                }
            });
            resolverUtils.setProxyObject(proxy);
            //Resolve class annotations, field annotations, method annotations after construction
            resolverUtils.resolveConstructorAnnotationsAfterConstruct(proxy, constructor, constructorParameters);
            resolverUtils.resolveClassAnnotationsAfterConstruct(proxy);
            resolverUtils.resolveFiledAnnotationsAfterConstruct(proxy);
            resolverUtils.resolveMethodAnnotationsAfterConstruct(proxy);
            return proxy;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create proxy instance for class " + clazz.getName(), e);
        }
    }

    /**
     * Return if the class is dynamic generated to be a proxy class
     * @param obj
     * @return
     */
    public static boolean isProxyObject(Object obj){
        return ProxyFactory.isProxyClass(obj.getClass());
    }

    /**
     * Wrapper a common object to be a Engine object
     * @param obj
     * @param processorMap
     * @param <T>
     * @return
     */
    public static <T> T enhanceProxy(T obj, final Map<Annotation, AnnotationResolver> processorMap){
        // 将一个现有的Object增强成支持annotation的代理 object, javassist
        return null;
    }

}
