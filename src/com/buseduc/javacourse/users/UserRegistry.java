package com.buseduc.javacourse.users;

import java.util.ArrayList;
import java.util.List;

public class UserRegistry {
    private static class RegistryHolder {
        private static UserRegistry instance = new UserRegistry();
    }
    public static UserRegistry getInstance() {
        return RegistryHolder.instance;
    }
    private List<UserServer> users;

    private UserRegistry() {
        users = new ArrayList<>();
    }

    public void addUser(UserServer user) {
        users.add(user);
    }

}
