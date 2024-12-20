[Getting Started](../index_de.md) / [DLC starten](run_application_de.md)

---

# DLC starten

---

Nachdem alles Nötige konfiguriert wurde, muss der DLC Mirror beim Starten der Applikation
initialisiert werden:

<details>
<summary><img style="height: 12px" src="../icons/java.svg"> <b>Application.java</b></summary>

```java
@SpringBootApplication
public class SampleApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleapp"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ShopApplication.class).run(args);
    }
}
```
</details>

---

|             **Konfiguration**             |            **Features**            |
|:-----------------------------------------:|:----------------------------------:|
| [<< Vorherige Seite](configuration_de.md) | [Nächste Seite >>](features_de.md) |

---

**DE** / [EN](../../english/guides/run_application_en.md)