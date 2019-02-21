package io.herrera.kevin.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.SneakyThrows;

/**
 * Provides a collection of utilities to simplify the use of reflections.
 */
public class Reflect {

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
     *
     * @throws NoSuchFieldException If the field could not be found.
     */
    @SneakyThrows({ NoSuchFieldException.class })
    public static Field findField(Class<?> clazz, String name) {
        Objects.requireNonNull(clazz, "The class is required.");
        Objects.requireNonNull(name, "The field name is required.");

        NoSuchFieldException exception = null;

        do {
            try {
                return makeAccessible(clazz.getDeclaredField(name));
            } catch (NoSuchFieldException cause) {
                if (exception == null) {
                    exception = cause;
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);

        throw exception;
    }

    /**
     * Finds a field with the given name in an object.
     *
     * <p>This method will check the current class of an object, and each superclass, for a field
     * that matches the given name. If the field is found, it is made accessible before it is
     * returned. If the field is not found, an exception is thrown.</p>
     *
     * @param object The object whose class contains the field.
     * @param name   The name of the field.
     *
     * @return The reflected field.
     *
     * @throws NoSuchFieldException If the field could not be found.
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
     *
     * @throws NoSuchMethodException If the method could not be found.
     */
    @SneakyThrows({ NoSuchMethodException.class })
    public static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        Objects.requireNonNull(clazz, "The class is required.");
        Objects.requireNonNull(name, "The method name is required.");

        NoSuchMethodException exception = null;

        do {
            try {
                return makeAccessible(clazz.getDeclaredMethod(name, parameterTypes));
            } catch (NoSuchMethodException cause) {
                if (exception == null) {
                    exception = cause;
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);

        throw exception;
    }

    /**
     * Finds a method with the given signature in an object.
     *
     * <p>This method will check the current class of an object, and each superclass, for a method
     * that matches the given signature. If the method is found, it is made accessible before it is
     * returned. If the method is not found, an exception is thrown.</p>
     *
     * @param object         The object whose class contains the method.
     * @param name           The name of the method.
     * @param parameterTypes The parameter types of the method.
     *
     * @return The reflected method.
     *
     * @throws NoSuchMethodException If the method could not be found.
     *
     * @see #findMethod(Class, String, Class[])
     */
    public static Method findMethod(Object object, String name, Class<?>... parameterTypes) {
        Objects.requireNonNull(object, "The object is required.");

        return findMethod(object.getClass(), name, parameterTypes);
    }

    /**
     * Returns the value of an instance field.
     *
     * @param object The object whose class contains the field.
     * @param name   The name of the field.
     *
     * @return The value of the field.
     *
     * @throws IllegalAccessException If the field could not be accessed.
     * @throws NoSuchFieldException   If the field could not be found.
     */
    @SneakyThrows({ IllegalAccessException.class })
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String name) {
        return (T) findField(object, name).get(object);
    }

    /**
     * Returns the value of a static field.
     *
     * @param clazz  The class containing the field.
     * @param name   The name of the field.
     *
     * @return The value of the field.
     *
     * @throws IllegalAccessException If the field could not be accessed.
     * @throws NoSuchFieldException   If the field could not be found.
     */
    @SneakyThrows({ IllegalAccessException.class })
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Class<?> clazz, String name) {
        return (T) findField(clazz, name).get(null);
    }

    /**
     * Makes a reflected object accessible.
     *
     * @param object The accessible object.
     *
     * @return The same accessible object.
     */
    private static <T extends AccessibleObject> T makeAccessible(T object) {
        object.setAccessible(true);

        return object;
    }
}
