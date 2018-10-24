// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveMall.proto
package com.wali.live.proto.LiveMall;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 命令字：zhibo.shopping.tap_ad_push
 */
public final class TapAdPushReq extends Message<TapAdPushReq, TapAdPushReq.Builder> {
  public static final ProtoAdapter<TapAdPushReq> ADAPTER = new ProtoAdapter_TapAdPushReq();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_UID = 0L;

  public static final Long DEFAULT_HOST_ID = 0L;

  public static final String DEFAULT_ROOM_ID = "";

  public static final Long DEFAULT_SKU = 0L;

  public static final Integer DEFAULT_SHOP_TYPE = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT64",
      label = WireField.Label.REQUIRED
  )
  public final Long uid;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#INT64",
      label = WireField.Label.REQUIRED
  )
  public final Long host_id;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING",
      label = WireField.Label.REQUIRED
  )
  public final String room_id;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  public final Long sku;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer shop_type;

  public TapAdPushReq(Long uid, Long host_id, String room_id, Long sku, Integer shop_type) {
    this(uid, host_id, room_id, sku, shop_type, ByteString.EMPTY);
  }

  public TapAdPushReq(Long uid, Long host_id, String room_id, Long sku, Integer shop_type,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uid = uid;
    this.host_id = host_id;
    this.room_id = room_id;
    this.sku = sku;
    this.shop_type = shop_type;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uid = uid;
    builder.host_id = host_id;
    builder.room_id = room_id;
    builder.sku = sku;
    builder.shop_type = shop_type;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TapAdPushReq)) return false;
    TapAdPushReq o = (TapAdPushReq) other;
    return unknownFields().equals(o.unknownFields())
        && uid.equals(o.uid)
        && host_id.equals(o.host_id)
        && room_id.equals(o.room_id)
        && Internal.equals(sku, o.sku)
        && Internal.equals(shop_type, o.shop_type);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + uid.hashCode();
      result = result * 37 + host_id.hashCode();
      result = result * 37 + room_id.hashCode();
      result = result * 37 + (sku != null ? sku.hashCode() : 0);
      result = result * 37 + (shop_type != null ? shop_type.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", uid=").append(uid);
    builder.append(", host_id=").append(host_id);
    builder.append(", room_id=").append(room_id);
    if (sku != null) builder.append(", sku=").append(sku);
    if (shop_type != null) builder.append(", shop_type=").append(shop_type);
    return builder.replace(0, 2, "TapAdPushReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return TapAdPushReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<TapAdPushReq, Builder> {
    public Long uid;

    public Long host_id;

    public String room_id;

    public Long sku;

    public Integer shop_type;

    public Builder() {
    }

    public Builder setUid(Long uid) {
      this.uid = uid;
      return this;
    }

    public Builder setHostId(Long host_id) {
      this.host_id = host_id;
      return this;
    }

    public Builder setRoomId(String room_id) {
      this.room_id = room_id;
      return this;
    }

    public Builder setSku(Long sku) {
      this.sku = sku;
      return this;
    }

    public Builder setShopType(Integer shop_type) {
      this.shop_type = shop_type;
      return this;
    }

    @Override
    public TapAdPushReq build() {
      if (uid == null
          || host_id == null
          || room_id == null) {
        throw Internal.missingRequiredFields(uid, "uid",
            host_id, "host_id",
            room_id, "room_id");
      }
      return new TapAdPushReq(uid, host_id, room_id, sku, shop_type, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_TapAdPushReq extends ProtoAdapter<TapAdPushReq> {
    public ProtoAdapter_TapAdPushReq() {
      super(FieldEncoding.LENGTH_DELIMITED, TapAdPushReq.class);
    }

    @Override
    public int encodedSize(TapAdPushReq value) {
      return ProtoAdapter.INT64.encodedSizeWithTag(1, value.uid)
          + ProtoAdapter.INT64.encodedSizeWithTag(2, value.host_id)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.room_id)
          + ProtoAdapter.INT64.encodedSizeWithTag(4, value.sku)
          + ProtoAdapter.INT32.encodedSizeWithTag(5, value.shop_type)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, TapAdPushReq value) throws IOException {
      ProtoAdapter.INT64.encodeWithTag(writer, 1, value.uid);
      ProtoAdapter.INT64.encodeWithTag(writer, 2, value.host_id);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.room_id);
      ProtoAdapter.INT64.encodeWithTag(writer, 4, value.sku);
      ProtoAdapter.INT32.encodeWithTag(writer, 5, value.shop_type);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public TapAdPushReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUid(ProtoAdapter.INT64.decode(reader)); break;
          case 2: builder.setHostId(ProtoAdapter.INT64.decode(reader)); break;
          case 3: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setSku(ProtoAdapter.INT64.decode(reader)); break;
          case 5: builder.setShopType(ProtoAdapter.INT32.decode(reader)); break;
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
    public TapAdPushReq redact(TapAdPushReq value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
