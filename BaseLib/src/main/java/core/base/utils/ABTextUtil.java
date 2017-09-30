package core.base.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import core.base.log.L;

/**
 * 文字编辑、处理、判断工具
 */
public class ABTextUtil {
    private static final String TAG = ABTextUtil.class.getSimpleName();

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return str == null || str.equals("");
    }

    /**
     * 判断是否为空
     *
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    /**
     * 判断是否全部有字母和数字组成
     *
     * @param name
     * @return
     */
    public static boolean isLimit(String name) {
        // ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
        Pattern p = Pattern.compile("[a-zA-Z0-9_]*");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 隐藏手机号码中间四位
     *
     * @param phone 13711112222
     * @return 137****2222
     */
    public static String getPhoneHide(String phone) {
        String phoneHide = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phoneHide;
    }

    /**
     * 判断是否为身份证号码
     */
    public static boolean isIDCard(String idCard) {
        if (isIDCard1(idCard) || isIDCard2(idCard)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断车牌号
     *
     * @param carID
     * @return
     */
    public static boolean isCarId(String carID) {
        // ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
        Matcher m = p.matcher(carID);
        return m.matches();
    }

    /**
     * 判断是否是15位身份证号码
     *
     * @return
     */
    public static boolean isIDCard1(String idCard) {
        // ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
        Pattern p = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
        Matcher m = p.matcher(idCard);
        return m.matches();
    }

    /**
     * 判断是否是18位身份证号码
     *
     * @return
     */
    public static boolean isIDCard2(String idCard) {
        // ^.[a-zA-Z]\w{m,n}$ 由m-n位的字母数字或下划线组成
        Pattern p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
        Matcher m = p.matcher(idCard);
        return m.matches();
    }

    /**
     * 判断是否是联系方式(手机号码+固话)
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNumber(String mobiles) {
        if (isMobileNumber(mobiles) || isTelNumber(mobiles)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    // /**
    // * 判断是否为手机号码且不为空
    // */
    // public static boolean isMobileNumberAndNoEmpty(Activity activity, String mobiles) {
    // if (isEmpty(mobiles)) {
    // T.s(activity, "请输入手机号");
    // return false;
    // } else if (!isMobileNumber(mobiles)) {
    // T.s(activity, "请输入正确的手机号码");
    // return false;
    // } else {
    // return true;
    // }
    //
    // }

    /**
     * 格式化double成两位小数
     *
     * @param number String或者double
     * @return
     */
    public static String formatDoubleTo2(Object number) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(number);
        } catch (Exception e) {
            L.e("格式化出错：" + e.getMessage());
        }
        return "0.00";
    }

    public static BigDecimal createBigDecimal(String number) {
        try {
            BigDecimal bigDecimal = new BigDecimal(number);
            return bigDecimal;
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG, "BigDecimal格式化错误了：" + number);
        }
        return new BigDecimal("0.00");
    }

    public static BigDecimal createBigDecimal(double number) {
        try {
            BigDecimal bigDecimal = new BigDecimal(number);
            return bigDecimal;
        } catch (Exception e) {
            L.e(TAG, "BigDecimal格式化错误了：" + number);
            e.printStackTrace();
        }
        return new BigDecimal("0.00");
    }

    public static void setText(EditText editText, Object object) {
        editText.setText(object == null ? "" : object.toString());
    }

    public static String getNoNullText(Object object, String defaultStr) {
        return object == null ? defaultStr : object.toString();
    }

    /**
     * 判断是否是固定电话号码
     *
     * @param telPhone
     * @return
     */
    public static boolean isTelNumber(String telPhone) {
        Pattern p = Pattern.compile("(\\d{3,4}-)\\d{6,8}");
        Matcher m = p.matcher(telPhone);
        return m.matches();
    }

    /**
     * 从米获取距离
     *
     * @return
     */
    public static String getDistance(int dis) {
        if (dis < 0)
            return "未知";

        String resu;
        // if (dis>=1000000) {
        // double Kdist = dis/1000;
        // // resu = (int)Kdist+ "k千米";
        // resu = (int)Kdist + "千米";
        // } else
        if (dis >= 1000) {
            double Kdist = dis / 1000.0;
            resu = String.format("%.2f", Kdist) + "千米";// 保留两位小数
        } else {
            resu = dis + "米";// 原始距离
        }

        return resu;
    }

    /**
     * 判断email格式是否正确
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * String转换到int
     *
     * @param str
     * @return
     */
    public static int String2int(String str, int defvalue) {
        int tem = defvalue;
        try {
            tem = Integer.parseInt(str);
        } catch (Exception e) {
        }
        return tem;
    }

    /**
     * 格式化doubel到2位小数
     *
     * @param value
     * @return
     */
    public static String double2FormatString(double value) {
        return String.format("%.2f", value);
    }

    /**
     * 判断是否是邮编
     */
    public static boolean isPostcode(String postcode) {

        String str = "^[1-9]\\d{5}(?!\\d)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(postcode);
        return m.matches();
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 返回TextView长度
     *
     * @param tv
     * @return
     */
    public static float measureTextLength(TextView tv) {
        Paint paint = new Paint();
        paint.setTextSize(tv.getTextSize());
        return paint.measureText(tv.getText() + "");
    }

    /**
     * 是否是一个完整的url
     *
     * @param url
     * @return
     */
    public static boolean isWebUrl(String url) {
        if (url == null)
            return false;
        // String pattern =
        // "^((http://)|(https://)|(ftp://)).*?.(html|php|jsp|asp|shtml|xhtml)";
        // return Pattern.matches(pattern, url);
        return Patterns.WEB_URL.matcher(url).matches();//true符合标准  false不符合标准
    }

    /**
     * 是否是一个完整的图片url
     *
     * @param url
     * @return
     */
    public static boolean isImg(String url) {
        String pattern = "^((http://)|(https://)|(ftp://)).*?.(png|jpg|jpeg|gif|bmp)";

        return Pattern.matches(pattern, url);
    }

    //四舍五入保留两位小数
    public static double getDouble(double sourceData, String sf) {
        DecimalFormat df = new DecimalFormat(sf);
        String str = df.format(sourceData);
        return Double.parseDouble(str);
    }

    public static boolean isBankCard(String mobiles) {
        Pattern p = Pattern.compile("^(\\d{16}|\\d{19})$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String getUrlParamValue(String url, String paramName) {
        if (url == null) return null;
        int index = url.indexOf(paramName + "=");
        String substring = null;
        if (index >= 0) {
            substring = url.substring(index);
            int i = substring.indexOf("&");
            if (i >= 0) {
                substring = substring.substring(0, i);
            }
            substring = substring.substring(substring.indexOf("=") + 1);
        }
        return substring;
    }

    public static String formatMobileHide4(String mobile) {
        if (TextUtils.isEmpty(mobile)) return "";
        return mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
    }

    /**
     * 获得字体的缩放密度
     *
     * @param context
     * @return
     */
    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }

    /**
     * *************************************************************
     */

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] obj) {
        return null == obj || obj.length <= 0;
    }

    public static boolean isEmpty(int[] obj) {
        return null == obj || obj.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }

    public static boolean isBlank(CharSequence charSequence) {
        return null == charSequence || charSequence.toString().trim().length() <= 0;
    }

    public static boolean isLeast(Object[] obj, int count) {
        return null != obj && obj.length >= count;
    }

    public static boolean isLeast(int[] objs, int count) {
        return null != objs && objs.length >= count;
    }

    public static boolean isEquals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    public static String trim(CharSequence charSequence) {
        return null == charSequence ? null : charSequence.toString().trim();
    }

    /**
     * 摘取里面第一个不为null的字符串
     *
     * @param options
     * @return
     */
    public static String pickFirstNotNull(CharSequence... options) {
        if (isEmpty(options)) {
            return null;
        }
        String result = null;
        for (CharSequence cs : options) {
            if (null != cs) {
                result = cs.toString();
                break;
            }
        }
        return result;
    }

    /**
     * 摘取里面第一个不为null的字符串
     *
     * @param options
     * @return
     */
    @SafeVarargs
    public static <T> T pickFirstNotNull(Class<T> clazz, T... options) {
        if (isEmpty(options)) {
            return null;
        }
        T result = null;
        for (T obj : options) {
            if (null != obj) {
                result = obj;
                break;
            }
        }
        return result;
    }

    /**
     * 替换文本为图片
     *
     * @param charSequence
     * @param regPattern
     * @param drawable
     * @return
     */
    public static SpannableString replaceImageSpan(CharSequence charSequence, String regPattern, Drawable drawable) {
        SpannableString ss = charSequence instanceof SpannableString ? (SpannableString) charSequence : new SpannableString(charSequence);
        try {
            ImageSpan is = new ImageSpan(drawable);
            Pattern pattern = Pattern.compile(regPattern);
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                String key = matcher.group();
                ss.setSpan(is, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ex) {
            L.e(TAG, ex);
        }

        return ss;
    }


    /**
     * 压缩字符串到Zip
     *
     * @param str
     * @return 压缩后字符串
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * 解压Zip字符串
     *
     * @param str
     * @return 解压后字符串
     * @throws IOException
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes("UTF-8"));
        return uncompress(in);
    }

    /**
     * 解压Zip字符串
     *
     * @param inputStream
     * @return 解压后字符串
     * @throws IOException
     */
    public static String uncompress(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream unzip = new GZIPInputStream(inputStream);
        byte[] buffer = new byte[256];
        int n;
        while ((n = unzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }

    /**
     * InputStream convert to string
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    /**
     * 解压Gzip获取
     *
     * @param is
     * @return
     */
    public static String inputStream2StringFromGZIP(InputStream is) {
        StringBuilder resultSb = new StringBuilder();
        BufferedInputStream bis = null;
        InputStreamReader reader = null;
        try {
            bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            // Gzip流的前两个字节是0x1f8b
            if (result != -1 && headerData == 0x1f8b) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            while ((readSize = reader.read(data)) > 0) {
                resultSb.append(data, 0, readSize);
            }
        } catch (Exception e) {
            L.e(TAG, e);
        } finally {
            ABFileUtil.closeIO(is, bis, reader);
        }
        return resultSb.toString();
    }

    private static int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    /**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     *
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers) {
        int temp = 0;
        int size = numbers.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }
    }

    //    1.用JAVA自带的函数
    public static boolean isNumeric1(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //    2.用正则表达式
    public static boolean isNumeric2(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //将html特殊字符转移成正常字符
    public static String escapeHtmlText(String str) {
        //微表情心理学&mdash;&middot;&reg;读心识人准到骨子里
        Spanned spanned = Html.fromHtml(str);
        return spanned.toString();
    }
}
