// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveMessage.proto
package com.wali.live.proto.LiveMessage;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 设置管理员消息
 */
public final class AdminMessage extends Message<AdminMessage, AdminMessage.Builder> {
  public static final ProtoAdapter<AdminMessage> ADAPTER = new ProtoAdapter_AdminMessage();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_ADMIN_USER = 0L;

  public static final String DEFAULT_ADMIN_NICK_NAME = "";

  /**
   * 设置／取消管理员的用户
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long admin_user;

  /**
   * 设置／取消管理员的昵称
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String admin_nick_name;

  public AdminMessage(Long admin_user, String admin_nick_name) {
    this(admin_user, admin_nick_name, ByteString.EMPTY);
  }

  public AdminMessage(Long admin_user, String admin_nick_name, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.admin_user = admin_user;
    this.admin_nick_name = admin_nick_name;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.admin_user = admin_user;
    builder.admin_nick_name = admin_nick_name;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof AdminMessage)) return false;
    AdminMessage o = (AdminMessage) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(admin_user, o.admin_user)
        && Internal.equals(admin_nick_name, o.admin_nick_name);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (admin_user != null ? admin_user.hashCode() : 0);
      result = result * 37 + (admin_nick_name != null ? admin_nick_name.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (admin_user != null) builder.append(", admin_user=").append(admin_user);
    if (admin_nick_name != null) builder.append(", admin_nick_name=").append(admin_nick_name);
    return builder.replace(0, 2, "AdminMessage{").append('}').toString();
  }

  public byte[] toByteArray() {
    return AdminMessage.ADAPTER.encode(this);
  }

  public static final AdminMessage parseFrom(byte[] data) throws IOException {
    AdminMessage c = null;
       c = AdminMessage.ADAPTER.decode(data);
    return c;
  }

  /**
   * 设置／取消管理员的用户
   */
  public Long getAdminUser() {
    if(admin_user==null){
        return DEFAULT_ADMIN_USER;
    }
    return admin_user;
  }

  /**
   * 设置／取消管理员的昵称
   */
  public String getAdminNickName() {
    if(admin_nick_name==null){
        return DEFAULT_ADMIN_NICK_NAME;
    }
    return admin_nick_name;
  }

  /**
   * 设置／取消管理员的用户
   */
  public boolean hasAdminUser() {
    return admin_user!=null;
  }

  /**
   * 设置／取消管理员的昵称
   */
  public boolean hasAdminNickName() {
    return admin_nick_name!=null;
  }

  public static final class Builder extends Message.Builder<AdminMessage, Builder> {
    public Long admin_user;

    public String admin_nick_name;

    public Builder() {
    }

    /**
     * 设置／取消管理员的用户
     */
    public Builder setAdminUser(Long admin_user) {
      this.admin_user = admin_user;
      return this;
    }

    /**
     * 设置／取消管理员的昵称
     */
    public Builder setAdminNickName(String admin_nick_name) {
      this.admin_nick_name = admin_nick_name;
      return this;
    }

    @Override
    public AdminMessage build() {
      return new AdminMessage(admin_user, admin_nick_name, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_AdminMessage extends ProtoAdapter<AdminMessage> {
    public ProtoAdapter_AdminMessage() {
      super(FieldEncoding.LENGTH_DELIMITED, AdminMessage.class);
    }

    @Override
    public int encodedSize(AdminMessage value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.admin_user)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.admin_nick_name)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, AdminMessage value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.admin_user);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.admin_nick_name);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public AdminMessage decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setAdminUser(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setAdminNickName(ProtoAdapter.STRING.decode(reader)); break;
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
    public AdminMessage redact(AdminMessage value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
