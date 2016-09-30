package com.therishka.androidlab_2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.therishka.androidlab_2.models.VkFriend;
import com.therishka.androidlab_2.network.RxVk;

import java.util.List;

public class FriendsActivityOptimised extends AppCompatActivity {

    ProgressBar mProgress;
    RecyclerView mRecyclerList;
    RecyclerFriendsAdapter mFriendsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_optimised);

        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        mRecyclerList = (RecyclerView) findViewById(R.id.friends_list);
        mFriendsAdapter = new RecyclerFriendsAdapter(this);
        mRecyclerList.setAdapter(mFriendsAdapter);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        getFriendsAndShowThem();
    }

    private void getFriendsAndShowThem() {
        showLoading();
        RxVk api = new RxVk();
        api.getFriends(new RxVk.RxVkListener<List<VkFriend>>() {
            @Override
            public void requestFinished(List<VkFriend> requestResult) {
                mFriendsAdapter.setFriendsList(requestResult);
                showFriends();
            }
        });
    }

    private void showLoading() {
        mRecyclerList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showFriends() {
        mRecyclerList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    @SuppressWarnings("WeakerAccess")
    private class RecyclerFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<VkFriend> mFriendsList;
        private Context mContext;

        public RecyclerFriendsAdapter(@NonNull Context context) {
            mContext = context;
        }

        public void setFriendsList(@Nullable List<VkFriend> friendsList) {
            mFriendsList = friendsList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
            return new FriendsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof FriendsViewHolder) {
                VkFriend friend = mFriendsList.get(position);
                ((FriendsViewHolder) holder).bind(friend);
                Glide.with(mContext).load(friend.getSmallPhotoUrl())
                        .fitCenter()
                        .into(((FriendsViewHolder) holder).avatar);
            }
        }

        @Override
        public int getItemCount() {
            return mFriendsList != null ? mFriendsList.size() : 0;
        }
    }

    @SuppressWarnings("WeakerAccess")
    private class FriendsViewHolder extends RecyclerView.ViewHolder {

        TextView fullName;
        ImageView avatar;
        View isOnline;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            fullName = (TextView) itemView.findViewById(R.id.full_name);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            isOnline = itemView.findViewById(R.id.is_user_online);
        }


        public void bind(VkFriend friend) {
            fullName.setText(friend.getName() + " " + friend.getSurname());
            isOnline.setVisibility(friend.isOnline() ? View.VISIBLE : View.GONE);
        }
    }

}
