package com.therishka.androidlab_2.network;

import android.content.Context;
import android.content.res.Resources;
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
import com.therishka.androidlab_2.R;
import com.therishka.androidlab_2.models.VkAttachments;
import com.therishka.androidlab_2.models.VkLikes;
import com.therishka.androidlab_2.models.VkNewsItem;
import com.therishka.androidlab_2.models.VkPhoto;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    RecyclerView mRecyclerList;
    RecyclerNewsAdapter mNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        
        mProgressBar = (ProgressBar) findViewById(R.id.loading_view);
        mRecyclerList  = (RecyclerView) findViewById(R.id.news_list);
        mNewsAdapter = new RecyclerNewsAdapter(this);
        mRecyclerList.setAdapter(mNewsAdapter);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        getNewsAndShowThem();
    }

    private void getNewsAndShowThem() {
        showLoading();
        RxVk api = new RxVk();
        api.getNews(new RxVk.RxVkListener<LinkedList<VkNewsItem>>() {
            @Override
            public void requestFinished(LinkedList<VkNewsItem> requestResult) {
                mNewsAdapter.setNewsList(requestResult);
                showNews();
            }

        });
    }

    private void showLoading() {
        mRecyclerList.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showNews() {
        mRecyclerList.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
    
    private class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<VkNewsItem> mNewsList;
        private Context mContext;

        public RecyclerNewsAdapter(@NonNull Context context) {
            mContext = context;
        }
        public void setNewsList(@Nullable LinkedList<VkNewsItem> newsList) {
            mNewsList = newsList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NewsViewHolder) {
                VkNewsItem news = mNewsList.get(position);

                ((NewsViewHolder) holder).bind(news);
                Glide.with(mContext).load(news.getPublisher().getPhoto_100())
                        .fitCenter()
                        .into(((NewsViewHolder) holder).avatar);

                List<VkAttachments> vkAttachments = news.getAttachments();
                if (vkAttachments!=null){
                    ((NewsViewHolder) holder).dropImage_1();
                    ((NewsViewHolder) holder).dropImage_2();
                    ((NewsViewHolder) holder).dropImage_3();
                    ((NewsViewHolder) holder).dropImage_4();
                    int count =1;
                    for (VkAttachments i: vkAttachments) {
                        String photo = null;
                        if (i!=null){
                            photo = getMaxPhoto(i.getPhoto());
                        }
                        if (photo!= null){
                                ImageView image = getImage((NewsViewHolder) holder, count);
                                if(image!=null) {
                                    image.setVisibility(View.VISIBLE);
                                    Glide.with(mContext).load(photo)
                                            .fitCenter()
                                            .into(image);
                                }

                        }
                        count++;
                        
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
           return mNewsList != null ? mNewsList.size() : 0;
        }

        public String getMaxPhoto(VkPhoto photo){
            String returnPhoto = null;
            if (photo != null) { returnPhoto = photo.getPhoto_2560();
                if (returnPhoto == null) {returnPhoto = photo.getPhoto_1280();
                    if (returnPhoto == null) {returnPhoto = photo.getPhoto_807();
                        if (returnPhoto == null) {returnPhoto = photo.getPhoto_604();
                            if (returnPhoto == null) {returnPhoto = photo.getPhoto_130();
                                if (returnPhoto == null) {returnPhoto = photo.getPhoto_75();
                                }
                            }
                        }
                    }
                }
            }
            return returnPhoto;
        }
        public ImageView getImage(NewsViewHolder holder, int count){
            ImageView image = null;
            if(count==1) image= holder.getImage_1();
            if(count==2) image= holder.getImage_2();
            if(count==3) image= holder.getImage_3();
            if(count==4) image= holder.getImage_4();
            return image;
        }

    }
    private class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView avatar;
        TextView text;
        ImageView content_image_1;
        ImageView content_image_2;
        ImageView content_image_3;
        ImageView content_image_4;
        TextView date;
        TextView likes;

        public NewsViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_group);
            avatar = (ImageView) itemView.findViewById(R.id.avatar_group);
            text = (TextView) itemView.findViewById(R.id.content_text);
            content_image_1 = (ImageView) itemView.findViewById(R.id.image_content_1);
            content_image_2 = (ImageView) itemView.findViewById(R.id.image_content_2);
            content_image_3 = (ImageView) itemView.findViewById(R.id.image_content_3);
            content_image_4 = (ImageView) itemView.findViewById(R.id.image_content_4);
            date = (TextView) itemView.findViewById(R.id.date);
            likes = (TextView) itemView.findViewById(R.id.likes);
        }
        public void bind(VkNewsItem news) {

            String dateMessage;
            Date date = new Date(news.getDate() * 1000);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy ");
            dateMessage = dateFormat.format(date);
            getDate().setText(dateMessage);


            VkLikes likes = news.getLikes();
            if (likes != null) {
                getLikes().setText("");
                getLikes().append(Integer.toString(likes.getCount()));
            }
            name.setText(news.getPublisher().getName());
            text.setText(news.getText());

        }
        public TextView getDate(){
            return date;
    }
        public TextView getLikes(){
            return likes;
    }
        public ImageView getImage_1(){
            return content_image_1;
        }
        public ImageView getImage_2(){
            return content_image_2;
        }
        public ImageView getImage_3(){
            return content_image_3;
        }
        public ImageView getImage_4(){return content_image_4;}
        public void dropImage_1(){content_image_1.setVisibility(View.GONE);}
        public void dropImage_2(){content_image_2.setVisibility(View.GONE);}
        public void dropImage_3(){content_image_3.setVisibility(View.GONE);}
        public void dropImage_4(){content_image_3.setVisibility(View.GONE);}


    }
}
