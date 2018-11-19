package cn.edu.xjtu.respro.http;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import cn.edu.xjtu.respro.util.URLs;

/**
 * 网络请求类
 * Created by Mg on 2018/3/5.
 */

public class NetworkRequestUtil {
    public static final String RET = "ret";
    public static final String MSG = "msg";
    public static final String RESULT = "result";

    public static void getRecordInfo(String code, String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.get(URLs.URL_GET_TRASH_PUT_RECORD_INFO)
                .addQueryParameter("code", code)
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }

    public static void getUserInfo(int id, String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.get(URLs.URL_GET_USER_INFO)
                .addQueryParameter("userId", Integer.toString(id))
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }
    public static void getExchangeFactor(String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.get(URLs.URL_GET_EXCHANGE_FACTOR)
                .addQueryParameter("t", Long.toString(System.currentTimeMillis()))
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }

    public static void autoInputPoints(String code, String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.post(URLs.URL_AUTO_INPUT_POINTS)
                .addBodyParameter("code", code)
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }

    public static void handInputPoints(int id, String detail, int sub, String improper, int deduct, String elect, int total, String note, String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.post(URLs.URL_HAND_INPUT_POINTS)
                .addBodyParameter("id", Integer.toString(id))
                .addBodyParameter("detail", detail)
                .addBodyParameter("subTotalPoints", Integer.toString(sub))
                .addBodyParameter("improperPut", improper)
                .addBodyParameter("deductPoints", Integer.toString(deduct))
                .addBodyParameter("electronics", elect)
                .addBodyParameter("totalPoints", Integer.toString(total))
                .addBodyParameter("note", note)
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }

    public static void bagInputPoints(String detail, Integer bagId, int sub, String improper, int deduct, String elect, int total, String note, String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.post(URLs.URL_BAG_INPUT_POINTS)
                .addBodyParameter("detail", detail)
                .addBodyParameter("bagId", Integer.toString(bagId))
                .addBodyParameter("subTotalPoints", Integer.toString(sub))
                .addBodyParameter("improperPut", improper)
                .addBodyParameter("deductPoints", Integer.toString(deduct))
                .addBodyParameter("electronics", elect)
                .addBodyParameter("totalPoints", Integer.toString(total))
                .addBodyParameter("note", note)
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }

    public static void getBagInfo(String bagCode, String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.get(URLs.URL_GET_BAG_INFO)
                .addQueryParameter("t", String.valueOf(System.currentTimeMillis()))
                .addQueryParameter("bagCode", bagCode)
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }

    public static void getTrashTypes(String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.get(URLs.URL_GET_TRASH_TYPES)
                .addQueryParameter("t", String.valueOf(System.currentTimeMillis()))
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }

    public static void addRecord(int userId, String detail, int sub, String improper, int deduct, String elect, int total, String note, String tag, final JSONObjectRequestListener parsedRequestListener){
        AndroidNetworking.post(URLs.URL_ADD_RECORD)
                .addBodyParameter("userId", Integer.toString(userId))
                .addBodyParameter("detail", detail)
                .addBodyParameter("subTotalPoints", Integer.toString(sub))
                .addBodyParameter("improperPut", improper)
                .addBodyParameter("deductPoints", Integer.toString(deduct))
                .addBodyParameter("electronics", elect)
                .addBodyParameter("totalPoints", Integer.toString(total))
                .addBodyParameter("note", note)
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(parsedRequestListener);
    }
}
