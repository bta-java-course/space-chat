package ee.gr.user;

import ee.gr.ConnectionListener;
import ee.gr.sendingdata.Message;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection {

    private final Socket socket;
    private final User currentUser;
    private Thread thread = null;
    private final ConnectionListener eventListener;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public Connection(ConnectionListener eventListener, String ipAdr, int port, User user) throws IOException {
        this(eventListener, new Socket(ipAdr, port), user);
    }

    public Connection(ConnectionListener connectionListener, Socket socket, User user) throws IOException {
        this.eventListener = connectionListener;
        this.socket = socket;
        this.currentUser = user;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.thread = new Thread(() -> {
            try {
                eventListener.onConnectionReady(Connection.this);
                while(!thread.isInterrupted()) {
                    String message = reader.readLine();
                    message = message.replace(":newline:", "\n\t");
                    eventListener.onReceiveString(Connection.this, message);
                }
            } catch (IOException e) {
                eventListener.onException(Connection.this, e);
            } finally {
                eventListener.onDisconnect(Connection.this);
            }
        });
        this.thread.start();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public synchronized void sendMessage(Message message) {
        String msg = message.getMessage();
        Pattern p = Pattern.compile("(::server::)",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(msg);
        try {
            if (!m.find()) writer.write(currentUser.getName() + ":" + "\n\t" + msg + "\r\n");
            else {
                msg = msg.replaceAll("(::server::)", "");
                writer.write("Server: " + msg + "\r\n");
            }
            writer.flush();
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        this.thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }

    public Socket getSocket() {
        return socket;
    }

}
