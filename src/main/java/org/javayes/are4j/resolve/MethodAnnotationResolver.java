package org.javayes.are4j.resolve;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface MethodAnnotationResolver extends AnnotationResolver{

    /**
     * Resolve Method annotation before construct the component
     * For example, want to register an annotated method to a registry
     *
     */
//    void resolveMethodAnnotationBeforeConstruct(MethodResolveContext methodResolveContext) throws ResolverException;

    /**
     * Resolve Method annotation after construct the component
     * like: @Inject on a Method
     */
    void resolveMethodAnnotationAfterConstruct(MethodResolveContext methodResolveContext) throws ResolverException;

    /**
     * Resolve Method annotation after before invoking method
     *
     * parameters of method can be replaced by methodResolveContext.setParameters(Object[] params)
     */
    void resolveMethodAnnotationBeforeMethod(MethodResolveContext methodResolveContext) throws ResolverException;

    /**
     * Resolve Method annotation after after method invoked
     *
     * result of method can be replaced by methodResolveContext.setResult(Object result)
     */
    void resolveMethodAnnotationAfterMethod(MethodResolveContext methodResolveContext) throws ResolverException;

    /**
     * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
     */
    public class MethodResolveContext extends ResolveContext {

        private Method method;
        private Object[] parameters;

        public MethodResolveContext(Class clazz, Object obj, Method method, Object[] parameters, Annotation annotation) {
            super(clazz, obj, annotation);
            this.method = method;
            this.parameters = parameters;
        }

        public Method getMethod() {
            return method;
        }

        public Object[] getParameters() {
            return parameters;
        }
    }

    /**
     * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
     */
    public class MethodParameterResolveContext extends ResolveContext {

        private Method method;
        private Object parameterObject;

        public MethodParameterResolveContext(Class clazz, Method method, Object parameterObject, Annotation annotation) {
            super(clazz, null, annotation);
            this.method = method;
            this.parameterObject = parameterObject;
        }

        public Method getMethod() {
            return method;
        }

        public Object getParameterObject() {
            return parameterObject;
        }

        /**
         * Parameter annotations use this method to update parameter object
         * @param parameterObject
         */
        public void setParameterObject(Object parameterObject) {
            this.parameterObject = parameterObject;
        }
    }
}
