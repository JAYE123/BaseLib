package core.base.utils;

/**
 * Created by zoe on 15/1/6.
 */
public class Constants {
    /**
     * Unknown network class
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;

    /**
     * wifi net work
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * "2G" networks
     */
    public static final int NETWORK_CLASS_2_G = 2;

    /**
     * "3G" networks
     */
    public static final int NETWORK_CLASS_3_G = 3;

    /**
     * "4G" networks
     */
    public static final int NETWORK_CLASS_4_G = 4;
    public static final class Regex{
        public static final String EMAIL_REG = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        /**
         * 手机号
         */
        public static final String MOBILE_REG = "^((1[0-9][0-9]))\\d{8}$";
        /**
         * 任意数字
         */

        public static final String ISNUMBER= "^[0-9]*$";
        /**
         * 密码 任意数字，字母
         */

        public static final String PASSWORD="^[0-9a-zA-Z_-]{6,20}$";
        /**
         * 生日
         */
        public static final String BIRTHDAY="([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
        /**
         * 长度在4~10之间，只能输入数字跟英文。
         */
        public static final String SHENBIANID="^[a-zA-Z0-9]{4,10}$";
        /**
         * 邀请码 只能 数字或大写字母
         */
        public static final String INVITE_CODE="^[0-9A-Z]{10}$";
        /**
         * 微信号，以字母开头，可以使用下划线，减号
         */
        public static final String WEIXIN="^(?i)([a-zA-Z][A-Za-z0-9_-]{5,19})$";
        /**
         * 真实姓名 ,只能输入汉字 ，长度2~20
         */
        public static final String REAL_NAME="^([\u4e00-\u9fa5]{2,20})|([a-zA-Z]{6,20})$";
        /**
         *  18身份证
         */
        public static final String IDENTITY_CARD="^([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x))|([A-Z]{1}[0-9]{6}\\(([0-9]{1}|A)\\))|([1-9]{1}[0-9]{6}\\([0-9]{1}\\))|(^[A-Z]{1}[0-9]{8,10})$";
        /**
         * 微信名,任何空白字符：空格，制表符、换页符，任何非空白字符，包含中文，字母，数字，长度1~32位
         */

        public static final String WEIXIN_NAME="[\\da-zA-Z\u4e00-\u9fa5]{1,32}";//(\\W\\w{1,50}) 未使用
        /**
         *  用户名
         */
        public static final String USER_NAME="^(?i)([a-zA-Z]{1}[_0-9a-zA-Z-]{5,19})$";
        /**
         * 中能输入中文或英文
         */
        public static final String TAOBAO_NAME="([\u4e00-\u9fa5]{2,32})|([a-zA-Z]{2,32})";

        /**
         *  QQ号码
         */
        public static final String QQ_NUM = "^[0-9]{5,12}$";
//	//匹配 任何字符。不包含中文，长度5,32位
//	public static final String WANGWANG="^[a-zA-Z0-9_\\W][^\u4e00-\u9fa5]{4,24}$";
        //有效的网址链接
	/*
	 *1 该正则表达式匹配的字符串必须以http://、https://、ftp://开头；

	2：该正则表达式能匹配URL或者IP地址；（如：http://www.baidu.com 或者 http://192.168.1.1）

	3：该正则表达式能匹配到URL的末尾，即能匹配到子URL；（如能匹配：http://www.baidu.com/s?wd=a&rsv_spt=1&issp=1&rsv_bp=0&ie=utf-8&tn=baiduhome_pg&inputT=1236）

	4：该正则表达式能够匹配端口号；
	 */
        /**
         *  旺旺号 50字符以内
         */
        public static final String WANGWANG_NUM = ".{1,50}";
        public static final String URL="((http|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        /**
         *   淘宝店铺链接
         */
        public static final String TAOBAO_URL="^(?i)((http://){0,1}([A-Za-z0-9-_]+).taobao.com)$";
        /**
         * 公司名
         */
        public static final String COMPANY="([\u4e00-\u9fa5]{2,32})|([a-zA-Z]{2,32})";
        /**
         *  UUID 一个128位长的数字，一般用16进制表示。
         *算法的核心思想是结合机器的网卡、当地时间、一个随机数来生成GUID。从理论上讲，如果一台机器每秒产生10000000个GUID，则可以保证（概率意义上）3240年不重复。
         */
        public static final String UUID="^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

        /**
         * 匹配中英文数字 空格
         */
        public static final String CHINESE_ABC_NUM_SPACE="^[\u4e00-\u9fa5a-zA-Z0-9\\s]{1,128}$";
        /**
         *  匹配中英文数字
         */
        public static final String CHINESE_ABC_NUM="^[\u4e00-\u9fa5a-zA-Z0-9]{1,128}$";
        /**
         * 匹配中文和问号
         */
        public static final String CHINESE_QUESTION="^[\u4e00-\u9fa5\\?]+$";
        /**
         *  电话
         */
        public static final String TELEPHONE="^[0-9-]{6,15}$";
        /**
         * 匹配数字和逗号
         */
        public static final String NUM_COMMA="^[0-9,]{1,256}$";
        /**
         * 匹配中文
         */
        public static final String CHINESE="^[\u4e00-\u9fa5]{1,64}$";
        /**
         * 匹配中英文 数字 下划线。
         */
        public static final String CHINESE_ABC_NUM_UNDERLINE="^[\u4e00-\u9fa5a-zA-Z0-9_]{1,64}$";
    }


}
