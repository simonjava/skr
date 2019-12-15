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

public final class PClubBecomeHostMsg extends Message<PClubBecomeHostMsg, PClubBecomeHostMsg.Builder> {
  public static final ProtoAdapter<PClubBecomeHostMsg> ADAPTER = new ProtoAdapter_PClubBecomeHostMsg();

  private static final long serialVersionUID = 0L;

  /**
   * 新增的主持人信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.PartyRoom.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo user;

  public PClubBecomeHostMsg(POnlineInfo user) {
    this(user, ByteString.EMPTY);
  }

  public PClubBecomeHostMsg(POnlineInfo user, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.user = user;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.user = user;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PClubBecomeHostMsg)) return false;
    PClubBecomeHostMsg o = (PClubBecomeHostMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(user, o.user);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (user != null ? user.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (user != null) builder.append(", user=").append(user);
    return builder.replace(0, 2, "PClubBecomeHostMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PClubBecomeHostMsg.ADAPTER.encode(this);
  }

  public static final PClubBecomeHostMsg parseFrom(byte[] data) throws IOException {
    PClubBecomeHostMsg c = null;
       c = PClubBecomeHostMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 新增的主持人信息
   */
  public POnlineInfo getUser() {
    if(user==null){
        return new POnlineInfo.Builder().build();
    }
    return user;
  }

  /**
   * 新增的主持人信息
   */
  public boolean hasUser() {
    return user!=null;
  }

  public static final class Builder extends Message.Builder<PClubBecomeHostMsg, Builder> {
    private POnlineInfo user;

    public Builder() {
    }

    /**
     * 新增的主持人信息
     */
    public Builder setUser(POnlineInfo user) {
      this.user = user;
      return this;
    }

    @Override
    public PClubBecomeHostMsg build() {
      return new PClubBecomeHostMsg(user, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PClubBecomeHostMsg extends ProtoAdapter<PClubBecomeHostMsg> {
    public ProtoAdapter_PClubBecomeHostMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PClubBecomeHostMsg.class);
    }

    @Override
    public int encodedSize(PClubBecomeHostMsg value) {
      return POnlineInfo.ADAPTER.encodedSizeWithTag(1, value.user)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PClubBecomeHostMsg value) throws IOException {
      POnlineInfo.ADAPTER.encodeWithTag(writer, 1, value.user);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PClubBecomeHostMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUser(POnlineInfo.ADAPTER.decode(reader)); break;
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
    public PClubBecomeHostMsg redact(PClubBecomeHostMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = POnlineInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
