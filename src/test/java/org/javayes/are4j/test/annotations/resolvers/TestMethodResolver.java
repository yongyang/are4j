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
        methodResolveContext.setParameters(new String[]{"value2_changed"});
    }

    public void resolveMethodAnnotationAfterMethod(MethodResolveContext methodResolveContext) throws ResolverException {

        //TODO: can't invoke method by reflection, cause dead loop
/*
        try {
            methodResolveContext.getOriginMethod().invoke(methodResolveContext.getObject(), "value2_changed");
        }
        catch (Exception e) {
            throw new ResolverException(e);
        }
*/
    }
}
