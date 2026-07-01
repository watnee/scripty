CREATE TABLE IF NOT EXISTS team_setting (
    id int NOT NULL AUTO_INCREMENT,
    team_name varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO team_setting (team_name) VALUES ('My Team');
