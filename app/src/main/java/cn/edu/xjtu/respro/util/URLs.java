package cn.edu.xjtu.respro.util;

/**
 * url类
 * Created by Mg on 2018/3/5.
 */

public class URLs {
    // private static final String URL_HOST = "http://192.168.100.90:8080";
    /*public static final String URL_HOST = "http://115.154.137.62:19001/cc"; https://www.disen.xyz/cc*/
    //  public static final String URL_HOST = "http://118.31.7.232:80/cc";
   public static final String URL_HOST = "https://www.disen.xyz/cc";
    private static final String ROOT_DIR = URL_HOST + "/appRecordPoints";

    public static final String  URL_GET_TRASH_PUT_RECORD_INFO = ROOT_DIR + "/getRecordInfo.do",
            URL_GET_USER_INFO = ROOT_DIR + "/getUserInfo.do",
            URL_AUTO_INPUT_POINTS = ROOT_DIR + "/autoInputPoints.do",
            URL_HAND_INPUT_POINTS = ROOT_DIR + "/handInputPoints.do",
            URL_ADD_RECORD = ROOT_DIR + "/addPutRecord.do",
            URL_GET_EXCHANGE_FACTOR = ROOT_DIR + "/getExchangeFactor.do",
            URL_BAG_INPUT_POINTS = ROOT_DIR + "/bagInputPoints.do",
            URL_GET_BAG_INFO = ROOT_DIR + "/getBagInfo.do",
            URL_GET_TRASH_TYPES = ROOT_DIR + "/getTrashTypes.do";
}
