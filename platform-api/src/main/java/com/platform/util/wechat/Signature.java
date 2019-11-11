package com.platform.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.platform.utils.ResourceUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


/**
 * 签名
 *
 * @author snn
 */
public class Signature {

    private static final Logger logger = Logger.getLogger(Signature.class);

    /**
     * 路由新的签名方法，针对甜圆
     * @param map
     * @return
     */
    public static String signMapBuildObject(Map<Object, Object> map) {

        if (CollectionUtils.isEmpty(map)) {
            logger.info("sign param map is null");
        }
        OrderSign os = new OrderSign();
        os.setOpenid(String.valueOf(map.get("openid")));
        os.setAppid(String.valueOf(map.get("appid")));
        os.setMch_id(String.valueOf(map.get("mch_id")));
        os.setNonce_str(String.valueOf(map.get("nonce_str")));
        os.setBody(String.valueOf(map.get("body")));
        os.setOut_trade_no(String.valueOf(map.get("out_trade_no")));
        os.setTotal_fee((String.valueOf( map.get("total_fee"))));
        os.setSpbill_create_ip(String.valueOf(map.get("spbill_create_ip")));
        os.setNotify_url(String.valueOf(map.get("notify_url")));
        os.setTrade_type(String.valueOf(map.get("trade_type")));
        os.setSign_type(String.valueOf(map.get("sign_type")));
        try {
            String sign = Signature.getSignTianYuan(os);
            os.setSign(sign);
        } catch (IllegalAccessException e) {
            logger.info("signMapBuildObject is IllegalAccessException e=", e);
            return null;
        }
        try {
            String result = HttpRequestTianYuan.sendPost(ResourceUtil.getConfigByName("wx.uniformorder"), os);
            return result;
        } catch (IOException e) {
            logger.info("signMapBuildObject is IOException e=", e);
            return null;
        } catch (UnrecoverableKeyException e) {
            logger.info("signMapBuildObject is UnrecoverableKeyException e=", e);
            return null;
        } catch (KeyManagementException e) {
            logger.info("signMapBuildObject is KeyManagementException e=", e);
            return null;
        } catch (KeyStoreException e) {
            logger.info("signMapBuildObject is KeyStoreException e=", e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            logger.info("signMapBuildObject is NoSuchAlgorithmException e=", e);
            return null;
        }
    }

    /**
     * 签名算法
     *
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getSignTianYuan(Object o) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                String name = f.getName();
                XStreamAlias anno = f.getAnnotation(XStreamAlias.class);
                if (anno != null)
                    name = anno.value();
                list.add(name + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + ResourceUtil.getConfigByName("wx.paySignKey");
        logger.info("签名数据：" + result);
        result = MD5.MD5Encode(result, "utf-8").toUpperCase();
        return result;
    }

    public static String getSign(Map<String, Object> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + Configure.getKey();
        //Util.log("sign Before MD5:" + result);
        result = MD5.MD5Encode(result).toUpperCase();
        //Util.log("sign Result:" + result);
        return result;
    }


}
