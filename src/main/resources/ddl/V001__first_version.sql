CREATE TABLE IF NOT EXISTS day_base
(
  id        BIGSERIAL PRIMARY KEY,
  day       VARCHAR(10) NOT NULL,
  base_code VARCHAR(3)  NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_day_base_code ON day_base (day, base_code);

CREATE TABLE IF NOT EXISTS currency_rate
(
  id          BIGSERIAL PRIMARY KEY,
  day_base_id BIGINT,
  code        VARCHAR(3),
  rate        NUMERIC(40, 20),
  FOREIGN KEY (day_base_id) REFERENCES DAY_BASE (id)
);

