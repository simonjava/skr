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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class RelayRoomInviteMsg extends Message<RelayRoomInviteMsg, RelayRoomInviteMsg.Builder> {
  public static final ProtoAdapter<RelayRoomInviteMsg> ADAPTER = new ProtoAdapter_RelayRoomInviteMsg();

  private static final long serialVersionUID = 0L;

  public static final ERInviteType DEFAULT_INVITETYPE = ERInviteType.RIT_UNKNOWN;

  public static final String DEFAULT_INVITEMSG = "";

  public static final Integer DEFAULT_ROOMID = 0;

  public static final Long DEFAULT_INVITETIMEMS = 0L;

  /**
   * 邀请类型
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Notification.ERInviteType#ADAPTER"
  )
  private final ERInviteType inviteType;

  /**
   * 发起邀请的用户详情
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.UserInfo#ADAPTER"
  )
  private final UserInfo user;

  /**
   * 邀请信息
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String inviteMsg;

  /**
   * 房间id
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  /**
   * 邀请时间戳
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long inviteTimeMs;

  public RelayRoomInviteMsg(ERInviteType inviteType, UserInfo user, String inviteMsg,
      Integer roomID, Long inviteTimeMs) {
    this(inviteType, user, inviteMsg, roomID, inviteTimeMs, ByteString.EMPTY);
  }

  public RelayRoomInviteMsg(ERInviteType inviteType, UserInfo user, String inviteMsg,
      Integer roomID, Long inviteTimeMs, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.inviteType = inviteType;
    this.user = user;
    this.inviteMsg = inviteMsg;
    this.roomID = roomID;
    this.inviteTimeMs = inviteTimeMs;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.inviteType = inviteType;
    builder.user = user;
    builder.inviteMsg = inviteMsg;
    builder.roomID = roomID;
    builder.inviteTimeMs = inviteTimeMs;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RelayRoomInviteMsg)) return false;
    RelayRoomInviteMsg o = (RelayRoomInviteMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(inviteType, o.inviteType)
        && Internal.equals(user, o.user)
        && Internal.equals(inviteMsg, o.inviteMsg)
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(inviteTimeMs, o.inviteTimeMs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (inviteType != null ? inviteType.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (inviteMsg != null ? inviteMsg.hashCode() : 0);
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (inviteTimeMs != null ? inviteTimeMs.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (inviteType != null) builder.append(", inviteType=").append(inviteType);
    if (user != null) builder.append(", user=").append(user);
    if (inviteMsg != null) builder.append(", inviteMsg=").append(inviteMsg);
    if (roomID != null) builder.append(", roomID=").append(roomID);
    if (inviteTimeMs != null) builder.append(", inviteTimeMs=").append(inviteTimeMs);
    return builder.replace(0, 2, "RelayRoomInviteMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RelayRoomInviteMsg.ADAPTER.encode(this);
  }

  public static final RelayRoomInviteMsg parseFrom(byte[] data) throws IOException {
    RelayRoomInviteMsg c = null;
       c = RelayRoomInviteMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 邀请类型
   */
  public ERInviteType getInviteType() {
    if(inviteType==null){
        return new ERInviteType.Builder().build();
    }
    return inviteType;
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
   * 邀请信息
   */
  public String getInviteMsg() {
    if(inviteMsg==null){
        return DEFAULT_INVITEMSG;
    }
    return inviteMsg;
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
   * 邀请时间戳
   */
  public Long getInviteTimeMs() {
    if(inviteTimeMs==null){
        return DEFAULT_INVITETIMEMS;
    }
    return inviteTimeMs;
  }

  /**
   * 邀请类型
   */
  public boolean hasInviteType() {
    return inviteType!=null;
  }

  /**
   * 发起邀请的用户详情
   */
  public boolean hasUser() {
    return user!=null;
  }

  /**
   * 邀请信息
   */
  public boolean hasInviteMsg() {
    return inviteMsg!=null;
  }

  /**
   * 房间id
   */
  public boolean hasRoomID() {
    return roomID!=null;
  }

  /**
   * 邀请时间戳
   */
  public boolean hasInviteTimeMs() {
    return inviteTimeMs!=null;
  }

  public static final class Builder extends Message.Builder<RelayRoomInviteMsg, Builder> {
    private ERInviteType inviteType;

    private UserInfo user;

    private String inviteMsg;

    private Integer roomID;

    private Long inviteTimeMs;

    public Builder() {
    }

    /**
     * 邀请类型
     */
    public Builder setInviteType(ERInviteType inviteType) {
      this.inviteType = inviteType;
      return this;
    }

    /**
     * 发起邀请的用户详情
     */
    public Builder setUser(UserInfo user) {
      this.user = user;
      return this;
    }

    /**
     * 邀请信息
     */
    public Builder setInviteMsg(String inviteMsg) {
      this.inviteMsg = inviteMsg;
      return this;
    }

    /**
     * 房间id
     */
    public Builder setRoomID(Integer roomID) {
      this.roomID = roomID;
      return this;
    }

    /**
     * 邀请时间戳
     */
    public Builder setInviteTimeMs(Long inviteTimeMs) {
      this.inviteTimeMs = inviteTimeMs;
      return this;
    }

    @Override
    public RelayRoomInviteMsg build() {
      return new RelayRoomInviteMsg(inviteType, user, inviteMsg, roomID, inviteTimeMs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RelayRoomInviteMsg extends ProtoAdapter<RelayRoomInviteMsg> {
    public ProtoAdapter_RelayRoomInviteMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, RelayRoomInviteMsg.class);
    }

    @Override
    public int encodedSize(RelayRoomInviteMsg value) {
      return ERInviteType.ADAPTER.encodedSizeWithTag(1, value.inviteType)
          + UserInfo.ADAPTER.encodedSizeWithTag(2, value.user)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.inviteMsg)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.roomID)
          + ProtoAdapter.SINT64.encodedSizeWithTag(6, value.inviteTimeMs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RelayRoomInviteMsg value) throws IOException {
      ERInviteType.ADAPTER.encodeWithTag(writer, 1, value.inviteType);
      UserInfo.ADAPTER.encodeWithTag(writer, 2, value.user);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.inviteMsg);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.roomID);
      ProtoAdapter.SINT64.encodeWithTag(writer, 6, value.inviteTimeMs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RelayRoomInviteMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: {
            try {
              builder.setInviteType(ERInviteType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 2: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
          case 3: builder.setInviteMsg(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.setInviteTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
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
    public RelayRoomInviteMsg redact(RelayRoomInviteMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
