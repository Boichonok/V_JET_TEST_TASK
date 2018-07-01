package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexboicko.v_jet_test_task.R;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaginationRecyclerViewAdapter extends PaginationRecyclerViewBaseAdapter<Item> {




    private OnItemClickListener onLongItemClickListener;



    public PaginationRecyclerViewAdapter(){

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return ItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bind(holder,position);
    }


    void bind(RecyclerView.ViewHolder holder,int position){
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        itemViewHolder.sourceName.setText(getItem(position).getSourceName());
        itemViewHolder.author.setText(getItem(position).getAuthor());
        itemViewHolder.publishAt.setText(getItem(position).getPublishAt());
        itemViewHolder.title.setText(getItem(position).getTitle());
        itemViewHolder.description.setText(getItem(position).getDescription());
        itemViewHolder.articleUrl.setText(getItem(position).getUrl().toString());
        if(getItem(position).getImageToArticle()!=null) {
            itemViewHolder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(itemViewHolder.itemView.getContext())
                    .load(getItem(position).getImageToArticle().toString())
                    .placeholder(R.drawable.erroe_load_image)
                    .error(R.drawable.erroe_load_image)
                    .into(itemViewHolder.imageView);
        }else {
            itemViewHolder.imageView.setVisibility(View.GONE);
        }
        itemViewHolder.itemButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(v.isClickable()){
                    v.setBackgroundColor(Color.rgb(233,232,232));
                }else {
                    v.setBackgroundColor(Color.rgb(255,255,255));
                }
                onLongItemClickListener.onItemClick(v,getItem(position),position);
                return false;
            }
        });


    }

    public void setOnLongItemClickListener(OnItemClickListener onItemClickListener) {
        this.onLongItemClickListener = onItemClickListener;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sourceName)
                TextView sourceName;
        @BindView(R.id.author)
                TextView author;
        @BindView(R.id.titleToArticle)
                TextView title;
        @BindView(R.id.description)
                TextView description;
        @BindView(R.id.articleUrl)
                TextView articleUrl;
        @BindView(R.id.imgToArticle)
                ImageView imageView;
        @BindView(R.id.publishAt)
                TextView publishAt;
        @BindView(R.id.itemButton)
        LinearLayout itemButton;

        ItemViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        static ItemViewHolder create(ViewGroup parent) {
            return new ItemViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false));
        }


    }

}
