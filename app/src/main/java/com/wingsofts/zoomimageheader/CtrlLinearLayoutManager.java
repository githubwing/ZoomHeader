package com.wingsofts.zoomimageheader;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 用来控制recyclerView可否滑动
 * Created by wing on 12/7/16.
 */
public class CtrlLinearLayoutManager extends LinearLayoutManager {
  private boolean isScrollEnabled = true;

  public CtrlLinearLayoutManager(Context context) {
    super(context);
  }

  public void setScrollEnabled(boolean flag) {
    this.isScrollEnabled = flag;
  }

  @Override
  public boolean canScrollVertically() {
    //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
    return isScrollEnabled && super.canScrollVertically();
  }
}
