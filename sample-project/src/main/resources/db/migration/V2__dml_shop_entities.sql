INSERT INTO SHOP_DOMAIN.CUSTOMER
(ID,
 CONCURRENCY_VERSION,
 USER_NAME,
 ADDRESS_STREET,
 ADDRESS_CITY,
 ADDRESS_STATE,
 ADDRESS_COUNTRY,
 ADDRESS_ZIPCODE,
 CREDIT_CARD_TYPE,
 CREDIT_CARD_CARD_NUMBER,
 CREDIT_CARD_CCV_NUMBER,
 CREDIT_CARD_OWNER_NAME,
 CREDIT_CARD_EXPIRATION,
 BLOCKED)
VALUES (1,
        0,
        'john_doe@whitehouse.com',
        '1600 Pennsylvania Avenue',
        'Washington, D.C.',
        'Washington, D.C.',
        'USA',
        '20500',
        'AMEX',
        '123456789123456',
        '123',
        'John Doe',
        CURRENT_DATE,
        0);

INSERT INTO SHOP_DOMAIN.PRODUCT(ID,
                                CONCURRENCY_VERSION,
                                DESCRIPTION,
                                NAME,
                                IMAGE,
                                PRICE_AMOUNT)
VALUES (1,
        0,
        NULL,
        'Things',
        NULL,
        99.99);

INSERT INTO SHOP_DOMAIN.PRODUCT(ID,
                                CONCURRENCY_VERSION,
                                DESCRIPTION,
                                NAME,
                                IMAGE,
                                PRICE_AMOUNT)
VALUES (2,
        0,
        'Yummi',
        'Bubble Gum',
        NULL,
        9.99);

INSERT INTO SHOP_DOMAIN.PRODUCT(ID,
                                CONCURRENCY_VERSION,
                                DESCRIPTION,
                                NAME,
                                IMAGE,
                                PRICE_AMOUNT)
VALUES (3,
        0,
        'Only 2 available!!!',
        'Herbs',
        NULL,
        99999.01);

INSERT INTO SHOP_DOMAIN."ORDER"(ID,
                                CONCURRENCY_VERSION,
                                CUSTOMER_ID,
                                CREATION,
                                STATUS)
VALUES (1,
        0,
        1,
        CURRENT_DATE,
        'SHIPPED');

INSERT INTO SHOP_DOMAIN.ORDER_ITEM(ID,
                                   CONCURRENCY_VERSION,
                                   ORDER_ID,
                                   PRODUCT_ID,
                                   PRODUCT_PRICE_AMOUNT,
                                   QUANTITY)
VALUES (1,
        0,
        1,
        1,
        88.88,
        3);

COMMIT;
