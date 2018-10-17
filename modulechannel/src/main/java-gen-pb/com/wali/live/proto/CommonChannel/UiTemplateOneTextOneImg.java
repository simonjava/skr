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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * type = 1,2,4,16
 */
public final class UiTemplateOneTextOneImg extends Message<UiTemplateOneTextOneImg, UiTemplateOneTextOneImg.Builder> {
  public static final ProtoAdapter<UiTemplateOneTextOneImg> ADAPTER = new ProtoAdapter_UiTemplateOneTextOneImg();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_HEADERNAME = "";

  public static final String DEFAULT_HEADERVIEWALLURI = "";

  /**
   * 这种模版的元素必须都是一张图，一段文字
   */
  @WireField(
      tag = 1,
      adapter = "com.wali.live.proto.CommonChannel.OneTextOneImgItemData#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<OneTextOneImgItemData> itemDatas;

  /**
   * header的名字
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String headerName;

  /**
   * 查看全部的uri，如果没有这个字段不展示查看全部
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String headerViewAllUri;

  public UiTemplateOneTextOneImg(List<OneTextOneImgItemData> itemDatas, String headerName,
      String headerViewAllUri) {
    this(itemDatas, headerName, headerViewAllUri, ByteString.EMPTY);
  }

  public UiTemplateOneTextOneImg(List<OneTextOneImgItemData> itemDatas, String headerName,
      String headerViewAllUri, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.itemDatas = Internal.immutableCopyOf("itemDatas", itemDatas);
    this.headerName = headerName;
    this.headerViewAllUri = headerViewAllUri;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.itemDatas = Internal.copyOf("itemDatas", itemDatas);
    builder.headerName = headerName;
    builder.headerViewAllUri = headerViewAllUri;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UiTemplateOneTextOneImg)) return false;
    UiTemplateOneTextOneImg o = (UiTemplateOneTextOneImg) other;
    return unknownFields().equals(o.unknownFields())
        && itemDatas.equals(o.itemDatas)
        && Internal.equals(headerName, o.headerName)
        && Internal.equals(headerViewAllUri, o.headerViewAllUri);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + itemDatas.hashCode();
      result = result * 37 + (headerName != null ? headerName.hashCode() : 0);
      result = result * 37 + (headerViewAllUri != null ? headerViewAllUri.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (!itemDatas.isEmpty()) builder.append(", itemDatas=").append(itemDatas);
    if (headerName != null) builder.append(", headerName=").append(headerName);
    if (headerViewAllUri != null) builder.append(", headerViewAllUri=").append(headerViewAllUri);
    return builder.replace(0, 2, "UiTemplateOneTextOneImg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return UiTemplateOneTextOneImg.ADAPTER.encode(this);
  }

  public static final UiTemplateOneTextOneImg parseFrom(byte[] data) throws IOException {
    UiTemplateOneTextOneImg c = null;
       c = UiTemplateOneTextOneImg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 这种模版的元素必须都是一张图，一段文字
   */
  public List<OneTextOneImgItemData> getItemDatasList() {
    if(itemDatas==null){
        return new java.util.ArrayList<OneTextOneImgItemData>();
    }
    return itemDatas;
  }

  /**
   * header的名字
   */
  public String getHeaderName() {
    if(headerName==null){
        return DEFAULT_HEADERNAME;
    }
    return headerName;
  }

  /**
   * 查看全部的uri，如果没有这个字段不展示查看全部
   */
  public String getHeaderViewAllUri() {
    if(headerViewAllUri==null){
        return DEFAULT_HEADERVIEWALLURI;
    }
    return headerViewAllUri;
  }

  /**
   * 这种模版的元素必须都是一张图，一段文字
   */
  public boolean hasItemDatasList() {
    return itemDatas!=null;
  }

  /**
   * header的名字
   */
  public boolean hasHeaderName() {
    return headerName!=null;
  }

  /**
   * 查看全部的uri，如果没有这个字段不展示查看全部
   */
  public boolean hasHeaderViewAllUri() {
    return headerViewAllUri!=null;
  }

  public static final class Builder extends Message.Builder<UiTemplateOneTextOneImg, Builder> {
    public List<OneTextOneImgItemData> itemDatas;

    public String headerName;

    public String headerViewAllUri;

    public Builder() {
      itemDatas = Internal.newMutableList();
    }

    /**
     * 这种模版的元素必须都是一张图，一段文字
     */
    public Builder addAllItemDatas(List<OneTextOneImgItemData> itemDatas) {
      Internal.checkElementsNotNull(itemDatas);
      this.itemDatas = itemDatas;
      return this;
    }

    /**
     * header的名字
     */
    public Builder setHeaderName(String headerName) {
      this.headerName = headerName;
      return this;
    }

    /**
     * 查看全部的uri，如果没有这个字段不展示查看全部
     */
    public Builder setHeaderViewAllUri(String headerViewAllUri) {
      this.headerViewAllUri = headerViewAllUri;
      return this;
    }

    @Override
    public UiTemplateOneTextOneImg build() {
      return new UiTemplateOneTextOneImg(itemDatas, headerName, headerViewAllUri, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_UiTemplateOneTextOneImg extends ProtoAdapter<UiTemplateOneTextOneImg> {
    public ProtoAdapter_UiTemplateOneTextOneImg() {
      super(FieldEncoding.LENGTH_DELIMITED, UiTemplateOneTextOneImg.class);
    }

    @Override
    public int encodedSize(UiTemplateOneTextOneImg value) {
      return OneTextOneImgItemData.ADAPTER.asRepeated().encodedSizeWithTag(1, value.itemDatas)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.headerName)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.headerViewAllUri)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, UiTemplateOneTextOneImg value) throws IOException {
      OneTextOneImgItemData.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.itemDatas);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.headerName);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.headerViewAllUri);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public UiTemplateOneTextOneImg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.itemDatas.add(OneTextOneImgItemData.ADAPTER.decode(reader)); break;
          case 2: builder.setHeaderName(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setHeaderViewAllUri(ProtoAdapter.STRING.decode(reader)); break;
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
    public UiTemplateOneTextOneImg redact(UiTemplateOneTextOneImg value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.itemDatas, OneTextOneImgItemData.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
