//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imkit.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import io.rong.imkit.R;

public class PromptPopupDialog extends AlertDialog {
  private Context mContext;
  private io.rong.imkit.utilities.PromptPopupDialog.OnPromptButtonClickedListener mPromptButtonClickedListener;
  private String mTitle;
  private String mPositiveButton;
  private String mMessage;
  private int mLayoutResId;

  public static io.rong.imkit.utilities.PromptPopupDialog newInstance(Context context, String title, String message) {
    return new io.rong.imkit.utilities.PromptPopupDialog(context, title, message);
  }

  public static io.rong.imkit.utilities.PromptPopupDialog newInstance(Context context, String message) {
    return new io.rong.imkit.utilities.PromptPopupDialog(context, message);
  }

  public static io.rong.imkit.utilities.PromptPopupDialog newInstance(Context context, String title, String message, String positiveButton) {
    return new io.rong.imkit.utilities.PromptPopupDialog(context, title, message, positiveButton);
  }

  public PromptPopupDialog(Context context, String title, String message, String positiveButton) {
    this(context, title, message);
    this.mPositiveButton = positiveButton;
  }

  public PromptPopupDialog(Context context, String title, String message) {
    super(context);
    this.mLayoutResId = R.layout.rc_dialog_popup_prompt;
    this.mContext = context;
    this.mTitle = title;
    this.mMessage = message;
  }

  public PromptPopupDialog(Context context, String message) {
    super(context);
    this.mContext = context;
    this.mMessage = message;
  }

  protected void onStart() {
    super.onStart();
    LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(this.mLayoutResId, (ViewGroup)null);
    TextView txtViewTitle = (TextView)view.findViewById(R.id.popup_dialog_title);
    TextView txtViewMessage = (TextView)view.findViewById(R.id.popup_dialog_message);
    TextView txtViewOK = (TextView)view.findViewById(R.id.popup_dialog_button_ok);
    TextView txtViewCancel = (TextView)view.findViewById(R.id.popup_dialog_button_cancel);
    txtViewOK.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if (io.rong.imkit.utilities.PromptPopupDialog.this.mPromptButtonClickedListener != null) {
          io.rong.imkit.utilities.PromptPopupDialog.this.mPromptButtonClickedListener.onPositiveButtonClicked();
        }

        io.rong.imkit.utilities.PromptPopupDialog.this.dismiss();
      }
    });
    txtViewCancel.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        io.rong.imkit.utilities.PromptPopupDialog.this.dismiss();
      }
    });
    if (!TextUtils.isEmpty(this.mTitle)) {
      txtViewTitle.setText(this.mTitle);
      txtViewTitle.setVisibility(View.VISIBLE);
    }

    if (!TextUtils.isEmpty(this.mPositiveButton)) {
      txtViewOK.setText(this.mPositiveButton);
    }

    txtViewMessage.setText(this.mMessage);
    this.setContentView(view);
    LayoutParams layoutParams = this.getWindow().getAttributes();
    layoutParams.width = this.gePopupWidth();
    layoutParams.height = -2;
    this.getWindow().setAttributes(layoutParams);
  }

  public io.rong.imkit.utilities.PromptPopupDialog setPromptButtonClickedListener(io.rong.imkit.utilities.PromptPopupDialog.OnPromptButtonClickedListener buttonClickedListener) {
    this.mPromptButtonClickedListener = buttonClickedListener;
    return this;
  }

  public io.rong.imkit.utilities.PromptPopupDialog setLayoutRes(int resId) {
    this.mLayoutResId = resId;
    return this;
  }

  private int gePopupWidth() {
    int distanceToBorder = (int)this.mContext.getResources().getDimension(R.dimen.rc_popup_dialog_distance_to_edge);
    return this.getScreenWidth() - 2 * distanceToBorder;
  }

  private int getScreenWidth() {
    return ((WindowManager)((WindowManager)this.mContext.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getWidth();
  }

  public interface OnPromptButtonClickedListener {
    void onPositiveButtonClicked();
  }
}
