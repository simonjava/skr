// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: battle_room.proto
package com.zq.live.proto.BattleRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EBRoundStatus implements WireEnum {
  /**
   * 轮次状态未知
   */
  BRS_UNKNOWN(0),

  /**
   * 轮次进入导唱阶段(wait阶段)
   */
  BRS_INTRO(1),

  /**
   * 轮次进入帮助阶段(wait阶段)
   */
  BRS_HELP(2),

  /**
   * 轮次进入演唱阶段(sing阶段)
   */
  BRS_SING(3),

  /**
   * 轮次已结束
   */
  BRS_END(4);

  public static final ProtoAdapter<EBRoundStatus> ADAPTER = new ProtoAdapter_EBRoundStatus();

  private final int value;

  EBRoundStatus(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EBRoundStatus fromValue(int value) {
    switch (value) {
      case 0: return BRS_UNKNOWN;
      case 1: return BRS_INTRO;
      case 2: return BRS_HELP;
      case 3: return BRS_SING;
      case 4: return BRS_END;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EBRoundStatus build() {
      return BRS_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EBRoundStatus extends EnumAdapter<EBRoundStatus> {
    ProtoAdapter_EBRoundStatus() {
      super(EBRoundStatus.class);
    }

    @Override
    protected EBRoundStatus fromValue(int value) {
      return EBRoundStatus.fromValue(value);
    }
  }
}
