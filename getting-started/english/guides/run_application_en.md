[Getting Started](../index_en.md) / [Run DLC](run_application_en.md)

---

# Run DLC

---

As long as you use the autoconfiguration feature, you can start the app like your usual Spring-Boot application.
<br/>
But if you don't, keep in mind to initialize the Domain-Mirror with the required domainBasePackages yourself. 

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>Application.java</b></summary>

```java
@SpringBootApplication
public class SampleApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("com.example.domain"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SampleApplication.class).run(args);
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
