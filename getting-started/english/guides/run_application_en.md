[Getting Started](../index_en.md) / [Run DLC](run_application_en.md)

---

# Run DLC

---

After everything has been configured properly, the DLC mirror has to be initialized when starting the application:

<details>
<summary><img style="height: 12px" src="../../icons/java.svg"> <b>Application.java</b></summary>

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

|         **Configuration**          |           **Features**            |
|:----------------------------------:|:---------------------------------:|
| [<< Previous](configuration_en.md) | [Next >>](features_en.md) |

---

**EN** / [DE](../../german/guides/configuration_de.md)