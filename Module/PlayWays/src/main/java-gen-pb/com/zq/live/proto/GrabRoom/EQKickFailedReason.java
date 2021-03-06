// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: grab_room.proto
package com.zq.live.proto.GrabRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EQKickFailedReason implements WireEnum {
  /**
   * 未知
   */
  KFR_UNKNOWN(0),

  /**
   * 被踢用户不在房间内
   */
  KFR_KICK_USER_NOT_IN_ROOM(1),

  /**
   * 多数人不同意
   */
  KFR_MOST_USER_NOT_AGREE(2);

  public static final ProtoAdapter<EQKickFailedReason> ADAPTER = new ProtoAdapter_EQKickFailedReason();

  private final int value;

  EQKickFailedReason(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EQKickFailedReason fromValue(int value) {
    switch (value) {
      case 0: return KFR_UNKNOWN;
      case 1: return KFR_KICK_USER_NOT_IN_ROOM;
      case 2: return KFR_MOST_USER_NOT_AGREE;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EQKickFailedReason build() {
      return KFR_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EQKickFailedReason extends EnumAdapter<EQKickFailedReason> {
    ProtoAdapter_EQKickFailedReason() {
      super(EQKickFailedReason.class);
    }

    @Override
    protected EQKickFailedReason fromValue(int value) {
      return EQKickFailedReason.fromValue(value);
    }
  }
}
