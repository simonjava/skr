package com.common.flutter.boost;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class TextPlatformViewFactory extends PlatformViewFactory {

    public TextPlatformViewFactory(MessageCodec<Object> createArgsCodec) {
        super(createArgsCodec);
    }

    /**
     * 新的组件叫做PlatformView,它允许开发者在flutter里面嵌入Android原生的view。
     * @param context
     * @param i
     * @param o
     * @return
     */
    @Override
    public PlatformView create(Context context, int i, Object o) {
        return new TextPlatformView(context);
    }

    private static class TextPlatformView implements PlatformView {

        private TextView platformTv;

        public TextPlatformView(Context context) {
            platformTv = new TextView(context);
            platformTv.setText("PlatformView Demo");
        }

        @Override
        public View getView() {
            return platformTv;
        }

        @Override
        public void dispose() {

        }
    }
}
