package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/3/13 11:08
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class FriendListBean implements Serializable {
    private List<FriendBean> users;

    public List<FriendBean> getUsers() {
        return users;
    }

    public void setUsers(List<FriendBean> users) {
        this.users = users;
    }
}
