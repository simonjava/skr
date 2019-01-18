// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.module.playways.rank.msg.event;

import com.module.playways.rank.msg.BasePushInfo;
import com.zq.live.proto.Room.QNoPassSingMsg;

public final class QNoPassSingMsgEvent {
  public BasePushInfo info;
  /**
   * 用户id
   */
  public Integer userID;

  /**
   * 轮次顺序
   */
  public Integer roundSeq;


  public QNoPassSingMsgEvent(BasePushInfo info, QNoPassSingMsg qNoPassSingMsg) {
    this.info = info;
    this.userID = qNoPassSingMsg.getUserID();
    this.roundSeq = qNoPassSingMsg.getRoundSeq();
  }
}
