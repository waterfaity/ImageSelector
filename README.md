# ImageSelector
##### 1.防微信图片选择
##### 2.拍照
##### 3.图片展示
##### 4.图片剪切
##### 5.图片压缩

### 使用:
```java
ImageSelector.with(activity)
    .options(new SelectImgOptions())//处理方式
    .compress(new CompressOptions())//图片压缩
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
    // 2.SELECT_IMG_MODULE_TYPE_CURSOR  调用ContentResolver所有图片资源(建议:速度更快)
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
##### 3.拍照(TakePhotoOptions):
```java
new TakePhotoOptions() 
    .setPathAuthority("")//设置FileProvider 的 authority  (7.0以后的共享文件)
```
##### 4.图片裁剪(CropImgOptions):
```java
new CropImgOptions()
    //裁剪引擎 1.CropImgOptions.CROP_TYPE_SElf本程序处理 2.CROP_TYPE_SYS 系统或第三方处理
    .setCropType(CropImgOptions.CROP_TYPE_SElf))
    //比例宽
    .setAspectX(1)
    //比例高
    .setAspectY(2)
    //保存路径
    .setCropPath("/sdcard/test/img")
    //需要裁剪的图片地址
    .setImgPath(path)
    //CropType = CROP_TYPE_SYS 时使用
    .setPathAuthority(pathName)
    //宽高(功能待完善,注:CropType = CROP_TYPE_SYS 可使用)
    .setWidth(with)
    .setHeight(height)
```
##### 4.图片展示(ShowImgOptions):
```java
ShowImgOptions showImgOptions=new ShowImgOptions()
    //单点图片是否关闭查看
    .setClickToDismiss(true)
    //当前pos
    .setCurrentPos(0)
    //添加图片集合
    .addImgList(resultDatas)
    //是否转场动画
    .setHasTranslateAnim(hasTransAnim)
//如果设置了转场动画,调用以下showImg();没有设置,调用execute()
ImageSelector.with(activity).options(showImgOptions).showImg(imageView,transitionName)
```
##### 5.图片压缩(CompressOptions):
```java
new CompressOptions()
    //最大宽
    .setMaxWidth(1500)
    //最大高
    .setMaxHeight(1500)
    //文件最大
    .setMaxSize(500)
    //压缩路径
    .setCompressPath("/sdcard/test/img");
```


