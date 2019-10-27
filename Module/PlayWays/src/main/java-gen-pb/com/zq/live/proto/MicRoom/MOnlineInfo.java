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
import com.zq.live.proto.Common.UserInfo;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class MOnlineInfo extends Message<MOnlineInfo, MOnlineInfo.Builder> {
  public static final ProtoAdapter<MOnlineInfo> ADAPTER = new ProtoAdapter_MOnlineInfo();

  private static final long serialVersionUID = 0L;

  public static final Boolean DEFAULT_ISONLINE = false;

  public static final EMUserRole DEFAULT_ROLE = EMUserRole.MRUR_UNKNOWN;

  public static final Boolean DEFAULT_ISCURSING = false;

  public static final Boolean DEFAULT_ISNEXTSING = false;

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
      adapter = "com.zq.live.proto.MicRoom.EMUserRole#ADAPTER"
  )
  private final EMUserRole role;

  /**
   * 当前在唱
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  private final Boolean isCurSing;

  /**
   * 下首在唱
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  private final Boolean isNextSing;

  public MOnlineInfo(UserInfo userInfo, Boolean isOnline, EMUserRole role, Boolean isCurSing,
      Boolean isNextSing) {
    this(userInfo, isOnline, role, isCurSing, isNextSing, ByteString.EMPTY);
  }

  public MOnlineInfo(UserInfo userInfo, Boolean isOnline, EMUserRole role, Boolean isCurSing,
      Boolean isNextSing, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userInfo = userInfo;
    this.isOnline = isOnline;
    this.role = role;
    this.isCurSing = isCurSing;
    this.isNextSing = isNextSing;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userInfo = userInfo;
    builder.isOnline = isOnline;
    builder.role = role;
    builder.isCurSing = isCurSing;
    builder.isNextSing = isNextSing;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MOnlineInfo)) return false;
    MOnlineInfo o = (MOnlineInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userInfo, o.userInfo)
        && Internal.equals(isOnline, o.isOnline)
        && Internal.equals(role, o.role)
        && Internal.equals(isCurSing, o.isCurSing)
        && Internal.equals(isNextSing, o.isNextSing);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userInfo != null ? userInfo.hashCode() : 0);
      result = result * 37 + (isOnline != null ? isOnline.hashCode() : 0);
      result = result * 37 + (role != null ? role.hashCode() : 0);
      result = result * 37 + (isCurSing != null ? isCurSing.hashCode() : 0);
      result = result * 37 + (isNextSing != null ? isNextSing.hashCode() : 0);
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
    if (isCurSing != null) builder.append(", isCurSing=").append(isCurSing);
    if (isNextSing != null) builder.append(", isNextSing=").append(isNextSing);
    return builder.replace(0, 2, "MOnlineInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MOnlineInfo.ADAPTER.encode(this);
  }

  public static final MOnlineInfo parseFrom(byte[] data) throws IOException {
    MOnlineInfo c = null;
       c = MOnlineInfo.ADAPTER.decode(data);
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
  public EMUserRole getRole() {
    if(role==null){
        return new EMUserRole.Builder().build();
    }
    return role;
  }

  /**
   * 当前在唱
   */
  public Boolean getIsCurSing() {
    if(isCurSing==null){
        return DEFAULT_ISCURSING;
    }
    return isCurSing;
  }

  /**
   * 下首在唱
   */
  public Boolean getIsNextSing() {
    if(isNextSing==null){
        return DEFAULT_ISNEXTSING;
    }
    return isNextSing;
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
   * 当前在唱
   */
  public boolean hasIsCurSing() {
    return isCurSing!=null;
  }

  /**
   * 下首在唱
   */
  public boolean hasIsNextSing() {
    return isNextSing!=null;
  }

  public static final class Builder extends Message.Builder<MOnlineInfo, Builder> {
    private UserInfo userInfo;

    private Boolean isOnline;

    private EMUserRole role;

    private Boolean isCurSing;

    private Boolean isNextSing;

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
    public Builder setRole(EMUserRole role) {
      this.role = role;
      return this;
    }

    /**
     * 当前在唱
     */
    public Builder setIsCurSing(Boolean isCurSing) {
      this.isCurSing = isCurSing;
      return this;
    }

    /**
     * 下首在唱
     */
    public Builder setIsNextSing(Boolean isNextSing) {
      this.isNextSing = isNextSing;
      return this;
    }

    @Override
    public MOnlineInfo build() {
      return new MOnlineInfo(userInfo, isOnline, role, isCurSing, isNextSing, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MOnlineInfo extends ProtoAdapter<MOnlineInfo> {
    public ProtoAdapter_MOnlineInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, MOnlineInfo.class);
    }

    @Override
    public int encodedSize(MOnlineInfo value) {
      return UserInfo.ADAPTER.encodedSizeWithTag(1, value.userInfo)
          + ProtoAdapter.BOOL.encodedSizeWithTag(2, value.isOnline)
          + EMUserRole.ADAPTER.encodedSizeWithTag(3, value.role)
          + ProtoAdapter.BOOL.encodedSizeWithTag(4, value.isCurSing)
          + ProtoAdapter.BOOL.encodedSizeWithTag(5, value.isNextSing)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MOnlineInfo value) throws IOException {
      UserInfo.ADAPTER.encodeWithTag(writer, 1, value.userInfo);
      ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.isOnline);
      EMUserRole.ADAPTER.encodeWithTag(writer, 3, value.role);
      ProtoAdapter.BOOL.encodeWithTag(writer, 4, value.isCurSing);
      ProtoAdapter.BOOL.encodeWithTag(writer, 5, value.isNextSing);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MOnlineInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserInfo(UserInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setIsOnline(ProtoAdapter.BOOL.decode(reader)); break;
          case 3: {
            try {
              builder.setRole(EMUserRole.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 4: builder.setIsCurSing(ProtoAdapter.BOOL.decode(reader)); break;
          case 5: builder.setIsNextSing(ProtoAdapter.BOOL.decode(reader)); break;
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
    public MOnlineInfo redact(MOnlineInfo value) {
      Builder builder = value.newBuilder();
      if (builder.userInfo != null) builder.userInfo = UserInfo.ADAPTER.redact(builder.userInfo);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}