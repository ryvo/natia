INSERT INTO rule (id, name) VALUES (1, 'Rule 1');

INSERT INTO rule_input_article (rule_id, code, description, amount) VALUES
  (1, '00001', 'Input article 1', 5),
  (1, '00002', 'Input article 2', 5);

INSERT INTO rule_output_article (rule_id, code, description, amount) VALUES
  (1, '00001', 'Output article 1', 1),
  (1, '00002', 'Output article 2', 1);