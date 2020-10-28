package com.example.ihsan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CharityItemAdapter extends RecyclerView.Adapter<CharityItemAdapter.CharityItemViewHolder>{
    private Context mContext;
    private ArrayList<CharityItem> charityItems = new ArrayList<>();
    OnDeleteButtonItemClickListener deleteButtonListener;
    OnUpdateButtonItemClickListener updateButtonListener;
    public interface OnDeleteButtonItemClickListener {
        void onDeleteIsClick(View button, int position);
    }
    public interface OnUpdateButtonItemClickListener {
        void onUpdateIsClick(View button, int position);
    }
    public void setUpdateButtonListener(OnUpdateButtonItemClickListener  updateButtonListener) {
        this.updateButtonListener= updateButtonListener;
    }
    public void setDeleteButtonListener(OnDeleteButtonItemClickListener  deleteButtonListener) {
        this.deleteButtonListener= deleteButtonListener;
    }
    public CharityItemAdapter(Context mContext, ArrayList<CharityItem> charityItems) {
        this.mContext = mContext;
        this.charityItems = charityItems;

    }

    @NonNull
    @Override
    public CharityItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.charityitem_list_item_layout,parent,false);
        CharityItemViewHolder charityItemViewHolder =new CharityItemViewHolder(view);
        return charityItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharityItemViewHolder holder, final int position) {
    holder.charityItemTxt.setText(charityItems.get(position).description);
        Picasso.with(mContext).load(charityItems.get(position).image).into(holder.charityItemImg);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonListener.onDeleteIsClick(view, position);
            }
        });
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButtonListener.onUpdateIsClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return charityItems.size();
    }

    public class CharityItemViewHolder extends RecyclerView.ViewHolder
    {   public Button deleteBtn,updateBtn;
        public ImageView charityItemImg;
        public TextView charityItemTxt;
        public CharityItemViewHolder(@NonNull View itemView) {
            super(itemView);
            charityItemImg = (ImageView)itemView.findViewById(R.id.charityItemImg);
            charityItemTxt = (TextView) itemView.findViewById(R.id.charityItemTxt);
            deleteBtn = (Button)itemView.findViewById(R.id.deleteBtn);
            updateBtn = (Button)itemView.findViewById(R.id.updateBtn);
        }
    }
}
