-- Limpa os dados para evitar conflito de tipos
TRUNCATE TABLE tb_suprofissional CASCADE;

-- Remove FK de profissional para especialidade
ALTER TABLE tb_suprofissional DROP CONSTRAINT fk_tb_suprofissional_especialidade;

-- Drop da coluna antiga e criacao da nova
ALTER TABLE tb_suprofissional DROP COLUMN especialidade_id;
ALTER TABLE tb_suprofissional ADD COLUMN especialidade varchar(255) not null;

-- Remove a tabela de especialidade antiga
DROP TABLE tb_suespecialidade;

-- Altera a tabela de atendimento
ALTER TABLE tb_suatendimento ALTER COLUMN profissional_id DROP NOT NULL;
ALTER TABLE tb_suatendimento ADD COLUMN especialidade varchar(255);
