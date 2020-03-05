// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: battle_room.proto
package com.zq.live.proto.BattleRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class BattleRoomConfig extends Message<BattleRoomConfig, BattleRoomConfig.Builder> {
  public static final ProtoAdapter<BattleRoomConfig> ADAPTER = new ProtoAdapter_BattleRoomConfig();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_TOTALMUSICCNT = 0;

  public static final Integer DEFAULT_HELPCARDCNT = 0;

  public static final Integer DEFAULT_SWITCHCARDCNT = 0;

  /**
   * 总共多少首比赛曲目
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer totalMusicCnt;

  /**
   * 可以使用的帮唱卡数量
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer helpCardCnt;

  /**
   * 可以使用的换歌卡数量
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer switchCardCnt;

  public BattleRoomConfig(Integer totalMusicCnt, Integer helpCardCnt, Integer switchCardCnt) {
    this(totalMusicCnt, helpCardCnt, switchCardCnt, ByteString.EMPTY);
  }

  public BattleRoomConfig(Integer totalMusicCnt, Integer helpCardCnt, Integer switchCardCnt,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.totalMusicCnt = totalMusicCnt;
    this.helpCardCnt = helpCardCnt;
    this.switchCardCnt = switchCardCnt;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.totalMusicCnt = totalMusicCnt;
    builder.helpCardCnt = helpCardCnt;
    builder.switchCardCnt = switchCardCnt;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof BattleRoomConfig)) return false;
    BattleRoomConfig o = (BattleRoomConfig) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(totalMusicCnt, o.totalMusicCnt)
        && Internal.equals(helpCardCnt, o.helpCardCnt)
        && Internal.equals(switchCardCnt, o.switchCardCnt);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (totalMusicCnt != null ? totalMusicCnt.hashCode() : 0);
      result = result * 37 + (helpCardCnt != null ? helpCardCnt.hashCode() : 0);
      result = result * 37 + (switchCardCnt != null ? switchCardCnt.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (totalMusicCnt != null) builder.append(", totalMusicCnt=").append(totalMusicCnt);
    if (helpCardCnt != null) builder.append(", helpCardCnt=").append(helpCardCnt);
    if (switchCardCnt != null) builder.append(", switchCardCnt=").append(switchCardCnt);
    return builder.replace(0, 2, "BattleRoomConfig{").append('}').toString();
  }

  public byte[] toByteArray() {
    return BattleRoomConfig.ADAPTER.encode(this);
  }

  public static final BattleRoomConfig parseFrom(byte[] data) throws IOException {
    BattleRoomConfig c = null;
       c = BattleRoomConfig.ADAPTER.decode(data);
    return c;
  }

  /**
   * 总共多少首比赛曲目
   */
  public Integer getTotalMusicCnt() {
    if(totalMusicCnt==null){
        return DEFAULT_TOTALMUSICCNT;
    }
    return totalMusicCnt;
  }

  /**
   * 可以使用的帮唱卡数量
   */
  public Integer getHelpCardCnt() {
    if(helpCardCnt==null){
        return DEFAULT_HELPCARDCNT;
    }
    return helpCardCnt;
  }

  /**
   * 可以使用的换歌卡数量
   */
  public Integer getSwitchCardCnt() {
    if(switchCardCnt==null){
        return DEFAULT_SWITCHCARDCNT;
    }
    return switchCardCnt;
  }

  /**
   * 总共多少首比赛曲目
   */
  public boolean hasTotalMusicCnt() {
    return totalMusicCnt!=null;
  }

  /**
   * 可以使用的帮唱卡数量
   */
  public boolean hasHelpCardCnt() {
    return helpCardCnt!=null;
  }

  /**
   * 可以使用的换歌卡数量
   */
  public boolean hasSwitchCardCnt() {
    return switchCardCnt!=null;
  }

  public static final class Builder extends Message.Builder<BattleRoomConfig, Builder> {
    private Integer totalMusicCnt;

    private Integer helpCardCnt;

    private Integer switchCardCnt;

    public Builder() {
    }

    /**
     * 总共多少首比赛曲目
     */
    public Builder setTotalMusicCnt(Integer totalMusicCnt) {
      this.totalMusicCnt = totalMusicCnt;
      return this;
    }

    /**
     * 可以使用的帮唱卡数量
     */
    public Builder setHelpCardCnt(Integer helpCardCnt) {
      this.helpCardCnt = helpCardCnt;
      return this;
    }

    /**
     * 可以使用的换歌卡数量
     */
    public Builder setSwitchCardCnt(Integer switchCardCnt) {
      this.switchCardCnt = switchCardCnt;
      return this;
    }

    @Override
    public BattleRoomConfig build() {
      return new BattleRoomConfig(totalMusicCnt, helpCardCnt, switchCardCnt, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_BattleRoomConfig extends ProtoAdapter<BattleRoomConfig> {
    public ProtoAdapter_BattleRoomConfig() {
      super(FieldEncoding.LENGTH_DELIMITED, BattleRoomConfig.class);
    }

    @Override
    public int encodedSize(BattleRoomConfig value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.totalMusicCnt)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.helpCardCnt)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.switchCardCnt)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, BattleRoomConfig value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.totalMusicCnt);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.helpCardCnt);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.switchCardCnt);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public BattleRoomConfig decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTotalMusicCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setHelpCardCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setSwitchCardCnt(ProtoAdapter.UINT32.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public BattleRoomConfig redact(BattleRoomConfig value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}