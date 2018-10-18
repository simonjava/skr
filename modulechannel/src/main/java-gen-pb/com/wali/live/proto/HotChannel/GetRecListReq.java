// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: HotChannel.proto
package com.wali.live.proto.HotChannel;

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

/**
 * *************************************** zhibo.recommend.reclist ***************************************
 * 根据当前观看的直播，关注等信息，获取推荐的直播列表
 */
public final class GetRecListReq extends Message<GetRecListReq, GetRecListReq.Builder> {
  public static final ProtoAdapter<GetRecListReq> ADAPTER = new ProtoAdapter_GetRecListReq();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_VIEWERID = 0L;

  public static final Long DEFAULT_ANCHORID = 0L;

  public static final String DEFAULT_PACKAGENAME = "";

  public static final Long DEFAULT_GAMEID = 0L;

  public static final Integer DEFAULT_RECTYPE = 0;

  public static final Integer DEFAULT_REQFROM = 0;

  /**
   * 观众id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long viewerId;

  /**
   * 主播id
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long anchorId;

  /**
   * app包名
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String packageName;

  /**
   * 游戏id
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long gameId;

  /**
   * 推荐类型，决定内容，1=根据当前观看的游戏直播推荐，2=我的关注
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer recType;

  /**
   * 请求来源，决定样式，1=游戏聊天室-更多直播，2=游戏全屏
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer reqFrom;

  public GetRecListReq(Long viewerId, Long anchorId, String packageName, Long gameId,
      Integer recType, Integer reqFrom) {
    this(viewerId, anchorId, packageName, gameId, recType, reqFrom, ByteString.EMPTY);
  }

  public GetRecListReq(Long viewerId, Long anchorId, String packageName, Long gameId,
      Integer recType, Integer reqFrom, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.viewerId = viewerId;
    this.anchorId = anchorId;
    this.packageName = packageName;
    this.gameId = gameId;
    this.recType = recType;
    this.reqFrom = reqFrom;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.viewerId = viewerId;
    builder.anchorId = anchorId;
    builder.packageName = packageName;
    builder.gameId = gameId;
    builder.recType = recType;
    builder.reqFrom = reqFrom;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetRecListReq)) return false;
    GetRecListReq o = (GetRecListReq) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(viewerId, o.viewerId)
        && Internal.equals(anchorId, o.anchorId)
        && Internal.equals(packageName, o.packageName)
        && Internal.equals(gameId, o.gameId)
        && Internal.equals(recType, o.recType)
        && Internal.equals(reqFrom, o.reqFrom);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (viewerId != null ? viewerId.hashCode() : 0);
      result = result * 37 + (anchorId != null ? anchorId.hashCode() : 0);
      result = result * 37 + (packageName != null ? packageName.hashCode() : 0);
      result = result * 37 + (gameId != null ? gameId.hashCode() : 0);
      result = result * 37 + (recType != null ? recType.hashCode() : 0);
      result = result * 37 + (reqFrom != null ? reqFrom.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (viewerId != null) builder.append(", viewerId=").append(viewerId);
    if (anchorId != null) builder.append(", anchorId=").append(anchorId);
    if (packageName != null) builder.append(", packageName=").append(packageName);
    if (gameId != null) builder.append(", gameId=").append(gameId);
    if (recType != null) builder.append(", recType=").append(recType);
    if (reqFrom != null) builder.append(", reqFrom=").append(reqFrom);
    return builder.replace(0, 2, "GetRecListReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GetRecListReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<GetRecListReq, Builder> {
    public Long viewerId;

    public Long anchorId;

    public String packageName;

    public Long gameId;

    public Integer recType;

    public Integer reqFrom;

    public Builder() {
    }

    /**
     * 观众id
     */
    public Builder setViewerId(Long viewerId) {
      this.viewerId = viewerId;
      return this;
    }

    /**
     * 主播id
     */
    public Builder setAnchorId(Long anchorId) {
      this.anchorId = anchorId;
      return this;
    }

    /**
     * app包名
     */
    public Builder setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
    }

    /**
     * 游戏id
     */
    public Builder setGameId(Long gameId) {
      this.gameId = gameId;
      return this;
    }

    /**
     * 推荐类型，决定内容，1=根据当前观看的游戏直播推荐，2=我的关注
     */
    public Builder setRecType(Integer recType) {
      this.recType = recType;
      return this;
    }

    /**
     * 请求来源，决定样式，1=游戏聊天室-更多直播，2=游戏全屏
     */
    public Builder setReqFrom(Integer reqFrom) {
      this.reqFrom = reqFrom;
      return this;
    }

    @Override
    public GetRecListReq build() {
      return new GetRecListReq(viewerId, anchorId, packageName, gameId, recType, reqFrom, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetRecListReq extends ProtoAdapter<GetRecListReq> {
    public ProtoAdapter_GetRecListReq() {
      super(FieldEncoding.LENGTH_DELIMITED, GetRecListReq.class);
    }

    @Override
    public int encodedSize(GetRecListReq value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.viewerId)
          + ProtoAdapter.UINT64.encodedSizeWithTag(2, value.anchorId)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.packageName)
          + ProtoAdapter.UINT64.encodedSizeWithTag(4, value.gameId)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.recType)
          + ProtoAdapter.UINT32.encodedSizeWithTag(6, value.reqFrom)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetRecListReq value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.viewerId);
      ProtoAdapter.UINT64.encodeWithTag(writer, 2, value.anchorId);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.packageName);
      ProtoAdapter.UINT64.encodeWithTag(writer, 4, value.gameId);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.recType);
      ProtoAdapter.UINT32.encodeWithTag(writer, 6, value.reqFrom);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetRecListReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setViewerId(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setAnchorId(ProtoAdapter.UINT64.decode(reader)); break;
          case 3: builder.setPackageName(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setGameId(ProtoAdapter.UINT64.decode(reader)); break;
          case 5: builder.setRecType(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.setReqFrom(ProtoAdapter.UINT32.decode(reader)); break;
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
    public GetRecListReq redact(GetRecListReq value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
