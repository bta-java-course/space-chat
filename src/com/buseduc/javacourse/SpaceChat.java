package com.buseduc.javacourse;

import com.buseduc.javacourse.channels.Channel;
import com.buseduc.javacourse.users.User;

import java.util.ArrayList;
import java.util.List;

public class SpaceChat {

    private List<User> users = new ArrayList<>();
    private List<Channel> channels = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
    }
}
