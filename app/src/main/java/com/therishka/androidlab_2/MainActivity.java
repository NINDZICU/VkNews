package com.therishka.androidlab_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.therishka.androidlab_2.network.NewsActivity;
import com.therishka.androidlab_2.network.RxVk;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mUserName;
    RelativeLayout mMainContent;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserName = (TextView) findViewById(R.id.user_name);
        mMainContent = (RelativeLayout) findViewById(R.id.main_content);
        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        Button toFriendsBtn = (Button) findViewById(R.id.to_friends_activity);
        toFriendsBtn.setOnClickListener(this);

        Button toFriendsOptimisedBtn = (Button) findViewById(R.id.to_friends_activity_optimised);
        toFriendsOptimisedBtn.setOnClickListener(this);

        Button btnToDialog = (Button) findViewById(R.id.btn_to_dialog_activity);
        btnToDialog.setOnClickListener(this);

        Button btnToNews = (Button) findViewById(R.id.btn_to_news_activity);
        btnToNews.setOnClickListener(this);

        setUserNameAndShowButtons();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_friends_activity:
                toFriends();
                break;
            case R.id.to_friends_activity_optimised:
                toFriendsOptimised();
                break;
            case R.id.btn_to_dialog_activity:
                toDialogs();
                break;
            case R.id.btn_to_news_activity:
                toNews();
                break;
        }
    }

    private void setUserNameAndShowButtons() {
        showLoading();
        RxVk vk = new RxVk();
        vk.getUsers(new RxVk.RxVkListener<VKList<VKApiUser>>() {
            @Override
            public void requestFinished(VKList<VKApiUser> requestResult) {
                showMainLayout();
                mUserName.setText(getString(R.string.main_activity_user_name,
                        requestResult.get(0).first_name, requestResult.get(0).last_name));
            }
        }, 0);
    }

    private void showMainLayout() {
        mMainContent.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
        mMainContent.setVisibility(View.GONE);
    }

    private void toFriends() {
        Intent friendsIntent = new Intent(this, FriendsActivity.class);
        startActivity(friendsIntent);
    }

    private void toFriendsOptimised() {
        Intent friendsIntent = new Intent(this, FriendsActivityOptimised.class);
        startActivity(friendsIntent);
    }
    private void toDialogs(){
        Intent dialogsIntent = new Intent(this, DialogActivity.class);
        startActivity(dialogsIntent);
    }private void toNews(){
        Intent newsIntent = new Intent(this, NewsActivity.class);
        startActivity(newsIntent);
    }
}