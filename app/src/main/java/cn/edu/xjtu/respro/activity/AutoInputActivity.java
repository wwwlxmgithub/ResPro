package cn.edu.xjtu.respro.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import cn.edu.xjtu.respro.R;
import cn.edu.xjtu.respro.http.NetworkRequestUtil;

public class AutoInputActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQ_SCAN_CODE = 1;
    private static final int REQ_CODE_REQUEST_PERMISSION = 2;
    private Button scanCodeBtn;
    private LinearLayout infoLinearLayout;
    private ImageView infoImage;
    private TextView infoText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_input);
        initViews();
    }

    private void initViews() {
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        setTitle("自动录入积分");
        scanCodeBtn = (Button) findViewById(R.id.scanCodeBtn);
        scanCodeBtn.setOnClickListener(this);

        infoLinearLayout = (LinearLayout) findViewById(R.id.infoLL);
        infoImage = (ImageView) findViewById(R.id.infoImageView);
        infoText = (TextView) findViewById(R.id.infoTextView);

        infoLinearLayout.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
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
        }
    }
    @TargetApi(23)
    private void checkPermissions() {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AutoInputActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_REQUEST_PERMISSION);
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
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_SCAN_CODE:
                if (RESULT_OK == resultCode) {
                    String  resultStr = data.getStringExtra(CaptureActivity.SCAN_RESULT);
                    /*String code = new String(Base64.decode(resultStr, Base64.DEFAULT));
                    showToastMessage("result code: " + code);*/
                    showUpload();
                    NetworkRequestUtil.autoInputPoints(resultStr, "autoInputPoints", new JSONObjectRequestListener() {
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
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showError(String msg) {
        scanCodeBtn.setEnabled(true);
        infoLinearLayout.setVisibility(View.VISIBLE);
        infoImage.setImageResource(R.drawable.ic_error_circle);
        infoText.setTextColor(getResources().getColor(R.color.error));
        infoText.setText(getResources().getString(R.string.fail_info, msg));
    }

    private void showSuccess() {
        scanCodeBtn.setEnabled(true);
        scanCodeBtn.setText("扫描下一个");
        infoLinearLayout.setVisibility(View.VISIBLE);
        infoImage.setImageResource(R.drawable.ic_check_circle_ok);
        infoText.setTextColor(getResources().getColor(R.color.upload_success));
        infoText.setText(R.string.succeed);
    }

    private void showUpload() {
        scanCodeBtn.setEnabled(false);
        infoLinearLayout.setVisibility(View.VISIBLE);
        infoImage.setImageResource(R.drawable.ic_cloud_upload);
        infoText.setTextColor(getResources().getColor(R.color.uploading));
        infoText.setText(R.string.uploading);
    }
}
