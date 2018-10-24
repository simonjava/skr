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
import java.util.List;
import okio.ByteString;

public final class SalesOrderResp extends Message<SalesOrderResp, SalesOrderResp.Builder> {
  public static final ProtoAdapter<SalesOrderResp> ADAPTER = new ProtoAdapter_SalesOrderResp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ERR_CODE = 0;

  public static final String DEFAULT_ERR_MSG = "";

  public static final Long DEFAULT_TIMESTAMP = 0L;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer err_code;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String err_msg;

  @WireField(
      tag = 3,
      adapter = "com.wali.live.proto.LiveMall.OrderInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<OrderInfo> order_info;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  public final Long timestamp;

  public SalesOrderResp(Integer err_code, String err_msg, List<OrderInfo> order_info,
      Long timestamp) {
    this(err_code, err_msg, order_info, timestamp, ByteString.EMPTY);
  }

  public SalesOrderResp(Integer err_code, String err_msg, List<OrderInfo> order_info,
      Long timestamp, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.err_code = err_code;
    this.err_msg = err_msg;
    this.order_info = Internal.immutableCopyOf("order_info", order_info);
    this.timestamp = timestamp;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.err_code = err_code;
    builder.err_msg = err_msg;
    builder.order_info = Internal.copyOf("order_info", order_info);
    builder.timestamp = timestamp;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SalesOrderResp)) return false;
    SalesOrderResp o = (SalesOrderResp) other;
    return unknownFields().equals(o.unknownFields())
        && err_code.equals(o.err_code)
        && Internal.equals(err_msg, o.err_msg)
        && order_info.equals(o.order_info)
        && Internal.equals(timestamp, o.timestamp);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + err_code.hashCode();
      result = result * 37 + (err_msg != null ? err_msg.hashCode() : 0);
      result = result * 37 + order_info.hashCode();
      result = result * 37 + (timestamp != null ? timestamp.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", err_code=").append(err_code);
    if (err_msg != null) builder.append(", err_msg=").append(err_msg);
    if (!order_info.isEmpty()) builder.append(", order_info=").append(order_info);
    if (timestamp != null) builder.append(", timestamp=").append(timestamp);
    return builder.replace(0, 2, "SalesOrderResp{").append('}').toString();
  }

  public byte[] toByteArray() {
    return SalesOrderResp.ADAPTER.encode(this);
  }

  public static final SalesOrderResp parseFrom(byte[] data) throws IOException {
    SalesOrderResp c = null;
       c = SalesOrderResp.ADAPTER.decode(data);
    return c;
  }

  public Integer getErrCode() {
    if(err_code==null){
        return DEFAULT_ERR_CODE;
    }
    return err_code;
  }

  public String getErrMsg() {
    if(err_msg==null){
        return DEFAULT_ERR_MSG;
    }
    return err_msg;
  }

  public List<OrderInfo> getOrderInfoList() {
    if(order_info==null){
        return new java.util.ArrayList<OrderInfo>();
    }
    return order_info;
  }

  public Long getTimestamp() {
    if(timestamp==null){
        return DEFAULT_TIMESTAMP;
    }
    return timestamp;
  }

  public boolean hasErrCode() {
    return err_code!=null;
  }

  public boolean hasErrMsg() {
    return err_msg!=null;
  }

  public boolean hasOrderInfoList() {
    return order_info!=null;
  }

  public boolean hasTimestamp() {
    return timestamp!=null;
  }

  public static final class Builder extends Message.Builder<SalesOrderResp, Builder> {
    public Integer err_code;

    public String err_msg;

    public List<OrderInfo> order_info;

    public Long timestamp;

    public Builder() {
      order_info = Internal.newMutableList();
    }

    public Builder setErrCode(Integer err_code) {
      this.err_code = err_code;
      return this;
    }

    public Builder setErrMsg(String err_msg) {
      this.err_msg = err_msg;
      return this;
    }

    public Builder addAllOrderInfo(List<OrderInfo> order_info) {
      Internal.checkElementsNotNull(order_info);
      this.order_info = order_info;
      return this;
    }

    public Builder setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    @Override
    public SalesOrderResp build() {
      return new SalesOrderResp(err_code, err_msg, order_info, timestamp, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_SalesOrderResp extends ProtoAdapter<SalesOrderResp> {
    public ProtoAdapter_SalesOrderResp() {
      super(FieldEncoding.LENGTH_DELIMITED, SalesOrderResp.class);
    }

    @Override
    public int encodedSize(SalesOrderResp value) {
      return ProtoAdapter.INT32.encodedSizeWithTag(1, value.err_code)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.err_msg)
          + OrderInfo.ADAPTER.asRepeated().encodedSizeWithTag(3, value.order_info)
          + ProtoAdapter.INT64.encodedSizeWithTag(4, value.timestamp)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, SalesOrderResp value) throws IOException {
      ProtoAdapter.INT32.encodeWithTag(writer, 1, value.err_code);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.err_msg);
      OrderInfo.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.order_info);
      ProtoAdapter.INT64.encodeWithTag(writer, 4, value.timestamp);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public SalesOrderResp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setErrCode(ProtoAdapter.INT32.decode(reader)); break;
          case 2: builder.setErrMsg(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.order_info.add(OrderInfo.ADAPTER.decode(reader)); break;
          case 4: builder.setTimestamp(ProtoAdapter.INT64.decode(reader)); break;
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
    public SalesOrderResp redact(SalesOrderResp value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.order_info, OrderInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
