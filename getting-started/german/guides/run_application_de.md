[Getting Started](../index_de.md) / [DLC starten](run_application_de.md)

---

# DLC starten

---

Nachdem alles Nötige konfiguriert wurde, muss der DLC Mirror beim Starten der Applikation
initialisiert werden:

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>Application.java</b></summary>

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

<b>HINWEIS:</b> Dieser Schritt ist nur notwendig, wenn DLC's Autoconfig-Feature nicht benutzt wird. 
Andernfalls genügt eine ganz simple Spring-Boot-App-Klasse:

```java
@SpringBootApplication
@EnableDlc
public class SampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ShopApplication.class).run(args);
    }
}
```

---

|             **Konfiguration**             |            **Features**            |
|:-----------------------------------------:|:----------------------------------:|
| [<< Vorherige Seite](configuration_de.md) | [Nächste Seite >>](features_de.md) |

---

**DE** / [EN](../../english/guides/run_application_en.md)
