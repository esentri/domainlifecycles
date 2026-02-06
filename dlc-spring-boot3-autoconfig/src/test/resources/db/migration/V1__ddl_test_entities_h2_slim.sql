CREATE SCHEMA test_domain;

CREATE SEQUENCE test_domain.test_root_simple_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;

CREATE TABLE test_domain.test_root_simple
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL,
    name                VARCHAR2(200)
);

CREATE SEQUENCE test_domain.an_aggregate_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;

CREATE TABLE test_domain.an_aggregate
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL
);

CREATE SEQUENCE test_domain.an_aggregate_domain_event_id_seq MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;

CREATE TABLE test_domain.an_aggregate_domain_event
(
    id                  NUMBER(18) PRIMARY KEY,
    concurrency_version NUMBER(18) NOT NULL
);