package org.javayes.are4j.test.annotations.resolvers;

import org.javayes.are4j.resolve.ConstructorAnnotationResolver;
import org.javayes.are4j.resolve.ResolverException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class TestConstructorResolver implements ConstructorAnnotationResolver {

    public void resolveConstructorAnnotationAfterConstruct(ConstructorResolveContext constructorResolveContext) throws ResolverException {
        System.out.println(this.getClass().getName() + ", after construct, Object=" + constructorResolveContext.getObject());
    }

    public void resolveConstructorAnnotationBeforeConstruct(ConstructorResolveContext constructorResolveContext) throws ResolverException {
        System.out.println(this.getClass().getName() + ", before construct");
    }
}
