// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: RoomInfo.proto
package com.wali.live.proto.RoomInfo;

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

public final class GamePackInfo extends Message<GamePackInfo, GamePackInfo.Builder> {
  public static final ProtoAdapter<GamePackInfo> ADAPTER = new ProtoAdapter_GamePackInfo();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_ZUID = 0L;

  public static final String DEFAULT_ROOMID = "";

  public static final String DEFAULT_CDNDOMAIN = "";

  public static final String DEFAULT_DISPLAYNAME = "";

  public static final Integer DEFAULT_GAMEID = 0;

  public static final String DEFAULT_PACAKGENAME = "";

  public static final String DEFAULT_ICON = "";

  public static final String DEFAULT_GAMEAPKSS1 = "";

  public static final String DEFAULT_GAMEAPK = "";

  public static final String DEFAULT_SHORTNAME = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long zuid;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String roomId;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String cdnDomain;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String displayName;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer gameId;

  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String pacakgeName;

  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String icon;

  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String gameApkSs1;

  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String gameApk;

  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String shortName;

  /**
   * 一级标签
   */
  @WireField(
      tag = 11,
      adapter = "com.wali.live.proto.RoomInfo.GameTagInfoDetail#ADAPTER"
  )
  public final GameTagInfoDetail level1;

  /**
   * 二级标签
   */
  @WireField(
      tag = 12,
      adapter = "com.wali.live.proto.RoomInfo.GameTagInfoDetail#ADAPTER"
  )
  public final GameTagInfoDetail level2;

  public GamePackInfo(Long zuid, String roomId, String cdnDomain, String displayName,
      Integer gameId, String pacakgeName, String icon, String gameApkSs1, String gameApk,
      String shortName, GameTagInfoDetail level1, GameTagInfoDetail level2) {
    this(zuid, roomId, cdnDomain, displayName, gameId, pacakgeName, icon, gameApkSs1, gameApk, shortName, level1, level2, ByteString.EMPTY);
  }

  public GamePackInfo(Long zuid, String roomId, String cdnDomain, String displayName,
      Integer gameId, String pacakgeName, String icon, String gameApkSs1, String gameApk,
      String shortName, GameTagInfoDetail level1, GameTagInfoDetail level2,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.zuid = zuid;
    this.roomId = roomId;
    this.cdnDomain = cdnDomain;
    this.displayName = displayName;
    this.gameId = gameId;
    this.pacakgeName = pacakgeName;
    this.icon = icon;
    this.gameApkSs1 = gameApkSs1;
    this.gameApk = gameApk;
    this.shortName = shortName;
    this.level1 = level1;
    this.level2 = level2;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.zuid = zuid;
    builder.roomId = roomId;
    builder.cdnDomain = cdnDomain;
    builder.displayName = displayName;
    builder.gameId = gameId;
    builder.pacakgeName = pacakgeName;
    builder.icon = icon;
    builder.gameApkSs1 = gameApkSs1;
    builder.gameApk = gameApk;
    builder.shortName = shortName;
    builder.level1 = level1;
    builder.level2 = level2;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GamePackInfo)) return false;
    GamePackInfo o = (GamePackInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(zuid, o.zuid)
        && Internal.equals(roomId, o.roomId)
        && Internal.equals(cdnDomain, o.cdnDomain)
        && Internal.equals(displayName, o.displayName)
        && Internal.equals(gameId, o.gameId)
        && Internal.equals(pacakgeName, o.pacakgeName)
        && Internal.equals(icon, o.icon)
        && Internal.equals(gameApkSs1, o.gameApkSs1)
        && Internal.equals(gameApk, o.gameApk)
        && Internal.equals(shortName, o.shortName)
        && Internal.equals(level1, o.level1)
        && Internal.equals(level2, o.level2);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (zuid != null ? zuid.hashCode() : 0);
      result = result * 37 + (roomId != null ? roomId.hashCode() : 0);
      result = result * 37 + (cdnDomain != null ? cdnDomain.hashCode() : 0);
      result = result * 37 + (displayName != null ? displayName.hashCode() : 0);
      result = result * 37 + (gameId != null ? gameId.hashCode() : 0);
      result = result * 37 + (pacakgeName != null ? pacakgeName.hashCode() : 0);
      result = result * 37 + (icon != null ? icon.hashCode() : 0);
      result = result * 37 + (gameApkSs1 != null ? gameApkSs1.hashCode() : 0);
      result = result * 37 + (gameApk != null ? gameApk.hashCode() : 0);
      result = result * 37 + (shortName != null ? shortName.hashCode() : 0);
      result = result * 37 + (level1 != null ? level1.hashCode() : 0);
      result = result * 37 + (level2 != null ? level2.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (zuid != null) builder.append(", zuid=").append(zuid);
    if (roomId != null) builder.append(", roomId=").append(roomId);
    if (cdnDomain != null) builder.append(", cdnDomain=").append(cdnDomain);
    if (displayName != null) builder.append(", displayName=").append(displayName);
    if (gameId != null) builder.append(", gameId=").append(gameId);
    if (pacakgeName != null) builder.append(", pacakgeName=").append(pacakgeName);
    if (icon != null) builder.append(", icon=").append(icon);
    if (gameApkSs1 != null) builder.append(", gameApkSs1=").append(gameApkSs1);
    if (gameApk != null) builder.append(", gameApk=").append(gameApk);
    if (shortName != null) builder.append(", shortName=").append(shortName);
    if (level1 != null) builder.append(", level1=").append(level1);
    if (level2 != null) builder.append(", level2=").append(level2);
    return builder.replace(0, 2, "GamePackInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GamePackInfo.ADAPTER.encode(this);
  }

  public static final GamePackInfo parseFrom(byte[] data) throws IOException {
    GamePackInfo c = null;
       c = GamePackInfo.ADAPTER.decode(data);
    return c;
  }

  public Long getZuid() {
    if(zuid==null){
        return DEFAULT_ZUID;
    }
    return zuid;
  }

  public String getRoomId() {
    if(roomId==null){
        return DEFAULT_ROOMID;
    }
    return roomId;
  }

  public String getCdnDomain() {
    if(cdnDomain==null){
        return DEFAULT_CDNDOMAIN;
    }
    return cdnDomain;
  }

  public String getDisplayName() {
    if(displayName==null){
        return DEFAULT_DISPLAYNAME;
    }
    return displayName;
  }

  public Integer getGameId() {
    if(gameId==null){
        return DEFAULT_GAMEID;
    }
    return gameId;
  }

  public String getPacakgeName() {
    if(pacakgeName==null){
        return DEFAULT_PACAKGENAME;
    }
    return pacakgeName;
  }

  public String getIcon() {
    if(icon==null){
        return DEFAULT_ICON;
    }
    return icon;
  }

  public String getGameApkSs1() {
    if(gameApkSs1==null){
        return DEFAULT_GAMEAPKSS1;
    }
    return gameApkSs1;
  }

  public String getGameApk() {
    if(gameApk==null){
        return DEFAULT_GAMEAPK;
    }
    return gameApk;
  }

  public String getShortName() {
    if(shortName==null){
        return DEFAULT_SHORTNAME;
    }
    return shortName;
  }

  /**
   * 一级标签
   */
  public GameTagInfoDetail getLevel1() {
    if(level1==null){
        return new GameTagInfoDetail.Builder().build();
    }
    return level1;
  }

  /**
   * 二级标签
   */
  public GameTagInfoDetail getLevel2() {
    if(level2==null){
        return new GameTagInfoDetail.Builder().build();
    }
    return level2;
  }

  public boolean hasZuid() {
    return zuid!=null;
  }

  public boolean hasRoomId() {
    return roomId!=null;
  }

  public boolean hasCdnDomain() {
    return cdnDomain!=null;
  }

  public boolean hasDisplayName() {
    return displayName!=null;
  }

  public boolean hasGameId() {
    return gameId!=null;
  }

  public boolean hasPacakgeName() {
    return pacakgeName!=null;
  }

  public boolean hasIcon() {
    return icon!=null;
  }

  public boolean hasGameApkSs1() {
    return gameApkSs1!=null;
  }

  public boolean hasGameApk() {
    return gameApk!=null;
  }

  public boolean hasShortName() {
    return shortName!=null;
  }

  /**
   * 一级标签
   */
  public boolean hasLevel1() {
    return level1!=null;
  }

  /**
   * 二级标签
   */
  public boolean hasLevel2() {
    return level2!=null;
  }

  public static final class Builder extends Message.Builder<GamePackInfo, Builder> {
    public Long zuid;

    public String roomId;

    public String cdnDomain;

    public String displayName;

    public Integer gameId;

    public String pacakgeName;

    public String icon;

    public String gameApkSs1;

    public String gameApk;

    public String shortName;

    public GameTagInfoDetail level1;

    public GameTagInfoDetail level2;

    public Builder() {
    }

    public Builder setZuid(Long zuid) {
      this.zuid = zuid;
      return this;
    }

    public Builder setRoomId(String roomId) {
      this.roomId = roomId;
      return this;
    }

    public Builder setCdnDomain(String cdnDomain) {
      this.cdnDomain = cdnDomain;
      return this;
    }

    public Builder setDisplayName(String displayName) {
      this.displayName = displayName;
      return this;
    }

    public Builder setGameId(Integer gameId) {
      this.gameId = gameId;
      return this;
    }

    public Builder setPacakgeName(String pacakgeName) {
      this.pacakgeName = pacakgeName;
      return this;
    }

    public Builder setIcon(String icon) {
      this.icon = icon;
      return this;
    }

    public Builder setGameApkSs1(String gameApkSs1) {
      this.gameApkSs1 = gameApkSs1;
      return this;
    }

    public Builder setGameApk(String gameApk) {
      this.gameApk = gameApk;
      return this;
    }

    public Builder setShortName(String shortName) {
      this.shortName = shortName;
      return this;
    }

    /**
     * 一级标签
     */
    public Builder setLevel1(GameTagInfoDetail level1) {
      this.level1 = level1;
      return this;
    }

    /**
     * 二级标签
     */
    public Builder setLevel2(GameTagInfoDetail level2) {
      this.level2 = level2;
      return this;
    }

    @Override
    public GamePackInfo build() {
      return new GamePackInfo(zuid, roomId, cdnDomain, displayName, gameId, pacakgeName, icon, gameApkSs1, gameApk, shortName, level1, level2, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GamePackInfo extends ProtoAdapter<GamePackInfo> {
    public ProtoAdapter_GamePackInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, GamePackInfo.class);
    }

    @Override
    public int encodedSize(GamePackInfo value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.zuid)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.roomId)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.cdnDomain)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.displayName)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.gameId)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.pacakgeName)
          + ProtoAdapter.STRING.encodedSizeWithTag(7, value.icon)
          + ProtoAdapter.STRING.encodedSizeWithTag(8, value.gameApkSs1)
          + ProtoAdapter.STRING.encodedSizeWithTag(9, value.gameApk)
          + ProtoAdapter.STRING.encodedSizeWithTag(10, value.shortName)
          + GameTagInfoDetail.ADAPTER.encodedSizeWithTag(11, value.level1)
          + GameTagInfoDetail.ADAPTER.encodedSizeWithTag(12, value.level2)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GamePackInfo value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.zuid);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.roomId);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.cdnDomain);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.displayName);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.gameId);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.pacakgeName);
      ProtoAdapter.STRING.encodeWithTag(writer, 7, value.icon);
      ProtoAdapter.STRING.encodeWithTag(writer, 8, value.gameApkSs1);
      ProtoAdapter.STRING.encodeWithTag(writer, 9, value.gameApk);
      ProtoAdapter.STRING.encodeWithTag(writer, 10, value.shortName);
      GameTagInfoDetail.ADAPTER.encodeWithTag(writer, 11, value.level1);
      GameTagInfoDetail.ADAPTER.encodeWithTag(writer, 12, value.level2);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GamePackInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setZuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setCdnDomain(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setDisplayName(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.setGameId(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.setPacakgeName(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setIcon(ProtoAdapter.STRING.decode(reader)); break;
          case 8: builder.setGameApkSs1(ProtoAdapter.STRING.decode(reader)); break;
          case 9: builder.setGameApk(ProtoAdapter.STRING.decode(reader)); break;
          case 10: builder.setShortName(ProtoAdapter.STRING.decode(reader)); break;
          case 11: builder.setLevel1(GameTagInfoDetail.ADAPTER.decode(reader)); break;
          case 12: builder.setLevel2(GameTagInfoDetail.ADAPTER.decode(reader)); break;
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
    public GamePackInfo redact(GamePackInfo value) {
      Builder builder = value.newBuilder();
      if (builder.level1 != null) builder.level1 = GameTagInfoDetail.ADAPTER.redact(builder.level1);
      if (builder.level2 != null) builder.level2 = GameTagInfoDetail.ADAPTER.redact(builder.level2);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
