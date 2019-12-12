package ee.gr;

import ee.gr.channel.Channel;
import ee.gr.sendingdata.ChannelUpdate;
import ee.gr.sendingdata.Message;
import ee.gr.user.User;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.ObjectInputStream;

public class DataListener extends Thread {

    public DataListener() {
        super(new TaskListenerData(Launch.getInstance().getInputStream(), Launch.getInstance()));
        this.setName("Main Listening Thread");
        this.setDaemon(true);
        System.out.println("should start");
        System.out.println(Thread.getAllStackTraces().size());
        this.start();
        System.out.println(Thread.getAllStackTraces().size());
    }

}

class TaskListenerData extends Task {

    private ObjectInputStream input;
    private Launch launcher;

    public TaskListenerData(ObjectInputStream input, Launch launcher) {
        this.input = input;
        this.launcher = launcher;
    }

    @Override
    protected Object call() throws Exception {
        listeningData();
        return null;
    }

    private void listeningData() throws IOException, ClassNotFoundException {
        while (true) {
            Object enteredObject = input.readObject();
            if (enteredObject instanceof Message) {
                System.out.println("Message Received");
                workingWithMessage((Message) enteredObject);
            } else if (enteredObject instanceof ChannelUpdate) {
                System.out.println("ChannelUpdate Received");
                workingWithChannelUpdate((ChannelUpdate) enteredObject);
            } else {
                System.out.println("New kind of object received: " + enteredObject);
            }
        }
    }

    private void workingWithMessage(Message enteredObject) {
        Message message = enteredObject;
        Channel channel = message.getChannel() == null ? launcher.getCurrentChannel() : launcher.getOwnChannel(message.getChannel());
        System.out.println(channel.getChannelName());
        message.setChannel(channel);
        channel.getMessageHistory().addMessage(message);
    }

    private void workingWithChannelUpdate(ChannelUpdate channelUpdate) {
        if (channelUpdate.getUsersList() != null)
            System.out.println(channelUpdate.getUsersList().size());
        User user = channelUpdate.getUser();
        Channel channel = channelUpdate.getChannel() != null ? launcher.getOwnChannel(channelUpdate.getChannel()) : launcher.getCurrentChannel();
        if (channelUpdate.getAction().equals(ChannelUpdate.Action.INIT_CHANNELS)) {
            System.out.println("received update INIT_CHANNELS");
            launcher.setChannelList(channelUpdate.getChannelList());
        } else if (channelUpdate.getAction().equals(ChannelUpdate.Action.CHANNEL_REMOVE)) {
            launcher.removeChannelByName(channelUpdate.getChannel().getChannelName());
        } else if (channelUpdate.getAction().equals(ChannelUpdate.Action.CHANNEL_ADD)) {
            launcher.getChannelList().add(channelUpdate.getChannel());
        } else if (channelUpdate.getAction().equals(ChannelUpdate.Action.USER_LOG_IN)) {
            System.out.println("received update user USER_LOG_IN");
            launcher.getCurrentChannel().getUsersList().clear();
            launcher.getCurrentChannel().getUsersList().addAll(channelUpdate.getUsersList());
            channelUpdate.getUsersList().forEach(System.out::println);
            launcher.getCurrentChannel().setUserListIsChanged(true);
        } else if (channelUpdate.getAction().equals(ChannelUpdate.Action.USER_LOG_OUT)) {
            System.out.println("received update user USER_LOG_OUT");
            launcher.getCurrentChannel().getUsersList().clear();
            launcher.getCurrentChannel().getUsersList().addAll(channelUpdate.getUsersList());
            launcher.getCurrentChannel().setUserListIsChanged(true);
        } else {
            System.out.println("Something wrong with receiving ChannelUpdate!");
        }
    }

}
