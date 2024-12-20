[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Open-API Extension](open_api_extension_en.md)

---

# Open-API Extension
API documentation based on [Spring Doc Open API](https://springdoc.org/), adapted to the needs of DDD, 
such as out-of-the-box use of the DLC Jackson auto-mapping feature and also showing the bean validations 
in the API documentation.

---

## Implementation
DLC unterstützt die Spring Open-API Dokumentation der REST-Schnittstellen,
und zwar konform zum unter [JSON-Mapping](json_mapping_en.md) definierten Mapping.
Insbesondere relevant für Objekte wie ID's oder 'single valued' Value-Objects.
Diese werden dementsprechend leserlicher in der Open-API Dokumentation dargestellt.
Die Implementation erfolgt dabei analog zur allgemeinen Nutzung der Open-API Dokumentation
in Spring-Boot Projekten, DLC beeinflusst lediglich die Schema-Beschreibungen der Parameter und
Rückgabe-Werte, und jedoch auch das Hinzufügen von Validations, sofern hier welche definiert wurden (siehe [Validations](validation_support_en.md)).

```
@RequestMapping("/api/customer")
public interface CustomerAPI {

...

    @Operation(summary = "Get existing customer by id")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "400", description = "Client Error")
    @ApiResponse(responseCode = "500", description = "Server Error")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ResponseObject<Customer>> get(@PathVariable("id") Customer.CustomerId id);
...
```

Zusätzlich zur Default-Konfiguration kann auch hier ein Customizer definiert werden:
```
@Bean
public DlcOpenApiCustomizer openApiCustomizer(
                                        SpringDocConfigProperties springDocConfigProperties,
                                        ClassProvider classProvider) {
    return new DlcOpenApiCustomizer(springDocConfigProperties, classProvider);
}
```

---

|          **JSON-Mapping**           |           **Persistence**            |
|:-----------------------------------:|:------------------------------------:|
| [<< Previous](json_mapping_en.md)   | [Next >>](persistence_en.md) |

---

**EN** / [DE](../../german/features/open_api_extension_de.md)
