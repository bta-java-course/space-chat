package ee.gr;

import com.sun.xml.internal.ws.util.StringUtils;
import ee.gr.channel.Channel;
import ee.gr.sendingdata.ChannelUpdate;
import ee.gr.sendingdata.Message;
import ee.gr.sendingdata.UserUpdate;
import ee.gr.user.Connection;
import ee.gr.user.User;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements ConnectionListener {

    private static int counter;
    private volatile List<Connection> connectionList;
    private volatile List<User> userList;
    private volatile List<Channel> channelList;
    private AesCrypt aesCrypt;

    private Server() throws NoSuchProviderException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        this.connectionList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.channelList = initChannels();
        this.aesCrypt = new AesCrypt();
        System.out.println("Server starts...");
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    counter++;
                    new Connection(this, socket, channelList, counter, userList);
                } catch (IOException e) {
                    System.out.println("Connection excptn: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Server createServer() throws NoSuchAlgorithmException,
            NoSuchPaddingException, NoSuchProviderException, InvalidKeyException {
        return new Server();
    }

    private List<Channel> initChannels() {
        List<Channel> list = new ArrayList<>();
        list.add(new Channel(Planet.UNIVERSE));
        list.add(new Channel(Planet.EARTH));
        list.add(new Channel(Planet.JUPITER));
        list.add(new Channel(Planet.MERCURY));
        list.add(new Channel(Planet.NEPTUNE));
        list.add(new Channel(Planet.SATURN));
        list.add(new Channel(Planet.URANUS));
        list.add(new Channel(Planet.MARS));
        list.add(new Channel(Planet.VENUS));
        return list;
    }

    public static void main(String[] args) throws NoSuchProviderException,
            InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        createServer();
    }

    private void workingWithMessage(Message message) {
        Channel channel = getOwnChannel(message.getChannel());
        if (message.getType().equals(Message.Type.FROM_USER_PRIVATE)){
            sendToCurrentUser(message);
        }
        else {
            channel.getMessageHistory().getMessageList().add(message);
            sendToCurrentChannel(message, channel);
        }
    }

    private void sendToCurrentUser(Message message) {
        User receiver = getOwnUser(message.getReceiver());
        User sender = getOwnUser(message.getUser());
        if (!receiver.isLoggedOut()) {
            receiver.getConnection().sendMessage(message);
            sender.getConnection().sendMessage(message);
            System.out.println("Private message was sent to " + receiver.getName() + " from " +
                    sender.getName() + ": " + message.getMessage());
        }
    }

    private void sendToCurrentChannel(Message message, Channel channel) {
        System.out.println("Channel " +
                channel.getChannelName() +
                ". " + message.getUser().getName() +
                ": " + message.getMessage());
        Message encryptedMessage = message.clone();
        channel.getUsersList().stream()
                .map(User::getConnection)
                .forEach(connection -> {
                    if (channel.getChannelName().equals(Planet.UNIVERSE.name())) {
                        connection.sendMessage(message);
                        System.out.println("Message was sent to " + connection.getCurrentUser().getName() + ": " +
                                message.getMessage());
                    }
                    else if (message.getUser().equals(connection.getCurrentUser())) {
                        connection.sendMessage(message);
                        System.out.println("Message was sent to " + connection.getCurrentUser().getName() + ": " +
                                message.getMessage());
                    }
                    else if (!message.getUser().getPlanet().equals(channel.getPlanet())) {
                        try {
                            encryptedMessage.setMessage(aesCrypt.encrypt(message.getMessage()));
                        } catch (BadPaddingException | IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }
                        connection.sendMessage(encryptedMessage);
                        System.out.println("Message was sent to " + connection.getCurrentUser().getName() + ": " +
                                message.getMessage());
                    }
                    else if(!message.getChannel().getPlanet().equals(connection.getCurrentUser().getPlanet())){

                        try {
                            encryptedMessage.setMessage(aesCrypt.encrypt(message.getMessage()));
                        } catch (BadPaddingException | IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }
                        connection.sendMessage(encryptedMessage);
                        System.out.println("Message was sent to " + connection.getCurrentUser().getName() + ": " +
                                message.getMessage());
                    }
                    else {
                        connection.sendMessage(message);
                        System.out.println("Message was sent to " + connection.getCurrentUser().getName() + ": " +
                                message.getMessage());
                    }
                });
    }

    private void workingWithChannelUpdate(ChannelUpdate channelUpdate) {
        Channel channel = getOwnChannel(channelUpdate.getChannel());
        User user = getOwnUser(channelUpdate.getUser());
        if (channelUpdate.getAction().equals(ChannelUpdate.Action.USER_LOG_IN)) {
            System.out.println("ChannelUpdate: Working with Logged in User");
            user.setCurrentChannel(channel);
            channel.getUsersList().add(user);
            System.out.println(user.getName() + " was added to " + channel.getChannelName() +
                    ", now here is " + channel.getUsersList().size() + " users.");
            sendToAllConnectionsUserLogIn(channel, user);
        } else if (channelUpdate.getAction().equals(ChannelUpdate.Action.USER_LOG_OUT)) {
            System.out.println("ChannelUpdate: Working with Logged out User");
            channel.getUsersList().remove(user);
            user.setCurrentChannel(null);
            sendToAllConnectionsUserLogOut(channel, user);
        }
    }

    private void sendToAllConnectionsUserLogIn(Channel channel, User user) {
        AtomicInteger counter = new AtomicInteger();
        connectionList.stream().filter(c -> c.getCurrentUser().getCurrentChannel() == channel)
                .forEach(c -> {
                    counter.getAndIncrement();
                    //System.out.println(counter);
                    c.sendChannelUpdate(new ChannelUpdate(
                            ChannelUpdate.Action.USER_LOG_IN, new ArrayList<>(channel.getUsersList())));
                    c.sendMessage(getLogInOutSystemMessage(channel, user, " entered "));
                    System.out.println("ChannelUpdate, that " + user.getName() + " logged in, was sent to " +
                            c.getCurrentUser().getName());
                    //channel.getUsersList().forEach(System.out::println);
                });
        counter.set(0);
        user.setLoggedOut(false);
    }

    private void sendToAllConnectionsUserLogOut(Channel channel, User user) {
        if (!getOwnUser(user).isLoggedOut()) {
            connectionList.stream().filter(c -> c.getCurrentUser().getCurrentChannel() == channel)
                    .forEach(c -> {
                        c.sendChannelUpdate(new ChannelUpdate(
                                ChannelUpdate.Action.USER_LOG_OUT, new ArrayList<>(channel.getUsersList())));
                        c.sendMessage(getLogInOutSystemMessage(channel, user, " left "));
                        System.out.println("ChannelUpdate, that " + user.getName() + " logged out, was sent to " +
                                c.getCurrentUser().getName());
                        //channel.getUsersList().forEach(System.out::println);
                    });
            getOwnUser(user).setLoggedOut(true);
        }
    }

    private Message getLogInOutSystemMessage(Channel channel, User user, String msg) {
        return new Message(
                user.getName() + " from "
                        + StringUtils.capitalize(user.getPlanet().name().toLowerCase())
                        + msg + StringUtils.capitalize(channel.getChannelName().toLowerCase()) + " channel.",
                channel, Message.Type.SYSTEM_CHANNEL);
    }

    private User getOwnUser(User user) {
        return userList.stream()
                .filter(usr -> usr.getId() == user.getId())
                .findAny().get();
    }

    private Channel getOwnChannel(Channel channel) {
        return channelList.stream()
                .filter(ch -> ch.getChannelName().equals(channel.getChannelName()))
                .findAny().get();
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connectionList.add(connection);
    }

    @Override
    public synchronized void onReceiveObject(Connection connection, Object object) {
        if (object instanceof Message) {
            workingWithMessage((Message) object);
            System.out.println("Message received.");
        } else if (object instanceof ChannelUpdate) {
            System.out.println("ChannelUpdate received.");
            workingWithChannelUpdate((ChannelUpdate) object);
        } else if (object instanceof UserUpdate) {
            UserUpdate userUpdate = ((UserUpdate) object);
            System.out.println("UserUpdate received.");
            //It will update user's info
        } else {
            System.out.println("New kind of object received: " + object);
        }
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        User user = connection.getCurrentUser();
        Channel channel = user.getCurrentChannel();
        if (channel != null) channel.getUsersList().remove(user);
        user.setCurrentChannel(null);
        sendToAllConnectionsUserLogOut(channel, user);
        connectionList.remove(connection);
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("TCPConnection excptn: " + e);
    }

}


