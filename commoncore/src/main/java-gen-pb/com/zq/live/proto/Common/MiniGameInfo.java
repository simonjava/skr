// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: common.proto
package com.zq.live.proto.Common;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class MiniGameInfo extends Message<MiniGameInfo, MiniGameInfo.Builder> {
  public static final ProtoAdapter<MiniGameInfo> ADAPTER = new ProtoAdapter_MiniGameInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_GAMEID = 0;

  public static final String DEFAULT_GAMENAME = "";

  public static final String DEFAULT_GAMERULE = "";

  public static final EMiniGamePlayType DEFAULT_GAMEPLAYTYPE = EMiniGamePlayType.EMGP_UNKNOWN;

  public static final String DEFAULT_KEYWORD = "";

  public static final String DEFAULT_FIXEDTXT = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer gameID;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String gameName;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String gameRule;

  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.Common.EMiniGamePlayType#ADAPTER"
  )
  private final EMiniGamePlayType gamePlayType;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String keyWord;

  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String fixedTxt;

  @WireField(
      tag = 7,
      adapter = "com.zq.live.proto.Common.MiniGameSongInfo#ADAPTER"
  )
  private final MiniGameSongInfo songInfo;

  public MiniGameInfo(Integer gameID, String gameName, String gameRule,
      EMiniGamePlayType gamePlayType, String keyWord, String fixedTxt, MiniGameSongInfo songInfo) {
    this(gameID, gameName, gameRule, gamePlayType, keyWord, fixedTxt, songInfo, ByteString.EMPTY);
  }

  public MiniGameInfo(Integer gameID, String gameName, String gameRule,
      EMiniGamePlayType gamePlayType, String keyWord, String fixedTxt, MiniGameSongInfo songInfo,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.gameID = gameID;
    this.gameName = gameName;
    this.gameRule = gameRule;
    this.gamePlayType = gamePlayType;
    this.keyWord = keyWord;
    this.fixedTxt = fixedTxt;
    this.songInfo = songInfo;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.gameID = gameID;
    builder.gameName = gameName;
    builder.gameRule = gameRule;
    builder.gamePlayType = gamePlayType;
    builder.keyWord = keyWord;
    builder.fixedTxt = fixedTxt;
    builder.songInfo = songInfo;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MiniGameInfo)) return false;
    MiniGameInfo o = (MiniGameInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(gameID, o.gameID)
        && Internal.equals(gameName, o.gameName)
        && Internal.equals(gameRule, o.gameRule)
        && Internal.equals(gamePlayType, o.gamePlayType)
        && Internal.equals(keyWord, o.keyWord)
        && Internal.equals(fixedTxt, o.fixedTxt)
        && Internal.equals(songInfo, o.songInfo);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (gameID != null ? gameID.hashCode() : 0);
      result = result * 37 + (gameName != null ? gameName.hashCode() : 0);
      result = result * 37 + (gameRule != null ? gameRule.hashCode() : 0);
      result = result * 37 + (gamePlayType != null ? gamePlayType.hashCode() : 0);
      result = result * 37 + (keyWord != null ? keyWord.hashCode() : 0);
      result = result * 37 + (fixedTxt != null ? fixedTxt.hashCode() : 0);
      result = result * 37 + (songInfo != null ? songInfo.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (gameID != null) builder.append(", gameID=").append(gameID);
    if (gameName != null) builder.append(", gameName=").append(gameName);
    if (gameRule != null) builder.append(", gameRule=").append(gameRule);
    if (gamePlayType != null) builder.append(", gamePlayType=").append(gamePlayType);
    if (keyWord != null) builder.append(", keyWord=").append(keyWord);
    if (fixedTxt != null) builder.append(", fixedTxt=").append(fixedTxt);
    if (songInfo != null) builder.append(", songInfo=").append(songInfo);
    return builder.replace(0, 2, "MiniGameInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MiniGameInfo.ADAPTER.encode(this);
  }

  public static final MiniGameInfo parseFrom(byte[] data) throws IOException {
    MiniGameInfo c = null;
       c = MiniGameInfo.ADAPTER.decode(data);
    return c;
  }

  public Integer getGameID() {
    if(gameID==null){
        return DEFAULT_GAMEID;
    }
    return gameID;
  }

  public String getGameName() {
    if(gameName==null){
        return DEFAULT_GAMENAME;
    }
    return gameName;
  }

  public String getGameRule() {
    if(gameRule==null){
        return DEFAULT_GAMERULE;
    }
    return gameRule;
  }

  public EMiniGamePlayType getGamePlayType() {
    if(gamePlayType==null){
        return new EMiniGamePlayType.Builder().build();
    }
    return gamePlayType;
  }

  public String getKeyWord() {
    if(keyWord==null){
        return DEFAULT_KEYWORD;
    }
    return keyWord;
  }

  public String getFixedTxt() {
    if(fixedTxt==null){
        return DEFAULT_FIXEDTXT;
    }
    return fixedTxt;
  }

  public MiniGameSongInfo getSongInfo() {
    if(songInfo==null){
        return new MiniGameSongInfo.Builder().build();
    }
    return songInfo;
  }

  public boolean hasGameID() {
    return gameID!=null;
  }

  public boolean hasGameName() {
    return gameName!=null;
  }

  public boolean hasGameRule() {
    return gameRule!=null;
  }

  public boolean hasGamePlayType() {
    return gamePlayType!=null;
  }

  public boolean hasKeyWord() {
    return keyWord!=null;
  }

  public boolean hasFixedTxt() {
    return fixedTxt!=null;
  }

  public boolean hasSongInfo() {
    return songInfo!=null;
  }

  public static final class Builder extends Message.Builder<MiniGameInfo, Builder> {
    private Integer gameID;

    private String gameName;

    private String gameRule;

    private EMiniGamePlayType gamePlayType;

    private String keyWord;

    private String fixedTxt;

    private MiniGameSongInfo songInfo;

    public Builder() {
    }

    public Builder setGameID(Integer gameID) {
      this.gameID = gameID;
      return this;
    }

    public Builder setGameName(String gameName) {
      this.gameName = gameName;
      return this;
    }

    public Builder setGameRule(String gameRule) {
      this.gameRule = gameRule;
      return this;
    }

    public Builder setGamePlayType(EMiniGamePlayType gamePlayType) {
      this.gamePlayType = gamePlayType;
      return this;
    }

    public Builder setKeyWord(String keyWord) {
      this.keyWord = keyWord;
      return this;
    }

    public Builder setFixedTxt(String fixedTxt) {
      this.fixedTxt = fixedTxt;
      return this;
    }

    public Builder setSongInfo(MiniGameSongInfo songInfo) {
      this.songInfo = songInfo;
      return this;
    }

    @Override
    public MiniGameInfo build() {
      return new MiniGameInfo(gameID, gameName, gameRule, gamePlayType, keyWord, fixedTxt, songInfo, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MiniGameInfo extends ProtoAdapter<MiniGameInfo> {
    public ProtoAdapter_MiniGameInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, MiniGameInfo.class);
    }

    @Override
    public int encodedSize(MiniGameInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.gameID)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.gameName)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.gameRule)
          + EMiniGamePlayType.ADAPTER.encodedSizeWithTag(4, value.gamePlayType)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.keyWord)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.fixedTxt)
          + MiniGameSongInfo.ADAPTER.encodedSizeWithTag(7, value.songInfo)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MiniGameInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.gameID);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.gameName);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.gameRule);
      EMiniGamePlayType.ADAPTER.encodeWithTag(writer, 4, value.gamePlayType);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.keyWord);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.fixedTxt);
      MiniGameSongInfo.ADAPTER.encodeWithTag(writer, 7, value.songInfo);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MiniGameInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setGameID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setGameName(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setGameRule(ProtoAdapter.STRING.decode(reader)); break;
          case 4: {
            try {
              builder.setGamePlayType(EMiniGamePlayType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 5: builder.setKeyWord(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setFixedTxt(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setSongInfo(MiniGameSongInfo.ADAPTER.decode(reader)); break;
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
    public MiniGameInfo redact(MiniGameInfo value) {
      Builder builder = value.newBuilder();
      if (builder.songInfo != null) builder.songInfo = MiniGameSongInfo.ADAPTER.redact(builder.songInfo);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
