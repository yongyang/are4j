package org.javayes.are4j.invoke;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ResolveClassAnnotationInvocationHandler implements InvocationHandler{

    public void chainInvoke(Invocation invocation) throws Exception {
        invocation.getResolverUtils().resolveClassAnnotationsBeforeMethod(invocation.getMethod());
    }

    public void chainReturn(Invocation invocation) throws Exception {
        invocation.getResolverUtils().resolveClassAnnotationsAfterMethod(invocation.getMethod());
    }

}
