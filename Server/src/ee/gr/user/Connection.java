package ee.gr.user;

import ee.gr.ConnectionListener;
import ee.gr.channel.Channel;
import ee.gr.sendingdata.ChannelUpdate;
import ee.gr.sendingdata.DataPackage;
import ee.gr.sendingdata.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Connection {

    private final Socket socket;
    private volatile User currentUser;
    private Thread thread = null;
    private final ConnectionListener eventListener;
    private ObjectInputStream inPut;
    private ObjectOutputStream outPut;

    public Connection(ConnectionListener connectionListener, Socket socket, List<Channel> channelList, int id, List<User> usersList) throws IOException {
        this.eventListener = connectionListener;
        this.socket = socket;
        this.thread = new Thread(() -> {
            run(socket, channelList, id, usersList);
        });
        this.thread.start();
    }

    private void run(Socket socket, List<Channel> channelList, int id, List<User> usersList) {
        try {
            this.inPut = new ObjectInputStream(socket.getInputStream());
            this.outPut = new ObjectOutputStream(socket.getOutputStream());
            this.currentUser = (User)inPut.readObject();
            currentUser.setCurrentChannel(channelList.get(0));
            currentUser.setId(id);
            outPut.writeObject(new DataPackage(
                    true,
                    true,
                    new ChannelUpdate(getClearChannels(channelList), ChannelUpdate.Action.INIT_CHANNELS),
                    currentUser));
                    outPut.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        currentUser.setConnection(this);
        eventListener.onConnectionReady(Connection.this);
        usersList.add(currentUser);
        System.out.println(currentUser.getName() + " connected.");
        try {
            while(true) {
                Object object = inPut.readObject();
                eventListener.onReceiveObject(Connection.this, object);
            }
        } catch (IOException | ClassNotFoundException e) {
            eventListener.onException(Connection.this, e);
        } finally {
            System.out.println(currentUser.getName() + " disconnected.");
            disconnect();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public synchronized void sendMessage(Message message) {
        try {
            outPut.writeObject(message);
            outPut.flush();
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void sendChannelUpdate(ChannelUpdate channelUpdate) {
        try {
            outPut.writeObject(channelUpdate);
            outPut.flush();
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        this.thread.interrupt();
        eventListener.onDisconnect(Connection.this);
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
        }
    }

    private List<Channel> getClearChannels(List<Channel> channelsList) {
        return channelsList;
    }

    @Override
    public String toString() {
        return "Connection: " + socket.getInetAddress() + ": " + socket.getPort();
    }

}
