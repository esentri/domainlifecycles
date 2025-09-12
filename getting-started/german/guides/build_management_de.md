[Getting Started](../index_de.md) / [Build Management](build_management_de)

---

# Build-Management

---

## Grundlagen für das Build-Management
Als Build-Management-Tool lassen sich Maven oder Gradle nutzen, welche beide im Folgenden berücksichtigt werden.
Den Ausgangspunkt bildet in diesem Guide ein Build-Setup für ein minimales Spring-Boot-Projekt. 

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.4.0'
    id 'io.spring.dependency-management' version '1.1.6'
}

group = 'com.example'
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

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> <b>pom.xml</b></summary>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

```
</details>

---

## DLC einbinden
Um die DLC-Funktionalität zu nutzen, reicht es bereits folgendes Artefakt einzubinden, 
welches die wichtigsten DLC-Funktionen bündelt:

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'io.domainlifecycles:spring-boot-starter:2.5.0'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"><b>pom.xml</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>io.domainlifecycles</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <version>2.5.0</version>
    </dependency>
</dependencies>
```
</details>

---

|             **Konfiguration**             |
|:-----------------------------------------:|
| [Nächste Seite >>](./configuration_de.md) |

---

**DE** / [EN](../../english/guides/build_management_en.md)
