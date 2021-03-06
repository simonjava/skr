// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: notification.proto
package com.zq.live.proto.Notification;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.UserInfo;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class InviteMicMsg extends Message<InviteMicMsg, InviteMicMsg.Builder> {
  public static final ProtoAdapter<InviteMicMsg> ADAPTER = new ProtoAdapter_InviteMicMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ROOMID = 0;

  /**
   * 发起邀请的用户详情
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.UserInfo#ADAPTER"
  )
  private final UserInfo user;

  /**
   * 房间id
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  public InviteMicMsg(UserInfo user, Integer roomID) {
    this(user, roomID, ByteString.EMPTY);
  }

  public InviteMicMsg(UserInfo user, Integer roomID, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.user = user;
    this.roomID = roomID;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.user = user;
    builder.roomID = roomID;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof InviteMicMsg)) return false;
    InviteMicMsg o = (InviteMicMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(user, o.user)
        && Internal.equals(roomID, o.roomID);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (user != null) builder.append(", user=").append(user);
    if (roomID != null) builder.append(", roomID=").append(roomID);
    return builder.replace(0, 2, "InviteMicMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return InviteMicMsg.ADAPTER.encode(this);
  }

  public static final InviteMicMsg parseFrom(byte[] data) throws IOException {
    InviteMicMsg c = null;
       c = InviteMicMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 发起邀请的用户详情
   */
  public UserInfo getUser() {
    if(user==null){
        return new UserInfo.Builder().build();
    }
    return user;
  }

  /**
   * 房间id
   */
  public Integer getRoomID() {
    if(roomID==null){
        return DEFAULT_ROOMID;
    }
    return roomID;
  }

  /**
   * 发起邀请的用户详情
   */
  public boolean hasUser() {
    return user!=null;
  }

  /**
   * 房间id
   */
  public boolean hasRoomID() {
    return roomID!=null;
  }

  public static final class Builder extends Message.Builder<InviteMicMsg, Builder> {
    private UserInfo user;

    private Integer roomID;

    public Builder() {
    }

    /**
     * 发起邀请的用户详情
     */
    public Builder setUser(UserInfo user) {
      this.user = user;
      return this;
    }

    /**
     * 房间id
     */
    public Builder setRoomID(Integer roomID) {
      this.roomID = roomID;
      return this;
    }

    @Override
    public InviteMicMsg build() {
      return new InviteMicMsg(user, roomID, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_InviteMicMsg extends ProtoAdapter<InviteMicMsg> {
    public ProtoAdapter_InviteMicMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, InviteMicMsg.class);
    }

    @Override
    public int encodedSize(InviteMicMsg value) {
      return UserInfo.ADAPTER.encodedSizeWithTag(1, value.user)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.roomID)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, InviteMicMsg value) throws IOException {
      UserInfo.ADAPTER.encodeWithTag(writer, 1, value.user);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.roomID);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public InviteMicMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
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
    public InviteMicMsg redact(InviteMicMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
