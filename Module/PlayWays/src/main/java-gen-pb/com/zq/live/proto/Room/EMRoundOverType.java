// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: mic_room.proto
package com.zq.live.proto.Room;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EMRoundOverType implements WireEnum {
  /**
   * 未知
   */
  EMROT_UNKNOWN(0),

  /**
   * 子轮次结束
   */
  EMROT_SUB_ROUND_OVER(1),

  /**
   * 主轮次结束
   */
  EMROT_MAIN_ROUND_OVER(2);

  public static final ProtoAdapter<EMRoundOverType> ADAPTER = new ProtoAdapter_EMRoundOverType();

  private final int value;

  EMRoundOverType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EMRoundOverType fromValue(int value) {
    switch (value) {
      case 0: return EMROT_UNKNOWN;
      case 1: return EMROT_SUB_ROUND_OVER;
      case 2: return EMROT_MAIN_ROUND_OVER;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EMRoundOverType build() {
      return EMROT_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EMRoundOverType extends EnumAdapter<EMRoundOverType> {
    ProtoAdapter_EMRoundOverType() {
      super(EMRoundOverType.class);
    }

    @Override
    protected EMRoundOverType fromValue(int value) {
      return EMRoundOverType.fromValue(value);
    }
  }
}
