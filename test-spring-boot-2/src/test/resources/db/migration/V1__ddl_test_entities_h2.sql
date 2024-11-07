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

CREATE SCHEMA test_domain;

CREATE SEQUENCE test_domain.test_root_simple_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;

CREATE TABLE test_domain.test_root_simple
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE TABLE test_domain.test_root_simple_uuid
(
    id                  VARCHAR2(36) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
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

CREATE SEQUENCE test_domain.lieferadresse_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.lieferadresse
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200) NOT NULL,
    strasse             VARCHAR2(200) NOT NULL,
    postleitzahl        VARCHAR2(10) NOT NULL,
    ort                 VARCHAR2(200) NOT NULL
);

CREATE SEQUENCE test_domain.bestellung_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.bestellung
(
    id                    NUMBER(18) PRIMARY KEY,
    concurrency_version   NUMBER(18) NOT NULL,
    prioritaet            NUMBER(1) NOT NULL,
    kunden_nummer         VARCHAR2(20) NOT NULL,
    gesamt_preis_betrag   NUMBER(10,2) NOT NULL,
    gesamt_preis_waehrung VARCHAR2(3) NOT NULL,
    lieferadresse_id      NUMBER(18) NOT NULL,
    FOREIGN KEY (lieferadresse_id) REFERENCES test_domain.lieferadresse (id)
);

CREATE TABLE test_domain.aktions_code
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(10) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.bestellung (id)
);

CREATE SEQUENCE test_domain.aktions_code_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE SEQUENCE test_domain.bestell_position_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

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

CREATE SEQUENCE test_domain.bestell_status_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.bestell_status
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    bestellung_id       NUMBER(18) NOT NULL,
    status_code         VARCHAR2(20),
    status_aenderung_am TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    FOREIGN KEY (bestellung_id) REFERENCES test_domain.bestellung (id)
);

CREATE SEQUENCE test_domain.bestell_kommentar_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

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

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2_one_to_many3_set_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2_one_to_many3_set
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_aggregate_root_value_objects_one_to_many2 (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_entity_value_objects_one_to_many_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.auto_mapped_vo_entity_value_objects_one_to_many
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_entity (id)
);

CREATE SEQUENCE test_domain.auto_mapped_vo_entity_value_objects_one_to_many_one_to_many_set_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.auto_mapped_vo_entity_value_objects_one_to_many_one_to_many_set
(
    id           NUMBER(18) PRIMARY KEY,
    container_id NUMBER(18) NOT NULL,
    value        VARCHAR2(20) NOT NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.auto_mapped_vo_entity_value_objects_one_to_many (id)
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

CREATE SEQUENCE test_domain.optional_entity_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

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

CREATE SEQUENCE test_domain.optional_aggregate_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

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

CREATE SEQUENCE test_domain.optional_entity_complex_value_object_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;

CREATE TABLE test_domain.optional_aggregate_ref_value_object_list
(
    id             NUMBER(18) PRIMARY KEY,
    container_id   NUMBER(18) NOT NULL,
    mandatory_text VARCHAR2(20) NOT NULL,
    optional_ref   NUMBER(18) NULL,
    FOREIGN KEY (container_id) REFERENCES test_domain.optional_aggregate (id),
    FOREIGN KEY (optional_ref) REFERENCES test_domain.ref_agg (id)
);

CREATE SEQUENCE test_domain.optional_aggregate_ref_value_object_list_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20;


CREATE TABLE test_domain.validated_aggregate_root
(
    id                  NUMBER(18) PRIMARY KEY,
    text                VARCHAR2(100) NOT NULL,
    optional_text       VARCHAR2(10),
    concurrency_version NUMBER(18) NOT NULL
);


