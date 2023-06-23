package com.gm.warn.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.TreeMap;

/**
 * @Description
 * @Author wuzp
 * @Date 2023/4/26 15:31
 **/
public class Sha256Utils {
    public static String getSign(String jj, String SecretKey, String SecretId) {
        JSONObject objParam = JSONObject.parseObject(jj);
        String serviceKey = SecretKey;
        //转化为JSONObject
        //转化为key/value的一元格式：Data.Temperature.0:'27'，并排序
        TreeMap<String, String> paramMap = new TreeMap<>();
        jsonObjectToMap(paramMap, objParam);
        //拼接请求字符串：key1=value1&key2=value2&...
        String paramStr = buildParamStr(paramMap);
        //对请求参数进行HmacSHA256签名
        try {
            String sigOutParam = sign(serviceKey, paramStr);
            return sigOutParam;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将JSON格式数据转为TreeMap
     *
     * @param paramMap
     * @param param
     */
    public static void jsonObjectToMap(TreeMap<String, String> paramMap, JSONObject param) {
        Set<String> it = param.keySet();
        for (String key : it) {
            Object obj = param.get(key);
            //如果key对应的value为空，则不填装到map
            if (obj != null) {
                if (obj instanceof JSONObject || obj instanceof JSONArray) {
                    relaxJsonStr(obj, key, paramMap);
                } else {
                    paramMap.put(key, String.valueOf(obj));
                }
            }
        }
    }
    /**
     * 将TreeMap转为一维结构并排序
     *
     * @param sourceJson
     * @param prefix
     * @param paramMap
     */
    public static void relaxJsonStr(Object sourceJson, String prefix, TreeMap<String, String> paramMap) {
        if (sourceJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) sourceJson;
            Set<String> it = jsonObject.keySet();
            for (String key : it) {
                Object obj = jsonObject.get(key);
                if (obj instanceof JSONObject || obj instanceof JSONArray) {
                    relaxJsonStr(obj, prefix + "." + key, paramMap);
                } else {
                    paramMap.put(prefix + "." + key, String.valueOf(obj));
                }
            }
        } else {
            JSONArray jsonArray = (JSONArray) sourceJson;
            int i = 0;
            for (Object jsonObject : jsonArray) {
                if (jsonObject instanceof JSONObject || jsonObject instanceof JSONArray) {
                    relaxJsonStr(jsonObject, prefix + "." + i, paramMap);
                } else {
                    paramMap.put(prefix + "." + i, String.valueOf(jsonObject));
                }
                ++i;
            }
        }
    }

    /**
     * 将一维Map转为参数值格式的字符串
     *
     * @param paramMap
     * @return
     */
    public static String buildParamStr(TreeMap<String, String> paramMap) {
        String retStr = "";
        for (String key : paramMap.keySet()) {
            String value = paramMap.get(key).toString();
            if (retStr.length() != 0) {
                retStr += '&';
            }
            retStr += key + '=' + value;
        }
        return retStr;
    }

    /**
     * 签名
     *
     * @param secretKey
     * @param sourceStr
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public static String sign(String secretKey, String sourceStr) throws
            NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        byte[] hash;
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secretKeySpec);
        hash = mac.doFinal(sourceStr.getBytes("UTF-8"));
        return DatatypeConverter.printBase64Binary(hash);
    }

    public static boolean signAnalysis(JSONObject jsonObject, String SecretKey, String SecretId){
        String signDMS = jsonObject.getString("sign");
        if (!StringUtils.isEmpty(signDMS)){
            jsonObject.remove("sign");
            String shaSign = Sha256Utils.getSign(jsonObject.toJSONString(),SecretKey,SecretId);
            if (signDMS.equals(shaSign)){
                return true;
            }
        }
        return false;
    }
}
