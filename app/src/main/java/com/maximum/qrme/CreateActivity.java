package com.maximum.qrme;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import io.github.anderscheow.validator.Validation;
import io.github.anderscheow.validator.Validator;
import io.github.anderscheow.validator.constant.Mode;
import io.github.anderscheow.validator.rules.BaseRule;


public class CreateActivity extends AppCompatActivity {

    String url;
    String textName;
    String textTelHome;
    String textTelWork;
    String textEmail;
    String textOrganization;
    String textJobTitle;
    String textBday;
    String textAddress;
    String textWebsite;
    TextInputLayout editName;
    TextInputLayout editTelHome;
    TextInputLayout editTelWork;
    TextInputLayout editEmail;
    TextInputLayout editOrganization;
    TextInputLayout editJobTitle;
    TextInputLayout editBday;
    TextInputLayout editAddress;
    TextInputLayout editWebsite;
    Button btnCreate;
    Button btnClear;
    LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create);

        editName = findViewById(R.id.editFirstName);
        editTelHome = findViewById(R.id.editTelHome);
        editTelWork = findViewById(R.id.editTelWork);
        editEmail = findViewById(R.id.editEmail);
        editOrganization = findViewById(R.id.editOrganization);
        editJobTitle = findViewById(R.id.editJobTitle);
        editBday = findViewById(R.id.editBday);
        editAddress = findViewById(R.id.editAddress);
        editWebsite = findViewById(R.id.editWebsite);
        btnCreate = findViewById(R.id.buttonCreate);
        btnClear = findViewById(R.id.buttonClear);
        linearLayout = findViewById(R.id.linearLayout);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetEditText(linearLayout);
            }
        });
    }

    public void checkConnectivityFirstTime() {
        if (checkConnectivity(this)) {
            Intent intent = new Intent(CreateActivity.this, QRImageView.class);
            intent.putExtra("EXTRA_URL", url);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.checkInternetText, Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkConnectivity(Context mContext) {
        ConnectivityManager manager =(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (manager != null) {
            activeNetwork = manager.getActiveNetworkInfo();
        }
        boolean connected = false;
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                connected = true;
            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                connected = true;
            }
        } else{
            connected = false;
        }
        return connected;
    }

    public void resetEditText(LinearLayout linearLayout) {
        for(int i = 0; i < linearLayout.getChildCount(); i++) {
            View v = linearLayout.getChildAt(i);
            if(v instanceof TextInputLayout) {
                ((TextInputLayout) v).getEditText().getText().clear();
            }
        }
    }

    public void validator() {

        Validation nameValidation = new Validation(editName)
                .and(new BaseRule() {
                    @Override
                    public boolean validate(Object s) {
                        return !((String) s).isEmpty();
                    }

                    @NonNull
                    @Override
                    public String errorMessage() {
                        return getString(R.string.invalidFieldText);
                    }
                });

        Validation workValidation = new Validation(editTelWork)
                .and(new BaseRule() {
                    @Override
                    public boolean validate(Object s) {
                        return !((String) s).isEmpty();
                    }

                    @NonNull
                    @Override
                    public String errorMessage() {
                        return "Invalid input, Field is empty!";
                    }
                });


        Validator.with(this)
                .setMode(Mode.CONTINUOUS)
                .validate(new Validator.OnValidateListener() {

                              @Override
                              public void onValidateSuccess(List<String> values) {
                                  stringsText();
                                  checkConnectivityFirstTime();
                              }

                              @Override
                              public void onValidateFailed() {
                                  Toast.makeText(getApplicationContext(), "Please check manadtory fields!", Toast.LENGTH_LONG).show();
                              }
                          },
                        nameValidation, workValidation);
    }

    public void stringsText() {
        textName = editName.getEditText().getText().toString();
        textTelHome = editTelHome.getEditText().getText().toString();
        textTelWork = editTelWork.getEditText().getText().toString();
        textEmail = editEmail.getEditText().getText().toString();
        textOrganization = editOrganization.getEditText().getText().toString();
        textJobTitle = editJobTitle.getEditText().getText().toString();
        textBday = editBday.getEditText().getText().toString();
        textAddress = editAddress.getEditText().getText().toString();;
        textWebsite = editWebsite.getEditText().getText().toString();
        url = "https://qrcode.tec-it.com/API/QRCode?data=BEGIN%3aVCARD%0d%0aVERSION%3a2.1%0d%0aN%3a%22" +
                textName + "%22%0d%0aTEL%3bHOME%3bVOICE%3a%22" +
                textTelHome + "%22%0d%0aTEL%3bWORK%3bVOICE%3a%22" +
                textTelWork + "%22%0d%0aEMAIL%3a%22" +
                textEmail + "%22%0d%0aORG%3a%22" +
                textOrganization + "%22%0d%0aTITLE%3a%22" +
                textJobTitle + "%22%0d%0aBDAY%3a" +
                textBday + "%0d%0aADR%3a%22" +
                textAddress + "%22%0d%0aURL%3a%22" +
                textWebsite + "%22%0d%0aEND%3aVCARD&backcolor=%23ffffff";
    }

}

