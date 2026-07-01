package com.scripty.controller;

import com.scripty.commandmodel.team.editteam.EditTeamCommandModel;
import com.scripty.service.TeamSettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/account/team")
public class TeamSettingController {

    @Autowired
    TeamSettingService teamSettingService;

    @RequestMapping(value = "/edit")
    public String edit(Model model) {
        EditTeamCommandModel commandModel = new EditTeamCommandModel();
        commandModel.setTeamName(teamSettingService.getTeamName());
        model.addAttribute("commandModel", commandModel);
        return "account/team-edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveEdit(@Valid @ModelAttribute("commandModel") EditTeamCommandModel commandModel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("commandModel", commandModel);
            return "account/team-edit";
        }

        teamSettingService.saveTeamName(commandModel.getTeamName());
        return "redirect:/account/list";
    }
}
