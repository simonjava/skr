// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LivePk.proto
package com.wali.live.proto.LivePK;

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

public final class PKInviteMsg extends Message<PKInviteMsg, PKInviteMsg.Builder> {
  public static final ProtoAdapter<PKInviteMsg> ADAPTER = new ProtoAdapter_PKInviteMsg();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_UUID = 0L;

  public static final String DEFAULT_LIVEID = "";

  public static final Long DEFAULT_PK_UUID = 0L;

  public static final Integer DEFAULT_TYPE = 0;

  public static final String DEFAULT_NICKNAME = "";

  public static final Long DEFAULT_ADMIN_UUID = 0L;

  public static final String DEFAULT_ADMIN_NICKNAME = "";

  public static final String DEFAULT_PK_NICKNAME = "";

  /**
   * 发起的主播id（场控发起传场控id）
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long uuid;

  /**
   * 发起的主播房间号（场控发起传进入的房间号）
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String liveid;

  /**
   * 被pk用户id
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long pk_uuid;

  /**
   * 0:主播邀请 1：场控邀请
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer type;

  /**
   * 发起主播的昵称
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String nickname;

  /**
   * pk设置
   */
  @WireField(
      tag = 6,
      adapter = "com.wali.live.proto.LivePK.PKSetting#ADAPTER"
  )
  public final PKSetting setting;

  /**
   * 场控id
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long admin_uuid;

  /**
   * 场控的昵称
   */
  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String admin_nickname;

  /**
   * 被pk主播的昵称
   */
  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String pk_nickname;

  public PKInviteMsg(Long uuid, String liveid, Long pk_uuid, Integer type, String nickname,
      PKSetting setting, Long admin_uuid, String admin_nickname, String pk_nickname) {
    this(uuid, liveid, pk_uuid, type, nickname, setting, admin_uuid, admin_nickname, pk_nickname, ByteString.EMPTY);
  }

  public PKInviteMsg(Long uuid, String liveid, Long pk_uuid, Integer type, String nickname,
      PKSetting setting, Long admin_uuid, String admin_nickname, String pk_nickname,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uuid = uuid;
    this.liveid = liveid;
    this.pk_uuid = pk_uuid;
    this.type = type;
    this.nickname = nickname;
    this.setting = setting;
    this.admin_uuid = admin_uuid;
    this.admin_nickname = admin_nickname;
    this.pk_nickname = pk_nickname;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uuid = uuid;
    builder.liveid = liveid;
    builder.pk_uuid = pk_uuid;
    builder.type = type;
    builder.nickname = nickname;
    builder.setting = setting;
    builder.admin_uuid = admin_uuid;
    builder.admin_nickname = admin_nickname;
    builder.pk_nickname = pk_nickname;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PKInviteMsg)) return false;
    PKInviteMsg o = (PKInviteMsg) other;
    return unknownFields().equals(o.unknownFields())
        && uuid.equals(o.uuid)
        && Internal.equals(liveid, o.liveid)
        && pk_uuid.equals(o.pk_uuid)
        && Internal.equals(type, o.type)
        && Internal.equals(nickname, o.nickname)
        && Internal.equals(setting, o.setting)
        && Internal.equals(admin_uuid, o.admin_uuid)
        && Internal.equals(admin_nickname, o.admin_nickname)
        && Internal.equals(pk_nickname, o.pk_nickname);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + uuid.hashCode();
      result = result * 37 + (liveid != null ? liveid.hashCode() : 0);
      result = result * 37 + pk_uuid.hashCode();
      result = result * 37 + (type != null ? type.hashCode() : 0);
      result = result * 37 + (nickname != null ? nickname.hashCode() : 0);
      result = result * 37 + (setting != null ? setting.hashCode() : 0);
      result = result * 37 + (admin_uuid != null ? admin_uuid.hashCode() : 0);
      result = result * 37 + (admin_nickname != null ? admin_nickname.hashCode() : 0);
      result = result * 37 + (pk_nickname != null ? pk_nickname.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", uuid=").append(uuid);
    if (liveid != null) builder.append(", liveid=").append(liveid);
    builder.append(", pk_uuid=").append(pk_uuid);
    if (type != null) builder.append(", type=").append(type);
    if (nickname != null) builder.append(", nickname=").append(nickname);
    if (setting != null) builder.append(", setting=").append(setting);
    if (admin_uuid != null) builder.append(", admin_uuid=").append(admin_uuid);
    if (admin_nickname != null) builder.append(", admin_nickname=").append(admin_nickname);
    if (pk_nickname != null) builder.append(", pk_nickname=").append(pk_nickname);
    return builder.replace(0, 2, "PKInviteMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PKInviteMsg.ADAPTER.encode(this);
  }

  public static final PKInviteMsg parseFrom(byte[] data) throws IOException {
    PKInviteMsg c = null;
       c = PKInviteMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 发起的主播id（场控发起传场控id）
   */
  public Long getUuid() {
    if(uuid==null){
        return DEFAULT_UUID;
    }
    return uuid;
  }

  /**
   * 发起的主播房间号（场控发起传进入的房间号）
   */
  public String getLiveid() {
    if(liveid==null){
        return DEFAULT_LIVEID;
    }
    return liveid;
  }

  /**
   * 被pk用户id
   */
  public Long getPkUuid() {
    if(pk_uuid==null){
        return DEFAULT_PK_UUID;
    }
    return pk_uuid;
  }

  /**
   * 0:主播邀请 1：场控邀请
   */
  public Integer getType() {
    if(type==null){
        return DEFAULT_TYPE;
    }
    return type;
  }

  /**
   * 发起主播的昵称
   */
  public String getNickname() {
    if(nickname==null){
        return DEFAULT_NICKNAME;
    }
    return nickname;
  }

  /**
   * pk设置
   */
  public PKSetting getSetting() {
    if(setting==null){
        return new PKSetting.Builder().build();
    }
    return setting;
  }

  /**
   * 场控id
   */
  public Long getAdminUuid() {
    if(admin_uuid==null){
        return DEFAULT_ADMIN_UUID;
    }
    return admin_uuid;
  }

  /**
   * 场控的昵称
   */
  public String getAdminNickname() {
    if(admin_nickname==null){
        return DEFAULT_ADMIN_NICKNAME;
    }
    return admin_nickname;
  }

  /**
   * 被pk主播的昵称
   */
  public String getPkNickname() {
    if(pk_nickname==null){
        return DEFAULT_PK_NICKNAME;
    }
    return pk_nickname;
  }

  /**
   * 发起的主播id（场控发起传场控id）
   */
  public boolean hasUuid() {
    return uuid!=null;
  }

  /**
   * 发起的主播房间号（场控发起传进入的房间号）
   */
  public boolean hasLiveid() {
    return liveid!=null;
  }

  /**
   * 被pk用户id
   */
  public boolean hasPkUuid() {
    return pk_uuid!=null;
  }

  /**
   * 0:主播邀请 1：场控邀请
   */
  public boolean hasType() {
    return type!=null;
  }

  /**
   * 发起主播的昵称
   */
  public boolean hasNickname() {
    return nickname!=null;
  }

  /**
   * pk设置
   */
  public boolean hasSetting() {
    return setting!=null;
  }

  /**
   * 场控id
   */
  public boolean hasAdminUuid() {
    return admin_uuid!=null;
  }

  /**
   * 场控的昵称
   */
  public boolean hasAdminNickname() {
    return admin_nickname!=null;
  }

  /**
   * 被pk主播的昵称
   */
  public boolean hasPkNickname() {
    return pk_nickname!=null;
  }

  public static final class Builder extends Message.Builder<PKInviteMsg, Builder> {
    public Long uuid;

    public String liveid;

    public Long pk_uuid;

    public Integer type;

    public String nickname;

    public PKSetting setting;

    public Long admin_uuid;

    public String admin_nickname;

    public String pk_nickname;

    public Builder() {
    }

    /**
     * 发起的主播id（场控发起传场控id）
     */
    public Builder setUuid(Long uuid) {
      this.uuid = uuid;
      return this;
    }

    /**
     * 发起的主播房间号（场控发起传进入的房间号）
     */
    public Builder setLiveid(String liveid) {
      this.liveid = liveid;
      return this;
    }

    /**
     * 被pk用户id
     */
    public Builder setPkUuid(Long pk_uuid) {
      this.pk_uuid = pk_uuid;
      return this;
    }

    /**
     * 0:主播邀请 1：场控邀请
     */
    public Builder setType(Integer type) {
      this.type = type;
      return this;
    }

    /**
     * 发起主播的昵称
     */
    public Builder setNickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    /**
     * pk设置
     */
    public Builder setSetting(PKSetting setting) {
      this.setting = setting;
      return this;
    }

    /**
     * 场控id
     */
    public Builder setAdminUuid(Long admin_uuid) {
      this.admin_uuid = admin_uuid;
      return this;
    }

    /**
     * 场控的昵称
     */
    public Builder setAdminNickname(String admin_nickname) {
      this.admin_nickname = admin_nickname;
      return this;
    }

    /**
     * 被pk主播的昵称
     */
    public Builder setPkNickname(String pk_nickname) {
      this.pk_nickname = pk_nickname;
      return this;
    }

    @Override
    public PKInviteMsg build() {
      return new PKInviteMsg(uuid, liveid, pk_uuid, type, nickname, setting, admin_uuid, admin_nickname, pk_nickname, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PKInviteMsg extends ProtoAdapter<PKInviteMsg> {
    public ProtoAdapter_PKInviteMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PKInviteMsg.class);
    }

    @Override
    public int encodedSize(PKInviteMsg value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.uuid)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.liveid)
          + ProtoAdapter.UINT64.encodedSizeWithTag(3, value.pk_uuid)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.type)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.nickname)
          + PKSetting.ADAPTER.encodedSizeWithTag(6, value.setting)
          + ProtoAdapter.UINT64.encodedSizeWithTag(7, value.admin_uuid)
          + ProtoAdapter.STRING.encodedSizeWithTag(8, value.admin_nickname)
          + ProtoAdapter.STRING.encodedSizeWithTag(9, value.pk_nickname)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PKInviteMsg value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.uuid);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.liveid);
      ProtoAdapter.UINT64.encodeWithTag(writer, 3, value.pk_uuid);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.type);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.nickname);
      PKSetting.ADAPTER.encodeWithTag(writer, 6, value.setting);
      ProtoAdapter.UINT64.encodeWithTag(writer, 7, value.admin_uuid);
      ProtoAdapter.STRING.encodeWithTag(writer, 8, value.admin_nickname);
      ProtoAdapter.STRING.encodeWithTag(writer, 9, value.pk_nickname);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PKInviteMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setLiveid(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setPkUuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 4: builder.setType(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setNickname(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setSetting(PKSetting.ADAPTER.decode(reader)); break;
          case 7: builder.setAdminUuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 8: builder.setAdminNickname(ProtoAdapter.STRING.decode(reader)); break;
          case 9: builder.setPkNickname(ProtoAdapter.STRING.decode(reader)); break;
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
    public PKInviteMsg redact(PKInviteMsg value) {
      Builder builder = value.newBuilder();
      if (builder.setting != null) builder.setting = PKSetting.ADAPTER.redact(builder.setting);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
