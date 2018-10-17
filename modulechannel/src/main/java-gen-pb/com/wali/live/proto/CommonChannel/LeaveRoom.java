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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * type=4 或102 extData填充内容
 */
public final class LeaveRoom extends Message<LeaveRoom, LeaveRoom.Builder> {
  public static final ProtoAdapter<LeaveRoom> ADAPTER = new ProtoAdapter_LeaveRoom();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_ENTERTIME = 0L;

  public static final Long DEFAULT_LEAVETIME = 0L;

  /**
   * 进入房间时间
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long enterTime;

  /**
   * 离开房间时间
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long leaveTime;

  public LeaveRoom(Long enterTime, Long leaveTime) {
    this(enterTime, leaveTime, ByteString.EMPTY);
  }

  public LeaveRoom(Long enterTime, Long leaveTime, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.enterTime = enterTime;
    this.leaveTime = leaveTime;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.enterTime = enterTime;
    builder.leaveTime = leaveTime;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof LeaveRoom)) return false;
    LeaveRoom o = (LeaveRoom) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(enterTime, o.enterTime)
        && Internal.equals(leaveTime, o.leaveTime);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (enterTime != null ? enterTime.hashCode() : 0);
      result = result * 37 + (leaveTime != null ? leaveTime.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (enterTime != null) builder.append(", enterTime=").append(enterTime);
    if (leaveTime != null) builder.append(", leaveTime=").append(leaveTime);
    return builder.replace(0, 2, "LeaveRoom{").append('}').toString();
  }

  public byte[] toByteArray() {
    return LeaveRoom.ADAPTER.encode(this);
  }

  public static final LeaveRoom parseFrom(byte[] data) throws IOException {
    LeaveRoom c = null;
       c = LeaveRoom.ADAPTER.decode(data);
    return c;
  }

  /**
   * 进入房间时间
   */
  public Long getEnterTime() {
    if(enterTime==null){
        return DEFAULT_ENTERTIME;
    }
    return enterTime;
  }

  /**
   * 离开房间时间
   */
  public Long getLeaveTime() {
    if(leaveTime==null){
        return DEFAULT_LEAVETIME;
    }
    return leaveTime;
  }

  /**
   * 进入房间时间
   */
  public boolean hasEnterTime() {
    return enterTime!=null;
  }

  /**
   * 离开房间时间
   */
  public boolean hasLeaveTime() {
    return leaveTime!=null;
  }

  public static final class Builder extends Message.Builder<LeaveRoom, Builder> {
    public Long enterTime;

    public Long leaveTime;

    public Builder() {
    }

    /**
     * 进入房间时间
     */
    public Builder setEnterTime(Long enterTime) {
      this.enterTime = enterTime;
      return this;
    }

    /**
     * 离开房间时间
     */
    public Builder setLeaveTime(Long leaveTime) {
      this.leaveTime = leaveTime;
      return this;
    }

    @Override
    public LeaveRoom build() {
      return new LeaveRoom(enterTime, leaveTime, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_LeaveRoom extends ProtoAdapter<LeaveRoom> {
    public ProtoAdapter_LeaveRoom() {
      super(FieldEncoding.LENGTH_DELIMITED, LeaveRoom.class);
    }

    @Override
    public int encodedSize(LeaveRoom value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.enterTime)
          + ProtoAdapter.UINT64.encodedSizeWithTag(2, value.leaveTime)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, LeaveRoom value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.enterTime);
      ProtoAdapter.UINT64.encodeWithTag(writer, 2, value.leaveTime);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public LeaveRoom decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setEnterTime(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setLeaveTime(ProtoAdapter.UINT64.decode(reader)); break;
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
    public LeaveRoom redact(LeaveRoom value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
