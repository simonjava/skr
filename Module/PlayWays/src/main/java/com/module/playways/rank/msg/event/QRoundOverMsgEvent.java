// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.module.playways.rank.msg.event;

import com.module.playways.rank.msg.BasePushInfo;
import com.zq.live.proto.Room.EQRoundOverReason;
import com.zq.live.proto.Room.EQRoundResultType;
import com.zq.live.proto.Room.QRoundInfo;
import com.zq.live.proto.Room.QRoundOverMsg;

public final class QRoundOverMsgEvent {
  public BasePushInfo info;
  /**
   * 本轮次结束的毫秒时间戳
   */
  public   Long roundOverTimeMs;

  /**
   * 退出用户的ID
   */
  public   Integer exitUserID;

  /**
   * 切换轮次原因
   */
  public   EQRoundOverReason overReason;

  /**
   * 演唱结果信息
   */
  public   EQRoundResultType resultType;

  /**
   * 下个轮次信息
   */
  public   QRoundInfo nextRound;

  public QRoundOverMsgEvent(BasePushInfo info, QRoundOverMsg qRoundOverMsg) {
    this.info = info;
    this.roundOverTimeMs = qRoundOverMsg.getRoundOverTimeMs();
    this.exitUserID = qRoundOverMsg.getExitUserID();
    this.overReason = qRoundOverMsg.getOverReason();
    this.resultType = qRoundOverMsg.getResultType();
    this.nextRound = qRoundOverMsg.getNextRound();
  }
}
