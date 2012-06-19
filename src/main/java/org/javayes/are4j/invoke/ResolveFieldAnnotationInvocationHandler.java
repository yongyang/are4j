package org.javayes.are4j.invoke;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
//TODO: looks don't need a Filed Annotation InvocationHandler, because don't need to resolve field while invoking method
public class ResolveFieldAnnotationInvocationHandler implements InvocationHandler{

    public void chainInvoke(Invocation invocation) throws Exception {
//        invocation.getResolverUtils().resolveFieldAnnotationsBeforeMethod(invocation.getMethod());
    }

    public void chainReturn(Invocation invocation) throws Exception {
//        invocation.getResolverUtils().resolveFieldAnnotationsAfterMethod(invocation.getMethod());
    }

}
