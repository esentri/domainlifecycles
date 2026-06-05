[Getting Started](../index_en.md) / [Configuration](configuration_en.md)

---

# Configuration

DLC offers many options for individualization and configuration. You can, of course, configure every bean
yourself, however DLC provides an autoconfiguration feature which allows you to simple en-/disable all of DLC's features
with a default configuration.

---

## Auto-Configuration
Below you see a minimal working example of using DLC's autoconfiguration feature by annotating your Spring-Boot app class
with `@EnableDLC`.
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

---

|         **Build-Management**          |            **Run DLC**            |
|:-------------------------------------:|:---------------------------------:|
| [<< Previous](build_management_en.md) | [Next >>](run_application_en.md)  |

---

**EN** / [DE](../../german/guides/configuration_de.md)
