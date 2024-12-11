# DLC starten

<hr/>

Nachdem alles Nötige konfiguriert wurde, muss der DLC Mirror beim Starten der Applikation
initialisiert werden:

<details>
<summary><i>Application.java</i></summary>

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