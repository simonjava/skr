// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: race_room.proto
package com.zq.live.proto.RaceRoom;

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
 * --proto_path=./proto --java_out=./Module/PlayWays/src/main/java-gen-pb/ race_room.proto
 * 擂台赛房间信令消息
 */
public final class RaceRoomMsg extends Message<RaceRoomMsg, RaceRoomMsg.Builder> {
  public static final ProtoAdapter<RaceRoomMsg> ADAPTER = new ProtoAdapter_RaceRoomMsg();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_TIMEMS = 0L;

  public static final ERaceRoomMsgType DEFAULT_MSGTYPE = ERaceRoomMsgType.RRM_UNKNOWN;

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
      adapter = "com.zq.live.proto.RaceRoom.ERaceRoomMsgType#ADAPTER"
  )
  private final ERaceRoomMsgType msgType;

  /**
   * 房间ID
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  /**
   * 擂台赛：加入游戏指令消息
   */
  @WireField(
      tag = 100,
      adapter = "com.zq.live.proto.RaceRoom.RJoinActionMsg#ADAPTER"
  )
  private final RJoinActionMsg rJoinActionMsg;

  /**
   * 擂台赛：加入游戏通知消息
   */
  @WireField(
      tag = 101,
      adapter = "com.zq.live.proto.RaceRoom.RJoinNoticeMsg#ADAPTER"
  )
  private final RJoinNoticeMsg rJoinNoticeMsg;

  /**
   * 擂台赛：退出游戏
   */
  @WireField(
      tag = 102,
      adapter = "com.zq.live.proto.RaceRoom.RExitGameMsg#ADAPTER"
  )
  private final RExitGameMsg rExitGameMsg;

  /**
   * 擂台赛：爆灯通知
   */
  @WireField(
      tag = 103,
      adapter = "com.zq.live.proto.RaceRoom.RBLightMsg#ADAPTER"
  )
  private final RBLightMsg rBLightMsg;

  /**
   * 擂台赛：想唱消息,即抢唱
   */
  @WireField(
      tag = 104,
      adapter = "com.zq.live.proto.RaceRoom.RWantSingChanceMsg#ADAPTER"
  )
  private final RWantSingChanceMsg rWantSingChanceMsg;

  /**
   * 擂台赛：获得抢唱的结果
   */
  @WireField(
      tag = 105,
      adapter = "com.zq.live.proto.RaceRoom.RGetSingChanceMsg#ADAPTER"
  )
  private final RGetSingChanceMsg rGetSingChanceMsg;

  /**
   * 擂台赛：同步状态
   */
  @WireField(
      tag = 106,
      adapter = "com.zq.live.proto.RaceRoom.RSyncStatusMsg#ADAPTER"
  )
  private final RSyncStatusMsg rSyncStatusMsg;

  /**
   * 擂台赛：轮次结束
   */
  @WireField(
      tag = 107,
      adapter = "com.zq.live.proto.RaceRoom.RaceRoundOverMsg#ADAPTER"
  )
  private final RaceRoundOverMsg rRoundOverMsg;

  public RaceRoomMsg(Long timeMs, ERaceRoomMsgType msgType, Integer roomID,
      RJoinActionMsg rJoinActionMsg, RJoinNoticeMsg rJoinNoticeMsg, RExitGameMsg rExitGameMsg,
      RBLightMsg rBLightMsg, RWantSingChanceMsg rWantSingChanceMsg,
      RGetSingChanceMsg rGetSingChanceMsg, RSyncStatusMsg rSyncStatusMsg,
      RaceRoundOverMsg rRoundOverMsg) {
    this(timeMs, msgType, roomID, rJoinActionMsg, rJoinNoticeMsg, rExitGameMsg, rBLightMsg, rWantSingChanceMsg, rGetSingChanceMsg, rSyncStatusMsg, rRoundOverMsg, ByteString.EMPTY);
  }

  public RaceRoomMsg(Long timeMs, ERaceRoomMsgType msgType, Integer roomID,
      RJoinActionMsg rJoinActionMsg, RJoinNoticeMsg rJoinNoticeMsg, RExitGameMsg rExitGameMsg,
      RBLightMsg rBLightMsg, RWantSingChanceMsg rWantSingChanceMsg,
      RGetSingChanceMsg rGetSingChanceMsg, RSyncStatusMsg rSyncStatusMsg,
      RaceRoundOverMsg rRoundOverMsg, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.timeMs = timeMs;
    this.msgType = msgType;
    this.roomID = roomID;
    this.rJoinActionMsg = rJoinActionMsg;
    this.rJoinNoticeMsg = rJoinNoticeMsg;
    this.rExitGameMsg = rExitGameMsg;
    this.rBLightMsg = rBLightMsg;
    this.rWantSingChanceMsg = rWantSingChanceMsg;
    this.rGetSingChanceMsg = rGetSingChanceMsg;
    this.rSyncStatusMsg = rSyncStatusMsg;
    this.rRoundOverMsg = rRoundOverMsg;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.timeMs = timeMs;
    builder.msgType = msgType;
    builder.roomID = roomID;
    builder.rJoinActionMsg = rJoinActionMsg;
    builder.rJoinNoticeMsg = rJoinNoticeMsg;
    builder.rExitGameMsg = rExitGameMsg;
    builder.rBLightMsg = rBLightMsg;
    builder.rWantSingChanceMsg = rWantSingChanceMsg;
    builder.rGetSingChanceMsg = rGetSingChanceMsg;
    builder.rSyncStatusMsg = rSyncStatusMsg;
    builder.rRoundOverMsg = rRoundOverMsg;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RaceRoomMsg)) return false;
    RaceRoomMsg o = (RaceRoomMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(timeMs, o.timeMs)
        && Internal.equals(msgType, o.msgType)
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(rJoinActionMsg, o.rJoinActionMsg)
        && Internal.equals(rJoinNoticeMsg, o.rJoinNoticeMsg)
        && Internal.equals(rExitGameMsg, o.rExitGameMsg)
        && Internal.equals(rBLightMsg, o.rBLightMsg)
        && Internal.equals(rWantSingChanceMsg, o.rWantSingChanceMsg)
        && Internal.equals(rGetSingChanceMsg, o.rGetSingChanceMsg)
        && Internal.equals(rSyncStatusMsg, o.rSyncStatusMsg)
        && Internal.equals(rRoundOverMsg, o.rRoundOverMsg);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (timeMs != null ? timeMs.hashCode() : 0);
      result = result * 37 + (msgType != null ? msgType.hashCode() : 0);
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (rJoinActionMsg != null ? rJoinActionMsg.hashCode() : 0);
      result = result * 37 + (rJoinNoticeMsg != null ? rJoinNoticeMsg.hashCode() : 0);
      result = result * 37 + (rExitGameMsg != null ? rExitGameMsg.hashCode() : 0);
      result = result * 37 + (rBLightMsg != null ? rBLightMsg.hashCode() : 0);
      result = result * 37 + (rWantSingChanceMsg != null ? rWantSingChanceMsg.hashCode() : 0);
      result = result * 37 + (rGetSingChanceMsg != null ? rGetSingChanceMsg.hashCode() : 0);
      result = result * 37 + (rSyncStatusMsg != null ? rSyncStatusMsg.hashCode() : 0);
      result = result * 37 + (rRoundOverMsg != null ? rRoundOverMsg.hashCode() : 0);
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
    if (rJoinActionMsg != null) builder.append(", rJoinActionMsg=").append(rJoinActionMsg);
    if (rJoinNoticeMsg != null) builder.append(", rJoinNoticeMsg=").append(rJoinNoticeMsg);
    if (rExitGameMsg != null) builder.append(", rExitGameMsg=").append(rExitGameMsg);
    if (rBLightMsg != null) builder.append(", rBLightMsg=").append(rBLightMsg);
    if (rWantSingChanceMsg != null) builder.append(", rWantSingChanceMsg=").append(rWantSingChanceMsg);
    if (rGetSingChanceMsg != null) builder.append(", rGetSingChanceMsg=").append(rGetSingChanceMsg);
    if (rSyncStatusMsg != null) builder.append(", rSyncStatusMsg=").append(rSyncStatusMsg);
    if (rRoundOverMsg != null) builder.append(", rRoundOverMsg=").append(rRoundOverMsg);
    return builder.replace(0, 2, "RaceRoomMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RaceRoomMsg.ADAPTER.encode(this);
  }

  public static final RaceRoomMsg parseFrom(byte[] data) throws IOException {
    RaceRoomMsg c = null;
       c = RaceRoomMsg.ADAPTER.decode(data);
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
  public ERaceRoomMsgType getMsgType() {
    if(msgType==null){
        return new ERaceRoomMsgType.Builder().build();
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
   * 擂台赛：加入游戏指令消息
   */
  public RJoinActionMsg getRJoinActionMsg() {
    if(rJoinActionMsg==null){
        return new RJoinActionMsg.Builder().build();
    }
    return rJoinActionMsg;
  }

  /**
   * 擂台赛：加入游戏通知消息
   */
  public RJoinNoticeMsg getRJoinNoticeMsg() {
    if(rJoinNoticeMsg==null){
        return new RJoinNoticeMsg.Builder().build();
    }
    return rJoinNoticeMsg;
  }

  /**
   * 擂台赛：退出游戏
   */
  public RExitGameMsg getRExitGameMsg() {
    if(rExitGameMsg==null){
        return new RExitGameMsg.Builder().build();
    }
    return rExitGameMsg;
  }

  /**
   * 擂台赛：爆灯通知
   */
  public RBLightMsg getRBLightMsg() {
    if(rBLightMsg==null){
        return new RBLightMsg.Builder().build();
    }
    return rBLightMsg;
  }

  /**
   * 擂台赛：想唱消息,即抢唱
   */
  public RWantSingChanceMsg getRWantSingChanceMsg() {
    if(rWantSingChanceMsg==null){
        return new RWantSingChanceMsg.Builder().build();
    }
    return rWantSingChanceMsg;
  }

  /**
   * 擂台赛：获得抢唱的结果
   */
  public RGetSingChanceMsg getRGetSingChanceMsg() {
    if(rGetSingChanceMsg==null){
        return new RGetSingChanceMsg.Builder().build();
    }
    return rGetSingChanceMsg;
  }

  /**
   * 擂台赛：同步状态
   */
  public RSyncStatusMsg getRSyncStatusMsg() {
    if(rSyncStatusMsg==null){
        return new RSyncStatusMsg.Builder().build();
    }
    return rSyncStatusMsg;
  }

  /**
   * 擂台赛：轮次结束
   */
  public RaceRoundOverMsg getRRoundOverMsg() {
    if(rRoundOverMsg==null){
        return new RaceRoundOverMsg.Builder().build();
    }
    return rRoundOverMsg;
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
   * 擂台赛：加入游戏指令消息
   */
  public boolean hasRJoinActionMsg() {
    return rJoinActionMsg!=null;
  }

  /**
   * 擂台赛：加入游戏通知消息
   */
  public boolean hasRJoinNoticeMsg() {
    return rJoinNoticeMsg!=null;
  }

  /**
   * 擂台赛：退出游戏
   */
  public boolean hasRExitGameMsg() {
    return rExitGameMsg!=null;
  }

  /**
   * 擂台赛：爆灯通知
   */
  public boolean hasRBLightMsg() {
    return rBLightMsg!=null;
  }

  /**
   * 擂台赛：想唱消息,即抢唱
   */
  public boolean hasRWantSingChanceMsg() {
    return rWantSingChanceMsg!=null;
  }

  /**
   * 擂台赛：获得抢唱的结果
   */
  public boolean hasRGetSingChanceMsg() {
    return rGetSingChanceMsg!=null;
  }

  /**
   * 擂台赛：同步状态
   */
  public boolean hasRSyncStatusMsg() {
    return rSyncStatusMsg!=null;
  }

  /**
   * 擂台赛：轮次结束
   */
  public boolean hasRRoundOverMsg() {
    return rRoundOverMsg!=null;
  }

  public static final class Builder extends Message.Builder<RaceRoomMsg, Builder> {
    private Long timeMs;

    private ERaceRoomMsgType msgType;

    private Integer roomID;

    private RJoinActionMsg rJoinActionMsg;

    private RJoinNoticeMsg rJoinNoticeMsg;

    private RExitGameMsg rExitGameMsg;

    private RBLightMsg rBLightMsg;

    private RWantSingChanceMsg rWantSingChanceMsg;

    private RGetSingChanceMsg rGetSingChanceMsg;

    private RSyncStatusMsg rSyncStatusMsg;

    private RaceRoundOverMsg rRoundOverMsg;

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
    public Builder setMsgType(ERaceRoomMsgType msgType) {
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
     * 擂台赛：加入游戏指令消息
     */
    public Builder setRJoinActionMsg(RJoinActionMsg rJoinActionMsg) {
      this.rJoinActionMsg = rJoinActionMsg;
      return this;
    }

    /**
     * 擂台赛：加入游戏通知消息
     */
    public Builder setRJoinNoticeMsg(RJoinNoticeMsg rJoinNoticeMsg) {
      this.rJoinNoticeMsg = rJoinNoticeMsg;
      return this;
    }

    /**
     * 擂台赛：退出游戏
     */
    public Builder setRExitGameMsg(RExitGameMsg rExitGameMsg) {
      this.rExitGameMsg = rExitGameMsg;
      return this;
    }

    /**
     * 擂台赛：爆灯通知
     */
    public Builder setRBLightMsg(RBLightMsg rBLightMsg) {
      this.rBLightMsg = rBLightMsg;
      return this;
    }

    /**
     * 擂台赛：想唱消息,即抢唱
     */
    public Builder setRWantSingChanceMsg(RWantSingChanceMsg rWantSingChanceMsg) {
      this.rWantSingChanceMsg = rWantSingChanceMsg;
      return this;
    }

    /**
     * 擂台赛：获得抢唱的结果
     */
    public Builder setRGetSingChanceMsg(RGetSingChanceMsg rGetSingChanceMsg) {
      this.rGetSingChanceMsg = rGetSingChanceMsg;
      return this;
    }

    /**
     * 擂台赛：同步状态
     */
    public Builder setRSyncStatusMsg(RSyncStatusMsg rSyncStatusMsg) {
      this.rSyncStatusMsg = rSyncStatusMsg;
      return this;
    }

    /**
     * 擂台赛：轮次结束
     */
    public Builder setRRoundOverMsg(RaceRoundOverMsg rRoundOverMsg) {
      this.rRoundOverMsg = rRoundOverMsg;
      return this;
    }

    @Override
    public RaceRoomMsg build() {
      return new RaceRoomMsg(timeMs, msgType, roomID, rJoinActionMsg, rJoinNoticeMsg, rExitGameMsg, rBLightMsg, rWantSingChanceMsg, rGetSingChanceMsg, rSyncStatusMsg, rRoundOverMsg, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RaceRoomMsg extends ProtoAdapter<RaceRoomMsg> {
    public ProtoAdapter_RaceRoomMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, RaceRoomMsg.class);
    }

    @Override
    public int encodedSize(RaceRoomMsg value) {
      return ProtoAdapter.SINT64.encodedSizeWithTag(1, value.timeMs)
          + ERaceRoomMsgType.ADAPTER.encodedSizeWithTag(2, value.msgType)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.roomID)
          + RJoinActionMsg.ADAPTER.encodedSizeWithTag(100, value.rJoinActionMsg)
          + RJoinNoticeMsg.ADAPTER.encodedSizeWithTag(101, value.rJoinNoticeMsg)
          + RExitGameMsg.ADAPTER.encodedSizeWithTag(102, value.rExitGameMsg)
          + RBLightMsg.ADAPTER.encodedSizeWithTag(103, value.rBLightMsg)
          + RWantSingChanceMsg.ADAPTER.encodedSizeWithTag(104, value.rWantSingChanceMsg)
          + RGetSingChanceMsg.ADAPTER.encodedSizeWithTag(105, value.rGetSingChanceMsg)
          + RSyncStatusMsg.ADAPTER.encodedSizeWithTag(106, value.rSyncStatusMsg)
          + RaceRoundOverMsg.ADAPTER.encodedSizeWithTag(107, value.rRoundOverMsg)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RaceRoomMsg value) throws IOException {
      ProtoAdapter.SINT64.encodeWithTag(writer, 1, value.timeMs);
      ERaceRoomMsgType.ADAPTER.encodeWithTag(writer, 2, value.msgType);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.roomID);
      RJoinActionMsg.ADAPTER.encodeWithTag(writer, 100, value.rJoinActionMsg);
      RJoinNoticeMsg.ADAPTER.encodeWithTag(writer, 101, value.rJoinNoticeMsg);
      RExitGameMsg.ADAPTER.encodeWithTag(writer, 102, value.rExitGameMsg);
      RBLightMsg.ADAPTER.encodeWithTag(writer, 103, value.rBLightMsg);
      RWantSingChanceMsg.ADAPTER.encodeWithTag(writer, 104, value.rWantSingChanceMsg);
      RGetSingChanceMsg.ADAPTER.encodeWithTag(writer, 105, value.rGetSingChanceMsg);
      RSyncStatusMsg.ADAPTER.encodeWithTag(writer, 106, value.rSyncStatusMsg);
      RaceRoundOverMsg.ADAPTER.encodeWithTag(writer, 107, value.rRoundOverMsg);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RaceRoomMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 2: {
            try {
              builder.setMsgType(ERaceRoomMsgType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 3: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 100: builder.setRJoinActionMsg(RJoinActionMsg.ADAPTER.decode(reader)); break;
          case 101: builder.setRJoinNoticeMsg(RJoinNoticeMsg.ADAPTER.decode(reader)); break;
          case 102: builder.setRExitGameMsg(RExitGameMsg.ADAPTER.decode(reader)); break;
          case 103: builder.setRBLightMsg(RBLightMsg.ADAPTER.decode(reader)); break;
          case 104: builder.setRWantSingChanceMsg(RWantSingChanceMsg.ADAPTER.decode(reader)); break;
          case 105: builder.setRGetSingChanceMsg(RGetSingChanceMsg.ADAPTER.decode(reader)); break;
          case 106: builder.setRSyncStatusMsg(RSyncStatusMsg.ADAPTER.decode(reader)); break;
          case 107: builder.setRRoundOverMsg(RaceRoundOverMsg.ADAPTER.decode(reader)); break;
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
    public RaceRoomMsg redact(RaceRoomMsg value) {
      Builder builder = value.newBuilder();
      if (builder.rJoinActionMsg != null) builder.rJoinActionMsg = RJoinActionMsg.ADAPTER.redact(builder.rJoinActionMsg);
      if (builder.rJoinNoticeMsg != null) builder.rJoinNoticeMsg = RJoinNoticeMsg.ADAPTER.redact(builder.rJoinNoticeMsg);
      if (builder.rExitGameMsg != null) builder.rExitGameMsg = RExitGameMsg.ADAPTER.redact(builder.rExitGameMsg);
      if (builder.rBLightMsg != null) builder.rBLightMsg = RBLightMsg.ADAPTER.redact(builder.rBLightMsg);
      if (builder.rWantSingChanceMsg != null) builder.rWantSingChanceMsg = RWantSingChanceMsg.ADAPTER.redact(builder.rWantSingChanceMsg);
      if (builder.rGetSingChanceMsg != null) builder.rGetSingChanceMsg = RGetSingChanceMsg.ADAPTER.redact(builder.rGetSingChanceMsg);
      if (builder.rSyncStatusMsg != null) builder.rSyncStatusMsg = RSyncStatusMsg.ADAPTER.redact(builder.rSyncStatusMsg);
      if (builder.rRoundOverMsg != null) builder.rRoundOverMsg = RaceRoundOverMsg.ADAPTER.redact(builder.rRoundOverMsg);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}