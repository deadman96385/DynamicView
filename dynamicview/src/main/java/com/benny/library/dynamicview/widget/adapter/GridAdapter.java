package com.benny.library.dynamicview.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.parser.ViewInflater;

import org.json.JSONArray;
import org.json.JSONObject;

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private JSONArray dataSource = new JSONArray();
    private ViewInflater inflater;

    public void setDataSource(JSONArray source) {
        dataSource = source;
        notifyDataSetChanged();
    }

    public void setInflater(ViewInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = inflater.inflate(parent.getContext(), parent);
            return new ViewHolder(view);
        }
        catch (Exception e) {
            Log.e("GridAdapter", "create view exception: " + e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).notifyDataChange(inflater, getItem(position));
    }

    @Override
    public int getItemCount() {
        return inflater == null || dataSource.length() == 0 ? 0 : dataSource.length();
    }

    public JSONObject getItem(int position) {
        return dataSource.optJSONObject(position);
    }

    public int getSpanSize(int position) {
        return getItem(position).optInt("colspan", 1);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
        }

        void notifyDataChange(ViewInflater inflater, JSONObject data) {
            inflater.bind(itemView, data);
        }
    }
}
