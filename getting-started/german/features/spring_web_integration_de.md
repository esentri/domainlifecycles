[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Spring-Web-Integration](spring_web_integration_de.md)

---

# Spring-Web-Integration

Nahtlose Integration zwischen Spring-Web und DLC, um Identities und "single-valued" ValueObjects als
Pfad-/Query-Parameters zu nutzen.

---

## Konfiguration
```Java
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    /**
     * Create a new simple web configuration for Spring working with DLC.
     */
    public WebConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Spring integration to enable mapping of single valued ValueObjects or Ids,
     * which are represented as basic properties in RestController endpoints.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToDomainIdentityConverterFactory(objectMapper));
        registry.addConverterFactory(new StringToDomainValueObjectConverterFactory(objectMapper));
    }

    /**
     * Optional DLC response format.
     */
    @Bean
    public ResponseEntityBuilder defaultResponseEntityBuilder() {
        return new DefaultResponseEntityBuilder();
    }

}
```

---

|                **Validation**                 |           **Domain-Diagrammer**            |
|:---------------------------------------------:|:------------------------------------------:|
| [<< Previous](validation_support_de.md) | [Nächste Seite >>](domain_diagrammer_de.md) |

---

**DE** / [EN](../../english/features/spring_web_integration_en.md)
