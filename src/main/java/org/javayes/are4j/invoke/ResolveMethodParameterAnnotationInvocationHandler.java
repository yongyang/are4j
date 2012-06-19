package org.javayes.are4j.invoke;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ResolveMethodParameterAnnotationInvocationHandler implements InvocationHandler{

    public void chainInvoke(Invocation invocation) throws Exception {
        // resolveMethodAnnotationsBeforeMethod will resolve parameter annotations first
        Object[] resolvedParameters = invocation.getResolverUtils().resolveMethodParameterAnnotationsBeforeMethod(invocation.getOriginMethod(),invocation.getParameters());
        invocation.setParameters(resolvedParameters);
    }

    public void chainReturn(Invocation invocation) throws Exception {
        invocation.getResolverUtils().resolveMethodAnnotationsAfterMethod(invocation.getMethod(), invocation.getParameters());
    }

}
