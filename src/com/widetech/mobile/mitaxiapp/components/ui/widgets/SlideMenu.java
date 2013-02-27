package com.widetech.mobile.mitaxiapp.components.ui.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created with IntelliJ IDEA.
 * User: AnderWeb
 * Date: 2/4/13
 * Time: 8:51 AM
 */
public class SlideMenu extends FrameLayout {

    private static final int ANIMATION_DURATION = 350;

    //View components
    private View menuView;
    private View contentView;

    private float menuSize;
    private boolean menuOpen = false;
    private GestureDetector openMenuDetector;

    public SlideMenu(Context context) {
        super(context);
        init(context);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        openMenuDetector = new GestureDetector(context,tapToCloseListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if(getChildCount()!=2) {
            throw new IllegalStateException
                    ("The SlideMenu MUST contain (and only contain) 2 children: The menu and the content!");
        }
        menuView = getChildAt(0);
        contentView = getChildAt(1);
        menuSize = ((LayoutParams)menuView.getLayoutParams()).width;
        menuView.setTranslationX(-menuSize/2);
        menuView.setVisibility(View.GONE);

        ViewPropertyAnimator contentAnimator = contentView.animate();
        contentAnimator.setDuration(ANIMATION_DURATION);
        contentAnimator.setInterpolator(new DecelerateInterpolator(2f));

        ViewPropertyAnimator menuAnimator = menuView.animate();
        menuAnimator.setDuration(ANIMATION_DURATION);
        menuAnimator.setInterpolator(new DecelerateInterpolator(2f));
    }

    public boolean isMenuOpen(){
        return menuOpen;
    }

    public void open() {
        contentView.setOnTouchListener(disableTouchesWhenOpenListener);
        contentView.setFocusable(false);
        menuView.setVisibility(View.VISIBLE);
        menuOpen = true;

        setupAnimation(contentView);
        setupAnimation(menuView);

        final ViewPropertyAnimator contentAnimator = contentView.animate();
        final ViewPropertyAnimator menuAnimator = menuView.animate();

        contentAnimator.translationX(menuSize);
        contentAnimator.setListener(animatorOpenListener);

        menuAnimator.translationX(0);
        menuAnimator.alpha(1f);
    }

    public void close() {
        contentView.setOnTouchListener(null);
        contentView.setFocusable(true);
        menuView.setVisibility(View.VISIBLE);
        menuOpen = false;

        setupAnimation(contentView);
        setupAnimation(menuView);

        final ViewPropertyAnimator contentAnimator = contentView.animate();
        final ViewPropertyAnimator menuAnimator = menuView.animate();

        contentAnimator.translationX(0);
        contentAnimator.setListener(animatorCloseListener);

        menuAnimator.translationX(-menuSize/2);
        menuAnimator.alpha(0f);
    }

    public void toggle() {
        if (menuOpen) {
            close();
        } else {
            open();
        }
    }

    private AnimatorListenerAdapter animatorOpenListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            manageLayers(menuView,false);
            manageLayers(contentView,false);
        }
    };

    private AnimatorListenerAdapter animatorCloseListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            manageLayers(menuView,false);
            manageLayers(contentView,false);
            menuView.setVisibility(View.GONE);
        }
    };

    private void setupAnimation(View view) {
        final ViewPropertyAnimator previousAnimator = view.animate();
        previousAnimator.setListener(null);
        previousAnimator.cancel();
        manageLayers(view,true);
    }


    private void manageLayers(View view, boolean hw){
        int layerType = hw ? LAYER_TYPE_HARDWARE:LAYER_TYPE_NONE;
        if(view.getLayerType() != layerType){
            view.setLayerType(layerType,null);
            if (hw) view.buildLayer();
        }
    }


    /**
     * Touch listener to intercept everything while the menu is open
     */
    private final OnTouchListener disableTouchesWhenOpenListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            openMenuDetector.onTouchEvent(motionEvent);
            return true;
        }
    };

    /**
     * Touch detector to handle tapping the content view to close it
     */
    private GestureDetector.SimpleOnGestureListener tapToCloseListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            playSoundEffect(SoundEffectConstants.CLICK);
            close();
            return true;
        }
    };

    //Manual movement

    protected float getCurrentPosition() {
        return contentView.getTranslationX();
    }

    protected float getOpenPosition() {
        return (menuSize);
    }

    protected void setCurrentPosition(float offset) {
        setupAnimation(contentView);
        setupAnimation(menuView);
        menuView.setVisibility(View.VISIBLE);
        final float max = getOpenPosition();
        final float current = Math.max(0,Math.min(max,getCurrentPosition()-offset));
        final float factor = current/max;

        contentView.setTranslationX(current);

        menuView.setTranslationX((1-factor)*(-menuSize/2));
        menuView.setAlpha(factor);
    }

}
