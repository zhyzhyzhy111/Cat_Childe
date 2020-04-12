package com.example.cat_childe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
    }

    void initView() {
        LinearLayout user_name = findViewById(R.id.user_name), password = findViewById(R.id.password);
        TextView text = user_name.findViewById(R.id.text);
        passwordError = password.findViewById(R.id.error);
        loginName = user_name.findViewById(R.id.edit);
        text.setText("用户名：");
        loginName.setHint("请输入用户名");

        text = password.findViewById(R.id.text);
        loginPassword = password.findViewById(R.id.edit);
        text.setText("密码：");
        loginPassword.setHint("请输入密码");

        TextView register = findViewById(R.id.go_register);
        ImageView back = findViewById(R.id.login_back);
        Button login = findViewById(R.id.login);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.go_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login:
                String name = loginName.getText().toString(), password = MD5Utils.md5(loginPassword.getText().toString());
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_MULTI_PROCESS);
                    String realPassword = preferences.getString(name,"");
                    if (realPassword.equals(password)) {
                        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this,MainActivity.class));
                        finish();
                    }
                    else {
                        passwordError.setText("用户名密码错误");
                        Toast.makeText(this,"用户名密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    passwordError.setText("请填写用户名密码");
                    Toast.makeText(this,"请填写用户名密码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    EditText loginName;
    EditText loginPassword;
    TextView passwordError;
}