package com.waterfairy.imageselect.utils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/16
 * @Description:
 */

public class ConstantUtils {
    //constantData
    public static final String RESULT_STRING = "data";
    public static final String MAX_NUM = "maxNum";//最大数量
    public static final String SEARCH_DEEP = "deep";//文件夹扫描深度
    public static final String SEARCH_PATHS = "searchPaths";//搜索路径
    public static final String LOAD_CACHE = "loadCache";//加载存储的搜索数据
    public static final String IGNORE_PATHS = "ignorePaths";//忽略路径
    public static final String GRID_NUM = "gridNum";//横排数量
    public static final String HAS_SELECT_FILES = "has_select_files";//已经选择的文件


    public static final int DEFAULT_MAX_NUM = 9;//默认最大数量
    public static final int DEFAULT_DEEP = 3;//默认深度
    public static final int DEFAULT_GRID_NUM_MIN = 3;//最小横排数量
    public static final int DEFAULT_GRID_NUM_MAX = 6;//最多

    public static final String STR_URL = "url";//url
    public static final String STR_PATH = "path";//本地地址
    public static final String STR_IMG_TITLE = "imgTitle";//图片title
    public static final String PATH_WX = "/storage/emulated/0/Tencent/MicroMsg/WeiXin";//4级文件
    public static final String PATH_QQ_RECV = "/storage/emulated/0/Tencent/QQfile_recv";//3级文件
    public static final String PATH_QQ_IMAGES = "/storage/emulated/0/Tencent/QQ_Images";//3级文件
    public static final String CURRENT_POS = "current_pos";
    public static final String IMG_PATH = "img_path";

    public static final int REQUEST_SELECT = 18121;//request 选择图片
    public static final int REQUEST_SHOW = 18122;//展示图片
    public static final int REQUEST_TAKE_PHOTO = 18123;//拍照
    public static final int REQUEST_CROP = 18124;//裁剪

    //DEFAULT_SELECT_IMG_TYPE
    public static final int SELECT_IMG_MODULE_TYPE_CURSOR = 1;

    public static final int TYPE_SELECT = 1;
    public static final int TYPE_SHOW = 2;
    public static final int TYPE_TAKE_PHOTO = 3;
    public static final int TYPE_CROP = 4;
    public static final String OPTIONS_BEAN = "options_bean";
    public static final String OPTIONS_TAG = "options_tag";
    public static final String OPTIONS_COMPRESS_BEAN = "options_compress_bean";
}
