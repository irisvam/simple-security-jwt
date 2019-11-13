CREATE SEQUENCE roles_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE SEQUENCE users_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE roles (
	id bigint NOT NULL DEFAULT nextval('roles_id_seq'::regclass),
	name character varying(15),
	CONSTRAINT tb_roles_pkey PRIMARY KEY (id),
	CONSTRAINT uk_roles_name UNIQUE (name)
);

CREATE TABLE users (
	id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
	name character varying(50),
	username character varying(50),
	email character varying(50),
	password character varying(100),
	CONSTRAINT tb_users_pkey PRIMARY KEY (id),
	CONSTRAINT uk_users_email UNIQUE (email),
	CONSTRAINT uk_users_username UNIQUE (username)
);

CREATE TABLE user_roles (
	user_id bigint NOT NULL,
	role_id bigint NOT NULL,
	CONSTRAINT tb_user_roles_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk_role_id FOREIGN KEY (role_id)
      REFERENCES public.roles (id) MATCH SIMPLE,
	CONSTRAINT fk_user_id FOREIGN KEY (user_id)
      REFERENCES public.users (id) MATCH SIMPLE
);

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
