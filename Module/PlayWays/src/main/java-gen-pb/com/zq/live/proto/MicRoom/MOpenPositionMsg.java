// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: mic_room.proto
package com.zq.live.proto.MicRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class MOpenPositionMsg extends Message<MOpenPositionMsg, MOpenPositionMsg.Builder> {
  public static final ProtoAdapter<MOpenPositionMsg> ADAPTER = new ProtoAdapter_MOpenPositionMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_POSITIONID = 0;

  /**
   * 位置id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer positionID;

  public MOpenPositionMsg(Integer positionID) {
    this(positionID, ByteString.EMPTY);
  }

  public MOpenPositionMsg(Integer positionID, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.positionID = positionID;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.positionID = positionID;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MOpenPositionMsg)) return false;
    MOpenPositionMsg o = (MOpenPositionMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(positionID, o.positionID);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (positionID != null ? positionID.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (positionID != null) builder.append(", positionID=").append(positionID);
    return builder.replace(0, 2, "MOpenPositionMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MOpenPositionMsg.ADAPTER.encode(this);
  }

  public static final MOpenPositionMsg parseFrom(byte[] data) throws IOException {
    MOpenPositionMsg c = null;
       c = MOpenPositionMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 位置id
   */
  public Integer getPositionID() {
    if(positionID==null){
        return DEFAULT_POSITIONID;
    }
    return positionID;
  }

  /**
   * 位置id
   */
  public boolean hasPositionID() {
    return positionID!=null;
  }

  public static final class Builder extends Message.Builder<MOpenPositionMsg, Builder> {
    private Integer positionID;

    public Builder() {
    }

    /**
     * 位置id
     */
    public Builder setPositionID(Integer positionID) {
      this.positionID = positionID;
      return this;
    }

    @Override
    public MOpenPositionMsg build() {
      return new MOpenPositionMsg(positionID, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MOpenPositionMsg extends ProtoAdapter<MOpenPositionMsg> {
    public ProtoAdapter_MOpenPositionMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, MOpenPositionMsg.class);
    }

    @Override
    public int encodedSize(MOpenPositionMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.positionID)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MOpenPositionMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.positionID);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MOpenPositionMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setPositionID(ProtoAdapter.UINT32.decode(reader)); break;
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
    public MOpenPositionMsg redact(MOpenPositionMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
