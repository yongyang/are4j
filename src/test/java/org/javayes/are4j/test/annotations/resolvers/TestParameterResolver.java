package org.javayes.are4j.test.annotations.resolvers;

import org.javayes.are4j.resolve.ConstructorAnnotationResolver;
import org.javayes.are4j.resolve.MethodAnnotationResolver;
import org.javayes.are4j.resolve.ParameterAnnotationResolver;
import org.javayes.are4j.resolve.ResolverException;
import org.javayes.are4j.test.annotations.TestParameterAnnotation;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class TestParameterResolver implements ParameterAnnotationResolver {

    public void resolveConstructorParameterAnnotationBeforeConstruct(ConstructorAnnotationResolver.ConstructorParameterResolveContext constructorParameterResolveContext) throws ResolverException {
        System.out.println(this.getClass().getName() + ", before construct");
        // change the value to "changed_field1_value"
        TestParameterAnnotation annotation = (TestParameterAnnotation)constructorParameterResolveContext.getAnnotation();
        constructorParameterResolveContext.setParameterObject(annotation.changeValue());
    }

    public void resolveMethodParameterAnnotationBeforeMethod(MethodAnnotationResolver.MethodParameterResolveContext methodParameterResolveContext) throws ResolverException {
        System.out.println(this.getClass().getName() + ", before Method " + methodParameterResolveContext.getMethod().getName());
        // change the value to "changed_field1_value"
        TestParameterAnnotation annotation = (TestParameterAnnotation)methodParameterResolveContext.getAnnotation();
        String newValue = annotation.changeValue();
        methodParameterResolveContext.setParameterObject(newValue);
    }
}
