# ImageSelector
##### 1.防微信图片选择
##### 2.拍照
##### 3.图片展示
##### 4.图片剪切
##### 5.图片压缩

### 使用:
```java
ImageSelector.with(activity)
    .options(new SelectImgOptions())
    .compress(new CompressOptions())
    .execute();
```

### 依赖:
```java
compile 'com.github.waterfaity:ImageSelector:2.2.54
```
### 详细:
##### 1.Options(AOptions): 
```java 
//设置进出场动画:启动进场,启动出场,关闭进场,关闭出场(注:如果设置则必须4个动画资源(R.anim.*))
setTransitionAnimRes(int[] transitionAnimRes) 
//设置一个tag  在onActivityResult() 中会接收该tag
setTag(Object object) / getTag()   
//设置屏幕方向 ConstantUtils.ORIENTATION_PORT(默认)/ConstantUtils.ORIENTATION_LAND
setScreenOrientation(int orientation) 

```

##### 2.图片选择(SelectImgOptions):
```java
new SelectImgOptions() 
    //是否包涵gif格式图片
    .setContainsGif(false) 
    //图片加载方式 
    // 1.遍历文件夹 默认 (不用传值) 
    // 2.SELECT_IMG_MODULE_TYPE_CURSOR  调用ContentResolver所有图片资源
    .setModelType(ConstantUtils.SELECT_IMG_MODULE_TYPE_CURSOR)
    //每行数量
    .setGridNum(3)
    //选择图片最大个数
    .setMaxNum(9)
    //搜索深度 setModelType=默认 时生效
    .setSearchDeep(4)
    //是否加载搜索缓存
    .setLoadCache(false)
    //添加搜索文件夹 setModelType=默认 时生效
    .addSearchPaths(searchPathList)
    //添加忽略文件夹
    .addIgnorePaths(ignorePathList) 
```
