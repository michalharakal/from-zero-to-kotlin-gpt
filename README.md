# from-zero-to-kotlin-gpt

This repository contains code and slides to my talk at Kotlin User Group Munich, 11/2024

* [Kotlin Meetup November Edition](https://www.meetup.com/de-DE/kotlin-user-group-munich/events/304290138/)

## Building and running

### publish KPTChat into local maven

```bash
cd KPTChat
./gradlew clean publishToMavenLocal
```

### JVM Desktop App

```bash
cd SinusApproximator
./gradlew clean composeApp:run
```
