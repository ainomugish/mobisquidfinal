package com.mobisquid.mobicash.wedget.drawer.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class BouncingSlidingDotView extends DotView {
    private static final String TAG = BouncingSlidingDotView.class.getSimpleName();
    private Paint paint = new Paint();
    private int centerX;
    private int centerY;
    private float radius;
    private AnimatorSet animatorSet;
    private int startLeft;
    private int targetLeft;
    private int targetTop;
    private Rect clipRect = new Rect();
    private int indexToParent = Integer.MIN_VALUE;
    private long ratioAnimationTotalDuration = 100L;
    private long growDisappearAnimationDuration = 100L;
    private float radiusScale = 0F;
    private float compressRatio = 0.20F;
    private int startTop;

    public BouncingSlidingDotView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(dotColor);
        canvas.save();
        canvas.getClipBounds(clipRect);
        clipRect.inset(-targetLeft, 0);
        canvas.translate(-targetLeft, 0);
        canvas.drawCircle(centerX, targetTop + centerY, radius * radiusScale, paint);
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        indexToParent = findIndexToParent((ViewGroup) getParent());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ViewGroup parent = (ViewGroup) getParent();

        startLeft = left;
        startTop = top;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = (Math.min(getWidth(), getHeight()) / 2);

        ratioAnimationTotalDuration = (long) ((animationTotalDuration / 2) * (findIndexToParent(parent) + 1F) / parent.getChildCount());
    }

    @Override
    public void setColor(@ColorInt int color) {
        super.setColor(color);
    }

    @Override
    public void setAnimationDuration(long duration) {
        super.setAnimationDuration(duration);
        growDisappearAnimationDuration = animationTotalDuration / 2;
    }

    @Override
    protected void init() {

    }

    @Override
    public void startDotAnimation() {
        stopDotAnimation();

        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            AnimatorSet moveAnimatorSet = getMoveAnimator(ratioAnimationTotalDuration);
            AnimatorSet growDisappearAnimatorSet = getGrowDisappearAnimator(growDisappearAnimationDuration);
            animatorSet.playSequentially(moveAnimatorSet, growDisappearAnimatorSet);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    radiusScale = compressRatio;
                    dotColor = dotSecondColor;
                }
            });
        }
        animatorSet.start();
    }

    @Override
    public void stopDotAnimation() {
        if (animatorSet != null) {
            animatorSet.isStarted();
            animatorSet.cancel();
        }
    }

    @Override
    public boolean isAnimating() {
        if (animatorSet != null && animatorSet.isStarted()) {
            return true;
        }

        return false;
    }

    @Override
    protected void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio) {
        this.compressRatio = compressRatio;
    }

    @NonNull
    private AnimatorSet getMoveAnimator(long ratioAnimationTotalDuration) {
        AnimatorSet moveAnimatorSet = new AnimatorSet();

        ValueAnimator slidingAnimator = ValueAnimator.ofInt(startLeft, 0);
        slidingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                targetLeft = (int) animator.getAnimatedValue();
                invalidate();
            }
        });
        slidingAnimator.setInterpolator(new LinearInterpolator());
        slidingAnimator.setDuration(ratioAnimationTotalDuration);

        AnimatorSet bounceSet = getBounceAnimatorSet(ratioAnimationTotalDuration);

        moveAnimatorSet.playTogether(slidingAnimator, bounceSet);
        return moveAnimatorSet;
    }

    @NonNull
    private AnimatorSet getGrowDisappearAnimator(long growDisappearAnimationDuration) {

        AnimatorSet growDisappearAnimatorSet = new AnimatorSet();

        ValueAnimator growAnimator = ValueAnimator.ofFloat(compressRatio, 1F);
        growAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                radiusScale = (float) animator.getAnimatedValue();
                invalidate();
            }
        });
        growAnimator.setInterpolator(new AccelerateInterpolator());
        growAnimator.setDuration(growDisappearAnimationDuration);

        ValueAnimator fadeAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), dotSecondColor, Color.TRANSPARENT);
        fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                dotColor = (int) animator.getAnimatedValue();
                invalidate();
            }
        });


        fadeAnimator.setDuration(growDisappearAnimationDuration);
        fadeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        growDisappearAnimatorSet.playTogether(growAnimator, fadeAnimator);
        return growDisappearAnimatorSet;
    }

    @NonNull
    private AnimatorSet getBounceAnimatorSet(long bounceDuration) {
        AnimatorSet bounceSet = new AnimatorSet();
        List<Animator> bounceAnimatorList = new ArrayList<>();

        ValueAnimator initialDownAnimator = ValueAnimator.ofInt(-startTop, 0);
        initialDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                targetTop = (int) animator.getAnimatedValue();
            }
        });
        initialDownAnimator.setInterpolator(new AccelerateInterpolator());

        bounceAnimatorList.add(initialDownAnimator);

        for (int i = 1; i <= indexToParent; i++) {

            ValueAnimator upAnimator = ValueAnimator.ofInt(0, -startTop);
            upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    targetTop = (int) animator.getAnimatedValue();
                }
            });
            upAnimator.setInterpolator(new DecelerateInterpolator());

            bounceAnimatorList.add(upAnimator);
            ValueAnimator downAnimator = ValueAnimator.ofInt(-startTop, 0);
            downAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    targetTop = (int) animator.getAnimatedValue();
                }
            });
            downAnimator.setInterpolator(new AccelerateInterpolator());

            bounceAnimatorList.add(downAnimator);
        }

        int size = bounceAnimatorList.size();
        for (Animator animator : bounceAnimatorList) {
            animator.setDuration(bounceDuration / size);
        }
        bounceSet.playSequentially(bounceAnimatorList);
        return bounceSet;
    }

    private int findIndexToParent(@NonNull ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) == this) {
                return i;
            }
        }
        return -1;
    }
}
