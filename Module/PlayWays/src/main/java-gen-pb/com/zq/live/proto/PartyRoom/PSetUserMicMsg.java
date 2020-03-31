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
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PSetUserMicMsg extends Message<PSetUserMicMsg, PSetUserMicMsg.Builder> {
  public static final ProtoAdapter<PSetUserMicMsg> ADAPTER = new ProtoAdapter_PSetUserMicMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_SEATSEQ = 0;

  public static final EMicStatus DEFAULT_MICSTATUS = EMicStatus.MS_UNKNOWN;

  /**
   * 用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 席位顺序
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer seatSeq;

  /**
   * 麦状态
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.PartyRoom.EMicStatus#ADAPTER"
  )
  private final EMicStatus micStatus;

  /**
   * 执行者
   */
  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo opUser;

  public PSetUserMicMsg(Integer userID, Integer seatSeq, EMicStatus micStatus, POnlineInfo opUser) {
    this(userID, seatSeq, micStatus, opUser, ByteString.EMPTY);
  }

  public PSetUserMicMsg(Integer userID, Integer seatSeq, EMicStatus micStatus, POnlineInfo opUser,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.seatSeq = seatSeq;
    this.micStatus = micStatus;
    this.opUser = opUser;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.seatSeq = seatSeq;
    builder.micStatus = micStatus;
    builder.opUser = opUser;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PSetUserMicMsg)) return false;
    PSetUserMicMsg o = (PSetUserMicMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(seatSeq, o.seatSeq)
        && Internal.equals(micStatus, o.micStatus)
        && Internal.equals(opUser, o.opUser);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (seatSeq != null ? seatSeq.hashCode() : 0);
      result = result * 37 + (micStatus != null ? micStatus.hashCode() : 0);
      result = result * 37 + (opUser != null ? opUser.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (seatSeq != null) builder.append(", seatSeq=").append(seatSeq);
    if (micStatus != null) builder.append(", micStatus=").append(micStatus);
    if (opUser != null) builder.append(", opUser=").append(opUser);
    return builder.replace(0, 2, "PSetUserMicMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PSetUserMicMsg.ADAPTER.encode(this);
  }

  public static final PSetUserMicMsg parseFrom(byte[] data) throws IOException {
    PSetUserMicMsg c = null;
       c = PSetUserMicMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户id
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 席位顺序
   */
  public Integer getSeatSeq() {
    if(seatSeq==null){
        return DEFAULT_SEATSEQ;
    }
    return seatSeq;
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
   * 用户id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 席位顺序
   */
  public boolean hasSeatSeq() {
    return seatSeq!=null;
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

  public static final class Builder extends Message.Builder<PSetUserMicMsg, Builder> {
    private Integer userID;

    private Integer seatSeq;

    private EMicStatus micStatus;

    private POnlineInfo opUser;

    public Builder() {
    }

    /**
     * 用户id
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 席位顺序
     */
    public Builder setSeatSeq(Integer seatSeq) {
      this.seatSeq = seatSeq;
      return this;
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

    @Override
    public PSetUserMicMsg build() {
      return new PSetUserMicMsg(userID, seatSeq, micStatus, opUser, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PSetUserMicMsg extends ProtoAdapter<PSetUserMicMsg> {
    public ProtoAdapter_PSetUserMicMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PSetUserMicMsg.class);
    }

    @Override
    public int encodedSize(PSetUserMicMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.seatSeq)
          + EMicStatus.ADAPTER.encodedSizeWithTag(3, value.micStatus)
          + POnlineInfo.ADAPTER.encodedSizeWithTag(4, value.opUser)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PSetUserMicMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.seatSeq);
      EMicStatus.ADAPTER.encodeWithTag(writer, 3, value.micStatus);
      POnlineInfo.ADAPTER.encodeWithTag(writer, 4, value.opUser);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PSetUserMicMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setSeatSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: {
            try {
              builder.setMicStatus(EMicStatus.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 4: builder.setOpUser(POnlineInfo.ADAPTER.decode(reader)); break;
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
    public PSetUserMicMsg redact(PSetUserMicMsg value) {
      Builder builder = value.newBuilder();
      if (builder.opUser != null) builder.opUser = POnlineInfo.ADAPTER.redact(builder.opUser);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
