# ImageSelector
防微信

使用:
-
Intent intent = new Intent(this, ImageSelectActivity.class);

intent.putExtra(ImageSelectActivity.MAX_NUM, 6); //最大选择数量

intent.putExtra(ImageSelectActivity.SCREEN_DIRECTION,ImageSelectActivity.SCREEN_LAND); //方向 水平land/垂直port

intent.putExtra(ImageSelectActivity.RESULT_STRING,"result"); //返回字段 :result 接收:getStringArrayList("result")

intent.putExtra(ImageSelectActivity.GRID_NUM,6); //gridView 水平列数

intent.putExtra(ImageSelectActivity.DEEP,3); //图片扫描深度默认 3层文件夹

startActivityForResult(intent, 1);
依赖:
-
compile 'com.github.waterfaity:ImageSelector:1.4'

