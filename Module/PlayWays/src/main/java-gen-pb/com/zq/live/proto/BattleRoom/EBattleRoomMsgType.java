// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: battle_room.proto
package com.zq.live.proto.BattleRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EBattleRoomMsgType implements WireEnum {
  /**
   * 未知消息
   */
  BRT_UNKNOWN(0),

  /**
   * 进入房间
   */
  BRT_USER_ENTER(10),

  /**
   * 轮次进入演唱
   */
  BRT_SING_ROUND(11),

  /**
   * 加载下个轮次
   */
  BRT_NEXT_ROUND(12),

  /**
   * 同步
   */
  BRT_SYNC(13),

  /**
   * 结束房间
   */
  BRT_GAME_OVER(14);

  public static final ProtoAdapter<EBattleRoomMsgType> ADAPTER = new ProtoAdapter_EBattleRoomMsgType();

  private final int value;

  EBattleRoomMsgType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EBattleRoomMsgType fromValue(int value) {
    switch (value) {
      case 0: return BRT_UNKNOWN;
      case 10: return BRT_USER_ENTER;
      case 11: return BRT_SING_ROUND;
      case 12: return BRT_NEXT_ROUND;
      case 13: return BRT_SYNC;
      case 14: return BRT_GAME_OVER;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EBattleRoomMsgType build() {
      return BRT_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EBattleRoomMsgType extends EnumAdapter<EBattleRoomMsgType> {
    ProtoAdapter_EBattleRoomMsgType() {
      super(EBattleRoomMsgType.class);
    }

    @Override
    protected EBattleRoomMsgType fromValue(int value) {
      return EBattleRoomMsgType.fromValue(value);
    }
  }
}