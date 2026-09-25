[Getting Started](../index_en.md) / [Configuration](configuration_en.md)

---

# Configuration

DLC offers many options for individualization and configuration. You can, of course, configure every bean
yourself, however DLC provides an autoconfiguration feature which allows you to simple en-/disable all of DLC's features
with a default configuration.

---

## Auto-Configuration
Below you see a minimal working example of using DLC's autoconfiguration feature by annotating your Spring-Boot app class
with `@EnableDlc`.
<br/>
**Note:** The `dlcMirrorBasePackages` are mandatory. If you have got multiple packages to be scanned, provide a comma-separated string.

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>Application.java</b></summary>

```java
@SpringBootApplication
@EnableDlc(dlcMirrorBasePackages = "com.example.domain")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
</details>

Every DLC feature can also be individually enabled/disabled and configured via Spring application properties
instead of (or on top of) `@EnableDlc`'s annotation attributes, e.g. `dlc.features.mirror.base-packages=...`,
`dlc.features.persistence.jooq.enabled=false` or `dlc.features.persistence.sql-dialect=POSTGRES`. jOOQ- vs
plain-JDBC-based persistence autoconfiguration is selected automatically based on what's on the classpath (jOOQ
wins if both are present). See the [DLC Spring Boot AutoConfig readme](../../../dlc-spring-boot-autoconfig/readme.md#available-autoconfig-modules)
for the full list of autoconfig modules and their properties.

---

|         **Build-Management**          |            **Run DLC**            |
|:-------------------------------------:|:---------------------------------:|
| [<< Previous](build_management_en.md) | [Next >>](run_application_en.md)  |

---

**EN** / [DE](../../german/guides/configuration_de.md)
