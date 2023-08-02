CREATE TABLE members (
  mbr_id SERIAL PRIMARY KEY,
  mbr_name VARCHAR(255) NOT NULL,
  mbr_cpf VARCHAR(11) NOT NULL UNIQUE,
  mbr_dt_association DATE NOT NULL,
  mbr_card_code VARCHAR(20) UNIQUE
);
