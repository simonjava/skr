// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: race_room.proto
package com.zq.live.proto.RaceRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum ERWantSingType implements WireEnum {
  /**
   * 默认抢唱类型：普通
   */
  ERWST_DEFAULT(0),

  /**
   * 带伴奏抢唱
   */
  ERWST_ACCOMPANY(1);

  public static final ProtoAdapter<ERWantSingType> ADAPTER = new ProtoAdapter_ERWantSingType();

  private final int value;

  ERWantSingType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static ERWantSingType fromValue(int value) {
    switch (value) {
      case 0: return ERWST_DEFAULT;
      case 1: return ERWST_ACCOMPANY;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public ERWantSingType build() {
      return ERWST_DEFAULT;
    }
  }

  private static final class ProtoAdapter_ERWantSingType extends EnumAdapter<ERWantSingType> {
    ProtoAdapter_ERWantSingType() {
      super(ERWantSingType.class);
    }

    @Override
    protected ERWantSingType fromValue(int value) {
      return ERWantSingType.fromValue(value);
    }
  }
}
