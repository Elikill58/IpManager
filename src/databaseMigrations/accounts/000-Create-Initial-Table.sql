CREATE TABLE IF NOT EXISTS ipmanager_accounts
(
    id CHAR(36) NOT NULL PRIMARY KEY,
    playername VARCHAR(16),
    ip VARCHAR(16) NOT NULL,
    proxy VARCHAR(16) NOT NULL,
    fai VARCHAR(256) NOT NULL,
    connection LONGTEXT,
    creation_time TIMESTAMP NOT NULL DEFAULT NOW()
);
