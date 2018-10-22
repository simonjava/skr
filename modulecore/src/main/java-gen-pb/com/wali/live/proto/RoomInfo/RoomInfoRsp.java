// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: RoomInfo.proto
package com.wali.live.proto.RoomInfo;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class RoomInfoRsp extends Message<RoomInfoRsp, RoomInfoRsp.Builder> {
  public static final ProtoAdapter<RoomInfoRsp> ADAPTER = new ProtoAdapter_RoomInfoRsp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RETCODE = 0;

  public static final String DEFAULT_DOWNSTREAMURL = "";

  public static final String DEFAULT_PLAYBACKURL = "";

  public static final String DEFAULT_SHAREURL = "";

  public static final Long DEFAULT_BEGINTIME = 0L;

  public static final String DEFAULT_LIVEID = "";

  public static final Integer DEFAULT_TYPE = 0;

  /**
   * 0:表示成功
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer retCode;

  /**
   * 完整的url拉流地址,根据客户端的ip选择最优
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String downStreamUrl;

  /**
   * 回放的url
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String playbackUrl;

  /**
   * 分享的url
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String shareUrl;

  /**
   * 直播开始时间
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long beginTime;

  /**
   * 房间id
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String liveid;

  /**
   * 类型，0公开，1私密,2 口令,3 门票
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer type;

  /**
   * 用户观看门票直播状态信息
   */
  @WireField(
      tag = 8,
      adapter = "com.wali.live.proto.RoomInfo.TicketLiveStatus#ADAPTER"
  )
  public final TicketLiveStatus ticketStatus;

  /**
   * 游戏包信息
   */
  @WireField(
      tag = 9,
      adapter = "com.wali.live.proto.RoomInfo.GamePackInfo#ADAPTER"
  )
  public final GamePackInfo gamepack_info;

  @WireField(
      tag = 10,
      adapter = "com.wali.live.proto.RoomInfo.ContestInfo#ADAPTER"
  )
  public final ContestInfo contest_info;

  public RoomInfoRsp(Integer retCode, String downStreamUrl, String playbackUrl, String shareUrl,
      Long beginTime, String liveid, Integer type, TicketLiveStatus ticketStatus,
      GamePackInfo gamepack_info, ContestInfo contest_info) {
    this(retCode, downStreamUrl, playbackUrl, shareUrl, beginTime, liveid, type, ticketStatus, gamepack_info, contest_info, ByteString.EMPTY);
  }

  public RoomInfoRsp(Integer retCode, String downStreamUrl, String playbackUrl, String shareUrl,
      Long beginTime, String liveid, Integer type, TicketLiveStatus ticketStatus,
      GamePackInfo gamepack_info, ContestInfo contest_info, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.retCode = retCode;
    this.downStreamUrl = downStreamUrl;
    this.playbackUrl = playbackUrl;
    this.shareUrl = shareUrl;
    this.beginTime = beginTime;
    this.liveid = liveid;
    this.type = type;
    this.ticketStatus = ticketStatus;
    this.gamepack_info = gamepack_info;
    this.contest_info = contest_info;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.retCode = retCode;
    builder.downStreamUrl = downStreamUrl;
    builder.playbackUrl = playbackUrl;
    builder.shareUrl = shareUrl;
    builder.beginTime = beginTime;
    builder.liveid = liveid;
    builder.type = type;
    builder.ticketStatus = ticketStatus;
    builder.gamepack_info = gamepack_info;
    builder.contest_info = contest_info;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RoomInfoRsp)) return false;
    RoomInfoRsp o = (RoomInfoRsp) other;
    return unknownFields().equals(o.unknownFields())
        && retCode.equals(o.retCode)
        && Internal.equals(downStreamUrl, o.downStreamUrl)
        && Internal.equals(playbackUrl, o.playbackUrl)
        && Internal.equals(shareUrl, o.shareUrl)
        && Internal.equals(beginTime, o.beginTime)
        && Internal.equals(liveid, o.liveid)
        && Internal.equals(type, o.type)
        && Internal.equals(ticketStatus, o.ticketStatus)
        && Internal.equals(gamepack_info, o.gamepack_info)
        && Internal.equals(contest_info, o.contest_info);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + retCode.hashCode();
      result = result * 37 + (downStreamUrl != null ? downStreamUrl.hashCode() : 0);
      result = result * 37 + (playbackUrl != null ? playbackUrl.hashCode() : 0);
      result = result * 37 + (shareUrl != null ? shareUrl.hashCode() : 0);
      result = result * 37 + (beginTime != null ? beginTime.hashCode() : 0);
      result = result * 37 + (liveid != null ? liveid.hashCode() : 0);
      result = result * 37 + (type != null ? type.hashCode() : 0);
      result = result * 37 + (ticketStatus != null ? ticketStatus.hashCode() : 0);
      result = result * 37 + (gamepack_info != null ? gamepack_info.hashCode() : 0);
      result = result * 37 + (contest_info != null ? contest_info.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", retCode=").append(retCode);
    if (downStreamUrl != null) builder.append(", downStreamUrl=").append(downStreamUrl);
    if (playbackUrl != null) builder.append(", playbackUrl=").append(playbackUrl);
    if (shareUrl != null) builder.append(", shareUrl=").append(shareUrl);
    if (beginTime != null) builder.append(", beginTime=").append(beginTime);
    if (liveid != null) builder.append(", liveid=").append(liveid);
    if (type != null) builder.append(", type=").append(type);
    if (ticketStatus != null) builder.append(", ticketStatus=").append(ticketStatus);
    if (gamepack_info != null) builder.append(", gamepack_info=").append(gamepack_info);
    if (contest_info != null) builder.append(", contest_info=").append(contest_info);
    return builder.replace(0, 2, "RoomInfoRsp{").append('}').toString();
  }

  public static final RoomInfoRsp parseFrom(byte[] data) throws IOException {
    RoomInfoRsp c = null;
       c = RoomInfoRsp.ADAPTER.decode(data);
    return c;
  }

  /**
   * 0:表示成功
   */
  public Integer getRetCode() {
    if(retCode==null){
        return DEFAULT_RETCODE;
    }
    return retCode;
  }

  /**
   * 完整的url拉流地址,根据客户端的ip选择最优
   */
  public String getDownStreamUrl() {
    if(downStreamUrl==null){
        return DEFAULT_DOWNSTREAMURL;
    }
    return downStreamUrl;
  }

  /**
   * 回放的url
   */
  public String getPlaybackUrl() {
    if(playbackUrl==null){
        return DEFAULT_PLAYBACKURL;
    }
    return playbackUrl;
  }

  /**
   * 分享的url
   */
  public String getShareUrl() {
    if(shareUrl==null){
        return DEFAULT_SHAREURL;
    }
    return shareUrl;
  }

  /**
   * 直播开始时间
   */
  public Long getBeginTime() {
    if(beginTime==null){
        return DEFAULT_BEGINTIME;
    }
    return beginTime;
  }

  /**
   * 房间id
   */
  public String getLiveid() {
    if(liveid==null){
        return DEFAULT_LIVEID;
    }
    return liveid;
  }

  /**
   * 类型，0公开，1私密,2 口令,3 门票
   */
  public Integer getType() {
    if(type==null){
        return DEFAULT_TYPE;
    }
    return type;
  }

  /**
   * 用户观看门票直播状态信息
   */
  public TicketLiveStatus getTicketStatus() {
    if(ticketStatus==null){
        return new TicketLiveStatus.Builder().build();
    }
    return ticketStatus;
  }

  /**
   * 游戏包信息
   */
  public GamePackInfo getGamepackInfo() {
    if(gamepack_info==null){
        return new GamePackInfo.Builder().build();
    }
    return gamepack_info;
  }

  public ContestInfo getContestInfo() {
    if(contest_info==null){
        return new ContestInfo.Builder().build();
    }
    return contest_info;
  }

  /**
   * 0:表示成功
   */
  public boolean hasRetCode() {
    return retCode!=null;
  }

  /**
   * 完整的url拉流地址,根据客户端的ip选择最优
   */
  public boolean hasDownStreamUrl() {
    return downStreamUrl!=null;
  }

  /**
   * 回放的url
   */
  public boolean hasPlaybackUrl() {
    return playbackUrl!=null;
  }

  /**
   * 分享的url
   */
  public boolean hasShareUrl() {
    return shareUrl!=null;
  }

  /**
   * 直播开始时间
   */
  public boolean hasBeginTime() {
    return beginTime!=null;
  }

  /**
   * 房间id
   */
  public boolean hasLiveid() {
    return liveid!=null;
  }

  /**
   * 类型，0公开，1私密,2 口令,3 门票
   */
  public boolean hasType() {
    return type!=null;
  }

  /**
   * 用户观看门票直播状态信息
   */
  public boolean hasTicketStatus() {
    return ticketStatus!=null;
  }

  /**
   * 游戏包信息
   */
  public boolean hasGamepackInfo() {
    return gamepack_info!=null;
  }

  public boolean hasContestInfo() {
    return contest_info!=null;
  }

  public static final class Builder extends Message.Builder<RoomInfoRsp, Builder> {
    public Integer retCode;

    public String downStreamUrl;

    public String playbackUrl;

    public String shareUrl;

    public Long beginTime;

    public String liveid;

    public Integer type;

    public TicketLiveStatus ticketStatus;

    public GamePackInfo gamepack_info;

    public ContestInfo contest_info;

    public Builder() {
    }

    /**
     * 0:表示成功
     */
    public Builder setRetCode(Integer retCode) {
      this.retCode = retCode;
      return this;
    }

    /**
     * 完整的url拉流地址,根据客户端的ip选择最优
     */
    public Builder setDownStreamUrl(String downStreamUrl) {
      this.downStreamUrl = downStreamUrl;
      return this;
    }

    /**
     * 回放的url
     */
    public Builder setPlaybackUrl(String playbackUrl) {
      this.playbackUrl = playbackUrl;
      return this;
    }

    /**
     * 分享的url
     */
    public Builder setShareUrl(String shareUrl) {
      this.shareUrl = shareUrl;
      return this;
    }

    /**
     * 直播开始时间
     */
    public Builder setBeginTime(Long beginTime) {
      this.beginTime = beginTime;
      return this;
    }

    /**
     * 房间id
     */
    public Builder setLiveid(String liveid) {
      this.liveid = liveid;
      return this;
    }

    /**
     * 类型，0公开，1私密,2 口令,3 门票
     */
    public Builder setType(Integer type) {
      this.type = type;
      return this;
    }

    /**
     * 用户观看门票直播状态信息
     */
    public Builder setTicketStatus(TicketLiveStatus ticketStatus) {
      this.ticketStatus = ticketStatus;
      return this;
    }

    /**
     * 游戏包信息
     */
    public Builder setGamepackInfo(GamePackInfo gamepack_info) {
      this.gamepack_info = gamepack_info;
      return this;
    }

    public Builder setContestInfo(ContestInfo contest_info) {
      this.contest_info = contest_info;
      return this;
    }

    @Override
    public RoomInfoRsp build() {
      return new RoomInfoRsp(retCode, downStreamUrl, playbackUrl, shareUrl, beginTime, liveid, type, ticketStatus, gamepack_info, contest_info, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RoomInfoRsp extends ProtoAdapter<RoomInfoRsp> {
    public ProtoAdapter_RoomInfoRsp() {
      super(FieldEncoding.LENGTH_DELIMITED, RoomInfoRsp.class);
    }

    @Override
    public int encodedSize(RoomInfoRsp value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.retCode)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.downStreamUrl)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.playbackUrl)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.shareUrl)
          + ProtoAdapter.UINT64.encodedSizeWithTag(5, value.beginTime)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.liveid)
          + ProtoAdapter.UINT32.encodedSizeWithTag(7, value.type)
          + TicketLiveStatus.ADAPTER.encodedSizeWithTag(8, value.ticketStatus)
          + GamePackInfo.ADAPTER.encodedSizeWithTag(9, value.gamepack_info)
          + ContestInfo.ADAPTER.encodedSizeWithTag(10, value.contest_info)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RoomInfoRsp value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.retCode);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.downStreamUrl);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.playbackUrl);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.shareUrl);
      ProtoAdapter.UINT64.encodeWithTag(writer, 5, value.beginTime);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.liveid);
      ProtoAdapter.UINT32.encodeWithTag(writer, 7, value.type);
      TicketLiveStatus.ADAPTER.encodeWithTag(writer, 8, value.ticketStatus);
      GamePackInfo.ADAPTER.encodeWithTag(writer, 9, value.gamepack_info);
      ContestInfo.ADAPTER.encodeWithTag(writer, 10, value.contest_info);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RoomInfoRsp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRetCode(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setDownStreamUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setPlaybackUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setShareUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.setBeginTime(ProtoAdapter.UINT64.decode(reader)); break;
          case 6: builder.setLiveid(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setType(ProtoAdapter.UINT32.decode(reader)); break;
          case 8: builder.setTicketStatus(TicketLiveStatus.ADAPTER.decode(reader)); break;
          case 9: builder.setGamepackInfo(GamePackInfo.ADAPTER.decode(reader)); break;
          case 10: builder.setContestInfo(ContestInfo.ADAPTER.decode(reader)); break;
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
    public RoomInfoRsp redact(RoomInfoRsp value) {
      Builder builder = value.newBuilder();
      if (builder.ticketStatus != null) builder.ticketStatus = TicketLiveStatus.ADAPTER.redact(builder.ticketStatus);
      if (builder.gamepack_info != null) builder.gamepack_info = GamePackInfo.ADAPTER.redact(builder.gamepack_info);
      if (builder.contest_info != null) builder.contest_info = ContestInfo.ADAPTER.redact(builder.contest_info);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
