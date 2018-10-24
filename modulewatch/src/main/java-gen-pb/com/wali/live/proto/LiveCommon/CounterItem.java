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
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class CounterItem extends Message<CounterItem, CounterItem.Builder> {
  public static final ProtoAdapter<CounterItem> ADAPTER = new ProtoAdapter_CounterItem();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_IMAGEURL = "";

  public static final String DEFAULT_COUNTERTEXT = "";

  public static final Boolean DEFAULT_ISBOLD = false;

  /**
   * 计数背景图片
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String imageUrl;

  /**
   * 计数文本,包含数字
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String counterText;

  /**
   * 计数组件文本颜色
   */
  @WireField(
      tag = 3,
      adapter = "com.wali.live.proto.LiveCommon.ColorConfig#ADAPTER"
  )
  public final ColorConfig textColor;

  /**
   * 计数组件描边颜色
   */
  @WireField(
      tag = 4,
      adapter = "com.wali.live.proto.LiveCommon.ColorConfig#ADAPTER"
  )
  public final ColorConfig textEdgeColor;

  /**
   * 计数组件文本是否加粗
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean isBold;

  public CounterItem(String imageUrl, String counterText, ColorConfig textColor,
      ColorConfig textEdgeColor, Boolean isBold) {
    this(imageUrl, counterText, textColor, textEdgeColor, isBold, ByteString.EMPTY);
  }

  public CounterItem(String imageUrl, String counterText, ColorConfig textColor,
      ColorConfig textEdgeColor, Boolean isBold, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.imageUrl = imageUrl;
    this.counterText = counterText;
    this.textColor = textColor;
    this.textEdgeColor = textEdgeColor;
    this.isBold = isBold;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.imageUrl = imageUrl;
    builder.counterText = counterText;
    builder.textColor = textColor;
    builder.textEdgeColor = textEdgeColor;
    builder.isBold = isBold;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CounterItem)) return false;
    CounterItem o = (CounterItem) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(imageUrl, o.imageUrl)
        && Internal.equals(counterText, o.counterText)
        && Internal.equals(textColor, o.textColor)
        && Internal.equals(textEdgeColor, o.textEdgeColor)
        && Internal.equals(isBold, o.isBold);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (imageUrl != null ? imageUrl.hashCode() : 0);
      result = result * 37 + (counterText != null ? counterText.hashCode() : 0);
      result = result * 37 + (textColor != null ? textColor.hashCode() : 0);
      result = result * 37 + (textEdgeColor != null ? textEdgeColor.hashCode() : 0);
      result = result * 37 + (isBold != null ? isBold.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (imageUrl != null) builder.append(", imageUrl=").append(imageUrl);
    if (counterText != null) builder.append(", counterText=").append(counterText);
    if (textColor != null) builder.append(", textColor=").append(textColor);
    if (textEdgeColor != null) builder.append(", textEdgeColor=").append(textEdgeColor);
    if (isBold != null) builder.append(", isBold=").append(isBold);
    return builder.replace(0, 2, "CounterItem{").append('}').toString();
  }

  public byte[] toByteArray() {
    return CounterItem.ADAPTER.encode(this);
  }

  public static final CounterItem parseFrom(byte[] data) throws IOException {
    CounterItem c = null;
       c = CounterItem.ADAPTER.decode(data);
    return c;
  }

  /**
   * 计数背景图片
   */
  public String getImageUrl() {
    if(imageUrl==null){
        return DEFAULT_IMAGEURL;
    }
    return imageUrl;
  }

  /**
   * 计数文本,包含数字
   */
  public String getCounterText() {
    if(counterText==null){
        return DEFAULT_COUNTERTEXT;
    }
    return counterText;
  }

  /**
   * 计数组件文本颜色
   */
  public ColorConfig getTextColor() {
    if(textColor==null){
        return new ColorConfig.Builder().build();
    }
    return textColor;
  }

  /**
   * 计数组件描边颜色
   */
  public ColorConfig getTextEdgeColor() {
    if(textEdgeColor==null){
        return new ColorConfig.Builder().build();
    }
    return textEdgeColor;
  }

  /**
   * 计数组件文本是否加粗
   */
  public Boolean getIsBold() {
    if(isBold==null){
        return DEFAULT_ISBOLD;
    }
    return isBold;
  }

  /**
   * 计数背景图片
   */
  public boolean hasImageUrl() {
    return imageUrl!=null;
  }

  /**
   * 计数文本,包含数字
   */
  public boolean hasCounterText() {
    return counterText!=null;
  }

  /**
   * 计数组件文本颜色
   */
  public boolean hasTextColor() {
    return textColor!=null;
  }

  /**
   * 计数组件描边颜色
   */
  public boolean hasTextEdgeColor() {
    return textEdgeColor!=null;
  }

  /**
   * 计数组件文本是否加粗
   */
  public boolean hasIsBold() {
    return isBold!=null;
  }

  public static final class Builder extends Message.Builder<CounterItem, Builder> {
    public String imageUrl;

    public String counterText;

    public ColorConfig textColor;

    public ColorConfig textEdgeColor;

    public Boolean isBold;

    public Builder() {
    }

    /**
     * 计数背景图片
     */
    public Builder setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    /**
     * 计数文本,包含数字
     */
    public Builder setCounterText(String counterText) {
      this.counterText = counterText;
      return this;
    }

    /**
     * 计数组件文本颜色
     */
    public Builder setTextColor(ColorConfig textColor) {
      this.textColor = textColor;
      return this;
    }

    /**
     * 计数组件描边颜色
     */
    public Builder setTextEdgeColor(ColorConfig textEdgeColor) {
      this.textEdgeColor = textEdgeColor;
      return this;
    }

    /**
     * 计数组件文本是否加粗
     */
    public Builder setIsBold(Boolean isBold) {
      this.isBold = isBold;
      return this;
    }

    @Override
    public CounterItem build() {
      return new CounterItem(imageUrl, counterText, textColor, textEdgeColor, isBold, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_CounterItem extends ProtoAdapter<CounterItem> {
    public ProtoAdapter_CounterItem() {
      super(FieldEncoding.LENGTH_DELIMITED, CounterItem.class);
    }

    @Override
    public int encodedSize(CounterItem value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.imageUrl)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.counterText)
          + ColorConfig.ADAPTER.encodedSizeWithTag(3, value.textColor)
          + ColorConfig.ADAPTER.encodedSizeWithTag(4, value.textEdgeColor)
          + ProtoAdapter.BOOL.encodedSizeWithTag(5, value.isBold)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, CounterItem value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.imageUrl);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.counterText);
      ColorConfig.ADAPTER.encodeWithTag(writer, 3, value.textColor);
      ColorConfig.ADAPTER.encodeWithTag(writer, 4, value.textEdgeColor);
      ProtoAdapter.BOOL.encodeWithTag(writer, 5, value.isBold);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public CounterItem decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setImageUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setCounterText(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setTextColor(ColorConfig.ADAPTER.decode(reader)); break;
          case 4: builder.setTextEdgeColor(ColorConfig.ADAPTER.decode(reader)); break;
          case 5: builder.setIsBold(ProtoAdapter.BOOL.decode(reader)); break;
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
    public CounterItem redact(CounterItem value) {
      Builder builder = value.newBuilder();
      if (builder.textColor != null) builder.textColor = ColorConfig.ADAPTER.redact(builder.textColor);
      if (builder.textEdgeColor != null) builder.textEdgeColor = ColorConfig.ADAPTER.redact(builder.textEdgeColor);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
