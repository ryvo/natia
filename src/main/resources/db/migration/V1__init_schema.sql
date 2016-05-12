CREATE TABLE article (
  code VARCHAR(50) NOT NULL,
  description VARCHAR(128) NOT NULL,
  PRIMARY KEY (code)
);

CREATE TABLE rule (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) NOT NULL
);

CREATE TABLE rule_input_article (
  rule_id BIGINT NOT NULL,
  code VARCHAR(50) NOT NULL,
  description VARCHAR(128) NOT NULL,
  amount INT NOT NULL,
  PRIMARY KEY (rule_id, code),
  FOREIGN KEY (rule_id) REFERENCES rule (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE rule_output_article (
  rule_id BIGINT NOT NULL,
  code VARCHAR(50) NOT NULL,
  description VARCHAR(128) NOT NULL,
  amount INT NOT NULL,
  PRIMARY KEY (rule_id, code),
  FOREIGN KEY (rule_id) REFERENCES rule (id) ON DELETE CASCADE ON UPDATE CASCADE
);