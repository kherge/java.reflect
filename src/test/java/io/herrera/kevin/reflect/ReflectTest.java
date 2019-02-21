package io.herrera.kevin.reflect;

import static io.herrera.kevin.reflect.Reflect.findField;
import static io.herrera.kevin.reflect.Reflect.findMethod;
import static io.herrera.kevin.reflect.Reflect.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
     * Verify that the instance method for a class is found and is accessible.
     */
    @Test
    public void findInstanceMethodInClassTest() throws Exception {
        Beta object = new Beta();
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
        assertEquals("super instance field", getFieldValue(new Beta(), "superInstanceField"));
    }

    /**
     * Verify that the value of a static field is returned.
     */
    @Test
    public void getStaticFieldValueTest() {
        assertEquals("super static field", getFieldValue(Beta.class, "superStaticField"));
    }

    /**
     * Initializes the object under test.
     */
    @BeforeEach
    public void setUp() {
        object = new Beta();
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
