# Chapter 1 – Creating the Project

---

Covered topics: Project setup

---

Before Emma’s webshop can sell its first product, we need a project to host the code.

We’ll use Spring Boot as our foundation and then add DLC on top.

## Step 1: Start a Minimal Spring Boot Project

You can use either Gradle or Maven.
Here’s a minimal setup using Gradle:

<details>
<summary><img style="height: 12px" src="../icons/gradle.svg" alt="gradle"><b>build.gradle</b></summary>

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.0'
    id 'io.spring.dependency-management' version '1.1.6'
}

group = 'com.shop'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
}
```
</details>

Or with Maven:

<details> <summary><img style="height: 12px" src="../icons/file-type-maven.svg" alt="maven"><b>pom.xml</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```
</details>


## Step 2: Add DLC

Next, we’ll add DLC to bring DDD principles to life.

<details> 
<summary><img style="height: 12px" src="../icons/gradle.svg" alt="gradle"><b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'io.domainlifecycles:spring-boot-starter:2.5.0'
}
```
</details> 

<details> <summary><img style="height: 12px" src="../icons/file-type-maven.svg" alt="maven"><b>pom.xml</b></summary>

```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>2.5.0</version>
</dependency>
```
</details>

DLC provides everything we need for:
- Domain modeling (aggregates, entities, value objects)
- Persistence
- Event handling
- Autoconfiguration for Spring Boot

## Step 3: First Run

Let’s make sure everything works by creating a simple application entry point.

```java
@SpringBootApplication
@EnableDlc(dlcDomainBasePackages = "com.shop.domain")
public class ShopApplication {
    static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}
```

When you run this class, Spring Boot should start successfully — DLC is now enabled!

✅ Checkpoint
You now have:
- A running Spring Boot application,
- DLC integrated and ready to scan your domain packages.

Next, we’ll quickly cover how to configure your DLC application before we dive right into development!

---

|      **Introduction**      | **Chapter 2 - Configuration** |
|:--------------------------:|:-----------------------------:|
| [<< Previous](c0_intro.md) |    [Next >>](c2_config.md)    |