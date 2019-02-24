package io.herrera.kevin.reflect;

import static io.herrera.kevin.reflect.Reflect.findAnyMethod;
import static io.herrera.kevin.reflect.Reflect.findField;
import static io.herrera.kevin.reflect.Reflect.findMethod;
import static io.herrera.kevin.reflect.Reflect.getFieldValue;
import static io.herrera.kevin.reflect.Reflect.invokeAnyMethod;
import static io.herrera.kevin.reflect.Reflect.invokeMethod;
import static io.herrera.kevin.reflect.Reflect.setFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifies that the reflection utilities function as intended.
 */
public class ReflectTest {

    /**
     * The object under test.
     */
    private Beta object;

    /**
     * Verify that the first method is returned.
     */
    @Test
    public void anyMethodTest() {
        assertEquals(
            findAnyMethod(Beta.class, "superInstanceMethod"),
            Reflect.on(Beta.class).anyMethod("superInstanceMethod")
        );

        assertEquals(
            findAnyMethod(object, "superInstanceMethod"),
            Reflect.on(object).anyMethod("superInstanceMethod")
        );
    }

    /**
     * Verify that the field is returned.
     */
    @Test
    public void fieldTest() {
        assertEquals(
            findField(Beta.class, "superInstanceField"),
            Reflect.on(Beta.class).field("superInstanceField")
        );

        assertEquals(
            findField(object, "superInstanceField"),
            Reflect.on(object).field("superInstanceField")
        );
    }

    /**
     * Verify that the first method is returned.
     */
    @ParameterizedTest
    @ValueSource(classes = { Alpha.class, Beta.class, ReflectTest.class })
    public void findAnyMethodTest(Class<?> clazz) throws Exception {
        if (clazz == ReflectTest.class) {
            Method method = findAnyMethod(object, "superInstanceMethod");

            assertEquals("super instance method: test", method.invoke(object, "test"));
        } else {
            Method method = findAnyMethod(clazz, "superStaticMethod");

            assertEquals("super static method: test", method.invoke(null, "test"));
        }
    }

    /**
     * Verify that an exception is thrown if no method is found.
     */
    @Test
    public void findAnyMethodExceptionTest() {
        assertThrows(NoSuchMethodException.class, () -> findAnyMethod(object, "doesNotExist"));
    }

    /**
     * Verify that the field is returned.
     */
    @ParameterizedTest
    @ValueSource(classes = { Alpha.class, Beta.class, ReflectTest.class })
    public void findFieldTest(Class<?> clazz) throws Exception {
        if (clazz == ReflectTest.class) {
            Field field = findField(object, "superInstanceField");

            assertEquals("super instance field", field.get(object));
        } else {
            Field field = findField(clazz, "superStaticField");

            assertEquals("super static field", field.get(null));
        }
    }

    /**
     * Verify that an exception is thrown if the field is found.
     */
    @Test
    public void findFieldExceptionTest() {
        assertThrows(NoSuchFieldException.class, () -> findField(object, "doesNotExist"));
    }

    /**
     * Verify that the method is returned.
     */
    @ParameterizedTest
    @ValueSource(classes = { Alpha.class, Beta.class, ReflectTest.class })
    public void findMethodTest(Class<?> clazz) throws Exception {
        if (clazz == ReflectTest.class) {
            Method method = findMethod(object, "superInstanceMethod", String.class);

            assertEquals("super instance method: test", method.invoke(object, "test"));
        } else {
            Method method = findMethod(clazz, "superStaticMethod", String.class);

            assertEquals("super static method: test", method.invoke(null, "test"));
        }
    }

    /**
     * Verify that an exception is thrown if the method is not found.
     */
    @Test
    public void findMethodExceptionTest() {
        assertThrows(NoSuchMethodException.class, () -> findMethod(object, "doesNotExist"));
    }

    /**
     * Verify that the field value is returned.
     */
    @Test
    public void getTest() {
        assertEquals(
            getFieldValue(Beta.class, "superStaticField"),
            Reflect.on(Beta.class).get("superStaticField")
        );

        assertEquals(
            getFieldValue(object, "superInstanceField"),
            Reflect.on(object).get("superInstanceField")
        );
    }

    /**
     * Verify that the field value is returned.
     */
    @Test
    public void getFieldValueTest() {
        assertEquals("super static field", getFieldValue(Beta.class, "superStaticField"));
        assertEquals("super instance field", getFieldValue(object, "superInstanceField"));
    }

    /**
     * Verify that the method is invoked and the result is returned.
     */
    @Test
    public void invokeTest() {
        assertEquals(
            invokeMethod(Beta.class, "superStaticMethod", "test"),
            Reflect.on(Beta.class).invoke("superStaticMethod", "test")
        );

        assertEquals(
            invokeMethod(object, "superStaticMethod", "test"),
            Reflect.on(object).invoke("superStaticMethod", "test")
        );
    }

    /**
     * Verify that the method is invoked and the result is returned.
     */
    @Test
    public void invokeAnyTest() {
        assertEquals(
            invokeAnyMethod(Beta.class, "superStaticMethod", "test"),
            Reflect.on(Beta.class).invokeAny("superStaticMethod", "test")
        );

        assertEquals(
            invokeAnyMethod(object, "superStaticMethod", "test"),
            Reflect.on(object).invokeAny("superStaticMethod", "test")
        );
    }

    /**
     * Verify that the first method is invoked and the result is returned.
     */
    @ParameterizedTest
    @ValueSource(classes = { Alpha.class, Beta.class, ReflectTest.class })
    public void invokeAnyMethodTest(Class<?> clazz) {
        if (clazz == ReflectTest.class) {
            assertEquals(
                "super instance method: test",
                invokeAnyMethod(object, "superInstanceMethod", "test")
            );
        } else {
            assertEquals(
                "super static method: test",
                invokeAnyMethod(clazz, "superStaticMethod", "test")
            );
        }
    }

    /**
     * Verify that the method exception is rethrown.
     */
    @Test
    public void invokeAnyMethodExceptionTest() {
        assertThrows(
            AlphaException.class,
            () -> invokeAnyMethod(Beta.class, "superStaticExceptionMethod")
        );

        assertThrows(
            AlphaException.class,
            () -> invokeAnyMethod(object, "superInstanceExceptionMethod")
        );
    }

    /**
     * Verify that the method is invoked and the result is returned.
     */
    @ParameterizedTest
    @ValueSource(classes = { Alpha.class, Beta.class, ReflectTest.class })
    public void invokeMethodTest(Class<?> clazz) {
        if (clazz == ReflectTest.class) {
            assertEquals(
                "super instance method: test",
                invokeMethod(object, "superInstanceMethod", "test")
            );
        } else {
            assertEquals(
                "super static method: test",
                invokeMethod(clazz, "superStaticMethod", "test")
            );
        }
    }

    /**
     * Verify that the method exception is rethrown.
     */
    @Test
    public void invokeMethodExceptionTest() {
        assertThrows(
            AlphaException.class,
            () -> invokeMethod(Beta.class, "superStaticExceptionMethod")
        );

        assertThrows(
            AlphaException.class,
            () -> invokeMethod(object, "superInstanceExceptionMethod")
        );
    }

    /**
     * Verify that the method is invoked and the result is returned.
     */
    @Test
    public void methodTest() {
        assertEquals(
            findMethod(Beta.class, "superStaticMethod", String.class),
            Reflect.on(Beta.class).method("superStaticMethod", String.class)
        );

        assertEquals(
            findMethod(object, "superInstanceMethod", String.class),
            Reflect.on(object).method("superInstanceMethod", String.class)
        );
    }

    /**
     * Verify that the field value is set.
     */
    @Test
    public void setTest() {
        String string = "changed";

        Reflect.on(object).set("superStaticField", string);

        assertSame(string, Beta.getSuperStaticField());

        Reflect.on(object).set("superInstanceField", string);

        assertSame(string, object.getSuperInstanceField());
    }

    /**
     * Verify that the field value is set.
     */
    @Test
    public void setFieldValueTest() {
        String string = "changed";

        setFieldValue(Beta.class, "superStaticField", string);

        assertSame(string, Beta.getSuperStaticField());

        setFieldValue(object, "superInstanceField", string);

        assertSame(string, object.getSuperInstanceField());
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
         * Returns the value of the super instance field.
         *
         * @return The value.
         */
        public String getSuperInstanceField() {
            return superInstanceField;
        }

        /**
         * Returns the value of the super static field.
         *
         * @return The value.
         */
        public static String getSuperStaticField() {
            return superStaticField;
        }

        /**
         * Resets the static instance fields.
         */
        public static void reset() {
            superStaticField = "super static field";
        }

        /**
         * A super class instance exception method.
         */
        private static void superInstanceExceptionMethod() throws AlphaException {
            throw new AlphaException("A method exception.");
        }

        /**
         * A super class instance method.
         *
         * @param string A string.
         */
        private String superInstanceMethod(String string) {
            return "super instance method: " + string;
        }

        /**
         * A super class static exception method.
         */
        private static void superStaticExceptionMethod() throws AlphaException {
            throw new AlphaException("A method exception.");
        }

        /**
         * A super class static method.
         *
         * @param string A string.
         */
        private static String superStaticMethod(String string) {
            return "super static method: " + string;
        }
    }

    /**
     * An exception class used for testing.
     */
    public static class AlphaException extends Exception {

        public AlphaException(String message) {
            super(message);
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
         *
         * @param string A string.
         */
        private String instanceMethod(String string) {
            return "instance method: " + string;
        }

        /**
         * A static method.
         *
         * @param string A string.
         */
        private static String staticMethod(String string) {
            return "static method: " + string;
        }
    }
}
