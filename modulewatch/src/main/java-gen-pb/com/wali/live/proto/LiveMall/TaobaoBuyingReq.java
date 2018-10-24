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
import java.util.List;
import okio.ByteString;

/**
 * 命令字: zhibo.shopping.taobao_buying
 * 观众点击一个淘宝商品，废弃
 */
public final class TaobaoBuyingReq extends Message<TaobaoBuyingReq, TaobaoBuyingReq.Builder> {
  public static final ProtoAdapter<TaobaoBuyingReq> ADAPTER = new ProtoAdapter_TaobaoBuyingReq();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_UID = 0L;

  public static final Long DEFAULT_HOST_ID = 0L;

  public static final String DEFAULT_ROOM_ID = "";

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
      adapter = "com.wali.live.proto.LiveMall.GoodsInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<GoodsInfo> good_info;

  public TaobaoBuyingReq(Long uid, Long host_id, String room_id, List<GoodsInfo> good_info) {
    this(uid, host_id, room_id, good_info, ByteString.EMPTY);
  }

  public TaobaoBuyingReq(Long uid, Long host_id, String room_id, List<GoodsInfo> good_info,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uid = uid;
    this.host_id = host_id;
    this.room_id = room_id;
    this.good_info = Internal.immutableCopyOf("good_info", good_info);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uid = uid;
    builder.host_id = host_id;
    builder.room_id = room_id;
    builder.good_info = Internal.copyOf("good_info", good_info);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TaobaoBuyingReq)) return false;
    TaobaoBuyingReq o = (TaobaoBuyingReq) other;
    return unknownFields().equals(o.unknownFields())
        && uid.equals(o.uid)
        && host_id.equals(o.host_id)
        && room_id.equals(o.room_id)
        && good_info.equals(o.good_info);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + uid.hashCode();
      result = result * 37 + host_id.hashCode();
      result = result * 37 + room_id.hashCode();
      result = result * 37 + good_info.hashCode();
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
    if (!good_info.isEmpty()) builder.append(", good_info=").append(good_info);
    return builder.replace(0, 2, "TaobaoBuyingReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return TaobaoBuyingReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<TaobaoBuyingReq, Builder> {
    public Long uid;

    public Long host_id;

    public String room_id;

    public List<GoodsInfo> good_info;

    public Builder() {
      good_info = Internal.newMutableList();
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

    public Builder addAllGoodInfo(List<GoodsInfo> good_info) {
      Internal.checkElementsNotNull(good_info);
      this.good_info = good_info;
      return this;
    }

    @Override
    public TaobaoBuyingReq build() {
      if (uid == null
          || host_id == null
          || room_id == null) {
        throw Internal.missingRequiredFields(uid, "uid",
            host_id, "host_id",
            room_id, "room_id");
      }
      return new TaobaoBuyingReq(uid, host_id, room_id, good_info, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_TaobaoBuyingReq extends ProtoAdapter<TaobaoBuyingReq> {
    public ProtoAdapter_TaobaoBuyingReq() {
      super(FieldEncoding.LENGTH_DELIMITED, TaobaoBuyingReq.class);
    }

    @Override
    public int encodedSize(TaobaoBuyingReq value) {
      return ProtoAdapter.INT64.encodedSizeWithTag(1, value.uid)
          + ProtoAdapter.INT64.encodedSizeWithTag(2, value.host_id)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.room_id)
          + GoodsInfo.ADAPTER.asRepeated().encodedSizeWithTag(4, value.good_info)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, TaobaoBuyingReq value) throws IOException {
      ProtoAdapter.INT64.encodeWithTag(writer, 1, value.uid);
      ProtoAdapter.INT64.encodeWithTag(writer, 2, value.host_id);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.room_id);
      GoodsInfo.ADAPTER.asRepeated().encodeWithTag(writer, 4, value.good_info);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public TaobaoBuyingReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUid(ProtoAdapter.INT64.decode(reader)); break;
          case 2: builder.setHostId(ProtoAdapter.INT64.decode(reader)); break;
          case 3: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.good_info.add(GoodsInfo.ADAPTER.decode(reader)); break;
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
    public TaobaoBuyingReq redact(TaobaoBuyingReq value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.good_info, GoodsInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
