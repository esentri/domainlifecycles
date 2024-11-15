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
