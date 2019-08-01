// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CombineRoom.proto
package com.zq.live.proto.CombineRoom;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

/**
 * ECombineRoomMsgType 房间消息类型
 */
public enum ECombineRoomMsgType implements WireEnum {
  /**
   * 未知消息
   */
  DRM_UNKNOWN(0),

  /**
   * pick类型
   */
  DRM_PICK(1),

  /**
   * 结束双人房
   */
  DRM_END_COMBINE_ROOM(2),

  /**
   * 解锁用户信息
   */
  DRM_UNLOCK_USER_INFO(3),

  /**
   * 加载歌曲信息
   */
  DRM_LOAD_MUSIC_INFO(4),

  /**
   * 同步状态
   */
  DRM_SYNC_STATUS(5),

  /**
   * 添加歌曲信息
   */
  DRM_ADD_MUSIC_INFO(6),

  /**
   * 删除歌曲信息
   */
  DRM_DEL_MUSIC_INFO(7),

  /**
   * 发起切换场景
   */
  DRM_REQ_CHANGE_SCENE(8),

  /**
   * 同意切换场景
   */
  DRM_AGREE_CHANGE_SCENE(9),

  /**
   * 选定游戏
   */
  DRM_CHOICE_GAME_TIEM(10),

  /**
   * 开始游戏
   */
  DRM_START_GAME(11),

  /**
   * 换游戏面板
   */
  DRM_CHANGE_GAME_PANEL(12),

  /**
   * 结束游戏
   */
  DRM_END_GAME(13),

  /**
   * 同步状态
   */
  DRM_CR_SYNC_STATUS_V2(14);

  public static final ProtoAdapter<ECombineRoomMsgType> ADAPTER = new ProtoAdapter_ECombineRoomMsgType();

  private final int value;

  ECombineRoomMsgType(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static ECombineRoomMsgType fromValue(int value) {
    switch (value) {
      case 0: return DRM_UNKNOWN;
      case 1: return DRM_PICK;
      case 2: return DRM_END_COMBINE_ROOM;
      case 3: return DRM_UNLOCK_USER_INFO;
      case 4: return DRM_LOAD_MUSIC_INFO;
      case 5: return DRM_SYNC_STATUS;
      case 6: return DRM_ADD_MUSIC_INFO;
      case 7: return DRM_DEL_MUSIC_INFO;
      case 8: return DRM_REQ_CHANGE_SCENE;
      case 9: return DRM_AGREE_CHANGE_SCENE;
      case 10: return DRM_CHOICE_GAME_TIEM;
      case 11: return DRM_START_GAME;
      case 12: return DRM_CHANGE_GAME_PANEL;
      case 13: return DRM_END_GAME;
      case 14: return DRM_CR_SYNC_STATUS_V2;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public ECombineRoomMsgType build() {
      return DRM_UNKNOWN;
    }
  }

  private static final class ProtoAdapter_ECombineRoomMsgType extends EnumAdapter<ECombineRoomMsgType> {
    ProtoAdapter_ECombineRoomMsgType() {
      super(ECombineRoomMsgType.class);
    }

    @Override
    protected ECombineRoomMsgType fromValue(int value) {
      return ECombineRoomMsgType.fromValue(value);
    }
  }
}