[Getting Started](../index_de.md) / [Konfiguration](configuration_de.md)

---

# Konfiguration

DLC bietet viele Möglichkeiten für Anpassungen und spezifische Konfiguration. Selbstverständlich können
alle benötigten Beans händisch angelegt und konfiguriert werden, jedoch bietet DLC die Möglichkeit 
eine Spring-Boot Autoconfiguration zu nutzen, welche im Folgenden erläutert wird.

---

## Auto-Configuration
Im Folgenden findet sich ein Beispiel, wie sich DLC's Autoconfiguration Feature nutzen lässt.
Aktiviert wird diese, indem an die Spring-Boot App Klasse die Annotation `@EnableDLC` hinzugefügt wird.
<br/>
**Hinweis:** Der Parameter `dlcDomainBasePackages` ist verpflichtend. 
Sollten hier mehrere Packages gescannt werden müssen, können diese als komma-separierter String übergeben werden.

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>Application.java</b></summary>

```java
@SpringBootApplication
@EnableDlc(dlcDomainBasePackages = "com.example.domain")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
</details>

---

|             **Build-Management**             |              **DLC starten**              |
|:--------------------------------------------:|:-----------------------------------------:|
| [<< Vorherige Seite](build_management_de.md) | [Nächste Seite >>](run_application_de.md) |

---

**DE** / [EN](../../english/guides/configuration_en.md)
