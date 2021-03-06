// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: battle_room.proto
package com.zq.live.proto.BattleRoom;

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
 * --proto_path=./proto --java_out=./Module/PlayWays/src/main/java-gen-pb/ battle_room.proto
 * 团战房信令消息
 */
public final class BattleRoomMsg extends Message<BattleRoomMsg, BattleRoomMsg.Builder> {
  public static final ProtoAdapter<BattleRoomMsg> ADAPTER = new ProtoAdapter_BattleRoomMsg();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_TIMEMS = 0L;

  public static final EBattleRoomMsgType DEFAULT_MSGTYPE = EBattleRoomMsgType.BRT_UNKNOWN;

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
      adapter = "com.zq.live.proto.BattleRoom.EBattleRoomMsgType#ADAPTER"
  )
  private final EBattleRoomMsgType msgType;

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
      adapter = "com.zq.live.proto.BattleRoom.BUserEnterMsg#ADAPTER"
  )
  private final BUserEnterMsg bUserEnterMsg;

  /**
   * 轮次进入演唱
   */
  @WireField(
      tag = 11,
      adapter = "com.zq.live.proto.BattleRoom.BSingRoundMsg#ADAPTER"
  )
  private final BSingRoundMsg bSingRoundMsg;

  /**
   * 轮次切换
   */
  @WireField(
      tag = 12,
      adapter = "com.zq.live.proto.BattleRoom.BNextRoundMsg#ADAPTER"
  )
  private final BNextRoundMsg bNextRoundMsg;

  /**
   * 同步状态
   */
  @WireField(
      tag = 13,
      adapter = "com.zq.live.proto.BattleRoom.BSyncMsg#ADAPTER"
  )
  private final BSyncMsg bSyncMsg;

  /**
   * 结束房间
   */
  @WireField(
      tag = 14,
      adapter = "com.zq.live.proto.BattleRoom.BGameOverMsg#ADAPTER"
  )
  private final BGameOverMsg bGameOverMsg;

  public BattleRoomMsg(Long timeMs, EBattleRoomMsgType msgType, Integer roomID,
      BUserEnterMsg bUserEnterMsg, BSingRoundMsg bSingRoundMsg, BNextRoundMsg bNextRoundMsg,
      BSyncMsg bSyncMsg, BGameOverMsg bGameOverMsg) {
    this(timeMs, msgType, roomID, bUserEnterMsg, bSingRoundMsg, bNextRoundMsg, bSyncMsg, bGameOverMsg, ByteString.EMPTY);
  }

  public BattleRoomMsg(Long timeMs, EBattleRoomMsgType msgType, Integer roomID,
      BUserEnterMsg bUserEnterMsg, BSingRoundMsg bSingRoundMsg, BNextRoundMsg bNextRoundMsg,
      BSyncMsg bSyncMsg, BGameOverMsg bGameOverMsg, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.timeMs = timeMs;
    this.msgType = msgType;
    this.roomID = roomID;
    this.bUserEnterMsg = bUserEnterMsg;
    this.bSingRoundMsg = bSingRoundMsg;
    this.bNextRoundMsg = bNextRoundMsg;
    this.bSyncMsg = bSyncMsg;
    this.bGameOverMsg = bGameOverMsg;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.timeMs = timeMs;
    builder.msgType = msgType;
    builder.roomID = roomID;
    builder.bUserEnterMsg = bUserEnterMsg;
    builder.bSingRoundMsg = bSingRoundMsg;
    builder.bNextRoundMsg = bNextRoundMsg;
    builder.bSyncMsg = bSyncMsg;
    builder.bGameOverMsg = bGameOverMsg;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof BattleRoomMsg)) return false;
    BattleRoomMsg o = (BattleRoomMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(timeMs, o.timeMs)
        && Internal.equals(msgType, o.msgType)
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(bUserEnterMsg, o.bUserEnterMsg)
        && Internal.equals(bSingRoundMsg, o.bSingRoundMsg)
        && Internal.equals(bNextRoundMsg, o.bNextRoundMsg)
        && Internal.equals(bSyncMsg, o.bSyncMsg)
        && Internal.equals(bGameOverMsg, o.bGameOverMsg);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (timeMs != null ? timeMs.hashCode() : 0);
      result = result * 37 + (msgType != null ? msgType.hashCode() : 0);
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (bUserEnterMsg != null ? bUserEnterMsg.hashCode() : 0);
      result = result * 37 + (bSingRoundMsg != null ? bSingRoundMsg.hashCode() : 0);
      result = result * 37 + (bNextRoundMsg != null ? bNextRoundMsg.hashCode() : 0);
      result = result * 37 + (bSyncMsg != null ? bSyncMsg.hashCode() : 0);
      result = result * 37 + (bGameOverMsg != null ? bGameOverMsg.hashCode() : 0);
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
    if (bUserEnterMsg != null) builder.append(", bUserEnterMsg=").append(bUserEnterMsg);
    if (bSingRoundMsg != null) builder.append(", bSingRoundMsg=").append(bSingRoundMsg);
    if (bNextRoundMsg != null) builder.append(", bNextRoundMsg=").append(bNextRoundMsg);
    if (bSyncMsg != null) builder.append(", bSyncMsg=").append(bSyncMsg);
    if (bGameOverMsg != null) builder.append(", bGameOverMsg=").append(bGameOverMsg);
    return builder.replace(0, 2, "BattleRoomMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return BattleRoomMsg.ADAPTER.encode(this);
  }

  public static final BattleRoomMsg parseFrom(byte[] data) throws IOException {
    BattleRoomMsg c = null;
       c = BattleRoomMsg.ADAPTER.decode(data);
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
  public EBattleRoomMsgType getMsgType() {
    if(msgType==null){
        return new EBattleRoomMsgType.Builder().build();
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
  public BUserEnterMsg getBUserEnterMsg() {
    if(bUserEnterMsg==null){
        return new BUserEnterMsg.Builder().build();
    }
    return bUserEnterMsg;
  }

  /**
   * 轮次进入演唱
   */
  public BSingRoundMsg getBSingRoundMsg() {
    if(bSingRoundMsg==null){
        return new BSingRoundMsg.Builder().build();
    }
    return bSingRoundMsg;
  }

  /**
   * 轮次切换
   */
  public BNextRoundMsg getBNextRoundMsg() {
    if(bNextRoundMsg==null){
        return new BNextRoundMsg.Builder().build();
    }
    return bNextRoundMsg;
  }

  /**
   * 同步状态
   */
  public BSyncMsg getBSyncMsg() {
    if(bSyncMsg==null){
        return new BSyncMsg.Builder().build();
    }
    return bSyncMsg;
  }

  /**
   * 结束房间
   */
  public BGameOverMsg getBGameOverMsg() {
    if(bGameOverMsg==null){
        return new BGameOverMsg.Builder().build();
    }
    return bGameOverMsg;
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
  public boolean hasBUserEnterMsg() {
    return bUserEnterMsg!=null;
  }

  /**
   * 轮次进入演唱
   */
  public boolean hasBSingRoundMsg() {
    return bSingRoundMsg!=null;
  }

  /**
   * 轮次切换
   */
  public boolean hasBNextRoundMsg() {
    return bNextRoundMsg!=null;
  }

  /**
   * 同步状态
   */
  public boolean hasBSyncMsg() {
    return bSyncMsg!=null;
  }

  /**
   * 结束房间
   */
  public boolean hasBGameOverMsg() {
    return bGameOverMsg!=null;
  }

  public static final class Builder extends Message.Builder<BattleRoomMsg, Builder> {
    private Long timeMs;

    private EBattleRoomMsgType msgType;

    private Integer roomID;

    private BUserEnterMsg bUserEnterMsg;

    private BSingRoundMsg bSingRoundMsg;

    private BNextRoundMsg bNextRoundMsg;

    private BSyncMsg bSyncMsg;

    private BGameOverMsg bGameOverMsg;

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
    public Builder setMsgType(EBattleRoomMsgType msgType) {
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
    public Builder setBUserEnterMsg(BUserEnterMsg bUserEnterMsg) {
      this.bUserEnterMsg = bUserEnterMsg;
      return this;
    }

    /**
     * 轮次进入演唱
     */
    public Builder setBSingRoundMsg(BSingRoundMsg bSingRoundMsg) {
      this.bSingRoundMsg = bSingRoundMsg;
      return this;
    }

    /**
     * 轮次切换
     */
    public Builder setBNextRoundMsg(BNextRoundMsg bNextRoundMsg) {
      this.bNextRoundMsg = bNextRoundMsg;
      return this;
    }

    /**
     * 同步状态
     */
    public Builder setBSyncMsg(BSyncMsg bSyncMsg) {
      this.bSyncMsg = bSyncMsg;
      return this;
    }

    /**
     * 结束房间
     */
    public Builder setBGameOverMsg(BGameOverMsg bGameOverMsg) {
      this.bGameOverMsg = bGameOverMsg;
      return this;
    }

    @Override
    public BattleRoomMsg build() {
      return new BattleRoomMsg(timeMs, msgType, roomID, bUserEnterMsg, bSingRoundMsg, bNextRoundMsg, bSyncMsg, bGameOverMsg, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_BattleRoomMsg extends ProtoAdapter<BattleRoomMsg> {
    public ProtoAdapter_BattleRoomMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, BattleRoomMsg.class);
    }

    @Override
    public int encodedSize(BattleRoomMsg value) {
      return ProtoAdapter.SINT64.encodedSizeWithTag(1, value.timeMs)
          + EBattleRoomMsgType.ADAPTER.encodedSizeWithTag(2, value.msgType)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.roomID)
          + BUserEnterMsg.ADAPTER.encodedSizeWithTag(10, value.bUserEnterMsg)
          + BSingRoundMsg.ADAPTER.encodedSizeWithTag(11, value.bSingRoundMsg)
          + BNextRoundMsg.ADAPTER.encodedSizeWithTag(12, value.bNextRoundMsg)
          + BSyncMsg.ADAPTER.encodedSizeWithTag(13, value.bSyncMsg)
          + BGameOverMsg.ADAPTER.encodedSizeWithTag(14, value.bGameOverMsg)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, BattleRoomMsg value) throws IOException {
      ProtoAdapter.SINT64.encodeWithTag(writer, 1, value.timeMs);
      EBattleRoomMsgType.ADAPTER.encodeWithTag(writer, 2, value.msgType);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.roomID);
      BUserEnterMsg.ADAPTER.encodeWithTag(writer, 10, value.bUserEnterMsg);
      BSingRoundMsg.ADAPTER.encodeWithTag(writer, 11, value.bSingRoundMsg);
      BNextRoundMsg.ADAPTER.encodeWithTag(writer, 12, value.bNextRoundMsg);
      BSyncMsg.ADAPTER.encodeWithTag(writer, 13, value.bSyncMsg);
      BGameOverMsg.ADAPTER.encodeWithTag(writer, 14, value.bGameOverMsg);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public BattleRoomMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 2: {
            try {
              builder.setMsgType(EBattleRoomMsgType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 3: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 10: builder.setBUserEnterMsg(BUserEnterMsg.ADAPTER.decode(reader)); break;
          case 11: builder.setBSingRoundMsg(BSingRoundMsg.ADAPTER.decode(reader)); break;
          case 12: builder.setBNextRoundMsg(BNextRoundMsg.ADAPTER.decode(reader)); break;
          case 13: builder.setBSyncMsg(BSyncMsg.ADAPTER.decode(reader)); break;
          case 14: builder.setBGameOverMsg(BGameOverMsg.ADAPTER.decode(reader)); break;
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
    public BattleRoomMsg redact(BattleRoomMsg value) {
      Builder builder = value.newBuilder();
      if (builder.bUserEnterMsg != null) builder.bUserEnterMsg = BUserEnterMsg.ADAPTER.redact(builder.bUserEnterMsg);
      if (builder.bSingRoundMsg != null) builder.bSingRoundMsg = BSingRoundMsg.ADAPTER.redact(builder.bSingRoundMsg);
      if (builder.bNextRoundMsg != null) builder.bNextRoundMsg = BNextRoundMsg.ADAPTER.redact(builder.bNextRoundMsg);
      if (builder.bSyncMsg != null) builder.bSyncMsg = BSyncMsg.ADAPTER.redact(builder.bSyncMsg);
      if (builder.bGameOverMsg != null) builder.bGameOverMsg = BGameOverMsg.ADAPTER.redact(builder.bGameOverMsg);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
