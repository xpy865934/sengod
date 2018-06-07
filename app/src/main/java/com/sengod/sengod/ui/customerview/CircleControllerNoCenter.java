package com.sengod.sengod.ui.customerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sengod.sengod.R;

import java.util.ArrayList;
import java.util.List;

public class CircleControllerNoCenter extends View {
    private Context mContext;

    private Paint mPaint;

    private Paint mClickPaint;

    private RectF mRectFBig;

    private RectF mRectFLittle;

    private Path mPathLeft;
    private Path mPathTop;
    private Path mPathRight;
    private Path mPathBottom;
    private Path mPathCenter;

    private float mInitSweepAngle = 0;
    private float mBigSweepAngle = 84;
    private float mLittleSweepAngle = 82;

    private float mBigMarginAngle;
    private float mLittleMarginAngle;

    //Region绘制区域
    private List<Region> mList;

    private Region mAllRegion;

    private Region mRegionTop;
    private Region mRegionRight;
    private Region mRegionLeft;
    private Region mRegionBottom;
    private Region mRegionCenter;

    private int mRadius;

    private static final int LEFT = 0;
    private static final int TOP = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 3;
    private static final int CENTER = 4;

    private int mClickFlag = -1;

    private int mWidth;

    private int mCurX, mCurY;

    private RegionViewClickListener mListener;

    public CircleControllerNoCenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    public CircleControllerNoCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public CircleControllerNoCenter(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public void setListener(RegionViewClickListener mListener) {
        this.mListener = mListener;
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#009688"));

        mClickPaint = new Paint(mPaint);
        mClickPaint.setColor(Color.parseColor("#097c72"));

        mPathLeft = new Path();
        mPathTop = new Path();
        mPathRight = new Path();
        mPathBottom = new Path();
        mPathCenter = new Path();

        mList = new ArrayList<>();

        mRegionLeft = new Region();
        mRegionTop = new Region();
        mRegionRight = new Region();
        mRegionBottom = new Region();
        mRegionCenter = new Region();

        mBigMarginAngle = 90 - mBigSweepAngle;
        mLittleMarginAngle = 90 - mLittleSweepAngle;
    }

    private void initPath() {
        mList.clear();
        // 初始化right路径
        mPathRight.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2,
                mBigSweepAngle);
        mPathRight.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2,
                -mLittleSweepAngle);
        mPathRight.close();

        // 计算right的区域
        mRegionRight.setPath(mPathRight, mAllRegion);
        mList.add(mRegionRight);

        // 初始化bottom路径
        mPathBottom.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2
                + mBigMarginAngle + mBigSweepAngle, mBigSweepAngle);
        mPathBottom.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2
                + mLittleMarginAngle + mLittleSweepAngle, -mLittleSweepAngle);
        mPathBottom.close();

        // 计算bottom的区域
        mRegionBottom.setPath(mPathBottom, mAllRegion);
        mList.add(mRegionBottom);

        // 初始化left路径
        mPathLeft.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2 + 2
                * (mBigMarginAngle + mBigSweepAngle), mBigSweepAngle);
        mPathLeft.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2
                        + 2 * (mLittleMarginAngle + mLittleSweepAngle),
                -mLittleSweepAngle);
        mPathLeft.close();

        // 计算left的区域
        mRegionLeft.setPath(mPathLeft, mAllRegion);
        mList.add(mRegionLeft);

        // 初始化top路径
        mPathTop.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2 + 3
                * (mBigMarginAngle + mBigSweepAngle), mBigSweepAngle);
        mPathTop.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2
                        + 3 * (mLittleMarginAngle + mLittleSweepAngle),
                -mLittleSweepAngle);
        mPathTop.close();

        // 计算top的区域
        mRegionTop.setPath(mPathTop, mAllRegion);
        mList.add(mRegionTop);

        // 初始化center路径
//        mPathCenter.addCircle(0, 0, mRadius, Path.Direction.CW);
//        mPathCenter.close();
//
//        // 计算center的区域
//        mRegionCenter.setPath(mPathCenter, mAllRegion);
//        mList.add(mRegionCenter);
    }

    public void drawText(Canvas canvas){

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(mWidth/150*20);
        textPaint.setStrokeWidth(2.0f);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD));

        //right
        canvas.restore();
        StaticLayout layout = new StaticLayout(mContext.getString(R.string.activity_rectifying_operation_right), textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
//        canvas.translate(mWidth / 4.5f,-mWidth/6);//从mWidth / 5,-mWidth/6开始画

        canvas.translate(mWidth*0.72f,mWidth*0.38f);//从mWidth*0.72f,mWidth*0.57f开始画
        layout.draw(canvas);
        canvas.restore();
        //top
        layout = new StaticLayout(mContext.getString(R.string.activity_rectifying_operation_top), textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
        canvas.translate(mWidth*0.40f,mWidth*0.05f);
        layout.draw(canvas);
        canvas.restore();

        //left
        layout = new StaticLayout(mContext.getString(R.string.activity_rectifying_operation_left), textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
        canvas.translate(mWidth*0.07f,mWidth*0.38f);
        layout.draw(canvas);
        canvas.restore();

        //bottom
        layout = new StaticLayout(mContext.getString(R.string.activity_rectifying_operation_bottom), textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
        canvas.translate(mWidth*0.40f,mWidth*0.72f);
        layout.draw(canvas);
        canvas.restore();

        //center
//        layout = new StaticLayout(mContext.getString(R.string.activity_rectifying_operation_center), textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
//        canvas.save();
//        canvas.translate(mWidth*0.40f,mWidth*0.38f);
//        layout.draw(canvas);
//        canvas.restore();


//
//        layout = new StaticLayout(mContext.getString(R.string.activity_rectifying_operation_right), textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
//        canvas.save();
//        canvas.translate(mWidth / 5,-mWidth/6);//从mRadius，0开始画
//        layout.draw(canvas);
        canvas.restore();//别忘了restore
   }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        canvas.drawPath(mPathRight, mPaint);
        canvas.drawPath(mPathBottom, mPaint);
        canvas.drawPath(mPathLeft, mPaint);
        canvas.drawPath(mPathTop, mPaint);
        //canvas.drawPath(mPathCenter, mPaint);

        switch (mClickFlag) {
            case RIGHT:
                canvas.drawPath(mPathRight, mClickPaint);
                break;
            case BOTTOM:
                canvas.drawPath(mPathBottom, mClickPaint);
                break;
            case LEFT:
                canvas.drawPath(mPathLeft, mClickPaint);
                break;
            case TOP:
                canvas.drawPath(mPathTop, mClickPaint);
                break;
            case CENTER:
                canvas.drawPath(mPathCenter, mClickPaint);
                break;
        }

        //注意放置的位置，不然会出现canvas绘制的位置错乱。
        drawText(canvas);

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mAllRegion = new Region(-mWidth, -mWidth, mWidth, mWidth);

        mRectFBig = new RectF(-mWidth / 2, -mWidth / 2, mWidth / 2, mWidth / 2);

        //控制圆弧是否接近圆心，即中间空白部分的大小
        mRectFLittle = new RectF();

        mRadius = mWidth / 6;

        initPath();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 减去移除 的位置
        mCurX = (int) event.getX() - getMeasuredWidth() / 2;
        mCurY = (int) event.getY() - getMeasuredHeight() / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                containRect(mCurX, mCurY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mClickFlag != -1) {
                    containRect(mCurX, mCurY);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mClickFlag != -1) {
                    switch (mClickFlag) {
                        case RIGHT:
                            if (mListener != null) {
                                mListener.clickRight();
                            }
                            break;
                        case BOTTOM:
                            if (mListener != null) {
                                mListener.clickBottom();
                            }
                            break;
                        case LEFT:
                            if (mListener != null) {
                                mListener.clickLeft();
                            }
                            break;
                        case TOP:
                            if (mListener != null) {
                                mListener.clickTop();
                            }
                            break;
                    }

                    mClickFlag = -1;
                }

                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    public void containRect(int x, int y) {
        int index = -1;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).contains(x, y)) {
                mClickFlag = switchRect(i);
                index = i;
                break;
            }
        }

        if (index == -1) {
            mClickFlag = -1;
        }
    }

    public int switchRect(int i) {
        switch (i) {
            case 0:
                Log.i("aaa", "RIGHT ");
                return RIGHT;
            case 1:
                Log.i("aaa", "BOTTOM ");
                return BOTTOM;
            case 2:
                Log.i("aaa", "LEFT ");
                return LEFT;
            case 3:
                Log.i("aaa", "TOP");
                return TOP;
            default:
                return -1;
        }
    }

    public interface RegionViewClickListener {
        /**
         * 左边按钮被点击了
         */
        public void clickLeft();

        /**
         * 上边按钮被点击了
         */
        public void clickTop();

        /**
         * 右边按钮被点击了
         */
        public void clickRight();

        /**
         * 下边按钮被点击了
         */
        public void clickBottom();
    }

}