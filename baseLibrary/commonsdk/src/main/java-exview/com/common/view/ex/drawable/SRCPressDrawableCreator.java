package com.common.view.ex.drawable;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import com.common.base.R;

public class SRCPressDrawableCreator implements ICreateDrawable {

    private GradientDrawable drawable;
    private TypedArray pressTa;
    private TypedArray typedArray;

    SRCPressDrawableCreator(GradientDrawable drawable, TypedArray typedArray, TypedArray pressTa) {
        this.drawable = drawable;
        this.pressTa = pressTa;
        this.typedArray = typedArray;
    }

    @Override
    public Drawable create() throws Exception {
        StateListDrawable stateListDrawable = new StateListDrawable();
        for (int i = 0; i < pressTa.getIndexCount(); i++) {
            int attr = pressTa.getIndex(i);

            if (attr == R.styleable.src_press_src_pressed_color) {
                int color = pressTa.getColor(attr, 0);
                GradientDrawable pressDrawable = DrawableFactory.getDrawable(typedArray);
                pressDrawable.setColor(color);
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
            } else if (attr == R.styleable.src_press_src_unpressed_color) {
                int color = pressTa.getColor(attr, 0);
                drawable.setColor(color);
                stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, drawable);
            }
        }
        return stateListDrawable;
    }
}
