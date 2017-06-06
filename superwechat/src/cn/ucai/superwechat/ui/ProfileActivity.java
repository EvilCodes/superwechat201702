package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.Constant;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.data.OnCompleteListener;
import cn.ucai.superwechat.data.Result;
import cn.ucai.superwechat.data.net.IUserModel;
import cn.ucai.superwechat.data.net.UserModel;
import cn.ucai.superwechat.db.UserDao;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;

/**
 * Created by clawpo on 2017/5/25.
 */

public class ProfileActivity extends BaseActivity {
    private static final String TAG = "ProfileActivity";
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.tv_userinfo_nick)
    TextView mTvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView mTvUserinfoName;
    @BindView(R.id.btn_add_contact)
    Button mBtnAddContact;
    @BindView(R.id.btn_send_msg)
    Button mBtnSendMsg;
    @BindView(R.id.btn_send_video)
    Button mBtnSendVideo;
    User user = null;
    String username;
    IUserModel model;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        super.onCreate(arg0);
        model = new UserModel();
        initData();
        showLeftBack();
    }

    private void initData() {
        username = getIntent().getStringExtra(I.User.USER_NAME);
        user = (User) getIntent().getSerializableExtra(I.User.TABLE_NAME);
        if (username==null && user==null){
            finish();
            return;
        }
        if (user==null){
            user = SuperWeChatHelper.getInstance().getAppContactList().get(username);
        }
        if (user==null  && username.equals(EMClient.getInstance().getCurrentUser())){
            user = SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAppUserInfo();
        }
        if (user!=null){
            showInfo();
        }else{
            showUser();
        }
        syncUserInfo();
    }

    private void showInfo() {
        mTvUserinfoName.setText(user.getMUserName());
        EaseUserUtils.setAppUserNick(user,mTvUserinfoNick);
        EaseUserUtils.setAppUserAvatar(ProfileActivity.this,user,mProfileImage);
        showButton(SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName()));
    }

    private void showButton(boolean isContact) {
        if (!user.getMUserName().equals(EMClient.getInstance().getCurrentUser())) {
            mBtnAddContact.setVisibility(isContact ? View.GONE : View.VISIBLE);
            mBtnSendMsg.setVisibility(isContact ? View.VISIBLE : View.GONE);
            mBtnSendVideo.setVisibility(isContact ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick(R.id.btn_add_contact)
    public void sendAddContactMsg(){
        MFGT.gotoSendMsg(ProfileActivity.this,user.getMUserName());
    }

    @OnClick(R.id.btn_send_msg)
    public void onSendMsg(){
        MFGT.gotoChat(ProfileActivity.this,user.getMUserName());
    }

    @OnClick(R.id.btn_send_video)
    public void onSendVideo(){
        MFGT.gotoVideo(ProfileActivity.this,user.getMUserName());
    }

    private void syncUserInfo(){
        model.loadUserInfo(ProfileActivity.this, username, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                boolean isSuccess = false;
                if (s!=null){
                    Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                    if (result!=null && result.isRetMsg()){
                        isSuccess = true;
                        user = result.getRetData();
                    }
                }
                if (!isSuccess){
                    showUser();
                }else{
                    showInfo();
                    saveUser2DB();
                }
            }

            @Override
            public void onError(String error) {
                showUser();
            }
        });
    }

    private void saveUser2DB() {
        if (SuperWeChatHelper.getInstance().getAppContactList().containsKey(username)) {
            // save contact
            UserDao userDao = new UserDao(ProfileActivity.this);
            userDao.saveAppContact(user);
            SuperWeChatHelper.getInstance().getAppContactList().put(user.getMUserName(), user);
            sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
        }
    }

    private void showUser(){
        mTvUserinfoName.setText(username);
        EaseUserUtils.setAppUserNick(username,mTvUserinfoNick);
        EaseUserUtils.setAppUserAvatar(ProfileActivity.this,username,mProfileImage);
    }

}
