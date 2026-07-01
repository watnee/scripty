package com.scripty.viewmodel.user.userlist;

import java.util.List;

public class UserListViewModel {

    private List<UserViewModel> users;
    private String teamName;

    public List<UserViewModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserViewModel> users) {
        this.users = users;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}
