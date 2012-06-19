package org.javayes.are4j.test.annotations.resolvers;

import org.javayes.are4j.resolve.FieldAnnotationResolver;
import org.javayes.are4j.resolve.ResolverException;
import org.javayes.are4j.test.annotations.TestFieldAnnotation;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class TestFieldResolver implements FieldAnnotationResolver {

    public void resolveFieldAnnotationAfterConstruct(FieldResolveContext fieldResolveContext) throws ResolverException {
        TestFieldAnnotation annotation = (TestFieldAnnotation)fieldResolveContext.getAnnotation();
        String initValue = annotation.initValue();
        String newValue = annotation.newValue();
        Field field = fieldResolveContext.getField();

        try {
            field.setAccessible(true);
            String value = field.get(fieldResolveContext.getObject()).toString();
            if(!initValue.equals(value)) {
                throw new ResolverException("init value is not equal to 'value1'");
            }

            field.set(fieldResolveContext.getObject(), newValue);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
