package com.chriswatnee.martinis.webservice;

import com.chriswatnee.martinis.commandmodel.user.createuser.CreateUserCommandModel;
import com.chriswatnee.martinis.commandmodel.user.edituser.EditUserCommandModel;
import com.chriswatnee.martinis.dto.User;
import com.chriswatnee.martinis.viewmodel.user.createuser.CreateUserViewModel;
import com.chriswatnee.martinis.viewmodel.user.edituser.EditUserViewModel;
import com.chriswatnee.martinis.viewmodel.user.userlist.UserListViewModel;

public interface UserWebService {

    public UserListViewModel getUserListViewModel();

    public CreateUserViewModel getCreateUserViewModel();
    public EditUserViewModel getEditUserViewModel(Integer id);

    public User saveCreateUserCommandModel(CreateUserCommandModel createUserCommandModel);
    public User saveEditUserCommandModel(EditUserCommandModel editUserCommandModel);

    public User deleteUser(Integer id);

}
