package com.wingsofts.zoomimageheader;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wing on 12/4/16.
 */
class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
  private final static int TYPE_ADDRESS = 0;
  private final static int TYPE_TIME = 1;
  private final static int TYPE_RECOMMEND = 2;
  private final static int TYPE_COMMENT = 3;

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    switch (viewType) {
      case TYPE_ADDRESS:
        return new ViewHolder(inflater.inflate(R.layout.item_address, parent, false));
      case TYPE_TIME:
        return new ViewHolder(inflater.inflate(R.layout.item_time, parent, false));
      case TYPE_RECOMMEND:
        return new ViewHolder(inflater.inflate(R.layout.item_recommend, parent, false));
      case TYPE_COMMENT:
        return new ViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
    }

    return new ViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
  }

  @Override public int getItemCount() {
    return 10;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View view) {
      super(view);
    }
  }

  @Override public int getItemViewType(int position) {

    switch (position) {
      case 0:
        return TYPE_ADDRESS;
      case 1:
        return TYPE_TIME;
      case 2:
        return TYPE_RECOMMEND;
      case 3:
        return TYPE_COMMENT;
    }

    return TYPE_COMMENT;
  }
}
