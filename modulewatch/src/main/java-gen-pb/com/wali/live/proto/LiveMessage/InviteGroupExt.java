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
 * 邀请入群消息协议
 */
public final class InviteGroupExt extends Message<InviteGroupExt, InviteGroupExt.Builder> {
  public static final ProtoAdapter<InviteGroupExt> ADAPTER = new ProtoAdapter_InviteGroupExt();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_INVITER = 0L;

  public static final String DEFAULT_INVITER_NICKNAME = "";

  public static final Long DEFAULT_FANS_GROUP_ID = 0L;

  public static final String DEFAULT_FANS_GROUP_ICON = "";

  public static final String DEFAULT_FANS_GROUP_NAME = "";

  public static final Long DEFAULT_TO_FRIEND = 0L;

  public static final String DEFAULT_TO_FRIEND_NICKNAME = "";

  /**
   * 邀请人id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long inviter;

  /**
   * 邀请人昵称
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String inviter_nickname;

  /**
   * 邀请的群id
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long fans_group_id;

  /**
   * 邀请的群头像
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String fans_group_icon;

  /**
   * 被邀请的群名称
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String fans_group_name;

  /**
   * 被邀请人id
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long to_friend;

  /**
   * 被邀请人昵称
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String to_friend_nickname;

  public InviteGroupExt(Long inviter, String inviter_nickname, Long fans_group_id,
      String fans_group_icon, String fans_group_name, Long to_friend, String to_friend_nickname) {
    this(inviter, inviter_nickname, fans_group_id, fans_group_icon, fans_group_name, to_friend, to_friend_nickname, ByteString.EMPTY);
  }

  public InviteGroupExt(Long inviter, String inviter_nickname, Long fans_group_id,
      String fans_group_icon, String fans_group_name, Long to_friend, String to_friend_nickname,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.inviter = inviter;
    this.inviter_nickname = inviter_nickname;
    this.fans_group_id = fans_group_id;
    this.fans_group_icon = fans_group_icon;
    this.fans_group_name = fans_group_name;
    this.to_friend = to_friend;
    this.to_friend_nickname = to_friend_nickname;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.inviter = inviter;
    builder.inviter_nickname = inviter_nickname;
    builder.fans_group_id = fans_group_id;
    builder.fans_group_icon = fans_group_icon;
    builder.fans_group_name = fans_group_name;
    builder.to_friend = to_friend;
    builder.to_friend_nickname = to_friend_nickname;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof InviteGroupExt)) return false;
    InviteGroupExt o = (InviteGroupExt) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(inviter, o.inviter)
        && Internal.equals(inviter_nickname, o.inviter_nickname)
        && Internal.equals(fans_group_id, o.fans_group_id)
        && Internal.equals(fans_group_icon, o.fans_group_icon)
        && Internal.equals(fans_group_name, o.fans_group_name)
        && Internal.equals(to_friend, o.to_friend)
        && Internal.equals(to_friend_nickname, o.to_friend_nickname);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (inviter != null ? inviter.hashCode() : 0);
      result = result * 37 + (inviter_nickname != null ? inviter_nickname.hashCode() : 0);
      result = result * 37 + (fans_group_id != null ? fans_group_id.hashCode() : 0);
      result = result * 37 + (fans_group_icon != null ? fans_group_icon.hashCode() : 0);
      result = result * 37 + (fans_group_name != null ? fans_group_name.hashCode() : 0);
      result = result * 37 + (to_friend != null ? to_friend.hashCode() : 0);
      result = result * 37 + (to_friend_nickname != null ? to_friend_nickname.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (inviter != null) builder.append(", inviter=").append(inviter);
    if (inviter_nickname != null) builder.append(", inviter_nickname=").append(inviter_nickname);
    if (fans_group_id != null) builder.append(", fans_group_id=").append(fans_group_id);
    if (fans_group_icon != null) builder.append(", fans_group_icon=").append(fans_group_icon);
    if (fans_group_name != null) builder.append(", fans_group_name=").append(fans_group_name);
    if (to_friend != null) builder.append(", to_friend=").append(to_friend);
    if (to_friend_nickname != null) builder.append(", to_friend_nickname=").append(to_friend_nickname);
    return builder.replace(0, 2, "InviteGroupExt{").append('}').toString();
  }

  public byte[] toByteArray() {
    return InviteGroupExt.ADAPTER.encode(this);
  }

  public static final InviteGroupExt parseFrom(byte[] data) throws IOException {
    InviteGroupExt c = null;
       c = InviteGroupExt.ADAPTER.decode(data);
    return c;
  }

  /**
   * 邀请人id
   */
  public Long getInviter() {
    if(inviter==null){
        return DEFAULT_INVITER;
    }
    return inviter;
  }

  /**
   * 邀请人昵称
   */
  public String getInviterNickname() {
    if(inviter_nickname==null){
        return DEFAULT_INVITER_NICKNAME;
    }
    return inviter_nickname;
  }

  /**
   * 邀请的群id
   */
  public Long getFansGroupId() {
    if(fans_group_id==null){
        return DEFAULT_FANS_GROUP_ID;
    }
    return fans_group_id;
  }

  /**
   * 邀请的群头像
   */
  public String getFansGroupIcon() {
    if(fans_group_icon==null){
        return DEFAULT_FANS_GROUP_ICON;
    }
    return fans_group_icon;
  }

  /**
   * 被邀请的群名称
   */
  public String getFansGroupName() {
    if(fans_group_name==null){
        return DEFAULT_FANS_GROUP_NAME;
    }
    return fans_group_name;
  }

  /**
   * 被邀请人id
   */
  public Long getToFriend() {
    if(to_friend==null){
        return DEFAULT_TO_FRIEND;
    }
    return to_friend;
  }

  /**
   * 被邀请人昵称
   */
  public String getToFriendNickname() {
    if(to_friend_nickname==null){
        return DEFAULT_TO_FRIEND_NICKNAME;
    }
    return to_friend_nickname;
  }

  /**
   * 邀请人id
   */
  public boolean hasInviter() {
    return inviter!=null;
  }

  /**
   * 邀请人昵称
   */
  public boolean hasInviterNickname() {
    return inviter_nickname!=null;
  }

  /**
   * 邀请的群id
   */
  public boolean hasFansGroupId() {
    return fans_group_id!=null;
  }

  /**
   * 邀请的群头像
   */
  public boolean hasFansGroupIcon() {
    return fans_group_icon!=null;
  }

  /**
   * 被邀请的群名称
   */
  public boolean hasFansGroupName() {
    return fans_group_name!=null;
  }

  /**
   * 被邀请人id
   */
  public boolean hasToFriend() {
    return to_friend!=null;
  }

  /**
   * 被邀请人昵称
   */
  public boolean hasToFriendNickname() {
    return to_friend_nickname!=null;
  }

  public static final class Builder extends Message.Builder<InviteGroupExt, Builder> {
    public Long inviter;

    public String inviter_nickname;

    public Long fans_group_id;

    public String fans_group_icon;

    public String fans_group_name;

    public Long to_friend;

    public String to_friend_nickname;

    public Builder() {
    }

    /**
     * 邀请人id
     */
    public Builder setInviter(Long inviter) {
      this.inviter = inviter;
      return this;
    }

    /**
     * 邀请人昵称
     */
    public Builder setInviterNickname(String inviter_nickname) {
      this.inviter_nickname = inviter_nickname;
      return this;
    }

    /**
     * 邀请的群id
     */
    public Builder setFansGroupId(Long fans_group_id) {
      this.fans_group_id = fans_group_id;
      return this;
    }

    /**
     * 邀请的群头像
     */
    public Builder setFansGroupIcon(String fans_group_icon) {
      this.fans_group_icon = fans_group_icon;
      return this;
    }

    /**
     * 被邀请的群名称
     */
    public Builder setFansGroupName(String fans_group_name) {
      this.fans_group_name = fans_group_name;
      return this;
    }

    /**
     * 被邀请人id
     */
    public Builder setToFriend(Long to_friend) {
      this.to_friend = to_friend;
      return this;
    }

    /**
     * 被邀请人昵称
     */
    public Builder setToFriendNickname(String to_friend_nickname) {
      this.to_friend_nickname = to_friend_nickname;
      return this;
    }

    @Override
    public InviteGroupExt build() {
      return new InviteGroupExt(inviter, inviter_nickname, fans_group_id, fans_group_icon, fans_group_name, to_friend, to_friend_nickname, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_InviteGroupExt extends ProtoAdapter<InviteGroupExt> {
    public ProtoAdapter_InviteGroupExt() {
      super(FieldEncoding.LENGTH_DELIMITED, InviteGroupExt.class);
    }

    @Override
    public int encodedSize(InviteGroupExt value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.inviter)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.inviter_nickname)
          + ProtoAdapter.UINT64.encodedSizeWithTag(3, value.fans_group_id)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.fans_group_icon)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.fans_group_name)
          + ProtoAdapter.UINT64.encodedSizeWithTag(6, value.to_friend)
          + ProtoAdapter.STRING.encodedSizeWithTag(7, value.to_friend_nickname)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, InviteGroupExt value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.inviter);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.inviter_nickname);
      ProtoAdapter.UINT64.encodeWithTag(writer, 3, value.fans_group_id);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.fans_group_icon);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.fans_group_name);
      ProtoAdapter.UINT64.encodeWithTag(writer, 6, value.to_friend);
      ProtoAdapter.STRING.encodeWithTag(writer, 7, value.to_friend_nickname);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public InviteGroupExt decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setInviter(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setInviterNickname(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setFansGroupId(ProtoAdapter.UINT64.decode(reader)); break;
          case 4: builder.setFansGroupIcon(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.setFansGroupName(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setToFriend(ProtoAdapter.UINT64.decode(reader)); break;
          case 7: builder.setToFriendNickname(ProtoAdapter.STRING.decode(reader)); break;
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
    public InviteGroupExt redact(InviteGroupExt value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
