/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.diagram.domain.config;

import io.domainlifecycles.diagram.DiagramConfig;

import java.util.Objects;

/**
 * Configuration options for a Domain Diagram
 *
 * @author Mario Herb
 */
public class DomainDiagramConfig implements DiagramConfig {
    
    private GeneralVisualSettings generalVisualSettings = GeneralVisualSettings.builder().build();
    private LayoutSettings layoutSettings =  LayoutSettings.builder().build();
    private StyleSettings styleSettings =  StyleSettings.builder().build();
    private DiagramTrimSettings diagramTrimSettings = DiagramTrimSettings.builder().build();

    /**
     * Gets the general visual settings for the diagram.
     *
     * @return the general visual settings configuration
     */
    public GeneralVisualSettings getGeneralVisualSettings() {
        return generalVisualSettings;
    }

    /**
     * Gets the layout settings for the diagram.
     *
     * @return the layout settings configuration
     */
    public LayoutSettings getLayoutSettings() {
        return layoutSettings;
    }

    /**
     * Gets the style settings for the diagram.
     *
     * @return the style settings configuration
     */
    public StyleSettings getStyleSettings() {
        return styleSettings;
    }

    /**
     * Gets the diagram trim settings.
     *
     * @return the diagram trim settings configuration
     */
    public DiagramTrimSettings getDiagramTrimSettings() {
        return diagramTrimSettings;
    }

    /**
     * Sets the general visual settings for the diagram.
     *
     * @param generalVisualSettings the general visual settings configuration to set
     * @throws NullPointerException if generalVisualSettings is null
     */
    public void setGeneralVisualSettings(GeneralVisualSettings generalVisualSettings) {
        this.generalVisualSettings = Objects.requireNonNull(generalVisualSettings, "GeneralVisualSettings must not be null");
    }

    /**
     * Sets the layout settings for the diagram.
     *
     * @param layoutSettings the layout settings configuration to set
     * @throws NullPointerException if layoutSettings is null
     */
    public void setLayoutSettings(LayoutSettings layoutSettings) {
        this.layoutSettings = Objects.requireNonNull(layoutSettings, "LayoutSettings must not be null");
    }

    /**
     * Sets the style settings for the diagram.
     *
     * @param styleSettings the style settings configuration to set
     * @throws NullPointerException if styleSettings is null
     */
    public void setStyleSettings(StyleSettings styleSettings) {
        this.styleSettings = Objects.requireNonNull(styleSettings, "StyleSettings must not be null");
    }

    /**
     * Sets the diagram trim settings.
     *
     * @param diagramTrimSettings the diagram trim settings configuration to set
     * @throws NullPointerException if diagramTrimSettings is null
     */
    public void setDiagramTrimSettings(DiagramTrimSettings diagramTrimSettings) {
        this.diagramTrimSettings = Objects.requireNonNull(diagramTrimSettings, "DiagramTrimSettings must not be null");
    }

    /**
     * Creates a new instance of DomainDiagramConfigBuilder.
     *
     * @return a new DomainDiagramConfigBuilder instance
     */
    public static DomainDiagramConfigBuilder builder() {
        return new DomainDiagramConfigBuilder();
    }

    /**
     * Builder class for creating DomainDiagramConfig instances.
     */
    public static class DomainDiagramConfigBuilder {
        private GeneralVisualSettings generalVisualSettings = GeneralVisualSettings.builder().build();
        private LayoutSettings layoutSettings = LayoutSettings.builder().build();
        private StyleSettings styleSettings = StyleSettings.builder().build();
        private DiagramTrimSettings diagramTrimSettings = DiagramTrimSettings.builder().build();

        /**
         * Sets the general visual settings for the diagram.
         *
         * @param generalVisualSettings the general visual settings to be used
         * @return this builder instance
         * @throws NullPointerException if generalVisualSettings is null
         */
        public DomainDiagramConfigBuilder withGeneralVisualSettings(GeneralVisualSettings generalVisualSettings) {
            this.generalVisualSettings = Objects.requireNonNull(generalVisualSettings, "GeneralVisualSettings must not be null");
            return this;
        }

        /**
         * Sets the layout settings for the diagram.
         *
         * @param layoutSettings the layout settings to be used
         * @return this builder instance
         * @throws NullPointerException if layoutSettings is null
         */
        public DomainDiagramConfigBuilder withLayoutSettings(LayoutSettings layoutSettings) {
            this.layoutSettings = Objects.requireNonNull(layoutSettings, "LayoutSettings must not be null");
            return this;
        }

        /**
         * Sets the style settings for the diagram.
         *
         * @param styleSettings the style settings to be used
         * @return this builder instance
         * @throws NullPointerException if styleSettings is null
         */
        public DomainDiagramConfigBuilder withStyleSettings(StyleSettings styleSettings) {
            this.styleSettings = Objects.requireNonNull(styleSettings, "StyleSettings must not be null");
            return this;
        }

        /**
         * Sets the diagram trim settings.
         *
         * @param diagramTrimSettings the diagram trim settings to be used
         * @return this builder instance
         * @throws NullPointerException if diagramTrimSettings is null
         */
        public DomainDiagramConfigBuilder withDiagramTrimSettings(DiagramTrimSettings diagramTrimSettings) {
            this.diagramTrimSettings = Objects.requireNonNull(diagramTrimSettings, "DiagramTrimSettings must not be null");
            return this;
        }

        /**
         * Builds a new DomainDiagramConfig instance with the current builder settings.
         *
         * @return a new DomainDiagramConfig instance
         */
        public DomainDiagramConfig build() {
            DomainDiagramConfig config = new DomainDiagramConfig();
            config.setGeneralVisualSettings(generalVisualSettings);
            config.setLayoutSettings(layoutSettings);
            config.setStyleSettings(styleSettings);
            config.setDiagramTrimSettings(diagramTrimSettings);
            return config;
        }
    }

}
