[![Build Status](https://travis-ci.org/kherge/java.reflect.svg?branch=master)](https://travis-ci.org/kherge/java.reflect)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=kherge_java.reflect&metric=alert_status)](https://sonarcloud.io/dashboard?id=kherge_java.reflect)

Reflect
=======

A library to simplify reflection operations.

Working with reflections naturally comes with a hefty dose of warnings and red flags. Once we know
what to watch out for, using reflections in Java can be a bit of a chore. This library provides a
set of utilities to help simplify field and method reflections.

```java
MyClass object = new MyClass("hello");
Reflect fluent = Reflect.on(object);

String greeting = fluent.get("myInternalGreeting");

fluent.invoke("sayGreeting", greeting);
```

Requirements
------------

- Java 8

Installation
------------

### Gradle

```groovy
compile 'io.herrera.kevin:reflect:?'
```

### Maven

```xml
<dependency>
  <groupId>io.herrera.kevin</groupId>
  <artifactId>reflect</artifactId>
  <version>?</version>
</dependency>
```

Usage
-----

There are two ways to use Reflect.

### Fluent

```java
import io.herrera.io.reflect.Reflect;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class FluentExample {
    public static void main() {

        // You can create a fluent interface using classes.
        Reflect fluent = Reflect.on(MyClass.class);

        // Or, you can create a fluent interface using instances.
        Reflect fluent = Reflect.on(myInstance);

        /* If you wrap a class, you can only access static members. If you wrap an instance, you
         * can access both instance and static members. Which is used depends on whether access
         * to instance members are needed or not.
         */

        // Find a method for a given name and get its accessible reflection. If it is overloaded,
        // only get the first one and ignore the rest.
        Method method = fluent.anyMethod("myMethod");

        // Find a field and get its accessible reflection.
        Field field = fluent.field("myField");

        // Get the value of a field.
        //
        // The library will automatically case the value to match. Note, however, that if the wrong
        // type is used, ClassCastException exception will be thrown. The type of the value should
        // be known at all times.
        Object myValue = fluent.get("myField");
        String myValue = fluent.get("myField");
        MyClass myValue = fluent.get("myField"); // etc.

        // Invoke a method with a signature that matches the arguments.
        //
        // Like the .get() method, the result will be automatically cast to the desired type. Also
        // note that if InvocationTargetException is thrown during invocation, the inner exception
        // is thrown instead.
        String myResult = fluent.invoke("myMethod", "arg A", "arg B", "arg C"); // etc.

        // Like a combination of anyMethod() and invoke(), find a method for a given name and
        // invoke it. If its overloaded, use the first one and ignore the rest.
        String myResult = fluent.invokeAny("myMethod", "arg A", "arg B", "arg C"); // etc.

        // Find a method with a matching signature.
        Method method = fluent.method("myMethod", ParameterType.class); // etc.

        // Set the value of a field.
        fluent.set("myField", "my value");
    }
}
```

### Static Methods

```java
import static io.herrera.io.reflect.Reflect.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class StaticMethodExample {
    public static void main() {

        // Find a field in a class and get its accessible reflection.
        Field field = findField(MyClass.class, "myFieldName");

        // Find a field in an instance and get its accessible reflection.
        Field field = findField(myObject, "myFieldName");

        // Get the value of a static field.
        //
        // The library will automatically case the value to match. Note, however, that if the wrong
        // type is used, ClassCastException exception will be thrown. The type of the value should
        // be known at all times.
        String myString = getFieldValue(MyClass.class, "myFieldName");

        // Get the value of an instance field.
        //
        // Again, the value is automatically cast to the desired type.
        String myString = getFieldValue(myObject, "myFieldName");

        // Set the value of a static field.
        setFieldValue(MyClass.class, "myStaticField", "my value");

        // Set the value of an instance field.
        setFieldValue(myObject, "myInstanceField", "my value");

        // Find a method in a class with a matching signature.
        Method method = findMethod(MyClass.class, "myMethodName", ParameterType.class); // etc.

        // Find a method in an instance with a matching signature.
        Method method = findMethod(myObject, "myMethodName", ParameterType.class); // etc.

        // Find a method for a given name in a class and get its accessible reflection. If it is
        // overloaded, only get the first one and ignore the rest.
        Method method = findAnyMethod(MyClass.class, "myMethodName");

        // Find a method for a given name in an instance and get its accessible reflection. If it
        // is overloaded, only get the first one and ignore the rest.
        Method method = findAnyMethod(myObject, "myMethodName");

        // Invoke a static method with a signature that matches the arguments.
        //
        // Like the getFieldValue() method, the result will be automatically cast to the desired
        // type. Also note that if InvocationTargetException is thrown during invocation, the inner
        // exception is thrown instead. If you try to invoke an instance method using a static
        // context, NullPointerException exception is thrown.
        String myString = invokeMethod(MyClass.class, "myMethodName", "arg A", "arg B"); // etc.

        // Invoke a instance method with a signature that matches the arguments.
        //
        // Same caveats as invoke a static method.
        String myString = invokeMethod(myObject, "myMethodName", "arg A", "arg B"); // etc.

        // Like a combination of findAnyMethod() and invokeMethod(), find a static method for a
        // given name and invoke it. If its overloaded, use the first one and ignore the rest.
        String myString = invokeAnyMethod(MyClass.class, "myMethodName", "arg A", "arg B"); // etc.

        // Like a combination of findAnyMethod() and invokeMethod(), find a instance method for a
        // given name and invoke it. If its overloaded, use the first one and ignore the rest.
        String myString = invokeAnyMethod(myObject, "myMethodName", "arg A", "arg B"); // etc.
    }
}
```

Notes
-----

- Generics and variadics can make using reflections very hard. If a method uses generic types or
  variadic parameter list, it is recommended that the method is found using its exact signature
  (e.g. `.method()`, `findMethod()`) instead of relying on the library to infer the signature (e.g.
  `.invoke()`, `invokeMethod()`). If the method is not overloaded, the methods `.invokeAny()` or
  `invokeAnyMethod()` could be used.

License
-------

This library is made available under the MIT and Apache 2.0 licenses.
