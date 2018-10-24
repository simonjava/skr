// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: RedEnvelope.proto
package com.wali.live.proto.RedEnvelope;

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

/**
 * 抢到红包的用户
 */
public final class Winner extends Message<Winner, Winner.Builder> {
  public static final ProtoAdapter<Winner> ADAPTER = new ProtoAdapter_Winner();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_USERID = 0L;

  public static final String DEFAULT_NICKNAME = "";

  public static final Integer DEFAULT_LEVEL = 0;

  public static final Integer DEFAULT_GAIN = 0;

  public static final Long DEFAULT_TIMESTAMP = 0L;

  public static final Long DEFAULT_AVATAR = 0L;

  /**
   * 用户uuid
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long userId;

  /**
   * 用户昵称
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String nickname;

  /**
   * 用户等级
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer level;

  /**
   * 用户抢红包赚取钻石数
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer gain;

  /**
   * 时间戳
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long timestamp;

  /**
   * 头像
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long avatar;

  public Winner(Long userId, String nickname, Integer level, Integer gain, Long timestamp,
      Long avatar) {
    this(userId, nickname, level, gain, timestamp, avatar, ByteString.EMPTY);
  }

  public Winner(Long userId, String nickname, Integer level, Integer gain, Long timestamp,
      Long avatar, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userId = userId;
    this.nickname = nickname;
    this.level = level;
    this.gain = gain;
    this.timestamp = timestamp;
    this.avatar = avatar;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userId = userId;
    builder.nickname = nickname;
    builder.level = level;
    builder.gain = gain;
    builder.timestamp = timestamp;
    builder.avatar = avatar;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Winner)) return false;
    Winner o = (Winner) other;
    return unknownFields().equals(o.unknownFields())
        && userId.equals(o.userId)
        && Internal.equals(nickname, o.nickname)
        && Internal.equals(level, o.level)
        && Internal.equals(gain, o.gain)
        && Internal.equals(timestamp, o.timestamp)
        && Internal.equals(avatar, o.avatar);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + userId.hashCode();
      result = result * 37 + (nickname != null ? nickname.hashCode() : 0);
      result = result * 37 + (level != null ? level.hashCode() : 0);
      result = result * 37 + (gain != null ? gain.hashCode() : 0);
      result = result * 37 + (timestamp != null ? timestamp.hashCode() : 0);
      result = result * 37 + (avatar != null ? avatar.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", userId=").append(userId);
    if (nickname != null) builder.append(", nickname=").append(nickname);
    if (level != null) builder.append(", level=").append(level);
    if (gain != null) builder.append(", gain=").append(gain);
    if (timestamp != null) builder.append(", timestamp=").append(timestamp);
    if (avatar != null) builder.append(", avatar=").append(avatar);
    return builder.replace(0, 2, "Winner{").append('}').toString();
  }

  public byte[] toByteArray() {
    return Winner.ADAPTER.encode(this);
  }

  public static final Winner parseFrom(byte[] data) throws IOException {
    Winner c = null;
       c = Winner.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户uuid
   */
  public Long getUserId() {
    if(userId==null){
        return DEFAULT_USERID;
    }
    return userId;
  }

  /**
   * 用户昵称
   */
  public String getNickname() {
    if(nickname==null){
        return DEFAULT_NICKNAME;
    }
    return nickname;
  }

  /**
   * 用户等级
   */
  public Integer getLevel() {
    if(level==null){
        return DEFAULT_LEVEL;
    }
    return level;
  }

  /**
   * 用户抢红包赚取钻石数
   */
  public Integer getGain() {
    if(gain==null){
        return DEFAULT_GAIN;
    }
    return gain;
  }

  /**
   * 时间戳
   */
  public Long getTimestamp() {
    if(timestamp==null){
        return DEFAULT_TIMESTAMP;
    }
    return timestamp;
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
   * 用户uuid
   */
  public boolean hasUserId() {
    return userId!=null;
  }

  /**
   * 用户昵称
   */
  public boolean hasNickname() {
    return nickname!=null;
  }

  /**
   * 用户等级
   */
  public boolean hasLevel() {
    return level!=null;
  }

  /**
   * 用户抢红包赚取钻石数
   */
  public boolean hasGain() {
    return gain!=null;
  }

  /**
   * 时间戳
   */
  public boolean hasTimestamp() {
    return timestamp!=null;
  }

  /**
   * 头像
   */
  public boolean hasAvatar() {
    return avatar!=null;
  }

  public static final class Builder extends Message.Builder<Winner, Builder> {
    public Long userId;

    public String nickname;

    public Integer level;

    public Integer gain;

    public Long timestamp;

    public Long avatar;

    public Builder() {
    }

    /**
     * 用户uuid
     */
    public Builder setUserId(Long userId) {
      this.userId = userId;
      return this;
    }

    /**
     * 用户昵称
     */
    public Builder setNickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    /**
     * 用户等级
     */
    public Builder setLevel(Integer level) {
      this.level = level;
      return this;
    }

    /**
     * 用户抢红包赚取钻石数
     */
    public Builder setGain(Integer gain) {
      this.gain = gain;
      return this;
    }

    /**
     * 时间戳
     */
    public Builder setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    /**
     * 头像
     */
    public Builder setAvatar(Long avatar) {
      this.avatar = avatar;
      return this;
    }

    @Override
    public Winner build() {
      return new Winner(userId, nickname, level, gain, timestamp, avatar, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Winner extends ProtoAdapter<Winner> {
    public ProtoAdapter_Winner() {
      super(FieldEncoding.LENGTH_DELIMITED, Winner.class);
    }

    @Override
    public int encodedSize(Winner value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.userId)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.nickname)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.level)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.gain)
          + ProtoAdapter.UINT64.encodedSizeWithTag(5, value.timestamp)
          + ProtoAdapter.UINT64.encodedSizeWithTag(6, value.avatar)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Winner value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.userId);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.nickname);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.level);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.gain);
      ProtoAdapter.UINT64.encodeWithTag(writer, 5, value.timestamp);
      ProtoAdapter.UINT64.encodeWithTag(writer, 6, value.avatar);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Winner decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserId(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setNickname(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setLevel(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setGain(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setTimestamp(ProtoAdapter.UINT64.decode(reader)); break;
          case 6: builder.setAvatar(ProtoAdapter.UINT64.decode(reader)); break;
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
    public Winner redact(Winner value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
