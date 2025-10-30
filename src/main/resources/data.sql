INSERT INTO users (id, name, email, password_hash, token, is_active, created, modified, last_login)
VALUES (
  '00000000-0000-0000-0000-000000000001',
  'Juan Perez',
  'juan@nisum.com',
  'aaa$$bbb$',
  '0000000aaaa$$$$$$11111',
  TRUE,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

INSERT INTO phones (id, number, citycode, contrycode, user_id)
VALUES (
  '00000000-0000-0000-0000-000000000101',
  '1234567',
  '1',
  '51',
  '00000000-0000-0000-0000-000000000001'
);



