// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: common.proto
package com.zq.live.proto.Common;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EKTVStatus implements WireEnum {
  /**
   * 进行中
   */
  EKS_GOING(0),

  /**
   * 暂停态
   */
  EKS_STOP(1);

  public static final ProtoAdapter<EKTVStatus> ADAPTER = new ProtoAdapter_EKTVStatus();

  private final int value;

  EKTVStatus(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EKTVStatus fromValue(int value) {
    switch (value) {
      case 0: return EKS_GOING;
      case 1: return EKS_STOP;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EKTVStatus build() {
      return EKS_GOING;
    }
  }

  private static final class ProtoAdapter_EKTVStatus extends EnumAdapter<EKTVStatus> {
    ProtoAdapter_EKTVStatus() {
      super(EKTVStatus.class);
    }

    @Override
    protected EKTVStatus fromValue(int value) {
      return EKTVStatus.fromValue(value);
    }
  }
}
