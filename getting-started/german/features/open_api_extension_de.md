[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Open-API Extension](open_api_extension_de.md)

---

# Open-API Extension
API Dokumentation auf Basis von [Spring Doc Open API](https://springdoc.org/), angepasst auf die Bedürfnisse von DDD,
wie zum Beispiel out-of-the-box Nutzung des DLC Jackson Auto-Mapping Features und auch Aufnehmen von Bean-Validations 
in die API-Doku.

---

## Implementierung
DLC unterstützt die Spring Open-API Dokumentation der REST-Schnittstellen,
und zwar konform zum unter [JSON-Mapping](json_mapping_de.md) definierten Mapping.
Insbesondere relevant für Objekte wie ID's oder 'single valued' Value-Objects.
Diese werden dementsprechend leserlicher in der Open-API Dokumentation dargestellt.
Die Implementierung erfolgt dabei analog zur allgemeinen Nutzung der Open-API Dokumentation
in Spring-Boot Projekten, DLC beeinflusst lediglich die Schema-Beschreibungen der Parameter und
Rückgabe-Werte, und jedoch auch das Hinzufügen von Validations, sofern hier welche definiert wurden (siehe [Validations](validation_support_de.md)).

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

|            **JSON-Mapping**             |           **Persistence**            |
|:---------------------------------------:|:------------------------------------:|
| [<< Previous](json_mapping_de.md) | [Nächste Seite >>](persistence_de.md) |

---

**DE** / [EN](../../english/features/open_api_extension_en.md)
