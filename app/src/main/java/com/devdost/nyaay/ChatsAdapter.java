package com.devdost.nyaay;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> names;
    private ArrayList<Integer> status;
    private Typeface bold;

    public ChatsAdapter(Context act, ArrayList<String> names, ArrayList<Integer> status) {
        this.context = act;
        this.names = names;
        this.status = status;
    }


    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_row, viewGroup, false);
        bold = Typeface.createFromAsset(viewGroup.getContext().getAssets(), "fonts/semibold.ttf");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.initial.setText(String.valueOf(names.get(i).charAt(0)).toUpperCase());
        viewHolder.name.setText(names.get(i));

        if (status.get(i) == 0) {
            viewHolder.offline.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.online.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView initial;
        private TextView name;
        private ImageView offline;
        private ImageView online;

        public ViewHolder(View view) {
            super(view);
            initial = (TextView) view.findViewById(R.id.initial);
            initial.setTypeface(bold);
            name = (TextView) view.findViewById(R.id.name);
            name.setTypeface(bold);
            offline = view.findViewById(R.id.offline);
            online = view.findViewById(R.id.online);
        }
    }

}