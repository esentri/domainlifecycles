[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Open-API Extension](open_api_extension_en.md)

---

# Open-API Extension
API documentation based on [Spring Doc Open API](https://springdoc.org/), adapted to the needs of DDD, 
such as out-of-the-box use of the DLC Jackson auto-mapping feature and also showing the bean validations 
in the API documentation.

---

## Implementation
DLC supports the Spring Open API documentation of the REST interfaces, conforming to the mapping defined 
under [JSON-Mapping](json_mapping_en.md).
This is particularly relevant for objects such as IDs or 'single valued' value objects.
These are accordingly displayed more legibly in the Open API documentation.
The implementation is analogous to the general use of the Open API documentation
in Spring-Boot projects, DLC only influences the schema descriptions of the parameters and return values, and also the 
addition of validations, if any have been defined (see [Validations](validation_support_en.md)).

```Java
@RequestMapping("/api/customer")
public interface CustomerAPI {

    //...

    @Operation(summary = "Get existing customer by id")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "400", description = "Client Error")
    @ApiResponse(responseCode = "500", description = "Server Error")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ResponseObject<Customer>> get(@PathVariable("id") Customer.CustomerId id);

    //...
}
```

---

|         **JSON-Mapping**          |       **Persistence**        |
|:---------------------------------:|:----------------------------:|
| [<< Previous](json_mapping_en.md) | [Next >>](persistence_en.md) |

---

**EN** / [DE](../../german/features/open_api_extension_de.md)
