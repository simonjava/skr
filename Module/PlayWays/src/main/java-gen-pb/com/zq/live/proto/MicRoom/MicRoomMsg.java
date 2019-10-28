// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: mic_room.proto
package com.zq.live.proto.MicRoom;

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
 * --proto_path=./proto --java_out=./Module/PlayWays/src/main/java-gen-pb/ mic_room.proto
 * 排麦房信令消息
 */
public final class MicRoomMsg extends Message<MicRoomMsg, MicRoomMsg.Builder> {
  public static final ProtoAdapter<MicRoomMsg> ADAPTER = new ProtoAdapter_MicRoomMsg();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_TIMEMS = 0L;

  public static final EMicRoomMsgType DEFAULT_MSGTYPE = EMicRoomMsgType.RMT_UNKNOWN;

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
      adapter = "com.zq.live.proto.MicRoom.EMicRoomMsgType#ADAPTER"
  )
  private final EMicRoomMsgType msgType;

  /**
   * 房间ID
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  /**
   * 加入游戏指令消息
   */
  @WireField(
      tag = 100,
      adapter = "com.zq.live.proto.MicRoom.MJoinActionMsg#ADAPTER"
  )
  private final MJoinActionMsg mJoinActionMsg;

  /**
   * 加入游戏通知消息
   */
  @WireField(
      tag = 101,
      adapter = "com.zq.live.proto.MicRoom.MJoinNoticeMsg#ADAPTER"
  )
  private final MJoinNoticeMsg mJoinNoticeMsg;

  /**
   * 退出游戏
   */
  @WireField(
      tag = 102,
      adapter = "com.zq.live.proto.MicRoom.MExitGameMsg#ADAPTER"
  )
  private final MExitGameMsg mExitGameMsg;

  /**
   * 同步状态
   */
  @WireField(
      tag = 103,
      adapter = "com.zq.live.proto.MicRoom.MSyncStatusMsg#ADAPTER"
  )
  private final MSyncStatusMsg syncStatusMsg;

  /**
   * 轮次结束
   */
  @WireField(
      tag = 104,
      adapter = "com.zq.live.proto.MicRoom.MRoundOverMsg#ADAPTER"
  )
  private final MRoundOverMsg mRoundOverMsg;

  /**
   * 点歌
   */
  @WireField(
      tag = 105,
      adapter = "com.zq.live.proto.MicRoom.MAddMusicMsg#ADAPTER"
  )
  private final MAddMusicMsg mAddMusicMsg;

  /**
   * 删歌
   */
  @WireField(
      tag = 106,
      adapter = "com.zq.live.proto.MicRoom.MDelMusicMsg#ADAPTER"
  )
  private final MDelMusicMsg mDelMusicMsg;

  /**
   * 置顶歌曲
   */
  @WireField(
      tag = 107,
      adapter = "com.zq.live.proto.MicRoom.MUpMusicMsg#ADAPTER"
  )
  private final MUpMusicMsg mUpMusicMsg;

  /**
   * 取消歌曲：合唱/PK伙伴已退出
   */
  @WireField(
      tag = 108,
      adapter = "com.zq.live.proto.MicRoom.MCancelMusic#ADAPTER"
  )
  private final MCancelMusic mCancelMusic;

  /**
   * 请求合唱/PK
   */
  @WireField(
      tag = 109,
      adapter = "com.zq.live.proto.MicRoom.MReqAddMusicMsg#ADAPTER"
  )
  private final MReqAddMusicMsg mReqAddMusicMsg;

  /**
   * 合唱模式放弃演唱
   */
  @WireField(
      tag = 110,
      adapter = "com.zq.live.proto.MicRoom.MCHOGiveUpMsg#ADAPTER"
  )
  private final MCHOGiveUpMsg mCHOGiveUpMsg;

  /**
   * spk模式内部轮次结束
   */
  @WireField(
      tag = 111,
      adapter = "com.zq.live.proto.MicRoom.MSPKInnerRoundOverMsg#ADAPTER"
  )
  private final MSPKInnerRoundOverMsg mSPKInnerRoundOverMsg;

  /**
   * 修改房间名字
   */
  @WireField(
      tag = 112,
      adapter = "com.zq.live.proto.MicRoom.MChangeRoomNameMsg#ADAPTER"
  )
  private final MChangeRoomNameMsg mChangeRoomNameMsg;

  /**
   * 修改房间等级限制
   */
  @WireField(
      tag = 113,
      adapter = "com.zq.live.proto.MicRoom.MChangeRoomLevelLimitMsg#ADAPTER"
  )
  private final MChangeRoomLevelLimitMsg mChangeRoomLevelLimitMsg;

  /**
   * 踢人
   */
  @WireField(
      tag = 114,
      adapter = "com.zq.live.proto.MicRoom.MKickoutUserMsg#ADAPTER"
  )
  private final MKickoutUserMsg mKickoutUserMsg;

  /**
   * 修改房主
   */
  @WireField(
      tag = 115,
      adapter = "com.zq.live.proto.MicRoom.MChangeRoomOwnerMsg#ADAPTER"
  )
  private final MChangeRoomOwnerMsg mChangeRoomOwnerMsg;

  /**
   * 房间匹配开关
   */
  @WireField(
      tag = 116,
      adapter = "com.zq.live.proto.MicRoom.MMatchStatusMsg#ADAPTER"
  )
  private final MMatchStatusMsg mMatchStatusMsg;

  /**
   * 实时机器评分 msgType == RM_ROUND_MACHINE_SCORE
   */
  @WireField(
      tag = 117,
      adapter = "com.zq.live.proto.MicRoom.MMachineScore#ADAPTER"
  )
  private final MMachineScore machineScore;

  public MicRoomMsg(Long timeMs, EMicRoomMsgType msgType, Integer roomID,
      MJoinActionMsg mJoinActionMsg, MJoinNoticeMsg mJoinNoticeMsg, MExitGameMsg mExitGameMsg,
      MSyncStatusMsg syncStatusMsg, MRoundOverMsg mRoundOverMsg, MAddMusicMsg mAddMusicMsg,
      MDelMusicMsg mDelMusicMsg, MUpMusicMsg mUpMusicMsg, MCancelMusic mCancelMusic,
      MReqAddMusicMsg mReqAddMusicMsg, MCHOGiveUpMsg mCHOGiveUpMsg,
      MSPKInnerRoundOverMsg mSPKInnerRoundOverMsg, MChangeRoomNameMsg mChangeRoomNameMsg,
      MChangeRoomLevelLimitMsg mChangeRoomLevelLimitMsg, MKickoutUserMsg mKickoutUserMsg,
      MChangeRoomOwnerMsg mChangeRoomOwnerMsg, MMatchStatusMsg mMatchStatusMsg,
      MMachineScore machineScore) {
    this(timeMs, msgType, roomID, mJoinActionMsg, mJoinNoticeMsg, mExitGameMsg, syncStatusMsg, mRoundOverMsg, mAddMusicMsg, mDelMusicMsg, mUpMusicMsg, mCancelMusic, mReqAddMusicMsg, mCHOGiveUpMsg, mSPKInnerRoundOverMsg, mChangeRoomNameMsg, mChangeRoomLevelLimitMsg, mKickoutUserMsg, mChangeRoomOwnerMsg, mMatchStatusMsg, machineScore, ByteString.EMPTY);
  }

  public MicRoomMsg(Long timeMs, EMicRoomMsgType msgType, Integer roomID,
      MJoinActionMsg mJoinActionMsg, MJoinNoticeMsg mJoinNoticeMsg, MExitGameMsg mExitGameMsg,
      MSyncStatusMsg syncStatusMsg, MRoundOverMsg mRoundOverMsg, MAddMusicMsg mAddMusicMsg,
      MDelMusicMsg mDelMusicMsg, MUpMusicMsg mUpMusicMsg, MCancelMusic mCancelMusic,
      MReqAddMusicMsg mReqAddMusicMsg, MCHOGiveUpMsg mCHOGiveUpMsg,
      MSPKInnerRoundOverMsg mSPKInnerRoundOverMsg, MChangeRoomNameMsg mChangeRoomNameMsg,
      MChangeRoomLevelLimitMsg mChangeRoomLevelLimitMsg, MKickoutUserMsg mKickoutUserMsg,
      MChangeRoomOwnerMsg mChangeRoomOwnerMsg, MMatchStatusMsg mMatchStatusMsg,
      MMachineScore machineScore, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.timeMs = timeMs;
    this.msgType = msgType;
    this.roomID = roomID;
    this.mJoinActionMsg = mJoinActionMsg;
    this.mJoinNoticeMsg = mJoinNoticeMsg;
    this.mExitGameMsg = mExitGameMsg;
    this.syncStatusMsg = syncStatusMsg;
    this.mRoundOverMsg = mRoundOverMsg;
    this.mAddMusicMsg = mAddMusicMsg;
    this.mDelMusicMsg = mDelMusicMsg;
    this.mUpMusicMsg = mUpMusicMsg;
    this.mCancelMusic = mCancelMusic;
    this.mReqAddMusicMsg = mReqAddMusicMsg;
    this.mCHOGiveUpMsg = mCHOGiveUpMsg;
    this.mSPKInnerRoundOverMsg = mSPKInnerRoundOverMsg;
    this.mChangeRoomNameMsg = mChangeRoomNameMsg;
    this.mChangeRoomLevelLimitMsg = mChangeRoomLevelLimitMsg;
    this.mKickoutUserMsg = mKickoutUserMsg;
    this.mChangeRoomOwnerMsg = mChangeRoomOwnerMsg;
    this.mMatchStatusMsg = mMatchStatusMsg;
    this.machineScore = machineScore;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.timeMs = timeMs;
    builder.msgType = msgType;
    builder.roomID = roomID;
    builder.mJoinActionMsg = mJoinActionMsg;
    builder.mJoinNoticeMsg = mJoinNoticeMsg;
    builder.mExitGameMsg = mExitGameMsg;
    builder.syncStatusMsg = syncStatusMsg;
    builder.mRoundOverMsg = mRoundOverMsg;
    builder.mAddMusicMsg = mAddMusicMsg;
    builder.mDelMusicMsg = mDelMusicMsg;
    builder.mUpMusicMsg = mUpMusicMsg;
    builder.mCancelMusic = mCancelMusic;
    builder.mReqAddMusicMsg = mReqAddMusicMsg;
    builder.mCHOGiveUpMsg = mCHOGiveUpMsg;
    builder.mSPKInnerRoundOverMsg = mSPKInnerRoundOverMsg;
    builder.mChangeRoomNameMsg = mChangeRoomNameMsg;
    builder.mChangeRoomLevelLimitMsg = mChangeRoomLevelLimitMsg;
    builder.mKickoutUserMsg = mKickoutUserMsg;
    builder.mChangeRoomOwnerMsg = mChangeRoomOwnerMsg;
    builder.mMatchStatusMsg = mMatchStatusMsg;
    builder.machineScore = machineScore;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MicRoomMsg)) return false;
    MicRoomMsg o = (MicRoomMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(timeMs, o.timeMs)
        && Internal.equals(msgType, o.msgType)
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(mJoinActionMsg, o.mJoinActionMsg)
        && Internal.equals(mJoinNoticeMsg, o.mJoinNoticeMsg)
        && Internal.equals(mExitGameMsg, o.mExitGameMsg)
        && Internal.equals(syncStatusMsg, o.syncStatusMsg)
        && Internal.equals(mRoundOverMsg, o.mRoundOverMsg)
        && Internal.equals(mAddMusicMsg, o.mAddMusicMsg)
        && Internal.equals(mDelMusicMsg, o.mDelMusicMsg)
        && Internal.equals(mUpMusicMsg, o.mUpMusicMsg)
        && Internal.equals(mCancelMusic, o.mCancelMusic)
        && Internal.equals(mReqAddMusicMsg, o.mReqAddMusicMsg)
        && Internal.equals(mCHOGiveUpMsg, o.mCHOGiveUpMsg)
        && Internal.equals(mSPKInnerRoundOverMsg, o.mSPKInnerRoundOverMsg)
        && Internal.equals(mChangeRoomNameMsg, o.mChangeRoomNameMsg)
        && Internal.equals(mChangeRoomLevelLimitMsg, o.mChangeRoomLevelLimitMsg)
        && Internal.equals(mKickoutUserMsg, o.mKickoutUserMsg)
        && Internal.equals(mChangeRoomOwnerMsg, o.mChangeRoomOwnerMsg)
        && Internal.equals(mMatchStatusMsg, o.mMatchStatusMsg)
        && Internal.equals(machineScore, o.machineScore);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (timeMs != null ? timeMs.hashCode() : 0);
      result = result * 37 + (msgType != null ? msgType.hashCode() : 0);
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (mJoinActionMsg != null ? mJoinActionMsg.hashCode() : 0);
      result = result * 37 + (mJoinNoticeMsg != null ? mJoinNoticeMsg.hashCode() : 0);
      result = result * 37 + (mExitGameMsg != null ? mExitGameMsg.hashCode() : 0);
      result = result * 37 + (syncStatusMsg != null ? syncStatusMsg.hashCode() : 0);
      result = result * 37 + (mRoundOverMsg != null ? mRoundOverMsg.hashCode() : 0);
      result = result * 37 + (mAddMusicMsg != null ? mAddMusicMsg.hashCode() : 0);
      result = result * 37 + (mDelMusicMsg != null ? mDelMusicMsg.hashCode() : 0);
      result = result * 37 + (mUpMusicMsg != null ? mUpMusicMsg.hashCode() : 0);
      result = result * 37 + (mCancelMusic != null ? mCancelMusic.hashCode() : 0);
      result = result * 37 + (mReqAddMusicMsg != null ? mReqAddMusicMsg.hashCode() : 0);
      result = result * 37 + (mCHOGiveUpMsg != null ? mCHOGiveUpMsg.hashCode() : 0);
      result = result * 37 + (mSPKInnerRoundOverMsg != null ? mSPKInnerRoundOverMsg.hashCode() : 0);
      result = result * 37 + (mChangeRoomNameMsg != null ? mChangeRoomNameMsg.hashCode() : 0);
      result = result * 37 + (mChangeRoomLevelLimitMsg != null ? mChangeRoomLevelLimitMsg.hashCode() : 0);
      result = result * 37 + (mKickoutUserMsg != null ? mKickoutUserMsg.hashCode() : 0);
      result = result * 37 + (mChangeRoomOwnerMsg != null ? mChangeRoomOwnerMsg.hashCode() : 0);
      result = result * 37 + (mMatchStatusMsg != null ? mMatchStatusMsg.hashCode() : 0);
      result = result * 37 + (machineScore != null ? machineScore.hashCode() : 0);
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
    if (mJoinActionMsg != null) builder.append(", mJoinActionMsg=").append(mJoinActionMsg);
    if (mJoinNoticeMsg != null) builder.append(", mJoinNoticeMsg=").append(mJoinNoticeMsg);
    if (mExitGameMsg != null) builder.append(", mExitGameMsg=").append(mExitGameMsg);
    if (syncStatusMsg != null) builder.append(", syncStatusMsg=").append(syncStatusMsg);
    if (mRoundOverMsg != null) builder.append(", mRoundOverMsg=").append(mRoundOverMsg);
    if (mAddMusicMsg != null) builder.append(", mAddMusicMsg=").append(mAddMusicMsg);
    if (mDelMusicMsg != null) builder.append(", mDelMusicMsg=").append(mDelMusicMsg);
    if (mUpMusicMsg != null) builder.append(", mUpMusicMsg=").append(mUpMusicMsg);
    if (mCancelMusic != null) builder.append(", mCancelMusic=").append(mCancelMusic);
    if (mReqAddMusicMsg != null) builder.append(", mReqAddMusicMsg=").append(mReqAddMusicMsg);
    if (mCHOGiveUpMsg != null) builder.append(", mCHOGiveUpMsg=").append(mCHOGiveUpMsg);
    if (mSPKInnerRoundOverMsg != null) builder.append(", mSPKInnerRoundOverMsg=").append(mSPKInnerRoundOverMsg);
    if (mChangeRoomNameMsg != null) builder.append(", mChangeRoomNameMsg=").append(mChangeRoomNameMsg);
    if (mChangeRoomLevelLimitMsg != null) builder.append(", mChangeRoomLevelLimitMsg=").append(mChangeRoomLevelLimitMsg);
    if (mKickoutUserMsg != null) builder.append(", mKickoutUserMsg=").append(mKickoutUserMsg);
    if (mChangeRoomOwnerMsg != null) builder.append(", mChangeRoomOwnerMsg=").append(mChangeRoomOwnerMsg);
    if (mMatchStatusMsg != null) builder.append(", mMatchStatusMsg=").append(mMatchStatusMsg);
    if (machineScore != null) builder.append(", machineScore=").append(machineScore);
    return builder.replace(0, 2, "MicRoomMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MicRoomMsg.ADAPTER.encode(this);
  }

  public static final MicRoomMsg parseFrom(byte[] data) throws IOException {
    MicRoomMsg c = null;
       c = MicRoomMsg.ADAPTER.decode(data);
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
  public EMicRoomMsgType getMsgType() {
    if(msgType==null){
        return new EMicRoomMsgType.Builder().build();
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
   * 加入游戏指令消息
   */
  public MJoinActionMsg getMJoinActionMsg() {
    if(mJoinActionMsg==null){
        return new MJoinActionMsg.Builder().build();
    }
    return mJoinActionMsg;
  }

  /**
   * 加入游戏通知消息
   */
  public MJoinNoticeMsg getMJoinNoticeMsg() {
    if(mJoinNoticeMsg==null){
        return new MJoinNoticeMsg.Builder().build();
    }
    return mJoinNoticeMsg;
  }

  /**
   * 退出游戏
   */
  public MExitGameMsg getMExitGameMsg() {
    if(mExitGameMsg==null){
        return new MExitGameMsg.Builder().build();
    }
    return mExitGameMsg;
  }

  /**
   * 同步状态
   */
  public MSyncStatusMsg getSyncStatusMsg() {
    if(syncStatusMsg==null){
        return new MSyncStatusMsg.Builder().build();
    }
    return syncStatusMsg;
  }

  /**
   * 轮次结束
   */
  public MRoundOverMsg getMRoundOverMsg() {
    if(mRoundOverMsg==null){
        return new MRoundOverMsg.Builder().build();
    }
    return mRoundOverMsg;
  }

  /**
   * 点歌
   */
  public MAddMusicMsg getMAddMusicMsg() {
    if(mAddMusicMsg==null){
        return new MAddMusicMsg.Builder().build();
    }
    return mAddMusicMsg;
  }

  /**
   * 删歌
   */
  public MDelMusicMsg getMDelMusicMsg() {
    if(mDelMusicMsg==null){
        return new MDelMusicMsg.Builder().build();
    }
    return mDelMusicMsg;
  }

  /**
   * 置顶歌曲
   */
  public MUpMusicMsg getMUpMusicMsg() {
    if(mUpMusicMsg==null){
        return new MUpMusicMsg.Builder().build();
    }
    return mUpMusicMsg;
  }

  /**
   * 取消歌曲：合唱/PK伙伴已退出
   */
  public MCancelMusic getMCancelMusic() {
    if(mCancelMusic==null){
        return new MCancelMusic.Builder().build();
    }
    return mCancelMusic;
  }

  /**
   * 请求合唱/PK
   */
  public MReqAddMusicMsg getMReqAddMusicMsg() {
    if(mReqAddMusicMsg==null){
        return new MReqAddMusicMsg.Builder().build();
    }
    return mReqAddMusicMsg;
  }

  /**
   * 合唱模式放弃演唱
   */
  public MCHOGiveUpMsg getMCHOGiveUpMsg() {
    if(mCHOGiveUpMsg==null){
        return new MCHOGiveUpMsg.Builder().build();
    }
    return mCHOGiveUpMsg;
  }

  /**
   * spk模式内部轮次结束
   */
  public MSPKInnerRoundOverMsg getMSPKInnerRoundOverMsg() {
    if(mSPKInnerRoundOverMsg==null){
        return new MSPKInnerRoundOverMsg.Builder().build();
    }
    return mSPKInnerRoundOverMsg;
  }

  /**
   * 修改房间名字
   */
  public MChangeRoomNameMsg getMChangeRoomNameMsg() {
    if(mChangeRoomNameMsg==null){
        return new MChangeRoomNameMsg.Builder().build();
    }
    return mChangeRoomNameMsg;
  }

  /**
   * 修改房间等级限制
   */
  public MChangeRoomLevelLimitMsg getMChangeRoomLevelLimitMsg() {
    if(mChangeRoomLevelLimitMsg==null){
        return new MChangeRoomLevelLimitMsg.Builder().build();
    }
    return mChangeRoomLevelLimitMsg;
  }

  /**
   * 踢人
   */
  public MKickoutUserMsg getMKickoutUserMsg() {
    if(mKickoutUserMsg==null){
        return new MKickoutUserMsg.Builder().build();
    }
    return mKickoutUserMsg;
  }

  /**
   * 修改房主
   */
  public MChangeRoomOwnerMsg getMChangeRoomOwnerMsg() {
    if(mChangeRoomOwnerMsg==null){
        return new MChangeRoomOwnerMsg.Builder().build();
    }
    return mChangeRoomOwnerMsg;
  }

  /**
   * 房间匹配开关
   */
  public MMatchStatusMsg getMMatchStatusMsg() {
    if(mMatchStatusMsg==null){
        return new MMatchStatusMsg.Builder().build();
    }
    return mMatchStatusMsg;
  }

  /**
   * 实时机器评分 msgType == RM_ROUND_MACHINE_SCORE
   */
  public MMachineScore getMachineScore() {
    if(machineScore==null){
        return new MMachineScore.Builder().build();
    }
    return machineScore;
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
   * 加入游戏指令消息
   */
  public boolean hasMJoinActionMsg() {
    return mJoinActionMsg!=null;
  }

  /**
   * 加入游戏通知消息
   */
  public boolean hasMJoinNoticeMsg() {
    return mJoinNoticeMsg!=null;
  }

  /**
   * 退出游戏
   */
  public boolean hasMExitGameMsg() {
    return mExitGameMsg!=null;
  }

  /**
   * 同步状态
   */
  public boolean hasSyncStatusMsg() {
    return syncStatusMsg!=null;
  }

  /**
   * 轮次结束
   */
  public boolean hasMRoundOverMsg() {
    return mRoundOverMsg!=null;
  }

  /**
   * 点歌
   */
  public boolean hasMAddMusicMsg() {
    return mAddMusicMsg!=null;
  }

  /**
   * 删歌
   */
  public boolean hasMDelMusicMsg() {
    return mDelMusicMsg!=null;
  }

  /**
   * 置顶歌曲
   */
  public boolean hasMUpMusicMsg() {
    return mUpMusicMsg!=null;
  }

  /**
   * 取消歌曲：合唱/PK伙伴已退出
   */
  public boolean hasMCancelMusic() {
    return mCancelMusic!=null;
  }

  /**
   * 请求合唱/PK
   */
  public boolean hasMReqAddMusicMsg() {
    return mReqAddMusicMsg!=null;
  }

  /**
   * 合唱模式放弃演唱
   */
  public boolean hasMCHOGiveUpMsg() {
    return mCHOGiveUpMsg!=null;
  }

  /**
   * spk模式内部轮次结束
   */
  public boolean hasMSPKInnerRoundOverMsg() {
    return mSPKInnerRoundOverMsg!=null;
  }

  /**
   * 修改房间名字
   */
  public boolean hasMChangeRoomNameMsg() {
    return mChangeRoomNameMsg!=null;
  }

  /**
   * 修改房间等级限制
   */
  public boolean hasMChangeRoomLevelLimitMsg() {
    return mChangeRoomLevelLimitMsg!=null;
  }

  /**
   * 踢人
   */
  public boolean hasMKickoutUserMsg() {
    return mKickoutUserMsg!=null;
  }

  /**
   * 修改房主
   */
  public boolean hasMChangeRoomOwnerMsg() {
    return mChangeRoomOwnerMsg!=null;
  }

  /**
   * 房间匹配开关
   */
  public boolean hasMMatchStatusMsg() {
    return mMatchStatusMsg!=null;
  }

  /**
   * 实时机器评分 msgType == RM_ROUND_MACHINE_SCORE
   */
  public boolean hasMachineScore() {
    return machineScore!=null;
  }

  public static final class Builder extends Message.Builder<MicRoomMsg, Builder> {
    private Long timeMs;

    private EMicRoomMsgType msgType;

    private Integer roomID;

    private MJoinActionMsg mJoinActionMsg;

    private MJoinNoticeMsg mJoinNoticeMsg;

    private MExitGameMsg mExitGameMsg;

    private MSyncStatusMsg syncStatusMsg;

    private MRoundOverMsg mRoundOverMsg;

    private MAddMusicMsg mAddMusicMsg;

    private MDelMusicMsg mDelMusicMsg;

    private MUpMusicMsg mUpMusicMsg;

    private MCancelMusic mCancelMusic;

    private MReqAddMusicMsg mReqAddMusicMsg;

    private MCHOGiveUpMsg mCHOGiveUpMsg;

    private MSPKInnerRoundOverMsg mSPKInnerRoundOverMsg;

    private MChangeRoomNameMsg mChangeRoomNameMsg;

    private MChangeRoomLevelLimitMsg mChangeRoomLevelLimitMsg;

    private MKickoutUserMsg mKickoutUserMsg;

    private MChangeRoomOwnerMsg mChangeRoomOwnerMsg;

    private MMatchStatusMsg mMatchStatusMsg;

    private MMachineScore machineScore;

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
    public Builder setMsgType(EMicRoomMsgType msgType) {
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
     * 加入游戏指令消息
     */
    public Builder setMJoinActionMsg(MJoinActionMsg mJoinActionMsg) {
      this.mJoinActionMsg = mJoinActionMsg;
      return this;
    }

    /**
     * 加入游戏通知消息
     */
    public Builder setMJoinNoticeMsg(MJoinNoticeMsg mJoinNoticeMsg) {
      this.mJoinNoticeMsg = mJoinNoticeMsg;
      return this;
    }

    /**
     * 退出游戏
     */
    public Builder setMExitGameMsg(MExitGameMsg mExitGameMsg) {
      this.mExitGameMsg = mExitGameMsg;
      return this;
    }

    /**
     * 同步状态
     */
    public Builder setSyncStatusMsg(MSyncStatusMsg syncStatusMsg) {
      this.syncStatusMsg = syncStatusMsg;
      return this;
    }

    /**
     * 轮次结束
     */
    public Builder setMRoundOverMsg(MRoundOverMsg mRoundOverMsg) {
      this.mRoundOverMsg = mRoundOverMsg;
      return this;
    }

    /**
     * 点歌
     */
    public Builder setMAddMusicMsg(MAddMusicMsg mAddMusicMsg) {
      this.mAddMusicMsg = mAddMusicMsg;
      return this;
    }

    /**
     * 删歌
     */
    public Builder setMDelMusicMsg(MDelMusicMsg mDelMusicMsg) {
      this.mDelMusicMsg = mDelMusicMsg;
      return this;
    }

    /**
     * 置顶歌曲
     */
    public Builder setMUpMusicMsg(MUpMusicMsg mUpMusicMsg) {
      this.mUpMusicMsg = mUpMusicMsg;
      return this;
    }

    /**
     * 取消歌曲：合唱/PK伙伴已退出
     */
    public Builder setMCancelMusic(MCancelMusic mCancelMusic) {
      this.mCancelMusic = mCancelMusic;
      return this;
    }

    /**
     * 请求合唱/PK
     */
    public Builder setMReqAddMusicMsg(MReqAddMusicMsg mReqAddMusicMsg) {
      this.mReqAddMusicMsg = mReqAddMusicMsg;
      return this;
    }

    /**
     * 合唱模式放弃演唱
     */
    public Builder setMCHOGiveUpMsg(MCHOGiveUpMsg mCHOGiveUpMsg) {
      this.mCHOGiveUpMsg = mCHOGiveUpMsg;
      return this;
    }

    /**
     * spk模式内部轮次结束
     */
    public Builder setMSPKInnerRoundOverMsg(MSPKInnerRoundOverMsg mSPKInnerRoundOverMsg) {
      this.mSPKInnerRoundOverMsg = mSPKInnerRoundOverMsg;
      return this;
    }

    /**
     * 修改房间名字
     */
    public Builder setMChangeRoomNameMsg(MChangeRoomNameMsg mChangeRoomNameMsg) {
      this.mChangeRoomNameMsg = mChangeRoomNameMsg;
      return this;
    }

    /**
     * 修改房间等级限制
     */
    public Builder setMChangeRoomLevelLimitMsg(MChangeRoomLevelLimitMsg mChangeRoomLevelLimitMsg) {
      this.mChangeRoomLevelLimitMsg = mChangeRoomLevelLimitMsg;
      return this;
    }

    /**
     * 踢人
     */
    public Builder setMKickoutUserMsg(MKickoutUserMsg mKickoutUserMsg) {
      this.mKickoutUserMsg = mKickoutUserMsg;
      return this;
    }

    /**
     * 修改房主
     */
    public Builder setMChangeRoomOwnerMsg(MChangeRoomOwnerMsg mChangeRoomOwnerMsg) {
      this.mChangeRoomOwnerMsg = mChangeRoomOwnerMsg;
      return this;
    }

    /**
     * 房间匹配开关
     */
    public Builder setMMatchStatusMsg(MMatchStatusMsg mMatchStatusMsg) {
      this.mMatchStatusMsg = mMatchStatusMsg;
      return this;
    }

    /**
     * 实时机器评分 msgType == RM_ROUND_MACHINE_SCORE
     */
    public Builder setMachineScore(MMachineScore machineScore) {
      this.machineScore = machineScore;
      return this;
    }

    @Override
    public MicRoomMsg build() {
      return new MicRoomMsg(timeMs, msgType, roomID, mJoinActionMsg, mJoinNoticeMsg, mExitGameMsg, syncStatusMsg, mRoundOverMsg, mAddMusicMsg, mDelMusicMsg, mUpMusicMsg, mCancelMusic, mReqAddMusicMsg, mCHOGiveUpMsg, mSPKInnerRoundOverMsg, mChangeRoomNameMsg, mChangeRoomLevelLimitMsg, mKickoutUserMsg, mChangeRoomOwnerMsg, mMatchStatusMsg, machineScore, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MicRoomMsg extends ProtoAdapter<MicRoomMsg> {
    public ProtoAdapter_MicRoomMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, MicRoomMsg.class);
    }

    @Override
    public int encodedSize(MicRoomMsg value) {
      return ProtoAdapter.SINT64.encodedSizeWithTag(1, value.timeMs)
          + EMicRoomMsgType.ADAPTER.encodedSizeWithTag(2, value.msgType)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.roomID)
          + MJoinActionMsg.ADAPTER.encodedSizeWithTag(100, value.mJoinActionMsg)
          + MJoinNoticeMsg.ADAPTER.encodedSizeWithTag(101, value.mJoinNoticeMsg)
          + MExitGameMsg.ADAPTER.encodedSizeWithTag(102, value.mExitGameMsg)
          + MSyncStatusMsg.ADAPTER.encodedSizeWithTag(103, value.syncStatusMsg)
          + MRoundOverMsg.ADAPTER.encodedSizeWithTag(104, value.mRoundOverMsg)
          + MAddMusicMsg.ADAPTER.encodedSizeWithTag(105, value.mAddMusicMsg)
          + MDelMusicMsg.ADAPTER.encodedSizeWithTag(106, value.mDelMusicMsg)
          + MUpMusicMsg.ADAPTER.encodedSizeWithTag(107, value.mUpMusicMsg)
          + MCancelMusic.ADAPTER.encodedSizeWithTag(108, value.mCancelMusic)
          + MReqAddMusicMsg.ADAPTER.encodedSizeWithTag(109, value.mReqAddMusicMsg)
          + MCHOGiveUpMsg.ADAPTER.encodedSizeWithTag(110, value.mCHOGiveUpMsg)
          + MSPKInnerRoundOverMsg.ADAPTER.encodedSizeWithTag(111, value.mSPKInnerRoundOverMsg)
          + MChangeRoomNameMsg.ADAPTER.encodedSizeWithTag(112, value.mChangeRoomNameMsg)
          + MChangeRoomLevelLimitMsg.ADAPTER.encodedSizeWithTag(113, value.mChangeRoomLevelLimitMsg)
          + MKickoutUserMsg.ADAPTER.encodedSizeWithTag(114, value.mKickoutUserMsg)
          + MChangeRoomOwnerMsg.ADAPTER.encodedSizeWithTag(115, value.mChangeRoomOwnerMsg)
          + MMatchStatusMsg.ADAPTER.encodedSizeWithTag(116, value.mMatchStatusMsg)
          + MMachineScore.ADAPTER.encodedSizeWithTag(117, value.machineScore)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MicRoomMsg value) throws IOException {
      ProtoAdapter.SINT64.encodeWithTag(writer, 1, value.timeMs);
      EMicRoomMsgType.ADAPTER.encodeWithTag(writer, 2, value.msgType);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.roomID);
      MJoinActionMsg.ADAPTER.encodeWithTag(writer, 100, value.mJoinActionMsg);
      MJoinNoticeMsg.ADAPTER.encodeWithTag(writer, 101, value.mJoinNoticeMsg);
      MExitGameMsg.ADAPTER.encodeWithTag(writer, 102, value.mExitGameMsg);
      MSyncStatusMsg.ADAPTER.encodeWithTag(writer, 103, value.syncStatusMsg);
      MRoundOverMsg.ADAPTER.encodeWithTag(writer, 104, value.mRoundOverMsg);
      MAddMusicMsg.ADAPTER.encodeWithTag(writer, 105, value.mAddMusicMsg);
      MDelMusicMsg.ADAPTER.encodeWithTag(writer, 106, value.mDelMusicMsg);
      MUpMusicMsg.ADAPTER.encodeWithTag(writer, 107, value.mUpMusicMsg);
      MCancelMusic.ADAPTER.encodeWithTag(writer, 108, value.mCancelMusic);
      MReqAddMusicMsg.ADAPTER.encodeWithTag(writer, 109, value.mReqAddMusicMsg);
      MCHOGiveUpMsg.ADAPTER.encodeWithTag(writer, 110, value.mCHOGiveUpMsg);
      MSPKInnerRoundOverMsg.ADAPTER.encodeWithTag(writer, 111, value.mSPKInnerRoundOverMsg);
      MChangeRoomNameMsg.ADAPTER.encodeWithTag(writer, 112, value.mChangeRoomNameMsg);
      MChangeRoomLevelLimitMsg.ADAPTER.encodeWithTag(writer, 113, value.mChangeRoomLevelLimitMsg);
      MKickoutUserMsg.ADAPTER.encodeWithTag(writer, 114, value.mKickoutUserMsg);
      MChangeRoomOwnerMsg.ADAPTER.encodeWithTag(writer, 115, value.mChangeRoomOwnerMsg);
      MMatchStatusMsg.ADAPTER.encodeWithTag(writer, 116, value.mMatchStatusMsg);
      MMachineScore.ADAPTER.encodeWithTag(writer, 117, value.machineScore);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MicRoomMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 2: {
            try {
              builder.setMsgType(EMicRoomMsgType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 3: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 100: builder.setMJoinActionMsg(MJoinActionMsg.ADAPTER.decode(reader)); break;
          case 101: builder.setMJoinNoticeMsg(MJoinNoticeMsg.ADAPTER.decode(reader)); break;
          case 102: builder.setMExitGameMsg(MExitGameMsg.ADAPTER.decode(reader)); break;
          case 103: builder.setSyncStatusMsg(MSyncStatusMsg.ADAPTER.decode(reader)); break;
          case 104: builder.setMRoundOverMsg(MRoundOverMsg.ADAPTER.decode(reader)); break;
          case 105: builder.setMAddMusicMsg(MAddMusicMsg.ADAPTER.decode(reader)); break;
          case 106: builder.setMDelMusicMsg(MDelMusicMsg.ADAPTER.decode(reader)); break;
          case 107: builder.setMUpMusicMsg(MUpMusicMsg.ADAPTER.decode(reader)); break;
          case 108: builder.setMCancelMusic(MCancelMusic.ADAPTER.decode(reader)); break;
          case 109: builder.setMReqAddMusicMsg(MReqAddMusicMsg.ADAPTER.decode(reader)); break;
          case 110: builder.setMCHOGiveUpMsg(MCHOGiveUpMsg.ADAPTER.decode(reader)); break;
          case 111: builder.setMSPKInnerRoundOverMsg(MSPKInnerRoundOverMsg.ADAPTER.decode(reader)); break;
          case 112: builder.setMChangeRoomNameMsg(MChangeRoomNameMsg.ADAPTER.decode(reader)); break;
          case 113: builder.setMChangeRoomLevelLimitMsg(MChangeRoomLevelLimitMsg.ADAPTER.decode(reader)); break;
          case 114: builder.setMKickoutUserMsg(MKickoutUserMsg.ADAPTER.decode(reader)); break;
          case 115: builder.setMChangeRoomOwnerMsg(MChangeRoomOwnerMsg.ADAPTER.decode(reader)); break;
          case 116: builder.setMMatchStatusMsg(MMatchStatusMsg.ADAPTER.decode(reader)); break;
          case 117: builder.setMachineScore(MMachineScore.ADAPTER.decode(reader)); break;
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
    public MicRoomMsg redact(MicRoomMsg value) {
      Builder builder = value.newBuilder();
      if (builder.mJoinActionMsg != null) builder.mJoinActionMsg = MJoinActionMsg.ADAPTER.redact(builder.mJoinActionMsg);
      if (builder.mJoinNoticeMsg != null) builder.mJoinNoticeMsg = MJoinNoticeMsg.ADAPTER.redact(builder.mJoinNoticeMsg);
      if (builder.mExitGameMsg != null) builder.mExitGameMsg = MExitGameMsg.ADAPTER.redact(builder.mExitGameMsg);
      if (builder.syncStatusMsg != null) builder.syncStatusMsg = MSyncStatusMsg.ADAPTER.redact(builder.syncStatusMsg);
      if (builder.mRoundOverMsg != null) builder.mRoundOverMsg = MRoundOverMsg.ADAPTER.redact(builder.mRoundOverMsg);
      if (builder.mAddMusicMsg != null) builder.mAddMusicMsg = MAddMusicMsg.ADAPTER.redact(builder.mAddMusicMsg);
      if (builder.mDelMusicMsg != null) builder.mDelMusicMsg = MDelMusicMsg.ADAPTER.redact(builder.mDelMusicMsg);
      if (builder.mUpMusicMsg != null) builder.mUpMusicMsg = MUpMusicMsg.ADAPTER.redact(builder.mUpMusicMsg);
      if (builder.mCancelMusic != null) builder.mCancelMusic = MCancelMusic.ADAPTER.redact(builder.mCancelMusic);
      if (builder.mReqAddMusicMsg != null) builder.mReqAddMusicMsg = MReqAddMusicMsg.ADAPTER.redact(builder.mReqAddMusicMsg);
      if (builder.mCHOGiveUpMsg != null) builder.mCHOGiveUpMsg = MCHOGiveUpMsg.ADAPTER.redact(builder.mCHOGiveUpMsg);
      if (builder.mSPKInnerRoundOverMsg != null) builder.mSPKInnerRoundOverMsg = MSPKInnerRoundOverMsg.ADAPTER.redact(builder.mSPKInnerRoundOverMsg);
      if (builder.mChangeRoomNameMsg != null) builder.mChangeRoomNameMsg = MChangeRoomNameMsg.ADAPTER.redact(builder.mChangeRoomNameMsg);
      if (builder.mChangeRoomLevelLimitMsg != null) builder.mChangeRoomLevelLimitMsg = MChangeRoomLevelLimitMsg.ADAPTER.redact(builder.mChangeRoomLevelLimitMsg);
      if (builder.mKickoutUserMsg != null) builder.mKickoutUserMsg = MKickoutUserMsg.ADAPTER.redact(builder.mKickoutUserMsg);
      if (builder.mChangeRoomOwnerMsg != null) builder.mChangeRoomOwnerMsg = MChangeRoomOwnerMsg.ADAPTER.redact(builder.mChangeRoomOwnerMsg);
      if (builder.mMatchStatusMsg != null) builder.mMatchStatusMsg = MMatchStatusMsg.ADAPTER.redact(builder.mMatchStatusMsg);
      if (builder.machineScore != null) builder.machineScore = MMachineScore.ADAPTER.redact(builder.machineScore);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
