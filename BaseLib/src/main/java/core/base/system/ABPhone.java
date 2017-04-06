package core.base.system;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.HashMap;

import core.base.log.L;
import core.base.utils.ABTextUtil;

/**
 *
 */
public class ABPhone {

    private static final String[] CONTACTOR_ION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    /**
     * 拨打电话，直接拨打，需要权限
     *
     * @param context
     * @param phoneNumber
     */
    public static void call(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }

    /**
     * 拨打电话，跳转到拨号界面
     *
     * @param context
     * @param phoneNumber
     */
    public static void callDial(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    /**
     * 发送短信息，跳转到发送短信界面
     *
     * @param context
     * @param phoneNumber
     * @param content
     */
    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (ABTextUtil.isEmpty(phoneNumber) ? "" : phoneNumber));
        //Uri uri = Uri.parse("smsto:"); //不填写收件人
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", ABTextUtil.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    /**
     * 获取1366个联系人只需0.5秒，读取本地联系人非常快！性能优化
     *
     *遍历方法
     Set<Map.Entry> entries = map.entrySet();
     for (Map.Entry entry : entries) {
     Object key=entry.getKey();//phone
     Object value=entry.getValue();//name
     }
     *
     * @param context
     * @return 电话号码
     */
    public static HashMap getContacts(Context context) {
        HashMap map = new HashMap();
        Cursor phones = null;
        ContentResolver cr = context.getContentResolver();
        try {
            phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, CONTACTOR_ION, null, null, "sort_key");
            if (phones != null) {
                final int contactIdIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                final int displayNameIndex = phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneString, displayNameString, contactIdString;
                while (phones.moveToNext()) {
                    phoneString = phones.getString(phoneIndex);
                    displayNameString = phones.getString(displayNameIndex);
                    contactIdString = phones.getString(contactIdIndex);
//                    L.e("GetPhoneContactUtils", "电话=" + phoneString);
                    L.e("GetPhoneContactUtils", "名字=" + displayNameString);
//                    L.e("GetPhoneContactUtils", "联系人id=" + contactIdString);
                    map.put(phoneString, displayNameString);
                }
                L.e("GetPhoneContactUtils", "联系人总数=" + map.size());
            }
        } catch (Exception e) {
            L.e("GetPhoneContactUtils", e.getMessage());
        } finally {
            if (phones != null)
                phones.close();
        }

        return map;
    }


    /**
     * 把数据写入到系统的联系人 需要权限
     * @param context
     * @param name
     * @param phone
     */
    public static void insertContacts(Context context,String name,String phone) {

        // 把数据写入到系统的联系人.
        ContentResolver resolver = context.getContentResolver();
        // ----------在raw_contant表中添加一条新的id---------------
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        // 插入联系人 必须要知道 新的联系人的id
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, "contact_id");
        int contact_id;
        if (cursor.moveToLast()) {
            contact_id = cursor.getInt(0) + 1; // 数据库里面有数据 最后一条联系人的id + 1
        } else {// 原先数据库是空的 从第一个联系人开始
            contact_id = 1;
        }
        ContentValues values = new ContentValues();
        values.put("contact_id", contact_id);
        resolver.insert(uri, values);

        // ------------在data表里面 添加id对应的数据-------------
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        // 插入姓名
        ContentValues nameValue = new ContentValues();
        nameValue.put("data1", name);
        nameValue.put("raw_contact_id", contact_id);
        nameValue.put("mimetype", "vnd.android.cursor.item/name");
        resolver.insert(dataUri, nameValue);

        // 插入电话
        ContentValues phoneValue = new ContentValues();
        phoneValue.put("data1", phone);
        phoneValue.put("raw_contact_id", contact_id);
        phoneValue.put("mimetype", "vnd.android.cursor.item/phone_v2");
        resolver.insert(dataUri, phoneValue);

        L.e("GetPhoneContactUtils", "插入数据成功="+name);

    }
}
