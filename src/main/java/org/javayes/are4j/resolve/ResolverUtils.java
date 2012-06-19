package org.javayes.are4j.resolve;

import org.javayes.are4j.Engine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ResolverUtils {

    private Class clazz;
    private Object proxyObject;

    private static Logger log = Logger.getLogger(ResolverUtils.class.getName());

    public ResolverUtils(Class clazz) {
        this.clazz = clazz;
    }

    public void setProxyObject(Object proxyObject) {
        this.proxyObject = proxyObject;
    }

    /**
     * Get all declared Fields array, include declared in supper classes.
     * if a field is re-declared in sub-class, will ignore super class one
     *
     * @param clazz class
     */
    private static Field[] getAllDeclaredFields(Class clazz) {
        List<Field> declaredFields = new ArrayList<Field>();
        List<String> declaredFiledNames = new ArrayList<String>();

        Class[] superClasses = getAllSuperclasses(clazz);
        for(Class superClass : superClasses){
            Field[] fields = superClass.getDeclaredFields();
            for(Field field : fields){
                if(!declaredFiledNames.contains(field.getName())) {
                    declaredFields.add(field);
                    declaredFiledNames.add(field.getName());
                }
            }
        }
        return declaredFields.toArray(new Field[declaredFields.size()]);
    }

    private static Class[] getAllSuperclasses(Class cls) {
        if (cls == null) {
            return new Class[0];
        }
        List<Class> classList = new ArrayList<Class>();
        Class superClass = cls;
        while (superClass != null && !Object.class.equals(superClass) && !Class.class.equals(superClass)) { // java.lang.Object not treat as superclass
            classList.add(superClass);
            superClass = superClass.getSuperclass();
        }
        return classList.toArray(new Class[classList.size()]);
    }

    public void resolveClassAnnotationsBeforeConstruct() throws ResolverException {
        for(Annotation classAnnotation : clazz.getAnnotations()){
            ClassAnnotationResolver.ClassResolveContext classResolveContext = new ClassAnnotationResolver.ClassResolveContext(clazz, null, classAnnotation);
//            classResolveContext.setAnnotation(classAnnotation);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(classAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + classAnnotation + ", skip resolve.");
                return;
            }
            else if(annotationResolver instanceof ClassAnnotationResolver){
                log.warning("Resolver " + annotationResolver + " is not for Class annotation " + classAnnotation + ", skip resolve.");
                return;
            }
            else {
                ((ClassAnnotationResolver)annotationResolver).resolveClassAnnotationBeforeConstruct(classResolveContext);
            }
        }
    }

    public void resolveClassAnnotationsAfterConstruct(Object proxy) throws ResolverException {
        for(Annotation classAnnotation : clazz.getAnnotations()){
            ClassAnnotationResolver.ClassResolveContext classResolveContext = new ClassAnnotationResolver.ClassResolveContext(clazz, proxy, classAnnotation);
//            classResolveContext.setAnnotation(classAnnotation);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(classAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + classAnnotation + ", skip resolve.");
                return;
            }
            else if(!(annotationResolver instanceof ClassAnnotationResolver)){
                log.warning("Resolver " + annotationResolver + " is not for Class annotation " + classAnnotation + ", skip resolve.");
                return;
            }
            else {
                ((ClassAnnotationResolver)annotationResolver).resolveClassAnnotationAfterConstruct(classResolveContext);
            }
        }
    }

    public void resolveClassAnnotationsBeforeMethod(Method method) throws ResolverException {
        for(Annotation classAnnotation : clazz.getAnnotations()){
            ClassAnnotationResolver.ClassResolveContext classResolveContext = new ClassAnnotationResolver.ClassResolveContext(clazz, proxyObject, classAnnotation);
//            classResolveContext.setAnnotation(classAnnotation);
            classResolveContext.setMethod(method);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(classAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + classAnnotation + ", skip resolve.");
                return;
            }
            else if(annotationResolver instanceof ClassAnnotationResolver){
                log.warning("Resolver " + annotationResolver + " is not for Class annotation " + classAnnotation + ", skip resolve.");
                return;
            }
            else {
                ((ClassAnnotationResolver)annotationResolver).resolveClassAnnotationBeforeMethod(classResolveContext);
            }
        }
    }

    public void resolveClassAnnotationsAfterMethod(Method method) throws ResolverException {
        for(Annotation classAnnotation : clazz.getAnnotations()){
            ClassAnnotationResolver.ClassResolveContext classResolveContext = new ClassAnnotationResolver.ClassResolveContext(clazz, proxyObject, classAnnotation);
//            classResolveContext.setAnnotation(classAnnotation);
            classResolveContext.setMethod(method);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(classAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + classAnnotation + ", skip resolve.");
                return;
            }
            else if(annotationResolver instanceof ClassAnnotationResolver){
                log.warning("Resolver " + annotationResolver + " is not for Class annotation " + classAnnotation + ", skip resolve.");
                return;
            }
            else {
                ((ClassAnnotationResolver)annotationResolver).resolveClassAnnotationAfterMethod(classResolveContext);
            }
        }
    }

    public void resolveFiledAnnotationsAfterConstruct(Object proxy) throws ResolverException {
        Field[] allDeclaredFields = getAllDeclaredFields(clazz);
        for(Field field : allDeclaredFields){
            Annotation[] fieldAnnotations =  field.getAnnotations();
            if(fieldAnnotations != null && fieldAnnotations.length !=0) {
                for(Annotation filedAnnotation : fieldAnnotations){
                    FieldAnnotationResolver.FieldResolveContext fieldResolveContext = new FieldAnnotationResolver.FieldResolveContext(clazz, proxy, field, filedAnnotation);
//                    fieldResolveContext.setAnnotation(filedAnnotation);
                    AnnotationResolver annotationResolver = Engine.getAnnotationResolver(filedAnnotation.annotationType());
                    if(annotationResolver == null) {
                        log.info("No resolver for " + filedAnnotation + ", skip resolve.");
                        return;
                    }
                    else if(!(annotationResolver instanceof FieldAnnotationResolver)){
                        log.warning("Resolver " + annotationResolver + " is not for Field annotation " + filedAnnotation + ", skip resolve.");
                        return;
                    }
                    else {
                        ((FieldAnnotationResolver)annotationResolver).resolveFieldAnnotationAfterConstruct(fieldResolveContext);
                    }
                }
            }
        }
    }

    public void resolveMethodAnnotationsAfterConstruct(Object proxy) throws ResolverException {
        for(Method method : clazz.getMethods()){
            //method annotations
            Annotation[] methodAnnotations = method.getDeclaredAnnotations();
            if(methodAnnotations != null && methodAnnotations.length !=0) {
                for(Annotation methodAnnotation : methodAnnotations){
                    MethodAnnotationResolver.MethodResolveContext methodResolveContext = new MethodAnnotationResolver.MethodResolveContext(clazz, proxy, method, null, methodAnnotation);
//                    methodResolveContext.setAnnotation(methodAnnotation);
                    AnnotationResolver annotationResolver = Engine.getAnnotationResolver(methodAnnotation.annotationType());
                    if(annotationResolver == null) {
                        log.info("No resolver for " + methodAnnotation + ", skip resolve.");
                        return;
                    }
                    else if(annotationResolver instanceof ClassAnnotationResolver){
                        log.warning("Resolver " + annotationResolver + " is not for Method annotation " + methodAnnotation + ", skip resolve.");
                        return;
                    }
                    else {
                        ((MethodAnnotationResolver)annotationResolver).resolveMethodAnnotationAfterConstruct(methodResolveContext);
                    }
                }
            }
        }
    }

    public Object[] resolveConstructorAnnotationsBeforeConstruct(Constructor constructor, Object[] parameters) throws ResolverException {
        Object[] resolvedParameters = parameters;
        for(Annotation constructorAnnotation : constructor.getAnnotations()){
            ConstructorAnnotationResolver.ConstructorResolveContext constructorResolveContext = new ConstructorAnnotationResolver.ConstructorResolveContext(clazz, null, constructor, resolvedParameters, constructorAnnotation);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(constructorAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + constructorAnnotation + ", skip resolve.");
            }
            else if(!(annotationResolver instanceof ConstructorAnnotationResolver)){
                log.warning("Resolver " + annotationResolver + " is not for Constructor annotation " + constructorAnnotation + ", skip resolve.");
            }
            else {
                ((ConstructorAnnotationResolver)annotationResolver).resolveConstructorAnnotationBeforeConstruct(constructorResolveContext);
                resolvedParameters = constructorResolveContext.getParameters();
            }
        }
        return resolvedParameters;
    }

    public void resolveConstructorAnnotationsAfterConstruct(Object obj, Constructor constructor, Object[] parameters) throws ResolverException {
        for(Annotation constructorAnnotation : constructor.getAnnotations()){
            ConstructorAnnotationResolver.ConstructorResolveContext constructorResolveContext = new ConstructorAnnotationResolver.ConstructorResolveContext(clazz, obj, constructor, parameters, constructorAnnotation);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(constructorAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + constructorAnnotation + ", skip resolve.");
                return;
            }
            else if(!(annotationResolver instanceof ConstructorAnnotationResolver)){
                log.warning("Resolver " + annotationResolver + " is not for Constructor annotation " + constructorAnnotation + ", skip resolve.");
                return;
            }
            else {
                ((ConstructorAnnotationResolver)annotationResolver).resolveConstructorAnnotationAfterConstruct(constructorResolveContext);
            }
        }
    }

    public Object[] resolveConstructorParameterAnnotationsBeforeConstruct(Constructor constructor, Object[] parameters) throws ResolverException {
        //resolve parameter annotations for constructor
        Object[] resolvedParameters = new Object[parameters.length];
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        for(int i=0; i<parameterAnnotations.length; i++){
            resolvedParameters[i] = parameters[i];
            for(int j=0; j<parameterAnnotations[i].length; j++ ){
                Annotation parameterAnnotation = parameterAnnotations[i][j];
                AnnotationResolver annotationResolver = Engine.getAnnotationResolver(parameterAnnotation.annotationType());
                if(annotationResolver == null) {
                    log.info("No resolver for " + parameterAnnotation + ", skip resolve.");
                }
                else if(!(annotationResolver instanceof ParameterAnnotationResolver)){
                    log.warning("Resolver " + annotationResolver + " is not for constructor parameter annotation " + parameterAnnotation + ", skip resolve.");
                }
                else {
                    ConstructorAnnotationResolver.ConstructorParameterResolveContext constructorParameterResolveContext = new ConstructorAnnotationResolver.ConstructorParameterResolveContext(clazz, constructor,  parameters[i], parameterAnnotation);
                    ((ParameterAnnotationResolver)annotationResolver).resolveConstructorParameterAnnotationBeforeConstruct(constructorParameterResolveContext);
                    resolvedParameters[i] = constructorParameterResolveContext.getParameterObject();
                }
            }
        }
        return resolvedParameters;
    }

    public Object[] resolveMethodAnnotationsBeforeMethod(Method method, Object[] parameters) throws ResolverException {
        Object[] resolvedParameters = parameters;
        for(Annotation constructorAnnotation : method.getAnnotations()){
            MethodAnnotationResolver.MethodResolveContext constructorResolveContext = new MethodAnnotationResolver.MethodResolveContext(clazz, null, method, resolvedParameters, constructorAnnotation);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(constructorAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + constructorAnnotation + ", skip resolve.");
            }
            else if(!(annotationResolver instanceof ConstructorAnnotationResolver)){
                log.warning("Resolver " + annotationResolver + " is not for Method annotation " + constructorAnnotation + ", skip resolve.");
            }
            else {
                ((MethodAnnotationResolver)annotationResolver).resolveMethodAnnotationBeforeMethod(constructorResolveContext);
                resolvedParameters = constructorResolveContext.getParameters();
            }
        }
        return resolvedParameters;
    }

    public Object[] resolveMethodParameterAnnotationsBeforeMethod(Method method, Object[] parameters) throws ResolverException {
        //resolve parameter annotations for method
        Object[] resolvedParameters = new Object[parameters.length];
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int i=0; i<parameterAnnotations.length; i++){
            resolvedParameters[i] = parameters[i];
            for(int j=0; j<parameterAnnotations[i].length; j++ ){
                Annotation parameterAnnotation = parameterAnnotations[i][j];
                AnnotationResolver annotationResolver = Engine.getAnnotationResolver(parameterAnnotation.annotationType());
                if(annotationResolver == null) {
                    log.info("No resolver for " + parameterAnnotation + ", skip resolve.");
                }
                else if(!(annotationResolver instanceof ParameterAnnotationResolver)){
                    log.warning("Resolver " + annotationResolver + " is not for method parameter annotation " + parameterAnnotation + ", skip resolve.");
                }
                else {
                    MethodAnnotationResolver.MethodParameterResolveContext methodParameterResolveContext = new MethodAnnotationResolver.MethodParameterResolveContext(clazz, method,  parameters[i], parameterAnnotation);
                    ((ParameterAnnotationResolver)annotationResolver).resolveMethodParameterAnnotationBeforeMethod(methodParameterResolveContext);
                    resolvedParameters[i] = methodParameterResolveContext.getParameterObject();
                }
            }
        }
        return resolvedParameters;
    }

    /**
     *
     * @param method current method to invoke
     * @param parameters parameters resolved by parameter and method annotations
     * @return result
     * @throws org.javayes.are4j.resolve.ResolverException
     */
    public Object[] resolveMethodAnnotationsAfterMethod(Method method, Object[] parameters) throws ResolverException {
        Object[] resolvedParameters = parameters;
        for(Annotation constructorAnnotation : method.getAnnotations()){
            MethodAnnotationResolver.MethodResolveContext constructorResolveContext = new MethodAnnotationResolver.MethodResolveContext(clazz, null, method, resolvedParameters, constructorAnnotation);
            AnnotationResolver annotationResolver = Engine.getAnnotationResolver(constructorAnnotation.annotationType());
            if(annotationResolver == null) {
                log.info("No resolver for " + constructorAnnotation + ", skip resolve.");
            }
            else if(!(annotationResolver instanceof ConstructorAnnotationResolver)){
                log.warning("Resolver " + annotationResolver + " is not for Constructor annotation " + constructorAnnotation + ", skip resolve.");
            }
            else {
                ((MethodAnnotationResolver)annotationResolver).resolveMethodAnnotationBeforeMethod(constructorResolveContext);
                resolvedParameters = constructorResolveContext.getParameters();
            }
        }
        return resolvedParameters;
    }

}
