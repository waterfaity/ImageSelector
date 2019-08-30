package com.waterfairy.imageselect.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/1/20 16:53
 * @info:
 */
public class LineDrawer {
    private static final String TAG = "lineDrawer";
    private float mDensity;
    //最大边界
    private RectF mMaxRect;
    //移动边界
    private RectF mRectF;
    //线
    private Paint mLinePaint;
    //阴影
    private Path mShadowPath;
    private Paint mShadowPaint;
    //矩形 边角
    private Paint mAreaPaint;
    private Path mAreaPath;
    //最小宽高
    private int minWidth, minHeight;
    //边缘线宽
    private int mLineWidthOutside;
    //内部线宽
    private int mLineWidthInside;
    //中间圆点半径
    private int circleRadius;
    //距离边宽度
    private int maxRectMargin;
    //距离bitmap边宽度
    private int bitmapMargin;
    //线两侧宽度之间的距离   增加可移动点的范围
    private int touchRadius;
    //线颜色
    private int colorTheme;

    //移动中
    private boolean isMoving;
    private MoveStyle moveStyle;
    private OnLineChangeListener onLineChangeListener;
    //移动旧坐标
    private float lastX;
    private float lastY;
    private RectF bitmapRect;
    private float ratio;

    public void setOnLineChangeListener(OnLineChangeListener onLineChangeListener) {
        this.onLineChangeListener = onLineChangeListener;
    }

    public LineDrawer(float density) {
        mDensity = density;
        initPaint();
        initSize();

    }

    private void initPaint() {
        colorTheme = Color.WHITE;
        mLinePaint = new Paint();
        mLinePaint.setColor(colorTheme);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mAreaPaint = new Paint();
        mAreaPaint.setColor(colorTheme);
        mAreaPath = new Path();

        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.parseColor("#44000000"));
        mShadowPath = new Path();
    }

    /**
     * 宽 = 外部线*2 + 内部线*2 + 矩形()*3
     */
    private void initSize() {
        mLineWidthInside = (int) (1 * mDensity);
        mLineWidthOutside = (int) (2 * mDensity);
        //最小宽高
        minWidth = (int) (mDensity * 50);
        minHeight = minWidth;
        circleRadius = (int) (mDensity * 3);
//        maxRectMargin = (int) (mDensity * 5);
//        bitmapMargin = (int) (mDensity * 5);
        touchRadius = (int) (mDensity * 13);
    }

    /**
     * 当前边界
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setBounds(float left, float top, float right, float bottom) {
        if (mRectF == null) {
            mRectF = new RectF(left, top, right, bottom);
        } else {
            mRectF.set(left, top, right, bottom);
        }
    }

    /**
     * view的边界 最大边界
     *
     * @param width
     * @param height
     */
    public void setMaxRect(int width, int height) {
        if (mMaxRect == null) {
            mMaxRect = new RectF();
        }
        mMaxRect.set(0, 0, width, height);
    }

    public void draw(Canvas canvas) {
        if (canvas == null || mRectF == null || mRectF.width() <= 0 || mRectF.height() <= 0) return;
        //画边界
        mLinePaint.setStrokeWidth(mLineWidthOutside);
        canvas.drawRect(mRectF.left + mDensity, mRectF.top + mDensity, mRectF.right - mDensity, mRectF.bottom - mDensity, mLinePaint);
        //画内部线条
        mLinePaint.setStrokeWidth(mLineWidthInside);

        //1 横
        float verMean = mRectF.height() / 3F;
        canvas.drawLine(mRectF.left, mRectF.top + verMean, mRectF.right, mRectF.top + verMean, mLinePaint);
        canvas.drawLine(mRectF.left, mRectF.bottom - verMean, mRectF.right, mRectF.bottom - verMean, mLinePaint);
        //2 竖
        float horMean = mRectF.width() / 3F;
        canvas.drawLine(mRectF.left + horMean, mRectF.top, mRectF.left + horMean, mRectF.bottom, mLinePaint);
        canvas.drawLine(mRectF.right - horMean, mRectF.top, mRectF.right - horMean, mRectF.bottom, mLinePaint);

        //画中间交叉圆 左上 右上 左下 右下
        mAreaPaint.reset();
        mAreaPaint.setColor(colorTheme);
        mAreaPath.reset();
        mAreaPath.addCircle(mRectF.left + horMean, mRectF.top + verMean, circleRadius, Path.Direction.CW);
        mAreaPath.addCircle(mRectF.right - horMean, mRectF.top + verMean, circleRadius, Path.Direction.CW);
        mAreaPath.addCircle(mRectF.left + horMean, mRectF.bottom - verMean, circleRadius, Path.Direction.CW);
        mAreaPath.addCircle(mRectF.right - horMean, mRectF.bottom - verMean, circleRadius, Path.Direction.CW);
        canvas.drawPath(mAreaPath, mAreaPaint);
        //画阴影
        //1外圈
        mShadowPath.reset();
        mShadowPath.moveTo(0, 0);
        mShadowPath.lineTo(mMaxRect.right, 0);
        mShadowPath.lineTo(mMaxRect.right, mMaxRect.bottom);
        mShadowPath.lineTo(0, mMaxRect.bottom);
        //2内圈
        mShadowPath.lineTo(0, mRectF.top);
        mShadowPath.lineTo(mRectF.left, mRectF.top);
        mShadowPath.lineTo(mRectF.left, mRectF.bottom);
        mShadowPath.lineTo(mRectF.right, mRectF.bottom);
        mShadowPath.lineTo(mRectF.right, mRectF.top);
        mShadowPath.lineTo(0, mRectF.top);
        mShadowPath.lineTo(0, 0);
        canvas.drawPath(mShadowPath, mShadowPaint);
    }

    /**
     * 是否按在可移动的区域
     *
     * @param event
     */
    public boolean isCanMove(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = false;
                //按下处理
                //1.判断是否在圆上
                if (MoveStyle.NO != (moveStyle = isInCircle(event.getX(), event.getY()))) {
                    isMoving = true;
                }
                //2.判断是否在 四角及四边线
                if (!isMoving && MoveStyle.NO != (moveStyle = isInCornerOrLine(event.getX(), event.getY()))) {
                    isMoving = true;
                }
                lastX = event.getX();
                lastY = event.getY();
                onLineChangeListener.onLineChange(this);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    //移动处理
                    if (ratio == 0)
                        handleMove(event.getX(), event.getY());
                    else handleMoveWithRadio(event.getX(), event.getY());
                    onLineChangeListener.onLineChange(this);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMoving)
                    onLineChangeListener.onLineChange(this);
                isMoving = false;
                break;
        }
        return isMoving;
    }

    /**
     * 比例拉伸移动
     *
     * @param x
     * @param y
     */
    private void handleMoveWithRadio(float x, float y) {
        float left = mRectF.left;
        float top = mRectF.top;
        float right = mRectF.right;
        float bottom = mRectF.bottom;
        switch (moveStyle) {
            case LEFT:
            case LT:
                //计算左侧 x坐标(注: 该left 已经做过边界校验)
                left = getLeft(x, left, right);
                //根据比例计算上y坐标
                top = bottom - (right - left) / ratio;
                //校验此时top坐标是否超过区域边界(最大框+maxRectMargin 对比 图片区域 的最大值)
                if (top < Math.max(mMaxRect.top + maxRectMargin, bitmapRect.top + bitmapMargin)) {
                    //超过边界 则 top 设置为边界值
                    top = Math.max(mMaxRect.top + maxRectMargin, bitmapRect.top + bitmapMargin);
                    //根据top 反求left值
                    left = right - (bottom - top) * ratio;
                }
                break;
            case RB:
            case RIGHT:
                right = getRight(x, left, right);
                bottom = top + (right - left) / ratio;
                if (bottom > Math.min(mMaxRect.bottom - maxRectMargin, bitmapRect.bottom - bitmapMargin)) {
                    bottom = Math.min(mMaxRect.bottom - maxRectMargin, bitmapRect.bottom - bitmapMargin);
                    right = (bottom - top) * ratio + left;
                }
                break;
            case LB:
            case BOTTOM:
                bottom = getBottom(y, top, bottom);
                left = right - (bottom - top) * ratio;
                if (left < Math.max(mMaxRect.left + maxRectMargin, bitmapRect.left + bitmapMargin)) {
                    left = Math.max(mMaxRect.left + maxRectMargin, bitmapRect.left + bitmapMargin);
                    bottom = top + (right - left) / ratio;
                }
                break;
            case RT:
            case TOP:
                top = getTop(y, top, bottom);
                right = left + (bottom - top) * ratio;
                if (right > Math.min(mMaxRect.right - maxRectMargin, bitmapRect.right - bitmapMargin)) {
                    right = Math.min(mMaxRect.right - maxRectMargin, bitmapRect.right - bitmapMargin);
                    top = bottom - (right - left) / ratio;
                }
                break;
            case ALL:
                //判断
                float withTemp = right - left;
                float heightTemp = bottom - top;
                left += (x - lastX);
                right += (x - lastX);
                //判断到达view的边缘(左右)
                if (x < lastX && left < maxRectMargin) {
                    //到达view左侧
                    left = maxRectMargin;
                    right = maxRectMargin + withTemp;
                } else if (x > lastX && right > mMaxRect.right - maxRectMargin) {
                    //到达view右侧
                    right = mMaxRect.right - maxRectMargin;
                    left = right - withTemp;
                } else if (x < lastX && left < bitmapRect.left + bitmapMargin) {
                    //到达bitmap左侧
                    left = bitmapRect.left + bitmapMargin;
                    right = left + withTemp;
                } else if (x > lastX && right > bitmapRect.right - bitmapMargin) {
                    //到达bitmap右侧
                    right = bitmapRect.right - bitmapMargin;
                    left = right - withTemp;
                }
                top += (y - lastY);
                bottom += (y - lastY);

                //判断到达view的边缘(上下)
                if (y < lastY && top < maxRectMargin) {
                    //到达view上侧
                    top = maxRectMargin;
                    bottom = maxRectMargin + heightTemp;
                } else if (y > lastY && bottom > mMaxRect.bottom - maxRectMargin) {
                    //到达view下侧
                    bottom = mMaxRect.bottom - maxRectMargin;
                    top = bottom - heightTemp;
                } else if (y < lastY && top < bitmapRect.top + bitmapMargin) {
                    //到达bitmap上侧
                    top = bitmapRect.top + bitmapMargin;
                    bottom = top + heightTemp;
                } else if (y > lastY && bottom > bitmapRect.bottom - bitmapMargin) {
                    //到达bitmap下侧
                    bottom = bitmapRect.bottom - bitmapMargin;
                    top = bottom - heightTemp;
                }
                break;
        }
        mRectF.set(left, top, right, bottom);
        lastX = x;
        lastY = y;
    }

    /**
     * 移动坐标
     *
     * @param x
     * @param y
     */
    private void handleMove(float x, float y) {
        float left = mRectF.left;
        float top = mRectF.top;
        float right = mRectF.right;
        float bottom = mRectF.bottom;
        //1-8 判断边缘(动1-2边)
        //9 :all  判断移向的边缘(四边都要移动)

        switch (moveStyle) {
            case LEFT:
                left = getLeft(x, left, right);
                break;
            case TOP:
                top = getTop(y, top, bottom);
                break;
            case RIGHT:
                right = getRight(x, left, right);
                break;
            case BOTTOM:
                bottom = getBottom(y, top, bottom);
                break;
            case LT:
                top = getTop(y, top, bottom);
                left = getLeft(x, left, right);
                break;
            case RT:
                top = getTop(y, top, bottom);
                right = getRight(x, left, right);
                break;
            case LB:
                left = getLeft(x, left, right);
                bottom = getBottom(y, top, bottom);
                break;
            case RB:
                right = getRight(x, left, right);
                bottom = getBottom(y, top, bottom);
                break;
            case ALL:
                //判断
                float withTemp = right - left;
                float heightTemp = bottom - top;
                left += (x - lastX);
                right += (x - lastX);
                //判断到达view的边缘(左右)
                if (x < lastX && left < maxRectMargin) {
                    //到达view左侧
                    left = maxRectMargin;
                    right = maxRectMargin + withTemp;
                } else if (x > lastX && right > mMaxRect.right - maxRectMargin) {
                    //到达view右侧
                    right = mMaxRect.right - maxRectMargin;
                    left = right - withTemp;
                } else if (x < lastX && left < bitmapRect.left + bitmapMargin) {
                    //到达bitmap左侧
                    left = bitmapRect.left + bitmapMargin;
                    right = left + withTemp;
                } else if (x > lastX && right > bitmapRect.right - bitmapMargin) {
                    //到达bitmap右侧
                    right = bitmapRect.right - bitmapMargin;
                    left = right - withTemp;
                }
                top += (y - lastY);
                bottom += (y - lastY);

                //判断到达view的边缘(上下)
                if (y < lastY && top < maxRectMargin) {
                    //到达view上侧
                    top = maxRectMargin;
                    bottom = maxRectMargin + heightTemp;
                } else if (y > lastY && bottom > mMaxRect.bottom - maxRectMargin) {
                    //到达view下侧
                    bottom = mMaxRect.bottom - maxRectMargin;
                    top = bottom - heightTemp;
                } else if (y < lastY && top < bitmapRect.top + bitmapMargin) {
                    //到达bitmap上侧
                    top = bitmapRect.top + bitmapMargin;
                    bottom = top + heightTemp;
                } else if (y > lastY && bottom > bitmapRect.bottom - bitmapMargin) {
                    //到达bitmap下侧
                    bottom = bitmapRect.bottom - bitmapMargin;
                    top = bottom - heightTemp;
                }
                break;
        }
        mRectF.set(left, top, right, bottom);
        lastX = x;
        lastY = y;
    }

    /**
     * 移动时获取上坐标
     *
     * @param y      手指y坐标
     * @param top    上
     * @param bottom
     * @return
     */
    private float getTop(float y, float top, float bottom) {
        Log.i(TAG, "getTop: " + y + "   " + top + "  " + bottom);
        top += (y - lastY);
        if (top < maxRectMargin) top = maxRectMargin;
        if (top < bitmapRect.top + bitmapMargin) top = bitmapRect.top + bitmapMargin;
        if (bottom - top < minHeight) {
            top = bottom - minHeight;
        }
        return top;
    }

    private float getBottom(float y, float top, float bottom) {
        bottom += (y - lastY);
        if (bottom > mMaxRect.bottom - maxRectMargin)
            bottom = mMaxRect.bottom - maxRectMargin;
        if (bottom > bitmapRect.bottom - bitmapMargin) bottom = bitmapRect.bottom - bitmapMargin;
        if (bottom - top < minHeight) {
            bottom = top + minHeight;
        }
        return bottom;
    }

    /**
     * @param x     移动的坐标
     * @param left  当前当前边框左坐标
     * @param right 当前当前边框右坐标
     * @return
     */
    private float getLeft(float x, float left, float right) {
        left += (x - lastX);
        if (left < maxRectMargin) left = maxRectMargin;
        if (left < bitmapRect.left + bitmapMargin) left = bitmapRect.left + bitmapMargin;
        if (right - left < minWidth) {
            left = right - minWidth;
        }
        return left;
    }

    private float getRight(float x, float left, float right) {
        right += (x - lastX);
        if (right > mMaxRect.right - maxRectMargin) right = mMaxRect.right - maxRectMargin;
        if (right > bitmapRect.right - bitmapMargin) right = bitmapRect.right - bitmapMargin;
        if (right - left < minWidth) {
            right = left + minWidth;
        }
        return right;
    }

    /**
     * 四角 或 四边线
     *
     * @param x
     * @param y
     * @return
     */
    private MoveStyle isInCornerOrLine(float x, float y) {
        if (isInLineVer(mRectF.left, x)) {
            //在左边竖线上
            if (isInLineHor(mRectF.top, y)) {
                //左边-上边 ->左上角
                return MoveStyle.LT;
            } else if (isInLineHor(mRectF.bottom, y)) {
                //左边-下边 ->左下角
                return MoveStyle.LB;
            } else if (isInside(mRectF.top, mRectF.bottom, y)) {
                //左边-中间 ->左边线
                return MoveStyle.LEFT;
            }
        } else if (isInLineVer(mRectF.right, x)) {
            //在右边竖线上
            if (isInLineHor(mRectF.top, y)) {
                //右边-上边 ->右上角
                return MoveStyle.RT;
            } else if (isInLineHor(mRectF.bottom, y)) {
                //右边-下边 ->右下角
                return MoveStyle.RB;
            } else if (isInside(mRectF.top, mRectF.bottom, y)) {
                //右边-中间 ->右边线
                return MoveStyle.RIGHT;
            }
        } else if (isInLineHor(mRectF.top, y) && isInside(mRectF.left, mRectF.right, x)) {
            //上边线
            return MoveStyle.TOP;
        } else if (isInLineHor(mRectF.bottom, y) && isInside(mRectF.left, mRectF.right, x)) {
            //下边线
            return MoveStyle.BOTTOM;
        }
        return MoveStyle.NO;
    }

    /**
     * 是否在两线中间
     *
     * @param line1
     * @param line2
     * @param value
     * @return
     */
    private boolean isInside(float line1, float line2, float value) {
        return value > line1 - touchRadius && value < line2 + touchRadius;
    }

    /**
     * 是否在横线上
     *
     * @param y  目标线
     * @param y1
     * @return
     */
    private boolean isInLineHor(float y, float y1) {
        return y1 >= y - touchRadius && y1 <= y + touchRadius;
    }

    /**
     * 是否在竖线上
     *
     * @param x
     * @param x1
     * @return
     */
    private boolean isInLineVer(float x, float x1) {
        return x1 >= x - touchRadius && x1 <= x + touchRadius;
    }

    /**
     * 是否在圆上(四个圆)
     *
     * @param x
     * @param y
     * @return
     */
    private MoveStyle isInCircle(float x, float y) {
        //1 横
        float verMean = mRectF.height() / 3F;
        float horMean = mRectF.width() / 3F;

        float x1 = mRectF.left + horMean;
        float x2 = mRectF.right - horMean;
        float y1 = mRectF.top + verMean;
        float y2 = mRectF.bottom - verMean;
        //左上 右上 左下 右下
        if (isInCircleSub(x1, y1, x, y) ||
                isInCircleSub(x1, y2, x, y) ||
                isInCircleSub(x2, y1, x, y) ||
                isInCircleSub(x2, y2, x, y)) {
            return MoveStyle.ALL;
        } else {
            return MoveStyle.NO;
        }
    }

    /**
     * 是否在圆上
     *
     * @param x
     * @param y
     * @param xTemp
     * @param yTemp
     * @return
     */
    private boolean isInCircleSub(float x, float y, float xTemp, float yTemp) {
        return xTemp >= (x - touchRadius) && xTemp <= (x + touchRadius) && yTemp >= (y - touchRadius) && yTemp <= (y + touchRadius);
    }


    public void freshBitmapRect(RectF bitmapRect) {
        this.bitmapRect = bitmapRect;
    }

    public RectF getLineRect() {
        return mRectF;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        if (ratio > 1) minWidth = (int) (minHeight * ratio);
        else minHeight = (int) (minWidth / ratio);
    }

    public float getRatio() {
        return ratio;
    }

    public interface OnLineChangeListener {
        void onLineChange(LineDrawer lineDrawer);
    }


    public enum MoveStyle {
        NO(0),
        LEFT(1),//左边界
        TOP(2),//上
        RIGHT(3),//右
        BOTTOM(4),//下
        //左上 右上 左下 右下
        LT(5),
        RT(6),
        LB(7),
        RB(8),
        ALL(9);//整体移动

        MoveStyle(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;
    }

    @Override
    public String toString() {
        return "moving: " + isMoving + " ; bounds: (" + (int) mRectF.left + "," + (int) mRectF.top + "," + (int) mRectF.right + "," + (int) mRectF.bottom + ") ; x: " + (int) lastX + " y: " + (int) lastY;
    }
}
