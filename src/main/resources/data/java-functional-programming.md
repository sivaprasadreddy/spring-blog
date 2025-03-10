# Functional Programming in Java: Lambda Expressions and Streams

## Introduction
With the release of Java 8, functional programming features were introduced to the language, revolutionizing how developers write Java code. This article explores the key functional programming concepts in Java, focusing on lambda expressions, functional interfaces, and the Stream API.

## Lambda Expressions

### What Are Lambda Expressions?
Lambda expressions are anonymous functions that provide a clear and concise way to implement single-method interfaces (functional interfaces). They enable you to treat functionality as a method argument, or code as data.

### Basic Syntax
```
// Basic lambda syntax
(parameters) -> expression

// With multiple statements
(parameters) -> {
    statement1;
    statement2;
    return result;
}
```

### Examples
```
// Traditional anonymous class
Runnable runnable1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello from anonymous class");
    }
};

// Equivalent lambda expression
Runnable runnable2 = () -> System.out.println("Hello from lambda");

// Lambda with parameters
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();

// Lambda with explicit type declaration
BiFunction<Integer, Integer, Integer> add = (Integer a, Integer b) -> a + b;
```

## Functional Interfaces

### What Are Functional Interfaces?
A functional interface is an interface with exactly one abstract method. Java 8 introduced the `@FunctionalInterface` annotation to mark interfaces as functional interfaces.

### Built-in Functional Interfaces
Java provides several built-in functional interfaces in the `java.util.function` package:

1. **Function<T, R>**: Takes an input of type T and returns a result of type R
   ```
   Function<String, Integer> length = s -> s.length();
   Integer len = length.apply("Hello"); // Returns 5
   ```

2. **Predicate<T>**: Takes an input of type T and returns a boolean
   ```
   Predicate<String> isEmpty = s -> s.isEmpty();
   boolean result = isEmpty.test(""); // Returns true
   ```

3. **Consumer<T>**: Takes an input of type T and returns no result
   ```
   Consumer<String> print = s -> System.out.println(s);
   print.accept("Hello World"); // Prints "Hello World"
   ```

4. **Supplier<T>**: Takes no input and returns a result of type T
   ```
   Supplier<Double> random = () -> Math.random();
   Double value = random.get(); // Returns a random double
   ```

5. **BiFunction<T, U, R>**: Takes inputs of types T and U and returns a result of type R
   ```
   BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
   Integer sum = add.apply(2, 3); // Returns 5
   ```

## Method References

Method references provide a shorthand notation for lambda expressions that call a single method:

```
// Static method reference
Function<String, Integer> parseInt = Integer::parseInt;

// Instance method reference of a particular object
Consumer<String> printer = System.out::println;

// Instance method reference of an arbitrary object of a particular type
Function<String, Integer> length = String::length;

// Constructor reference
Supplier<List<String>> listSupplier = ArrayList::new;
```

## Stream API

### What Is the Stream API?
The Stream API provides a functional approach to processing collections of objects. A stream is a sequence of elements that supports sequential and parallel aggregate operations.

### Creating Streams
```
// From a collection
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> streamFromList = list.stream();

// From an array
String[] array = {"a", "b", "c"};
Stream<String> streamFromArray = Arrays.stream(array);

// Using Stream.of
Stream<String> streamOfElements = Stream.of("a", "b", "c");

// Infinite streams
Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 1);
```

### Common Stream Operations

1. **Filter**: Returns a stream consisting of elements that match the given predicate
   ```
   List<String> filtered = list.stream()
       .filter(s -> s.startsWith("a"))
       .collect(Collectors.toList());
   ```

2. **Map**: Returns a stream consisting of the results of applying the given function
   ```
   List<Integer> lengths = list.stream()
       .map(String::length)
       .collect(Collectors.toList());
   ```

3. **Sorted**: Returns a stream consisting of the elements sorted according to natural order
   ```
   List<String> sorted = list.stream()
       .sorted()
       .collect(Collectors.toList());
   ```

4. **Reduce**: Performs a reduction on the elements of the stream
   ```
   Optional<String> reduced = list.stream()
       .reduce((s1, s2) -> s1 + s2);
   ```

5. **Collect**: Performs a mutable reduction operation on the elements of the stream
   ```
   String joined = list.stream()
       .collect(Collectors.joining(", "));
   ```

### Parallel Streams
Streams can be processed in parallel to leverage multi-core architectures:
```
List<String> result = list.parallelStream()
    .filter(s -> s.length() > 2)
    .collect(Collectors.toList());
```

## Optional

The `Optional` class is a container object that may or may not contain a non-null value. It helps avoid null pointer exceptions:

```
// Creating Optional objects
Optional<String> optional1 = Optional.of("value");
Optional<String> optional2 = Optional.ofNullable(nullableValue);
Optional<String> optional3 = Optional.empty();

// Using Optional
optional1.ifPresent(System.out::println);
String result = optional1.orElse("default");
String result2 = optional1.orElseGet(() -> "computed default");
```

## Conclusion

Functional programming in Java has transformed how developers write code, making it more concise, readable, and maintainable. Lambda expressions, functional interfaces, and the Stream API are powerful tools that every Java developer should master to write modern, efficient Java applications.