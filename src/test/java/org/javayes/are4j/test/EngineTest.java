package org.javayes.are4j.test;

import org.javayes.are4j.Engine;
import org.javayes.are4j.test.annotations.EmptyTargetAnnotation;
import org.javayes.are4j.test.annotations.NoRetentionAnnotation;
import org.javayes.are4j.test.annotations.NoTargetAnnotation;
import org.javayes.are4j.test.annotations.NotRuntimePolicyAnnotation;
import org.javayes.are4j.test.annotations.TestFieldAnnotation;
import org.javayes.are4j.test.annotations.TestMethodAnnotation;
import org.javayes.are4j.test.annotations.resolvers.TestClassResolver;
import org.javayes.are4j.test.annotations.TestConstructorAnnotation;
import org.javayes.are4j.test.annotations.resolvers.TestConstructorResolver;
import org.javayes.are4j.test.annotations.TestParameterAnnotation;
import org.javayes.are4j.test.annotations.resolvers.TestFieldResolver;
import org.javayes.are4j.test.annotations.resolvers.TestMethodResolver;
import org.javayes.are4j.test.annotations.resolvers.TestParameterResolver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class EngineTest {

    @BeforeClass
    public static void setupBeforeClass(){

    }

    @AfterClass
    public static void teardownAfterClass() {
        Engine.clearAnnotations();
    }

    @Before
    public void setupBeforeMethod(){

    }

    @After
    public void teardownAfterMethod() {
        Engine.clearAnnotations();
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyResolverMap() throws Exception {
        Engine.clearAnnotations();
        Engine.newProxyInstance(TestObject.class);
    }

    @Test(expected = RuntimeException.class)
    public void testNoRetentionAnnotation() throws Exception {
        Engine.registerAnnotation(NoRetentionAnnotation.class, new TestClassResolver());
        Engine.newProxyInstance(TestObject.class);
    }

    @Test(expected = RuntimeException.class)
    public void testNotRuntimePolicyAnnotation() throws Exception {
        Engine.registerAnnotation(NotRuntimePolicyAnnotation.class, new TestClassResolver());
        Engine.newProxyInstance(TestObject.class);
    }

    @Test
    public void testNoTargetAnnotation() throws Exception {
        Engine.registerAnnotation(NoTargetAnnotation.class, new TestClassResolver());
        Engine.newProxyInstance(TestObject.class);
        //WARN log
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyTargetAnnotation() throws Exception {
        Engine.registerAnnotation(EmptyTargetAnnotation.class, new TestClassResolver());
        Engine.newProxyInstance(TestObject.class);
    }


    @Test
    public void testIsProxyClass() throws Exception{
        Engine.registerAnnotation(TestConstructorAnnotation.class, new TestConstructorResolver());
        TestObject testObject = Engine.newProxyInstance(TestObject.class);
        Assert.assertTrue(Engine.isProxyObject(testObject));

    }

    @Test
    public void testConstructAnnotation() throws Exception{
        Engine.registerAnnotation(TestConstructorAnnotation.class, new TestConstructorResolver());
        TestObject testObject = Engine.newProxyInstance(TestObject.class);
        Assert.assertEquals(null, testObject.getField1());
    }

    @Test
    public void testConstructWithParameterAnnotation() throws Exception{
        Engine.registerAnnotation(TestConstructorAnnotation.class, new TestConstructorResolver());
        Engine.registerAnnotation(TestParameterAnnotation.class, new TestParameterResolver());
        TestObject testObject = Engine.newProxyInstance(TestObject.class, new Class[]{Object.class}, new Object[]{"field1_value"});
        // "field1_value" will be changed to "changed_field1_value" by  TestParameterAnnotation

        Assert.assertEquals("changed_field1_value", testObject.getField1());
    }

    @Test
    public void testConstructWithParameterAnnotation2() throws Exception{
        Engine.registerAnnotation(TestParameterAnnotation.class, new TestParameterResolver());
        TestObject testObject = Engine.newProxyInstance(TestObject.class, new Class[]{Object.class}, new Object[]{"field1_value"});
        // "field1_value" will be changed to "changed_field1_value" by  TestParameterAnnotation
        Assert.assertEquals("changed_field1_value", testObject.getField1());
    }

    @Test
    public void testFieldAnnotation() throws Exception{
        Engine.registerAnnotation(TestFieldAnnotation.class, new TestFieldResolver());
        TestObject testObject = Engine.newProxyInstance(TestObject.class);
        // TestFieldResolver will check the initvalue and set value to "value2"
        Assert.assertEquals("value2", testObject.getField2());
    }

    @Test
    public void testMethodParameterAnnotation() throws Exception{
        Engine.registerAnnotation(TestParameterAnnotation.class, new TestParameterResolver());
        TestObject testObject = Engine.newProxyInstance(TestObject.class);
        testObject.setField2("");
        // TestFieldResolver will check the initvalue and set value to "value2"
        Assert.assertEquals("value2_changed", testObject.getField2());
    }

    @Test
    public void testMethodAnnotation() throws Exception{
        Engine.registerAnnotation(TestMethodAnnotation.class, new TestMethodResolver());
        TestObject testObject = Engine.newProxyInstance(TestObject.class);
        //TODO:
        testObject.setField2_2("");
        // TestFieldResolver will check the init value and set value to "value2"
        Assert.assertEquals("value2_changed", testObject.getField2());
    }

    @Test
    public void testUnregisterAnnotation(){
        Engine.registerAnnotation(TestMethodAnnotation.class, new TestMethodResolver());
        Engine.unregisterAnnotation(TestMethodAnnotation.class);
        Assert.assertNull(Engine.getAnnotationResolver(TestMethodAnnotation.class));
    }
}

