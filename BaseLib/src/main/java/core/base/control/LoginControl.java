package core.base.control;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginControl {

    protected static SharedPreferences login_sp = null;
    private static String mToken = "token";

    public static synchronized void init(Context context) {
        if (login_sp == null) {
            login_sp = context.getSharedPreferences(LoginControl.class.getName(), Context.MODE_PRIVATE);
        }
    }

    public static void saveToken(String token) {
        login_sp.edit().putString(mToken, token).apply();
    }

    public static String getToken() {
        return login_sp.getString(mToken, null);
    }

    public static void saveIsGuide(boolean isGuide) {
        login_sp.edit().putBoolean("isGuide", isGuide).apply();
    }

    public static boolean getIsGuide() {
        return login_sp.getBoolean("isGuide", false);
    }

    public static void saveIsBind(int number) {
        login_sp.edit().putInt("isBind", number).apply();
    }

    public static int getIsBind() {
        return login_sp.getInt("isBind", 0);
    }

    public static void saveGoneMessage(long number) {
        login_sp.edit().putLong("message", number).apply();
    }

    public static long getGoneMessage() {
        return login_sp.getLong("message", 0);
    }

    public static void saveUserName(String username) {
        login_sp.edit().putString("username", username).apply();

    }

    public static String getUserName() {
        return login_sp.getString("username", null);
    }

    public static void saveUserPassword(String userPassword) {
        login_sp.edit().putString("password", userPassword).apply();
    }

    public static String getUserPassword() {
        return login_sp.getString("password", null);
    }

    public static void clearLogin() {
        if (login_sp != null) {
            login_sp.edit().clear().apply();
        }
    }

}
