package core.base.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.format.Formatter;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import core.base.application.ABApplication;
import core.base.log.L;
import core.base.time.ABTimeUtil;

import static android.R.attr.path;

/**
 * 文件操作类
 */
public class ABFileUtil {

    public static final String TAG = ABFileUtil.class.getSimpleName();

    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();

    public static String getSDPATH() {
        return SD_CARD_PATH;
    }

    public static File obtainDirF(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String obtainDirS(String path) {
        return obtainDirF(path).getAbsolutePath();
    }


    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File creatSDFile(String fileRelativePath) throws IOException {
        File file = new File(SD_CARD_PATH + fileRelativePath);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirRelativePath
     */
    public static File creatSDDir(String dirRelativePath) {
        File dir = new File(SD_CARD_PATH + dirRelativePath);
        dir.mkdirs();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String fileRelativePath) {
        File file = new File(SD_CARD_PATH + fileRelativePath);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File write2SDFromInput(String relativePath, String fileName, InputStream input) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(relativePath);
            file = creatSDFile(relativePath + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(output);
        }
        return file;
    }

    /**
     * 先质量压缩到90%，再把bitmap保存到sd卡上
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @return
     * @author com.tiantian
     */
    public static int saveBitmap2SD(String relativePath, String fileName, Bitmap bm) {
        return saveBitmap2SD(relativePath, fileName, bm, 90);
    }

    /**
     * 先质量压缩到指定百分比（0% ~ 90%），再把bitmap保存到sd卡上
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @param quality
     * @return
     */
    public static int saveBitmap2SD(String relativePath, String fileName, Bitmap bm, int quality) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        FileOutputStream out = null;
        try {
            creatSDDir(relativePath);
            file = creatSDFile(relativePath + fileName);
            out = new FileOutputStream(file.getPath());
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            closeIO(out);
        }
    }

    /**
     * 先质量压缩到指定百分比（0% ~ 90%），再把bitmap保存到sd卡上
     *
     * @param filePath
     * @param bm
     * @param quality
     * @return
     */
    public static int saveBitmap2SDAbsolute(String filePath, Bitmap bm, int quality) {
        File file = null;
        FileOutputStream out = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file.getPath());
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            closeIO(out);
        }
    }


    /**
     * 压缩图片直到容量小于200kb，并保存到sdcard
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @return
     */
    public static int saveBitmap2SDWithCapacity(String relativePath, String fileName, Bitmap bm) {
        return saveBitmap2SDWithCapacity(relativePath, fileName, bm, 200);
    }

    /**
     * 压缩图片直到容量小于指定值(kb)，并保存到sdcard
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @param capacity
     * @return
     */
    public static int saveBitmap2SDWithCapacity(String relativePath, String fileName, Bitmap bm, int capacity) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        FileOutputStream out = null;
        ByteArrayInputStream bais = null;
        try {
            creatSDDir(relativePath);
            file = creatSDFile(relativePath + fileName);
            out = new FileOutputStream(file.getPath());
//            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            bais = compressImage(bm, capacity);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bais.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            closeIO(out, bais);
        }

    }

    /**
     * 压缩图片直到容量小于指定值(kb)
     *
     * @param image
     * @param capacity
     * @return
     */
    public static ByteArrayInputStream compressImage(Bitmap image, int capacity) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > capacity) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if (options < 10) {
                break;
            }
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        baos.reset();
        return bais;
    }


    /**
     * 把uri转为File对象
     *
     * @param context
     * @param uri
     * @return
     */
    public static File uri2File(Context context, Uri uri) {
        // 而managedquery在api 11 被弃用，所以要转为使用CursorLoader,并使用loadInBackground来返回
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
            cursor = loader.loadInBackground();
            if (null == cursor) {
                return null;
            }
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        } catch (Exception ex) {
            L.e(TAG, ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static File uri2FileInteral(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        File file = uri2File(context, uri);
        return null == file ? new File(uri.getPath()) : file;
    }


    public static void openFile(Context context, String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMimeType(path);
        //参数二type见下面
        intent.setDataAndType(Uri.fromFile(new File(path)), type);
        context.startActivity(intent);
    }

    public static String getMimeType(String uri) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void deleteFile(File... files) {
        if (!ABTextUtil.isEmpty(files)) {
            for (File file : files) {
                try {
                    file.delete();
                } catch (RuntimeException e) {
                    L.e(TAG, e);
                }
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFolder(File file) {
        if (file.exists() && file.isDirectory()) {//判断是文件还是目录
            if (file.listFiles().length > 0) {//若目录下没有文件则直接删除
                //若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = file.listFiles();
                int i = file.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        deleteFolder(delFile[j]);//递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();//删除文件  
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件夹
     *
     * @param filePath
     */
    public static void deleteFolder(String filePath) {
        File file = new File(filePath);
        deleteFolder(file);
    }


    /**
     * 从文件中读取Byte数组
     * Add by Hubert
     *
     * @param file
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // 获取文件大小
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // 文件太大，无法读取
            throw new IOException("File is to large " + file.getName());
        }

        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int) length];

        // 读取数据到byte数组中
        int offset = 0;

        int numRead = 0;

        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // 确保所有数据均被读取
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();

        return bytes;
    }

    public static byte[] getBytesFromInputStream(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        return outStream.toByteArray();
    }

    public static File getFileFromInputStream(File file, InputStream ins) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }

    /**
     * 获取拍照的图片路径
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getDCIMFile() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 扫描目录（扫描后可以及时在图库中看到）
     *
     * @param context
     * @param path
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void scanFile(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }

    /**
     * 获取项目根路径
     *
     * @param context
     * @param applicationDir
     * @return
     */
    public static String getApplicationPath(Context context, String applicationDir) {
        // check whether the device has external storage
        if (ABAppUtil.haveSDCard()) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString()
                    + File.separator + applicationDir + File.separator;
            L.d(TAG, "have sdcard! sdcard path: " + sdcardPath);
            return sdcardPath;
        } else {
            String dirPath = context.getCacheDir().getAbsoluteFile() + File.separator;
            L.d(TAG, "have no sdcard! dir path: " + dirPath);
            return dirPath;
        }
    }

    /**
     * 获取一个文件对象，如果不存在，则自动创建
     *
     * @param filePath
     * @return
     */
    public static File getFileAutoCreated(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            L.e(TAG, "[getFileAutoCreated]file[" + filePath + "] is directory!");
            return file;
        }
        if (file.exists()) {
            return file;
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            L.e(TAG, e);
        }
        return file;
    }

    /**
     * 获取一个目录对象，如果不存在，则自动创建
     *
     * @param dirPath
     * @return
     */
    public static File getDirAutoCreated(String dirPath) {
        File dirFile = new File(dirPath);
        if (dirFile.isFile()) {
            L.e(TAG, "[getDirAutoCreated]file[" + dirPath + "] is file!");
            return dirFile;
        }
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }

    /**
     * 文件转化为Object
     *
     * @param fileName 文件全部路径
     * @return Object null 读取失败
     * 实现序列化的对象也可以写入本地
     * implements Serializable
     */
    public static Object file2Object(String fileName) {

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 把Object输出到文件
     *
     * @param obj        要保存的对象，可以是List<String>这种列表对象
     * @param outputFile 输出文件的绝对路径
     */
    public static void object2File(Object obj, String outputFile) {
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(outputFile));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取单个文件的大小or获取目录包括包含的文件的总大小
     *
     * @param directory 文件获取目录
     * @return
     */
    public static long getDirectorySize(File directory) {
        if (!directory.exists()) return 0;
        if (directory.isDirectory()) {
            long directorySize = 0;
            for (File file : directory.listFiles()) {
                directorySize += getDirectorySize(file);
            }
            return directorySize;
        } else {
            return directory.length();
        }
    }

    /**
     * 清空某个目录下的所有文件和文件夹
     *
     * @param directory
     */
    public static void clearDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.exists() && file.isFile()) {
                    file.delete();
                } else if (file.exists() && file.isDirectory()) {
                    clearDirectory(file);
                    file.delete();
                }
            }
        }
    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                L.e(TAG, "close IO ERROR...", e);
            }
        }
    }

    /**
     * recyle bitmaps
     *
     * @param bitmaps
     */
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (ABTextUtil.isEmpty(bitmaps)) {
            return;
        }

        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }


    /**
     * 复制文件
     *
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            L.e(TAG, "file(from) is null or is not exists!!");
            return;
        }
        if (null == to) {
            L.e(TAG, "file(to) is null!!");
            return;
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);

            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = is.read(buffer))) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            L.e(TAG, e);
        } finally {
            closeIO(is, os);
        }


    }

    /**
     * 从文件中读取文本
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        StringBuilder resultSb = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            L.e(TAG, e);
        }
        return inputStream2String(is);
    }

    public static String readFile(File file) {
        return readFile(file.getPath());
    }

    /**
     * 从assets中读取文本
     *
     * @param name
     * @return
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            L.e(TAG, e);
        }
        return inputStream2String(is);

    }

    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
            L.e(TAG, ex);
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    /**
     * 写文本到文件
     *
     * @param path
     * @param content
     * @return
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static int writeFile(String path, String content) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(content.getBytes(Charset.forName("UTF-8")));
            os.flush();
            return 0;
        } catch (Exception e) {
            L.e(TAG, e);
        } finally {
            closeIO(os);
        }
        return -1;
    }

    public static int writeFile(File file, String content) {
        return writeFile(file.getPath(), content);
    }

    public static String formatFileSize(long directorySize) {
        return Formatter.formatFileSize(ABApplication.getInstance(), directorySize);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp, String fileDir) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), fileDir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = ABTimeUtil.millisToStringDate(System.currentTimeMillis()) + ".jpg";

        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
}