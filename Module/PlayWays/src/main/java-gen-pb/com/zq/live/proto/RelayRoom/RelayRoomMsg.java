// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: relay_room.proto
package com.zq.live.proto.RelayRoom;

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
 * java -jar -Dfile.encoding=UTF-8 ./proto/wire-compiler-2.3.0-SNAPSHOT-jar-with-dependencies.jar \
 * --proto_path=./proto --java_out=./Module/PlayWays/src/main/java-gen-pb/ relay_room.proto
 * 接唱房信令消息
 */
public final class RelayRoomMsg extends Message<RelayRoomMsg, RelayRoomMsg.Builder> {
  public static final ProtoAdapter<RelayRoomMsg> ADAPTER = new ProtoAdapter_RelayRoomMsg();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_TIMEMS = 0L;

  public static final ERelayRoomMsgType DEFAULT_MSGTYPE = ERelayRoomMsgType.RRT_UNKNOWN;

  public static final Integer DEFAULT_ROOMID = 0;

  /**
   * 房间消息产生时间，单位毫秒
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long timeMs;

  /**
   * 消息类型
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.RelayRoom.ERelayRoomMsgType#ADAPTER"
  )
  private final ERelayRoomMsgType msgType;

  /**
   * 房间ID
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  /**
   * 进入房间(系统消息下发）
   */
  @WireField(
      tag = 10,
      adapter = "com.zq.live.proto.RelayRoom.RUserEnterMsg#ADAPTER"
  )
  private final RUserEnterMsg rUserEnterMsg;

  /**
   * 结束房间(倒计时结束、玩家退出)
   */
  @WireField(
      tag = 11,
      adapter = "com.zq.live.proto.RelayRoom.RGameOverMsg#ADAPTER"
  )
  private final RGameOverMsg rGameOverMsg;

  /**
   * 加载下个轮次
   */
  @WireField(
      tag = 12,
      adapter = "com.zq.live.proto.RelayRoom.RNextRoundMsg#ADAPTER"
  )
  private final RNextRoundMsg rNextRoundMsg;

  /**
   * 解锁点歌
   */
  @WireField(
      tag = 13,
      adapter = "com.zq.live.proto.RelayRoom.RUnlockMsg#ADAPTER"
  )
  private final RUnlockMsg rUnlockMsg;

  /**
   * 同步状态
   */
  @WireField(
      tag = 14,
      adapter = "com.zq.live.proto.RelayRoom.RSyncMsg#ADAPTER"
  )
  private final RSyncMsg rSyncMsg;

  /**
   * 请求接唱
   */
  @WireField(
      tag = 15,
      adapter = "com.zq.live.proto.RelayRoom.RReqAddMusicMsg#ADAPTER"
  )
  private final RReqAddMusicMsg rReqAddMusicMsg;

  /**
   * 点歌
   */
  @WireField(
      tag = 16,
      adapter = "com.zq.live.proto.RelayRoom.RAddMusicMsg#ADAPTER"
  )
  private final RAddMusicMsg rAddMusicMsg;

  /**
   * 删歌
   */
  @WireField(
      tag = 17,
      adapter = "com.zq.live.proto.RelayRoom.RDelMusicMsg#ADAPTER"
  )
  private final RDelMusicMsg rDelMusicMsg;

  /**
   * 置顶
   */
  @WireField(
      tag = 18,
      adapter = "com.zq.live.proto.RelayRoom.RUpMusicMsg#ADAPTER"
  )
  private final RUpMusicMsg rUpMusicMsg;

  /**
   * 静音
   */
  @WireField(
      tag = 19,
      adapter = "com.zq.live.proto.RelayRoom.RMuteMsg#ADAPTER"
  )
  private final RMuteMsg rMuteMsg;

  public RelayRoomMsg(Long timeMs, ERelayRoomMsgType msgType, Integer roomID,
      RUserEnterMsg rUserEnterMsg, RGameOverMsg rGameOverMsg, RNextRoundMsg rNextRoundMsg,
      RUnlockMsg rUnlockMsg, RSyncMsg rSyncMsg, RReqAddMusicMsg rReqAddMusicMsg,
      RAddMusicMsg rAddMusicMsg, RDelMusicMsg rDelMusicMsg, RUpMusicMsg rUpMusicMsg,
      RMuteMsg rMuteMsg) {
    this(timeMs, msgType, roomID, rUserEnterMsg, rGameOverMsg, rNextRoundMsg, rUnlockMsg, rSyncMsg, rReqAddMusicMsg, rAddMusicMsg, rDelMusicMsg, rUpMusicMsg, rMuteMsg, ByteString.EMPTY);
  }

  public RelayRoomMsg(Long timeMs, ERelayRoomMsgType msgType, Integer roomID,
      RUserEnterMsg rUserEnterMsg, RGameOverMsg rGameOverMsg, RNextRoundMsg rNextRoundMsg,
      RUnlockMsg rUnlockMsg, RSyncMsg rSyncMsg, RReqAddMusicMsg rReqAddMusicMsg,
      RAddMusicMsg rAddMusicMsg, RDelMusicMsg rDelMusicMsg, RUpMusicMsg rUpMusicMsg,
      RMuteMsg rMuteMsg, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.timeMs = timeMs;
    this.msgType = msgType;
    this.roomID = roomID;
    this.rUserEnterMsg = rUserEnterMsg;
    this.rGameOverMsg = rGameOverMsg;
    this.rNextRoundMsg = rNextRoundMsg;
    this.rUnlockMsg = rUnlockMsg;
    this.rSyncMsg = rSyncMsg;
    this.rReqAddMusicMsg = rReqAddMusicMsg;
    this.rAddMusicMsg = rAddMusicMsg;
    this.rDelMusicMsg = rDelMusicMsg;
    this.rUpMusicMsg = rUpMusicMsg;
    this.rMuteMsg = rMuteMsg;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.timeMs = timeMs;
    builder.msgType = msgType;
    builder.roomID = roomID;
    builder.rUserEnterMsg = rUserEnterMsg;
    builder.rGameOverMsg = rGameOverMsg;
    builder.rNextRoundMsg = rNextRoundMsg;
    builder.rUnlockMsg = rUnlockMsg;
    builder.rSyncMsg = rSyncMsg;
    builder.rReqAddMusicMsg = rReqAddMusicMsg;
    builder.rAddMusicMsg = rAddMusicMsg;
    builder.rDelMusicMsg = rDelMusicMsg;
    builder.rUpMusicMsg = rUpMusicMsg;
    builder.rMuteMsg = rMuteMsg;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RelayRoomMsg)) return false;
    RelayRoomMsg o = (RelayRoomMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(timeMs, o.timeMs)
        && Internal.equals(msgType, o.msgType)
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(rUserEnterMsg, o.rUserEnterMsg)
        && Internal.equals(rGameOverMsg, o.rGameOverMsg)
        && Internal.equals(rNextRoundMsg, o.rNextRoundMsg)
        && Internal.equals(rUnlockMsg, o.rUnlockMsg)
        && Internal.equals(rSyncMsg, o.rSyncMsg)
        && Internal.equals(rReqAddMusicMsg, o.rReqAddMusicMsg)
        && Internal.equals(rAddMusicMsg, o.rAddMusicMsg)
        && Internal.equals(rDelMusicMsg, o.rDelMusicMsg)
        && Internal.equals(rUpMusicMsg, o.rUpMusicMsg)
        && Internal.equals(rMuteMsg, o.rMuteMsg);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (timeMs != null ? timeMs.hashCode() : 0);
      result = result * 37 + (msgType != null ? msgType.hashCode() : 0);
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (rUserEnterMsg != null ? rUserEnterMsg.hashCode() : 0);
      result = result * 37 + (rGameOverMsg != null ? rGameOverMsg.hashCode() : 0);
      result = result * 37 + (rNextRoundMsg != null ? rNextRoundMsg.hashCode() : 0);
      result = result * 37 + (rUnlockMsg != null ? rUnlockMsg.hashCode() : 0);
      result = result * 37 + (rSyncMsg != null ? rSyncMsg.hashCode() : 0);
      result = result * 37 + (rReqAddMusicMsg != null ? rReqAddMusicMsg.hashCode() : 0);
      result = result * 37 + (rAddMusicMsg != null ? rAddMusicMsg.hashCode() : 0);
      result = result * 37 + (rDelMusicMsg != null ? rDelMusicMsg.hashCode() : 0);
      result = result * 37 + (rUpMusicMsg != null ? rUpMusicMsg.hashCode() : 0);
      result = result * 37 + (rMuteMsg != null ? rMuteMsg.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (timeMs != null) builder.append(", timeMs=").append(timeMs);
    if (msgType != null) builder.append(", msgType=").append(msgType);
    if (roomID != null) builder.append(", roomID=").append(roomID);
    if (rUserEnterMsg != null) builder.append(", rUserEnterMsg=").append(rUserEnterMsg);
    if (rGameOverMsg != null) builder.append(", rGameOverMsg=").append(rGameOverMsg);
    if (rNextRoundMsg != null) builder.append(", rNextRoundMsg=").append(rNextRoundMsg);
    if (rUnlockMsg != null) builder.append(", rUnlockMsg=").append(rUnlockMsg);
    if (rSyncMsg != null) builder.append(", rSyncMsg=").append(rSyncMsg);
    if (rReqAddMusicMsg != null) builder.append(", rReqAddMusicMsg=").append(rReqAddMusicMsg);
    if (rAddMusicMsg != null) builder.append(", rAddMusicMsg=").append(rAddMusicMsg);
    if (rDelMusicMsg != null) builder.append(", rDelMusicMsg=").append(rDelMusicMsg);
    if (rUpMusicMsg != null) builder.append(", rUpMusicMsg=").append(rUpMusicMsg);
    if (rMuteMsg != null) builder.append(", rMuteMsg=").append(rMuteMsg);
    return builder.replace(0, 2, "RelayRoomMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RelayRoomMsg.ADAPTER.encode(this);
  }

  public static final RelayRoomMsg parseFrom(byte[] data) throws IOException {
    RelayRoomMsg c = null;
       c = RelayRoomMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 房间消息产生时间，单位毫秒
   */
  public Long getTimeMs() {
    if(timeMs==null){
        return DEFAULT_TIMEMS;
    }
    return timeMs;
  }

  /**
   * 消息类型
   */
  public ERelayRoomMsgType getMsgType() {
    if(msgType==null){
        return new ERelayRoomMsgType.Builder().build();
    }
    return msgType;
  }

  /**
   * 房间ID
   */
  public Integer getRoomID() {
    if(roomID==null){
        return DEFAULT_ROOMID;
    }
    return roomID;
  }

  /**
   * 进入房间(系统消息下发）
   */
  public RUserEnterMsg getRUserEnterMsg() {
    if(rUserEnterMsg==null){
        return new RUserEnterMsg.Builder().build();
    }
    return rUserEnterMsg;
  }

  /**
   * 结束房间(倒计时结束、玩家退出)
   */
  public RGameOverMsg getRGameOverMsg() {
    if(rGameOverMsg==null){
        return new RGameOverMsg.Builder().build();
    }
    return rGameOverMsg;
  }

  /**
   * 加载下个轮次
   */
  public RNextRoundMsg getRNextRoundMsg() {
    if(rNextRoundMsg==null){
        return new RNextRoundMsg.Builder().build();
    }
    return rNextRoundMsg;
  }

  /**
   * 解锁点歌
   */
  public RUnlockMsg getRUnlockMsg() {
    if(rUnlockMsg==null){
        return new RUnlockMsg.Builder().build();
    }
    return rUnlockMsg;
  }

  /**
   * 同步状态
   */
  public RSyncMsg getRSyncMsg() {
    if(rSyncMsg==null){
        return new RSyncMsg.Builder().build();
    }
    return rSyncMsg;
  }

  /**
   * 请求接唱
   */
  public RReqAddMusicMsg getRReqAddMusicMsg() {
    if(rReqAddMusicMsg==null){
        return new RReqAddMusicMsg.Builder().build();
    }
    return rReqAddMusicMsg;
  }

  /**
   * 点歌
   */
  public RAddMusicMsg getRAddMusicMsg() {
    if(rAddMusicMsg==null){
        return new RAddMusicMsg.Builder().build();
    }
    return rAddMusicMsg;
  }

  /**
   * 删歌
   */
  public RDelMusicMsg getRDelMusicMsg() {
    if(rDelMusicMsg==null){
        return new RDelMusicMsg.Builder().build();
    }
    return rDelMusicMsg;
  }

  /**
   * 置顶
   */
  public RUpMusicMsg getRUpMusicMsg() {
    if(rUpMusicMsg==null){
        return new RUpMusicMsg.Builder().build();
    }
    return rUpMusicMsg;
  }

  /**
   * 静音
   */
  public RMuteMsg getRMuteMsg() {
    if(rMuteMsg==null){
        return new RMuteMsg.Builder().build();
    }
    return rMuteMsg;
  }

  /**
   * 房间消息产生时间，单位毫秒
   */
  public boolean hasTimeMs() {
    return timeMs!=null;
  }

  /**
   * 消息类型
   */
  public boolean hasMsgType() {
    return msgType!=null;
  }

  /**
   * 房间ID
   */
  public boolean hasRoomID() {
    return roomID!=null;
  }

  /**
   * 进入房间(系统消息下发）
   */
  public boolean hasRUserEnterMsg() {
    return rUserEnterMsg!=null;
  }

  /**
   * 结束房间(倒计时结束、玩家退出)
   */
  public boolean hasRGameOverMsg() {
    return rGameOverMsg!=null;
  }

  /**
   * 加载下个轮次
   */
  public boolean hasRNextRoundMsg() {
    return rNextRoundMsg!=null;
  }

  /**
   * 解锁点歌
   */
  public boolean hasRUnlockMsg() {
    return rUnlockMsg!=null;
  }

  /**
   * 同步状态
   */
  public boolean hasRSyncMsg() {
    return rSyncMsg!=null;
  }

  /**
   * 请求接唱
   */
  public boolean hasRReqAddMusicMsg() {
    return rReqAddMusicMsg!=null;
  }

  /**
   * 点歌
   */
  public boolean hasRAddMusicMsg() {
    return rAddMusicMsg!=null;
  }

  /**
   * 删歌
   */
  public boolean hasRDelMusicMsg() {
    return rDelMusicMsg!=null;
  }

  /**
   * 置顶
   */
  public boolean hasRUpMusicMsg() {
    return rUpMusicMsg!=null;
  }

  /**
   * 静音
   */
  public boolean hasRMuteMsg() {
    return rMuteMsg!=null;
  }

  public static final class Builder extends Message.Builder<RelayRoomMsg, Builder> {
    private Long timeMs;

    private ERelayRoomMsgType msgType;

    private Integer roomID;

    private RUserEnterMsg rUserEnterMsg;

    private RGameOverMsg rGameOverMsg;

    private RNextRoundMsg rNextRoundMsg;

    private RUnlockMsg rUnlockMsg;

    private RSyncMsg rSyncMsg;

    private RReqAddMusicMsg rReqAddMusicMsg;

    private RAddMusicMsg rAddMusicMsg;

    private RDelMusicMsg rDelMusicMsg;

    private RUpMusicMsg rUpMusicMsg;

    private RMuteMsg rMuteMsg;

    public Builder() {
    }

    /**
     * 房间消息产生时间，单位毫秒
     */
    public Builder setTimeMs(Long timeMs) {
      this.timeMs = timeMs;
      return this;
    }

    /**
     * 消息类型
     */
    public Builder setMsgType(ERelayRoomMsgType msgType) {
      this.msgType = msgType;
      return this;
    }

    /**
     * 房间ID
     */
    public Builder setRoomID(Integer roomID) {
      this.roomID = roomID;
      return this;
    }

    /**
     * 进入房间(系统消息下发）
     */
    public Builder setRUserEnterMsg(RUserEnterMsg rUserEnterMsg) {
      this.rUserEnterMsg = rUserEnterMsg;
      return this;
    }

    /**
     * 结束房间(倒计时结束、玩家退出)
     */
    public Builder setRGameOverMsg(RGameOverMsg rGameOverMsg) {
      this.rGameOverMsg = rGameOverMsg;
      return this;
    }

    /**
     * 加载下个轮次
     */
    public Builder setRNextRoundMsg(RNextRoundMsg rNextRoundMsg) {
      this.rNextRoundMsg = rNextRoundMsg;
      return this;
    }

    /**
     * 解锁点歌
     */
    public Builder setRUnlockMsg(RUnlockMsg rUnlockMsg) {
      this.rUnlockMsg = rUnlockMsg;
      return this;
    }

    /**
     * 同步状态
     */
    public Builder setRSyncMsg(RSyncMsg rSyncMsg) {
      this.rSyncMsg = rSyncMsg;
      return this;
    }

    /**
     * 请求接唱
     */
    public Builder setRReqAddMusicMsg(RReqAddMusicMsg rReqAddMusicMsg) {
      this.rReqAddMusicMsg = rReqAddMusicMsg;
      return this;
    }

    /**
     * 点歌
     */
    public Builder setRAddMusicMsg(RAddMusicMsg rAddMusicMsg) {
      this.rAddMusicMsg = rAddMusicMsg;
      return this;
    }

    /**
     * 删歌
     */
    public Builder setRDelMusicMsg(RDelMusicMsg rDelMusicMsg) {
      this.rDelMusicMsg = rDelMusicMsg;
      return this;
    }

    /**
     * 置顶
     */
    public Builder setRUpMusicMsg(RUpMusicMsg rUpMusicMsg) {
      this.rUpMusicMsg = rUpMusicMsg;
      return this;
    }

    /**
     * 静音
     */
    public Builder setRMuteMsg(RMuteMsg rMuteMsg) {
      this.rMuteMsg = rMuteMsg;
      return this;
    }

    @Override
    public RelayRoomMsg build() {
      return new RelayRoomMsg(timeMs, msgType, roomID, rUserEnterMsg, rGameOverMsg, rNextRoundMsg, rUnlockMsg, rSyncMsg, rReqAddMusicMsg, rAddMusicMsg, rDelMusicMsg, rUpMusicMsg, rMuteMsg, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RelayRoomMsg extends ProtoAdapter<RelayRoomMsg> {
    public ProtoAdapter_RelayRoomMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, RelayRoomMsg.class);
    }

    @Override
    public int encodedSize(RelayRoomMsg value) {
      return ProtoAdapter.SINT64.encodedSizeWithTag(1, value.timeMs)
          + ERelayRoomMsgType.ADAPTER.encodedSizeWithTag(2, value.msgType)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.roomID)
          + RUserEnterMsg.ADAPTER.encodedSizeWithTag(10, value.rUserEnterMsg)
          + RGameOverMsg.ADAPTER.encodedSizeWithTag(11, value.rGameOverMsg)
          + RNextRoundMsg.ADAPTER.encodedSizeWithTag(12, value.rNextRoundMsg)
          + RUnlockMsg.ADAPTER.encodedSizeWithTag(13, value.rUnlockMsg)
          + RSyncMsg.ADAPTER.encodedSizeWithTag(14, value.rSyncMsg)
          + RReqAddMusicMsg.ADAPTER.encodedSizeWithTag(15, value.rReqAddMusicMsg)
          + RAddMusicMsg.ADAPTER.encodedSizeWithTag(16, value.rAddMusicMsg)
          + RDelMusicMsg.ADAPTER.encodedSizeWithTag(17, value.rDelMusicMsg)
          + RUpMusicMsg.ADAPTER.encodedSizeWithTag(18, value.rUpMusicMsg)
          + RMuteMsg.ADAPTER.encodedSizeWithTag(19, value.rMuteMsg)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RelayRoomMsg value) throws IOException {
      ProtoAdapter.SINT64.encodeWithTag(writer, 1, value.timeMs);
      ERelayRoomMsgType.ADAPTER.encodeWithTag(writer, 2, value.msgType);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.roomID);
      RUserEnterMsg.ADAPTER.encodeWithTag(writer, 10, value.rUserEnterMsg);
      RGameOverMsg.ADAPTER.encodeWithTag(writer, 11, value.rGameOverMsg);
      RNextRoundMsg.ADAPTER.encodeWithTag(writer, 12, value.rNextRoundMsg);
      RUnlockMsg.ADAPTER.encodeWithTag(writer, 13, value.rUnlockMsg);
      RSyncMsg.ADAPTER.encodeWithTag(writer, 14, value.rSyncMsg);
      RReqAddMusicMsg.ADAPTER.encodeWithTag(writer, 15, value.rReqAddMusicMsg);
      RAddMusicMsg.ADAPTER.encodeWithTag(writer, 16, value.rAddMusicMsg);
      RDelMusicMsg.ADAPTER.encodeWithTag(writer, 17, value.rDelMusicMsg);
      RUpMusicMsg.ADAPTER.encodeWithTag(writer, 18, value.rUpMusicMsg);
      RMuteMsg.ADAPTER.encodeWithTag(writer, 19, value.rMuteMsg);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RelayRoomMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 2: {
            try {
              builder.setMsgType(ERelayRoomMsgType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 3: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 10: builder.setRUserEnterMsg(RUserEnterMsg.ADAPTER.decode(reader)); break;
          case 11: builder.setRGameOverMsg(RGameOverMsg.ADAPTER.decode(reader)); break;
          case 12: builder.setRNextRoundMsg(RNextRoundMsg.ADAPTER.decode(reader)); break;
          case 13: builder.setRUnlockMsg(RUnlockMsg.ADAPTER.decode(reader)); break;
          case 14: builder.setRSyncMsg(RSyncMsg.ADAPTER.decode(reader)); break;
          case 15: builder.setRReqAddMusicMsg(RReqAddMusicMsg.ADAPTER.decode(reader)); break;
          case 16: builder.setRAddMusicMsg(RAddMusicMsg.ADAPTER.decode(reader)); break;
          case 17: builder.setRDelMusicMsg(RDelMusicMsg.ADAPTER.decode(reader)); break;
          case 18: builder.setRUpMusicMsg(RUpMusicMsg.ADAPTER.decode(reader)); break;
          case 19: builder.setRMuteMsg(RMuteMsg.ADAPTER.decode(reader)); break;
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
    public RelayRoomMsg redact(RelayRoomMsg value) {
      Builder builder = value.newBuilder();
      if (builder.rUserEnterMsg != null) builder.rUserEnterMsg = RUserEnterMsg.ADAPTER.redact(builder.rUserEnterMsg);
      if (builder.rGameOverMsg != null) builder.rGameOverMsg = RGameOverMsg.ADAPTER.redact(builder.rGameOverMsg);
      if (builder.rNextRoundMsg != null) builder.rNextRoundMsg = RNextRoundMsg.ADAPTER.redact(builder.rNextRoundMsg);
      if (builder.rUnlockMsg != null) builder.rUnlockMsg = RUnlockMsg.ADAPTER.redact(builder.rUnlockMsg);
      if (builder.rSyncMsg != null) builder.rSyncMsg = RSyncMsg.ADAPTER.redact(builder.rSyncMsg);
      if (builder.rReqAddMusicMsg != null) builder.rReqAddMusicMsg = RReqAddMusicMsg.ADAPTER.redact(builder.rReqAddMusicMsg);
      if (builder.rAddMusicMsg != null) builder.rAddMusicMsg = RAddMusicMsg.ADAPTER.redact(builder.rAddMusicMsg);
      if (builder.rDelMusicMsg != null) builder.rDelMusicMsg = RDelMusicMsg.ADAPTER.redact(builder.rDelMusicMsg);
      if (builder.rUpMusicMsg != null) builder.rUpMusicMsg = RUpMusicMsg.ADAPTER.redact(builder.rUpMusicMsg);
      if (builder.rMuteMsg != null) builder.rMuteMsg = RMuteMsg.ADAPTER.redact(builder.rMuteMsg);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}