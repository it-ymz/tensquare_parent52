package com.tensquare.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="tb_nofriend")
@IdClass(Nofriend.class)  //联合组件的意思
public class Nofriend implements Serializable{

    @Id
    private String userid;

    @Id
    private String friendid;

    public Nofriend() {
    }

    @Override
    public String toString() {
        return "Nofriend{" +
                "userid='" + userid + '\'' +
                ", friendid='" + friendid + '\'' +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }


    public Nofriend(String userid, String friendid) {
        this.userid = userid;
        this.friendid = friendid;
    }
}
