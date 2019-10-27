// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.GrabRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

/**
 * ERoomMsgType 房间消息类型
 */
public enum ERoomMsgType implements WireEnum {
  /**
   * 未知消息
   */
  RM_UNKNOWN(0),

  /**
   * 普通评论消息
   */
  RM_COMMENT(10),

  /**
   * 特殊表情消息
   */
  RM_SPECIAL_EMOJI(11),

  /**
   * 动态表情消息
   */
  RM_DYNAMIC_EMOJI(12),

  /**
   * 语音消息
   */
  RM_AUDIO_MSG(13),

  /**
   * 加入游戏指令消息
   */
  RM_JOIN_ACTION(100),

  /**
   * 加入游戏通知消息
   */
  RM_JOIN_NOTICE(101),

  /**
   * 准备游戏通知消息
   */
  RM_READY_NOTICE(102),

  /**
   * 游戏轮次结束通知消息
   */
  RM_ROUND_OVER(103),

  /**
   * 轮次和游戏结束通知消息
   */
  RM_ROUND_AND_GAME_OVER(104),

  /**
   * app进程后台切换通知
   */
  RM_APP_SWAP(105),

  /**
   * 状态同步
   */
  RM_SYNC_STATUS(106),

  /**
   * 在开始游戏前退出
   */
  RM_EXIT_GAME_BEFORE_PLAY(107),

  /**
   * 在完成游戏后退出
   */
  RM_EXIT_GAME_AFTER_PLAY(108),

  /**
   * 在游戏中,但不再round时退出
   */
  RM_EXIT_GAME_OUT_ROUND(109),

  /**
   * 游戏投票结果消息
   */
  RM_VOTE_RESULT(110),

  /**
   * 实时机器评分
   */
  RM_ROUND_MACHINE_SCORE(111),

  /**
   * 一唱到底：想唱消息
   */
  RM_Q_WANT_SING_CHANCE(112),

  /**
   * 一唱到底：获得轮次机会
   */
  RM_Q_GET_SING_CHANCE(113),

  /**
   * 一唱到底：同步状态
   */
  RM_Q_SYNC_STATUS(114),

  /**
   * 一唱到底：轮次结束
   */
  RM_Q_ROUND_OVER(115),

  /**
   * 一唱到底：最后轮次结束，即游戏结束，游戏结果数据
   */
  RM_Q_ROUND_AND_GAME_OVER(116),

  /**
   * 一唱到底：演唱不通过，即灭灯
   */
  RM_Q_NO_PASS_SING(117),

  /**
   * 一唱到底：退出游戏
   */
  RM_Q_EXIT_GAME(118),

  /**
   * 排位赛：爆灯通知
   */
  RM_PK_BLIGHT(119),

  /**
   * 排位赛：灭灯通知
   */
  RM_PK_MLIGHT(120),

  /**
   * 一唱到底：爆灯通知
   */
  RM_Q_BLIGHT(121),

  /**
   * 一唱到底：灭灯通知
   */
  RM_Q_MLIGHT(122),

  /**
   * 一唱到底：加入游戏通知消息
   */
  RM_Q_JOIN_NOTICE(123),

  /**
   * 一唱到底：加入游戏指令消息
   */
  RM_Q_JOIN_ACTION(124),

  /**
   * 一唱到底：踢人请求信令消息
   */
  RM_Q_KICK_USER_REQUEST(125),

  /**
   * 一唱到底：踢人结果信令消息
   */
  RM_Q_KICK_USER_RESULT(126),

  /**
   * 一唱到底：通知房间用户游戏开始
   */
  RM_Q_GAME_BEGIN(127),

  /**
   * 一唱到底：切换专场提示消息
   */
  RM_Q_CHANGE_MUSIC_TAG(128),

  /**
   * 一唱到底：金币变动消息
   */
  RM_Q_COIN_CHANGE(129),

  /**
   * 一唱到底：合唱模式放弃演唱
   */
  RM_Q_CHO_GIVEUP(131),

  /**
   * 一唱到底：spk模式内部轮次结束
   */
  RM_Q_PK_INNER_ROUND_OVER(132),

  /**
   * 一唱到底：房主修改房间名称
   */
  RM_Q_CHANGE_ROOM_NAME(133),

  /**
   * 送礼
   */
  RM_G_PRESENT_GIFT(134),

  /**
   * 告知伴奏开始
   */
  RM_ROUND_ACC_BEGIN(190);

  public static final ProtoAdapter<ERoomMsgType> ADAPTER = new ProtoAdapter_ERoomMsgType();

  private final int value;

  ERoomMsgType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static ERoomMsgType fromValue(int value) {
    switch (value) {
      case 0: return RM_UNKNOWN;
      case 10: return RM_COMMENT;
      case 11: return RM_SPECIAL_EMOJI;
      case 12: return RM_DYNAMIC_EMOJI;
      case 13: return RM_AUDIO_MSG;
      case 100: return RM_JOIN_ACTION;
      case 101: return RM_JOIN_NOTICE;
      case 102: return RM_READY_NOTICE;
      case 103: return RM_ROUND_OVER;
      case 104: return RM_ROUND_AND_GAME_OVER;
      case 105: return RM_APP_SWAP;
      case 106: return RM_SYNC_STATUS;
      case 107: return RM_EXIT_GAME_BEFORE_PLAY;
      case 108: return RM_EXIT_GAME_AFTER_PLAY;
      case 109: return RM_EXIT_GAME_OUT_ROUND;
      case 110: return RM_VOTE_RESULT;
      case 111: return RM_ROUND_MACHINE_SCORE;
      case 112: return RM_Q_WANT_SING_CHANCE;
      case 113: return RM_Q_GET_SING_CHANCE;
      case 114: return RM_Q_SYNC_STATUS;
      case 115: return RM_Q_ROUND_OVER;
      case 116: return RM_Q_ROUND_AND_GAME_OVER;
      case 117: return RM_Q_NO_PASS_SING;
      case 118: return RM_Q_EXIT_GAME;
      case 119: return RM_PK_BLIGHT;
      case 120: return RM_PK_MLIGHT;
      case 121: return RM_Q_BLIGHT;
      case 122: return RM_Q_MLIGHT;
      case 123: return RM_Q_JOIN_NOTICE;
      case 124: return RM_Q_JOIN_ACTION;
      case 125: return RM_Q_KICK_USER_REQUEST;
      case 126: return RM_Q_KICK_USER_RESULT;
      case 127: return RM_Q_GAME_BEGIN;
      case 128: return RM_Q_CHANGE_MUSIC_TAG;
      case 129: return RM_Q_COIN_CHANGE;
      case 131: return RM_Q_CHO_GIVEUP;
      case 132: return RM_Q_PK_INNER_ROUND_OVER;
      case 133: return RM_Q_CHANGE_ROOM_NAME;
      case 134: return RM_G_PRESENT_GIFT;
      case 190: return RM_ROUND_ACC_BEGIN;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public ERoomMsgType build() {
      return RM_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_ERoomMsgType extends EnumAdapter<ERoomMsgType> {
    ProtoAdapter_ERoomMsgType() {
      super(ERoomMsgType.class);
    }

    @Override
    protected ERoomMsgType fromValue(int value) {
      return ERoomMsgType.fromValue(value);
    }
  }
}