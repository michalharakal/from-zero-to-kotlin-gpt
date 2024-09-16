# from-zero-to-kotlin-gpt

This repository contains code and slides to my talks about implementing a simple generative pre-tained transformers model

https://rheinwerk-kkon.de/programm/harakal-mit-kotlin-ein-eigenes-gpt-modell-bauen/

## Building and running

### publish KPTChat into local maven

```bash
cd KPTChat
./gradlew clean publishMavenLocal
```

### JVM Desktop App

```bash
cd SinusApproximator
./gradlew clean composeApp:run
```

