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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class CombineRoomRefuseMsg extends Message<CombineRoomRefuseMsg, CombineRoomRefuseMsg.Builder> {
  public static final ProtoAdapter<CombineRoomRefuseMsg> ADAPTER = new ProtoAdapter_CombineRoomRefuseMsg();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_REFUSEMSG = "";

  /**
   * 拒绝行为发出的用户信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.UserInfo#ADAPTER"
  )
  private final UserInfo user;

  /**
   * 拒绝反馈文案
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String refuseMsg;

  public CombineRoomRefuseMsg(UserInfo user, String refuseMsg) {
    this(user, refuseMsg, ByteString.EMPTY);
  }

  public CombineRoomRefuseMsg(UserInfo user, String refuseMsg, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.user = user;
    this.refuseMsg = refuseMsg;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.user = user;
    builder.refuseMsg = refuseMsg;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CombineRoomRefuseMsg)) return false;
    CombineRoomRefuseMsg o = (CombineRoomRefuseMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(user, o.user)
        && Internal.equals(refuseMsg, o.refuseMsg);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (refuseMsg != null ? refuseMsg.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (user != null) builder.append(", user=").append(user);
    if (refuseMsg != null) builder.append(", refuseMsg=").append(refuseMsg);
    return builder.replace(0, 2, "CombineRoomRefuseMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return CombineRoomRefuseMsg.ADAPTER.encode(this);
  }

  public static final CombineRoomRefuseMsg parseFrom(byte[] data) throws IOException {
    CombineRoomRefuseMsg c = null;
       c = CombineRoomRefuseMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 拒绝行为发出的用户信息
   */
  public UserInfo getUser() {
    if(user==null){
        return new UserInfo.Builder().build();
    }
    return user;
  }

  /**
   * 拒绝反馈文案
   */
  public String getRefuseMsg() {
    if(refuseMsg==null){
        return DEFAULT_REFUSEMSG;
    }
    return refuseMsg;
  }

  /**
   * 拒绝行为发出的用户信息
   */
  public boolean hasUser() {
    return user!=null;
  }

  /**
   * 拒绝反馈文案
   */
  public boolean hasRefuseMsg() {
    return refuseMsg!=null;
  }

  public static final class Builder extends Message.Builder<CombineRoomRefuseMsg, Builder> {
    private UserInfo user;

    private String refuseMsg;

    public Builder() {
    }

    /**
     * 拒绝行为发出的用户信息
     */
    public Builder setUser(UserInfo user) {
      this.user = user;
      return this;
    }

    /**
     * 拒绝反馈文案
     */
    public Builder setRefuseMsg(String refuseMsg) {
      this.refuseMsg = refuseMsg;
      return this;
    }

    @Override
    public CombineRoomRefuseMsg build() {
      return new CombineRoomRefuseMsg(user, refuseMsg, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_CombineRoomRefuseMsg extends ProtoAdapter<CombineRoomRefuseMsg> {
    public ProtoAdapter_CombineRoomRefuseMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, CombineRoomRefuseMsg.class);
    }

    @Override
    public int encodedSize(CombineRoomRefuseMsg value) {
      return UserInfo.ADAPTER.encodedSizeWithTag(1, value.user)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.refuseMsg)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, CombineRoomRefuseMsg value) throws IOException {
      UserInfo.ADAPTER.encodeWithTag(writer, 1, value.user);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.refuseMsg);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public CombineRoomRefuseMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setRefuseMsg(ProtoAdapter.STRING.decode(reader)); break;
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
    public CombineRoomRefuseMsg redact(CombineRoomRefuseMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
