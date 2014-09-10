# Workout Tracker

This is a demonstration of how functional programming techniques can improve Vaadin UI code. This is realized by implementing an application UI three times:

* [java7 branch](https://github.com/hezamu/WorkoutTracker/tree/java7) contains the starting point that utilizes Vaadin 7 and Java 7.
* [java8 branch](https://github.com/hezamu/WorkoutTracker/tree/java8) contains the an improved versio utilizing various improvements available in Java 8.
* [scala branch](https://github.com/hezamu/WorkoutTracker/tree/scala) is built in Scala using the [Scaladin](https://github.com/henrikerola/scaladin) library, which wraps the Vaadin API in Scala. DAO and logic layers remain Java 8.
* [rx branch](https://github.com/hezamu/WorkoutTracker/tree/rx) implements the business logic in a reactive way using [RxJava](https://github.com/ReactiveX/RxJava) Observables with [RxVaadin](https://github.com/hezamu/RxVaadin). Othewise it is identical with the Java 8 version.

This demo concentrates on the UI code. There is a backend, but it is just a dummy implementation.