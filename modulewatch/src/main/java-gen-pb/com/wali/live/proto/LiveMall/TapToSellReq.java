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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 命令字： zhibo.shopping.tap_to_sell
 */
public final class TapToSellReq extends Message<TapToSellReq, TapToSellReq.Builder> {
  public static final ProtoAdapter<TapToSellReq> ADAPTER = new ProtoAdapter_TapToSellReq();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_ROOM_ID = "";

  public static final Long DEFAULT_UID = 0L;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING",
      label = WireField.Label.REQUIRED
  )
  public final String room_id;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#INT64",
      label = WireField.Label.REQUIRED
  )
  public final Long uid;

  public TapToSellReq(String room_id, Long uid) {
    this(room_id, uid, ByteString.EMPTY);
  }

  public TapToSellReq(String room_id, Long uid, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.room_id = room_id;
    this.uid = uid;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.room_id = room_id;
    builder.uid = uid;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TapToSellReq)) return false;
    TapToSellReq o = (TapToSellReq) other;
    return unknownFields().equals(o.unknownFields())
        && room_id.equals(o.room_id)
        && uid.equals(o.uid);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + room_id.hashCode();
      result = result * 37 + uid.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", room_id=").append(room_id);
    builder.append(", uid=").append(uid);
    return builder.replace(0, 2, "TapToSellReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return TapToSellReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<TapToSellReq, Builder> {
    public String room_id;

    public Long uid;

    public Builder() {
    }

    public Builder setRoomId(String room_id) {
      this.room_id = room_id;
      return this;
    }

    public Builder setUid(Long uid) {
      this.uid = uid;
      return this;
    }

    @Override
    public TapToSellReq build() {
      if (room_id == null
          || uid == null) {
        throw Internal.missingRequiredFields(room_id, "room_id",
            uid, "uid");
      }
      return new TapToSellReq(room_id, uid, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_TapToSellReq extends ProtoAdapter<TapToSellReq> {
    public ProtoAdapter_TapToSellReq() {
      super(FieldEncoding.LENGTH_DELIMITED, TapToSellReq.class);
    }

    @Override
    public int encodedSize(TapToSellReq value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.room_id)
          + ProtoAdapter.INT64.encodedSizeWithTag(2, value.uid)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, TapToSellReq value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.room_id);
      ProtoAdapter.INT64.encodeWithTag(writer, 2, value.uid);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public TapToSellReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setUid(ProtoAdapter.INT64.decode(reader)); break;
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
    public TapToSellReq redact(TapToSellReq value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
