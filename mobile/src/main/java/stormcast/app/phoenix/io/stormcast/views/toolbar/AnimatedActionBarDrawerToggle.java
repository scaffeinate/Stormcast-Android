package stormcast.app.phoenix.io.stormcast.views.toolbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by sudharti on 10/1/17.
 */

public class AnimatedActionBarDrawerToggle extends ActionBarDrawerToggle {
    private static final float MENU_POSITION = 0f;
    private static final float ARROW_POSITION = 1.0f;
    private static final int ANIMATION_DURATION = 300;

    private final DrawerLayout mDrawerLayout;
    private final DecelerateInterpolator mInterpolator = new DecelerateInterpolator();
    private final ValueAnimator stateMenuAnum = ValueAnimator.ofFloat(ARROW_POSITION, MENU_POSITION);
    private final ValueAnimator arrowAnim = ValueAnimator.ofFloat(MENU_POSITION, ARROW_POSITION);
    private ValueAnimator anim;

    public AnimatedActionBarDrawerToggle(Activity activity, DrawerLayout mDrawerLayout,
                                         Toolbar toolbar, int openDrawerContentDescriptionResource, int closeDrawerContentDescriptionResource) {
        super(activity, mDrawerLayout, toolbar, openDrawerContentDescriptionResource, closeDrawerContentDescriptionResource);
        this.mDrawerLayout = mDrawerLayout;
    }

    public void animateToBackArrow() {
        animate(State.UP_ARROW);
    }

    public void animateToMenu() {
        animate(State.MENU);
    }

    private void animate(State state) {
        anim = (state == State.MENU) ? stateMenuAnum : arrowAnim;
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                onDrawerSlide(mDrawerLayout, slideOffset);
            }
        });

        anim.setInterpolator(mInterpolator);
        anim.setDuration(ANIMATION_DURATION);

        anim.start();
    }

    private enum State {UP_ARROW, MENU}
}
