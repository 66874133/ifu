
package com.funnel.svc.util;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

/**
 * json转换工具类<br>
 * 创建人: wanghua4 <br>
 * 创建时间:2014-7-1
 */
public class JsonUtil
{

    /**
     * 将java对象转换成JSONObject对象
     * 
     * @param object
     *            java对象
     * @return JSONObject对象
     */
    public static JSONObject toJSONObject(Object object)
    {
        if (null == object)
        {
            return null;
        }
        JSONValue.COMPRESSION = new JSONStyle(2);
        if (object instanceof String)
        {
            return (JSONObject)JSONValue.parse((String)object);
        }
        else
        {
            return (JSONObject)JSONValue.parse(JSONValue.toJSONString(object));
        }

    }

    /**
     * 将字符串转换成JSONObject对象
     * 
     * @param json
     *            json字符串
     * @return JSONObject对象
     */
    public static JSONObject toJSONObject(String json)
    {
        JSONValue.COMPRESSION = new JSONStyle(2);
        return (JSONObject)JSONValue.parse(json);
    }

    /**
     * 将java对象转换成JSONArray对象
     * 
     * @param object
     *            java对象
     * @return JSONArray对象
     */
    public static JSONArray toJSONArray(Object object)
    {
        JSONValue.COMPRESSION = new JSONStyle(2);
        return (JSONArray)JSONValue.parse(JSONValue.toJSONString(object));
    }

    /**
     * 将字符串转换成JSONArray对象
     * 
     * @param json
     *            json字符串
     * @return JSONArray对象
     */
    public static JSONArray toJSONArray(String json)
    {
        JSONValue.COMPRESSION = new JSONStyle(2);
        return (JSONArray)JSONValue.parse(json);
    }

    /**
     * 将java对象转换成json格式字符串
     * 
     * @param object
     *            java对象
     * @return json格式字符串
     */
    public static String toJSONString(Object object)
    {
        JSONValue.COMPRESSION = new JSONStyle(2);
        return JSONValue.toJSONString(object);
    }

    public static <T> T toObject(JSONObject jsonObject, Class<T> class1)
    {
        JSONValue.COMPRESSION = new JSONStyle(2);
        return JSONValue.parse(toJSONString(jsonObject), class1);
    }

    public static <T> List<T> toObject(JSONArray jsonArray, Class<T> class1)
    {
        List<T> list = new ArrayList<T>();
        for (Object obj : jsonArray)
        {
            list.add(toObject((JSONObject)obj, class1));
        }
        return list;
    }
}
