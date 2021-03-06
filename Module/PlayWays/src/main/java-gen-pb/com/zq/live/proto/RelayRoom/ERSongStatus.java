// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: relay_room.proto
package com.zq.live.proto.RelayRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum ERSongStatus implements WireEnum {
  /**
   * 未知
   */
  ERUSI_UNKNOWN(0),

  /**
   * 演唱中
   */
  ERUSI_IN_PLAY(1),

  /**
   * 下一首
   */
  ERUSI_NEXT_PLAY(2);

  public static final ProtoAdapter<ERSongStatus> ADAPTER = new ProtoAdapter_ERSongStatus();

  private final int value;

  ERSongStatus(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static ERSongStatus fromValue(int value) {
    switch (value) {
      case 0: return ERUSI_UNKNOWN;
      case 1: return ERUSI_IN_PLAY;
      case 2: return ERUSI_NEXT_PLAY;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public ERSongStatus build() {
      return ERUSI_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_ERSongStatus extends EnumAdapter<ERSongStatus> {
    ProtoAdapter_ERSongStatus() {
      super(ERSongStatus.class);
    }

    @Override
    protected ERSongStatus fromValue(int value) {
      return ERSongStatus.fromValue(value);
    }
  }
}
