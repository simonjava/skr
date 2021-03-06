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

public final class CombineRoomInviteMsg extends Message<CombineRoomInviteMsg, CombineRoomInviteMsg.Builder> {
  public static final ProtoAdapter<CombineRoomInviteMsg> ADAPTER = new ProtoAdapter_CombineRoomInviteMsg();

  private static final long serialVersionUID = 0L;

  public static final EInviteType DEFAULT_INVITETYPE = EInviteType.IT_UNKNOWN;

  public static final String DEFAULT_INVITEMSG = "";

  public static final Integer DEFAULT_ROOMID = 0;

  /**
   * 邀请类型
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Notification.EInviteType#ADAPTER"
  )
  private final EInviteType inviteType;

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

  public CombineRoomInviteMsg(EInviteType inviteType, UserInfo user, String inviteMsg,
      Integer roomID) {
    this(inviteType, user, inviteMsg, roomID, ByteString.EMPTY);
  }

  public CombineRoomInviteMsg(EInviteType inviteType, UserInfo user, String inviteMsg,
      Integer roomID, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.inviteType = inviteType;
    this.user = user;
    this.inviteMsg = inviteMsg;
    this.roomID = roomID;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.inviteType = inviteType;
    builder.user = user;
    builder.inviteMsg = inviteMsg;
    builder.roomID = roomID;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CombineRoomInviteMsg)) return false;
    CombineRoomInviteMsg o = (CombineRoomInviteMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(inviteType, o.inviteType)
        && Internal.equals(user, o.user)
        && Internal.equals(inviteMsg, o.inviteMsg)
        && Internal.equals(roomID, o.roomID);
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
    return builder.replace(0, 2, "CombineRoomInviteMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return CombineRoomInviteMsg.ADAPTER.encode(this);
  }

  public static final CombineRoomInviteMsg parseFrom(byte[] data) throws IOException {
    CombineRoomInviteMsg c = null;
       c = CombineRoomInviteMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 邀请类型
   */
  public EInviteType getInviteType() {
    if(inviteType==null){
        return new EInviteType.Builder().build();
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

  public static final class Builder extends Message.Builder<CombineRoomInviteMsg, Builder> {
    private EInviteType inviteType;

    private UserInfo user;

    private String inviteMsg;

    private Integer roomID;

    public Builder() {
    }

    /**
     * 邀请类型
     */
    public Builder setInviteType(EInviteType inviteType) {
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

    @Override
    public CombineRoomInviteMsg build() {
      return new CombineRoomInviteMsg(inviteType, user, inviteMsg, roomID, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_CombineRoomInviteMsg extends ProtoAdapter<CombineRoomInviteMsg> {
    public ProtoAdapter_CombineRoomInviteMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, CombineRoomInviteMsg.class);
    }

    @Override
    public int encodedSize(CombineRoomInviteMsg value) {
      return EInviteType.ADAPTER.encodedSizeWithTag(1, value.inviteType)
          + UserInfo.ADAPTER.encodedSizeWithTag(2, value.user)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.inviteMsg)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.roomID)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, CombineRoomInviteMsg value) throws IOException {
      EInviteType.ADAPTER.encodeWithTag(writer, 1, value.inviteType);
      UserInfo.ADAPTER.encodeWithTag(writer, 2, value.user);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.inviteMsg);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.roomID);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public CombineRoomInviteMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: {
            try {
              builder.setInviteType(EInviteType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 2: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
          case 3: builder.setInviteMsg(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
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
    public CombineRoomInviteMsg redact(CombineRoomInviteMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
