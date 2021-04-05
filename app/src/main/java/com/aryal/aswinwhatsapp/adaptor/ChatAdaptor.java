package com.aryal.aswinwhatsapp.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryal.aswinwhatsapp.R;
import com.aryal.aswinwhatsapp.models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdaptor extends RecyclerView.Adapter{
    ArrayList<Messages> messages;
    Context context;
    String receiverId;
    int senderViewType=1;
    int receiverViewType=2;

    public ChatAdaptor(ArrayList<Messages> messages, Context context, String receiverId) {
        this.messages = messages;
        this.context = context;
        this.receiverId = receiverId;
    }

    public ChatAdaptor(ArrayList<Messages> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==senderViewType){
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else{

                View view= LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
                return new ReceiverViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      final   Messages message=messages.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderRoom=FirebaseAuth.getInstance().getUid()+receiverId;
                                database.getReference().child("chats").child(senderRoom)
                            .child(message.getMessageId()).setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
                return true;
            }
        });


        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMessage.setText(message.getMessage());
        }else{
            ((ReceiverViewHolder)holder).receiverMessage.setText(message.getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position){
            if (messages.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) {
                return senderViewType;

            } else {
                return receiverViewType;
            }

        }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
    TextView receiverMessage,receiverTime;

    public ReceiverViewHolder(@NonNull View itemView) {
        super(itemView);
        receiverMessage=itemView.findViewById(R.id.receiverText);
        receiverTime=itemView.findViewById(R.id.receiverTime);

    }}
    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage=itemView.findViewById(R.id.senderText);


        }
}
}
