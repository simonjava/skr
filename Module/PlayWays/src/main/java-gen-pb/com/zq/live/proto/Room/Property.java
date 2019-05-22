// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.Room;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 用户金币、红钻资产, -1 表示没有变动
 */
public final class Property extends Message<Property, Property.Builder> {
  public static final ProtoAdapter<Property> ADAPTER = new ProtoAdapter_Property();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Float DEFAULT_COINBALANCE = 0.0f;

  public static final Float DEFAULT_HONGZUANBALANCE = 0.0f;

  public static final Long DEFAULT_LASTCHANGEMS = 0L;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  private final Float coinBalance;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  private final Float hongZuanBalance;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  private final Long lastChangeMs;

  public Property(Integer userID, Float coinBalance, Float hongZuanBalance, Long lastChangeMs) {
    this(userID, coinBalance, hongZuanBalance, lastChangeMs, ByteString.EMPTY);
  }

  public Property(Integer userID, Float coinBalance, Float hongZuanBalance, Long lastChangeMs,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.coinBalance = coinBalance;
    this.hongZuanBalance = hongZuanBalance;
    this.lastChangeMs = lastChangeMs;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.coinBalance = coinBalance;
    builder.hongZuanBalance = hongZuanBalance;
    builder.lastChangeMs = lastChangeMs;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Property)) return false;
    Property o = (Property) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(coinBalance, o.coinBalance)
        && Internal.equals(hongZuanBalance, o.hongZuanBalance)
        && Internal.equals(lastChangeMs, o.lastChangeMs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (coinBalance != null ? coinBalance.hashCode() : 0);
      result = result * 37 + (hongZuanBalance != null ? hongZuanBalance.hashCode() : 0);
      result = result * 37 + (lastChangeMs != null ? lastChangeMs.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (coinBalance != null) builder.append(", coinBalance=").append(coinBalance);
    if (hongZuanBalance != null) builder.append(", hongZuanBalance=").append(hongZuanBalance);
    if (lastChangeMs != null) builder.append(", lastChangeMs=").append(lastChangeMs);
    return builder.replace(0, 2, "Property{").append('}').toString();
  }

  public byte[] toByteArray() {
    return Property.ADAPTER.encode(this);
  }

  public static final Property parseFrom(byte[] data) throws IOException {
    Property c = null;
       c = Property.ADAPTER.decode(data);
    return c;
  }

  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  public Float getCoinBalance() {
    if(coinBalance==null){
        return DEFAULT_COINBALANCE;
    }
    return coinBalance;
  }

  public Float getHongZuanBalance() {
    if(hongZuanBalance==null){
        return DEFAULT_HONGZUANBALANCE;
    }
    return hongZuanBalance;
  }

  public Long getLastChangeMs() {
    if(lastChangeMs==null){
        return DEFAULT_LASTCHANGEMS;
    }
    return lastChangeMs;
  }

  public boolean hasUserID() {
    return userID!=null;
  }

  public boolean hasCoinBalance() {
    return coinBalance!=null;
  }

  public boolean hasHongZuanBalance() {
    return hongZuanBalance!=null;
  }

  public boolean hasLastChangeMs() {
    return lastChangeMs!=null;
  }

  public static final class Builder extends Message.Builder<Property, Builder> {
    private Integer userID;

    private Float coinBalance;

    private Float hongZuanBalance;

    private Long lastChangeMs;

    public Builder() {
    }

    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    public Builder setCoinBalance(Float coinBalance) {
      this.coinBalance = coinBalance;
      return this;
    }

    public Builder setHongZuanBalance(Float hongZuanBalance) {
      this.hongZuanBalance = hongZuanBalance;
      return this;
    }

    public Builder setLastChangeMs(Long lastChangeMs) {
      this.lastChangeMs = lastChangeMs;
      return this;
    }

    @Override
    public Property build() {
      return new Property(userID, coinBalance, hongZuanBalance, lastChangeMs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Property extends ProtoAdapter<Property> {
    public ProtoAdapter_Property() {
      super(FieldEncoding.LENGTH_DELIMITED, Property.class);
    }

    @Override
    public int encodedSize(Property value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.FLOAT.encodedSizeWithTag(2, value.coinBalance)
          + ProtoAdapter.FLOAT.encodedSizeWithTag(3, value.hongZuanBalance)
          + ProtoAdapter.INT64.encodedSizeWithTag(4, value.lastChangeMs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Property value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.FLOAT.encodeWithTag(writer, 2, value.coinBalance);
      ProtoAdapter.FLOAT.encodeWithTag(writer, 3, value.hongZuanBalance);
      ProtoAdapter.INT64.encodeWithTag(writer, 4, value.lastChangeMs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Property decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setCoinBalance(ProtoAdapter.FLOAT.decode(reader)); break;
          case 3: builder.setHongZuanBalance(ProtoAdapter.FLOAT.decode(reader)); break;
          case 4: builder.setLastChangeMs(ProtoAdapter.INT64.decode(reader)); break;
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
    public Property redact(Property value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}