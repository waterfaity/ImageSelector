# ImageSelector
防微信

### 使用:
```java
Intent intent = new Intent(this, ImageSelectActivity.class);

intent.putExtra(ImageSelectActivity.MAX_NUM, 9); //最大选择数量(默认9)

intent.putExtra(ImageSelectActivity.SCREEN_DIRECTION,ImageSelectActivity.SCREEN_LAND); //方向 水平land/垂直port

intent.putExtra(ImageSelectActivity.RESULT_STRING,"data"); //返回字段 :data(默认) 接收:getStringArrayList("data")

intent.putExtra(ImageSelectActivity.GRID_NUM,3); //gridView 水平列数 (默认3)

intent.putExtra(ImageSelectActivity.DEEP,3); //图片扫描深度默认 3(默认)层文件夹

startActivityForResult(intent, 1);
```

### 依赖:
```java
compile 'com.github.waterfaity:ImageSelector:1.4'
```
