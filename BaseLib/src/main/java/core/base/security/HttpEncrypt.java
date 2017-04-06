package core.base.security;

import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.toolbox.HttpParamsEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import core.base.security.des3.Des3;

public class HttpEncrypt {
	public static void main(String[] args) {

	}

	public static String getAppSign(Map<String, String> params, String md5Pwd) {
		if(params==null) params=new HashMap<>();
		StringBuffer buff = new StringBuffer();
		Set<Entry<String, String>> entrySet = params.entrySet();
		Set<String> tempSet = new TreeSet();
		String sign = "";
		for (Entry<String, String> entry : entrySet) {
			if ("sign".equals(entry.getKey())) {
				sign = entry.getValue();
			} else {
				tempSet.add(entry.getKey() + entry.getValue());
			}
		}
		for (String param : tempSet) {
			buff.append(param).append("|");
		}
		if(md5Pwd!=null&&md5Pwd.length()==32){
			buff.append(md5Pwd.substring(8, 24));
		}
		try {
			return MD5.md5(Des3.encode(buff.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
	}
	public static String getAppSign(HttpParams params, String md5Pwd) {
		if(params==null) params=new HttpParams();
		StringBuffer buff = new StringBuffer();
		ArrayList<HttpParamsEntry> urlParamsMap = params.getUrlParamsMap();
		Set<String> tempSet = new TreeSet();
		String sign = "";
		for (HttpParamsEntry httpParamsEntry : urlParamsMap) {
			if ("sign".equals(httpParamsEntry.k)) {
				sign = httpParamsEntry.v;
			} else {
				tempSet.add(httpParamsEntry.k + httpParamsEntry.v);
			}
		}
		for (String param : tempSet) {
			buff.append(param).append("|");
		}
		if(md5Pwd!=null&&md5Pwd.length()==32){
			buff.append(md5Pwd.substring(8, 24));
		}
//		L.e("hongliang","before_sign="+buff.toString());
		try {
//			String encode = Des3.encode(buff.toString());
//			L.e("hongliang","sign_encode="+encode);
//			sign = MD5.md5(Des3.encode(buff.toString()));
//			L.e("hongliang","sign="+sign);
			return MD5.md5(Des3.encode(buff.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
	}

	/**
	 * 
	 * @param params
	 * @param md5Pwd
	 * @return
	 */
	public static String getSign(Map<String, String[]> params, String md5Pwd) {
		if(params==null) params=new HashMap<>();
		StringBuffer buff = new StringBuffer();
		Set<Entry<String, String[]>> entrySet = params.entrySet();
		Set<String> tempSet = new TreeSet();
		String sign = "";
		for (Entry<String, String[]> entry : entrySet) {
			if ("sign".equals(entry.getKey())) {
				sign = getValue(entry);
			} else {
				tempSet.add(entry.getKey() + getValue(entry));
			}
		}
		for (String param : tempSet) {
			buff.append(param).append("|");
		}
		if(md5Pwd!=null&&md5Pwd.length()==32){
			buff.append(md5Pwd.substring(8, 24));
		}
		try {
			return MD5.md5(Des3.encode(buff.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
	}

	/**
	 * 
	 * @param params
	 * @param md5Pwd
	 * @return
	 */
	public static boolean decode(Map<String, String[]> params, String md5Pwd) {
		String sign = "";
		String[] value = params.get("sign");
		if (value != null && value.length > 0) {
			sign = value[0];
		}
		return sign.equals(getSign(params, md5Pwd));
	}

	public static String getValue(Entry<String, String[]> entry) {
		String[] value = entry.getValue();
		if (value != null && value.length > 0) {
			return value[0];
		}
		return "";
	}

}
