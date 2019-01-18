// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.Room;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EQRoundResultType implements WireEnum {
  /**
   * 未知
   */
  ROT_UNKNOWN(0),

  /**
   * 有种结束叫刚刚开始（t<30%）
   */
  ROT_TYPE_1(1),

  /**
   * 有份悲伤叫都没及格(30%<=t <60%)
   */
  ROT_TYPE_2(2),

  /**
   * 有种遗憾叫明明可以（60%<=t<90%）
   */
  ROT_TYPE_3(3),

  /**
   * 有种可惜叫我觉得你行（90%<=t<=100%)
   */
  ROT_TYPE_4(4),

  /**
   * 有种优秀叫一唱到底（全部唱完）
   */
  ROT_TYPE_5(5);

  public static final ProtoAdapter<EQRoundResultType> ADAPTER = new ProtoAdapter_EQRoundResultType();

  private final int value;

  EQRoundResultType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EQRoundResultType fromValue(int value) {
    switch (value) {
      case 0: return ROT_UNKNOWN;
      case 1: return ROT_TYPE_1;
      case 2: return ROT_TYPE_2;
      case 3: return ROT_TYPE_3;
      case 4: return ROT_TYPE_4;
      case 5: return ROT_TYPE_5;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EQRoundResultType build() {
      return ROT_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EQRoundResultType extends EnumAdapter<EQRoundResultType> {
    ProtoAdapter_EQRoundResultType() {
      super(EQRoundResultType.class);
    }

    @Override
    protected EQRoundResultType fromValue(int value) {
      return EQRoundResultType.fromValue(value);
    }
  }
}
