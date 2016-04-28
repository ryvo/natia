CREATE TABLE catalogue (
  id INT IDENTITY,
  name VARCHAR(128) NOT NULL,
  valid_from DATE,
  valid_to DATE,
  creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE article (
  catalogue_id INT NOT NULL,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(128) NOT NULL,
  PRIMARY KEY (catalogue_id, code),
  CONSTRAINT fk__article__catalogue FOREIGN KEY (catalogue_id) REFERENCES catalogue(id) ON DELETE CASCADE ON UPDATE CASCADE
);