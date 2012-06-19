package org.javayes.are4j.resolve;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface FieldAnnotationResolver extends AnnotationResolver{

    /**
     * Resolve Field annotation before construct the component <br>
     * For example, want to register an annotated field to a registry
     *
     * TODO: 觉得没有必要在 construct 之前解析 filed annotations
     */
//    void resolveFieldAnnotationBeforeConstruct(FieldResolveContext fieldResolveContext) throws ResolverException;

    /**
     * Resolve Field annotation after construct the component
     * like: @Inject on a Field
     */
    void resolveFieldAnnotationAfterConstruct(FieldResolveContext fieldResolveContext) throws ResolverException;

    /**
     * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
     */
    public class FieldResolveContext extends ResolveContext {

        private Field field;

        public FieldResolveContext(Class clazz, Object obj, Field field, Annotation annotation) {
            super(clazz, obj, annotation);
            this.field = field;
        }

        public Field getField() {
            return field;
        }
    }
}
