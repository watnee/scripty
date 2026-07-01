package com.scripty.controller;

import com.scripty.commandmodel.team.createteam.CreateTeamCommandModel;
import com.scripty.commandmodel.team.editteam.EditTeamCommandModel;
import com.scripty.dto.Team;
import com.scripty.service.TeamService;
import com.scripty.viewmodel.team.teamlist.TeamListViewModel;
import com.scripty.viewmodel.team.teamshow.TeamShowViewModel;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/team")
public class TeamRestController {

    @Autowired
    TeamService teamService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> list() {
        TeamListViewModel viewModel = teamService.getTeamListViewModel();
        return new ResponseEntity<>(viewModel.getTeams(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> show(@PathVariable Integer id) {
        TeamShowViewModel viewModel = teamService.getTeamShowViewModel(id);
        return new ResponseEntity<>(viewModel, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@Valid @RequestBody CreateTeamCommandModel commandModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(buildErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        Team team = teamService.saveCreateTeamCommandModel(commandModel);
        Map<String, Object> response = new HashMap<>();
        response.put("id", team.getId());
        response.put("name", team.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody EditTeamCommandModel commandModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(buildErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        commandModel.setId(id);
        Team team = teamService.saveEditTeamCommandModel(commandModel);
        Map<String, Object> response = new HashMap<>();
        response.put("id", team.getId());
        response.put("name", team.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Team team = teamService.deleteTeam(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", team.getId());
        response.put("name", team.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{teamId}/members/{userId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addMember(@PathVariable Integer teamId, @PathVariable Integer userId) {
        teamService.addMember(teamId, userId);
        return new ResponseEntity<>(Map.of("status", "added"), HttpStatus.OK);
    }

    @RequestMapping(value = "/{teamId}/members/{userId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> removeMember(@PathVariable Integer teamId, @PathVariable Integer userId) {
        teamService.removeMember(teamId, userId);
        return new ResponseEntity<>(Map.of("status", "removed"), HttpStatus.OK);
    }

    @RequestMapping(value = "/{teamId}/projects/{projectId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addProject(@PathVariable Integer teamId, @PathVariable Integer projectId) {
        teamService.addProject(teamId, projectId);
        return new ResponseEntity<>(Map.of("status", "added"), HttpStatus.OK);
    }

    @RequestMapping(value = "/{teamId}/projects/{projectId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> removeProject(@PathVariable Integer teamId, @PathVariable Integer projectId) {
        teamService.removeProject(teamId, projectId);
        return new ResponseEntity<>(Map.of("status", "removed"), HttpStatus.OK);
    }

    private Map<String, String> buildErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
