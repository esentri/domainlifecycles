[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Spring-Web-Integration](spring_web_integration_en.md)

---

# Spring-Web-Integration

Seamless integration between Spring-Web and DLC to use identities and single-valued ValueObjects 
as path/query parameters.

---

## Configuration
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

|             **Validation**              |       **Domain-Diagrammer**        |
|:---------------------------------------:|:----------------------------------:|
| [<< Previous](validation_support_en.md) | [Next >>](domain_diagrammer_en.md) |

---

**EN** / [DE](../../german/features/spring_web_integration_de.md)
