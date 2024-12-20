[Getting Started](../index_en.md) / [Projekt erstellen](create_project_en.md)

---

# Projekt erstellen

---

## Grundlagen
Als Build-Management-Tool lassen sich Maven oder Gradle nutzen, welche beide im Folgenden berücksichtigt werden.
Grundlage bildet in diesem Guide ein minimales Spring-Boot-Projekt. 

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg"> <b>build.gradle</b></summary>

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
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
    useJUnitPlatform()
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg"> <b>pom.xml</b></summary>

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
    <name>demo</name>
    <description>Demo DLC Application</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
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
welches die wichtigsten DLC-Funktionen bündelt. Kompatibel mit Spring-Boot Version 2 & 3.

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'io.domainlifecycles:spring-boot-3-jooq-complete:2.0.0'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg"> <b>pom.xml</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>io.domainlifecycles</groupId>
        <artifactId>spring-boot-3-jooq-complete</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```
</details>

---

|           **Konfiguration**            |
|:--------------------------------------:|
| [Next >>](configuration_en.md) |

---

**EN** / [DE](../../german/guides/configuration_de.md)