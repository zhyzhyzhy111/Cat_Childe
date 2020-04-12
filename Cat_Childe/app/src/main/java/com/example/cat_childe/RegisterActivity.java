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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
    }

    void initView() {
        LinearLayout user_name = findViewById(R.id.user_name), verification = findViewById(R.id.verification),
                password = findViewById(R.id.password), confirm = findViewById(R.id.confirm);

        TextView text = user_name.findViewById(R.id.text);
        registerName = user_name.findViewById(R.id.edit);
        nameError = user_name.findViewById(R.id.error);
        text.setText("用户名：");
        registerName.setHint("请输入用户名");

        text = password.findViewById(R.id.text);
        registerPassword = password.findViewById(R.id.edit);
        passwordError = password.findViewById(R.id.error);
        text.setText("密码：");
        registerPassword.setHint("请输入密码");

        text = confirm.findViewById(R.id.text);
        registerConfirm = confirm.findViewById(R.id.edit);
        confirmError = confirm.findViewById(R.id.error);
        text.setText("确认：");
        registerConfirm.setHint("请确认密码");

        text = verification.findViewById(R.id.text);
        registerVerify = verification.findViewById(R.id.edit);
        verifyError = verification.findViewById(R.id.error);
        text.setText("验证码：");
        registerVerify.setHint("请输入验证码");

        verifyPicture = findViewById(R.id.verification_pic);
        verifyPicture.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode();

        ImageView back = findViewById(R.id.register_back);
        Button register = findViewById(R.id.register);
        back.setOnClickListener(this);
        register.setOnClickListener(this);
        verifyPicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.verification_pic:
                setNewCode();
                break;
            case R.id.register:
                String name = registerName.getText().toString(), password = registerPassword.getText().toString(),
                        confirm = registerConfirm.getText().toString(), verify = registerVerify.getText().toString().toLowerCase();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm) && !TextUtils.isEmpty(verify)) {
                    if (password.equals(confirm)) {
                        if (!isExistUserName(name)) {
                            if (realCode.equals(verify)) {
                                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                                saveRegisterInfo(name,password);
                                Intent intent = new Intent();
                                intent.putExtra("userName",name);
                                setResult(RESULT_OK,intent);
                                RegisterActivity.this.finish();
                            } else {
                                setNewCode();
                                verifyError.setText("验证码错误");
                                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            setNewCode();
                            nameError.setText("用户名已存在");
                            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        setNewCode();
                        confirmError.setText("前后输入不一致");
                        Toast.makeText(this, "前后输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    setNewCode();
                    if (TextUtils.isEmpty(name))
                        nameError.setText("请输入用户名");
                    if (TextUtils.isEmpty(password))
                        passwordError.setText("请输入密码");
                    if (TextUtils.isEmpty(confirm))
                        confirmError.setText("请输入确认密码");
                    if (TextUtils.isEmpty(verify))
                        verifyError.setText("请输入验证码");
                    Toast.makeText(this,"请完善信息",Toast.LENGTH_SHORT).show();
                }
        }
    }

    void setNewCode() {
        verifyPicture.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode();
    }

    boolean isExistUserName(String userName) {
        SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String password = preferences.getString(userName,"");
        return !password.equals("");
    }

    void saveRegisterInfo(String userName,String password) {
        String mdPassword = MD5Utils.md5(password);
        SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(userName,mdPassword);
        if (!editor.commit()) ;//这里其实可以添加一个抛出异常，因为commit返回false代表失败
    }

    EditText registerName;
    EditText registerConfirm;
    EditText registerPassword;
    EditText registerVerify;
    ImageView verifyPicture;
    TextView nameError;
    TextView passwordError;
    TextView confirmError;
    TextView verifyError;
    String realCode;

}



