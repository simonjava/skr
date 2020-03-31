// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: party_room.proto
package com.zq.live.proto.PartyRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.POnlineInfo;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PChangeSeatMsg extends Message<PChangeSeatMsg, PChangeSeatMsg.Builder> {
  public static final ProtoAdapter<PChangeSeatMsg> ADAPTER = new ProtoAdapter_PChangeSeatMsg();

  private static final long serialVersionUID = 0L;

  /**
   * 用户信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo user;

  /**
   * 换前座位信息
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.PartyRoom.SeatInfo#ADAPTER"
  )
  private final SeatInfo beforeSeatInfo;

  /**
   * 换后座位信息
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.PartyRoom.SeatInfo#ADAPTER"
  )
  private final SeatInfo afterSeatInfo;

  public PChangeSeatMsg(POnlineInfo user, SeatInfo beforeSeatInfo, SeatInfo afterSeatInfo) {
    this(user, beforeSeatInfo, afterSeatInfo, ByteString.EMPTY);
  }

  public PChangeSeatMsg(POnlineInfo user, SeatInfo beforeSeatInfo, SeatInfo afterSeatInfo,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.user = user;
    this.beforeSeatInfo = beforeSeatInfo;
    this.afterSeatInfo = afterSeatInfo;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.user = user;
    builder.beforeSeatInfo = beforeSeatInfo;
    builder.afterSeatInfo = afterSeatInfo;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PChangeSeatMsg)) return false;
    PChangeSeatMsg o = (PChangeSeatMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(user, o.user)
        && Internal.equals(beforeSeatInfo, o.beforeSeatInfo)
        && Internal.equals(afterSeatInfo, o.afterSeatInfo);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (beforeSeatInfo != null ? beforeSeatInfo.hashCode() : 0);
      result = result * 37 + (afterSeatInfo != null ? afterSeatInfo.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (user != null) builder.append(", user=").append(user);
    if (beforeSeatInfo != null) builder.append(", beforeSeatInfo=").append(beforeSeatInfo);
    if (afterSeatInfo != null) builder.append(", afterSeatInfo=").append(afterSeatInfo);
    return builder.replace(0, 2, "PChangeSeatMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PChangeSeatMsg.ADAPTER.encode(this);
  }

  public static final PChangeSeatMsg parseFrom(byte[] data) throws IOException {
    PChangeSeatMsg c = null;
       c = PChangeSeatMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户信息
   */
  public POnlineInfo getUser() {
    if(user==null){
        return new POnlineInfo.Builder().build();
    }
    return user;
  }

  /**
   * 换前座位信息
   */
  public SeatInfo getBeforeSeatInfo() {
    if(beforeSeatInfo==null){
        return new SeatInfo.Builder().build();
    }
    return beforeSeatInfo;
  }

  /**
   * 换后座位信息
   */
  public SeatInfo getAfterSeatInfo() {
    if(afterSeatInfo==null){
        return new SeatInfo.Builder().build();
    }
    return afterSeatInfo;
  }

  /**
   * 用户信息
   */
  public boolean hasUser() {
    return user!=null;
  }

  /**
   * 换前座位信息
   */
  public boolean hasBeforeSeatInfo() {
    return beforeSeatInfo!=null;
  }

  /**
   * 换后座位信息
   */
  public boolean hasAfterSeatInfo() {
    return afterSeatInfo!=null;
  }

  public static final class Builder extends Message.Builder<PChangeSeatMsg, Builder> {
    private POnlineInfo user;

    private SeatInfo beforeSeatInfo;

    private SeatInfo afterSeatInfo;

    public Builder() {
    }

    /**
     * 用户信息
     */
    public Builder setUser(POnlineInfo user) {
      this.user = user;
      return this;
    }

    /**
     * 换前座位信息
     */
    public Builder setBeforeSeatInfo(SeatInfo beforeSeatInfo) {
      this.beforeSeatInfo = beforeSeatInfo;
      return this;
    }

    /**
     * 换后座位信息
     */
    public Builder setAfterSeatInfo(SeatInfo afterSeatInfo) {
      this.afterSeatInfo = afterSeatInfo;
      return this;
    }

    @Override
    public PChangeSeatMsg build() {
      return new PChangeSeatMsg(user, beforeSeatInfo, afterSeatInfo, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PChangeSeatMsg extends ProtoAdapter<PChangeSeatMsg> {
    public ProtoAdapter_PChangeSeatMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PChangeSeatMsg.class);
    }

    @Override
    public int encodedSize(PChangeSeatMsg value) {
      return POnlineInfo.ADAPTER.encodedSizeWithTag(1, value.user)
          + SeatInfo.ADAPTER.encodedSizeWithTag(2, value.beforeSeatInfo)
          + SeatInfo.ADAPTER.encodedSizeWithTag(3, value.afterSeatInfo)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PChangeSeatMsg value) throws IOException {
      POnlineInfo.ADAPTER.encodeWithTag(writer, 1, value.user);
      SeatInfo.ADAPTER.encodeWithTag(writer, 2, value.beforeSeatInfo);
      SeatInfo.ADAPTER.encodeWithTag(writer, 3, value.afterSeatInfo);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PChangeSeatMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUser(POnlineInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setBeforeSeatInfo(SeatInfo.ADAPTER.decode(reader)); break;
          case 3: builder.setAfterSeatInfo(SeatInfo.ADAPTER.decode(reader)); break;
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
    public PChangeSeatMsg redact(PChangeSeatMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = POnlineInfo.ADAPTER.redact(builder.user);
      if (builder.beforeSeatInfo != null) builder.beforeSeatInfo = SeatInfo.ADAPTER.redact(builder.beforeSeatInfo);
      if (builder.afterSeatInfo != null) builder.afterSeatInfo = SeatInfo.ADAPTER.redact(builder.afterSeatInfo);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
