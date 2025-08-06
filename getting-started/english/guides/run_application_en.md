[Getting Started](../index_en.md) / [Run DLC](run_application_en.md)

---

# Run DLC

---

Once everything necessary has been configured properly, the DLC Mirror must be initialized when the application is 
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

<b>NOTE:</b> This step in only necessary when you don't use DLC's Autoconfiguration feature.
Otherwise, your application class simply looks like this:

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

|         **Configuration**          |       **Features**        |
|:----------------------------------:|:-------------------------:|
| [<< Previous](configuration_en.md) | [Next >>](features_en.md) |

---

**EN** / [DE](../../german/guides/configuration_de.md)
