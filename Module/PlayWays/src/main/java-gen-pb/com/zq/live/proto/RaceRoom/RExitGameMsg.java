// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: race_room.proto
package com.zq.live.proto.RaceRoom;

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

public final class RExitGameMsg extends Message<RExitGameMsg, RExitGameMsg.Builder> {
  public static final ProtoAdapter<RExitGameMsg> ADAPTER = new ProtoAdapter_RExitGameMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final ERUserRole DEFAULT_ROLE = ERUserRole.ERUR_UNKNOWN;

  /**
   * 用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 角色
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.RaceRoom.ERUserRole#ADAPTER"
  )
  private final ERUserRole role;

  public RExitGameMsg(Integer userID, ERUserRole role) {
    this(userID, role, ByteString.EMPTY);
  }

  public RExitGameMsg(Integer userID, ERUserRole role, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.role = role;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.role = role;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RExitGameMsg)) return false;
    RExitGameMsg o = (RExitGameMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(role, o.role);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (role != null ? role.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (role != null) builder.append(", role=").append(role);
    return builder.replace(0, 2, "RExitGameMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RExitGameMsg.ADAPTER.encode(this);
  }

  public static final RExitGameMsg parseFrom(byte[] data) throws IOException {
    RExitGameMsg c = null;
       c = RExitGameMsg.ADAPTER.decode(data);
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
   * 角色
   */
  public ERUserRole getRole() {
    if(role==null){
        return new ERUserRole.Builder().build();
    }
    return role;
  }

  /**
   * 用户id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 角色
   */
  public boolean hasRole() {
    return role!=null;
  }

  public static final class Builder extends Message.Builder<RExitGameMsg, Builder> {
    private Integer userID;

    private ERUserRole role;

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
     * 角色
     */
    public Builder setRole(ERUserRole role) {
      this.role = role;
      return this;
    }

    @Override
    public RExitGameMsg build() {
      return new RExitGameMsg(userID, role, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RExitGameMsg extends ProtoAdapter<RExitGameMsg> {
    public ProtoAdapter_RExitGameMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, RExitGameMsg.class);
    }

    @Override
    public int encodedSize(RExitGameMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ERUserRole.ADAPTER.encodedSizeWithTag(2, value.role)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RExitGameMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ERUserRole.ADAPTER.encodeWithTag(writer, 2, value.role);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RExitGameMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: {
            try {
              builder.setRole(ERUserRole.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
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
    public RExitGameMsg redact(RExitGameMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
