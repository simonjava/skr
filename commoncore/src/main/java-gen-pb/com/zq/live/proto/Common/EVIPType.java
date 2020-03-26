// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Common.proto
package com.zq.live.proto.Common;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

/**
 * 加v类型
 */
public enum EVIPType implements WireEnum {
  /**
   * 未知：没有加v`
   */
  EVT_UNKNOWN(0),

  /**
   * 红v
   */
  EVT_RED_V(1),

  /**
   * 金v
   */
  EVT_GOLDEN_V(2),

  /**
   * “豪”徽章奖励
   */
  EVT_HAO(3),

  /**
   * “人气”徽章奖励
   */
  EVT_RENQI(4),

  /**
   * “恋爱”徽章奖励
   */
  EVT_LA_V(5),

  /**
   * “心动”徽章奖励
   */
  EVT_XD_V(6);

  public static final ProtoAdapter<EVIPType> ADAPTER = new ProtoAdapter_EVIPType();

  private final int value;

  EVIPType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EVIPType fromValue(int value) {
    switch (value) {
      case 0: return EVT_UNKNOWN;
      case 1: return EVT_RED_V;
      case 2: return EVT_GOLDEN_V;
      case 3: return EVT_HAO;
      case 4: return EVT_RENQI;
      case 5: return EVT_LA_V;
      case 6: return EVT_XD_V;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EVIPType build() {
      return EVT_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EVIPType extends EnumAdapter<EVIPType> {
    ProtoAdapter_EVIPType() {
      super(EVIPType.class);
    }

    @Override
    protected EVIPType fromValue(int value) {
      return EVIPType.fromValue(value);
    }
  }
}
