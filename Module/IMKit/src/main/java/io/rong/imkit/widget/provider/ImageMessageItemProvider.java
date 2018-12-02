//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imkit.widget.provider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.core.avatar.AvatarUtils;
import com.common.image.fresco.FrescoWorker;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.R;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider.MessageProvider;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.message.ImageMessage;

@ProviderTag(
        messageContent = ImageMessage.class,
        showProgress = false,
        showReadState = true
)
public class ImageMessageItemProvider extends MessageProvider<ImageMessage> {
    private static final String TAG = "ImageMessageItemProvider";

    private static final int IMAGE_DEFAULT_WIDTH = 200;
    private static final int IMAGE_DEFAULT_HEIGHT = 200;

    public ImageMessageItemProvider() {
    }

    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_image_message, (ViewGroup) null);
        io.rong.imkit.widget.provider.ImageMessageItemProvider.ViewHolder holder = new io.rong.imkit.widget.provider.ImageMessageItemProvider.ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.rc_msg);
        holder.img = (SimpleDraweeView) view.findViewById(R.id.rc_img);
        view.setTag(holder);
        return view;
    }

    public void onItemClick(View view, int position, ImageMessage content, UIMessage message) {
        if (content != null) {
            EventBus.getDefault().post(new Event.ShowImagePreviewFragment(content));
        }
    }

    public void bindView(View v, int position, ImageMessage content, UIMessage message) {
        io.rong.imkit.widget.provider.ImageMessageItemProvider.ViewHolder holder = (io.rong.imkit.widget.provider.ImageMessageItemProvider.ViewHolder) v.getTag();
        if (message.getMessageDirection() == MessageDirection.SEND) {
            v.setBackgroundResource(R.drawable.rc_ic_bubble_no_right);
        } else {
            v.setBackgroundResource(R.drawable.rc_ic_bubble_no_left);
        }

        FrescoWorker.preLoadImg(holder.img, IMAGE_DEFAULT_WIDTH, IMAGE_DEFAULT_HEIGHT, content.getThumUri().toString(), 0);
        int progress = message.getProgress();
        SentStatus status = message.getSentStatus();
        if (status.equals(SentStatus.SENDING) && progress < 100) {
            holder.message.setText(progress + "%");
            holder.message.setVisibility(View.VISIBLE);
        } else {
            holder.message.setVisibility(View.GONE);
        }

    }

    public Spannable getContentSummary(ImageMessage data) {
        return null;
    }

    public Spannable getContentSummary(Context context, ImageMessage data) {
        return new SpannableString(context.getString(R.string.rc_message_content_image));
    }

    private static class ViewHolder {
        SimpleDraweeView img;
        TextView message;

        private ViewHolder() {
        }
    }
}
