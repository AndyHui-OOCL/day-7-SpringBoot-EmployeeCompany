CREATE TABLE t_employee
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255),
    age        INT,
    salary     FLOAT,
    status     BOOLEAN,
    company_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES t_company (id)
);