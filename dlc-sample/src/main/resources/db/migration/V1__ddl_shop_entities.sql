CREATE SCHEMA SHOP_DOMAIN;

CREATE SEQUENCE SHOP_DOMAIN.PRODUCT_ID_SEQ MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;
CREATE SEQUENCE SHOP_DOMAIN.CUSTOMER_ID_SEQ MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;
CREATE SEQUENCE SHOP_DOMAIN.ORDER_ID_SEQ MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;
CREATE SEQUENCE SHOP_DOMAIN.ORDER_ITEM_ID_SEQ MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1111 CACHE 20;

CREATE TABLE SHOP_DOMAIN.PRODUCT
(
    ID                  NUMBER(18) PRIMARY KEY,
    CONCURRENCY_VERSION NUMBER(18)    NOT NULL,
    DESCRIPTION         VARCHAR2(1000)        ,
    NAME                VARCHAR2(200) NOT NULL,
    IMAGE               VARCHAR2(1000)        ,
    PRICE_AMOUNT        NUMBER(10,2)   NOT NULL
);

CREATE TABLE SHOP_DOMAIN.CUSTOMER
(
    ID                          NUMBER(18)    PRIMARY KEY,
    CONCURRENCY_VERSION         NUMBER(18)    NOT NULL,
    USER_NAME                   VARCHAR2(100) NOT NULL,
    ADDRESS_STREET              VARCHAR2(50)  NOT NULL,
    ADDRESS_CITY                VARCHAR2(50)  NOT NULL,
    ADDRESS_STATE               VARCHAR2(50)  NOT NULL,
    ADDRESS_COUNTRY             VARCHAR2(50)  NOT NULL,
    ADDRESS_ZIPCODE             VARCHAR2(10)  NOT NULL,
    CREDIT_CARD_TYPE            VARCHAR2(20)          ,
    CREDIT_CARD_CARD_NUMBER     VARCHAR2(19)          ,
    CREDIT_CARD_CCV_NUMBER      VARCHAR2(4)           ,
    CREDIT_CARD_OWNER_NAME      VARCHAR2(100)         ,
    CREDIT_CARD_EXPIRATION      DATE                  ,
    BLOCKED                     NUMBER(1)     NOT NULL
);

CREATE TABLE "SHOP_DOMAIN"."ORDER"
(
    ID                  NUMBER(18)                     PRIMARY KEY,
    CONCURRENCY_VERSION NUMBER(18)                     NOT NULL,
    CUSTOMER_ID         NUMBER(18)                     NOT NULL,
    CREATION            TIMESTAMP(6) WITH TIME ZONE    NOT NULL,
    STATUS              VARCHAR2(10)                  NOT NULL,
    CONSTRAINT FK_CUSTOMER_CUSTOMER_ID FOREIGN KEY (CUSTOMER_ID) REFERENCES SHOP_DOMAIN.CUSTOMER (ID)
);

CREATE TABLE SHOP_DOMAIN.ORDER_ITEM
(
    ID                    NUMBER(18)   PRIMARY KEY,
    CONCURRENCY_VERSION   NUMBER(18)   NOT NULL,
    ORDER_ID              NUMBER(18)   NOT NULL,
    PRODUCT_ID            NUMBER(18)   NOT NULL,
    PRODUCT_PRICE_AMOUNT  NUMBER(10,2) NOT NULL,
    QUANTITY              NUMBER(18)   NOT NULL,
    CONSTRAINT FK_ODER_ORDER_ID FOREIGN KEY (ORDER_ID) REFERENCES SHOP_DOMAIN."ORDER" (ID),
    CONSTRAINT FK_PRODUCT_PRODUCT_ID FOREIGN KEY (PRODUCT_ID) REFERENCES SHOP_DOMAIN.PRODUCT (ID)
);
