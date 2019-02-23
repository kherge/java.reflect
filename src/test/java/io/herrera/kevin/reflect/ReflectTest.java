package io.herrera.kevin.reflect;

import static io.herrera.kevin.reflect.Reflect.findAnyMethod;
import static io.herrera.kevin.reflect.Reflect.findField;
import static io.herrera.kevin.reflect.Reflect.findMethod;
import static io.herrera.kevin.reflect.Reflect.getFieldValue;
import static io.herrera.kevin.reflect.Reflect.invokeAnyMethod;
import static io.herrera.kevin.reflect.Reflect.invokeMethod;
import static io.herrera.kevin.reflect.Reflect.setFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that the reflection utilities function as intended.
 */
public class ReflectTest {

    /**
     * The object under test.
     */
    private Beta object;

    /**
     * Verify that an exception is thrown if no method is found.
     */
    @Test
    public void findAnyInstanceMethodExceptionTest() {
        assertThrows(NoSuchMethodException.class, () -> findAnyMethod(object, "doesNotExist"));
    }

    /**
     * Verify that the first method is returned for a class.
     */
    @Test
    public void findAnyInstanceMethodInClassTest() throws Exception {
        Method method = findAnyMethod(object, "instanceMethod");

        assertNotNull(method);
        assertSame(Beta.class, method.getDeclaringClass());
        assertEquals("instance method", method.invoke(object));
    }

    /**
     * Verify that the first method is returned for a superclass.
     */
    @Test
    public void findAnyInstanceMethodInSuperclassTest() throws Exception {
        Method method = findAnyMethod(object, "superInstanceMethod");

        assertNotNull(method);
        assertSame(Alpha.class, method.getDeclaringClass());
        assertEquals("super instance method", method.invoke(object));
    }

    /**
     * Verify that an exception is thrown if no method is found.
     */
    @Test
    public void findAnyStaticMethodExceptionTest() {
        assertThrows(NoSuchMethodException.class, () -> findAnyMethod(Beta.class, "doesNotExist"));
    }

    /**
     * Verify that the first method is returned for a class.
     */
    @Test
    public void findAnyStaticMethodInClassTest() throws Exception {
        Method method = findAnyMethod(object, "staticMethod");

        assertNotNull(method);
        assertSame(Beta.class, method.getDeclaringClass());
        assertEquals("static method", method.invoke(null));
    }

    /**
     * Verify that the first method is returned for a superclass.
     */
    @Test
    public void findAnyStaticMethodInSuperclassTest() throws Exception {
        Method method = findAnyMethod(object, "superStaticMethod");

        assertNotNull(method);
        assertSame(Alpha.class, method.getDeclaringClass());
        assertEquals("super static method", method.invoke(null));
    }

    /**
     * Verify that an exception is thrown if an instance field is not found.
     */
    @Test
    public void findInstanceFieldExceptionTest() {
        assertThrows(NoSuchFieldException.class, () -> findField(object, "doesNotExist"));
    }

    /**
     * Verify that the instance field for a class is found and is accessible.
     */
    @Test
    public void findInstanceFieldInClassTest() throws Exception {
        Field field = findField(object, "instanceField");

        assertNotNull(field);
        assertSame(Beta.class, field.getDeclaringClass());
        assertEquals("instance field", field.get(object));
    }

    /**
     * Verify that the instance field for a superclass is found and is accessible.
     */
    @Test
    public void findInstanceFieldInSuperclassTest() throws Exception {
        Field field = findField(object, "superInstanceField");

        assertNotNull(field);
        assertSame(Alpha.class, field.getDeclaringClass());
        assertEquals("super instance field", field.get(object));
    }

    /**
     * Verify that an exception is thrown if an instance method is not found.
     */
    @Test
    public void findInstanceMethodExceptionTest() {
        assertThrows(NoSuchMethodException.class, () -> findMethod(object, "doesNotExist"));
    }

    /**
     * Verify that the instance method for a class is found and is accessible.
     */
    @Test
    public void findInstanceMethodInClassTest() throws Exception {
        Method method = findMethod(object, "instanceMethod");

        assertNotNull(method);
        assertSame(Beta.class, method.getDeclaringClass());
        assertEquals("instance method", method.invoke(object));
    }

    /**
     * Verify that the instance method for a superclass is found and is accessible.
     */
    @Test
    public void findInstanceMethodInSuperclassTest() throws Exception {
        Method method = findMethod(object, "superInstanceMethod");

        assertNotNull(method);
        assertSame(Alpha.class, method.getDeclaringClass());
        assertEquals("super instance method", method.invoke(object));
    }

    /**
     * Verify that an exception is thrown if a static field is not found.
     */
    @Test
    public void findStaticFieldExceptionTest() {
        assertThrows(NoSuchFieldException.class, () -> findField(Beta.class, "doesNotExist"));
    }

    /**
     * Verify that the static field for a class is found and is accessible.
     */
    @Test
    public void findStaticFieldInClassTest() throws Exception {
        Field field = findField(Beta.class, "staticField");

        assertNotNull(field);
        assertSame(Beta.class, field.getDeclaringClass());
        assertEquals("static field", field.get(null));
    }

    /**
     * Verify that the static field for a superclass is found and is accessible.
     */
    @Test
    public void findStaticFieldInSuperclassTest() throws Exception {
        Field field = findField(Beta.class, "superStaticField");

        assertNotNull(field);
        assertSame(Alpha.class, field.getDeclaringClass());
        assertEquals("super static field", field.get(null));
    }

    /**
     * Verify that an exception is thrown if a static method is not found.
     */
    @Test
    public void findStaticMethodExceptionTest() {
        assertThrows(NoSuchMethodException.class, () -> findMethod(Beta.class, "doesNotExist"));
    }

    /**
     * Verify that the static method for a class is found and is accessible.
     */
    @Test
    public void findStaticMethodInClassTest() throws Exception {
        Method method = findMethod(Beta.class, "staticMethod");

        assertNotNull(method);
        assertSame(Beta.class, method.getDeclaringClass());
        assertEquals("static method", method.invoke(null));
    }

    /**
     * Verify that the static method for a superclass is found and is accessible.
     */
    @Test
    public void findStaticMethodInSuperclassTest() throws Exception {
        Method method = findMethod(Beta.class, "superStaticMethod");

        assertNotNull(method);
        assertSame(Alpha.class, method.getDeclaringClass());
        assertEquals("super static method", method.invoke(null));
    }

    /**
     * Verify that the value of an instance field is returned.
     */
    @Test
    public void getInstanceFieldValueTest() {
        assertEquals("super instance field", getFieldValue(object, "superInstanceField"));
    }

    /**
     * Verify that the value of a static field is returned.
     */
    @Test
    public void getStaticFieldValueTest() {
        assertEquals("super static field", getFieldValue(Beta.class, "superStaticField"));
    }

    /**
     * Verify that the first found method is invoked.
     */
    @Test
    public void invokeAnyInstanceMethodTest() {
        assertEquals("super instance method", invokeAnyMethod(object, "superInstanceMethod"));
    }

    /**
     * Verify that the invocation target exception is rethrown.
     */
    @Test
    public void invokeAnyInstanceMethodExceptionTest() {
        assertThrows(
            Exception.class,
            () -> invokeAnyMethod(Beta.class, "superStaticExceptionMethod")
        );
    }

    /**
     * Verify that the first found method is invoked.
     */
    @Test
    public void invokeAnyStaticMethodTest() {
        assertThrows(
            Exception.class,
            () -> invokeAnyMethod(Beta.class, "superStaticExceptionMethod")
        );
    }

    /**
     * Verify that the invocation target exception is rethrown.
     */
    @Test
    public void invokeAnyStaticMethodExceptionTest() {
        assertEquals("super static method", invokeMethod(Beta.class, "superStaticMethod"));
    }

    /**
     * Verify that the instance method is invoked.
     */
    @Test
    public void invokeInstanceMethodTest() {
        assertEquals("super instance method", invokeMethod(object, "superInstanceMethod"));
    }

    /**
     * Verify that the invocation target exception is rethrown.
     */
    @Test
    public void invokeMethodThrowInvocationTargetExceptionTest() {
        assertThrows(Exception.class, () -> invokeMethod(Beta.class, "superStaticExceptionMethod"));
    }

    /**
     * Verify that the static method is invoked.
     */
    @Test
    public void invokeStaticMethodTest() {
        assertEquals("super static method", invokeMethod(Beta.class, "superStaticMethod"));
    }

    /**
     * Verify that the value of an instance field is set.
     */
    @Test
    public void setInstanceFieldValueTest() {
        String value = "new value";

        setFieldValue(object, "superInstanceField", value);

        assertEquals(value, getFieldValue(object, "superInstanceField"));
    }

    /**
     * Verify that the value of a static field is set.
     */
    @Test
    public void setStaticFieldValueTest() {
        String value = "new value";

        setFieldValue(Beta.class, "superStaticField", value);

        assertEquals(value, getFieldValue(Beta.class, "superStaticField"));
    }

    /**
     * Initializes the object under test.
     */
    @BeforeEach
    public void setUp() {
        object = new Beta();
    }

    /**
     * Resets modified values.
     */
    @AfterEach
    public void tearDown() {
        Alpha.reset();
        Beta.reset();
    }

    /**
     * A superclass used for testing.
     */
    public static class Alpha {

        /**
         * A superclass instance field.
         */
        private String superInstanceField = "super instance field";

        /**
         * A superclass static field.
         */
        private static String superStaticField = "super static field";

        /**
         * Resets the static instance fields.
         */
        public static void reset() {
            superStaticField = "super static field";
        }

        /**
         * A super class instance method.
         */
        private String superInstanceMethod() {
            return "super instance method";
        }

        /**
         * A super class static method.
         */
        private static String superStaticMethod() {
            return "super static method";
        }

        /**
         * A super class static exception method.
         */
        private static void superStaticExceptionMethod() throws Exception {
            throw new Exception("A method exception.");
        }
    }

    /**
     * A class used for testing.
     */
    public static class Beta extends Alpha {

        /**
         * An instance field.
         */
        private String instanceField = "instance field";

        /**
         * A static field.
         */
        private static String staticField = "static field";

        /**
         * Resets the static instance fields.
         */
        public static void reset() {
            staticField = "static field";
        }

        /**
         * An instance method.
         */
        private String instanceMethod() {
            return "instance method";
        }

        /**
         * A static method.
         */
        private static String staticMethod() {
            return "static method";
        }
    }
}
