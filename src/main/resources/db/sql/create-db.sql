CREATE TABLE logs(
  id         VARCHAR(30) PRIMARY KEY,
  duration VARCHAR(30),
  type  VARCHAR(50),
  host VARCHAR(50),
  alert BOOLEAN
);
