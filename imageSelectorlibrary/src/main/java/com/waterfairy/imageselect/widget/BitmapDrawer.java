package com.waterfairy.imageselect.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/1/21 11:32
 * @info:
 */
public class BitmapDrawer implements ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = "BitmapDrawer";
    private float lastX, lastY;
    private ImageView imageView;
    private boolean isDrag;//拖动
    private boolean isZoom;//缩放
    private boolean isFling;//飞滚中
    private GestureDetector gestureDetector;//手势  双击放大/缩小
    private ScaleGestureDetector scaleGestureDetector;//手势  双指放大
    private int mLastPointCount;
    private float[] matrixValues = new float[9];

    private float BIGGER = 6;//最大缩放倍数
    private float SMALLER;//默认是加载图片时的缩放大小
    private float currentSmallScale;
    private float currentScale;//当前缩放
    private RectF lineRect;//剪切线rect
    private RectF oriRect;
    private boolean canTouchArriveEdge = true;
    private boolean enable = true;
    private GestureFlingTool gestureFlingTool;//自动滑动衰减


    public BitmapDrawer(ImageView imageView) {
        this.imageView = imageView;
        gestureDetector = new GestureDetector(imageView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (onDrawerChangeListener != null) onDrawerChangeListener.onDoubleClickListener();
                float scale = getScale();
                initSmaller(scale);
                if (scale < BIGGER / 4F - 0.1F) {
                    //->2
                    startAnim(scale, BIGGER / 4F, e.getX(), e.getY());
                } else if (scale < BIGGER - 0.1F) {
                    //->4
                    startAnim(scale, BIGGER, e.getX(), e.getY());
                } else {
                    //->1
                    startAnim(scale, Math.max(SMALLER, currentSmallScale), e.getX(), e.getY());
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                if (onDrawerChangeListener != null) onDrawerChangeListener.onLongClickListener();
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (onDrawerChangeListener != null) onDrawerChangeListener.onClickListener();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                isFling = true;
                Log.i(TAG, "onFling: " + velocityX + "," + velocityY + " -- " + e1.getX() + "," + e1.getY() + "  --  " + e2.getX() + "," + e2.getY());
                if (gestureFlingTool == null) {
                    gestureFlingTool = new GestureFlingTool();
                    gestureFlingTool.setOnFlingListener(new GestureFlingTool.OnFlingListener() {
                        @Override
                        public void onFling(int x, int y) {
                            boolean move = move(x, y);
//                            if (!move) {
//                                gestureFlingTool.stop();
//                            }
                        }

                        @Override
                        public void onFlingEnd() {
                            isFling = false;
                        }
                    });
                }
                gestureFlingTool.startFling(e1, e2, velocityX, velocityY);

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(imageView.getContext(), this);
    }

    /**
     * 初始化最小区域额
     *
     * @param scale
     */
    private void initSmaller(float scale) {
        if (SMALLER == 0) {
            SMALLER = scale;
            oriRect = getMatrixRectF();
        }
    }

    private void startAnim(final float fromScale, final float targetScale, final float x, final float y) {
        Log.i(TAG, "startAnim: " + isZoom + "   " + isDrag + "    " + fromScale + " " + targetScale);
        if (isZoom || isDrag) return;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(1F);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                Matrix matrix = imageView.getImageMatrix();
                float targetScaleTemp = (targetScale - fromScale) * value + fromScale;
                targetScaleTemp = targetScaleTemp / getScale();
                Log.i(TAG, "onAnimationUpdate: " + targetScaleTemp + "   " + targetScaleTemp + "   " + x + "   " + y);

                matrix.postScale(targetScaleTemp, targetScaleTemp, x, y);
                checkBorderAndCenterWhenScale();
                onDrawerChangeListener.onBitmapChange(BitmapDrawer.this);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isZoom = false;
            }
        });
        valueAnimator.start();
        isZoom = true;
    }

    private OnDrawerChangeListener onDrawerChangeListener;

    public boolean isCanMove(MotionEvent event) {
        if (!enable) return false;
        //双击事件进行关联   /  单击
        if (gestureDetector.onTouchEvent(event)) {
            //如果是双击的话就直接不向下执行了
            return true;
        }
        //求平均点
        int x = 0, y = 0;
        int pointCount = event.getPointerCount();
        for (int i = 0; i < pointCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointCount;
        y = y / pointCount;
        if (event.getPointerCount() == 2) {
            //双指缩放
            scaleGestureDetector.onTouchEvent(event);
        }
        if (mLastPointCount != pointCount) {
            //手指数改变 重记 防止错误
            lastX = x;
            lastY = y;
        }
        mLastPointCount = pointCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //触摸是 飞滚 不再处理
                if (gestureFlingTool != null) gestureFlingTool.stop();
                lastX = event.getX();
                lastY = event.getY();
                isDrag = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //所有的touch
                isDrag = false;
                isZoom = false;
                break;
            case MotionEvent.ACTION_MOVE:
                boolean move = move(x, y);

                if (!canTouchArriveEdge && !move) {
                    //校验是否低达边缘
                    lastX = x;
                    lastY = y;
                    return false;
                }
                break;

        }
        lastX = x;
        lastY = y;
        return isDrag || isZoom;
    }

    private boolean move(int x, int y) {
        boolean move = true;
        if ((isDrag || isFling) && !isZoom) {
            RectF matrixRectF = getMatrixRectF();

            float dx = x - lastX;
            float dy = y - lastY;


            boolean isHorMove = Math.abs(dx) > Math.abs(dy);//水平移动

            if (matrixRectF.width() < imageView.getWidth()) {
                dx = 0;
            } else {
                if (dx > 0) {
                    //右移
                    if (matrixRectF.left + dx > 0) {
                        //左侧超界
                        dx = -matrixRectF.left;
                        if (isHorMove)
                            move = false;
                    }
                } else if (dx < 0) {
                    //左移
                    if (matrixRectF.right + dx < imageView.getWidth()) {
                        dx = imageView.getWidth() - matrixRectF.right;
                        if (isHorMove)
                            move = false;
                    }
                }
            }
            if (matrixRectF.height() < imageView.getHeight()) {
                dy = 0;
            } else {
                if (dy > 0) {
                    //下移
                    if (matrixRectF.top + dy > 0) {
                        //上侧超界
                        dy = -matrixRectF.top;
                        if (!isHorMove)
                            move = false;
                    }
                } else if (dy < 0) {
                    //上移
                    if (matrixRectF.bottom + dy < imageView.getHeight()) {
                        dy = imageView.getHeight() - matrixRectF.bottom;
                        if (!isHorMove)
                            move = false;
                    }
                }
            }
            //单指移动
            Matrix imageMatrix = imageView.getImageMatrix();
            imageMatrix.postTranslate(dx, dy);
            onDrawerChangeListener.onBitmapChange(this);
        }
        return move;
    }


    public void setOnDrawerChangeListener(OnDrawerChangeListener onLineChangeListener) {
        this.onDrawerChangeListener = onLineChangeListener;
    }


    private float getSmallScale() {
        return Math.max(SMALLER, currentSmallScale);
    }

    /**
     * 根据手势缩放中
     *
     * @param detector
     * @return
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        Matrix matrix = imageView.getImageMatrix();
        currentScale = getScale();
        initSmaller(currentScale);
        float scaleFactor = detector.getScaleFactor();
        //缩小  放大
        if ((currentScale > getSmallScale() && scaleFactor < 1.0F) || (currentScale < BIGGER && scaleFactor > 1.0F)) {
            if (currentScale * scaleFactor < getSmallScale()) {
                scaleFactor = getSmallScale() / currentScale;
            }
            if (currentScale * scaleFactor > BIGGER) {
                scaleFactor = BIGGER / currentScale;
            }
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            onDrawerChangeListener.onBitmapChange(this);
        }
        return true;
    }

    /**
     * 在缩放时，进行图片显示范围的控制
     */
    private void checkBorderAndCenterWhenScale() {

        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = imageView.getWidth();
        int height = imageView.getHeight();

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rect.width() < width) {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
        }
        if (rect.height() < height) {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
        }
//        Log.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);

        imageView.getImageMatrix().postTranslate(deltaX, deltaY);

    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = imageView.getImageMatrix();
        RectF rect = new RectF();
        Drawable d = imageView.getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    public RectF getBitmapRect() {
        return getMatrixRectF();
    }

    /**
     * 缩放开始
     *
     * @param detector
     * @return
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        isZoom = true;
        return true;
    }

    /**
     * 缩放结束
     *
     * @param detector
     */
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        isZoom = false;
    }

    public void freshLineRect(RectF lineRect) {
        this.lineRect = lineRect;
        //计算最小缩放大小
        calcMinScale();
    }

    private void calcMinScale() {
        if (lineRect != null && SMALLER != 0 && oriRect != null) {
            float centerX = oriRect.centerX();
            float centerY = oriRect.centerY();
            //left
            float scaleLeft = 0;
            if (lineRect.left < centerX) {
                scaleLeft = SMALLER / (centerX - oriRect.left) * (centerX - lineRect.left);
            }
            //top
            float scaleTop = 0;
            if (lineRect.top < centerY) {
                scaleTop = SMALLER / (centerY - oriRect.top) * (centerY - lineRect.top);
            }
            //right
            float scaleRight = 0;
            if (lineRect.right > centerX) {
                scaleRight = SMALLER / (oriRect.right - centerX) * (lineRect.right - centerX);
            }
            //bottom
            float scaleBottom = 0;
            if (lineRect.bottom > centerY) {
                scaleBottom = SMALLER / (oriRect.bottom - centerY) * (lineRect.bottom - centerY);
            }
            currentSmallScale = Math.max(Math.max(scaleTop, scaleBottom), Math.max(scaleLeft, scaleRight));
        }
    }

    public void setCanTouchArriveEdge(boolean canTouchArriveEdge) {
        this.canTouchArriveEdge = canTouchArriveEdge;
    }

    public boolean getCanTouchArriveEdge() {
        return canTouchArriveEdge;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean getEnable() {
        return enable;
    }

    public interface OnDrawerChangeListener {
        void onBitmapChange(BitmapDrawer bitmapDrawer);

        void onClickListener();

        void onDoubleClickListener();

        boolean onLongClickListener();
    }

    /**
     * 获得当前的缩放比例
     *
     * @return
     */
    private float getScale() {
        imageView.getImageMatrix().getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }
}
