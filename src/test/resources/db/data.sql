SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE rule;
TRUNCATE TABLE rule_input_article;
TRUNCATE TABLE rule_output_article;

SET REFERENTIAL_INTEGRITY TRUE;



INSERT INTO rule (id, name, rank) VALUES (1, 'Default rule', 0);

INSERT INTO rule_input_article (rule_id, code, description, amount, rank) VALUES
  (1, '00001', 'Default input article 1', 5, 0),
  (1, '00002', 'Default input article 2', 5, 1);

INSERT INTO rule_output_article (rule_id, code, description, amount, rank) VALUES
  (1, '00001', 'Default output article 1', 1, 0),
  (1, '00002', 'Default output article 2', 1, 1);