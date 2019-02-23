package io.herrera.kevin.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import lombok.SneakyThrows;

/**
 * Provides a collection of utilities to simplify the use of reflections.
 */
public class Reflect {

    /**
     * Finds any method with the given name in a class.
     *
     * <p>This method will check the current class, and each superclass, for any method that
     * matches the given name. If a method is found, it is made accessible before it is returned.
     * If a method is not found, an exception is thrown.</p>
     *
     * @param clazz The class containing the method.
     * @param name  The name of the method.
     *
     * @return The reflected method.
     */
    @SneakyThrows({ NoSuchMethodException.class })
    public static Method findAnyMethod(Class<?> clazz, String name) {
        Objects.requireNonNull(clazz, "The class is required.");
        Objects.requireNonNull(name, "The method name is required.");

        Method[] methods = Arrays
            .stream(clazz.getDeclaredMethods())
            .filter(method -> method.getName().equals(name))
            .toArray(Method[]::new);

        if (methods.length > 0) {
            return makeAccessible(methods[0]);
        } else if (clazz.getSuperclass() != null) {
            return findAnyMethod(clazz.getSuperclass(), name);
        }

        throw new NoSuchMethodException(String.format("%s.%s()", clazz.getName(), name));
    }

    /**
     * Finds any method with the given name in an object.
     *
     * @param object The object whose class contains the method.
     * @param name   The name of the method.
     *
     * @return The reflected method.
     *
     * @see #findAnyMethod(Class, String)
     */
    public static Method findAnyMethod(Object object, String name) {
        return findAnyMethod(object.getClass(), name);
    }

    /**
     * Finds a field with the given name in a class.
     *
     * <p>This method will check the current class, and each superclass, for a field that matches
     * the given name. If the field is found, it is made accessible before it is returned. If the
     * field is not found, an exception is thrown.</p>
     *
     * @param clazz The class containing the field.
     * @param name  The name of the field.
     *
     * @return The reflected field.
     */
    @SneakyThrows({ NoSuchFieldException.class })
    public static Field findField(Class<?> clazz, String name) {
        Objects.requireNonNull(clazz, "The class is required.");
        Objects.requireNonNull(name, "The field name is required.");

        try {
            return makeAccessible(clazz.getDeclaredField(name));
        } catch (NoSuchFieldException cause) {
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), name);
            }

            throw cause;
        }
    }

    /**
     * Finds a field with the given name in an object.
     *
     * @param object The object whose class contains the field.
     * @param name   The name of the field.
     *
     * @return The reflected field.
     *
     * @see #findField(Class, String)
     */
    public static Field findField(Object object, String name) {
        Objects.requireNonNull(object, "The object is required.");

        return findField(object.getClass(), name);
    }

    /**
     * Finds a method with the given signature in a class.
     *
     * <p>This method will check the current class, and each superclass, for a method that matches
     * the given signature. If the method is found, it is made accessible before it is returned. If
     * the method is not found, an exception is thrown.</p>
     *
     * @param clazz          The class containing the method.
     * @param name           The name of the method.
     * @param parameterTypes The parameter types of the method.
     *
     * @return The reflected method.
     */
    @SneakyThrows({ NoSuchMethodException.class })
    public static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        Objects.requireNonNull(clazz, "The class is required.");
        Objects.requireNonNull(name, "The method name is required.");

        try {
            return makeAccessible(clazz.getDeclaredMethod(name, parameterTypes));
        } catch (NoSuchMethodException cause) {
            if (clazz.getSuperclass() != null) {
                return findMethod(clazz.getSuperclass(), name, parameterTypes);
            }

            throw cause;
        }
    }

    /**
     * Finds a method with the given signature in an object.
     *
     * @param object         The object whose class contains the method.
     * @param name           The name of the method.
     * @param parameterTypes The parameter types of the method.
     *
     * @return The reflected method.
     *
     * @see #findMethod(Class, String, Class[])
     */
    public static Method findMethod(Object object, String name, Class<?>... parameterTypes) {
        Objects.requireNonNull(object, "The object is required.");

        return findMethod(object.getClass(), name, parameterTypes);
    }

    /**
     * Returns the value of a static field.
     *
     * @param <T>    The type of the field.
     * @param clazz  The class containing the field.
     * @param name   The name of the field.
     *
     * @return The value of the field.
     *
     * @see #findField(Class, String)
     */
    @SneakyThrows({ IllegalAccessException.class })
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Class<?> clazz, String name) {
        return (T) findField(clazz, name).get(null);
    }

    /**
     * Returns the value of an instance field.
     *
     * @param <T>    The type of the field.
     * @param object The object whose class contains the field.
     * @param name   The name of the field.
     *
     * @return The value of the field.
     *
     * @see #findField(Object, String)
     */
    @SneakyThrows({ IllegalAccessException.class })
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String name) {
        return (T) findField(object, name).get(object);
    }

    /**
     * Invokes any static method and returns its result.
     *
     * @param <T>       The type of the method result.
     * @param clazz     The class containing the method.
     * @param name      The name of the method.
     * @param arguments The arguments for the method.
     *
     * @return The result of the method.
     *
     * @see #findAnyMethod(Class, String)
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T invokeAnyMethod(Class<?> clazz, String name, Object... arguments) {
        try {
            return (T) findAnyMethod(clazz, name).invoke(null, arguments);
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        }
    }

    /**
     * Invokes any instance method and returns its result.
     *
     * @param <T>       The type of the method result.
     * @param object    The object to use if an instance method is invoked.
     * @param name      The name of the method.
     * @param arguments The arguments for the method.
     *
     * @return The result of the method.
     *
     * @see #findAnyMethod(Object, String)
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T invokeAnyMethod(Object object, String name, Object... arguments) {
        try {
            return (T) findAnyMethod(object, name).invoke(object, arguments);
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        }
    }

    /**
     * Invokes a method and returns its result.
     *
     * @param <T>       The type of the method result.
     * @param clazz     The class containing the method.
     * @param object    The object to use if an instance method is invoked.
     * @param name      The name of the method.
     * @param arguments The arguments for the method.
     *
     * @return The result of the method.
     *
     * @throws IllegalArgumentException If the method could not accept an argument.
     *
     * @see #findMethod(Class, String, Class[])
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static <T> T invokeMethod(
        Class<?> clazz,
        Object object,
        String name,
        Object... arguments
    ) {
        Method method = findMethod(
            clazz,
            name,
            Arrays.stream(arguments).map(Object::getClass).toArray(Class<?>[]::new)
        );

        try {
            return (T) method.invoke(object, arguments);
        } catch (InvocationTargetException cause) {
            throw cause.getCause();
        }
    }

    /**
     * Invokes a static method and returns its result.
     *
     * <p>A method with the same name and parameters types for the given arguments will be found,
     * invoked, and its results are returned. If a matching method could not be found or invoked,
     * an exception is thrown. If the method throws its own exception, the reflection exception
     * wrapper, <code>InvocationTargetException</code>, is unwrapped and the inner exception is
     * thrown.</p>
     *
     * @param <T>       The type of the method result.
     * @param clazz     The class containing the method.
     * @param name      The name of the method.
     * @param arguments The arguments for the method.
     *
     * @return The result of the method.
     *
     * @throws IllegalArgumentException If the method could not accept an argument.
     */
    public static <T> T invokeMethod(Class<?> clazz, String name, Object... arguments) {
        return invokeMethod(clazz, null, name, arguments);
    }

    /**
     * Invokes an instance method and returns its result.
     *
     * @param <T>       The type of the method result.
     * @param object    The object whose class contains the method.
     * @param name      The name of the method.
     * @param arguments The arguments for the method.
     *
     * @return The result of the method.
     *
     * @throws IllegalArgumentException If the method could not accept an argument.
     *
     * @see #invokeMethod(Class, String, Object...)
     */
    public static <T> T invokeMethod(Object object, String name, Object... arguments) {
        return invokeMethod(object.getClass(), object, name, arguments);
    }

    /**
     * Makes a reflected object accessible.
     *
     * @param <T>    The type of the object.
     * @param object The accessible object.
     *
     * @return The same accessible object.
     */
    private static <T extends AccessibleObject> T makeAccessible(T object) {
        object.setAccessible(true);

        return object;
    }

    /**
     * Sets the value of a static field.
     *
     * @param <T>    The type of the field.
     * @param clazz  The class containing the field.
     * @param name   The name of the field.
     * @param value  The new value for the field.
     *
     * @throws IllegalArgumentException If the field could not accept the given value.
     *
     * @see #findField(Class, String)
     */
    @SneakyThrows({ IllegalAccessException.class })
    public static <T> void setFieldValue(Class<?> clazz, String name, T value) {
        findField(clazz, name).set(null, value);
    }

    /**
     * Sets the value of an instance field.
     *
     * @param <T>    The type of the field.
     * @param object The object whose class contains the field.
     * @param name   The name of the field.
     * @param value  The new value for the field.
     *
     * @throws IllegalArgumentException If the field could not accept the given value.
     *
     * @see #findField(Object, String)
     */
    @SneakyThrows({ IllegalAccessException.class })
    public static <T> void setFieldValue(Object object, String name, T value) {
        findField(object, name).set(object, value);
    }

    private Reflect() {
        // Should not be initialized.
    }
}
