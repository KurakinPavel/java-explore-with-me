DROP TABLE IF EXISTS PUBLIC.HITS;

CREATE TABLE IF NOT EXISTS PUBLIC.HITS
(
    HIT_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    APP    VARCHAR(255) NOT NULL,
    URI    VARCHAR(255) NOT NULL,
    IP     VARCHAR(50) NOT NULL,
    MOMENT TIMESTAMP WITHOUT TIME ZONE
);
