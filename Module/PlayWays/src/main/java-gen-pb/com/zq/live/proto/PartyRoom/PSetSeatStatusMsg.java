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

public final class PSetSeatStatusMsg extends Message<PSetSeatStatusMsg, PSetSeatStatusMsg.Builder> {
  public static final ProtoAdapter<PSetSeatStatusMsg> ADAPTER = new ProtoAdapter_PSetSeatStatusMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_SEATSEQ = 0;

  public static final ESeatStatus DEFAULT_SEATSTATUS = ESeatStatus.SS_UNKNOWN;

  /**
   * 座位序号
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer seatSeq;

  /**
   * 座位状态
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.PartyRoom.ESeatStatus#ADAPTER"
  )
  private final ESeatStatus seatStatus;

  /**
   * 执行者
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo opUser;

  public PSetSeatStatusMsg(Integer seatSeq, ESeatStatus seatStatus, POnlineInfo opUser) {
    this(seatSeq, seatStatus, opUser, ByteString.EMPTY);
  }

  public PSetSeatStatusMsg(Integer seatSeq, ESeatStatus seatStatus, POnlineInfo opUser,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.seatSeq = seatSeq;
    this.seatStatus = seatStatus;
    this.opUser = opUser;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.seatSeq = seatSeq;
    builder.seatStatus = seatStatus;
    builder.opUser = opUser;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PSetSeatStatusMsg)) return false;
    PSetSeatStatusMsg o = (PSetSeatStatusMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(seatSeq, o.seatSeq)
        && Internal.equals(seatStatus, o.seatStatus)
        && Internal.equals(opUser, o.opUser);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (seatSeq != null ? seatSeq.hashCode() : 0);
      result = result * 37 + (seatStatus != null ? seatStatus.hashCode() : 0);
      result = result * 37 + (opUser != null ? opUser.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (seatSeq != null) builder.append(", seatSeq=").append(seatSeq);
    if (seatStatus != null) builder.append(", seatStatus=").append(seatStatus);
    if (opUser != null) builder.append(", opUser=").append(opUser);
    return builder.replace(0, 2, "PSetSeatStatusMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PSetSeatStatusMsg.ADAPTER.encode(this);
  }

  public static final PSetSeatStatusMsg parseFrom(byte[] data) throws IOException {
    PSetSeatStatusMsg c = null;
       c = PSetSeatStatusMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 座位序号
   */
  public Integer getSeatSeq() {
    if(seatSeq==null){
        return DEFAULT_SEATSEQ;
    }
    return seatSeq;
  }

  /**
   * 座位状态
   */
  public ESeatStatus getSeatStatus() {
    if(seatStatus==null){
        return new ESeatStatus.Builder().build();
    }
    return seatStatus;
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
   * 座位序号
   */
  public boolean hasSeatSeq() {
    return seatSeq!=null;
  }

  /**
   * 座位状态
   */
  public boolean hasSeatStatus() {
    return seatStatus!=null;
  }

  /**
   * 执行者
   */
  public boolean hasOpUser() {
    return opUser!=null;
  }

  public static final class Builder extends Message.Builder<PSetSeatStatusMsg, Builder> {
    private Integer seatSeq;

    private ESeatStatus seatStatus;

    private POnlineInfo opUser;

    public Builder() {
    }

    /**
     * 座位序号
     */
    public Builder setSeatSeq(Integer seatSeq) {
      this.seatSeq = seatSeq;
      return this;
    }

    /**
     * 座位状态
     */
    public Builder setSeatStatus(ESeatStatus seatStatus) {
      this.seatStatus = seatStatus;
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
    public PSetSeatStatusMsg build() {
      return new PSetSeatStatusMsg(seatSeq, seatStatus, opUser, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PSetSeatStatusMsg extends ProtoAdapter<PSetSeatStatusMsg> {
    public ProtoAdapter_PSetSeatStatusMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PSetSeatStatusMsg.class);
    }

    @Override
    public int encodedSize(PSetSeatStatusMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.seatSeq)
          + ESeatStatus.ADAPTER.encodedSizeWithTag(2, value.seatStatus)
          + POnlineInfo.ADAPTER.encodedSizeWithTag(3, value.opUser)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PSetSeatStatusMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.seatSeq);
      ESeatStatus.ADAPTER.encodeWithTag(writer, 2, value.seatStatus);
      POnlineInfo.ADAPTER.encodeWithTag(writer, 3, value.opUser);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PSetSeatStatusMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setSeatSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: {
            try {
              builder.setSeatStatus(ESeatStatus.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 3: builder.setOpUser(POnlineInfo.ADAPTER.decode(reader)); break;
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
    public PSetSeatStatusMsg redact(PSetSeatStatusMsg value) {
      Builder builder = value.newBuilder();
      if (builder.opUser != null) builder.opUser = POnlineInfo.ADAPTER.redact(builder.opUser);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
