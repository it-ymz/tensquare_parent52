package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.Nofriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid,String friendid){
        //先判断userid到friendid是否有数据，有就是重复添加好友，返回0
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(friend!=null){
            return 0;
        }
        //直接添加好友，让好友表中的userid到friendid方向的type为0
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //判读从friendid到userid是否有数据，如果有，把双方的状态都改为1
        if(friendDao.findByUseridAndFriendid(friendid,userid)!=null){
            //通过查询，在friendid查询userid，当返回的 值不为空，那么认为就是friend也关注了userid
            //需要将islike都改为1
            friendDao.updateIsLike("1",userid,friendid);
            friendDao.updateIsLike("1",friendid,userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        //先判断已经是非好友
        Nofriend nofriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if(nofriend!=null){
            return 0;
        }
        nofriend = new Nofriend();
        nofriend.setFriendid(friendid);
        nofriend.setUserid(userid);
        noFriendDao.save(nofriend);
        return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除好友表中userid到friendid这条数据
        friendDao.deleteFriend(userid,friendid);
        //更新friendid到userid的islike为0
        friendDao.updateIsLike("0",friendid,userid);
        //非好友表中添加数据
        Nofriend noFriend = new Nofriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
