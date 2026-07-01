package com.scripty.service;

import com.scripty.commandmodel.team.createteam.CreateTeamCommandModel;
import com.scripty.commandmodel.team.editteam.EditTeamCommandModel;
import com.scripty.dto.Project;
import com.scripty.dto.Team;
import com.scripty.dto.User;
import com.scripty.repository.ProjectRepository;
import com.scripty.repository.TeamRepository;
import com.scripty.repository.UserRepository;
import com.scripty.viewmodel.project.projectlist.ProjectViewModel;
import com.scripty.viewmodel.team.createteam.CreateTeamViewModel;
import com.scripty.viewmodel.team.editteam.EditTeamViewModel;
import com.scripty.viewmodel.team.teamlist.TeamListViewModel;
import com.scripty.viewmodel.team.teamlist.TeamViewModel;
import com.scripty.viewmodel.team.teamshow.TeamShowViewModel;
import com.scripty.viewmodel.user.userlist.UserViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           UserRepository userRepository,
                           ProjectRepository projectRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Team read(Integer id) {
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamListViewModel getTeamListViewModel() {
        TeamListViewModel vm = new TeamListViewModel();
        List<Team> teams = teamRepository.findAllByOrderByNameAsc();
        List<TeamViewModel> teamViewModels = new ArrayList<>();
        for (Team team : teams) {
            TeamViewModel tvm = new TeamViewModel();
            tvm.setId(team.getId());
            tvm.setName(team.getName());
            tvm.setMemberCount(team.getMembers().size());
            tvm.setProjectCount(team.getProjects().size());
            teamViewModels.add(tvm);
        }
        vm.setTeams(teamViewModels);
        return vm;
    }

    @Override
    @Transactional(readOnly = true)
    public TeamShowViewModel getTeamShowViewModel(Integer id) {
        TeamShowViewModel vm = new TeamShowViewModel();
        Team team = teamRepository.findById(id).orElse(null);

        vm.setId(team.getId());
        vm.setName(team.getName());

        Set<Integer> memberIds = team.getMembers().stream()
                .map(User::getId).collect(Collectors.toSet());
        Set<Integer> projectIds = team.getProjects().stream()
                .map(Project::getId).collect(Collectors.toSet());

        List<UserViewModel> members = new ArrayList<>();
        for (User user : team.getMembers()) {
            UserViewModel uvm = new UserViewModel();
            uvm.setId(user.getId());
            uvm.setUsername(user.getUsername());
            uvm.setFirstName(user.getFirstName());
            uvm.setLastName(user.getLastName());
            members.add(uvm);
        }
        vm.setMembers(members);

        List<ProjectViewModel> projects = new ArrayList<>();
        for (Project project : team.getProjects()) {
            ProjectViewModel pvm = new ProjectViewModel();
            pvm.setId(project.getId());
            pvm.setTitle(project.getTitle());
            projects.add(pvm);
        }
        vm.setProjects(projects);

        List<UserViewModel> availableUsers = new ArrayList<>();
        for (User user : userRepository.findAllByOrderByUsernameAsc()) {
            if (!memberIds.contains(user.getId())) {
                UserViewModel uvm = new UserViewModel();
                uvm.setId(user.getId());
                uvm.setUsername(user.getUsername());
                uvm.setFirstName(user.getFirstName());
                uvm.setLastName(user.getLastName());
                availableUsers.add(uvm);
            }
        }
        vm.setAvailableUsers(availableUsers);

        List<ProjectViewModel> availableProjects = new ArrayList<>();
        for (Project project : projectRepository.findAllByOrderByTitleAsc()) {
            if (!projectIds.contains(project.getId())) {
                ProjectViewModel pvm = new ProjectViewModel();
                pvm.setId(project.getId());
                pvm.setTitle(project.getTitle());
                availableProjects.add(pvm);
            }
        }
        vm.setAvailableProjects(availableProjects);

        return vm;
    }

    @Override
    public CreateTeamViewModel getCreateTeamViewModel() {
        CreateTeamViewModel vm = new CreateTeamViewModel();
        vm.setCreateTeamCommandModel(new CreateTeamCommandModel());
        return vm;
    }

    @Override
    public EditTeamViewModel getEditTeamViewModel(Integer id) {
        EditTeamViewModel vm = new EditTeamViewModel();
        Team team = teamRepository.findById(id).orElse(null);
        vm.setId(id);
        EditTeamCommandModel commandModel = new EditTeamCommandModel();
        commandModel.setId(team.getId());
        commandModel.setName(team.getName());
        vm.setEditTeamCommandModel(commandModel);
        return vm;
    }

    @Override
    @Transactional
    public Team saveCreateTeamCommandModel(CreateTeamCommandModel cmd) {
        Team team = new Team();
        team.setName(cmd.getName());
        return teamRepository.save(team);
    }

    @Override
    @Transactional
    public Team saveEditTeamCommandModel(EditTeamCommandModel cmd) {
        Team team = teamRepository.findById(cmd.getId()).orElse(null);
        team.setName(cmd.getName());
        return teamRepository.save(team);
    }

    @Override
    @Transactional
    public Team deleteTeam(Integer id) {
        Team team = teamRepository.findById(id).orElse(null);
        teamRepository.delete(team);
        return team;
    }

    @Override
    @Transactional
    public void addMember(Integer teamId, Integer userId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        team.getMembers().add(user);
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void removeMember(Integer teamId, Integer userId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        team.getMembers().removeIf(u -> u.getId().equals(userId));
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void addProject(Integer teamId, Integer projectId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);
        team.getProjects().add(project);
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void removeProject(Integer teamId, Integer projectId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        team.getProjects().removeIf(p -> p.getId().equals(projectId));
        teamRepository.save(team);
    }
}
