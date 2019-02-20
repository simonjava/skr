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
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class BLightInfo extends Message<BLightInfo, BLightInfo.Builder> {
  public static final ProtoAdapter<BLightInfo> ADAPTER = new ProtoAdapter_BLightInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Long DEFAULT_TIMEMS = 0L;

  /**
   * 玩家id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer userID;

  /**
   * 爆灯时间戳
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  public final Long timeMs;

  public BLightInfo(Integer userID, Long timeMs) {
    this(userID, timeMs, ByteString.EMPTY);
  }

  public BLightInfo(Integer userID, Long timeMs, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.timeMs = timeMs;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.timeMs = timeMs;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof BLightInfo)) return false;
    BLightInfo o = (BLightInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(timeMs, o.timeMs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (timeMs != null ? timeMs.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (timeMs != null) builder.append(", timeMs=").append(timeMs);
    return builder.replace(0, 2, "BLightInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return BLightInfo.ADAPTER.encode(this);
  }

  public static final BLightInfo parseFrom(byte[] data) throws IOException {
    BLightInfo c = null;
       c = BLightInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 玩家id
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 爆灯时间戳
   */
  public Long getTimeMs() {
    if(timeMs==null){
        return DEFAULT_TIMEMS;
    }
    return timeMs;
  }

  /**
   * 玩家id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 爆灯时间戳
   */
  public boolean hasTimeMs() {
    return timeMs!=null;
  }

  public static final class Builder extends Message.Builder<BLightInfo, Builder> {
    public Integer userID;

    public Long timeMs;

    public Builder() {
    }

    /**
     * 玩家id
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 爆灯时间戳
     */
    public Builder setTimeMs(Long timeMs) {
      this.timeMs = timeMs;
      return this;
    }

    @Override
    public BLightInfo build() {
      return new BLightInfo(userID, timeMs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_BLightInfo extends ProtoAdapter<BLightInfo> {
    public ProtoAdapter_BLightInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, BLightInfo.class);
    }

    @Override
    public int encodedSize(BLightInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.SINT64.encodedSizeWithTag(2, value.timeMs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, BLightInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.SINT64.encodeWithTag(writer, 2, value.timeMs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public BLightInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
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
    public BLightInfo redact(BLightInfo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
