package org.javayes.are4j.test.annotations.resolvers;

import org.javayes.are4j.resolve.MethodAnnotationResolver;
import org.javayes.are4j.resolve.ResolverException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class TestMethodResolver implements MethodAnnotationResolver {

    public void resolveMethodAnnotationAfterConstruct(MethodResolveContext methodResolveContext) throws ResolverException {
        //TODO:
    }

    public void resolveMethodAnnotationBeforeMethod(MethodResolveContext methodResolveContext) throws ResolverException {

    }

    public void resolveMethodAnnotationAfterMethod(MethodResolveContext methodResolveContext) throws ResolverException {
        try {
            methodResolveContext.getMethod().invoke(methodResolveContext.getObject(), "value2_changed");
        }
        catch (Exception e) {
            throw new ResolverException(e);
        }
    }
}
