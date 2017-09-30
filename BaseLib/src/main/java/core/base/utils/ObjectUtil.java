package core.base.utils;

import android.content.Context;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil {
    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        String str = null;
        try {
            str = Base64.encodeToString(toByteArray(obj), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 字符串转对象
     *
     * @return
     */
    public static Object toObject(String base64str) {
        Object obj = null;
        try {
            obj = toObject(Base64.decode(base64str, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * Object序列化类
     *
     * @param data
     * @param filename
     * @return
     */
    public static boolean saveObject(Context context, Object data,
                                     String filename) {
        FileOutputStream out;
        ObjectOutputStream outputStream;
        try {
            out = context.openFileOutput(filename + ".odb",
                    Context.MODE_PRIVATE);
            outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(data);
            outputStream.flush();
            out.flush();
            outputStream.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Object反序列化
     *
     * @return
     */
    public static Object readObject(Context context, String filename) {
        FileInputStream in = null;
        ObjectInputStream oin = null;
        Object data = null;
        try {
            in = context.openFileInput(filename + ".odb");
            oin = new ObjectInputStream(in);
            data = oin.readObject();
            oin.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
