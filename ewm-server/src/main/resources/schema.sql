DROP TABLE IF EXISTS PUBLIC.PARTICIPATION_REQUESTS;
DROP TABLE IF EXISTS PUBLIC.EVENT_COMPILATIONS;
DROP TABLE IF EXISTS PUBLIC.MARKS;
DROP TABLE IF EXISTS PUBLIC.EVENTS;
DROP TABLE IF EXISTS PUBLIC.COMPILATIONS;
DROP TABLE IF EXISTS PUBLIC.CATEGORIES;
DROP TABLE IF EXISTS PUBLIC.USERS;
DROP TABLE IF EXISTS PUBLIC.LOCATIONS;

CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    USER_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME    VARCHAR(250) NOT NULL,
    EMAIL   VARCHAR(254) NOT NULL,
    RATING  INTEGER NOT NULL,
    CONSTRAINT UQ_USER_EMAIL  UNIQUE (EMAIL)
);

CREATE TABLE IF NOT EXISTS PUBLIC.CATEGORIES
(
    CATEGORY_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME        VARCHAR(50) NOT NULL,
    CONSTRAINT UQ_NAME UNIQUE (NAME)
);

CREATE TABLE IF NOT EXISTS PUBLIC.COMPILATIONS
(
    COMPILATION_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    TITLE          VARCHAR(50) NOT NULL,
    PINNED         INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.LOCATIONS
(
    LOCATION_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    LAT         DOUBLE PRECISION NOT NULL,
    LON         DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.EVENTS
(
    EVENT_ID           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    ANNOTATION         VARCHAR(2000) NOT NULL,
    CATEGORY_ID        INTEGER REFERENCES PUBLIC.CATEGORIES (CATEGORY_ID) ON DELETE RESTRICT,
    CREATED_ON         TIMESTAMP WITHOUT TIME ZONE,
    DESCRIPTION        VARCHAR(7000) NOT NULL,
    EVENT_DATE         TIMESTAMP WITHOUT TIME ZONE,
    INITIATOR_ID       INTEGER REFERENCES PUBLIC.USERS (USER_ID) ON DELETE CASCADE,
    LOCATION_ID        INTEGER REFERENCES PUBLIC.LOCATIONS (LOCATION_ID) ON DELETE CASCADE,
    PAID               INTEGER NOT NULL,
    PARTICIPANT_LIMIT  INTEGER NOT NULL,
    CONFIRMED_REQUESTS INTEGER NOT NULL,
    PUBLISHED_ON       TIMESTAMP WITHOUT TIME ZONE,
    REQUEST_MODERATION INTEGER NOT NULL,
    STATE              INTEGER NOT NULL,
    TITLE              VARCHAR(120) NOT NULL,
    RATING             INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.PARTICIPATION_REQUESTS
(
    REQUEST_ID   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    CREATED      TIMESTAMP WITHOUT TIME ZONE,
    EVENT_ID     INTEGER REFERENCES PUBLIC.EVENTS (EVENT_ID) ON DELETE CASCADE,
    REQUESTER_ID INTEGER REFERENCES PUBLIC.USERS (USER_ID) ON DELETE CASCADE,
    STATUS       INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.MARKS
(
    MARK_ID      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EVALUATOR_ID INTEGER REFERENCES PUBLIC.USERS (USER_ID) ON DELETE CASCADE,
    EVENT_ID     INTEGER REFERENCES PUBLIC.EVENTS (EVENT_ID) ON DELETE CASCADE,
    SCORE        INTEGER NOT NULL,
    CONSTRAINT uq_evaluator_event UNIQUE (EVALUATOR_ID, EVENT_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.EVENT_COMPILATIONS
(
    PAIR_ID        INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    COMPILATION_ID INTEGER REFERENCES PUBLIC.COMPILATIONS (COMPILATION_ID) ON DELETE CASCADE,
    EVENT_ID       INTEGER REFERENCES PUBLIC.EVENTS (EVENT_ID) ON DELETE CASCADE,
    CONSTRAINT uq_event_compilation UNIQUE (EVENT_ID, COMPILATION_ID)
);