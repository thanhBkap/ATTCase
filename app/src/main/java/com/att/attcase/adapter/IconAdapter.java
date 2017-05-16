package com.att.attcase.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.att.attcase.R;

/**
 * Created by mac on 5/15/17.
 */

public class IconAdapter extends RelativeLayout {
    int baseh,basew,basex,basey;
    ImageButton btnXoaIcon,btnXoayIcon,btnPhongTo;
    Context context;
    int imageInt;
    boolean freeze = false;
    int i;
    ImageView imageIcon,imageVien;
    RelativeLayout layoutBg,layGroup;
    RelativeLayout.LayoutParams layoutParams;
    public LayoutInflater mInflater;
    int margl,margt;
    Bitmap orginalBitmap;
    int pivx,pivy;

    public int getImageInt() {
        return imageInt;
    }

    public void setImageInt(int imageInt) {
        imageIcon.setImageResource(imageInt);
        this.imageInt = imageInt;
    }

    float startDegree;

    public IconAdapter(Context paramcontext,int imgInt) {
        super(paramcontext);
        context = paramcontext;
        imageInt = imgInt;
        layGroup = this;
        basex = 0;
        basey = 0;
        pivx = 0;
        pivy = 0;
        mInflater = ((LayoutInflater) paramcontext.getSystemService("layout_inflater"));
        mInflater.inflate(R.layout.icon_layout,this,true);
        btnXoaIcon  = ((ImageButton) findViewById(R.id.xoa_icon));
        btnXoayIcon = ((ImageButton) findViewById(R.id.xoay_icon));
        btnPhongTo  = ((ImageButton) findViewById(R.id.phongto_icon));
        imageVien   = ((ImageView)   findViewById(R.id.icon_border));
        layoutParams = new RelativeLayout.LayoutParams(250,250);
        layGroup.setLayoutParams(layoutParams);
        imageIcon   = ((ImageView)   findViewById(R.id.icon));
        imageIcon.setImageResource(imageInt);
        imageIcon.setTag(Integer.valueOf(0));
        setOnTouchListener(new OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(IconAdapter.this.context,
                    new GestureDetector.SimpleOnGestureListener() {
                        public boolean onDoubleTap(MotionEvent paramAnonymous2MotionEvent) {
                            return false;
                        }
                    });
            @Override
            public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                IconAdapter.this.visiball();
                if (!IconAdapter.this.freeze) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            layGroup.invalidate();
                            gestureDetector.onTouchEvent(event);
                            layGroup.bringToFront();
                            layGroup.performClick();

                            basex   =   ((int) (event.getRawX() - layoutParams.leftMargin));
                            basey   =   ((int) (event.getRawY() - layoutParams.topMargin));
                            break;

                        case MotionEvent.ACTION_MOVE:
                            int i = (int) event.getRawX();
                            int j = (int) event.getRawY();
                            layoutBg = (RelativeLayout) (getParent());
                            if ((i - basex > -(layGroup.getWidth()*2 / 3))
                                    && (i - basex < layoutBg.getWidth() - layGroup.getWidth()/3)) {
                                layoutParams.leftMargin = (i - basex);
                            }

                            if ((j - basey > -(layGroup.getHeight() * 2 / 3))
                                    && (j - basey < layoutBg.getHeight() - layGroup.getHeight() / 3)) {
                                layoutParams.topMargin = (j - basey);
                            }

                            layoutParams.rightMargin = -9999999;
                            layoutParams.bottomMargin = -9999999;
                            layGroup.setLayoutParams(layoutParams);
                            break;
                    }
                    return true;
                }
                return true;
            }
        });

        this.btnPhongTo.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!IconAdapter.this.freeze) {
                    int j = (int) event.getRawX();
                    int i = (int) event.getRawY();
                    layoutParams = (LayoutParams) layGroup.getLayoutParams();
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            IconAdapter.this.layGroup.invalidate();
                            IconAdapter.this.basex = j;
                            IconAdapter.this.basey = i;
                            IconAdapter.this.basew = IconAdapter.this.layGroup.getWidth();
                            IconAdapter.this.baseh = IconAdapter.this.layGroup.getHeight();
                            int[] loaction = new int[2];
                            layGroup.getLocationOnScreen(loaction);
                            margl = layoutParams.leftMargin;
                            margt = layoutParams.topMargin;
                            break;
                        case MotionEvent.ACTION_MOVE:

                            float f2 = (float) Math.toDegrees(Math.atan2(i - IconAdapter.this.basey, j - IconAdapter.this.basex));
                            float f1 = f2;
                            if (f2 < 0.0F) {
                                f1 = f2 + 360.0F;
                            }
                            j -= IconAdapter.this.basex;
                            int k = i - IconAdapter.this.basey;
                            i = (int) (Math.sqrt(j * j + k * k) * Math.cos(Math.toRadians(f1
                                    - IconAdapter.this.layGroup.getRotation())));
                            j = (int) (Math.sqrt(i * i + k * k) * Math.sin(Math.toRadians(f1
                                    - IconAdapter.this.layGroup.getRotation())));
                            k = i * 2 + IconAdapter.this.basew;
                            int m = j * 2 + IconAdapter.this.baseh;
                            if (k > 150) {
                                layoutParams.width = k;
                                layoutParams.leftMargin = (IconAdapter.this.margl - i);
                            }
                            if (m > 150) {
                                layoutParams.height = m;
                                layoutParams.topMargin = (IconAdapter.this.margt - j);
                            }
                            IconAdapter.this.layGroup.setLayoutParams(layoutParams);
                            IconAdapter.this.layGroup.performLongClick();
                            break;
                    }
                    return true;

                }
                return IconAdapter.this.freeze;
            }
        });

        this.btnXoayIcon.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!IconAdapter.this.freeze) {
                    layoutParams = (RelativeLayout.LayoutParams) IconAdapter.this.layGroup.getLayoutParams();
                    IconAdapter.this.layoutBg = ((RelativeLayout) IconAdapter.this.getParent());
                    int[] arrayOfInt = new int[2];
                    layoutBg.getLocationOnScreen(arrayOfInt);
                    int i = (int) event.getRawX() - arrayOfInt[0];
                    int j = (int) event.getRawY() - arrayOfInt[1];
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            IconAdapter.this.layGroup.invalidate();
                            IconAdapter.this.startDegree = layGroup.getRotation();
                            IconAdapter.this.pivx = (layoutParams.leftMargin + IconAdapter.this.getWidth() / 2);
                            IconAdapter.this.pivy = (layoutParams.topMargin + IconAdapter.this.getHeight() / 2);
                            IconAdapter.this.basex = (i - IconAdapter.this.pivx);
                            IconAdapter.this.basey = (IconAdapter.this.pivy - j);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            int k = IconAdapter.this.pivx;
                            int m = IconAdapter.this.pivy;
                            j = (int) (Math.toDegrees(Math.atan2(IconAdapter.this.basey, IconAdapter.this.basex)) - Math
                                    .toDegrees(Math.atan2(m - j, i - k)));
                            i = j;
                            if (j < 0) {
                                i = j + 360;
                            }
                            IconAdapter.this.layGroup.setRotation((IconAdapter.this.startDegree + i) % 360.0F);
                            break;
                    }

                    return true;
                }
                return IconAdapter.this.freeze;
            }
        });
        this.btnXoaIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (!IconAdapter.this.freeze) {
                    layoutBg = ((RelativeLayout) IconAdapter.this.getParent());
                    layoutBg.performClick();
                    layoutBg.removeView(IconAdapter.this.layGroup);
                }
            }
        });
    }

    public void disableAll() {
        btnXoaIcon.setVisibility(View.INVISIBLE);
        btnXoayIcon.setVisibility(View.INVISIBLE);
        btnPhongTo.setVisibility(View.INVISIBLE);
        imageVien.setVisibility(View.INVISIBLE);
    }

    private void visiball() {
        btnXoaIcon.setVisibility(View.VISIBLE);
        btnXoayIcon.setVisibility(View.VISIBLE);
        btnPhongTo.setVisibility(View.VISIBLE);
        imageVien.setVisibility(View.VISIBLE);
    }

}