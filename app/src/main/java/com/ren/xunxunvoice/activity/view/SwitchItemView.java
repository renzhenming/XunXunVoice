package com.ren.xunxunvoice.activity.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.ren.xunxunvoice.R;

/**
 * Created by Administrator on 2017/6/3.
 */
public class SwitchItemView extends View {

    private Paint paint;
    //空件宽高
    private int mHeight;
    private int mWidth;
    //获取到值
    private String value_item_text;
    private Bitmap value_item_bitmap;
    private float value_item_text_size;
    private int value_item_text_color;
    //文字坐标
    private int textX;
    private int textY;
    //图片矩形区域
    private Rect iconRect;
    private int mHeightWithoutPadding;
    //图片和文字之间的padding
    private float value_item_icon_padding_text;
    //选中时的颜色
    private int tabSelectedColor = Color.BLUE;
    private int tabUnSelectedColor = Color.BLACK;

    private Bitmap maskBitmap;
    //0.0f ~ 1.0f
    private float mAlpha = 0.0f;

    public void setTabmAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
        if (Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }

    public SwitchItemView(Context context) {
        this(context,null);
    }

    public SwitchItemView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public SwitchItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取数据
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchItemView);
        value_item_text = typedArray.getString(R.styleable.SwitchItemView_item_text);
        BitmapDrawable drawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.SwitchItemView_item_icon);
        value_item_bitmap = drawable.getBitmap();
        value_item_text_size = typedArray.getDimension(R.styleable.SwitchItemView_item_test_size, 20);
        value_item_text_color = typedArray.getColor(R.styleable.SwitchItemView_item_test_color, Color.parseColor("#2b2b2b"));
        value_item_icon_padding_text = typedArray.getDimension(R.styleable.SwitchItemView_item_icon_padding_text, 5);

        typedArray.recycle();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(tabUnSelectedColor);
        paint.setTextSize(value_item_text_size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取空间宽高
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        //获取控件中内容的实际高，不包括padding
        mHeightWithoutPadding = mHeight - getPaddingBottom() - getPaddingTop();
        //文字和图片都使用画笔画上，所以需要确定图片和文字的位置信息

        //获取文字宽高
        Rect textRect = new Rect();
        paint.getTextBounds(value_item_text,0,value_item_text.length(),textRect);
        int textWidth = textRect.width();
        int textHeight = textRect.height();

        //画图片方式canvas.drawBitmap(bitmap,srcRect,desRect,paint);
        //确定图片的rect,（这样图片就不会存在越界的情况）,因此rect需要得到上下左右的位置信息

        //获取图片宽高(约定图片是正放心，宽=高)
        int iconHeight = mHeightWithoutPadding-textHeight;
        int left = (mWidth - iconHeight)/2;
        int top = getPaddingTop();
        int right = left+iconHeight;
        int bottom = top + iconHeight;
        iconRect = new Rect(left,top,right,bottom);

        //计算文字的坐标
        textX = (mWidth - textWidth)/2;
        textY = iconRect.bottom+textHeight+(int) (value_item_icon_padding_text+0.5);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAlpha(255);
        //画图片（引用图片什么样，就画什么样）
        canvas.drawBitmap(value_item_bitmap,null,iconRect,paint);

        //画纯色图片
        //在内存中画与引用图片相同但是色调不同的图(alpha值为0~255)
        int alpha = (int) (mAlpha * 255);
        drawAlphaIcon(alpha);
        //将这个内存中画得的图片绘制(maskBitmap是和空间同样大小的，所以从0，0坐标开始画起)
        canvas.drawBitmap(maskBitmap,0,0,paint);

        //画文字
        paint.setColor(tabUnSelectedColor);
        paint.setAlpha(255-alpha);
        canvas.drawText(value_item_text,textX,textY,paint);

        //画纯色文字
        paint.setColor(tabSelectedColor);
        paint.setAlpha(alpha);
        canvas.drawText(value_item_text,textX,textY,paint);
    }

    /**
     * 绘制icon颜色渐变效果（仿微信）
     * 这样一来，在内存中就有了这样的一个bitmap，此时这个bitmap并不会显示出来
     * @param alpha
     */
    private void drawAlphaIcon(int alpha){
        //绘制一个和控件同样大小的bitmap
        maskBitmap = Bitmap.createBitmap(mWidth,mHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(maskBitmap);
        Paint paint = new Paint();
        //注意：设置颜色要在设置透明度之前
        paint.setColor(tabSelectedColor);
        paint.setAlpha(alpha);
        //在icon所在区域中画一个纯色的矩形区域
        canvas.drawRect(iconRect,paint);

        //原始icon和纯色区域取交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAlpha(255);
        //执行下边这句后maskBitmap对象就有了一个纯色的与图标相同的图
        canvas.drawBitmap(value_item_bitmap,null,iconRect,paint);
    }
}



















