package ee.gr.sendingdata;

import ee.gr.user.User;

import java.io.Serializable;

public class DataPackage implements Serializable {

    private boolean loginIsCorrect;
    private boolean passwordIsCorrect;
    private ChannelUpdate channelUpdate;
    private User user;

    public DataPackage(boolean loginIsCorrect, boolean passwordIsCorrect, ChannelUpdate channelUpdate, User user) {
        this.loginIsCorrect = loginIsCorrect;
        this.passwordIsCorrect = passwordIsCorrect;
        this.channelUpdate = channelUpdate;
        this.user = user;
    }

    public boolean isLoginIsCorrect() {
        return loginIsCorrect;
    }

    public void setLoginIsCorrect(boolean loginIsCorrect) {
        this.loginIsCorrect = loginIsCorrect;
    }

    public boolean isPasswordIsCorrect() {
        return passwordIsCorrect;
    }

    public void setPasswordIsCorrect(boolean passwordIsCorrect) {
        this.passwordIsCorrect = passwordIsCorrect;
    }

    public ChannelUpdate getChannelUpdate() {
        return channelUpdate;
    }

    public void setChannelUpdate(ChannelUpdate channelUpdate) {
        this.channelUpdate = channelUpdate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
