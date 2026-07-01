package com.scripty.commandmodel.team.editteam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditTeamCommandModel {

    private Integer id;

    @NotBlank(message = "You must supply a value for Name.")
    @Size(max = 100, message = "Name must be no more than 100 characters in length.")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
