// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Notification.proto
package com.zq.live.proto.Notification;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EInviteType implements WireEnum {
  /**
   * 未知
   */
  IT_UNKNOWN(0),

  /**
   * 在双人房间外邀请
   */
  IT_OUT_COMBINE_ROOM(1),

  /**
   * 在双人房间内邀请
   */
  IT_IN_COMBINE_ROOM(2);

  public static final ProtoAdapter<EInviteType> ADAPTER = new ProtoAdapter_EInviteType();

  private final int value;

  EInviteType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EInviteType fromValue(int value) {
    switch (value) {
      case 0: return IT_UNKNOWN;
      case 1: return IT_OUT_COMBINE_ROOM;
      case 2: return IT_IN_COMBINE_ROOM;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EInviteType build() {
      return IT_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EInviteType extends EnumAdapter<EInviteType> {
    ProtoAdapter_EInviteType() {
      super(EInviteType.class);
    }

    @Override
    protected EInviteType fromValue(int value) {
      return EInviteType.fromValue(value);
    }
  }
}