// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: grab_room.proto
package com.zq.live.proto.GrabRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.MusicInfo;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * 加入指令
 */
public final class JoinActionMsg extends Message<JoinActionMsg, JoinActionMsg.Builder> {
  public static final ProtoAdapter<JoinActionMsg> ADAPTER = new ProtoAdapter_JoinActionMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_GAMEID = 0;

  public static final Long DEFAULT_CREATETIMEMS = 0L;

  /**
   * 游戏ID
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer gameID;

  /**
   * 创建毫秒时间戳
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long createTimeMs;

  /**
   * 玩家信息
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.GrabRoom.PlayerInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<PlayerInfo> players;

  /**
   * 共同演唱音乐信息
   */
  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.Common.MusicInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<MusicInfo> music;

  /**
   * 配置信息
   */
  @WireField(
      tag = 5,
      adapter = "com.zq.live.proto.GrabRoom.GameConfig#ADAPTER"
  )
  private final GameConfig config;

  /**
   * 声网token
   */
  @WireField(
      tag = 6,
      adapter = "com.zq.live.proto.GrabRoom.AgoraTokenInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<AgoraTokenInfo> tokens;

  public JoinActionMsg(Integer gameID, Long createTimeMs, List<PlayerInfo> players,
      List<MusicInfo> music, GameConfig config, List<AgoraTokenInfo> tokens) {
    this(gameID, createTimeMs, players, music, config, tokens, ByteString.EMPTY);
  }

  public JoinActionMsg(Integer gameID, Long createTimeMs, List<PlayerInfo> players,
      List<MusicInfo> music, GameConfig config, List<AgoraTokenInfo> tokens,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.gameID = gameID;
    this.createTimeMs = createTimeMs;
    this.players = Internal.immutableCopyOf("players", players);
    this.music = Internal.immutableCopyOf("music", music);
    this.config = config;
    this.tokens = Internal.immutableCopyOf("tokens", tokens);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.gameID = gameID;
    builder.createTimeMs = createTimeMs;
    builder.players = Internal.copyOf("players", players);
    builder.music = Internal.copyOf("music", music);
    builder.config = config;
    builder.tokens = Internal.copyOf("tokens", tokens);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof JoinActionMsg)) return false;
    JoinActionMsg o = (JoinActionMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(gameID, o.gameID)
        && Internal.equals(createTimeMs, o.createTimeMs)
        && players.equals(o.players)
        && music.equals(o.music)
        && Internal.equals(config, o.config)
        && tokens.equals(o.tokens);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (gameID != null ? gameID.hashCode() : 0);
      result = result * 37 + (createTimeMs != null ? createTimeMs.hashCode() : 0);
      result = result * 37 + players.hashCode();
      result = result * 37 + music.hashCode();
      result = result * 37 + (config != null ? config.hashCode() : 0);
      result = result * 37 + tokens.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (gameID != null) builder.append(", gameID=").append(gameID);
    if (createTimeMs != null) builder.append(", createTimeMs=").append(createTimeMs);
    if (!players.isEmpty()) builder.append(", players=").append(players);
    if (!music.isEmpty()) builder.append(", music=").append(music);
    if (config != null) builder.append(", config=").append(config);
    if (!tokens.isEmpty()) builder.append(", tokens=").append(tokens);
    return builder.replace(0, 2, "JoinActionMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return JoinActionMsg.ADAPTER.encode(this);
  }

  public static final JoinActionMsg parseFrom(byte[] data) throws IOException {
    JoinActionMsg c = null;
       c = JoinActionMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 游戏ID
   */
  public Integer getGameID() {
    if(gameID==null){
        return DEFAULT_GAMEID;
    }
    return gameID;
  }

  /**
   * 创建毫秒时间戳
   */
  public Long getCreateTimeMs() {
    if(createTimeMs==null){
        return DEFAULT_CREATETIMEMS;
    }
    return createTimeMs;
  }

  /**
   * 玩家信息
   */
  public List<PlayerInfo> getPlayersList() {
    if(players==null){
        return new java.util.ArrayList<PlayerInfo>();
    }
    return players;
  }

  /**
   * 共同演唱音乐信息
   */
  public List<MusicInfo> getMusicList() {
    if(music==null){
        return new java.util.ArrayList<MusicInfo>();
    }
    return music;
  }

  /**
   * 配置信息
   */
  public GameConfig getConfig() {
    if(config==null){
        return new GameConfig.Builder().build();
    }
    return config;
  }

  /**
   * 声网token
   */
  public List<AgoraTokenInfo> getTokensList() {
    if(tokens==null){
        return new java.util.ArrayList<AgoraTokenInfo>();
    }
    return tokens;
  }

  /**
   * 游戏ID
   */
  public boolean hasGameID() {
    return gameID!=null;
  }

  /**
   * 创建毫秒时间戳
   */
  public boolean hasCreateTimeMs() {
    return createTimeMs!=null;
  }

  /**
   * 玩家信息
   */
  public boolean hasPlayersList() {
    return players!=null;
  }

  /**
   * 共同演唱音乐信息
   */
  public boolean hasMusicList() {
    return music!=null;
  }

  /**
   * 配置信息
   */
  public boolean hasConfig() {
    return config!=null;
  }

  /**
   * 声网token
   */
  public boolean hasTokensList() {
    return tokens!=null;
  }

  public static final class Builder extends Message.Builder<JoinActionMsg, Builder> {
    private Integer gameID;

    private Long createTimeMs;

    private List<PlayerInfo> players;

    private List<MusicInfo> music;

    private GameConfig config;

    private List<AgoraTokenInfo> tokens;

    public Builder() {
      players = Internal.newMutableList();
      music = Internal.newMutableList();
      tokens = Internal.newMutableList();
    }

    /**
     * 游戏ID
     */
    public Builder setGameID(Integer gameID) {
      this.gameID = gameID;
      return this;
    }

    /**
     * 创建毫秒时间戳
     */
    public Builder setCreateTimeMs(Long createTimeMs) {
      this.createTimeMs = createTimeMs;
      return this;
    }

    /**
     * 玩家信息
     */
    public Builder addAllPlayers(List<PlayerInfo> players) {
      Internal.checkElementsNotNull(players);
      this.players = players;
      return this;
    }

    /**
     * 共同演唱音乐信息
     */
    public Builder addAllMusic(List<MusicInfo> music) {
      Internal.checkElementsNotNull(music);
      this.music = music;
      return this;
    }

    /**
     * 配置信息
     */
    public Builder setConfig(GameConfig config) {
      this.config = config;
      return this;
    }

    /**
     * 声网token
     */
    public Builder addAllTokens(List<AgoraTokenInfo> tokens) {
      Internal.checkElementsNotNull(tokens);
      this.tokens = tokens;
      return this;
    }

    @Override
    public JoinActionMsg build() {
      return new JoinActionMsg(gameID, createTimeMs, players, music, config, tokens, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_JoinActionMsg extends ProtoAdapter<JoinActionMsg> {
    public ProtoAdapter_JoinActionMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, JoinActionMsg.class);
    }

    @Override
    public int encodedSize(JoinActionMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.gameID)
          + ProtoAdapter.SINT64.encodedSizeWithTag(2, value.createTimeMs)
          + PlayerInfo.ADAPTER.asRepeated().encodedSizeWithTag(3, value.players)
          + MusicInfo.ADAPTER.asRepeated().encodedSizeWithTag(4, value.music)
          + GameConfig.ADAPTER.encodedSizeWithTag(5, value.config)
          + AgoraTokenInfo.ADAPTER.asRepeated().encodedSizeWithTag(6, value.tokens)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, JoinActionMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.gameID);
      ProtoAdapter.SINT64.encodeWithTag(writer, 2, value.createTimeMs);
      PlayerInfo.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.players);
      MusicInfo.ADAPTER.asRepeated().encodeWithTag(writer, 4, value.music);
      GameConfig.ADAPTER.encodeWithTag(writer, 5, value.config);
      AgoraTokenInfo.ADAPTER.asRepeated().encodeWithTag(writer, 6, value.tokens);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public JoinActionMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setGameID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setCreateTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 3: builder.players.add(PlayerInfo.ADAPTER.decode(reader)); break;
          case 4: builder.music.add(MusicInfo.ADAPTER.decode(reader)); break;
          case 5: builder.setConfig(GameConfig.ADAPTER.decode(reader)); break;
          case 6: builder.tokens.add(AgoraTokenInfo.ADAPTER.decode(reader)); break;
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
    public JoinActionMsg redact(JoinActionMsg value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.players, PlayerInfo.ADAPTER);
      Internal.redactElements(builder.music, MusicInfo.ADAPTER);
      if (builder.config != null) builder.config = GameConfig.ADAPTER.redact(builder.config);
      Internal.redactElements(builder.tokens, AgoraTokenInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
