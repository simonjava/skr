// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: mic_room.proto
package com.zq.live.proto.MicRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum ESongStatus implements WireEnum {
  /**
   * 未知
   */
  EUSI_UNKNOWN(0),

  /**
   * 演唱中
   */
  EUSI_IN_PLAY(1),

  /**
   * 下一首
   */
  EUSI_NEXT_PLAY(2);

  public static final ProtoAdapter<ESongStatus> ADAPTER = new ProtoAdapter_ESongStatus();

  private final int value;

  ESongStatus(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static ESongStatus fromValue(int value) {
    switch (value) {
      case 0: return EUSI_UNKNOWN;
      case 1: return EUSI_IN_PLAY;
      case 2: return EUSI_NEXT_PLAY;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public ESongStatus build() {
      return EUSI_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_ESongStatus extends EnumAdapter<ESongStatus> {
    ProtoAdapter_ESongStatus() {
      super(ESongStatus.class);
    }

    @Override
    protected ESongStatus fromValue(int value) {
      return ESongStatus.fromValue(value);
    }
  }
}