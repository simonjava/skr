// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveCommon.proto
package com.wali.live.proto.LiveCommon;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class ClickItem extends Message<ClickItem, ClickItem.Builder> {
  public static final ProtoAdapter<ClickItem> ADAPTER = new ProtoAdapter_ClickItem();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_CLICKTYPE = 0;

  public static final String DEFAULT_CLICKIMAGEURL = "";

  public static final Integer DEFAULT_CLICKINTERVAL = 0;

  public static final Integer DEFAULT_GIFTID = 0;

  public static final String DEFAULT_CLICKWAITINGIMAGEURL = "";

  public static final String DEFAULT_WARNINGTEXT = "";

  public static final String DEFAULT_PUSHSENDSUCCTEXT = "";

  public static final Integer DEFAULT_PKINTERVAL = 0;

  /**
   * 点击类型: 0:增加分数, 1:选中礼物
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer clickType;

  /**
   * 点击按钮背景图
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String clickImageUrl;

  /**
   * 点击间隔,单位:秒
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer clickInterval;

  /**
   * 礼物id,点击类型为选中礼物时使用
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer giftId;

  /**
   * 点击按钮等待背景图
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String clickWaitingImageUrl;

  /**
   * 未到时间点击显示的提示文本
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String warningText;

  /**
   * 成功发出后显示的提示文本
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String pushSendSuccText;

  /**
   * pk间隔,单位:秒, 默认30s
   */
  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer pkInterval;

  public ClickItem(Integer clickType, String clickImageUrl, Integer clickInterval, Integer giftId,
      String clickWaitingImageUrl, String warningText, String pushSendSuccText,
      Integer pkInterval) {
    this(clickType, clickImageUrl, clickInterval, giftId, clickWaitingImageUrl, warningText, pushSendSuccText, pkInterval, ByteString.EMPTY);
  }

  public ClickItem(Integer clickType, String clickImageUrl, Integer clickInterval, Integer giftId,
      String clickWaitingImageUrl, String warningText, String pushSendSuccText, Integer pkInterval,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.clickType = clickType;
    this.clickImageUrl = clickImageUrl;
    this.clickInterval = clickInterval;
    this.giftId = giftId;
    this.clickWaitingImageUrl = clickWaitingImageUrl;
    this.warningText = warningText;
    this.pushSendSuccText = pushSendSuccText;
    this.pkInterval = pkInterval;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.clickType = clickType;
    builder.clickImageUrl = clickImageUrl;
    builder.clickInterval = clickInterval;
    builder.giftId = giftId;
    builder.clickWaitingImageUrl = clickWaitingImageUrl;
    builder.warningText = warningText;
    builder.pushSendSuccText = pushSendSuccText;
    builder.pkInterval = pkInterval;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ClickItem)) return false;
    ClickItem o = (ClickItem) other;
    return unknownFields().equals(o.unknownFields())
        && clickType.equals(o.clickType)
        && Internal.equals(clickImageUrl, o.clickImageUrl)
        && Internal.equals(clickInterval, o.clickInterval)
        && Internal.equals(giftId, o.giftId)
        && Internal.equals(clickWaitingImageUrl, o.clickWaitingImageUrl)
        && Internal.equals(warningText, o.warningText)
        && Internal.equals(pushSendSuccText, o.pushSendSuccText)
        && Internal.equals(pkInterval, o.pkInterval);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + clickType.hashCode();
      result = result * 37 + (clickImageUrl != null ? clickImageUrl.hashCode() : 0);
      result = result * 37 + (clickInterval != null ? clickInterval.hashCode() : 0);
      result = result * 37 + (giftId != null ? giftId.hashCode() : 0);
      result = result * 37 + (clickWaitingImageUrl != null ? clickWaitingImageUrl.hashCode() : 0);
      result = result * 37 + (warningText != null ? warningText.hashCode() : 0);
      result = result * 37 + (pushSendSuccText != null ? pushSendSuccText.hashCode() : 0);
      result = result * 37 + (pkInterval != null ? pkInterval.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", clickType=").append(clickType);
    if (clickImageUrl != null) builder.append(", clickImageUrl=").append(clickImageUrl);
    if (clickInterval != null) builder.append(", clickInterval=").append(clickInterval);
    if (giftId != null) builder.append(", giftId=").append(giftId);
    if (clickWaitingImageUrl != null) builder.append(", clickWaitingImageUrl=").append(clickWaitingImageUrl);
    if (warningText != null) builder.append(", warningText=").append(warningText);
    if (pushSendSuccText != null) builder.append(", pushSendSuccText=").append(pushSendSuccText);
    if (pkInterval != null) builder.append(", pkInterval=").append(pkInterval);
    return builder.replace(0, 2, "ClickItem{").append('}').toString();
  }

  public byte[] toByteArray() {
    return ClickItem.ADAPTER.encode(this);
  }

  public static final ClickItem parseFrom(byte[] data) throws IOException {
    ClickItem c = null;
       c = ClickItem.ADAPTER.decode(data);
    return c;
  }

  /**
   * 点击类型: 0:增加分数, 1:选中礼物
   */
  public Integer getClickType() {
    if(clickType==null){
        return DEFAULT_CLICKTYPE;
    }
    return clickType;
  }

  /**
   * 点击按钮背景图
   */
  public String getClickImageUrl() {
    if(clickImageUrl==null){
        return DEFAULT_CLICKIMAGEURL;
    }
    return clickImageUrl;
  }

  /**
   * 点击间隔,单位:秒
   */
  public Integer getClickInterval() {
    if(clickInterval==null){
        return DEFAULT_CLICKINTERVAL;
    }
    return clickInterval;
  }

  /**
   * 礼物id,点击类型为选中礼物时使用
   */
  public Integer getGiftId() {
    if(giftId==null){
        return DEFAULT_GIFTID;
    }
    return giftId;
  }

  /**
   * 点击按钮等待背景图
   */
  public String getClickWaitingImageUrl() {
    if(clickWaitingImageUrl==null){
        return DEFAULT_CLICKWAITINGIMAGEURL;
    }
    return clickWaitingImageUrl;
  }

  /**
   * 未到时间点击显示的提示文本
   */
  public String getWarningText() {
    if(warningText==null){
        return DEFAULT_WARNINGTEXT;
    }
    return warningText;
  }

  /**
   * 成功发出后显示的提示文本
   */
  public String getPushSendSuccText() {
    if(pushSendSuccText==null){
        return DEFAULT_PUSHSENDSUCCTEXT;
    }
    return pushSendSuccText;
  }

  /**
   * pk间隔,单位:秒, 默认30s
   */
  public Integer getPkInterval() {
    if(pkInterval==null){
        return DEFAULT_PKINTERVAL;
    }
    return pkInterval;
  }

  /**
   * 点击类型: 0:增加分数, 1:选中礼物
   */
  public boolean hasClickType() {
    return clickType!=null;
  }

  /**
   * 点击按钮背景图
   */
  public boolean hasClickImageUrl() {
    return clickImageUrl!=null;
  }

  /**
   * 点击间隔,单位:秒
   */
  public boolean hasClickInterval() {
    return clickInterval!=null;
  }

  /**
   * 礼物id,点击类型为选中礼物时使用
   */
  public boolean hasGiftId() {
    return giftId!=null;
  }

  /**
   * 点击按钮等待背景图
   */
  public boolean hasClickWaitingImageUrl() {
    return clickWaitingImageUrl!=null;
  }

  /**
   * 未到时间点击显示的提示文本
   */
  public boolean hasWarningText() {
    return warningText!=null;
  }

  /**
   * 成功发出后显示的提示文本
   */
  public boolean hasPushSendSuccText() {
    return pushSendSuccText!=null;
  }

  /**
   * pk间隔,单位:秒, 默认30s
   */
  public boolean hasPkInterval() {
    return pkInterval!=null;
  }

  public static final class Builder extends Message.Builder<ClickItem, Builder> {
    public Integer clickType;

    public String clickImageUrl;

    public Integer clickInterval;

    public Integer giftId;

    public String clickWaitingImageUrl;

    public String warningText;

    public String pushSendSuccText;

    public Integer pkInterval;

    public Builder() {
    }

    /**
     * 点击类型: 0:增加分数, 1:选中礼物
     */
    public Builder setClickType(Integer clickType) {
      this.clickType = clickType;
      return this;
    }

    /**
     * 点击按钮背景图
     */
    public Builder setClickImageUrl(String clickImageUrl) {
      this.clickImageUrl = clickImageUrl;
      return this;
    }

    /**
     * 点击间隔,单位:秒
     */
    public Builder setClickInterval(Integer clickInterval) {
      this.clickInterval = clickInterval;
      return this;
    }

    /**
     * 礼物id,点击类型为选中礼物时使用
     */
    public Builder setGiftId(Integer giftId) {
      this.giftId = giftId;
      return this;
    }

    /**
     * 点击按钮等待背景图
     */
    public Builder setClickWaitingImageUrl(String clickWaitingImageUrl) {
      this.clickWaitingImageUrl = clickWaitingImageUrl;
      return this;
    }

    /**
     * 未到时间点击显示的提示文本
     */
    public Builder setWarningText(String warningText) {
      this.warningText = warningText;
      return this;
    }

    /**
     * 成功发出后显示的提示文本
     */
    public Builder setPushSendSuccText(String pushSendSuccText) {
      this.pushSendSuccText = pushSendSuccText;
      return this;
    }

    /**
     * pk间隔,单位:秒, 默认30s
     */
    public Builder setPkInterval(Integer pkInterval) {
      this.pkInterval = pkInterval;
      return this;
    }

    @Override
    public ClickItem build() {
      return new ClickItem(clickType, clickImageUrl, clickInterval, giftId, clickWaitingImageUrl, warningText, pushSendSuccText, pkInterval, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_ClickItem extends ProtoAdapter<ClickItem> {
    public ProtoAdapter_ClickItem() {
      super(FieldEncoding.LENGTH_DELIMITED, ClickItem.class);
    }

    @Override
    public int encodedSize(ClickItem value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.clickType)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.clickImageUrl)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.clickInterval)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.giftId)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.clickWaitingImageUrl)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.warningText)
          + ProtoAdapter.STRING.encodedSizeWithTag(7, value.pushSendSuccText)
          + ProtoAdapter.UINT32.encodedSizeWithTag(8, value.pkInterval)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, ClickItem value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.clickType);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.clickImageUrl);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.clickInterval);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.giftId);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.clickWaitingImageUrl);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.warningText);
      ProtoAdapter.STRING.encodeWithTag(writer, 7, value.pushSendSuccText);
      ProtoAdapter.UINT32.encodeWithTag(writer, 8, value.pkInterval);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public ClickItem decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setClickType(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setClickImageUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setClickInterval(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setGiftId(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setClickWaitingImageUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setWarningText(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setPushSendSuccText(ProtoAdapter.STRING.decode(reader)); break;
          case 8: builder.setPkInterval(ProtoAdapter.UINT32.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public ClickItem redact(ClickItem value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
