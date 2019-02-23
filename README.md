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

String greeting = getFieldValue(object, "myInternalGreeting");

invokeMethod(object, "sayGreeting", greeting);
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

> It is important to note that `findField` and `findMethod` will find any declared member, not just
> public ones. It is also important to note that `invokeMethod` may not work with methods that make
> use of generics and variadics. It is recommended that you use `invokeAnyMethod` instead of
> `invokeMethod` when a method accepts generics or variadics. If the method is overloaded, you may
> want to use `findMethod` instead.

```java
import static io.herrera.io.reflect.Reflect.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Example {
    public static void main() {

        // Retrieve a reflected field by class.
        Field field = findField(MyClass.class, "myFieldName");

        // Retrieve a reflected field by object.
        Field field = findField(myObject, "myFieldName");

        // Retrieve the value of a static field.
        String myString = getFieldValue(MyClass.class, "myFieldName");

        // Retrieve the value of an instance field.
        String myString = getFieldValue(myObject, "myFieldName");

        // Set the value of a static field.
        setFieldValue(MyClass.class, "myStaticField", "my value");

        // Set the value of an instance field.
        setFieldValue(myObject, "myInstanceField", "my value");

        // Retrieve a reflected method by class.
        Method method = findMethod(MyClass.class, "myMethodName");
        Method method = findMethod(MyClass.class, "myMethodName", TypeA.class, TypeB.class); // etc.

        // Retrieve a reflected method by object.
        Method method = findMethod(myObject, "myMethodName");
        Method method = findMethod(myObject, "myMethodName", TypeA.class, TypeB.class); // etc.

        // Retrieve any reflected method by class for a given name.
        Method method = findAnyMethod(MyClass.class, "myMethodName");

        // Retrieve any reflected method by object for a given name.
        Method method = findAnyMethod(myObject, "myMethodName");

        // Invoke a static method and retrieve its result.
        String myString = invokeMethod(MyClass.class, "myMethodName", "arg A", "arg B"); // etc.

        // Invoke an instance method and retrieve its result.
        String myString = invokeMethod(myObject, "myMethodName", "arg A", "arg B"); // etc.

        // Invoke any static method with a given name and retrieve its result.
        String myString = invokeAnyMethod(MyClass.class, "myMethodName", "arg A", "arg B"); // etc.

        // Invoke any instance method with a given name and retrieve its result.
        String myString = invokeAnyMethod(myObject, "myMethodName", "arg A", "arg B"); // etc.
    }
}
```

License
-------

This library is made available under the MIT and Apache 2.0 licenses.
