package ee.gr.GUI.controllers;

import com.sun.xml.internal.ws.util.StringUtils;
import ee.gr.GUI.ClientWindow;
import ee.gr.Launch;
import ee.gr.channel.Channel;
import ee.gr.sendingdata.ChannelUpdate;
import ee.gr.sendingdata.Message;
import ee.gr.user.User;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class mainChannelController implements Initializable {

    private Socket socket;
    @FXML
    private Label smileOne;
    @FXML
    private Label smileTwo;
    @FXML
    private Label smileThree;
    @FXML
    private Label smileFour;
    @FXML
    private Label smileFive;
    @FXML
    private Label smileSix;
    @FXML
    private Label smileSeven;
    @FXML
    private Label smileEight;
    @FXML
    private Label smileNine;
    @FXML
    public VBox chatBox;
    @FXML
    private ScrollPane scrollPaneChat;
    @FXML
    private Button buttonSendMsg;
    @FXML
    private Button buttonLogOut;
    @FXML
    private Label labelChannelName;
    @FXML
    private Button buttonChangeChannel;
    @FXML
    private TextArea textAreaMsg;
    @FXML
    private VBox vBoxUsers;
    @FXML
    private Label labelPrivate;
    @FXML
    private Label labelClosePrivate;
    private ObjectInputStream inPut;
    private ObjectOutputStream outPut;
    private Launch launchInstance;
    private Channel currentChannel;
    private AnimationTimer listenerForChatBox;
    private AnimationTimer listenerForUsersBox;
    private AnimationTimer listenerForDisconnect;
    private List<Label> smileList;
    private User receiver;

    public void initialize(URL location, ResourceBundle resources) {
        scrollPaneChat.vvalueProperty().bind(chatBox.heightProperty());
        launchInstance = Launch.getInstance();
        socket = launchInstance.getSocket();
        currentChannel = launchInstance.getCurrentChannel();
        labelChannelName.setText(StringUtils.capitalize(currentChannel.getChannelName().toLowerCase().concat(" users:")));
        inPut = launchInstance.getInputStream();
        outPut = launchInstance.getOutputStream();
        labelClosePrivate.setDisable(true);
        labelClosePrivate.setVisible(false);
        initActionSmiles();
        startListeningForMsg();
        startLookingForUsers();
        startLookingForDisconnect();
        sendLogInUpdate();
    }

    private void initActionSmiles() {
        smileList = Arrays.asList(
                smileOne, smileTwo, smileThree,
                smileFour, smileFive, smileSix,
                smileSeven, smileEight, smileNine);
        for (Label nextSmile : smileList) {
            nextSmile.setOnMouseClicked(event -> {
                textAreaMsg.appendText("::" + nextSmile.getId() + "::");
            });
        }
    }

    private void startLookingForDisconnect() {
        listenerForDisconnect = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (socket.isClosed()) {
                    try {
                        logOut();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        listenerForDisconnect.start();
    }

    //Change for current channel MessageHistory listening
    private void startListeningForMsg() {
        listenerForChatBox = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (currentChannel.getMessageHistory().isUpdated()) {
                    Message lastMessage = currentChannel.getMessageHistory().getLastMessage();
                    if (lastMessage != null) {
                        addingMsgToVBox(lastMessage);
                    }
                }
            }

            private void addingMsgToVBox(Message msg) {
                if (msg.getMessage() != null) {
                    TextFlow wholeMessage = new TextFlow();
                    String msgText = msg.getMessage().replace("::newline::", "\n");
                    if (msg.getType().equals(Message.Type.SYSTEM_ALL) ||
                            msg.getType().equals(Message.Type.SYSTEM_CHANNEL) ||
                            msg.getType().equals(Message.Type.SYSTEM_FOR_USER)) {
                        workWithSystemMessages(msg, wholeMessage, msgText);
                    } else {
                        workWithUsersMessages(msg, wholeMessage, msgText);
                    }
                    chatBox.getChildren().add(wholeMessage);
                }
            }

            private void workWithSystemMessages(Message msg, TextFlow wholeMessage, String msgText) {
                Text text = new Text(msg.getUser().getName() + ": " + msgText);
                text.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
                wholeMessage.getChildren().add(text);
            }

            private void workWithUsersMessages(Message msg, TextFlow wholeMessage, String msgText) {
                Text user = new Text(msg.getUser().getName() + ":" + "\n");
                initTextForPrivateMessage(user);
                user.setStyle("-fx-font-weight: bold; -fx-cursor: hand");
                user.setWrappingWidth(chatBox.getMaxWidth());
                if (msg.getType().equals(Message.Type.FROM_USER_PRIVATE)) {
                    user.setFill(Color.valueOf("#840606"));
                    user.setWrappingWidth(chatBox.getMaxWidth());
                    Text prefixText = new Text();
                    prefixText.setFill(Color.valueOf("#840606"));
                    prefixText.setStyle("-fx-font-weight: bold");
                    if (msg.getUser().equals(launchInstance.getActiveUser())) {
                        Text receiver = new Text(msg.getReceiver().getName() + ":" + "\n");
                        initTextForPrivateMessage(receiver);
                        receiver.setFill(Color.valueOf("#840606"));
                        receiver.setStyle("-fx-font-weight: bold; -fx-cursor: hand");
                        receiver.setWrappingWidth(chatBox.getMaxWidth());
                        prefixText.setText("Private message to ");
                        wholeMessage.getChildren().add(prefixText);
                        wholeMessage.getChildren().add(receiver);
                    }
                    else {
                        prefixText.setText("Private message from ");
                        wholeMessage.getChildren().add(prefixText);
                        wholeMessage.getChildren().add(user);
                    }
                } else
                    wholeMessage.getChildren().add(user);
                for (String nextString : getSplitText(msgText)) {
                    if (smileList.stream().anyMatch(label -> ("::" + label.getId() + "::").equals(nextString))) {
                        Label label = smileList.stream().filter(label1 -> ("::" + label1.getId() + "::").equals(nextString))
                                .findFirst().get();
                        Image smileImage = new Image(((ImageView)label.getGraphic()).getImage().impl_getUrl());
                        Label newLabel = new Label("", new ImageView(smileImage));
                        wholeMessage.getChildren().add(newLabel);
                    } else {
                        Text text = new Text(nextString);
                        text.setWrappingWidth(chatBox.getMaxWidth());
                        wholeMessage.getChildren().add(text);
                    }
                }
            }

            private List<String> getSplitText(String text) {
                List<String> list = Arrays.asList(text);
                for (Label smile : smileList) {
                    Pattern p = Pattern.compile("(?<=::" + smile.getId() + "::)(?=::" + smile.getId() + "::)|" +
                            "(?<=.)(?=::" + smile.getId() + "::)|" +
                            "(?<=::" + smile.getId() + "::)(?=.)");
                    List<String> midList = new ArrayList<>();
                    for (String string : list) {
                        if (string.contains("::" + smile.getId() + "::")){
                            List<String> deepList = Arrays.asList(p.split(string));
                            deepList.forEach(c -> midList.add(c));
                        }
                        else midList.add(string);
                    }
                    list = midList;
                }
                return list;
            }
        };
        listenerForChatBox.start();
    }
    private void startLookingForUsers() {
        listenerForUsersBox = new AnimationTimer() {
            @Override
            public void handle(long now) {
             if (currentChannel.isUserListIsChanged()) {
                 System.out.println("Enters to Users Listener");
                 vBoxUsers.getChildren().clear();
                 currentChannel.getUsersList().forEach(user -> {
                     String name = user.getName();
                     Label label = new Label(name);
                     label.setStyle("-fx-cursor: hand;");
                     initTextForPrivateMessage(label);
                     label.setTextFill(Color.WHITE);
                     vBoxUsers.getChildren().add(label);
                 });
                 currentChannel.setUserListIsChanged(false);
             }
            }
        };
        listenerForUsersBox.start();
    }

    @FXML
    private void sendMsg() throws IOException {
        String msg = textAreaMsg.getText();
        msg = msg.replace("\n", "::newline::");
        if (!labelClosePrivate.isDisabled()) {
            outPut.writeObject(new Message(msg, launchInstance.getActiveUser(), receiver, currentChannel, Message.Type.FROM_USER_PRIVATE));
            labelPrivate.setText("");
            labelClosePrivate.setDisable(true);
            labelClosePrivate.setVisible(false);
            receiver = null;
        } else
            outPut.writeObject(new Message(msg, launchInstance.getActiveUser(), currentChannel, Message.Type.FROM_USER));
        textAreaMsg.setText("");
    }

    @FXML
    private void logOut() throws IOException {
        chatBox.getChildren().clear();
        launchInstance.getActiveUser().setLoggedOut(true);
        listenerForChatBox.stop();
        listenerForChatBox = null;
        listenerForUsersBox.stop();
        listenerForUsersBox = null;
        launchInstance.stopListener();
        socket.close();
        outPut.close();
        inPut.close();
        changeScene("GUI/controllers/LogIn.fxml");
    }

    @FXML
    private void changeChannel() throws IOException {
        outPut.writeObject(new ChannelUpdate(launchInstance.getActiveUser(), currentChannel, ChannelUpdate.Action.USER_LOG_OUT));
        chatBox.getChildren().clear();
        launchInstance.getActiveUser().setLoggedOut(true);
        listenerForDisconnect.stop();
        listenerForDisconnect = null;
        listenerForChatBox.stop();
        listenerForChatBox = null;
        listenerForUsersBox.stop();
        listenerForUsersBox = null;
        changeScene("GUI/controllers/ChannelPick.fxml");
    }

    private void changeScene(String s) throws IOException {
        File ne = new File(Launch.PROPERTY.concat(Launch.PATH + s));
        Pane myPane = FXMLLoader.load(ne.toURL());
        ClientWindow.getMyStage().setScene(new Scene(myPane));
    }

    private void sendLogInUpdate() {
        ChannelUpdate channelUpdate = new ChannelUpdate(launchInstance.getActiveUser(), currentChannel, ChannelUpdate.Action.USER_LOG_IN);
        try {
            outPut.writeObject(channelUpdate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTextForPrivateMessage(Label label) {
        label.setOnMouseClicked(event -> {
            receiver = currentChannel.getUserByName(label.getText()).clone();
            labelPrivate.setText(label.getText());
            labelClosePrivate.setVisible(true);
            labelClosePrivate.setDisable(false);
        });
    }

    private void initTextForPrivateMessage(Text text) {
        text.setOnMouseClicked(event -> {
            String textS = text.getText();
            textS = textS.replace("\n", "");
            textS = textS.substring(0, textS.length() - 1);
            receiver = currentChannel.getUserByName(textS).clone();
            if (receiver == null) return;
            labelPrivate.setText(textS);
            labelClosePrivate.setVisible(true);
            labelClosePrivate.setDisable(false);
        });
    }

    @FXML
    private void removePrivateLabel() {
        labelPrivate.setText("");
        labelClosePrivate.setDisable(true);
        labelClosePrivate.setVisible(false);
        receiver = null;
    }

}
