package com.therishka.androidlab_2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.therishka.androidlab_2.models.VkDialog;
import com.therishka.androidlab_2.models.VkDialogResponse;
import com.therishka.androidlab_2.network.RxVk;

import java.util.List;

public class DialogActivity extends AppCompatActivity {

    ProgressBar mProgress;
    RecyclerDialogAdapter mDialogAdapter;
    RecyclerView mRecyclerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        mRecyclerList = (RecyclerView) findViewById(R.id.dialogs_list);
        mDialogAdapter = new RecyclerDialogAdapter(this);
        mRecyclerList.setAdapter(mDialogAdapter);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));
//        RxVk rxVk= new RxVk();
//        rxVk.getDialogs(new RxVk.RxVkListener<VkDialogResponse>() {
//
//            @Override
//            public void requestFinished(VkDialogResponse requestResult) {
//                mDialogAdapter.setDialogsList(requestResult.getDialogs());
//            }
//
//
//        });
        getDialogsAndShowThem();
    }


    private void getDialogsAndShowThem() {
        showLoading();
        RxVk api = new RxVk();
        api.getDialogs(new RxVk.RxVkListener<VkDialogResponse>() {
            @Override
            public void requestFinished(VkDialogResponse requestResult) {
                mDialogAdapter.setDialogsList(requestResult.getDialogs());
                showDialogs();
            }
        });
    }

    private void showLoading() {
        mRecyclerList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showDialogs() {
        mRecyclerList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private class RecyclerDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List <VkDialog> mDialogsList;
        private Context mContext;

        public RecyclerDialogAdapter(@NonNull Context context) {
            mContext = context;
        }

        public void setDialogsList(@Nullable List<VkDialog> dialogsList){
            mDialogsList = dialogsList;
            notifyDataSetChanged();                         //непонятно что это
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
            return new DialogsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof DialogsViewHolder) {
                VkDialog dialog = mDialogsList.get(position);
                ((DialogsViewHolder) holder).bind(dialog);
                Glide.with(mContext).load(dialog.getPhoto())
                        .fitCenter()
                        .into(((DialogsViewHolder) holder).avatar);
            }
        }

        @Override
        public int getItemCount() {
            return mDialogsList != null ? mDialogsList.size() : 0;
        }
    }

    class DialogsViewHolder extends RecyclerView.ViewHolder {

        TextView fullName;
        ImageView avatar;
        View isRead;
        TextView message;


        public DialogsViewHolder(View itemView) {
            super(itemView);
            fullName = (TextView) itemView.findViewById(R.id.full_name);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            isRead = itemView.findViewById(R.id.is_message_read);
            message= (TextView) itemView.findViewById(R.id.last_message);
        }

        public void bind(VkDialog dialog) {
            fullName.setText(dialog.getUsername());
            message.setText(dialog.getMessage());
            isRead.setVisibility(dialog.is_read() ? View.VISIBLE : View.GONE);
        }
    }


}
