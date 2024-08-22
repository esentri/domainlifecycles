package io.domainlifecycles.events.springgruelboxintegration;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryClient;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.AnotherApplicationService;
import io.domainlifecycles.events.AnotherService;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.InitializedDomain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.util.List;
import java.util.Locale;

@SpringBootApplication()
public class TestApplicationGruelboxIntegration {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationGruelboxIntegration.class).run(args);
    }

    @Bean
    InitializedDomain initializedDomain(){
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events"));
        return Domain.getInitializedDomain();
    }

    @Bean
    public AnApplicationService anApplicationService(){
        return new AnApplicationService();
    }

    @Bean
    public ADomainService aDomainService(){
        return new ADomainService();
    }

    @Bean
    public ARepository aRepository(){
        return new ARepository();
    }

    @Bean
    public AQueryClient aQueryClient(){
        return new AQueryClient();
    }

    @Bean
    public AnOutboundService anOutboundService(){
        return new AnOutboundService();
    }

    @Bean
    public AnotherService anotherService(){
        return new AnotherService();
    }

    @Bean
    public AnotherApplicationService anotherApplicationService(AnotherService anotherService) { return new AnotherApplicationService(anotherService); }

    /**
     * This method creates and configures a ServiceProvider instance, which is responsible for providing instances of various types of services.
     * It takes three parameters: repositories, applicationServices, and domainServices, which are lists of Repository, ApplicationService, and DomainService instances respectively
     */
    @Bean
    public ServiceProvider serviceProvider(
        List<Repository<?,?>> repositories,
        List<ApplicationService> applicationServices,
        List<DomainService> domainServices,
        List<QueryClient<?>> queryClients,
        List<OutboundService> outboundServices
    ){
        var services = new Services();
        repositories.forEach(services::registerRepositoryInstance);
        applicationServices.forEach(services::registerApplicationServiceInstance);
        domainServices.forEach(services::registerDomainServiceInstance);
        queryClients.forEach(services::registerQueryClientInstance);
        outboundServices.forEach(services::registerOutboundServiceInstance);
        return services;
    }

    @Bean
    @DependsOn("initializedDomain")
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }

    @Bean
    @DependsOn("initializedDomain")
    DlcJacksonModule dlcModuleConfiguration(
        List<? extends JacksonMappingCustomizer<?>> customizers,
        DomainObjectBuilderProvider domainObjectBuilderProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(
            domainObjectBuilderProvider
        );
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }

}
