package com.source.studsimulator.ui.fragments;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.source.studsimulator.R;
import com.source.studsimulator.model.entity.Payable;

import java.lang.ref.WeakReference;
import java.util.List;

public class OneActiveButtonAdapter
        extends RecyclerView.Adapter<OneActiveButtonAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button button;

        ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.button);
        }
    }

    private List<Payable> buttons;
    private int indexOfActivatedButton = -1;
    AdapterListener adapterListener;

    public void setIndexOfActivatedButton(int indexOfActivatedButton) {
        if (indexOfActivatedButton == this.indexOfActivatedButton) {
            this.indexOfActivatedButton = -1;
        } else {
            this.indexOfActivatedButton = indexOfActivatedButton;
        }
    }

    public OneActiveButtonAdapter(List<Payable> buttons) {
        this.buttons = buttons;
    }

    public AdapterListener getAdapterListener() {
        return adapterListener;
    }

    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ind) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buttons_linear_layout,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int ind) {
        viewHolder.button.setText(buttons.get(ind).getTitle());
        viewHolder.button.setBackgroundColor(ind == indexOfActivatedButton ? Color.GREEN : Color.WHITE);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onClick(ind);
            }
        });
    }

    interface AdapterListener {
        void onClick(int position);
    }
}
