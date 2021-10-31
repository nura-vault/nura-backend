# nura-backend

<div align="center">
  <a href="https://www.oracle.com/java/" target="_blank">
    <img
      src="https://img.shields.io/badge/Written%20in-java-%23EF4041?style=for-the-badge"
      height="30"
    />
  </a>
  <a href="https://spring.io/" target="_blank">
    <img
      src="https://img.shields.io/badge/spring-boot-%27a147?style=for-the-badge"
      height="30"
    />
  </a>
</div>

## 📚 Introduction

nura-backend is a RESTful API for managing [nura's](https://github.com/nura-vault/nura-pwa) data. It is written in Java and uses Spring Boot. Internally, is uses [mongo-db](https://www.mongodb.com/) to store and read data.

<div align="center">
    <img
      src="https://i.imgur.com/76nyAjR.png"
    />
</div>

## ☁ Magic

All the endpoints use a `Authentication` header to authenticate each request and present user specific results. Depending on the request, the Authentication header varies:

### Signin

```js
Authentication: <base64(username:password)>
```

After sucessfully signing in, the response will contain a token. This is later used to authenticate all other requests.

### Vault / Archive

```js
Authentication: <base64(username:token)>
```

The REST endpoinst look as follows:

<div align="center">
    <img
      src="https://i.imgur.com/HnbRWVu.png"
    />
</div>

**(outdated)**

## 🧱 Build it yourself

In case you want to host the database yourself, you can easily build it with docker:

### 🐳 Dockerized

Just run the following command:

```shell
$ git clone https://github.com/nura-vault/nura-backend.git .
$ docker-compose up
```

This will expose the container to port `:8080`

## 📦 Standalone

In case you don't want to use docker, things get a bit more complicated. And by a bit, I mean a lot more complicated. First of all you need to make sure you have a running [mongo-db](https://www.mongodb.com/) database. Next you have to clone the project:

```shell
$ git clone https://github.com/nura-vault/nura-backend.git .
```

Navigate to `src/main/resources/application.properties` and edit the properties as follows:

```properties
...
spring.data.mongodb.host      = localhost
spring.data.mongodb.port      = 27017
spring.data.mongodb.database  = nura
...
```

Make sure you have [maven](https://maven.apache.org/) and  [Java-11](http://jdk.java.net/java-se-ri/11) installed. Then run the following command at the root of the project:

```shell
$ mvn install -B -ntp -DskipTests=true -f pom.xml
```

Now you can start the application:

```shell
$ java -jar target/nura-1.0.jar
```

This will expose the application to port `:8080`