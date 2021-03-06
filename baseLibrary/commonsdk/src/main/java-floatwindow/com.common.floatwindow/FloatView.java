package com.common.floatwindow;

import android.view.View;

/**
 * Created by yhao on 17-11-14.
 * https://github.com/yhaolpz
 */

abstract class FloatView {


    abstract void init();

    abstract void dismiss();

    void updateXY(int x, int y) {
    }

    void updateX(int x) {
    }

    void updateY(int y) {
    }

    int getX() {
        return 0;
    }

    int getY() {
        return 0;
    }
}
