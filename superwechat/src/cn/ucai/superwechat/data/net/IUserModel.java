package cn.ucai.superwechat.data.net;

import android.content.Context;

import java.io.File;

import cn.ucai.superwechat.data.OnCompleteListener;

/**
 * Created by clawpo on 2017/5/19.
 */

public interface IUserModel {
    void register(Context context, String username, String nickname, String password,
                  OnCompleteListener<String> listener);

    void unRegister(Context context, String username, OnCompleteListener<String> listener);

    void loadUserInfo(Context context,String username,OnCompleteListener<String> listener);

    void updateAvatar(Context context, String username, String avatarType,
                      File file, OnCompleteListener<String> listener);

    void updateNick(Context context,String username,String nickname,
                    OnCompleteListener<String> listener);

    void addContact(Context context,String username,String cname,OnCompleteListener<String> listener);

    void deleteContact(Context context,String username,String cname,OnCompleteListener<String> listener);

    void loadContact(Context context,String username,OnCompleteListener<String> listener);

    void createGroup(Context context,String hxid,String name,String des,String owner,
                     boolean isPublic,boolean isInviets,File file,OnCompleteListener<String> listener);
}
