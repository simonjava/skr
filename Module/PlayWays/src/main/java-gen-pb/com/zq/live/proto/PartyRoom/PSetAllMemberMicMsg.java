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
import java.util.List;
import okio.ByteString;

public final class PSetAllMemberMicMsg extends Message<PSetAllMemberMicMsg, PSetAllMemberMicMsg.Builder> {
  public static final ProtoAdapter<PSetAllMemberMicMsg> ADAPTER = new ProtoAdapter_PSetAllMemberMicMsg();

  private static final long serialVersionUID = 0L;

  public static final EMicStatus DEFAULT_MICSTATUS = EMicStatus.MS_UNKNOWN;

  /**
   * 麦状态
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.PartyRoom.EMicStatus#ADAPTER"
  )
  private final EMicStatus micStatus;

  /**
   * 执行者
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo opUser;

  /**
   * 座位最新信息
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.PartyRoom.SeatInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<SeatInfo> seats;

  public PSetAllMemberMicMsg(EMicStatus micStatus, POnlineInfo opUser, List<SeatInfo> seats) {
    this(micStatus, opUser, seats, ByteString.EMPTY);
  }

  public PSetAllMemberMicMsg(EMicStatus micStatus, POnlineInfo opUser, List<SeatInfo> seats,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.micStatus = micStatus;
    this.opUser = opUser;
    this.seats = Internal.immutableCopyOf("seats", seats);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.micStatus = micStatus;
    builder.opUser = opUser;
    builder.seats = Internal.copyOf("seats", seats);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PSetAllMemberMicMsg)) return false;
    PSetAllMemberMicMsg o = (PSetAllMemberMicMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(micStatus, o.micStatus)
        && Internal.equals(opUser, o.opUser)
        && seats.equals(o.seats);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (micStatus != null ? micStatus.hashCode() : 0);
      result = result * 37 + (opUser != null ? opUser.hashCode() : 0);
      result = result * 37 + seats.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (micStatus != null) builder.append(", micStatus=").append(micStatus);
    if (opUser != null) builder.append(", opUser=").append(opUser);
    if (!seats.isEmpty()) builder.append(", seats=").append(seats);
    return builder.replace(0, 2, "PSetAllMemberMicMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PSetAllMemberMicMsg.ADAPTER.encode(this);
  }

  public static final PSetAllMemberMicMsg parseFrom(byte[] data) throws IOException {
    PSetAllMemberMicMsg c = null;
       c = PSetAllMemberMicMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 麦状态
   */
  public EMicStatus getMicStatus() {
    if(micStatus==null){
        return new EMicStatus.Builder().build();
    }
    return micStatus;
  }

  /**
   * 执行者
   */
  public POnlineInfo getOpUser() {
    if(opUser==null){
        return new POnlineInfo.Builder().build();
    }
    return opUser;
  }

  /**
   * 座位最新信息
   */
  public List<SeatInfo> getSeatsList() {
    if(seats==null){
        return new java.util.ArrayList<SeatInfo>();
    }
    return seats;
  }

  /**
   * 麦状态
   */
  public boolean hasMicStatus() {
    return micStatus!=null;
  }

  /**
   * 执行者
   */
  public boolean hasOpUser() {
    return opUser!=null;
  }

  /**
   * 座位最新信息
   */
  public boolean hasSeatsList() {
    return seats!=null;
  }

  public static final class Builder extends Message.Builder<PSetAllMemberMicMsg, Builder> {
    private EMicStatus micStatus;

    private POnlineInfo opUser;

    private List<SeatInfo> seats;

    public Builder() {
      seats = Internal.newMutableList();
    }

    /**
     * 麦状态
     */
    public Builder setMicStatus(EMicStatus micStatus) {
      this.micStatus = micStatus;
      return this;
    }

    /**
     * 执行者
     */
    public Builder setOpUser(POnlineInfo opUser) {
      this.opUser = opUser;
      return this;
    }

    /**
     * 座位最新信息
     */
    public Builder addAllSeats(List<SeatInfo> seats) {
      Internal.checkElementsNotNull(seats);
      this.seats = seats;
      return this;
    }

    @Override
    public PSetAllMemberMicMsg build() {
      return new PSetAllMemberMicMsg(micStatus, opUser, seats, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PSetAllMemberMicMsg extends ProtoAdapter<PSetAllMemberMicMsg> {
    public ProtoAdapter_PSetAllMemberMicMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PSetAllMemberMicMsg.class);
    }

    @Override
    public int encodedSize(PSetAllMemberMicMsg value) {
      return EMicStatus.ADAPTER.encodedSizeWithTag(1, value.micStatus)
          + POnlineInfo.ADAPTER.encodedSizeWithTag(2, value.opUser)
          + SeatInfo.ADAPTER.asRepeated().encodedSizeWithTag(3, value.seats)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PSetAllMemberMicMsg value) throws IOException {
      EMicStatus.ADAPTER.encodeWithTag(writer, 1, value.micStatus);
      POnlineInfo.ADAPTER.encodeWithTag(writer, 2, value.opUser);
      SeatInfo.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.seats);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PSetAllMemberMicMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: {
            try {
              builder.setMicStatus(EMicStatus.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 2: builder.setOpUser(POnlineInfo.ADAPTER.decode(reader)); break;
          case 3: builder.seats.add(SeatInfo.ADAPTER.decode(reader)); break;
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
    public PSetAllMemberMicMsg redact(PSetAllMemberMicMsg value) {
      Builder builder = value.newBuilder();
      if (builder.opUser != null) builder.opUser = POnlineInfo.ADAPTER.redact(builder.opUser);
      Internal.redactElements(builder.seats, SeatInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
