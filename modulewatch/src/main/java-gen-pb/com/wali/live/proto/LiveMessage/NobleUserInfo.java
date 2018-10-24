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
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class NobleUserInfo extends Message<NobleUserInfo, NobleUserInfo.Builder> {
  public static final ProtoAdapter<NobleUserInfo> ADAPTER = new ProtoAdapter_NobleUserInfo();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_AVATAR = 0L;

  public static final String DEFAULT_NICKNAME = "";

  public static final Integer DEFAULT_GENDER = 0;

  public static final Integer DEFAULT_LEVEL = 0;

  public static final Integer DEFAULT_NOBLELEVEL = 0;

  public static final Long DEFAULT_UUID = 0L;

  /**
   * 头像
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long avatar;

  /**
   * 昵称
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String nickname;

  /**
   * 性别
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer gender;

  /**
   * 等级
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer level;

  /**
   * 贵族id 100表示子爵 200表示伯爵 300表示侯爵 400表示公爵 500表示国王
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer nobleLevel;

  /**
   * 用户id
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long uuid;

  public NobleUserInfo(Long avatar, String nickname, Integer gender, Integer level,
      Integer nobleLevel, Long uuid) {
    this(avatar, nickname, gender, level, nobleLevel, uuid, ByteString.EMPTY);
  }

  public NobleUserInfo(Long avatar, String nickname, Integer gender, Integer level,
      Integer nobleLevel, Long uuid, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.avatar = avatar;
    this.nickname = nickname;
    this.gender = gender;
    this.level = level;
    this.nobleLevel = nobleLevel;
    this.uuid = uuid;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.avatar = avatar;
    builder.nickname = nickname;
    builder.gender = gender;
    builder.level = level;
    builder.nobleLevel = nobleLevel;
    builder.uuid = uuid;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof NobleUserInfo)) return false;
    NobleUserInfo o = (NobleUserInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(avatar, o.avatar)
        && Internal.equals(nickname, o.nickname)
        && Internal.equals(gender, o.gender)
        && Internal.equals(level, o.level)
        && Internal.equals(nobleLevel, o.nobleLevel)
        && Internal.equals(uuid, o.uuid);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (avatar != null ? avatar.hashCode() : 0);
      result = result * 37 + (nickname != null ? nickname.hashCode() : 0);
      result = result * 37 + (gender != null ? gender.hashCode() : 0);
      result = result * 37 + (level != null ? level.hashCode() : 0);
      result = result * 37 + (nobleLevel != null ? nobleLevel.hashCode() : 0);
      result = result * 37 + (uuid != null ? uuid.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (avatar != null) builder.append(", avatar=").append(avatar);
    if (nickname != null) builder.append(", nickname=").append(nickname);
    if (gender != null) builder.append(", gender=").append(gender);
    if (level != null) builder.append(", level=").append(level);
    if (nobleLevel != null) builder.append(", nobleLevel=").append(nobleLevel);
    if (uuid != null) builder.append(", uuid=").append(uuid);
    return builder.replace(0, 2, "NobleUserInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return NobleUserInfo.ADAPTER.encode(this);
  }

  public static final NobleUserInfo parseFrom(byte[] data) throws IOException {
    NobleUserInfo c = null;
       c = NobleUserInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 头像
   */
  public Long getAvatar() {
    if(avatar==null){
        return DEFAULT_AVATAR;
    }
    return avatar;
  }

  /**
   * 昵称
   */
  public String getNickname() {
    if(nickname==null){
        return DEFAULT_NICKNAME;
    }
    return nickname;
  }

  /**
   * 性别
   */
  public Integer getGender() {
    if(gender==null){
        return DEFAULT_GENDER;
    }
    return gender;
  }

  /**
   * 等级
   */
  public Integer getLevel() {
    if(level==null){
        return DEFAULT_LEVEL;
    }
    return level;
  }

  /**
   * 贵族id 100表示子爵 200表示伯爵 300表示侯爵 400表示公爵 500表示国王
   */
  public Integer getNobleLevel() {
    if(nobleLevel==null){
        return DEFAULT_NOBLELEVEL;
    }
    return nobleLevel;
  }

  /**
   * 用户id
   */
  public Long getUuid() {
    if(uuid==null){
        return DEFAULT_UUID;
    }
    return uuid;
  }

  /**
   * 头像
   */
  public boolean hasAvatar() {
    return avatar!=null;
  }

  /**
   * 昵称
   */
  public boolean hasNickname() {
    return nickname!=null;
  }

  /**
   * 性别
   */
  public boolean hasGender() {
    return gender!=null;
  }

  /**
   * 等级
   */
  public boolean hasLevel() {
    return level!=null;
  }

  /**
   * 贵族id 100表示子爵 200表示伯爵 300表示侯爵 400表示公爵 500表示国王
   */
  public boolean hasNobleLevel() {
    return nobleLevel!=null;
  }

  /**
   * 用户id
   */
  public boolean hasUuid() {
    return uuid!=null;
  }

  public static final class Builder extends Message.Builder<NobleUserInfo, Builder> {
    public Long avatar;

    public String nickname;

    public Integer gender;

    public Integer level;

    public Integer nobleLevel;

    public Long uuid;

    public Builder() {
    }

    /**
     * 头像
     */
    public Builder setAvatar(Long avatar) {
      this.avatar = avatar;
      return this;
    }

    /**
     * 昵称
     */
    public Builder setNickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    /**
     * 性别
     */
    public Builder setGender(Integer gender) {
      this.gender = gender;
      return this;
    }

    /**
     * 等级
     */
    public Builder setLevel(Integer level) {
      this.level = level;
      return this;
    }

    /**
     * 贵族id 100表示子爵 200表示伯爵 300表示侯爵 400表示公爵 500表示国王
     */
    public Builder setNobleLevel(Integer nobleLevel) {
      this.nobleLevel = nobleLevel;
      return this;
    }

    /**
     * 用户id
     */
    public Builder setUuid(Long uuid) {
      this.uuid = uuid;
      return this;
    }

    @Override
    public NobleUserInfo build() {
      return new NobleUserInfo(avatar, nickname, gender, level, nobleLevel, uuid, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_NobleUserInfo extends ProtoAdapter<NobleUserInfo> {
    public ProtoAdapter_NobleUserInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, NobleUserInfo.class);
    }

    @Override
    public int encodedSize(NobleUserInfo value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.avatar)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.nickname)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.gender)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.level)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.nobleLevel)
          + ProtoAdapter.UINT64.encodedSizeWithTag(6, value.uuid)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, NobleUserInfo value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.avatar);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.nickname);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.gender);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.level);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.nobleLevel);
      ProtoAdapter.UINT64.encodeWithTag(writer, 6, value.uuid);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public NobleUserInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setAvatar(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setNickname(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setGender(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setLevel(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setNobleLevel(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.setUuid(ProtoAdapter.UINT64.decode(reader)); break;
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
    public NobleUserInfo redact(NobleUserInfo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
