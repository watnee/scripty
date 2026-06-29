package com.chriswatnee.martinis.controller;

import com.chriswatnee.martinis.commandmodel.user.createuser.CreateUserCommandModel;
import com.chriswatnee.martinis.commandmodel.user.edituser.EditUserCommandModel;
import com.chriswatnee.martinis.dto.User;
import com.chriswatnee.martinis.viewmodel.user.createuser.CreateUserViewModel;
import com.chriswatnee.martinis.viewmodel.user.edituser.EditUserViewModel;
import com.chriswatnee.martinis.viewmodel.user.userlist.UserListViewModel;
import com.chriswatnee.martinis.webservice.UserWebService;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    @Inject
    UserWebService userWebService;

    @RequestMapping(value = "/list")
    public String list(Model model) {

        UserListViewModel viewModel = userWebService.getUserListViewModel();

        model.addAttribute("viewModel", viewModel);

        return "account/list";
    }

    @RequestMapping(value = "/delete")
    public String delete(@RequestParam Integer id) {

        userWebService.deleteUser(id);

        return "redirect:/account/list";
    }

    @RequestMapping(value = "/edit")
    public String edit(@RequestParam Integer id, Model model) {

        EditUserViewModel viewModel = userWebService.getEditUserViewModel(id);

        model.addAttribute("viewModel", viewModel);
        model.addAttribute("commandModel", viewModel.getEditUserCommandModel());

        return "account/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveEdit(@Valid @ModelAttribute("commandModel") EditUserCommandModel commandModel, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            EditUserViewModel viewModel = userWebService.getEditUserViewModel(commandModel.getId());

            model.addAttribute("viewModel", viewModel);
            model.addAttribute("commandModel", commandModel);

            return "account/edit";
        }

        userWebService.saveEditUserCommandModel(commandModel);

        return "redirect:/account/list";
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {

        CreateUserViewModel viewModel = userWebService.getCreateUserViewModel();

        model.addAttribute("viewModel", viewModel);
        model.addAttribute("commandModel", viewModel.getCreateUserCommandModel());

        return "account/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String saveCreate(@Valid @ModelAttribute("commandModel") CreateUserCommandModel commandModel, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            CreateUserViewModel viewModel = userWebService.getCreateUserViewModel();

            model.addAttribute("viewModel", viewModel);
            model.addAttribute("commandModel", commandModel);

            return "account/create";
        }

        userWebService.saveCreateUserCommandModel(commandModel);

        return "redirect:/account/list";
    }
}
