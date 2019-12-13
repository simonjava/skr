// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: party_room.proto
package com.zq.live.proto.PartyRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EPartyRoomMsgType implements WireEnum {
  /**
   * 未知消息
   */
  PRT_UNKNOWN(0),

  /**
   * 加入游戏通知消息
   */
  PRT_JOIN_NOTICE(10),

  /**
   * 修改公告
   */
  PRT_FIX_ROOM_NOTICE(11),

  /**
   * 设置管理员
   */
  PRT_SET_ROOM_ADMIN(12),

  /**
   * 全员禁麦、全员开麦
   */
  PRT_SET_ALL_MEMBER_MIC(13),

  /**
   * 用户禁麦、解除禁麦
   */
  PRT_SET_USER_MIC(14),

  /**
   * 设置席位状态：关闭席位、打开席位
   */
  PRT_SET_SEAT_STATUS(15),

  /**
   * 申请嘉宾
   */
  PRT_APPLY_FOR_GUEST(16),

  /**
   * 获得席位:上麦
   */
  PRT_GET_SEAT(17),

  /**
   * 还回席位:下麦
   */
  PRT_BACK_SEAT(18),

  /**
   * 邀请用户
   */
  PRT_INVITE_USER(19),

  /**
   * 换座位
   */
  PRT_CHANGE_SEAT(20),

  /**
   * 踢人
   */
  PRT_KICK_OUT_USER(21),

  /**
   * 加载下个轮次
   */
  PRT_NEXT_ROUND(22),

  /**
   * 用户退出房间
   */
  PRT_EXIT_GAME(23),

  /**
   * 同步状态
   */
  PRT_SYNC(24),

  /**
   * 发送表情
   */
  PRT_DYNAMIC_EMOJI(25),

  /**
   * 游戏结束
   */
  PRT_GAME_OVER(26),

  /**
   * 修改房间主题
   */
  PRT_CHANGE_ROOM_TOPIC(27),

  /**
   * 修改房间进入权限
   */
  PRT_CHANGE_ROOM_ENTER_PERMISSION(28),

  /**
   * 更新人气值
   */
  PRT_UPDATE_POPULARITY(29);

  public static final ProtoAdapter<EPartyRoomMsgType> ADAPTER = new ProtoAdapter_EPartyRoomMsgType();

  private final int value;

  EPartyRoomMsgType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EPartyRoomMsgType fromValue(int value) {
    switch (value) {
      case 0: return PRT_UNKNOWN;
      case 10: return PRT_JOIN_NOTICE;
      case 11: return PRT_FIX_ROOM_NOTICE;
      case 12: return PRT_SET_ROOM_ADMIN;
      case 13: return PRT_SET_ALL_MEMBER_MIC;
      case 14: return PRT_SET_USER_MIC;
      case 15: return PRT_SET_SEAT_STATUS;
      case 16: return PRT_APPLY_FOR_GUEST;
      case 17: return PRT_GET_SEAT;
      case 18: return PRT_BACK_SEAT;
      case 19: return PRT_INVITE_USER;
      case 20: return PRT_CHANGE_SEAT;
      case 21: return PRT_KICK_OUT_USER;
      case 22: return PRT_NEXT_ROUND;
      case 23: return PRT_EXIT_GAME;
      case 24: return PRT_SYNC;
      case 25: return PRT_DYNAMIC_EMOJI;
      case 26: return PRT_GAME_OVER;
      case 27: return PRT_CHANGE_ROOM_TOPIC;
      case 28: return PRT_CHANGE_ROOM_ENTER_PERMISSION;
      case 29: return PRT_UPDATE_POPULARITY;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EPartyRoomMsgType build() {
      return PRT_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EPartyRoomMsgType extends EnumAdapter<EPartyRoomMsgType> {
    ProtoAdapter_EPartyRoomMsgType() {
      super(EPartyRoomMsgType.class);
    }

    @Override
    protected EPartyRoomMsgType fromValue(int value) {
      return EPartyRoomMsgType.fromValue(value);
    }
  }
}
