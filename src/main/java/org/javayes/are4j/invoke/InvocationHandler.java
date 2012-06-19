package org.javayes.are4j.invoke;

/**
 * InvocationHandler is a invocation processor of the invocation chain. <br>
 * When invoking a method of one component, container will use InvocationHandler chain to handle it. <br>
 * Default, Container use AnnotationResolverInvocationHandler and MethodInvocationHandler to handle every invocation.<br>
 * You can add your specified InvocationHandler to container by change {@link code.google.jcontainer.annotation.Container#invocationHandlers()},
 * but must reserve these two in the end, adding your InvocationHandler can extend the ability for your container.
 * <p>
 * <pre>
 * For example:
 * &#64;Container(
 *       name = "SimpleContainer",
 *       ......
 *       invocationHandlers = {MyInvocationHandler.class,
 *                             AnnotationResolverInvocationHandler.class,
 *                             MethodInvocationHandler.class}
 * )
 * </pre>
 * </p>
 *
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface InvocationHandler {

    /**
     * start to invoke this InvocationHandler in the chain
     *
     * @param invocation
     * @throws Exception
     */
    void chainInvoke(Invocation invocation) throws Exception;

    /**
     * Chain return to this InvocationHandler, do something needed
     * ex: clear resources created in chainInvoke
     *
     * @param invocation
     * @throws Exception
     */
    void chainReturn(Invocation invocation) throws Exception;

}

