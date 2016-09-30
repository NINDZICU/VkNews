package com.therishka.androidlab_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class LauncherActivity extends AppCompatActivity implements VKCallback<VKAccessToken> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(this, VKScope.MESSAGES, VKScope.WALL, VKScope.FRIENDS);
        } else {
            toMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (VKSdk.onActivityResult(requestCode, resultCode, data, this)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // VK AUTH SUCCESS
    @Override
    public void onResult(VKAccessToken res) {
        toMainActivity();
    }

    // VK AUTH ERROR
    @Override
    public void onError(VKError error) {
        //fk u user
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
