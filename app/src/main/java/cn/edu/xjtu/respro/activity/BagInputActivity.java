package cn.edu.xjtu.respro.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.xjtu.respro.R;
import cn.edu.xjtu.respro.bean.TrashType;
import cn.edu.xjtu.respro.http.NetworkRequestUtil;
import cn.edu.xjtu.respro.util.Constant;
import cn.edu.xjtu.respro.view.GridViewForScrollView;

public class BagInputActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQ_SCAN_CODE = 11;
    private static final int REQ_CODE_REQUEST_PERMISSION = 12;

    private ScrollView scrollView;
    private TextView userName, subTotalPoints;
    private EditText totalPoints, deductPoints, electronicsEditText, noteEText, improperEditText;

    private Button scanCodeBtn;
    private LinearLayout infoLinearLayout;
    private ImageView infoImage;
    private TextView infoText;

    private Integer bagId;

    private GridViewForScrollView gridView;

    private GridViewAdapter gridViewAdapter;

    private List<TrashType> typeList;
    private Map<TrashType, Integer> typeWeightMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_input);
        initViews();
        getTrashTypes();
        typeWeightMap = new HashMap<>();
    }

    private void getTrashTypes() {
        NetworkRequestUtil.getTrashTypes("getTrashTypes", new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int err = response.getInt(NetworkRequestUtil.RET);
                    if (err == 0) {
                        typeList = TrashType.parse2List(response.getJSONArray(NetworkRequestUtil.RESULT));
                        if (typeList != null) {
                            gridViewAdapter = new GridViewAdapter();
                            gridView.setAdapter(gridViewAdapter);
                        }
                    }else {
                        showToastMessage("网络出错！无法获得类型列表！");
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                    showToastMessage("数据错误！");
                    System.out.println(response.toString());
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                showToastMessage("网络访问出错！");
            }
        });
    }

    private void showTypes() {
        scrollView.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        setTitle("环保袋录积分");

        scanCodeBtn = (Button) findViewById(R.id.scanCodeBtn);
        scanCodeBtn.setOnClickListener(this);

        Button submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);

        userName = (TextView) findViewById(R.id.userNameTextView);
        totalPoints = (EditText) findViewById(R.id.totalPointsEditText);
        noteEText = (EditText) findViewById(R.id.noteEditText);
        infoLinearLayout = (LinearLayout) findViewById(R.id.infoLL);
        infoImage = (ImageView) findViewById(R.id.infoImageView);
        infoText = (TextView) findViewById(R.id.infoTextView);

        infoLinearLayout.setVisibility(View.INVISIBLE);

        gridView = (GridViewForScrollView) findViewById(R.id.gridView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, 0);
        scrollView.setVisibility(View.GONE);

        subTotalPoints = (TextView) findViewById(R.id.subPointsEditText);

        deductPoints = (EditText) findViewById(R.id.deductPointsEditText);

        electronicsEditText = (EditText) findViewById(R.id.electronicsEditText);

        improperEditText = (EditText) findViewById(R.id.improperEditText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    @TargetApi(23)
    private void checkPermissions() {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BagInputActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_REQUEST_PERMISSION);
        }else {
            startActivityForResult(this, CaptureActivity.class, REQ_SCAN_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(this, CaptureActivity.class, REQ_SCAN_CODE);
            }else {
                showToastMessage("请在设置中打开拍照权限~");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scanCodeBtn:
                if (Build.VERSION.SDK_INT >= 23)  {
                    checkPermissions();
                }else {
                    startActivityForResult(this, CaptureActivity.class, REQ_SCAN_CODE);
                }
                break;
            case R.id.submitBtn:
                if (bagId == null) {
                    showToastMessage("请扫描二维码~");
                    return;
                }
                double subTotalPointsDouble = Double.parseDouble(subTotalPoints.getText().toString());
                String improper = improperEditText.getText().toString();
                if (TextUtils.isEmpty(improper)) {
                    improper = "无";
                }
                String deductP = deductPoints.getText().toString();
                System.out.println("deduct p " + deductP);
                if (TextUtils.isEmpty(deductP)) {
                    showToastMessage("请输入扣除积分~");
                    return;
                }
                double deduct = Double.parseDouble(deductP);

                String elect = electronicsEditText.getText().toString();
                if (TextUtils.isEmpty(elect)){
                    elect = "无";
                }

                String total = totalPoints.getText().toString();
                if (TextUtils.isEmpty(total)) {
                    showToastMessage("请输入总积分~");
                    return;
                }
                double totalP = 0;
                try {
                    totalP = Double.parseDouble(total);
                }catch (Exception ex) {
                    showToastMessage("请输入数字！");
                    return;
                }

                String note = noteEText.getText().toString().trim();
                if (/*desc.length() < Constant.NOTE_TEXT_MIN_LENGTH || */note.length() > Constant.NOTE_TEXT_MAX_LENGTH){
                    showToastMessage("备注字数超出限制~");
                    return;
                }
                if (TextUtils.isEmpty(note)) {
                    note = "无";
                }
                JSONArray array = new JSONArray();
                try {
                    for (TrashType type : typeList) {
                        JSONObject object = new JSONObject();
                        object.put("name", type.getTypeName());
                        object.put("weight", typeWeightMap.get(type) == null ? "0" : typeWeightMap.get(type) / 1000.0f);
                        object.put("points", typeWeightMap.get(type) == null ? "0" : (typeWeightMap.get(type) * type.getPoints())/ 1000.0f );
                        array.put(object);
                    }
                }catch (Exception ex) {
                    showToastMessage("处理数据出错！");
                    ex.printStackTrace();
                }


                submitPoints(array.toString(), bagId, (int)(subTotalPointsDouble * 1000), improper, (int)(deduct * 1000),
                        elect, (int)(totalP * 1000), note);
                break;
        }
    }

    private void submitPoints(String detail, Integer bagId, int sub, String improper, int deduct, String elect, int total, String note) {
        showUpload();
        NetworkRequestUtil.bagInputPoints(detail, bagId, sub, improper, deduct, elect, total, note, "bagInputPoints", new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int ret = response.getInt(NetworkRequestUtil.RET);
                    if (ret == 0) {
                        showSuccess();
                    } else {
                        showError(response.getString(NetworkRequestUtil.MSG));
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                showError("网络错误！");
                anError.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_SCAN_CODE:
                if (RESULT_OK == resultCode) {
                    String resultStr = data.getStringExtra(CaptureActivity.SCAN_RESULT);
                    showGettingInfo();
                    NetworkRequestUtil.getBagInfo(resultStr, "bagInputPoints", new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int ret = response.getInt(NetworkRequestUtil.RET);
                                if (ret == 0) {
                                    showInfo(response.getInt("id"), response.getString("userName"));
                                    if (typeList != null) {
                                        clearInfo();
                                    }else {
                                        getTrashTypes();
                                    }
                                    showTypes();
                                } else {
                                    showError(response.getString(NetworkRequestUtil.MSG));
                                }
                            }catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            showError("网络错误！");
                            anError.printStackTrace();
                        }
                    });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearInfo() {
        for(TrashType t : typeList) {
            t.setWeight(0);
        }
    }

    private void showInfo(int id, String name) {
        bagId = id;
        infoLinearLayout.setVisibility(View.GONE);
        scanCodeBtn.setVisibility(View.GONE);
        userName.setText(name);
        totalPoints.setText(String.valueOf(0));
        noteEText.setText("");
        deductPoints.setText("0");
        improperEditText.setText("");
        electronicsEditText.setText("");
        showTypes();
        // 清空数据
        int cnt = gridView.getChildCount();
        for (int i  =0; i < cnt; i++) {
            EditText et = (EditText) gridView.getChildAt(0).findViewById(R.id.weightEditText);
            et.setText("");
        }
        for (Map.Entry<TrashType, Integer> entry : typeWeightMap.entrySet()){
            entry.setValue(0);
        }
    }

    private void showGettingInfo() {
        scrollView.setVisibility(View.GONE);
        scanCodeBtn.setVisibility(View.GONE);
        infoLinearLayout.setVisibility(View.VISIBLE);
        infoImage.setImageResource(R.drawable.ic_cloud_download);
        infoText.setText(R.string.getting_info);
    }

    private void showError(String msg) {
        scrollView.setVisibility(View.GONE);
        scanCodeBtn.setVisibility(View.VISIBLE);
        scanCodeBtn.setEnabled(true);
        scanCodeBtn.setText("重新扫描");
        infoLinearLayout.setVisibility(View.VISIBLE);
        infoImage.setImageResource(R.drawable.ic_error_circle);
        infoText.setTextColor(getResources().getColor(R.color.error));
        infoText.setText(getResources().getString(R.string.fail_info, msg));
    }

    private void showSuccess() {
        scrollView.setVisibility(View.GONE);
        scanCodeBtn.setVisibility(View.VISIBLE);
        scanCodeBtn.setEnabled(true);
        scanCodeBtn.setText("扫描下一个");
        infoLinearLayout.setVisibility(View.VISIBLE);
        infoImage.setImageResource(R.drawable.ic_check_circle_ok);
        infoText.setTextColor(getResources().getColor(R.color.upload_success));
        infoText.setText(R.string.succeed);
    }

    private void showUpload() {
        scrollView.setVisibility(View.GONE);
        scanCodeBtn.setEnabled(false);
        infoLinearLayout.setVisibility(View.VISIBLE);
        infoImage.setImageResource(R.drawable.ic_cloud_upload);
        infoText.setTextColor(getResources().getColor(R.color.uploading));
        infoText.setText(R.string.uploading);
    }

    class GridViewAdapter extends BaseAdapter {
        class ViewHolder{
            TextView typeNameTextView;
            EditText weightEditText;
        }
        @Override
        public int getCount() {
            return typeList.size();
        }

        @Override
        public Object getItem(int position) {
            return typeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return typeList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(BagInputActivity.this).inflate(R.layout.grid_item, null, false);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.typeNameTextView = (TextView) convertView.findViewById(R.id.typeNameTextView);
                holder.weightEditText = (EditText) convertView.findViewById(R.id.weightEditText);
                convertView.setTag(holder);
            }
            final TrashType type = typeList.get(position);
            holder.typeNameTextView.setText(type.getTypeName());
            holder.weightEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        typeWeightMap.put(type, 0);
                        updateSubTotalPoints();
                        return;
                    }
                    if (!s.toString().matches("[0-9]+")) {
                        typeWeightMap.put(type, 0);
                        showToastMessage("请输入数字");
                        return;
                    }
                    Integer weight = 0;
                    try {
                        weight = Integer.parseInt(s.toString());
                    }catch (Exception ex) {
                        showToastMessage("数字超出范围！");
                        weight = 0;
                    }
                    typeWeightMap.put(type, weight);
                    updateSubTotalPoints();
                }
            });
            return convertView;
        }
    }

    private void updateSubTotalPoints() {
        double sum = 0;
        for (TrashType type : typeList) {
            Integer num = typeWeightMap.get(type);
            if (num != null) {
                sum += num * type.getPoints();
            }
        }
        System.out.println(sum);
        subTotalPoints.setText(String.format("%.4f", sum / 1000));
    }
}
