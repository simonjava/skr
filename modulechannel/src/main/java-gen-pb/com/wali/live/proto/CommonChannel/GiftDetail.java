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
import okio.ByteString;

/**
 * type=3 extData填充内容
 */
public final class GiftDetail extends Message<GiftDetail, GiftDetail.Builder> {
  public static final ProtoAdapter<GiftDetail> ADAPTER = new ProtoAdapter_GiftDetail();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_GIFTID = 0;

  public static final Integer DEFAULT_GIFTWORTH = 0;

  /**
   * 礼物id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer giftId;

  /**
   * 礼物价值
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer giftWorth;

  public GiftDetail(Integer giftId, Integer giftWorth) {
    this(giftId, giftWorth, ByteString.EMPTY);
  }

  public GiftDetail(Integer giftId, Integer giftWorth, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.giftId = giftId;
    this.giftWorth = giftWorth;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.giftId = giftId;
    builder.giftWorth = giftWorth;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GiftDetail)) return false;
    GiftDetail o = (GiftDetail) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(giftId, o.giftId)
        && Internal.equals(giftWorth, o.giftWorth);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (giftId != null ? giftId.hashCode() : 0);
      result = result * 37 + (giftWorth != null ? giftWorth.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (giftId != null) builder.append(", giftId=").append(giftId);
    if (giftWorth != null) builder.append(", giftWorth=").append(giftWorth);
    return builder.replace(0, 2, "GiftDetail{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GiftDetail.ADAPTER.encode(this);
  }

  public static final GiftDetail parseFrom(byte[] data) throws IOException {
    GiftDetail c = null;
       c = GiftDetail.ADAPTER.decode(data);
    return c;
  }

  /**
   * 礼物id
   */
  public Integer getGiftId() {
    if(giftId==null){
        return DEFAULT_GIFTID;
    }
    return giftId;
  }

  /**
   * 礼物价值
   */
  public Integer getGiftWorth() {
    if(giftWorth==null){
        return DEFAULT_GIFTWORTH;
    }
    return giftWorth;
  }

  /**
   * 礼物id
   */
  public boolean hasGiftId() {
    return giftId!=null;
  }

  /**
   * 礼物价值
   */
  public boolean hasGiftWorth() {
    return giftWorth!=null;
  }

  public static final class Builder extends Message.Builder<GiftDetail, Builder> {
    public Integer giftId;

    public Integer giftWorth;

    public Builder() {
    }

    /**
     * 礼物id
     */
    public Builder setGiftId(Integer giftId) {
      this.giftId = giftId;
      return this;
    }

    /**
     * 礼物价值
     */
    public Builder setGiftWorth(Integer giftWorth) {
      this.giftWorth = giftWorth;
      return this;
    }

    @Override
    public GiftDetail build() {
      return new GiftDetail(giftId, giftWorth, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GiftDetail extends ProtoAdapter<GiftDetail> {
    public ProtoAdapter_GiftDetail() {
      super(FieldEncoding.LENGTH_DELIMITED, GiftDetail.class);
    }

    @Override
    public int encodedSize(GiftDetail value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.giftId)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.giftWorth)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GiftDetail value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.giftId);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.giftWorth);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GiftDetail decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setGiftId(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setGiftWorth(ProtoAdapter.UINT32.decode(reader)); break;
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
    public GiftDetail redact(GiftDetail value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
