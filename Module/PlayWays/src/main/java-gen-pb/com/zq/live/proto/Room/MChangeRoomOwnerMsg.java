// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: mic_room.proto
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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class MChangeRoomOwnerMsg extends Message<MChangeRoomOwnerMsg, MChangeRoomOwnerMsg.Builder> {
  public static final ProtoAdapter<MChangeRoomOwnerMsg> ADAPTER = new ProtoAdapter_MChangeRoomOwnerMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  /**
   * 新房主id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  public MChangeRoomOwnerMsg(Integer userID) {
    this(userID, ByteString.EMPTY);
  }

  public MChangeRoomOwnerMsg(Integer userID, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MChangeRoomOwnerMsg)) return false;
    MChangeRoomOwnerMsg o = (MChangeRoomOwnerMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    return builder.replace(0, 2, "MChangeRoomOwnerMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MChangeRoomOwnerMsg.ADAPTER.encode(this);
  }

  public static final MChangeRoomOwnerMsg parseFrom(byte[] data) throws IOException {
    MChangeRoomOwnerMsg c = null;
       c = MChangeRoomOwnerMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 新房主id
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 新房主id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  public static final class Builder extends Message.Builder<MChangeRoomOwnerMsg, Builder> {
    private Integer userID;

    public Builder() {
    }

    /**
     * 新房主id
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    @Override
    public MChangeRoomOwnerMsg build() {
      return new MChangeRoomOwnerMsg(userID, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MChangeRoomOwnerMsg extends ProtoAdapter<MChangeRoomOwnerMsg> {
    public ProtoAdapter_MChangeRoomOwnerMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, MChangeRoomOwnerMsg.class);
    }

    @Override
    public int encodedSize(MChangeRoomOwnerMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MChangeRoomOwnerMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MChangeRoomOwnerMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
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
    public MChangeRoomOwnerMsg redact(MChangeRoomOwnerMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
