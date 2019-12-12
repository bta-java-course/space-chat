package ee.gr;

import ee.gr.user.Connection;

public interface ConnectionListener {

    void onConnectionReady(Connection connection);

    void onReceiveObject(Connection connection, Object object);

    void onDisconnect(Connection connection);

    void onException(Connection connection, Exception e);

}
