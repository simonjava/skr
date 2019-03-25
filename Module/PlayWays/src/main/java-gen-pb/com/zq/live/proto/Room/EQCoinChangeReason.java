// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.Room;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EQCoinChangeReason implements WireEnum {
  /**
   * 未知
   */
  OAR_UNKNOWN(0),

  /**
   * 达到指定轮次后，给予房主金币奖励
   */
  OAR_OWNER_ROUD_AWARD(1);

  public static final ProtoAdapter<EQCoinChangeReason> ADAPTER = new ProtoAdapter_EQCoinChangeReason();

  private final int value;

  EQCoinChangeReason(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EQCoinChangeReason fromValue(int value) {
    switch (value) {
      case 0: return OAR_UNKNOWN;
      case 1: return OAR_OWNER_ROUD_AWARD;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EQCoinChangeReason build() {
      return OAR_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EQCoinChangeReason extends EnumAdapter<EQCoinChangeReason> {
    ProtoAdapter_EQCoinChangeReason() {
      super(EQCoinChangeReason.class);
    }

    @Override
    protected EQCoinChangeReason fromValue(int value) {
      return EQCoinChangeReason.fromValue(value);
    }
  }
}
