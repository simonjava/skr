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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 命令字：zhibo.shopping.gen_user_order
 * 观众购买成功淘宝商品发送给服务器
 */
public final class GenUserOrderReq extends Message<GenUserOrderReq, GenUserOrderReq.Builder> {
  public static final ProtoAdapter<GenUserOrderReq> ADAPTER = new ProtoAdapter_GenUserOrderReq();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_ROOM_ID = "";

  @WireField(
      tag = 1,
      adapter = "com.wali.live.proto.LiveMall.OrderInfo#ADAPTER",
      label = WireField.Label.REQUIRED
  )
  public final OrderInfo order_info;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String room_id;

  public GenUserOrderReq(OrderInfo order_info, String room_id) {
    this(order_info, room_id, ByteString.EMPTY);
  }

  public GenUserOrderReq(OrderInfo order_info, String room_id, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.order_info = order_info;
    this.room_id = room_id;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.order_info = order_info;
    builder.room_id = room_id;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GenUserOrderReq)) return false;
    GenUserOrderReq o = (GenUserOrderReq) other;
    return unknownFields().equals(o.unknownFields())
        && order_info.equals(o.order_info)
        && Internal.equals(room_id, o.room_id);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + order_info.hashCode();
      result = result * 37 + (room_id != null ? room_id.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", order_info=").append(order_info);
    if (room_id != null) builder.append(", room_id=").append(room_id);
    return builder.replace(0, 2, "GenUserOrderReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GenUserOrderReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<GenUserOrderReq, Builder> {
    public OrderInfo order_info;

    public String room_id;

    public Builder() {
    }

    public Builder setOrderInfo(OrderInfo order_info) {
      this.order_info = order_info;
      return this;
    }

    public Builder setRoomId(String room_id) {
      this.room_id = room_id;
      return this;
    }

    @Override
    public GenUserOrderReq build() {
      if (order_info == null) {
        throw Internal.missingRequiredFields(order_info, "order_info");
      }
      return new GenUserOrderReq(order_info, room_id, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GenUserOrderReq extends ProtoAdapter<GenUserOrderReq> {
    public ProtoAdapter_GenUserOrderReq() {
      super(FieldEncoding.LENGTH_DELIMITED, GenUserOrderReq.class);
    }

    @Override
    public int encodedSize(GenUserOrderReq value) {
      return OrderInfo.ADAPTER.encodedSizeWithTag(1, value.order_info)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.room_id)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GenUserOrderReq value) throws IOException {
      OrderInfo.ADAPTER.encodeWithTag(writer, 1, value.order_info);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.room_id);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GenUserOrderReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setOrderInfo(OrderInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
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
    public GenUserOrderReq redact(GenUserOrderReq value) {
      Builder builder = value.newBuilder();
      builder.order_info = OrderInfo.ADAPTER.redact(builder.order_info);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
