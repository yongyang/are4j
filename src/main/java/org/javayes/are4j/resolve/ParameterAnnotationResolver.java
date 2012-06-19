package org.javayes.are4j.resolve;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface ParameterAnnotationResolver extends AnnotationResolver {
    /**
     * Resolve parameter annotation before construct
     *
     */
    void resolveConstructorParameterAnnotationBeforeConstruct(ConstructorAnnotationResolver.ConstructorParameterResolveContext constructorParameterResolveContext) throws ResolverException;

    /**
     * Resolve parameter annotation before method
     *
     */
    void resolveMethodParameterAnnotationBeforeMethod(MethodAnnotationResolver.MethodParameterResolveContext methodParameterResolveContext) throws ResolverException;

}
