// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: RadioSignal.proto
package com.wali.live.proto.RadioSignal;

import com.squareup.wire.EnumAdapter;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.lang.Override;

/**
 * 1:apply push, 2: invite push  3: approve push  4: accept push  5: quit push  6: kick push 7: status update 8: mute 9: unmute 10: cancel apply 11:online status
 */
public enum SignalAction implements WireEnum {
  APPLY(1),

  INVITE(2),

  APPROVE(3),

  ACCEPT(4),

  QUIT(5),

  KICK(6),

  STATUS_UPDATE(7),

  MUTE(8),

  UNMUTE(9),

  CANCEL_APPLY(10),

  ONLINE_STATUS(11);

  public static final ProtoAdapter<SignalAction> ADAPTER = new ProtoAdapter_SignalAction();

  private final int value;

  SignalAction(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static SignalAction fromValue(int value) {
    switch (value) {
      case 1: return APPLY;
      case 2: return INVITE;
      case 3: return APPROVE;
      case 4: return ACCEPT;
      case 5: return QUIT;
      case 6: return KICK;
      case 7: return STATUS_UPDATE;
      case 8: return MUTE;
      case 9: return UNMUTE;
      case 10: return CANCEL_APPLY;
      case 11: return ONLINE_STATUS;
      default: return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }

  public static final class Builder {
    public SignalAction build() {
      return APPLY;
    }
  }

  private static final class ProtoAdapter_SignalAction extends EnumAdapter<SignalAction> {
    ProtoAdapter_SignalAction() {
      super(SignalAction.class);
    }

    @Override
    protected SignalAction fromValue(int value) {
      return SignalAction.fromValue(value);
    }
  }
}
