// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: mic_room.proto
package com.zq.live.proto.Room;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EMRoundStatus implements WireEnum {
  /**
   * 轮次状态未知
   */
  MRS_UNKNOWN(0),

  /**
   * 轮次进入导唱阶段(等待阶段)
   */
  MRS_INTRO(1),

  /**
   * 轮次进入演唱阶段(单人演唱)
   */
  MRS_SING(2),

  /**
   * 合唱演唱阶段
   */
  MRS_CHO_SING(3),

  /**
   * spk第一位用户演唱
   */
  MRS_SPK_FIRST_PEER_SING(4),

  /**
   * spk第二位用户演唱
   */
  MRS_SPK_SECOND_PEER_SING(5),

  /**
   * 暂停(用户游戏插入活动)
   */
  MRS_PAUSE(6),

  /**
   * 轮次已结束
   */
  MRS_END(7);

  public static final ProtoAdapter<EMRoundStatus> ADAPTER = new ProtoAdapter_EMRoundStatus();

  private final int value;

  EMRoundStatus(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EMRoundStatus fromValue(int value) {
    switch (value) {
      case 0: return MRS_UNKNOWN;
      case 1: return MRS_INTRO;
      case 2: return MRS_SING;
      case 3: return MRS_CHO_SING;
      case 4: return MRS_SPK_FIRST_PEER_SING;
      case 5: return MRS_SPK_SECOND_PEER_SING;
      case 6: return MRS_PAUSE;
      case 7: return MRS_END;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EMRoundStatus build() {
      return MRS_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_EMRoundStatus extends EnumAdapter<EMRoundStatus> {
    ProtoAdapter_EMRoundStatus() {
      super(EMRoundStatus.class);
    }

    @Override
    protected EMRoundStatus fromValue(int value) {
      return EMRoundStatus.fromValue(value);
    }
  }
}
