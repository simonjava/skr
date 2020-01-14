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
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PRspInviteBeGuestSuccess extends Message<PRspInviteBeGuestSuccess, PRspInviteBeGuestSuccess.Builder> {
  public static final ProtoAdapter<PRspInviteBeGuestSuccess> ADAPTER = new ProtoAdapter_PRspInviteBeGuestSuccess();

  private static final long serialVersionUID = 0L;

  /**
   * 座位信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.PartyRoom.SeatInfo#ADAPTER"
  )
  private final SeatInfo seatInfo;

  public PRspInviteBeGuestSuccess(SeatInfo seatInfo) {
    this(seatInfo, ByteString.EMPTY);
  }

  public PRspInviteBeGuestSuccess(SeatInfo seatInfo, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.seatInfo = seatInfo;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.seatInfo = seatInfo;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PRspInviteBeGuestSuccess)) return false;
    PRspInviteBeGuestSuccess o = (PRspInviteBeGuestSuccess) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(seatInfo, o.seatInfo);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (seatInfo != null ? seatInfo.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (seatInfo != null) builder.append(", seatInfo=").append(seatInfo);
    return builder.replace(0, 2, "PRspInviteBeGuestSuccess{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PRspInviteBeGuestSuccess.ADAPTER.encode(this);
  }

  public static final PRspInviteBeGuestSuccess parseFrom(byte[] data) throws IOException {
    PRspInviteBeGuestSuccess c = null;
       c = PRspInviteBeGuestSuccess.ADAPTER.decode(data);
    return c;
  }

  /**
   * 座位信息
   */
  public SeatInfo getSeatInfo() {
    if(seatInfo==null){
        return new SeatInfo.Builder().build();
    }
    return seatInfo;
  }

  /**
   * 座位信息
   */
  public boolean hasSeatInfo() {
    return seatInfo!=null;
  }

  public static final class Builder extends Message.Builder<PRspInviteBeGuestSuccess, Builder> {
    private SeatInfo seatInfo;

    public Builder() {
    }

    /**
     * 座位信息
     */
    public Builder setSeatInfo(SeatInfo seatInfo) {
      this.seatInfo = seatInfo;
      return this;
    }

    @Override
    public PRspInviteBeGuestSuccess build() {
      return new PRspInviteBeGuestSuccess(seatInfo, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PRspInviteBeGuestSuccess extends ProtoAdapter<PRspInviteBeGuestSuccess> {
    public ProtoAdapter_PRspInviteBeGuestSuccess() {
      super(FieldEncoding.LENGTH_DELIMITED, PRspInviteBeGuestSuccess.class);
    }

    @Override
    public int encodedSize(PRspInviteBeGuestSuccess value) {
      return SeatInfo.ADAPTER.encodedSizeWithTag(1, value.seatInfo)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PRspInviteBeGuestSuccess value) throws IOException {
      SeatInfo.ADAPTER.encodeWithTag(writer, 1, value.seatInfo);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PRspInviteBeGuestSuccess decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setSeatInfo(SeatInfo.ADAPTER.decode(reader)); break;
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
    public PRspInviteBeGuestSuccess redact(PRspInviteBeGuestSuccess value) {
      Builder builder = value.newBuilder();
      if (builder.seatInfo != null) builder.seatInfo = SeatInfo.ADAPTER.redact(builder.seatInfo);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
