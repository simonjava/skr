// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CommonChannel.proto
package com.wali.live.proto.CommonChannel;

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
import java.util.List;
import okio.ByteString;

/**
 * uitype=29
 */
public final class UiTemplateRanking extends Message<UiTemplateRanking, UiTemplateRanking.Builder> {
  public static final ProtoAdapter<UiTemplateRanking> ADAPTER = new ProtoAdapter_UiTemplateRanking();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_JUMPSCHEMEURI = "";

  public static final String DEFAULT_TEXT1 = "";

  public static final String DEFAULT_TEXT2 = "";

  public static final Integer DEFAULT_ICONSTYLE = 0;

  @WireField(
      tag = 1,
      adapter = "com.wali.live.proto.CommonChannel.RankingItemData#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<RankingItemData> itemDatas;

  /**
   * uitemplate跳转schemaUri
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String jumpSchemeUri;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String text1;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String text2;

  /**
   * 目前用于控制uitype=29用户头像左上角是否显示小图标，默认0=无，1=排名
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer iconStyle;

  public UiTemplateRanking(List<RankingItemData> itemDatas, String jumpSchemeUri, String text1,
      String text2, Integer iconStyle) {
    this(itemDatas, jumpSchemeUri, text1, text2, iconStyle, ByteString.EMPTY);
  }

  public UiTemplateRanking(List<RankingItemData> itemDatas, String jumpSchemeUri, String text1,
      String text2, Integer iconStyle, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.itemDatas = Internal.immutableCopyOf("itemDatas", itemDatas);
    this.jumpSchemeUri = jumpSchemeUri;
    this.text1 = text1;
    this.text2 = text2;
    this.iconStyle = iconStyle;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.itemDatas = Internal.copyOf("itemDatas", itemDatas);
    builder.jumpSchemeUri = jumpSchemeUri;
    builder.text1 = text1;
    builder.text2 = text2;
    builder.iconStyle = iconStyle;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UiTemplateRanking)) return false;
    UiTemplateRanking o = (UiTemplateRanking) other;
    return unknownFields().equals(o.unknownFields())
        && itemDatas.equals(o.itemDatas)
        && Internal.equals(jumpSchemeUri, o.jumpSchemeUri)
        && Internal.equals(text1, o.text1)
        && Internal.equals(text2, o.text2)
        && Internal.equals(iconStyle, o.iconStyle);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + itemDatas.hashCode();
      result = result * 37 + (jumpSchemeUri != null ? jumpSchemeUri.hashCode() : 0);
      result = result * 37 + (text1 != null ? text1.hashCode() : 0);
      result = result * 37 + (text2 != null ? text2.hashCode() : 0);
      result = result * 37 + (iconStyle != null ? iconStyle.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (!itemDatas.isEmpty()) builder.append(", itemDatas=").append(itemDatas);
    if (jumpSchemeUri != null) builder.append(", jumpSchemeUri=").append(jumpSchemeUri);
    if (text1 != null) builder.append(", text1=").append(text1);
    if (text2 != null) builder.append(", text2=").append(text2);
    if (iconStyle != null) builder.append(", iconStyle=").append(iconStyle);
    return builder.replace(0, 2, "UiTemplateRanking{").append('}').toString();
  }

  public byte[] toByteArray() {
    return UiTemplateRanking.ADAPTER.encode(this);
  }

  public static final UiTemplateRanking parseFrom(byte[] data) throws IOException {
    UiTemplateRanking c = null;
       c = UiTemplateRanking.ADAPTER.decode(data);
    return c;
  }

  public List<RankingItemData> getItemDatasList() {
    if(itemDatas==null){
        return new java.util.ArrayList<RankingItemData>();
    }
    return itemDatas;
  }

  /**
   * uitemplate跳转schemaUri
   */
  public String getJumpSchemeUri() {
    if(jumpSchemeUri==null){
        return DEFAULT_JUMPSCHEMEURI;
    }
    return jumpSchemeUri;
  }

  public String getText1() {
    if(text1==null){
        return DEFAULT_TEXT1;
    }
    return text1;
  }

  public String getText2() {
    if(text2==null){
        return DEFAULT_TEXT2;
    }
    return text2;
  }

  /**
   * 目前用于控制uitype=29用户头像左上角是否显示小图标，默认0=无，1=排名
   */
  public Integer getIconStyle() {
    if(iconStyle==null){
        return DEFAULT_ICONSTYLE;
    }
    return iconStyle;
  }

  public boolean hasItemDatasList() {
    return itemDatas!=null;
  }

  /**
   * uitemplate跳转schemaUri
   */
  public boolean hasJumpSchemeUri() {
    return jumpSchemeUri!=null;
  }

  public boolean hasText1() {
    return text1!=null;
  }

  public boolean hasText2() {
    return text2!=null;
  }

  /**
   * 目前用于控制uitype=29用户头像左上角是否显示小图标，默认0=无，1=排名
   */
  public boolean hasIconStyle() {
    return iconStyle!=null;
  }

  public static final class Builder extends Message.Builder<UiTemplateRanking, Builder> {
    public List<RankingItemData> itemDatas;

    public String jumpSchemeUri;

    public String text1;

    public String text2;

    public Integer iconStyle;

    public Builder() {
      itemDatas = Internal.newMutableList();
    }

    public Builder addAllItemDatas(List<RankingItemData> itemDatas) {
      Internal.checkElementsNotNull(itemDatas);
      this.itemDatas = itemDatas;
      return this;
    }

    /**
     * uitemplate跳转schemaUri
     */
    public Builder setJumpSchemeUri(String jumpSchemeUri) {
      this.jumpSchemeUri = jumpSchemeUri;
      return this;
    }

    public Builder setText1(String text1) {
      this.text1 = text1;
      return this;
    }

    public Builder setText2(String text2) {
      this.text2 = text2;
      return this;
    }

    /**
     * 目前用于控制uitype=29用户头像左上角是否显示小图标，默认0=无，1=排名
     */
    public Builder setIconStyle(Integer iconStyle) {
      this.iconStyle = iconStyle;
      return this;
    }

    @Override
    public UiTemplateRanking build() {
      return new UiTemplateRanking(itemDatas, jumpSchemeUri, text1, text2, iconStyle, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_UiTemplateRanking extends ProtoAdapter<UiTemplateRanking> {
    public ProtoAdapter_UiTemplateRanking() {
      super(FieldEncoding.LENGTH_DELIMITED, UiTemplateRanking.class);
    }

    @Override
    public int encodedSize(UiTemplateRanking value) {
      return RankingItemData.ADAPTER.asRepeated().encodedSizeWithTag(1, value.itemDatas)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.jumpSchemeUri)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.text1)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.text2)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.iconStyle)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, UiTemplateRanking value) throws IOException {
      RankingItemData.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.itemDatas);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.jumpSchemeUri);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.text1);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.text2);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.iconStyle);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public UiTemplateRanking decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.itemDatas.add(RankingItemData.ADAPTER.decode(reader)); break;
          case 2: builder.setJumpSchemeUri(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setText1(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setText2(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.setIconStyle(ProtoAdapter.UINT32.decode(reader)); break;
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
    public UiTemplateRanking redact(UiTemplateRanking value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.itemDatas, RankingItemData.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
