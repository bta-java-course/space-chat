package com.buseduc.javacourse;

import com.buseduc.javacourse.channels.Channel;
import com.buseduc.javacourse.channels.MainChannel;
import com.buseduc.javacourse.users.User;

import java.util.ArrayList;
import java.util.List;

public class SpaceChat {
    private List<User> userList;
    private List<Channel> channelList;
    public static void main(String[] args) {
        System.out.println("START");

    }

    public SpaceChat() {
        userList = new ArrayList<>();
        channelList = new ArrayList<>();
        Channel main = new MainChannel("main");
    }
}
