package org.javayes.are4j.resolve;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ResolverException extends Exception {

    public ResolverException() {
    }

    public ResolverException(Throwable cause) {
        super(cause);
    }

    public ResolverException(String message) {
        super(message);
    }

    public ResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
