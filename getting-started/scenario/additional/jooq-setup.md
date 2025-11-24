# JOOQ-Setup

---

To be able to use DLC's persistence module, we first have to set it up correctly with JOOQ.

## Configuration
As soon as jOOQ is provided as dependency, DLC jOOQ Persistence will be enabled automatically.
See [DLC Spring Boot AutoConfig](./../../../dlc-spring-boot-autoconfig/readme.md).

Specifically, you need to use JOOQ's code generator so that the required tables and records are generated.
[More information](https://www.jooq.org/doc/latest/manual/code-generation/codegen-execution/).

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
plugins {
    id 'org.jooq.jooq-codegen-gradle' version '3.20.6'
}

jooq {
    configuration {
        jdbc {
            driver = 'org.h2.Driver'
            url = 'jdbc:h2:~/my-db'
            user = 'sa'
            password = ''
        }
        generator {
            database {
                name = 'org.jooq.meta.h2.H2Database'
                inputSchema = 'PUBLIC'
            }
            generate {
                daos = true
                pojos = true
            }
            target {
                packageName = 'com.example.records'
                directory = 'build/generated-sources/jooq'
            }
        }
    }
}

dependencies {
    jooqCodegen 'com.h2database:h2:2.3.232'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"><b>pom.xml</b></summary>

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jooq</groupId>
            <artifactId>jooq-codegen-maven</artifactId>
            <version>${jooq.version}</version>
            <executions>
                <execution>
                    <id>generate-jooq</id>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <jdbc>
                            <driver>org.h2.Driver</driver>
                            <url>jdbc:h2:~/my-db</url>
                            <user>sa</user>
                            <password></password>
                        </jdbc>
                        <generator>
                            <database>
                                <name>org.jooq.meta.h2.H2Database</name>
                                <inputSchema>PUBLIC</inputSchema>
                            </database>
                            <generate>
                                <daos>true</daos>
                                <pojos>true</pojos>
                            </generate>
                            <target>
                                <packageName>com.example.records</packageName>
                                <directory>${project.build.directory}/generated-sources/jooq</directory>
                            </target>
                        </generator>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
</details>