/* Instantiierung
                             test_root
                             /
                          1:1 must test_entity_1
                          /              \
            1:1 opt test_entity_2  a     1:1 opt test_entity_2 b
                 /                          \
            1:n  opt test_entity_3            1:n  opt test_entity_3
          /                                     \
    1:n  opt test_entity_4                   1:n  opt test_entity_4
            /                                     \
1:n opt test_entity_5                          1:n opt test_entity_5
         /                                           \
n:1 opt test_entity_6                             n:1 opt test_entity_6
 */

-- the "test_domain" schema itself is created by Flyway (ContainerMigrations.migrate's createSchemas(true)), not by this script

CREATE TABLE test_domain.test_root
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_entity_2
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id)
);

CREATE TABLE test_domain.test_entity_1
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    test_entity_2_id_a  BIGINT,
    test_entity_2_id_b  BIGINT,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id),
    FOREIGN KEY (test_entity_2_id_a) REFERENCES test_domain.test_entity_2 (id),
    FOREIGN KEY (test_entity_2_id_b) REFERENCES test_domain.test_entity_2 (id)
);

CREATE TABLE test_domain.test_entity_3
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    test_entity_2_id    BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id),
    FOREIGN KEY (test_entity_2_id) REFERENCES test_domain.test_entity_2 (id)
);

CREATE TABLE test_domain.test_entity_4
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_entity_3_id    BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_entity_3_id) REFERENCES test_domain.test_entity_3 (id)
);

CREATE TABLE test_domain.test_entity_6
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_entity_5
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    test_entity_4_id    BIGINT NOT NULL,
    test_entity_6_id    BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id),
    FOREIGN KEY (test_entity_4_id) REFERENCES test_domain.test_entity_4 (id),
    FOREIGN KEY (test_entity_6_id) REFERENCES test_domain.test_entity_6 (id)
);

CREATE SEQUENCE test_domain.test_root_simple_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111;


CREATE TABLE test_domain.test_root_simple
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_entity_one_to_one_leading
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_root_one_to_one_leading
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_entity_id      BIGINT,
    name                VARCHAR(200),
    FOREIGN KEY (test_entity_id) REFERENCES test_domain.test_entity_one_to_one_leading (id)
);


CREATE TABLE test_domain.test_root_one_to_one_following
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_entity_one_to_one_following
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_one_to_one_following (id)
);

CREATE TABLE test_domain.test_root_one_to_many
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_entity_one_to_many
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_one_to_many (id)
);

CREATE UNIQUE INDEX test_entity_3_name_unique ON test_domain.test_entity_3 (name);

CREATE TABLE test_domain.test_entity_b_one_to_one_following_leading
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_root_one_to_one_following_leading
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_entity_id      BIGINT,
    name                VARCHAR(200),
    FOREIGN KEY (test_entity_id) REFERENCES test_domain.test_entity_b_one_to_one_following_leading (id)
);

CREATE TABLE test_domain.test_entity_a_one_to_one_following_leading
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_one_to_one_following_leading (id)
);

CREATE TABLE test_domain.test_root_hierarchical
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    parent_id           BIGINT,
    name                VARCHAR(200),
    FOREIGN KEY (parent_id) REFERENCES test_domain.test_root_hierarchical (id)
);

CREATE TABLE test_domain.test_root_hierarchical_backref
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    parent_id           BIGINT,
    name                VARCHAR(200),
    FOREIGN KEY (parent_id) REFERENCES test_domain.test_root_hierarchical_backref (id)
);

CREATE TABLE test_domain.test_root_many_to_many
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_entity_many_to_many_a
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_root_id        BIGINT NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_many_to_many (id)
);

CREATE TABLE test_domain.test_entity_many_to_many_b
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_entity_many_to_many_join
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    test_entity_a_id    BIGINT NOT NULL,
    test_entity_b_id    BIGINT NOT NULL,
    FOREIGN KEY (test_entity_a_id) REFERENCES test_domain.test_entity_many_to_many_a (id),
    FOREIGN KEY (test_entity_b_id) REFERENCES test_domain.test_entity_many_to_many_b (id)
);

CREATE TABLE test_domain.test_root_simple_uuid
(
    id                  VARCHAR(36) PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE SEQUENCE test_domain.vo_aggregate_three_level_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_aggregate_three_level
(
    identification_number                             BIGINT PRIMARY KEY,
    concurrency_version                              BIGINT NOT NULL,
    info                                             VARCHAR(200),
    my_complex_vo_value_a                            VARCHAR(200),
    my_complex_vo_value_b_value                      VARCHAR(200),
    three_level_vo_own_value                         BIGINT,
    three_level_vo_level_two_a_level_three_a_text    VARCHAR(200),
    three_level_vo_level_two_a_level_three_a_another VARCHAR(200),
    three_level_vo_level_two_a_level_three_b_text    VARCHAR(200),
    three_level_vo_level_two_a_level_three_b_another VARCHAR(200),
    three_level_vo_level_two_b_level_three_a_text    VARCHAR(200),
    three_level_vo_level_two_b_level_three_a_another VARCHAR(200),
    three_level_vo_level_two_b_level_three_b_text    VARCHAR(200),
    three_level_vo_level_two_b_level_three_b_another VARCHAR(200)
);

/*
   1 OrderBv3 has 1-n OrderItemsBv3
   1 OrderBv3 has exactly 1 OrderStatusBv3
   1 OrderBv3 has 0-n CommentsBv3
   1 OrderBv3 must have exactly 1 DeliveryAddressBv3
   1 OrderBv3 can have several PromoCodesBv3 (Value Object) assigned

   Only a maximum of 1 item per article is allowed per order, i.e.
   there must not be 2 items in an order referencing the same article

   The article referenced by an order item points to another AggregateRoot

   The customer identified by customer number is part of another Bounded Context
 */

CREATE SEQUENCE test_domain.delivery_address_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.delivery_address_bv3
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200) NOT NULL,
    street              VARCHAR(200) NOT NULL,
    postal_code         VARCHAR(10) NOT NULL,
    city                VARCHAR(200) NOT NULL
);

CREATE SEQUENCE test_domain.order_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.order_bv3
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    priority            BIGINT NOT NULL,
    customer_number     VARCHAR(20) NOT NULL,
    delivery_address_id BIGINT NOT NULL,
    FOREIGN KEY (delivery_address_id) REFERENCES test_domain.delivery_address_bv3 (id)
);

CREATE TABLE test_domain.promo_code_bv3
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(10) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.order_bv3 (id)
);

CREATE SEQUENCE test_domain.promo_code_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE SEQUENCE test_domain.order_item_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.order_item_bv3
(
    id                    BIGINT PRIMARY KEY,
    concurrency_version   BIGINT NOT NULL,
    order_id              BIGINT NOT NULL,
    article_id            BIGINT NOT NULL,
    quantity              BIGINT NOT NULL,
    unit_price_amount     NUMERIC(10,2) NOT NULL,
    unit_price_currency   VARCHAR(3) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES test_domain.order_bv3 (id)
);

CREATE UNIQUE INDEX order_article_bv3_unique ON test_domain.order_item_bv3 (order_id, article_id);

CREATE SEQUENCE test_domain.order_status_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.order_status_bv3
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    order_id            BIGINT NOT NULL,
    status_code         VARCHAR(20),
    status_changed_at   TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES test_domain.order_bv3 (id)
);

CREATE SEQUENCE test_domain.order_comment_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.order_comment_bv3
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    order_id            BIGINT NOT NULL,
    commented_at        TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    comment_text        VARCHAR(1000),
    FOREIGN KEY (order_id) REFERENCES test_domain.order_bv3 (id)
);

/*
Value Objects case
*/

CREATE TABLE test_domain.vo_aggregate_root
(
    id                          BIGINT PRIMARY KEY,
    concurrency_version         BIGINT NOT NULL,
    text                        VARCHAR(20) NULL,
    my_simple_vo_value          VARCHAR(20) NOT NULL,
    my_complex_vo_value_a       VARCHAR(20) NULL,
    my_complex_vo_value_b_value VARCHAR(20) NULL,
    vo_identity_ref_value       VARCHAR(20) NULL,
    vo_identity_ref_id_ref      BIGINT NULL
);

CREATE TABLE test_domain.vo_entity
(
    id                          BIGINT PRIMARY KEY,
    root_id                     BIGINT NOT NULL,
    concurrency_version         BIGINT NOT NULL,
    text                        VARCHAR(20) NULL,
    my_complex_vo_value_a       VARCHAR(20) NULL,
    my_complex_vo_value_b_value VARCHAR(20) NULL,
    FOREIGN KEY (root_id) REFERENCES test_domain.vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.simple_vo_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.simple_vo_one_to_many
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.simple_vo_one_to_many_2_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.simple_vo_one_to_many_2
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.simple_vo_one_to_many_3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.simple_vo_one_to_many_3
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.simple_vo_one_to_many_2 (id)
);

CREATE SEQUENCE test_domain.vo_one_to_many_entity_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_one_to_many_entity
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_entity (id)
);

CREATE SEQUENCE test_domain.vo_one_to_many_entity_2_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_one_to_many_entity_2
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_one_to_many_entity (id)
);

/*
Auto Mapping Value Objects case
*/

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root
(
    id                          BIGINT PRIMARY KEY,
    concurrency_version         BIGINT NOT NULL,
    text                        VARCHAR(20) NULL,
    my_simple_vo_value          VARCHAR(20) NOT NULL,
    my_complex_vo_value_a       VARCHAR(20) NULL,
    my_complex_vo_value_b_value VARCHAR(20) NULL,
    vo_identity_ref_value       VARCHAR(20) NULL,
    vo_identity_ref_id_ref      BIGINT NULL
);

CREATE TABLE test_domain.auto_mapped_vo_entity
(
    id                          BIGINT PRIMARY KEY,
    root_id                     BIGINT NOT NULL,
    concurrency_version         BIGINT NOT NULL,
    text                        VARCHAR(20) NULL,
    my_complex_vo_value_a       VARCHAR(20) NULL,
    my_complex_vo_value_b_value VARCHAR(20) NULL,
    FOREIGN KEY (root_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.amvo_root_o2m2_o2m3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.amvo_root_o2m2_o2m3
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2 (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_entity_value_objects_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_entity_value_objects_one_to_many
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_entity (id)
);

CREATE SEQUENCE test_domain.amvo_entity_o2m_o2m_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.amvo_entity_o2m_o2m
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value        VARCHAR(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_entity_value_objects_one_to_many (id)
);


/*
 Configuration
 */

CREATE TABLE test_domain.configuration
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL
);

CREATE TABLE test_domain.global_configuration_table_entry
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    configuration_id    BIGINT NOT NULL,
    x                   INTEGER,
    y                   INTEGER,
    FOREIGN KEY (configuration_id) REFERENCES test_domain.configuration (id)
);

CREATE TABLE test_domain.another_configuration
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL
);

CREATE TABLE test_domain.tangible_configuration_table_entry
(
    id                       BIGINT PRIMARY KEY,
    concurrency_version      BIGINT NOT NULL,
    another_configuration_id BIGINT NOT NULL,
    x                        INTEGER,
    y                        INTEGER,
    FOREIGN KEY (another_configuration_id) REFERENCES test_domain.another_configuration (id)
);


/*
 Optional Case
 */
CREATE TABLE test_domain.ref_agg
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    mandatory_text      VARCHAR(20) NOT NULL,
    optional_text       VARCHAR(20) NULL
);

CREATE SEQUENCE test_domain.optional_entity_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.optional_entity
(
    id                                                                 BIGINT PRIMARY KEY,
    concurrency_version                                                BIGINT NOT NULL,
    mandatory_text                                                     VARCHAR(20) NOT NULL,
    optional_text                                                      VARCHAR(20) NULL,
    mandatory_simple_value_object_value                                VARCHAR(20) NOT NULL,
    optional_simple_value_object_value                                 VARCHAR(20) NULL,
    mandatory_complex_value_object_mandatory_text                      VARCHAR(20) NOT NULL,
    mandatory_complex_value_object_optional_text                       VARCHAR(20) NULL,
    mandatory_complex_vo_mandatory_simple_vo_value VARCHAR(20) NOT NULL,
    mandatory_complex_vo_optional_simple_vo_value  VARCHAR(20) NULL,
    mandatory_complex_value_object_optional_long                       BIGINT NULL,
    optional_complex_value_object_mandatory_text                       VARCHAR(20) NULL,
    optional_complex_value_object_optional_text                        VARCHAR(20) NULL,
    optional_complex_vo_mandatory_simple_vo_value  VARCHAR(20) NULL,
    optional_complex_vo_optional_simple_vo_value   VARCHAR(20) NULL,
    optional_complex_value_object_optional_long                        BIGINT NULL
);

CREATE SEQUENCE test_domain.optional_aggregate_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.optional_aggregate
(
    id                                                                 BIGINT PRIMARY KEY,
    concurrency_version                                                BIGINT NOT NULL,
    mandatory_text                                                     VARCHAR(20) NOT NULL,
    optional_text                                                      VARCHAR(20) NULL,
    optional_long                                                      BIGINT NULL,
    mandatory_simple_value_object_value                                VARCHAR(20) NOT NULL,
    optional_simple_value_object_value                                 VARCHAR(20) NULL,
    mandatory_complex_value_object_mandatory_text                      VARCHAR(20) NOT NULL,
    mandatory_complex_value_object_optional_text                       VARCHAR(20) NULL,
    mandatory_complex_vo_mandatory_simple_vo_value VARCHAR(20) NOT NULL,
    mandatory_complex_vo_optional_simple_vo_value  VARCHAR(20) NULL,
    mandatory_complex_value_object_optional_long                       BIGINT NULL,
    optional_entity_id                                                 BIGINT NULL,
    optional_ref_id                                                    BIGINT NULL,
    ref_value_object_mandatory_text                                    VARCHAR(20) NOT NULL,
    ref_value_object_optional_ref                                      BIGINT NULL,
    optional_complex_value_object_mandatory_text                       VARCHAR(20) NULL,
    optional_complex_value_object_optional_text                        VARCHAR(20) NULL,
    optional_complex_vo_mandatory_simple_vo_value  VARCHAR(20) NULL,
    optional_complex_vo_optional_simple_vo_value   VARCHAR(20) NULL,
    optional_complex_value_object_optional_long                        BIGINT NULL,
    FOREIGN KEY (optional_entity_id) REFERENCES test_domain.optional_entity (id),
    FOREIGN KEY (optional_ref_id) REFERENCES test_domain.ref_agg (id),
    FOREIGN KEY (ref_value_object_optional_ref) REFERENCES test_domain.ref_agg (id)
);

CREATE TABLE test_domain.optional_entity_complex_value_object_list
(
    id                                  BIGINT PRIMARY KEY,
    container_id                        BIGINT NOT NULL,
    mandatory_text                      VARCHAR(20) NOT NULL,
    optional_text                       VARCHAR(20) NULL,
    mandatory_simple_value_object_value VARCHAR(20) NOT NULL,
    optional_simple_value_object_value  VARCHAR(20) NULL,
    optional_long                       BIGINT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.optional_entity (id)
);

CREATE SEQUENCE test_domain.optional_entity_complex_value_object_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.optional_aggregate_ref_value_object_list
(
    id             BIGINT PRIMARY KEY,
    container_id   BIGINT NOT NULL,
    mandatory_text VARCHAR(20) NOT NULL,
    optional_ref   BIGINT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.optional_aggregate (id),
    FOREIGN KEY (optional_ref) REFERENCES test_domain.ref_agg (id)
);

CREATE SEQUENCE test_domain.optional_aggregate_ref_value_object_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;


CREATE TABLE test_domain.test_entity
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL
);

CREATE TABLE test_domain.validated_aggregate_root
(
    id                  BIGINT PRIMARY KEY,
    text                VARCHAR(100) NOT NULL,
    optional_text       VARCHAR(10),
    concurrency_version BIGINT NOT NULL
);

CREATE TABLE test_domain.validated_aggregate_root2
(
    id                  BIGINT PRIMARY KEY,
    text                VARCHAR(100) NOT NULL,
    optional_text       VARCHAR(10),
    concurrency_version BIGINT NOT NULL
);

CREATE TABLE test_domain.test_root_temporal
(
    id                  BIGINT PRIMARY KEY,
    local_date          DATE,
    local_time          TIMESTAMP(6) WITHOUT TIME ZONE,
    local_date_time     TIMESTAMP(6) WITHOUT TIME ZONE,
    my_year             INTEGER,
    year_month          INTEGER,
    month_day           SMALLINT,
    zoned_date_time     TIMESTAMP(6) WITH TIME ZONE,
    my_calendar         TIMESTAMP(6) WITH TIME ZONE,
    my_date             TIMESTAMP(6) WITH TIME ZONE,
    my_instant          TIMESTAMP(6) WITH TIME ZONE,
    offset_date_time    TIMESTAMP(6) WITH TIME ZONE,
    offset_time         TIMESTAMP(6) WITH TIME ZONE,
    concurrency_version BIGINT NOT NULL
);

CREATE SEQUENCE test_domain.vehicle_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vehicle
(
    id                  BIGINT PRIMARY KEY,
    length_cm           INTEGER,
    brand               VARCHAR(100),
    type                VARCHAR(100),
    gears               BIGINT,
    concurrency_version BIGINT NOT NULL
);

CREATE SEQUENCE test_domain.vehicle_extended_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vehicle_extended
(
    id                  BIGINT PRIMARY KEY,
    length_cm           INTEGER,
    brand               VARCHAR(100),
    type                VARCHAR(100),
    gears               BIGINT,
    concurrency_version BIGINT NOT NULL
);

CREATE SEQUENCE test_domain.engine_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.engine
(
    id                  BIGINT PRIMARY KEY,
    vehicle_extended_id BIGINT NOT NULL,
    ps                  INTEGER,
    type                VARCHAR(100),
    concurrency_version BIGINT NOT NULL,
    FOREIGN KEY (vehicle_extended_id) REFERENCES test_domain.vehicle_extended (id)
);

CREATE SEQUENCE test_domain.bike_with_components_bike_components_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bike_with_components_bike_components
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    type         VARCHAR(100),
    manufacturer VARCHAR(100),
    FOREIGN KEY (container_id) REFERENCES test_domain.vehicle_extended (id)
);


CREATE SEQUENCE test_domain.record_test_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.record_test
(
    id                  BIGINT PRIMARY KEY,
    my_value            VARCHAR(100),
    my_vo_value1        VARCHAR(100),
    my_vo_value2        BIGINT,
    concurrency_version BIGINT NOT NULL
);

CREATE SEQUENCE test_domain.record_test_my_vo_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.record_test_my_vo_list
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value1       VARCHAR(100),
    value2       BIGINT,
    FOREIGN KEY (container_id) REFERENCES test_domain.record_test (id)
);

CREATE SEQUENCE test_domain.record_test_my_vo_set_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.record_test_my_vo_set
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    value1       VARCHAR(100),
    value2       BIGINT,
    FOREIGN KEY (container_id) REFERENCES test_domain.record_test (id)
);


CREATE TABLE test_domain.tree_root
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(100)
);

CREATE TABLE test_domain.tree_node
(
    id                  BIGINT PRIMARY KEY,
    node_name           VARCHAR(100),
    parent_node_id      BIGINT,
    root_id             BIGINT,
    concurrency_version BIGINT NOT NULL,
    FOREIGN KEY (parent_node_id) REFERENCES test_domain.tree_node (id),
    FOREIGN KEY (root_id) REFERENCES test_domain.tree_root (id)
);

CREATE TABLE test_domain.test_root_one_to_one_vo_dedicated
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_root_one_to_one_vo_dedicated_vo
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    name         VARCHAR(100),
    FOREIGN KEY (container_id) REFERENCES test_domain.test_root_one_to_one_vo_dedicated (id)
);

CREATE SEQUENCE test_domain.test_root_one_to_one_vo_dedicated_vo_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.concrete_root
(
    my_id               BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);


CREATE TABLE test_domain.vo_aggregate_two_level
(
    id                    BIGINT PRIMARY KEY,
    concurrency_version   BIGINT NOT NULL,
    level_one_first_text  VARCHAR(200),
    level_one_second_text VARCHAR(200),
    level_one_third_bool  BOOLEAN
);


CREATE TABLE test_domain.vo_aggregate_primitive
(
    id                          BIGINT PRIMARY KEY,
    concurrency_version         BIGINT NOT NULL,
    simple_val                  BIGINT,
    complex_val                 BIGINT,
    complex_num                 BIGINT,
    nested_simple_val           BIGINT,
    nested_complex_val          BIGINT,
    nested_complex_num          BIGINT,
    optional_simple_val         BIGINT,
    optional_complex_val        BIGINT,
    optional_complex_num        BIGINT,
    optional_nested_simple_val  BIGINT,
    optional_nested_complex_val BIGINT,
    optional_nested_complex_num BIGINT
);

CREATE TABLE test_domain.vo_aggregate_primitive_record_mapped_simple
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    val          BIGINT,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_primitive (id)
);

CREATE TABLE test_domain.vo_aggregate_primitive_record_mapped_complex
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    val          BIGINT,
    num          BIGINT,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_primitive (id)
);

CREATE TABLE test_domain.vo_aggregate_primitive_record_mapped_nested
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    simple_val   BIGINT,
    complex_val  BIGINT,
    complex_num  BIGINT,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_primitive (id)
);

CREATE SEQUENCE test_domain.vo_aggregate_primitive_record_mapped_simple_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_primitive_record_mapped_complex_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_primitive_record_mapped_nested_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_aggregate_nested
(
    id                                   BIGINT PRIMARY KEY,
    concurrency_version                  BIGINT NOT NULL,
    nested_enum_single_valued_enum_value CHAR(1),
    nested_simple_vo_nested_val          BIGINT,
    nested_id_id_ref                     BIGINT
);

CREATE TABLE test_domain.vo_aggregate_nested_nested_enum_single_valued_list
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    enum_value   CHAR(1),
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_nested (id)
);

CREATE TABLE test_domain.vo_aggregate_nested_nested_simple_vo_list
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    nested_val   BIGINT,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_nested (id)
);

CREATE TABLE test_domain.vo_aggregate_nested_nested_id_list
(
    id           BIGINT PRIMARY KEY,
    container_id BIGINT NOT NULL,
    id_ref       BIGINT,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_nested (id)
);

CREATE SEQUENCE test_domain.vo_aggregate_nested_nested_enum_single_valued_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_nested_nested_simple_vo_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_nested_nested_id_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.test_root_simple_ignoring
(
    id                  BIGINT PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200),
    ignored_column      VARCHAR(200)
);


CREATE TABLE test_domain.test_root_uuid
(
    id                  VARCHAR(36) PRIMARY KEY,
    concurrency_version BIGINT NOT NULL,
    name                VARCHAR(200)
);

CREATE TABLE test_domain.test_root_uuid_vo_list
(
    id                  VARCHAR(36) PRIMARY KEY,
    container_id        VARCHAR(36) NOT NULL,
    name                VARCHAR(200),
    FOREIGN KEY (container_id) REFERENCES test_domain.test_root_uuid (id)
);

CREATE TABLE test_domain.root_id_enum_list (
                                                    id BIGINT PRIMARY KEY,
                                                    concurrency_version BIGINT NOT NULL,
                                                    name VARCHAR(200)
);

CREATE TABLE test_domain.root_id_enum_list_enum_list (
                                                                         id BIGINT PRIMARY KEY,
                                                                         container_id BIGINT NOT NULL,
                                                                         value VARCHAR(20),
                                                                         FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list(id)
);

CREATE TABLE test_domain.root_id_enum_list_id_list (
                                                         id BIGINT PRIMARY KEY,
                                                         container_id BIGINT NOT NULL,
                                                         value BIGINT,
                                                         FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list (
                                               id BIGINT PRIMARY KEY,
                                               root_id BIGINT NOT NULL,
                                               concurrency_version BIGINT NOT NULL,
                                               FOREIGN KEY (root_id) REFERENCES test_domain.root_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_id_list (
                                                       id BIGINT PRIMARY KEY,
                                                       container_id BIGINT NOT NULL,
                                                       value BIGINT,
                                                       FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_enum_list (
                                                         id BIGINT PRIMARY KEY,
                                                         container_id BIGINT NOT NULL,
                                                         value VARCHAR(20),
                                                         FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_value_with_lists_enums (
                                                           id BIGINT PRIMARY KEY,
                                                           container_id BIGINT NOT NULL,
                                                           value VARCHAR(20),
                                                           FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_value_with_lists_ids (
                                                                        id BIGINT PRIMARY KEY,
                                                                        container_id BIGINT NOT NULL,
                                                                        value BIGINT,
                                                                        FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);
CREATE TABLE test_domain.root_id_enum_list_value_with_lists_list (
                                                        id BIGINT PRIMARY KEY,
                                                        container_id BIGINT NOT NULL,
                                                        FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list(id)
);

CREATE TABLE test_domain.root_id_enum_list_value_with_lists_list_enums (
                                                        id BIGINT PRIMARY KEY,
                                                        container_id BIGINT NOT NULL,
                                                        value VARCHAR(20),
                                                        FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list_value_with_lists_list(id)
);

CREATE TABLE test_domain.root_id_enum_list_value_with_lists_list_ids (
                                                        id BIGINT PRIMARY KEY,
                                                        container_id BIGINT NOT NULL,
                                                        value BIGINT,
                                                        FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list_value_with_lists_list(id)
);

CREATE SEQUENCE test_domain.root_id_enum_list_id_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.root_id_enum_list_enum_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_id_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_enum_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_value_with_lists_enums_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_value_with_lists_ids_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.root_id_enum_list_value_with_lists_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.root_id_enum_list_value_with_lists_list_enums_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.root_id_enum_list_value_with_lists_list_ids_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE SEQUENCE test_domain.root_id_enum_list_uuid_id_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.root_id_enum_list_uuid_id_list (
                                                        id BIGINT PRIMARY KEY,
                                                        container_id BIGINT NOT NULL,
                                                        value VARCHAR(36),
                                                        FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list(id)
);

/*
Array typed fields (see AssertedContainableTypeMirror#getBinaryTypeName)
*/

CREATE SEQUENCE test_domain.test_root_array_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111;

CREATE TABLE test_domain.test_root_array
(
    id                           BIGINT PRIMARY KEY,
    concurrency_version          BIGINT NOT NULL,
    name                         VARCHAR(20) NULL,
    payload                      BYTEA NULL,
    crypto_vo_chiffrat           BYTEA NULL,
    crypto_vo_salt               BYTEA NULL,
    crypto_vo_key_version        BIGINT NULL
);
