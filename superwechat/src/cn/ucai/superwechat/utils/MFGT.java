package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.easeui.domain.User;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.GuideActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.ProfileActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SettingsActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;

/**
 * Created by clawpo on 2017/5/19.
 */

public class MFGT {

    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    private static void startActivity(Context context, Class clazz){
        context.startActivity(new Intent(context, clazz));
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    private static void startActivity(Context context, Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoGuide(Activity activity) {
        startActivity(activity,GuideActivity.class);
    }

    public static void gotoLogin(Activity activity) {
        startActivity(activity,LoginActivity.class);
    }

    public static void gotoMain(Activity activity) {
        startActivity(activity, MainActivity.class);
    }

    public static void gotoRegister(Activity activity) {
        startActivity(activity, RegisterActivity.class);
    }

    public static void gotoSettings(Activity activity) {
        startActivity(activity,SettingsActivity.class);
    }

    public static void loguot(Activity activity) {
        startActivity(activity,new Intent(activity,LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    public static void gotoProfile(Activity activity) {
        startActivity(activity, UserProfileActivity.class);
    }

    public static void gotoProfile(Activity activity, User user) {
        startActivity(activity,new Intent(activity, ProfileActivity.class)
        .putExtra(I.User.TABLE_NAME,user));
    }
}
