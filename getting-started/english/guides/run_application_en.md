[Getting Started](../index_en.md) / [Run DLC](run_application_en.md)

---

# Run DLC

---

Once everything necessary has been configured properly , the DLC Mirror must be initialized when the application is 
started:

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

---

|         **Configuration**          |       **Features**        |
|:----------------------------------:|:-------------------------:|
| [<< Previous](configuration_en.md) | [Next >>](features_en.md) |

---

**EN** / [DE](../../german/guides/configuration_de.md)
