package org.javayes.are4j.test;

import org.javayes.are4j.test.annotations.TestClassAnnotation;
import org.javayes.are4j.test.annotations.TestConstructorAnnotation;
import org.javayes.are4j.test.annotations.TestFieldAnnotation;
import org.javayes.are4j.test.annotations.TestMethodAnnotation;
import org.javayes.are4j.test.annotations.TestParameterAnnotation;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
@TestClassAnnotation
public class TestObject {
    
    private Object field1;

    @TestFieldAnnotation
    private Object field2 = "value1";

    @TestConstructorAnnotation
    public TestObject() {
    }

    public TestObject(@TestParameterAnnotation Object field1) {
        this.field1 = field1;
    }

    public Object getField1() {
        return field1;
    }

    public void setField2(@TestParameterAnnotation(changeValue = "value2_changed")Object field2) {
        this.field2 = field2;
    }

    @TestMethodAnnotation
    public void setField2_2(Object field2) {
        this.field2 = field2;
    }

    public Object getField2() {
        return field2;
    }

}
