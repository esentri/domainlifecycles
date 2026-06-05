[Getting Started](../index_de.md) / [DLC starten](run_application_de.md)

---

# DLC starten

---

Solange das Autoconfiguration-Feature genutzt wird, wird eine DLC Applikation gestartet wie eine übliche
Spring Boot App.

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>Application.java</b></summary>

```java
@SpringBootApplication
@EnableDlc(dlcMirrorBasePackages = "com.example.domain")
public class SampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SampleApplication.class).run(args);
    }
}
```
</details>

---

|              **Konfiguration**               |            **Features**            |
|:--------------------------------------------:|:----------------------------------:|
|  [<< Vorherige Seite](configuration_de.md)   | [Nächste Seite >>](features_de.md) |

---

**DE** / [EN](../../english/guides/run_application_en.md)
