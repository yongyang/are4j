package org.javayes.are4j.invoke;

import org.javayes.are4j.resolve.MethodAnnotationResolver;
import org.javayes.are4j.resolve.ResolverUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Invocation {

    private static Logger log = Logger.getLogger(Invocation.class.getName());

    // invocation handles, will invoke one by one as a chain
    protected List<InvocationHandler> handlers = new ArrayList<InvocationHandler>();
    {
        handlers.add(new ResolveClassAnnotationInvocationHandler());
//        handlers.add(new ResolveFieldAnnotationInvocationHandler());
        handlers.add(new ResolveMethodParameterAnnotationInvocationHandler());
        handlers.add(new ResolveMethodAnnotationInvocationHandler());
        handlers.add(new ReflectInvokerInvocationHandler());
    }

    private Object object;
    private Method method;
    // need originMethod to get method parameter annotations
    private Method originMethod;
    private Object[] parameters;
    private Object result;
    private Exception exception;

    private Map<String, Object> userData = new HashMap<String, Object>(5);

    private MethodAnnotationResolver.MethodResolveContext methodResolveContext;

    private ResolverUtils resolverUtils;

    public Invocation(Object object, Method method, Method originMethod, Object[] parameters, ResolverUtils resolverUtils) {
        this.object = object;
        this.method = method;
        this.originMethod = originMethod;
        this.parameters = parameters;
        this.resolverUtils = resolverUtils;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

    public Method getOriginMethod() {
        return originMethod;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }

    private void setException(Exception exception) {
        this.exception = exception;
    }

    public void setUserData(String key, Object value) {
        userData.put(key, value);
    }

    public Object getUserData(String key) {
        return userData.get(key);
    }

    public MethodAnnotationResolver.MethodResolveContext getAnnotationResolveContext() {
        return methodResolveContext;
    }

    public void setAnnotationResolveContext(MethodAnnotationResolver.MethodResolveContext methodResolveContext) {
        this.methodResolveContext = methodResolveContext;
    }

    public Annotation[] getClassAnnotations() {
        return getObject().getClass().getDeclaredAnnotations();
    }

    public Annotation[] getMethodAnnotations() {
        return getMethod().getDeclaredAnnotations();
    }

    public Annotation[][] getParameterAnnotations(){
        return getMethod().getParameterAnnotations();
    }

    /**
     * invoke InvocationHandlers one by one
     */
    public final void invoke() throws Exception {
        Iterator<InvocationHandler> chain = handlers.iterator();
        invokeChain(chain);
    }
    private final void invokeChain(final Iterator<InvocationHandler> chain) throws Exception {
        if(chain.hasNext()) {
            InvocationHandler handler = chain.next();
            try {
//                log.info("Chain - " + handler.getClass().getName() + ".chainInvoke");
                handler.chainInvoke(this);
                invokeChain(chain);
            }
            catch (Exception e) {
//                log.log(Level.SEVERE, "Chain - " + handler.getClass().getName() + " threw exception " + e.getMessage(), e);
                this.setException(e);
                throw e;
            }
            finally {
//                log.info("Chain - " + handler.getClass().getName() + ".chainReturn");
                handler.chainReturn(this);
            }
        }
    }

    public ResolverUtils getResolverUtils() {
        return resolverUtils;
    }
}
