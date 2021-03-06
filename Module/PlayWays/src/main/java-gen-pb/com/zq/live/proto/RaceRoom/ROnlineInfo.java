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
import com.zq.live.proto.Common.UserInfo;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class ROnlineInfo extends Message<ROnlineInfo, ROnlineInfo.Builder> {
  public static final ProtoAdapter<ROnlineInfo> ADAPTER = new ProtoAdapter_ROnlineInfo();

  private static final long serialVersionUID = 0L;

  public static final Boolean DEFAULT_ISONLINE = false;

  public static final ERUserRole DEFAULT_ROLE = ERUserRole.ERUR_UNKNOWN;

  /**
   * 用户信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.UserInfo#ADAPTER"
  )
  private final UserInfo userInfo;

  /**
   * 是否在线
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  private final Boolean isOnline;

  /**
   * 用户角色
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.RaceRoom.ERUserRole#ADAPTER"
  )
  private final ERUserRole role;

  /**
   * 蒙面用户信息
   */
  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.RaceRoom.FakeUserInfo#ADAPTER"
  )
  private final FakeUserInfo fakeUserInfo;

  public ROnlineInfo(UserInfo userInfo, Boolean isOnline, ERUserRole role,
      FakeUserInfo fakeUserInfo) {
    this(userInfo, isOnline, role, fakeUserInfo, ByteString.EMPTY);
  }

  public ROnlineInfo(UserInfo userInfo, Boolean isOnline, ERUserRole role,
      FakeUserInfo fakeUserInfo, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userInfo = userInfo;
    this.isOnline = isOnline;
    this.role = role;
    this.fakeUserInfo = fakeUserInfo;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userInfo = userInfo;
    builder.isOnline = isOnline;
    builder.role = role;
    builder.fakeUserInfo = fakeUserInfo;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ROnlineInfo)) return false;
    ROnlineInfo o = (ROnlineInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userInfo, o.userInfo)
        && Internal.equals(isOnline, o.isOnline)
        && Internal.equals(role, o.role)
        && Internal.equals(fakeUserInfo, o.fakeUserInfo);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userInfo != null ? userInfo.hashCode() : 0);
      result = result * 37 + (isOnline != null ? isOnline.hashCode() : 0);
      result = result * 37 + (role != null ? role.hashCode() : 0);
      result = result * 37 + (fakeUserInfo != null ? fakeUserInfo.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userInfo != null) builder.append(", userInfo=").append(userInfo);
    if (isOnline != null) builder.append(", isOnline=").append(isOnline);
    if (role != null) builder.append(", role=").append(role);
    if (fakeUserInfo != null) builder.append(", fakeUserInfo=").append(fakeUserInfo);
    return builder.replace(0, 2, "ROnlineInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return ROnlineInfo.ADAPTER.encode(this);
  }

  public static final ROnlineInfo parseFrom(byte[] data) throws IOException {
    ROnlineInfo c = null;
       c = ROnlineInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户信息
   */
  public UserInfo getUserInfo() {
    if(userInfo==null){
        return new UserInfo.Builder().build();
    }
    return userInfo;
  }

  /**
   * 是否在线
   */
  public Boolean getIsOnline() {
    if(isOnline==null){
        return DEFAULT_ISONLINE;
    }
    return isOnline;
  }

  /**
   * 用户角色
   */
  public ERUserRole getRole() {
    if(role==null){
        return new ERUserRole.Builder().build();
    }
    return role;
  }

  /**
   * 蒙面用户信息
   */
  public FakeUserInfo getFakeUserInfo() {
    if(fakeUserInfo==null){
        return new FakeUserInfo.Builder().build();
    }
    return fakeUserInfo;
  }

  /**
   * 用户信息
   */
  public boolean hasUserInfo() {
    return userInfo!=null;
  }

  /**
   * 是否在线
   */
  public boolean hasIsOnline() {
    return isOnline!=null;
  }

  /**
   * 用户角色
   */
  public boolean hasRole() {
    return role!=null;
  }

  /**
   * 蒙面用户信息
   */
  public boolean hasFakeUserInfo() {
    return fakeUserInfo!=null;
  }

  public static final class Builder extends Message.Builder<ROnlineInfo, Builder> {
    private UserInfo userInfo;

    private Boolean isOnline;

    private ERUserRole role;

    private FakeUserInfo fakeUserInfo;

    public Builder() {
    }

    /**
     * 用户信息
     */
    public Builder setUserInfo(UserInfo userInfo) {
      this.userInfo = userInfo;
      return this;
    }

    /**
     * 是否在线
     */
    public Builder setIsOnline(Boolean isOnline) {
      this.isOnline = isOnline;
      return this;
    }

    /**
     * 用户角色
     */
    public Builder setRole(ERUserRole role) {
      this.role = role;
      return this;
    }

    /**
     * 蒙面用户信息
     */
    public Builder setFakeUserInfo(FakeUserInfo fakeUserInfo) {
      this.fakeUserInfo = fakeUserInfo;
      return this;
    }

    @Override
    public ROnlineInfo build() {
      return new ROnlineInfo(userInfo, isOnline, role, fakeUserInfo, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_ROnlineInfo extends ProtoAdapter<ROnlineInfo> {
    public ProtoAdapter_ROnlineInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, ROnlineInfo.class);
    }

    @Override
    public int encodedSize(ROnlineInfo value) {
      return UserInfo.ADAPTER.encodedSizeWithTag(1, value.userInfo)
          + ProtoAdapter.BOOL.encodedSizeWithTag(2, value.isOnline)
          + ERUserRole.ADAPTER.encodedSizeWithTag(3, value.role)
          + FakeUserInfo.ADAPTER.encodedSizeWithTag(4, value.fakeUserInfo)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, ROnlineInfo value) throws IOException {
      UserInfo.ADAPTER.encodeWithTag(writer, 1, value.userInfo);
      ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.isOnline);
      ERUserRole.ADAPTER.encodeWithTag(writer, 3, value.role);
      FakeUserInfo.ADAPTER.encodeWithTag(writer, 4, value.fakeUserInfo);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public ROnlineInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserInfo(UserInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setIsOnline(ProtoAdapter.BOOL.decode(reader)); break;
          case 3: {
            try {
              builder.setRole(ERUserRole.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 4: builder.setFakeUserInfo(FakeUserInfo.ADAPTER.decode(reader)); break;
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
    public ROnlineInfo redact(ROnlineInfo value) {
      Builder builder = value.newBuilder();
      if (builder.userInfo != null) builder.userInfo = UserInfo.ADAPTER.redact(builder.userInfo);
      if (builder.fakeUserInfo != null) builder.fakeUserInfo = FakeUserInfo.ADAPTER.redact(builder.fakeUserInfo);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
