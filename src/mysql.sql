CREATE DATABASE FindFiles;

CREATE TABLE FindFiles.FindResult (
  id      INT          NOT NULL AUTO_INCREMENT,
  keyword VARCHAR(255) NOT NULL COMMENT 'Ключевое слово',
  path    VARCHAR(255) NOT NULL COMMENT 'Имя файла',
  line    INT          NOT NULL COMMENT 'Строка',
  PRIMARY KEY (id)
) ENGINE = INNODB;