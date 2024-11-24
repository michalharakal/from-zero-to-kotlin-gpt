# from-zero-to-kotlin-gpt

This repository contains code and slides to my talk at JAX London 2024

* [From Zero to Kotlin GPT in 45 Minutes](https://jaxlondon.com/data-machine-learning/building-kotlin-gpt-model)

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
