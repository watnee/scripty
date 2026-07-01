CREATE TABLE IF NOT EXISTS team (
	id int NOT NULL AUTO_INCREMENT,
	name varchar(100) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS team_member (
	team_id int NOT NULL,
	user_id int NOT NULL,
	PRIMARY KEY (team_id, user_id),
	FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS team_project (
	team_id int NOT NULL,
	project_id int NOT NULL,
	PRIMARY KEY (team_id, project_id),
	FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
	FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);
