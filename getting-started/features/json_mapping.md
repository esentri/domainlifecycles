[Getting Started](../index.md) / [Features](../features.md) / [JSON-Mapping](json_mapping.md)

<hr/>

# JSON-Mapping
[Jackson](https://github.com/FasterXML/jackson) based JSON mapping.
-   Auto Mapping DDD Bausteine von JSON zu Java und vice versa
-   Spring kompatibel
-   MappingCustomizer API um Mapping zu individualisieren
-   Optional: Sequenz basiertes Bereitstellen einer ID durch Datenbank innerhalb des JSON-Mappers

<hr/>

## Implementierung
Ein an DDD angepasstes, besseres JSON-Mapping wird bereits sichergestellt durch die 
vorgenommene Konfiguration unter [Projekt erstellen](../configuration.md#JSON-Mapping).

Im weiteren Verlauf kann das Default-Mapping angepasst werden durch das überschreiben einer oder mehrerer Methoden des
`JacksonMappingCustomizer`, wie folgt:
```
public class CustomerMappingCustomizer extends JacksonMappingCustomizer<Customer>{

    public CustomerMappingCustomizer() {
        super(Customer.class);
    }

    @Override
    public void afterObjectRead(PersistableMappingContext mappingContext, ObjectCodec codec) {
        DomainObjectBuilder<?> b = mappingContext.domainObjectBuilder;
        
        // alter some of the mapping configurations
    }

}
```

und die Konfiguration anschließend aktivieren:

```
@Configuration
public class JacksonConfiguration {

    @Bean
    DlcJacksonModule dlcModuleConfiguration(List<? extends JacksonMappingCustomizer<?>> customizers,
                                            DomainObjectBuilderProvider domainObjectBuilderProvider,
                                            EntityIdentityProvider entityIdentityProvider,
                                            IdentityFactory identityFactory,
                                            ClassProvider classProvider) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider, identityFactory, classProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }
}
```

<hr/>

|            **Domain-Object Builders**             |            **OpenAPI-Extension**            |
|:-------------------------------------------------:|:-------------------------------------------:|
| [<< Vorherige Seite](./dommainobject_builders.md) | [Nächste Seite >>](./open_api_extension.md) |
