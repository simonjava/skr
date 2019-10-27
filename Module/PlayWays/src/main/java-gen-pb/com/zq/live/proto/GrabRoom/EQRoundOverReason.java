// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.GrabRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EQRoundOverReason implements WireEnum {
  /**
   * 未知
   */
  ROR_UNKNOWN(0),

  /**
   * 上个轮次结束
   */
  ROR_LAST_ROUND_OVER(1),

  /**
   * 没人抢唱
   */
  ROR_NO_ONE_SING(2),

  /**
   * 当前玩家退出
   */
  ROR_IN_ROUND_PLAYER_EXIT(3),

  /**
   * 多人灭灯
   */
  ROR_MULTI_NO_PASS(4),

  /**
   * 自己放弃演唱
   */
  ROR_SELF_GIVE_UP(5),

  /**
   * 合唱成功
   */
  ROR_CHO_SUCCESS(6),

  /**
   * 合唱失败
   */
  ROR_CHO_FAILED(7),

  /**
   * 合唱人数不够
   */
  ROR_CHO_NOT_ENOUTH_PLAYER(8),

  /**
   * SPK人数不够
   */
  ROR_SPK_NOT_ENOUTH_PLAYER(9),

  /**
   * 连麦小游戏人数不够
   */
  ROR_MIN_GAME_NOT_ENOUTH_PLAYER(10),

  /**
   * 连麦小游戏房主结束轮次
   */
  ROR_MIN_GAME_OWNER_END_ROUND(11),

  /**
   * 用户不玩了
   */
  ROR_MIN_GAME_NOT_PLAY(12),

  /**
   * 自由麦模式正常结束
   */
  ROR_FREE_MICRO_NORMAL_END(13),

  /**
   * 自由麦模式房主停止
   */
  ROR_FREE_MICRO_OWNER_STOP(14);

  public static final ProtoAdapter<EQRoundOverReason> ADAPTER = new ProtoAdapter_EQRoundOverReason();

  private final int value;

  EQRoundOverReason(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EQRoundOverReason fromValue(int value) {
    switch (value) {
      case 0: return ROR_UNKNOWN;
      case 1: return ROR_LAST_ROUND_OVER;
      case 2: return ROR_NO_ONE_SING;
      case 3: return ROR_IN_ROUND_PLAYER_EXIT;
      case 4: return ROR_MULTI_NO_PASS;
      case 5: return ROR_SELF_GIVE_UP;
      case 6: return ROR_CHO_SUCCESS;
      case 7: return ROR_CHO_FAILED;
      case 8: return ROR_CHO_NOT_ENOUTH_PLAYER;
      case 9: return ROR_SPK_NOT_ENOUTH_PLAYER;
      case 10: return ROR_MIN_GAME_NOT_ENOUTH_PLAYER;
      case 11: return ROR_MIN_GAME_OWNER_END_ROUND;
      case 12: return ROR_MIN_GAME_NOT_PLAY;
      case 13: return ROR_FREE_MICRO_NORMAL_END;
      case 14: return ROR_FREE_MICRO_OWNER_STOP;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EQRoundOverReason build() {
      return ROR_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EQRoundOverReason extends EnumAdapter<EQRoundOverReason> {
    ProtoAdapter_EQRoundOverReason() {
      super(EQRoundOverReason.class);
    }

    @Override
    protected EQRoundOverReason fromValue(int value) {
      return EQRoundOverReason.fromValue(value);
    }
  }
}