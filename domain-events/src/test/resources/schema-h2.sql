CREATE TABLE IF NOT EXISTS outbox ( id           VARCHAR(36) PRIMARY KEY,
                      domain_event VARCHAR(8000) NOT NULL,
                      inserted     TIMESTAMP NOT NULL,
                      batch_id     VARCHAR(36) NULL,
                      processing_result      VARCHAR(30) NULL,
                      delivery_started     TIMESTAMP NULL
    );
