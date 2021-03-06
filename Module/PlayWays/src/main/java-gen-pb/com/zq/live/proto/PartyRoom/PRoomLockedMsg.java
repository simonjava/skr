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

public final class PRoomLockedMsg extends Message<PRoomLockedMsg, PRoomLockedMsg.Builder> {
  public static final ProtoAdapter<PRoomLockedMsg> ADAPTER = new ProtoAdapter_PRoomLockedMsg();

  private static final long serialVersionUID = 0L;

  public static final ERoomLockType DEFAULT_ROOMLOCKTYPE = ERoomLockType.RTT_UNKNOWN;

  public static final String DEFAULT_MSG = "";

  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.PartyRoom.ERoomLockType#ADAPTER"
  )
  private final ERoomLockType roomLockType;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String msg;

  public PRoomLockedMsg(ERoomLockType roomLockType, String msg) {
    this(roomLockType, msg, ByteString.EMPTY);
  }

  public PRoomLockedMsg(ERoomLockType roomLockType, String msg, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.roomLockType = roomLockType;
    this.msg = msg;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.roomLockType = roomLockType;
    builder.msg = msg;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PRoomLockedMsg)) return false;
    PRoomLockedMsg o = (PRoomLockedMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(roomLockType, o.roomLockType)
        && Internal.equals(msg, o.msg);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (roomLockType != null ? roomLockType.hashCode() : 0);
      result = result * 37 + (msg != null ? msg.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (roomLockType != null) builder.append(", roomLockType=").append(roomLockType);
    if (msg != null) builder.append(", msg=").append(msg);
    return builder.replace(0, 2, "PRoomLockedMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PRoomLockedMsg.ADAPTER.encode(this);
  }

  public static final PRoomLockedMsg parseFrom(byte[] data) throws IOException {
    PRoomLockedMsg c = null;
       c = PRoomLockedMsg.ADAPTER.decode(data);
    return c;
  }

  public ERoomLockType getRoomLockType() {
    if(roomLockType==null){
        return new ERoomLockType.Builder().build();
    }
    return roomLockType;
  }

  public String getMsg() {
    if(msg==null){
        return DEFAULT_MSG;
    }
    return msg;
  }

  public boolean hasRoomLockType() {
    return roomLockType!=null;
  }

  public boolean hasMsg() {
    return msg!=null;
  }

  public static final class Builder extends Message.Builder<PRoomLockedMsg, Builder> {
    private ERoomLockType roomLockType;

    private String msg;

    public Builder() {
    }

    public Builder setRoomLockType(ERoomLockType roomLockType) {
      this.roomLockType = roomLockType;
      return this;
    }

    public Builder setMsg(String msg) {
      this.msg = msg;
      return this;
    }

    @Override
    public PRoomLockedMsg build() {
      return new PRoomLockedMsg(roomLockType, msg, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PRoomLockedMsg extends ProtoAdapter<PRoomLockedMsg> {
    public ProtoAdapter_PRoomLockedMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PRoomLockedMsg.class);
    }

    @Override
    public int encodedSize(PRoomLockedMsg value) {
      return ERoomLockType.ADAPTER.encodedSizeWithTag(1, value.roomLockType)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.msg)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PRoomLockedMsg value) throws IOException {
      ERoomLockType.ADAPTER.encodeWithTag(writer, 1, value.roomLockType);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.msg);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PRoomLockedMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: {
            try {
              builder.setRoomLockType(ERoomLockType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 2: builder.setMsg(ProtoAdapter.STRING.decode(reader)); break;
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
    public PRoomLockedMsg redact(PRoomLockedMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
