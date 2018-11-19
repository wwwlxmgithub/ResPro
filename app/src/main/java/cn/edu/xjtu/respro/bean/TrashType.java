package cn.edu.xjtu.respro.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 垃圾类型
 * Created by Mg on 2018/5/25.
 */

public class TrashType {
    public static final String  ID = "id",
            TYPE_NAME = "typeName",
            POINTS = "points";
    private int id;

    private String typeName;

    private float points; // 每千克积分

    private double weight = 0; // 实际重量（千克）

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TrashType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", points=" + points +
                ", weight=" + weight +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public static List<TrashType> parse2List(JSONArray array){
        List<TrashType> list = new ArrayList<>();
        int len = array.length();
        for (int i = 0;i < len; i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                TrashType type = new TrashType();
                type.setId(object.getInt(ID));
                type.setPoints(object.getInt(POINTS) / 1000.0f);
                type.setTypeName(object.getString(TYPE_NAME));
                list.add(type);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
