package com.scripty.commandmodel.team.editteam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditTeamCommandModel {

    @NotBlank(message = "Team name is required")
    @Size(max = 100, message = "Team name must be 100 characters or less")
    private String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
