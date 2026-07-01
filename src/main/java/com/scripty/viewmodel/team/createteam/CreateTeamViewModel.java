package com.scripty.viewmodel.team.createteam;

import com.scripty.commandmodel.team.createteam.CreateTeamCommandModel;

public class CreateTeamViewModel {

    private CreateTeamCommandModel createTeamCommandModel;

    public CreateTeamCommandModel getCreateTeamCommandModel() {
        return createTeamCommandModel;
    }

    public void setCreateTeamCommandModel(CreateTeamCommandModel createTeamCommandModel) {
        this.createTeamCommandModel = createTeamCommandModel;
    }
}
