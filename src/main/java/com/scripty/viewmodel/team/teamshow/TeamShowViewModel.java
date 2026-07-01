package com.scripty.viewmodel.team.teamshow;

import com.scripty.viewmodel.user.userlist.UserViewModel;
import com.scripty.viewmodel.project.projectlist.ProjectViewModel;
import java.util.List;

public class TeamShowViewModel {

    private int id;
    private String name;
    private List<UserViewModel> members;
    private List<ProjectViewModel> projects;
    private List<UserViewModel> availableUsers;
    private List<ProjectViewModel> availableProjects;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserViewModel> getMembers() {
        return members;
    }

    public void setMembers(List<UserViewModel> members) {
        this.members = members;
    }

    public List<ProjectViewModel> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectViewModel> projects) {
        this.projects = projects;
    }

    public List<UserViewModel> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(List<UserViewModel> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public List<ProjectViewModel> getAvailableProjects() {
        return availableProjects;
    }

    public void setAvailableProjects(List<ProjectViewModel> availableProjects) {
        this.availableProjects = availableProjects;
    }
}
