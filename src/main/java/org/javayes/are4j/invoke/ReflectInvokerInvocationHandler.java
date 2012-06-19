package org.javayes.are4j.invoke;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ReflectInvokerInvocationHandler implements InvocationHandler{

    public void chainInvoke(Invocation invocation) throws Exception {
        Object result = invocation.getMethod().invoke(invocation.getObject(), invocation.getParameters());
        invocation.setResult(result);
    }

    public void chainReturn(Invocation invocation) throws Exception {

    }

}
