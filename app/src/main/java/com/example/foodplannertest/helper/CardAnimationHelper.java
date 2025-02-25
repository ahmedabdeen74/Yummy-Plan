package com.example.foodplannertest.helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.RecyclerView;

public class CardAnimationHelper {


    public static void applyEnterAnimation(RecyclerView.ViewHolder viewHolder, int position) {
        View itemView = viewHolder.itemView;


        itemView.setAlpha(0f);
        itemView.setTranslationY(50f);
        itemView.setRotationY(-10f);


        AnimatorSet animatorSet = new AnimatorSet();


        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 1f);
        alphaAnimator.setDuration(350);


        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(itemView, "translationY", 50f, 0f);
        translationAnimator.setDuration(400);
        translationAnimator.setInterpolator(new DecelerateInterpolator());


        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(itemView, "rotationY", -10f, 0f);
        rotationAnimator.setDuration(450);
        rotationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());


        long delayTime = position * 100;
        animatorSet.setStartDelay(delayTime);


        animatorSet.playTogether(alphaAnimator, translationAnimator, rotationAnimator);
        animatorSet.start();
    }

    public static void updateCardPosition(View view, float centerX, float viewCenterX) {

        float distanceFromCenter = viewCenterX - centerX;


        float rotationFactor = -distanceFromCenter / 40f;


        view.setRotationY(rotationFactor);
        view.setRotation(rotationFactor / 3);


        float scaleFactor = 1.0f - Math.min(Math.abs(distanceFromCenter / 1000f), 0.15f);
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);

        view.setAlpha(Math.max(0.7f, 1.0f - Math.abs(distanceFromCenter / 1500f)));


        float elevation = 20 - Math.abs(distanceFromCenter) / 100f;
        view.setElevation(elevation);

        float translationY = Math.abs(distanceFromCenter) / 30f;
        view.setTranslationY(translationY);


        view.setTranslationZ(-Math.abs(distanceFromCenter) / 30f);
    }
}
