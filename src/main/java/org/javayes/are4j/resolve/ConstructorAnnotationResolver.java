package org.javayes.are4j.resolve;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface ConstructorAnnotationResolver extends AnnotationResolver{
    /**
     * Resolve class annotation before construct the component
     *
     */
    void resolveConstructorAnnotationBeforeConstruct(ConstructorResolveContext constructorResolveContext) throws ResolverException;

    /**
     * Resolve class annotation after the component was constructed
     *
     */
    void resolveConstructorAnnotationAfterConstruct(ConstructorResolveContext constructorResolveContext) throws ResolverException;

    /**
     * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
     */
    public class ConstructorResolveContext extends ResolveContext {

        private Constructor constructor;
        private Object[] parameters;

        public ConstructorResolveContext(Class clazz, Object obj, Constructor constructor, Object[] parameters, Annotation annotation) {
            super(clazz, obj, annotation);
            this.constructor = constructor;
            this.parameters = parameters;
        }

        public Constructor getConstructor() {
            return constructor;
        }

        public Object[] getParameters() {
            return parameters;
        }

        public void setParameters(Object[] parameters) {
            //TODO: check parameter compatibility
            this.parameters = parameters;
        }
    }

    /**
     * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
     */
    public class ConstructorParameterResolveContext extends ResolveContext {

        private Constructor constructor;
        private Object parameterObject;

        public ConstructorParameterResolveContext(Class clazz, Constructor constructor, Object parameterObject, Annotation annotation) {
            super(clazz, null, annotation);
            this.constructor = constructor;
            this.parameterObject = parameterObject;
        }

        public Constructor getConstructor() {
            return constructor;
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
