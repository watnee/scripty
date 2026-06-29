package com.chriswatnee.martinis.viewmodel.user.createuser;

import com.chriswatnee.martinis.commandmodel.user.createuser.CreateUserCommandModel;

public class CreateUserViewModel {

    private CreateUserCommandModel createUserCommandModel;

    public CreateUserCommandModel getCreateUserCommandModel() {
        return createUserCommandModel;
    }

    public void setCreateUserCommandModel(CreateUserCommandModel createUserCommandModel) {
        this.createUserCommandModel = createUserCommandModel;
    }

}
