package org.javayes.are4j.invoke;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ResolveMethodAnnotationInvocationHandler implements InvocationHandler{

    public void chainInvoke(Invocation invocation) throws Exception {
        Object[] resolvedParameters = invocation.getResolverUtils().resolveMethodAnnotationsBeforeMethod(invocation.getObject(), invocation.getOriginMethod(),invocation.getParameters());
        invocation.setParameters(resolvedParameters);
    }

    public void chainReturn(Invocation invocation) throws Exception {
        invocation.getResolverUtils().resolveMethodAnnotationsAfterMethod(invocation.getObject(), invocation.getOriginMethod(), invocation.getParameters());
    }

}
