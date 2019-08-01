// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CombineRoom.proto
package com.zq.live.proto.CombineRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

public enum EGameStage implements WireEnum {
  /**
   * 未知场景
   */
  GS_Unknown(0),

  /**
   * 选择游戏阶段
   */
  GS_ChoicGameItem(1),

  /**
   * 游戏进行阶段
   */
  GS_InGamePlay(2);

  public static final ProtoAdapter<EGameStage> ADAPTER = new ProtoAdapter_EGameStage();

  private final int value;

  EGameStage(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static EGameStage fromValue(int value) {
    switch (value) {
      case 0: return GS_Unknown;
      case 1: return GS_ChoicGameItem;
      case 2: return GS_InGamePlay;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public EGameStage build() {
      return GS_Unknown;
    }
  }

  private static final class ProtoAdapter_EGameStage extends EnumAdapter<EGameStage> {
    ProtoAdapter_EGameStage() {
      super(EGameStage.class);
    }

    @Override
    protected EGameStage fromValue(int value) {
      return EGameStage.fromValue(value);
    }
  }
}