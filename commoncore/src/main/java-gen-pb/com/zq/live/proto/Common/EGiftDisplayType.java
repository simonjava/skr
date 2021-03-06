// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: common.proto
package com.zq.live.proto.Common;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

/**
 * 礼物展示类型
 */
public enum EGiftDisplayType implements WireEnum {
  EGDT_Unknown(0),

  EGDT_Big(1),

  EGDT_Medium(2),

  EGDT_Small(3),

  EGDT_Free(4);

  public static final ProtoAdapter<EGiftDisplayType> ADAPTER = new ProtoAdapter_EGiftDisplayType();

  private final int value;

  EGiftDisplayType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EGiftDisplayType fromValue(int value) {
    switch (value) {
      case 0: return EGDT_Unknown;
      case 1: return EGDT_Big;
      case 2: return EGDT_Medium;
      case 3: return EGDT_Small;
      case 4: return EGDT_Free;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EGiftDisplayType build() {
      return EGDT_Unknown;
    }
  }

  private static final class ProtoAdapter_EGiftDisplayType extends EnumAdapter<EGiftDisplayType> {
    ProtoAdapter_EGiftDisplayType() {
      super(EGiftDisplayType.class);
    }

    @Override
    protected EGiftDisplayType fromValue(int value) {
      return EGiftDisplayType.fromValue(value);
    }
  }
}
