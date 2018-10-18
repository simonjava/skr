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
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * http://wiki.n.miui.com/pages/viewpage.action?pageId=25049393
 */
public final class ChannelItem extends Message<ChannelItem, ChannelItem.Builder> {
  public static final ProtoAdapter<ChannelItem> ADAPTER = new ProtoAdapter_ChannelItem();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_UITYPE = 0;

  public static final ByteString DEFAULT_UIDATA = ByteString.EMPTY;

  public static final Boolean DEFAULT_FULLCOLUMN = false;

  public static final Integer DEFAULT_SECTIONID = 0;

  public static final Boolean DEFAULT_ISHIDE = false;

  /**
   * 根据不同的UIType去解析UiData
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer uiType;

  /**
   * UiTemplateOneTextOneImg对应1,2,4,UiTemplateUserInfo对应3,5
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#BYTES",
      label = WireField.Label.REQUIRED
  )
  public final ByteString uiData;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean fullColumn;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer sectionId;

  /**
   * 是否隐藏这个模板，默认false：显示（目前只针对关注分栏的一行三列样式21/26有效）
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean isHide;

  public ChannelItem(Integer uiType, ByteString uiData, Boolean fullColumn, Integer sectionId,
      Boolean isHide) {
    this(uiType, uiData, fullColumn, sectionId, isHide, ByteString.EMPTY);
  }

  public ChannelItem(Integer uiType, ByteString uiData, Boolean fullColumn, Integer sectionId,
      Boolean isHide, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uiType = uiType;
    this.uiData = uiData;
    this.fullColumn = fullColumn;
    this.sectionId = sectionId;
    this.isHide = isHide;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uiType = uiType;
    builder.uiData = uiData;
    builder.fullColumn = fullColumn;
    builder.sectionId = sectionId;
    builder.isHide = isHide;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChannelItem)) return false;
    ChannelItem o = (ChannelItem) other;
    return unknownFields().equals(o.unknownFields())
        && uiType.equals(o.uiType)
        && uiData.equals(o.uiData)
        && Internal.equals(fullColumn, o.fullColumn)
        && Internal.equals(sectionId, o.sectionId)
        && Internal.equals(isHide, o.isHide);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + uiType.hashCode();
      result = result * 37 + uiData.hashCode();
      result = result * 37 + (fullColumn != null ? fullColumn.hashCode() : 0);
      result = result * 37 + (sectionId != null ? sectionId.hashCode() : 0);
      result = result * 37 + (isHide != null ? isHide.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", uiType=").append(uiType);
    builder.append(", uiData=").append(uiData);
    if (fullColumn != null) builder.append(", fullColumn=").append(fullColumn);
    if (sectionId != null) builder.append(", sectionId=").append(sectionId);
    if (isHide != null) builder.append(", isHide=").append(isHide);
    return builder.replace(0, 2, "ChannelItem{").append('}').toString();
  }

  public byte[] toByteArray() {
    return ChannelItem.ADAPTER.encode(this);
  }

  public static final ChannelItem parseFrom(byte[] data) throws IOException {
    ChannelItem c = null;
       c = ChannelItem.ADAPTER.decode(data);
    return c;
  }

  /**
   * 根据不同的UIType去解析UiData
   */
  public Integer getUiType() {
    if(uiType==null){
        return DEFAULT_UITYPE;
    }
    return uiType;
  }

  /**
   * UiTemplateOneTextOneImg对应1,2,4,UiTemplateUserInfo对应3,5
   */
  public ByteString getUiData() {
    if(uiData==null){
        return ByteString.of(new byte[0]);
    }
    return uiData;
  }

  public Boolean getFullColumn() {
    if(fullColumn==null){
        return DEFAULT_FULLCOLUMN;
    }
    return fullColumn;
  }

  public Integer getSectionId() {
    if(sectionId==null){
        return DEFAULT_SECTIONID;
    }
    return sectionId;
  }

  /**
   * 是否隐藏这个模板，默认false：显示（目前只针对关注分栏的一行三列样式21/26有效）
   */
  public Boolean getIsHide() {
    if(isHide==null){
        return DEFAULT_ISHIDE;
    }
    return isHide;
  }

  /**
   * 根据不同的UIType去解析UiData
   */
  public boolean hasUiType() {
    return uiType!=null;
  }

  /**
   * UiTemplateOneTextOneImg对应1,2,4,UiTemplateUserInfo对应3,5
   */
  public boolean hasUiData() {
    return uiData!=null;
  }

  public boolean hasFullColumn() {
    return fullColumn!=null;
  }

  public boolean hasSectionId() {
    return sectionId!=null;
  }

  /**
   * 是否隐藏这个模板，默认false：显示（目前只针对关注分栏的一行三列样式21/26有效）
   */
  public boolean hasIsHide() {
    return isHide!=null;
  }

  public static final class Builder extends Message.Builder<ChannelItem, Builder> {
    public Integer uiType;

    public ByteString uiData;

    public Boolean fullColumn;

    public Integer sectionId;

    public Boolean isHide;

    public Builder() {
    }

    /**
     * 根据不同的UIType去解析UiData
     */
    public Builder setUiType(Integer uiType) {
      this.uiType = uiType;
      return this;
    }

    /**
     * UiTemplateOneTextOneImg对应1,2,4,UiTemplateUserInfo对应3,5
     */
    public Builder setUiData(ByteString uiData) {
      this.uiData = uiData;
      return this;
    }

    public Builder setFullColumn(Boolean fullColumn) {
      this.fullColumn = fullColumn;
      return this;
    }

    public Builder setSectionId(Integer sectionId) {
      this.sectionId = sectionId;
      return this;
    }

    /**
     * 是否隐藏这个模板，默认false：显示（目前只针对关注分栏的一行三列样式21/26有效）
     */
    public Builder setIsHide(Boolean isHide) {
      this.isHide = isHide;
      return this;
    }

    @Override
    public ChannelItem build() {
      return new ChannelItem(uiType, uiData, fullColumn, sectionId, isHide, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_ChannelItem extends ProtoAdapter<ChannelItem> {
    public ProtoAdapter_ChannelItem() {
      super(FieldEncoding.LENGTH_DELIMITED, ChannelItem.class);
    }

    @Override
    public int encodedSize(ChannelItem value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.uiType)
          + ProtoAdapter.BYTES.encodedSizeWithTag(2, value.uiData)
          + ProtoAdapter.BOOL.encodedSizeWithTag(3, value.fullColumn)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.sectionId)
          + ProtoAdapter.BOOL.encodedSizeWithTag(5, value.isHide)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, ChannelItem value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.uiType);
      ProtoAdapter.BYTES.encodeWithTag(writer, 2, value.uiData);
      ProtoAdapter.BOOL.encodeWithTag(writer, 3, value.fullColumn);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.sectionId);
      ProtoAdapter.BOOL.encodeWithTag(writer, 5, value.isHide);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public ChannelItem decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUiType(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setUiData(ProtoAdapter.BYTES.decode(reader)); break;
          case 3: builder.setFullColumn(ProtoAdapter.BOOL.decode(reader)); break;
          case 4: builder.setSectionId(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setIsHide(ProtoAdapter.BOOL.decode(reader)); break;
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
    public ChannelItem redact(ChannelItem value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
