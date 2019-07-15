// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Common.proto
package com.zq.live.proto.Common;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

/**
 * 房间类型
 */
public enum EMsgRoomMediaType implements WireEnum {
  /**
   * 未知
   */
  EMR_UNKNOWN(0),

  /**
   * 音频房
   */
  EMR_AUDIO(1),

  /**
   * 视频房
   */
  EMR_VIDEO(2);

  public static final ProtoAdapter<EMsgRoomMediaType> ADAPTER = new ProtoAdapter_EMsgRoomMediaType();

  private final int value;

  EMsgRoomMediaType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EMsgRoomMediaType fromValue(int value) {
    switch (value) {
      case 0: return EMR_UNKNOWN;
      case 1: return EMR_AUDIO;
      case 2: return EMR_VIDEO;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EMsgRoomMediaType build() {
      return EMR_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EMsgRoomMediaType extends EnumAdapter<EMsgRoomMediaType> {
    ProtoAdapter_EMsgRoomMediaType() {
      super(EMsgRoomMediaType.class);
    }

    @Override
    protected EMsgRoomMediaType fromValue(int value) {
      return EMsgRoomMediaType.fromValue(value);
    }
  }
}
