# Android-Navigation-Fragment-Share-Element-Example
## 说明

Android 使用Navigation导航切换Fragment中使用共享元素过渡动画的例子：将在listFragment的RecyclerView的Item共享元素过渡到pagerFragment的Viewpager的item中去。而且Viewpager经过任意切换后依然可以过渡回listFragment中去。

## 效果图

![效果图](markdownAssets/result.gif)

## 重点

1. listFragment中的ExitSharedElementCallback 与 pagerFragment的EnterSharedElementCallback 的name进入与返回都必须相同，可以更改view，但若进入与返回的name不同，则无法实现返回的过渡。

2. Fragment不像Activity能直接设Result，但有` setFragmentResultListener() `这样的机制来回传参数，而且是在Fragment的onStart时期调用，故此时可以用` postponeEnterTransition() `延迟动画播放。
