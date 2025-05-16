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
 *  Copyright 2019-2025 the original author or authors.
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

/**
 * Represents the layout configuration settings for a domain diagram.
 * The configuration includes properties such as direction, ranker, and acycler
 * which dictate the layout behavior.
 * <p>
 * This class is immutable, and the default values for its fields are:
 * - direction: "down"
 * - ranker: "longest-path"
 * - acycler: "greedy"
 * <p>
 * Use {@code LayoutSettingsBuilder} for constructing instances of {@code LayoutSettings}
 * with custom configurations.
 *
 * @author Mario Herb
 */
public class LayoutSettings {

    private static final String DEFAULT_DIRECTION = "down";
    private static final String DEFAULT_RANKER = "longest-path";
    private static final String DEFAULT_ACYCLER = "greedy";

    private final String direction;
    private final String ranker;
    private final String acycler;

    private LayoutSettings(
        String direction,
        String ranker,
        String acycler
    ) {
        this.direction = direction;
        this.ranker = ranker;
        this.acycler = acycler;
    }

    /**
     * Retrieves the layout direction setting.
     *
     * @return the direction configuration as a string
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Retrieves the ranker configuration for layout settings.
     *
     * @return the ranker configuration as a string
     */
    public String getRanker() {
        return ranker;
    }

    /**
     * Retrieves the acycler configuration for layout settings.
     *
     * @return the acycler configuration as a string
     */
    public String getAcycler() {
        return acycler;
    }

    /**
     * Creates a new builder for {@code LayoutSettings}.
     * The builder provides a fluent API to configure properties such as direction,
     * ranker, and acycler for a domain diagram layout.
     *
     * @return a new instance of {@code LayoutSettingsBuilder}
     */
    public static LayoutSettingsBuilder builder() {
        return new LayoutSettingsBuilder();
    }

    /**
     * StyleSettingsBuilder class for creating instances of {@code LayoutSettings}.
     * Provides a fluent API to configure properties such as direction, ranker, and acycler
     * for a domain diagram layout.
     */
    public static class LayoutSettingsBuilder {
        private String direction$value;
        private String ranker$value;
        private String acycler$value;

        LayoutSettingsBuilder() {
        }

        /**
         * Configures the direction of the layout in the domain diagram.
         *
         * @param direction the direction to apply for the layout (e.g., "right", "down")
         * @return the current instance of {@code LayoutSettingsBuilder} for method chaining
         */
        public LayoutSettingsBuilder withDirection(String direction) {
            this.direction$value = direction;
            return this;
        }

        /**
         * Sets the ranker configuration for the domain diagram.
         *
         * @param ranker the ranker style to use for organizing elements (e.g., "network-simplex")
         * @return the current instance of {@code LayoutSettingsBuilder} for method chaining
         */
        public LayoutSettingsBuilder withRanker(String ranker) {
            this.ranker$value = ranker;
            return this;
        }

        /**
         * Configures the acycler setting for the domain diagram.
         *
         * @param acycler the acycler algorithm to be used for edge rendering
         * @return the current instance of {@code LayoutSettingsBuilder} for method chaining
         */
        public LayoutSettingsBuilder withAcycler(String acycler) {
            this.acycler$value = acycler;
            return this;
        }

        /**
         * Builds and returns a {@code LayoutSettings} instance by applying the configured properties.
         * If any property is not explicitly set, a default value is used. That also means explicitly setting
         * null activates the default.
         *
         * @return a new {@code LayoutSettings} instance containing the configured or default values for direction, ranker, and acycler.
         */
        public LayoutSettings build() {
            return new LayoutSettings(
                direction$value == null ? DEFAULT_DIRECTION : direction$value,
                ranker$value == null ? DEFAULT_RANKER : ranker$value,
                acycler$value == null ? DEFAULT_ACYCLER : acycler$value
            );
        }
    }
}
