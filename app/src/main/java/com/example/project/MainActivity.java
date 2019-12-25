package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.percentlayout.widget.PercentLayoutHelper;
import androidx.percentlayout.widget.PercentRelativeLayout;

public class MainActivity extends AppCompatActivity {

    TextView txtSignup ,textSignin;
    LinearLayout llSignup;
    LinearLayout llSignin;
    Button btnSignup,btnSignin ;
    EditText email_signin , pass_signin , email_signup,pass_signup,mobile ,pass_confirm;

    private static Context appContext;
    DBConnection helper = new DBConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();

        email_signin = findViewById(R.id.email_signin);
        pass_signin = findViewById(R.id.pass_signin);

        email_signup = findViewById(R.id.email_signup);
        pass_signup = findViewById(R.id.pass_signup);
        mobile = findViewById(R.id.mobile);
        pass_confirm= findViewById(R.id.pass_confirm);

        btnSignin = findViewById(R.id.btnSignin);
        btnSignup = findViewById(R.id.btnSignup);

        llSignin= findViewById(R.id.llSignin);
        llSignup= findViewById(R.id.llSignup);

        /// for showSigninForm and showSignupForm
        textSignin= findViewById(R.id.textSignin);
        txtSignup= findViewById(R.id.txtSignup);
        helper.connection();
        textSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSigninForm();
            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignupForm();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email_signin.getText().toString();
                String pass = pass_signin.getText().toString();
                boolean status = helper.login(mail, pass);
                if (status == true){
                    Intent intent = new Intent(MainActivity.this,Menu.class);
                    RWFile storage = new RWFile();
                    storage.writeToFile(mail);
                    startActivity(intent);
                    finish();
                }
                else{Toast.makeText(MainActivity.this,"Your login credentials are wrong!",Toast.LENGTH_LONG).show();}
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_up = email_signup.getText().toString();
                String pass_up = pass_signup.getText().toString();
                String pass_conf = pass_confirm.getText().toString();
                String mobile_up = mobile.getText().toString();
                boolean status = false;
                if(Validation (email_up,pass_up,pass_conf,mobile_up)) {
                    status = helper.register(email_up,pass_up,mobile_up);
                    Toast.makeText(getApplicationContext(),helper.sendMessage(),Toast.LENGTH_LONG).show();
                    if (status == true) {
                        boolean status2 = helper.login(email_up,pass_up);
                        if (status2 == true) {
                            Intent intent = new Intent(MainActivity.this,Menu.class);
                            RWFile storage = new RWFile();
                            storage.writeToFile(email_up);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });
    }

    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin =  paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.14f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.86f;
        llSignup.requestLayout();

        txtSignup.setVisibility(View.GONE);
        textSignin.setVisibility(View.VISIBLE);
        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.86f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.14f;
        llSignup.requestLayout();

        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        txtSignup.setVisibility(View.VISIBLE);
        textSignin.setVisibility(View.GONE);
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }


    public boolean Validation (String email_up,String pass_up,String pass_conf,String mobile_up){

        if (email_up.equals("")|| pass_up.equals("")|| mobile_up.equals("")||pass_conf.equals("")){
            Toast.makeText(getApplicationContext(),"Enter your Email , Password and mobile number ",Toast.LENGTH_LONG).show();
            return false;
        }
        /// chick if it is email or not
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_up).matches()) {
            Toast.makeText(getApplicationContext(),"Enter Valid Email",Toast.LENGTH_LONG).show();
            return false;
        }
        /// chick if Password Matches
        else if (!pass_up.equals(pass_conf)) {
            Toast.makeText(getApplicationContext(), "Password Does Not Matches", Toast.LENGTH_LONG).show();
            pass_signup.setText("");
            pass_confirm.setText("");
            return false;
        }
        else
            return true;

    }

    public static Context getAppContext() {
        return appContext;
    }
}
