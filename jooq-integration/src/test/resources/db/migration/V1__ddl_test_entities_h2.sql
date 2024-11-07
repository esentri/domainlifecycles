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

CREATE SCHEMA test_domain;

CREATE TABLE test_domain.test_root
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_entity_2
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id)
);

CREATE TABLE test_domain.test_entity_1
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    test_entity_2_id_a  NUMBER(18),
    test_entity_2_id_b  NUMBER(18),
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id),
    FOREIGN KEY (test_entity_2_id_a) REFERENCES test_domain.test_entity_2 (id),
    FOREIGN KEY (test_entity_2_id_b) REFERENCES test_domain.test_entity_2 (id)
);

CREATE TABLE test_domain.test_entity_3
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    test_entity_2_id    NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id),
    FOREIGN KEY (test_entity_2_id) REFERENCES test_domain.test_entity_2 (id)
);

CREATE TABLE test_domain.test_entity_4
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_entity_3_id    NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_entity_3_id) REFERENCES test_domain.test_entity_3 (id)
);

CREATE TABLE test_domain.test_entity_6
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_entity_5
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    test_entity_4_id    NUMBER(18) NOT NULL,
    test_entity_6_id    NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root (id),
    FOREIGN KEY (test_entity_4_id) REFERENCES test_domain.test_entity_4 (id),
    FOREIGN KEY (test_entity_6_id) REFERENCES test_domain.test_entity_6 (id)
);

CREATE SEQUENCE test_domain.test_root_simple_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111;


CREATE TABLE test_domain.test_root_simple
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_entity_one_to_one_leading
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_root_one_to_one_leading
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_entity_id      NUMBER(18),
    name                VARCHAR2(200),
    FOREIGN KEY (test_entity_id) REFERENCES test_domain.test_entity_one_to_one_leading (id)
);


CREATE TABLE test_domain.test_root_one_to_one_following
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_entity_one_to_one_following
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_one_to_one_following (id)
);

CREATE TABLE test_domain.test_root_one_to_many
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_entity_one_to_many
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_one_to_many (id)
);

CREATE UNIQUE INDEX test_domain.test_entity_3_name_unique ON test_domain.test_entity_3 (name);

CREATE TABLE test_domain.test_entity_b_one_to_one_following_leading
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_root_one_to_one_following_leading
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_entity_id      NUMBER(18),
    name                VARCHAR2(200),
    FOREIGN KEY (test_entity_id) REFERENCES test_domain.test_entity_b_one_to_one_following_leading (id)
);

CREATE TABLE test_domain.test_entity_a_one_to_one_following_leading
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_one_to_one_following_leading (id)
);

CREATE TABLE test_domain.test_root_hierarchical
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    parent_id           NUMBER(18),
    name                VARCHAR2(200),
    FOREIGN KEY (parent_id) REFERENCES test_domain.test_root_hierarchical (id)
);

CREATE TABLE test_domain.test_root_hierarchical_backref
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    parent_id           NUMBER(18),
    name                VARCHAR2(200),
    FOREIGN KEY (parent_id) REFERENCES test_domain.test_root_hierarchical_backref (id)
);

CREATE TABLE test_domain.test_root_many_to_many
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_entity_many_to_many_a
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_root_id        NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    FOREIGN KEY (test_root_id) REFERENCES test_domain.test_root_many_to_many (id)
);

CREATE TABLE test_domain.test_entity_many_to_many_b
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_entity_many_to_many_join
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    test_entity_a_id    NUMBER(18) NOT NULL,
    test_entity_b_id    NUMBER(18) NOT NULL,
    FOREIGN KEY (test_entity_a_id) REFERENCES test_domain.test_entity_many_to_many_a (id),
    FOREIGN KEY (test_entity_b_id) REFERENCES test_domain.test_entity_many_to_many_b (id)
);

CREATE TABLE test_domain.test_root_simple_uuid
(
    id                  VARCHAR2(36) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE SEQUENCE test_domain.vo_aggregate_three_level_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_aggregate_three_level
(
    identifikations_nummer                           NUMBER(18) PRIMARY KEY,
    concurrency_version                              NUMBER(18) NOT NULL,
    info                                             VARCHAR2(200),
    my_complex_vo_value_a                            VARCHAR2(200),
    my_complex_vo_value_b_value                      VARCHAR2(200),
    three_level_vo_own_value                         NUMBER(18),
    three_level_vo_level_two_a_level_three_a_text    VARCHAR2(200),
    three_level_vo_level_two_a_level_three_a_another VARCHAR2(200),
    three_level_vo_level_two_a_level_three_b_text    VARCHAR2(200),
    three_level_vo_level_two_a_level_three_b_another VARCHAR2(200),
    three_level_vo_level_two_b_level_three_a_text    VARCHAR2(200),
    three_level_vo_level_two_b_level_three_a_another VARCHAR2(200),
    three_level_vo_level_two_b_level_three_b_text    VARCHAR2(200),
    three_level_vo_level_two_b_level_three_b_another VARCHAR2(200)
);

/*
   1 Bestellung hat 1-n Bestellpositionen
   1 Bestellung hat genau 1 Bestellstatus
   1 Bestellung hat 0-n Kommentare
   1 Bestellung muss genau 1 Lieferadresse besitzen
   1 Bestellung kann mehrere AktionsCodes (Value Object) zugeordnet haben
   Es wird pro Bestellung nur maximal 1 Position pro Artikel erlaubt, d.h.
   es darf keine 2 Positionen in einer Bestellung geben, bei welcher der gleiche Artikel gewählt wurde

   Der Artikel aus einer Bestellposition verweist auf ein anderes AggregateRoot

   Der Kunde mit Kundennummer ist Teil eines anderen Bounded Context
 */

CREATE SEQUENCE test_domain.lieferadresse_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.lieferadresse
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200) NOT NULL,
    strasse             VARCHAR2(200) NOT NULL,
    postleitzahl        VARCHAR2(10) NOT NULL,
    ort                 VARCHAR2(200) NOT NULL
);

CREATE SEQUENCE test_domain.bestellung_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestellung
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    prioritaet          NUMBER(1) NOT NULL,
    kunden_nummer       VARCHAR2(20) NOT NULL,
    lieferadresse_id    NUMBER(18) NOT NULL,
    FOREIGN KEY (lieferadresse_id) REFERENCES test_domain.lieferadresse (id)
);

CREATE TABLE test_domain.aktions_code
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(10) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.bestellung (id)
);

CREATE SEQUENCE test_domain.aktions_code_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE SEQUENCE test_domain.bestell_position_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestell_position
(
    id                   NUMBER(18) PRIMARY KEY,
    concurrency_version  NUMBER(18) NOT NULL,
    bestellung_id        NUMBER(18) NOT NULL,
    artikel_id           NUMBER(18) NOT NULL,
    stueckzahl           NUMBER(10) NOT NULL,
    stueckpreis_betrag   NUMBER(10,2) NOT NULL,
    stueckpreis_waehrung VARCHAR2(3) NOT NULL,
    FOREIGN KEY (bestellung_id) REFERENCES test_domain.bestellung (id)
);

CREATE UNIQUE INDEX test_domain.bestellung_artikel_unique ON test_domain.bestell_position (bestellung_id, artikel_id);

CREATE SEQUENCE test_domain.bestell_status_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestell_status
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    bestellung_id       NUMBER(18) NOT NULL,
    status_code         VARCHAR2(20),
    status_aenderung_am TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    FOREIGN KEY (bestellung_id) REFERENCES test_domain.bestellung (id)
);

CREATE SEQUENCE test_domain.bestell_kommentar_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestell_kommentar
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    bestellung_id       NUMBER(18) NOT NULL,
    kommentar_am        TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    kommentar_text      VARCHAR2(1000),
    FOREIGN KEY (bestellung_id) REFERENCES test_domain.bestellung (id)
);

/*
   1 BestellungBv3 hat 1-n BestellpositionenBv3
   1 BestellungBv3 hat genau 1 BestellstatusBv3
   1 BestellungBv3 hat 0-n KommentareBv3
   1 BestellungBv3 muss genau 1 LieferadresseBv3 besitzen
   1 BestellungBv3 kann mehrere AktionsCodesBv3 (Value Object) zugeordnet haben
   Es wird pro Bestellung nur maximal 1 Position pro Artikel erlaubt, d.h.
   es darf keine 2 Positionen in einer Bestellung geben, bei welcher der gleiche Artikel gewählt wurde

   Der Artikel aus einer Bestellposition verweist auf ein anderes AggregateRoot

   Der Kunde mit Kundennummer ist Teil eines anderen Bounded Context
 */

CREATE SEQUENCE test_domain.lieferadresse_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.lieferadresse_bv3
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200) NOT NULL,
    strasse             VARCHAR2(200) NOT NULL,
    postleitzahl        VARCHAR2(10) NOT NULL,
    ort                 VARCHAR2(200) NOT NULL
);

CREATE SEQUENCE test_domain.bestellung_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestellung_bv3
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    prioritaet          NUMBER(1) NOT NULL,
    kunden_nummer       VARCHAR2(20) NOT NULL,
    lieferadresse_id    NUMBER(18) NOT NULL,
    FOREIGN KEY (lieferadresse_id) REFERENCES test_domain.lieferadresse_bv3 (id)
);

CREATE TABLE test_domain.aktions_code_bv3
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(10) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.bestellung_bv3 (id)
);

CREATE SEQUENCE test_domain.aktions_code_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE SEQUENCE test_domain.bestell_position_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestell_position_bv3
(
    id                   NUMBER(18) PRIMARY KEY,
    concurrency_version  NUMBER(18) NOT NULL,
    bestellung_id        NUMBER(18) NOT NULL,
    artikel_id           NUMBER(18) NOT NULL,
    stueckzahl           NUMBER(10) NOT NULL,
    stueckpreis_betrag   NUMBER(10,2) NOT NULL,
    stueckpreis_waehrung VARCHAR2(3) NOT NULL,
    FOREIGN KEY (bestellung_id) REFERENCES test_domain.bestellung_bv3 (id)
);

CREATE UNIQUE INDEX test_domain.bestellung_artikel_bv3_unique ON test_domain.bestell_position_bv3 (bestellung_id, artikel_id);

CREATE SEQUENCE test_domain.bestell_status_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestell_status_bv3
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    bestellung_id       NUMBER(18) NOT NULL,
    status_code         VARCHAR2(20),
    status_aenderung_am TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    FOREIGN KEY (bestellung_id) REFERENCES test_domain.bestellung_bv3 (id)
);

CREATE SEQUENCE test_domain.bestell_kommentar_id_bv3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bestell_kommentar_bv3
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    bestellung_id       NUMBER(18) NOT NULL,
    kommentar_am        TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    kommentar_text      VARCHAR2(1000),
    FOREIGN KEY (bestellung_id) REFERENCES test_domain.bestellung_bv3 (id)
);

/*
Value Objects case
*/

CREATE TABLE test_domain.vo_aggregate_root
(
    id                          NUMBER(18) PRIMARY KEY,
    concurrency_version         NUMBER(18) NOT NULL,
    text                        VARCHAR2(20) NULL,
    my_simple_vo_value          VARCHAR2(20) NOT NULL,
    my_complex_vo_value_a       VARCHAR2(20) NULL,
    my_complex_vo_value_b_value VARCHAR2(20) NULL,
    vo_identity_ref_value       VARCHAR2(20) NULL,
    vo_identity_ref_id_ref      NUMBER(18) NULL
);

CREATE TABLE test_domain.vo_entity
(
    id                          NUMBER(18) PRIMARY KEY,
    root_id                     NUMBER(18) NOT NULL,
    concurrency_version         NUMBER(18) NOT NULL,
    text                        VARCHAR2(20) NULL,
    my_complex_vo_value_a       VARCHAR2(20) NULL,
    my_complex_vo_value_b_value VARCHAR2(20) NULL,
    FOREIGN KEY (root_id) REFERENCES test_domain.vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.simple_vo_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.simple_vo_one_to_many
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.simple_vo_one_to_many_2_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.simple_vo_one_to_many_2
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.simple_vo_one_to_many_3_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.simple_vo_one_to_many_3
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.simple_vo_one_to_many_2 (id)
);

CREATE SEQUENCE test_domain.vo_one_to_many_entity_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_one_to_many_entity
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_entity (id)
);

CREATE SEQUENCE test_domain.vo_one_to_many_entity_2_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_one_to_many_entity_2
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_one_to_many_entity (id)
);

/*
Auto Mapping Value Objects case
*/

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root
(
    id                          NUMBER(18) PRIMARY KEY,
    concurrency_version         NUMBER(18) NOT NULL,
    text                        VARCHAR2(20) NULL,
    my_simple_vo_value          VARCHAR2(20) NOT NULL,
    my_complex_vo_value_a       VARCHAR2(20) NULL,
    my_complex_vo_value_b_value VARCHAR2(20) NULL,
    vo_identity_ref_value       VARCHAR2(20) NULL,
    vo_identity_ref_id_ref      NUMBER(18) NULL
);

CREATE TABLE test_domain.auto_mapped_vo_entity
(
    id                          NUMBER(18) PRIMARY KEY,
    root_id                     NUMBER(18) NOT NULL,
    concurrency_version         NUMBER(18) NOT NULL,
    text                        VARCHAR2(20) NULL,
    my_complex_vo_value_a       VARCHAR2(20) NULL,
    my_complex_vo_value_b_value VARCHAR2(20) NULL,
    FOREIGN KEY (root_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2_one_to_many3_set_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2_one_to_many3_set
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2 (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_entity_value_objects_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_entity_value_objects_one_to_many
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_entity (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_entity_value_objects_one_to_many_one_to_many_set_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.auto_mapped_vo_entity_value_objects_one_to_many_one_to_many_set
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_entity_value_objects_one_to_many (id)
);


/*
 Configuration
 */

CREATE TABLE test_domain.configuration
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL
);

CREATE TABLE test_domain.global_configuration_table_entry
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    configuration_id    NUMBER(18) NOT NULL,
    x                   NUMBER(9),
    y                   NUMBER(9),
    FOREIGN KEY (configuration_id) REFERENCES test_domain.configuration (id)
);

CREATE TABLE test_domain.another_configuration
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL
);

CREATE TABLE test_domain.tangible_configuration_table_entry
(
    id                       NUMBER(18) PRIMARY KEY,
    concurrency_version      NUMBER(18) NOT NULL,
    another_configuration_id NUMBER(18) NOT NULL,
    x                        NUMBER(9),
    y                        NUMBER(9),
    FOREIGN KEY (another_configuration_id) REFERENCES test_domain.another_configuration (id)
);


/*
 Optional Case
 */
CREATE TABLE test_domain.ref_agg
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    mandatory_text      VARCHAR2(20) NOT NULL,
    optional_text       VARCHAR2(20) NULL
);

CREATE SEQUENCE test_domain.optional_entity_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.optional_entity
(
    id                                                                 NUMBER(18) PRIMARY KEY,
    concurrency_version                                                NUMBER(18) NOT NULL,
    mandatory_text                                                     VARCHAR2(20) NOT NULL,
    optional_text                                                      VARCHAR2(20) NULL,
    mandatory_simple_value_object_value                                VARCHAR2(20) NOT NULL,
    optional_simple_value_object_value                                 VARCHAR2(20) NULL,
    mandatory_complex_value_object_mandatory_text                      VARCHAR2(20) NOT NULL,
    mandatory_complex_value_object_optional_text                       VARCHAR2(20) NULL,
    mandatory_complex_value_object_mandatory_simple_value_object_value VARCHAR2(20) NOT NULL,
    mandatory_complex_value_object_optional_simple_value_object_value  VARCHAR2(20) NULL,
    mandatory_complex_value_object_optional_long                       NUMBER(18) NULL,
    optional_complex_value_object_mandatory_text                       VARCHAR2(20) NULL,
    optional_complex_value_object_optional_text                        VARCHAR2(20) NULL,
    optional_complex_value_object_mandatory_simple_value_object_value  VARCHAR2(20) NULL,
    optional_complex_value_object_optional_simple_value_object_value   VARCHAR2(20) NULL,
    optional_complex_value_object_optional_long                        NUMBER(18) NULL
);

CREATE SEQUENCE test_domain.optional_aggregate_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.optional_aggregate
(
    id                                                                 NUMBER(18) PRIMARY KEY,
    concurrency_version                                                NUMBER(18) NOT NULL,
    mandatory_text                                                     VARCHAR2(20) NOT NULL,
    optional_text                                                      VARCHAR2(20) NULL,
    optional_long                                                      NUMBER(18) NULL,
    mandatory_simple_value_object_value                                VARCHAR2(20) NOT NULL,
    optional_simple_value_object_value                                 VARCHAR2(20) NULL,
    mandatory_complex_value_object_mandatory_text                      VARCHAR2(20) NOT NULL,
    mandatory_complex_value_object_optional_text                       VARCHAR2(20) NULL,
    mandatory_complex_value_object_mandatory_simple_value_object_value VARCHAR2(20) NOT NULL,
    mandatory_complex_value_object_optional_simple_value_object_value  VARCHAR2(20) NULL,
    mandatory_complex_value_object_optional_long                       NUMBER(18) NULL,
    optional_entity_id                                                 NUMBER(18) NULL,
    optional_ref_id                                                    NUMBER(18) NULL,
    ref_value_object_mandatory_text                                    VARCHAR2(20) NOT NULL,
    ref_value_object_optional_ref                                      NUMBER(18) NULL,
    optional_complex_value_object_mandatory_text                       VARCHAR2(20) NULL,
    optional_complex_value_object_optional_text                        VARCHAR2(20) NULL,
    optional_complex_value_object_mandatory_simple_value_object_value  VARCHAR2(20) NULL,
    optional_complex_value_object_optional_simple_value_object_value   VARCHAR2(20) NULL,
    optional_complex_value_object_optional_long                        NUMBER(18) NULL,
    FOREIGN KEY (optional_entity_id) REFERENCES test_domain.optional_entity (id),
    FOREIGN KEY (optional_ref_id) REFERENCES test_domain.ref_agg (id),
    FOREIGN KEY (ref_value_object_optional_ref) REFERENCES test_domain.ref_agg (id)
);

CREATE TABLE test_domain.optional_entity_complex_value_object_list
(
    id                                  NUMBER(18) PRIMARY KEY,
    container_id                        NUMBER(18) NOT NULL,
    mandatory_text                      VARCHAR2(20) NOT NULL,
    optional_text                       VARCHAR2(20) NULL,
    mandatory_simple_value_object_value VARCHAR2(20) NOT NULL,
    optional_simple_value_object_value  VARCHAR2(20) NULL,
    optional_long                       NUMBER(18) NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.optional_entity (id)
);

CREATE SEQUENCE test_domain.optional_entity_complex_value_object_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.optional_aggregate_ref_value_object_list
(
    id             NUMBER(18) PRIMARY KEY,
    container_id   NUMBER(18) NOT NULL,
    mandatory_text VARCHAR2(20) NOT NULL,
    optional_ref   NUMBER(18) NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.optional_aggregate (id),
    FOREIGN KEY (optional_ref) REFERENCES test_domain.ref_agg (id)
);

CREATE SEQUENCE test_domain.optional_aggregate_ref_value_object_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;


CREATE TABLE test_domain.test_entity
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL
);

CREATE TABLE test_domain.validated_aggregate_root
(
    id                  NUMBER(18) PRIMARY KEY,
    text                VARCHAR2(100) NOT NULL,
    optional_text       VARCHAR2(10),
    concurrency_version NUMBER(18) NOT NULL
);

CREATE TABLE test_domain.validated_aggregate_root2
(
    id                  NUMBER(18) PRIMARY KEY,
    text                VARCHAR2(100) NOT NULL,
    optional_text       VARCHAR2(10),
    concurrency_version NUMBER(18) NOT NULL
);

CREATE TABLE test_domain.test_root_temporal
(
    id                  NUMBER(18) PRIMARY KEY,
    local_date          DATE,
    local_time          TIMESTAMP(6) WITHOUT TIME ZONE,
    local_date_time     TIMESTAMP(6) WITHOUT TIME ZONE,
    my_year             NUMBER(6),
    year_month          NUMBER(6),
    month_day           NUMBER(4),
    zoned_date_time     TIMESTAMP(6) WITH TIME ZONE,
    my_calendar         TIMESTAMP(6) WITH TIME ZONE,
    my_date             TIMESTAMP(6) WITH TIME ZONE,
    my_instant          TIMESTAMP(6) WITH TIME ZONE,
    offset_date_time    TIMESTAMP(6) WITH TIME ZONE,
    offset_time         TIMESTAMP(6) WITH TIME ZONE,
    concurrency_version NUMBER(18) NOT NULL
);

CREATE SEQUENCE test_domain.vehicle_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vehicle
(
    id                  NUMBER(18) PRIMARY KEY,
    length_cm           NUMBER(6),
    brand               varchar2(100),
    type                varchar2(100),
    gears               NUMBER(10),
    concurrency_version NUMBER(18) NOT NULL
);

CREATE SEQUENCE test_domain.vehicle_extended_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vehicle_extended
(
    id                  NUMBER(18) PRIMARY KEY,
    length_cm           NUMBER(6),
    brand               varchar2(100),
    type                varchar2(100),
    gears               NUMBER(10),
    concurrency_version NUMBER(18) NOT NULL
);

CREATE SEQUENCE test_domain.engine_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.engine
(
    id                  NUMBER(18) PRIMARY KEY,
    vehicle_extended_id NUMBER(18) NOT NULL,
    ps                  NUMBER(6),
    type                varchar2(100),
    concurrency_version NUMBER(18) NOT NULL,
    FOREIGN KEY (vehicle_extended_id) REFERENCES test_domain.vehicle_extended (id)
);

CREATE SEQUENCE test_domain.bike_with_components_bike_components_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.bike_with_components_bike_components
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    type         varchar2(100),
    manufacturer varchar2(100),
    FOREIGN KEY (container_id) REFERENCES test_domain.vehicle_extended (id)
);


CREATE SEQUENCE test_domain.record_test_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.record_test
(
    id                  NUMBER(18) PRIMARY KEY,
    my_value            VARCHAR2(100),
    my_vo_value1        VARCHAR2(100),
    my_vo_value2        NUMBER(18),
    concurrency_version NUMBER(18) NOT NULL
);

CREATE SEQUENCE test_domain.record_test_my_vo_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.record_test_my_vo_list
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value1       VARCHAR2(100),
    value2       NUMBER(18),
    FOREIGN KEY (container_id) REFERENCES test_domain.record_test (id)
);

CREATE SEQUENCE test_domain.record_test_my_vo_set_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.record_test_my_vo_set
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value1       VARCHAR2(100),
    value2       NUMBER(18),
    FOREIGN KEY (container_id) REFERENCES test_domain.record_test (id)
);


CREATE TABLE test_domain.tree_root
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(100)
);

CREATE TABLE test_domain.tree_node
(
    id                  NUMBER(18) PRIMARY KEY,
    node_name           VARCHAR2(100),
    parent_node_id      NUMBER(18),
    root_id             NUMBER(18),
    concurrency_version NUMBER(18) NOT NULL,
    FOREIGN KEY (parent_node_id) REFERENCES test_domain.tree_node (id),
    FOREIGN KEY (root_id) REFERENCES test_domain.tree_root (id)
);

CREATE TABLE test_domain.test_root_one_to_one_vo_dedicated
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_root_one_to_one_vo_dedicated_vo
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    name         VARCHAR2(100),
    FOREIGN KEY (container_id) REFERENCES test_domain.test_root_one_to_one_vo_dedicated (id)
);

CREATE SEQUENCE test_domain.test_root_one_to_one_vo_dedicated_vo_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.concrete_root
(
    my_id               NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);


CREATE TABLE test_domain.vo_aggregate_two_level
(
    id                    NUMBER(18) PRIMARY KEY,
    concurrency_version   NUMBER(18) NOT NULL,
    level_one_first_text  VARCHAR2(200),
    level_one_second_text VARCHAR2(200),
    level_one_third_bool  NUMBER(1)
);


CREATE TABLE test_domain.vo_aggregate_primitive
(
    id                          NUMBER(18) PRIMARY KEY,
    concurrency_version         NUMBER(18) NOT NULL,
    simple_val                  NUMBER(18),
    complex_val                 NUMBER(18),
    complex_num                 NUMBER(18),
    nested_simple_val           NUMBER(18),
    nested_complex_val          NUMBER(18),
    nested_complex_num          NUMBER(18),
    optional_simple_val         NUMBER(18),
    optional_complex_val        NUMBER(18),
    optional_complex_num        NUMBER(18),
    optional_nested_simple_val  NUMBER(18),
    optional_nested_complex_val NUMBER(18),
    optional_nested_complex_num NUMBER(18)
);

CREATE TABLE test_domain.vo_aggregate_primitive_record_mapped_simple
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    val          NUMBER(18),
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_primitive (id)
);

CREATE TABLE test_domain.vo_aggregate_primitive_record_mapped_complex
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    val          NUMBER(18),
    num          NUMBER(18),
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_primitive (id)
);

CREATE TABLE test_domain.vo_aggregate_primitive_record_mapped_nested
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    simple_val   NUMBER(18),
    complex_val  NUMBER(18),
    complex_num  NUMBER(18),
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_primitive (id)
);

CREATE SEQUENCE test_domain.vo_aggregate_primitive_record_mapped_simple_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_primitive_record_mapped_complex_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_primitive_record_mapped_nested_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.vo_aggregate_nested
(
    id                                   NUMBER(18) PRIMARY KEY,
    concurrency_version                  NUMBER(18) NOT NULL,
    nested_enum_single_valued_enum_value CHAR(1),
    nested_simple_vo_nested_val          NUMBER(18),
    nested_id_id_ref                     NUMBER(18)
);

CREATE TABLE test_domain.vo_aggregate_nested_nested_enum_single_valued_list
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    enum_value   CHAR(1),
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_nested (id)
);

CREATE TABLE test_domain.vo_aggregate_nested_nested_simple_vo_list
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    nested_val   NUMBER(18),
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_nested (id)
);

CREATE TABLE test_domain.vo_aggregate_nested_nested_id_list
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    id_ref       NUMBER(18),
    FOREIGN KEY (container_id) REFERENCES test_domain.vo_aggregate_nested (id)
);

CREATE SEQUENCE test_domain.vo_aggregate_nested_nested_enum_single_valued_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_nested_nested_simple_vo_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.vo_aggregate_nested_nested_id_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

CREATE TABLE test_domain.test_root_simple_ignoring
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200),
    ignored_column      VARCHAR2(200)
);

/*
CREATE TABLE test_domain.root_id_enum_list (
                                                    id NUMBER(18) PRIMARY KEY,
                                                    concurrency_version NUMBER(18) NOT NULL,
                                                    name VARCHAR2(200)
);

CREATE TABLE test_domain.root_id_enum_list_enum_list (
                                                                         id NUMBER(18) PRIMARY KEY,
                                                                         container_id NUMBER(18) NOT NULL,
                                                                         value VARCHAR2(20),
                                                                         FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list(id)
);

CREATE TABLE test_domain.root_id_enum_list_id_list (
                                                         id NUMBER(18) PRIMARY KEY,
                                                         container_id NUMBER(18) NOT NULL,
                                                         value NUMBER(18),
                                                         FOREIGN KEY (container_id) REFERENCES test_domain.root_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list (
                                               id NUMBER(18) PRIMARY KEY,
                                               root_id NUMBER(10) NOT NULL,
                                               concurrency_version NUMBER(18) NOT NULL,
                                               FOREIGN KEY (root_id) REFERENCES test_domain.root_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_id_list (
                                                       id NUMBER(18) PRIMARY KEY,
                                                       container_id NUMBER(18) NOT NULL,
                                                       value NUMBER(18),
                                                       FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_enum_list (
                                                         id NUMBER(18) PRIMARY KEY,
                                                         container_id NUMBER(18) NOT NULL,
                                                         name VARCHAR2(200),
                                                         FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_value_with_lists_enums (
                                                           id NUMBER(18) PRIMARY KEY,
                                                           container_id NUMBER(18) NOT NULL,
                                                           name VARCHAR2(200),
                                                           FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);

CREATE TABLE test_domain.entity_id_enum_list_value_with_lists_ids (
                                                                        id NUMBER(18) PRIMARY KEY,
                                                                        container_id NUMBER(18) NOT NULL,
                                                                        value NUMBER(18),
                                                                        FOREIGN KEY (container_id) REFERENCES test_domain.entity_id_enum_list(id)
);
CREATE SEQUENCE test_domain.root_id_enum_list_id_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.root_id_enum_list_enum_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_id_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_enum_list_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_value_with_lists_enums_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE test_domain.entity_id_enum_list_value_with_lists_ids_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;

*/
