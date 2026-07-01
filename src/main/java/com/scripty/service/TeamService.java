package com.scripty.service;

import com.scripty.commandmodel.team.createteam.CreateTeamCommandModel;
import com.scripty.commandmodel.team.editteam.EditTeamCommandModel;
import com.scripty.dto.Team;
import com.scripty.viewmodel.team.createteam.CreateTeamViewModel;
import com.scripty.viewmodel.team.editteam.EditTeamViewModel;
import com.scripty.viewmodel.team.teamlist.TeamListViewModel;
import com.scripty.viewmodel.team.teamshow.TeamShowViewModel;

public interface TeamService {

    Team read(Integer id);

    TeamListViewModel getTeamListViewModel();
    TeamShowViewModel getTeamShowViewModel(Integer id);

    CreateTeamViewModel getCreateTeamViewModel();
    EditTeamViewModel getEditTeamViewModel(Integer id);

    Team saveCreateTeamCommandModel(CreateTeamCommandModel commandModel);
    Team saveEditTeamCommandModel(EditTeamCommandModel commandModel);

    Team deleteTeam(Integer id);

    void addMember(Integer teamId, Integer userId);
    void removeMember(Integer teamId, Integer userId);

    void addProject(Integer teamId, Integer projectId);
    void removeProject(Integer teamId, Integer projectId);
}
