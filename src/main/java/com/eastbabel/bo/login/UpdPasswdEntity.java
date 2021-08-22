package com.eastbabel.bo.login;

import lombok.Data;

@Data
public class UpdPasswdEntity {

    private String username;

    private String oldPassword;

    private String newPassword;

}
