package org.javayes.are4j.resolve;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface AnnotationResolver {

    /**
     * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
     */
    public abstract class ResolveContext {

        /**
         * Component class
         */
        private Class clazz;

        /**
         * Component instance
         */
        private Object object;

        /**
         * annotation want to resolve
         */
        private Annotation annotation;

        /**
         * user data map set user during resolving annotation <br>
         * datas can be transferred among many Class/Method/Field annotation
         */
        private Map<String, Object> userDatas = new HashMap<String, Object>(5);

        public ResolveContext(Class clazz, Object object, Annotation annotation) {
            this.clazz = clazz;
            this.annotation = annotation;
            this.object = object;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public void setAnnotation(Annotation annotation) {
            this.annotation = annotation;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        /**
         * set user data, datas can be transferred among many Class/Method/Field annotation
         */
        public void setUserData(String key, Object value) {
            userDatas.put(key, value);
        }

        /**
         * return user data by key, datas can be transferred among many Class/Method/Field annotation
         */
        public Object getUserData(String key) {
            return userDatas.get(key);
        }

        public void clearUserData(){
            userDatas.clear();
        }
    }
}
