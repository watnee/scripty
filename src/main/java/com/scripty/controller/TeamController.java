package com.scripty.controller;

import com.scripty.commandmodel.team.createteam.CreateTeamCommandModel;
import com.scripty.commandmodel.team.editteam.EditTeamCommandModel;
import com.scripty.dto.Team;
import com.scripty.service.TeamService;
import com.scripty.viewmodel.team.createteam.CreateTeamViewModel;
import com.scripty.viewmodel.team.editteam.EditTeamViewModel;
import com.scripty.viewmodel.team.teamlist.TeamListViewModel;
import com.scripty.viewmodel.team.teamshow.TeamShowViewModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/team")
public class TeamController {

    @Autowired
    TeamService teamService;

    @RequestMapping(value = "/list")
    public String list(Model model) {
        TeamListViewModel viewModel = teamService.getTeamListViewModel();
        model.addAttribute("viewModel", viewModel);
        return "team/list";
    }

    @RequestMapping(value = "/show")
    public String show(@RequestParam Integer id, Model model) {
        TeamShowViewModel viewModel = teamService.getTeamShowViewModel(id);
        model.addAttribute("viewModel", viewModel);
        return "team/show";
    }

    @RequestMapping(value = "/delete")
    public String delete(@RequestParam Integer id) {
        teamService.deleteTeam(id);
        return "redirect:/team/list";
    }

    @RequestMapping(value = "/edit")
    public String edit(@RequestParam Integer id, Model model) {
        EditTeamViewModel viewModel = teamService.getEditTeamViewModel(id);
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("commandModel", viewModel.getEditTeamCommandModel());
        return "team/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveEdit(@Valid @ModelAttribute("commandModel") EditTeamCommandModel commandModel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            EditTeamViewModel viewModel = teamService.getEditTeamViewModel(commandModel.getId());
            model.addAttribute("viewModel", viewModel);
            model.addAttribute("commandModel", commandModel);
            return "team/edit";
        }
        Team team = teamService.saveEditTeamCommandModel(commandModel);
        return "redirect:/team/show?id=" + team.getId();
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {
        CreateTeamViewModel viewModel = teamService.getCreateTeamViewModel();
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("commandModel", viewModel.getCreateTeamCommandModel());
        return "team/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String saveCreate(@Valid @ModelAttribute("commandModel") CreateTeamCommandModel commandModel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            CreateTeamViewModel viewModel = teamService.getCreateTeamViewModel();
            model.addAttribute("viewModel", viewModel);
            model.addAttribute("commandModel", commandModel);
            return "team/create";
        }
        Team team = teamService.saveCreateTeamCommandModel(commandModel);
        return "redirect:/team/show?id=" + team.getId();
    }

    @RequestMapping(value = "/addMember", method = RequestMethod.POST)
    public String addMember(@RequestParam Integer teamId, @RequestParam Integer userId) {
        teamService.addMember(teamId, userId);
        return "redirect:/team/show?id=" + teamId;
    }

    @RequestMapping(value = "/removeMember")
    public String removeMember(@RequestParam Integer teamId, @RequestParam Integer userId) {
        teamService.removeMember(teamId, userId);
        return "redirect:/team/show?id=" + teamId;
    }

    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    public String addProject(@RequestParam Integer teamId, @RequestParam Integer projectId) {
        teamService.addProject(teamId, projectId);
        return "redirect:/team/show?id=" + teamId;
    }

    @RequestMapping(value = "/removeProject")
    public String removeProject(@RequestParam Integer teamId, @RequestParam Integer projectId) {
        teamService.removeProject(teamId, projectId);
        return "redirect:/team/show?id=" + teamId;
    }
}
