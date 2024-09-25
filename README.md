# Quarkus Redis Project - Version 3.14.3

This project demonstrates the usage of Quarkus 3.14.3 with Redis, implementing two different repositories: RedisLowLevelAPIRepository and RedisHighLevelAPIRepository. Each repository provides an alternative way to connect and interact with Redis.<br>
Features:

    Quarkus 3.14.3
    Redis operations
    Interface-driven architecture
    Two Redis repository implementations:
        RedisLowLevelAPIRepository (Low-level API connection)
        RedisHighLevelAPIRepository (High-level API connection)

# Project Structure

src<br>
└── main<br>
├── java<br>
│   └── br/com/redis/service<br>
│       ├── RedisService.java<br>
│   └── br/com/redis/repository<br>
│       ├── RedisLowLevelAPIRepository.java <br>
│       └── RedisHighLevelAPIRepository.java
[
]()
# RedisService Interface
[README.md](README.md)
The RedisService interface defines methods that are implemented by both RedisLowLevelAPIRepository and RedisHighLevelAPIRepository.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus_redis-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Redis Client ([guide](https://quarkus.io/guides/redis)): Connect to Redis in either imperative or reactive style
