package core.base.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @作者: XQ
 * @创建时间：15-7-20 下午10:08
 * @类说明:登录控制类
 */
public class LoginControl {
    public static SharedPreferences login_sp = null;
    protected static String PRFS_LOGIN_TOKEN = "token";
    protected static String PRFS_LOGIN_USERNAME = "username";
    protected static String PRFS_LOGIN_USERPSW = "userpsw";
    protected static String PRFS_LOGIN_USERPSWSCORE = "userpswscore";
    protected static String PRFS_LOGIN_USERID = "userid";
    protected static String SEND_POINT_ID = "deliveryId";
    protected static String userToken;
    protected static String userName;
    protected static String userPsw;
    protected static String userPswScore;
    protected static int userId;
    protected static int sendPointId;

    public static synchronized void init(Context context) {
        if (login_sp == null) {
            login_sp = context.getSharedPreferences(LoginControl.class.getName(), Context.MODE_PRIVATE);
        }
    }

    public static void saveToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            userToken = token;
            login_sp.edit().putString(PRFS_LOGIN_TOKEN, token).commit();
        }
    }

    public static void saveUserid(int userid) {
        if (userid > 0) {
            userId = userid;
            login_sp.edit().putInt(PRFS_LOGIN_USERID, userid).commit();
        }
    }

    public static int getUserId() {
        userId = login_sp.getInt(PRFS_LOGIN_USERID, 0);
        return userId;
    }

    public static void saveIsGuide(boolean isGuide) {
        login_sp.edit().putBoolean("isGuide", isGuide).commit();
    }

    public static boolean getIsGuide() {
        return login_sp.getBoolean("isGuide", false);
    }

    public static void saveIntoAppNum(int number) {
        login_sp.edit().putInt("appNum", number).commit();
    }

    public static int getIntoAppNum() {
        return login_sp.getInt("appNum", 0);
    }

    public static void saveSendPointId(int id) {
        if (id > 0) {
            sendPointId = id;
            login_sp.edit().putInt(SEND_POINT_ID, id).commit();
        }
    }

    public static int getSendPointId() {
        sendPointId = login_sp.getInt(SEND_POINT_ID, 0);
        return sendPointId;
    }


    public static String getToken() {
        userToken = login_sp.getString(PRFS_LOGIN_TOKEN, "");
//        L.e("LoginControl--getToken=" + userToken);
        return userToken;
    }


    public static void saveUserName(String username) {
        if (!TextUtils.isEmpty(username)) {
            userName = username;
            login_sp.edit().putString(PRFS_LOGIN_USERNAME, username).commit();
        }
    }

    public static void saveUserPassword(String userPassword) {
        if (!TextUtils.isEmpty(userPassword)) {
            userPsw = userPassword;
            login_sp.edit().putString(PRFS_LOGIN_USERPSW, userPassword).commit();
        }
    }

    /**
     * 积分商城用的加密字符串
     *
     * @param userPasswordScore
     */
    public static void saveUserPswScore(String userPasswordScore) {
        if (!TextUtils.isEmpty(userPasswordScore)) {
            userPswScore = userPasswordScore;
            login_sp.edit().putString(PRFS_LOGIN_USERPSWSCORE, userPasswordScore).commit();
        }
    }


    public static String getUserName() {
        userName = login_sp.getString(PRFS_LOGIN_USERNAME, "");
        return userName;
    }

    public static String getUserPassword() {
        userPsw = login_sp.getString(PRFS_LOGIN_USERPSW, "");
        return userPsw;
    }

    /**
     * @return 积分商城用的加密字符串
     */
    public static String getUserPswScore() {
        userPswScore = login_sp.getString(PRFS_LOGIN_USERPSWSCORE, "");
        return userPswScore;
    }


    //false 不为空
    public static boolean isLogin() {
        if (TextUtils.isEmpty(getToken())) {
            return false;
        } else {
            return true;
        }
    }

    public static void clearLogin() {
        userToken = null;
        userId = -1;
        userName = null;
        userPsw = null;
        userPswScore = null;
        sendPointId = -1;
        if (login_sp != null) {
            if (login_sp.edit() != null) {
                login_sp.edit().clear().commit();
            }
        }
    }
}
