package org.javayes.are4j.resolve;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface ClassAnnotationResolver extends AnnotationResolver{
    /**
     * Resolve class annotation before construct the component
     *
     */
    void resolveClassAnnotationBeforeConstruct(ClassResolveContext classResolveContext) throws ResolverException;

    /**
     * Resolve class annotation after the component was constructed
     *
     */
    void resolveClassAnnotationAfterConstruct(ClassResolveContext classResolveContext) throws ResolverException;

    /**
     * Resolve class annotation before invoke a method, for example @Intercept on class
     */
    void resolveClassAnnotationBeforeMethod(ClassResolveContext classResolveContext) throws ResolverException;

    /**
     * Resolve class annotation before a method was invoked
     */
    void resolveClassAnnotationAfterMethod(ClassResolveContext classResolveContext) throws ResolverException;

    /**
     * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
     */
    public class ClassResolveContext extends ResolveContext {

        private Method method;

        public ClassResolveContext(Class clazz, Object obj, Annotation annotation) {
            super(clazz, obj, annotation);
        }

        public Method getMethod() {
            return method;
        }

        // set method while resolve class annotation before/after method
        public void setMethod(Method method) {
            this.method = method;
        }
    }
}
