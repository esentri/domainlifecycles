[Getting Started](index.md) / [DLC starten](run_application.md)

<hr/>

# DLC starten

<hr/>

Nachdem alles Nötige konfiguriert wurde, muss der DLC Mirror beim Starten der Applikation
initialisiert werden:

<details>
<summary><img style="height: 12px" src="icons/java.svg"> <b>Application.java</b></summary>

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

<hr/>

|            **Konfiguration**             |           **Features**            |
|:----------------------------------------:|:---------------------------------:|
| [<< Vorherige Seite](./configuration.md) | [Nächste Seite >>](./features.md) |