package com.scripty.viewmodel.team.editteam;

import com.scripty.commandmodel.team.editteam.EditTeamCommandModel;

public class EditTeamViewModel {

    private int id;
    private EditTeamCommandModel editTeamCommandModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EditTeamCommandModel getEditTeamCommandModel() {
        return editTeamCommandModel;
    }

    public void setEditTeamCommandModel(EditTeamCommandModel editTeamCommandModel) {
        this.editTeamCommandModel = editTeamCommandModel;
    }
}
