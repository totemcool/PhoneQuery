package com.mxj.phonequery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mxj.phonequery.model.Phone;
import com.mxj.phonequery.mvp.MvpMainView;
import com.mxj.phonequery.mvp.impl.MainPresenter;

public class MainActivity extends FragmentActivity implements View.OnClickListener, MvpMainView {

    private EditText input_phone;
    private TextView result_phone;
    private TextView result_province;
    private TextView result_type;
    private TextView result_carrier;
    private Button btn_search;

    private  MainPresenter mainPresenter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_phone = findViewById(R.id.input_phone);
        result_phone = findViewById(R.id.result_phone);
        result_province = findViewById(R.id.result_province);
        result_type = findViewById(R.id.result_type);
        result_carrier = findViewById(R.id.result_carrier);
        btn_search = findViewById(R.id.btn_search);

        btn_search.setOnClickListener(this);

        mainPresenter = new MainPresenter(this);
        mainPresenter.attach(this);
    }

    @Override
    public void onClick(View view) {
        mainPresenter.checkPhone(input_phone.getText().toString());
    }

    @Override
    public void showLoading() {
        if(dialog == null){
            dialog = ProgressDialog.show(this,"","正在加载....",true,false);
        }else{
            dialog.setTitle("");
            dialog.setMessage("正在加载...");
        }
        dialog.show();
    }

    @Override
    public void hideLoading() {
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateView() {
        Phone phone = mainPresenter.getPhoneInfo();
        result_phone.setText("手机号:" + phone.getTelString());
        result_province.setText("省份:" + phone.getProvince());
        result_type.setText("运营商:" + phone.getCatName());
        result_carrier.setText("归属运营商:" + phone.getCarrier());
    }
}
