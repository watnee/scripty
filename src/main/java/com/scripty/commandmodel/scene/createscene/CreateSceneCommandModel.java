/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scripty.commandmodel.scene.createscene;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author chris
 */
public class CreateSceneCommandModel {
    
    @NotBlank(message = "You must supply a value for Name.")
    @Size(max = 255, message = "Name must be no more than 255 characters in length.")
    private String name;

    private String label;

    private Integer projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    
}
