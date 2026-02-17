package io.domainlifecycles.autoconfig.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dlc.features")
public class FeatureProperties {

    private final Feature builder = new Feature();
    private final Mirror mirror = new Mirror();
    private final Feature jackson2 = new Feature();
    private final Feature jackson = new Feature();
    private final Persistence persistence = new Persistence();
    private final Events events = new Events();
    private final Feature openapi = new Feature();
    private final Feature springweb = new Feature();
    private final ServiceKinds serviceKinds = new ServiceKinds();

    public Feature getBuilder() {
        return builder;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public Feature getJackson2() {
        return jackson2;
    }

    public Feature getJackson() {
        return jackson;
    }

    public Persistence getPersistence() {
        return persistence;
    }

    public Events getEvents() {
        return events;
    }

    public Feature getOpenapi() {
        return openapi;
    }

    public Feature getSpringweb() {
        return springweb;
    }

    public ServiceKinds getServiceKinds() {
        return serviceKinds;
    }

    public static class Feature {

        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Mirror {

        private boolean enabled = true;
        private String[] basePackages;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getBasePackages() {
            return basePackages;
        }

        public void setBasePackages(String[] basePackages) {
            this.basePackages = basePackages;
        }
    }

    public static class Persistence {

        private boolean enabled = true;
        private String jooqRecordPackage;
        private String sqlDialect;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getJooqRecordPackage() {
            return jooqRecordPackage;
        }

        public void setJooqRecordPackage(String jooqRecordPackage) {
            this.jooqRecordPackage = jooqRecordPackage;
        }

        public String getSqlDialect() {
            return sqlDialect;
        }

        public void setSqlDialect(String sqlDialect) {
            this.sqlDialect = sqlDialect;
        }
    }

    public static class Events {

        private final Inmemory inmemory = new Inmemory();
        private final Springbus springbus = new Springbus();

        public Inmemory getInmemory() {
            return inmemory;
        }

        public Springbus getSpringbus() {
            return springbus;
        }
    }

    public static class ServiceKinds {
        private boolean enabled = true;
        private String[] packages;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getPackages() {
            return packages;
        }

        public void setPackages(String[] packages) {
            this.packages = packages;
        }
    }

    public static class Springbus{
        private boolean enabled = true;
        private boolean aggregateDomainEvents = true;
        private boolean serviceKindsProxy = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isAggregateDomainEvents() {
            return aggregateDomainEvents;
        }

        public void setAggregateDomainEvents(boolean aggregateDomainEvents) {
            this.aggregateDomainEvents = aggregateDomainEvents;
        }

        public boolean isServiceKindsProxy() {
            return serviceKindsProxy;
        }

        public void setServiceKindsProxy(boolean serviceKindsProxy) {
            this.serviceKindsProxy = serviceKindsProxy;
        }
    }

    public static class Inmemory {

        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
