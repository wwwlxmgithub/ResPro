package cn.edu.xjtu.respro.activity;

import android.os.Bundle;
import android.view.View;

import cn.edu.xjtu.respro.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setTitle("缔森积分录入App");
    }

    private void initViews() {
        findViewById(R.id.handInputBtn).setOnClickListener(this);
        findViewById(R.id.autoInputBtn).setOnClickListener(this);
        findViewById(R.id.addRecordBtn).setOnClickListener(this);
        findViewById(R.id.bagBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.autoInputBtn:
                startActivity(this, AutoInputActivity.class);
                break;
            case R.id.handInputBtn:
                startActivity(this, HandInputActivity.class);
                break;
            case R.id.addRecordBtn:
                startActivity(this, AddRecordActivity.class);
                break;
            case R.id.bagBtn:
                startActivity(this, BagInputActivity.class);
                break;
        }
    }
}
