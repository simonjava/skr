// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.Room;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * 准备游戏通知消息
 */
public final class ReadyNoticeMsg extends Message<ReadyNoticeMsg, ReadyNoticeMsg.Builder> {
  public static final ProtoAdapter<ReadyNoticeMsg> ADAPTER = new ProtoAdapter_ReadyNoticeMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_HASREADYEDUSERCNT = 0;

  public static final Boolean DEFAULT_ISGAMESTART = false;

  /**
   * 准备信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Room.ReadyInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<ReadyInfo> readyInfo;

  /**
   * 已经准备人数
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#SINT32"
  )
  public final Integer hasReadyedUserCnt;

  /**
   * 游戏是否开始
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean isGameStart;

  /**
   * 轮次信息
   */
  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.Room.RoundInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<RoundInfo> roundInfo;

  /**
   * 游戏信息
   */
  @WireField(
      tag = 5,
      adapter = "com.zq.live.proto.Room.GameStartInfo#ADAPTER"
  )
  public final GameStartInfo gameStartInfo;

  /**
   * 一场到底轮次信息
   */
  @WireField(
      tag = 6,
      adapter = "com.zq.live.proto.Room.QRoundInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<QRoundInfo> qRoundInfo;

  public ReadyNoticeMsg(List<ReadyInfo> readyInfo, Integer hasReadyedUserCnt, Boolean isGameStart,
      List<RoundInfo> roundInfo, GameStartInfo gameStartInfo, List<QRoundInfo> qRoundInfo) {
    this(readyInfo, hasReadyedUserCnt, isGameStart, roundInfo, gameStartInfo, qRoundInfo, ByteString.EMPTY);
  }

  public ReadyNoticeMsg(List<ReadyInfo> readyInfo, Integer hasReadyedUserCnt, Boolean isGameStart,
      List<RoundInfo> roundInfo, GameStartInfo gameStartInfo, List<QRoundInfo> qRoundInfo,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.readyInfo = Internal.immutableCopyOf("readyInfo", readyInfo);
    this.hasReadyedUserCnt = hasReadyedUserCnt;
    this.isGameStart = isGameStart;
    this.roundInfo = Internal.immutableCopyOf("roundInfo", roundInfo);
    this.gameStartInfo = gameStartInfo;
    this.qRoundInfo = Internal.immutableCopyOf("qRoundInfo", qRoundInfo);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.readyInfo = Internal.copyOf("readyInfo", readyInfo);
    builder.hasReadyedUserCnt = hasReadyedUserCnt;
    builder.isGameStart = isGameStart;
    builder.roundInfo = Internal.copyOf("roundInfo", roundInfo);
    builder.gameStartInfo = gameStartInfo;
    builder.qRoundInfo = Internal.copyOf("qRoundInfo", qRoundInfo);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ReadyNoticeMsg)) return false;
    ReadyNoticeMsg o = (ReadyNoticeMsg) other;
    return unknownFields().equals(o.unknownFields())
        && readyInfo.equals(o.readyInfo)
        && Internal.equals(hasReadyedUserCnt, o.hasReadyedUserCnt)
        && Internal.equals(isGameStart, o.isGameStart)
        && roundInfo.equals(o.roundInfo)
        && Internal.equals(gameStartInfo, o.gameStartInfo)
        && qRoundInfo.equals(o.qRoundInfo);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + readyInfo.hashCode();
      result = result * 37 + (hasReadyedUserCnt != null ? hasReadyedUserCnt.hashCode() : 0);
      result = result * 37 + (isGameStart != null ? isGameStart.hashCode() : 0);
      result = result * 37 + roundInfo.hashCode();
      result = result * 37 + (gameStartInfo != null ? gameStartInfo.hashCode() : 0);
      result = result * 37 + qRoundInfo.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (!readyInfo.isEmpty()) builder.append(", readyInfo=").append(readyInfo);
    if (hasReadyedUserCnt != null) builder.append(", hasReadyedUserCnt=").append(hasReadyedUserCnt);
    if (isGameStart != null) builder.append(", isGameStart=").append(isGameStart);
    if (!roundInfo.isEmpty()) builder.append(", roundInfo=").append(roundInfo);
    if (gameStartInfo != null) builder.append(", gameStartInfo=").append(gameStartInfo);
    if (!qRoundInfo.isEmpty()) builder.append(", qRoundInfo=").append(qRoundInfo);
    return builder.replace(0, 2, "ReadyNoticeMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return ReadyNoticeMsg.ADAPTER.encode(this);
  }

  public static final ReadyNoticeMsg parseFrom(byte[] data) throws IOException {
    ReadyNoticeMsg c = null;
       c = ReadyNoticeMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 准备信息
   */
  public List<ReadyInfo> getReadyInfoList() {
    if(readyInfo==null){
        return new java.util.ArrayList<ReadyInfo>();
    }
    return readyInfo;
  }

  /**
   * 已经准备人数
   */
  public Integer getHasReadyedUserCnt() {
    if(hasReadyedUserCnt==null){
        return DEFAULT_HASREADYEDUSERCNT;
    }
    return hasReadyedUserCnt;
  }

  /**
   * 游戏是否开始
   */
  public Boolean getIsGameStart() {
    if(isGameStart==null){
        return DEFAULT_ISGAMESTART;
    }
    return isGameStart;
  }

  /**
   * 轮次信息
   */
  public List<RoundInfo> getRoundInfoList() {
    if(roundInfo==null){
        return new java.util.ArrayList<RoundInfo>();
    }
    return roundInfo;
  }

  /**
   * 游戏信息
   */
  public GameStartInfo getGameStartInfo() {
    if(gameStartInfo==null){
        return new GameStartInfo.Builder().build();
    }
    return gameStartInfo;
  }

  /**
   * 一场到底轮次信息
   */
  public List<QRoundInfo> getQRoundInfoList() {
    if(qRoundInfo==null){
        return new java.util.ArrayList<QRoundInfo>();
    }
    return qRoundInfo;
  }

  /**
   * 准备信息
   */
  public boolean hasReadyInfoList() {
    return readyInfo!=null;
  }

  /**
   * 已经准备人数
   */
  public boolean hasHasReadyedUserCnt() {
    return hasReadyedUserCnt!=null;
  }

  /**
   * 游戏是否开始
   */
  public boolean hasIsGameStart() {
    return isGameStart!=null;
  }

  /**
   * 轮次信息
   */
  public boolean hasRoundInfoList() {
    return roundInfo!=null;
  }

  /**
   * 游戏信息
   */
  public boolean hasGameStartInfo() {
    return gameStartInfo!=null;
  }

  /**
   * 一场到底轮次信息
   */
  public boolean hasQRoundInfoList() {
    return qRoundInfo!=null;
  }

  public static final class Builder extends Message.Builder<ReadyNoticeMsg, Builder> {
    public List<ReadyInfo> readyInfo;

    public Integer hasReadyedUserCnt;

    public Boolean isGameStart;

    public List<RoundInfo> roundInfo;

    public GameStartInfo gameStartInfo;

    public List<QRoundInfo> qRoundInfo;

    public Builder() {
      readyInfo = Internal.newMutableList();
      roundInfo = Internal.newMutableList();
      qRoundInfo = Internal.newMutableList();
    }

    /**
     * 准备信息
     */
    public Builder addAllReadyInfo(List<ReadyInfo> readyInfo) {
      Internal.checkElementsNotNull(readyInfo);
      this.readyInfo = readyInfo;
      return this;
    }

    /**
     * 已经准备人数
     */
    public Builder setHasReadyedUserCnt(Integer hasReadyedUserCnt) {
      this.hasReadyedUserCnt = hasReadyedUserCnt;
      return this;
    }

    /**
     * 游戏是否开始
     */
    public Builder setIsGameStart(Boolean isGameStart) {
      this.isGameStart = isGameStart;
      return this;
    }

    /**
     * 轮次信息
     */
    public Builder addAllRoundInfo(List<RoundInfo> roundInfo) {
      Internal.checkElementsNotNull(roundInfo);
      this.roundInfo = roundInfo;
      return this;
    }

    /**
     * 游戏信息
     */
    public Builder setGameStartInfo(GameStartInfo gameStartInfo) {
      this.gameStartInfo = gameStartInfo;
      return this;
    }

    /**
     * 一场到底轮次信息
     */
    public Builder addAllQRoundInfo(List<QRoundInfo> qRoundInfo) {
      Internal.checkElementsNotNull(qRoundInfo);
      this.qRoundInfo = qRoundInfo;
      return this;
    }

    @Override
    public ReadyNoticeMsg build() {
      return new ReadyNoticeMsg(readyInfo, hasReadyedUserCnt, isGameStart, roundInfo, gameStartInfo, qRoundInfo, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_ReadyNoticeMsg extends ProtoAdapter<ReadyNoticeMsg> {
    public ProtoAdapter_ReadyNoticeMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, ReadyNoticeMsg.class);
    }

    @Override
    public int encodedSize(ReadyNoticeMsg value) {
      return ReadyInfo.ADAPTER.asRepeated().encodedSizeWithTag(1, value.readyInfo)
          + ProtoAdapter.SINT32.encodedSizeWithTag(2, value.hasReadyedUserCnt)
          + ProtoAdapter.BOOL.encodedSizeWithTag(3, value.isGameStart)
          + RoundInfo.ADAPTER.asRepeated().encodedSizeWithTag(4, value.roundInfo)
          + GameStartInfo.ADAPTER.encodedSizeWithTag(5, value.gameStartInfo)
          + QRoundInfo.ADAPTER.asRepeated().encodedSizeWithTag(6, value.qRoundInfo)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, ReadyNoticeMsg value) throws IOException {
      ReadyInfo.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.readyInfo);
      ProtoAdapter.SINT32.encodeWithTag(writer, 2, value.hasReadyedUserCnt);
      ProtoAdapter.BOOL.encodeWithTag(writer, 3, value.isGameStart);
      RoundInfo.ADAPTER.asRepeated().encodeWithTag(writer, 4, value.roundInfo);
      GameStartInfo.ADAPTER.encodeWithTag(writer, 5, value.gameStartInfo);
      QRoundInfo.ADAPTER.asRepeated().encodeWithTag(writer, 6, value.qRoundInfo);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public ReadyNoticeMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.readyInfo.add(ReadyInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setHasReadyedUserCnt(ProtoAdapter.SINT32.decode(reader)); break;
          case 3: builder.setIsGameStart(ProtoAdapter.BOOL.decode(reader)); break;
          case 4: builder.roundInfo.add(RoundInfo.ADAPTER.decode(reader)); break;
          case 5: builder.setGameStartInfo(GameStartInfo.ADAPTER.decode(reader)); break;
          case 6: builder.qRoundInfo.add(QRoundInfo.ADAPTER.decode(reader)); break;
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
    public ReadyNoticeMsg redact(ReadyNoticeMsg value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.readyInfo, ReadyInfo.ADAPTER);
      Internal.redactElements(builder.roundInfo, RoundInfo.ADAPTER);
      if (builder.gameStartInfo != null) builder.gameStartInfo = GameStartInfo.ADAPTER.redact(builder.gameStartInfo);
      Internal.redactElements(builder.qRoundInfo, QRoundInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
