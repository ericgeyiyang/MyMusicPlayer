package com.example.geyiyang.eric_x_music.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;

/**
 * Created by geyiyang on 2017/10/26.
 */

public class CDview extends View {
    private static final String TAG = "CDview";
    private static final int MSG_RUN = 0x00000100;
    private static final int MSG_ROTATE = 0x00000010;
    private static final int MSG_DECELERATE = 0x00000001;
    private static final int TIME_UPDATE = 20;
    private static final float ANGLEVELOSITY=0.0144f;
    private float mDegree = 0.0f;
    private float mIncreasedDegree = 0.0f;
    private float mDecelerationRate = 0.1f;
    private Timer timer;
    private float mAngleVelosity;
    private Matrix mMatrix;
    private boolean isInterrupted=true;
    private MyTread mMyTread;
    private volatile boolean isRunning;
    private volatile boolean isPlaying=false;

    private Bitmap mCoverBitmap;
    private long mCount1,mCount2;
    public CDview(Context context) {
        super(context);
        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
    }

    public CDview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
    }

    public CDview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
    }

    public CDview(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCoverBitmap == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = 0;
        int height = 0;
        // mode of width
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // mode of height
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // size of width
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // size of height
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            // child compute the size of itself
            width = mCoverBitmap.getWidth();
            // parent assign the max size, child should <= widthSize
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mCoverBitmap.getHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCoverBitmap == null) return;
        canvas.save();
        mMatrix.setRotate(mDegree, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.drawBitmap(mCoverBitmap, mMatrix, null);
        canvas.restore();
    }

    public void setImage(Bitmap bitmap) {
        int widthSize = bitmap.getWidth();
        int heightSize = bitmap.getHeight();
        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize,
                MeasureSpec.AT_MOST);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize,
                MeasureSpec.AT_MOST);

        measure(widthSpec, heightSpec);

        mCoverBitmap = createCircleBitmap(bitmap);
        requestLayout();
        invalidate();
    }

    public void setRotation(float degree) {
        mIncreasedDegree = degree;
        mHandler.sendEmptyMessage(MSG_ROTATE);
    }
    private Bitmap createCircleBitmap(Bitmap src) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setARGB(255, 241, 239, 229);

        Bitmap target = Bitmap.createBitmap(getMeasuredWidth(),
                getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2,
                getMeasuredWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, 0, 0, paint);

        return target;
    }

//    private TimerTask task;
//    private void initialtimer() {
//        task=new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.sendEmptyMessage(MSG_DECELERATE);
//                mCount1++;
//                Log.i(TAG, "run: --->"+mCount1);
//            }
//        };
//        timer = new Timer();
//    }

    public class MyTread extends Thread {
        @Override
        public void run() {
            while (true) {
                mHandler.sendEmptyMessage(MSG_DECELERATE);
                mCount1++;
//                Log.i(TAG, "run: --->"+mCount1);
                SystemClock.sleep(TIME_UPDATE);
                if (this.interrupted()) {
                    Log.i(TAG, "run: --->interrupted");
                    isInterrupted = true;
                    return;
                }
            }
        }
    }
    /**
     * @param v 最后5点ontouch的平均角加速度
     */


    public void decelerate(float v,boolean isPlaying) {
//        initialtimer();
        mAngleVelosity=v;
        if (mMyTread == null) {
            mMyTread = new MyTread();
        }
        if (isInterrupted) {
            mMyTread.setPriority(Thread.MAX_PRIORITY);
            mMyTread.start();
            isInterrupted = false;
        }
//        timer.schedule(task,0,TIME_UPDATE);
        mCount1=mCount2=0;
        this.isPlaying=isPlaying;
   }

    /**
     * 开始旋转
     */
    public void start() {
        if (isRunning)
            return;
        isRunning = true;
        mHandler.sendEmptyMessageDelayed(MSG_RUN, TIME_UPDATE);
    }

    /**
     * 暂停旋转
     */
    public void pause() {
        if (!isRunning)
            return;
        isRunning = false;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RUN) {
                if (isRunning) {
                    mDegree = (ANGLEVELOSITY*TIME_UPDATE+mDegree)%360.0f;//25s转360度，0.0144度/ms
                    invalidate();
                    sendEmptyMessageDelayed(MSG_RUN, TIME_UPDATE);
                }
            }
            if (msg.what == MSG_ROTATE) {
                mDegree = (mDegree + mIncreasedDegree) % 360.0f;
                invalidate();
                mDecelerationRate=0.06f;
            }
            if (msg.what == MSG_DECELERATE) {
                Log.i(TAG, "handleMessage: --->"+mIncreasedDegree+","+mAngleVelosity+","+mDecelerationRate);
                mCount2++;
//                Log.i(TAG, "handleMessage: --->"+mCount2);
                if (mAngleVelosity >= 0) {
                    mIncreasedDegree = mAngleVelosity * TIME_UPDATE;
                    mAngleVelosity-=mDecelerationRate;
                    if (mAngleVelosity < 0.6f) {
                        if(mDecelerationRate<=0.003f)
                            mDecelerationRate = 0.003f;
                        else
                            mDecelerationRate /= 1.14;
                        if(mAngleVelosity<=0.04f)
                            mDecelerationRate = 0.001f;

                    }
                    if (!isPlaying&&mAngleVelosity < 0.001f) {
                        pause();
//                        timer.cancel();
                        mMyTread.interrupt();
                        mAngleVelosity=0;
                        mDecelerationRate=0.1f;
                    }
                    if (isPlaying&&mAngleVelosity < ANGLEVELOSITY) {
//                        timer.cancel();
                        mMyTread.interrupt();
                        mAngleVelosity = ANGLEVELOSITY;
                        mDecelerationRate=0.1f;
                            start();
                    }
                } else {
                    mIncreasedDegree = mAngleVelosity * TIME_UPDATE;
                    mAngleVelosity += mDecelerationRate;
                    if (mAngleVelosity > -0.8f) {
                        if(mDecelerationRate<=0.003f)
                            mDecelerationRate=0.003f;
                        else
                            mDecelerationRate /= 1.14;
                    }
                    if (!isPlaying&&mAngleVelosity > -0.001f) {
                        pause();
//                        timer.cancel();
                        mMyTread.interrupt();
                        mAngleVelosity=0;
                        mDecelerationRate=0.1f;
                    }
                    if (isPlaying&&mAngleVelosity > ANGLEVELOSITY) {
//                        timer.cancel();
                        mMyTread.interrupt();
                        mAngleVelosity = ANGLEVELOSITY;
                        mDecelerationRate=0.1f;
                        start();
                    }
                }
                mDegree = (mDegree + mIncreasedDegree) % 360.0f;
                invalidate();
            }
        }
    };
}
