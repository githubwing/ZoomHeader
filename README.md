#zoomheaderview


![image](https://github.com/githubwing/ZoomHeader/raw/master/img.gif)




## [下载体验](https://github.com/githubwing/ZoomHeader/raw/master/app-debug.apk)


**先吐槽下饿了么。不提示左右可以滑动。我还是无意中发现的。不提示我怎么知道可以滑动？？**


这是一个模仿饿了么详情页的例子。**并非一个库，并非拿来就可以用，主要讲解思路以及如何实现，可能有一些细节没有处理。**
讲述了如何实现。具体祥见源码。



### 他是一个Activity还是两个？

相信你肯定有这样的疑问，答案是一个。你看到的中间imageview是viewpager。在Viewpager上面是一个透明的View。当然，这个Activity的背景也是透明的。

### 实现思路

我使用CoordinatorLayout+Behavior实现的。说实话，Behavior真心强大。。


### viewpager+头部

整个实现的思路是这样的。整体布局从上到下依次是：

* 透明View
* viewpager
* RecyclerView


其中透明View和Viewpager 合并成一个自定义的Header。当这个Header上移的时候，图片放大，并且RecyclerView联动上衣，从透明转向并且不透明。


所以首先要定制一个透明的可移动的HeaderView。

在onTouchEvent处理一下手势。。

```java
@Override public boolean onTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        return true;
      case MotionEvent.ACTION_MOVE:
		 if(上下移动到阀值){
		 	展开为详情()
		 }else if(上下滑动到阀值，恢复viewpager){
		 }else if(下滑，则关闭Activity)
```

将header分为三种状态：

* 上移。则展开为详情页。
* 下移，则恢复为viewpager。
* 再下移，则finish Activity。


在上移的过程中，遇到了一点小挑战，这里分享下：

上移的过程中，图片需要放大。但是在做的过程中，不能使用LayoutParams实现。这里就关系到一些动画的小细节。

**动画使用LayoutParams实现是一个禁忌。他会导致不停requestLayout，从而影响UI性能。**

所以这里我的一个解法就是，我放大图片，不是真正的改变ImageView大小，而是去Scale图片。即使看起来变大了，他的View真正大小也不会变。

所以，有一句话叫做**真亦是假、假亦是真** 真真假假，你又何必当真呢？动画效果只要遵循这句话，基本上都是可以实现的。你所看到的效果都是假的。都是障眼法。View变大不是真正的变大。View悬浮不是真正的悬浮(有可能是显隐)。就像变魔术一样。。其实很简单。

接下来又遇到问题了。图片放大了，文字如何对齐？ 文字的位置当然也不能真正改变。所以这里使用TranslationX实现。在图片放大的过程中，使用scale的系数，与两个端点值进行一个线性变化计算。主要文字对齐代码如下：

```java
	  bottom.offsetLeftAndRight(
            (int) (target.getWidth() / 2 - target.getWidth() * (1 + progress) / 2
                + MarginConfig.MARGIN_LEFT_RIGHT - bottom.getX()));

```

第二个点。就是在图片放大过程中，底部文字和按钮左右padding不能变。这也是我没有封装成一个拿来就用的View的原因(其实还是水平不够)。因为这些空间需要全部按照上方的方法进行动态计算。。所以也是比较坑爹的。。

### ViewPager

拿了网上一个画廊的效果。直接

```java
    setPageTransformer(true, new ZoomOutPageTransformer());
```

这里注意，需要改变一下view的绘制顺序，保证当前view是最后绘制处于最上层

```java
/改变系统绘制顺序
  @Override protected int getChildDrawingOrder(int childCount, int i) {

    int position = getCurrentItem();
    if(position<0){
      return i;
    }else{
      if(i == childCount - 1){//这是最后一个需要刷新的item
        if(position>i){
          position=i;
        }
        return position;
      }
      if(i == position){//这是原本要在最后一个刷新的item
        return childCount - 1;
      }
    }
    return i;
  }
}

```

### RecyclerView

RecyclerView最开始是完全透明的。并且跟随HeaderView上移而上移，在上移的过程中渐渐显示出来。 需要监听RecyclerView滚动，当RecyclerView滚动到顶部的时候。告知Header，该恢复最初原样了。

```java
@Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target,
      float velocityX, float velocityY, boolean consumed) {

    //向下Fling并且到顶部
    if (velocityY < 0 && ((RecyclerView) target).getChildAt(0).getY() == 0) {
      mDependency.restore(mDependency.getY());
    }
    return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
  }

  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
      int dx, int dy, int[] consumed) {

    //如果在顶部
    if (((RecyclerView) target).getChildAt(0).getY() == 0) {
      //向下滑动
      if (dy < 0) {
        mDependency.setY(mDependency.getY() - dy);
        //小于阀值
        if (mDependency.getY() < 500) {
          mDependency.restore(mDependency.getY());
        }
      }
    }
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
  }
}
```


### Behavior

让header和RecyclerView关联起来的就是Behavior了。Behavior之前写过几篇介绍过了，这里就不再啰嗦。

denpendcy为HeaderView。并且监听RecyclerView的滑动。


具体的细节还是看源码吧~

如果你觉得还不错，欢迎Star  
欢迎加入我的qq群: 425983695



# License

    Copyright 2016 androidwing1992

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
