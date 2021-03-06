// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: common.proto
package com.zq.live.proto.Common;

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
 * 段位描述
 */
public final class UserRanking extends Message<UserRanking, UserRanking.Builder> {
  public static final ProtoAdapter<UserRanking> ADAPTER = new ProtoAdapter_UserRanking();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_MAINRANKING = 0;

  public static final Integer DEFAULT_SUBRANKING = 0;

  public static final Long DEFAULT_CURREXP = 0L;

  public static final Long DEFAULT_MAXEXP = 0L;

  public static final String DEFAULT_RANKINGDESC = "";

  /**
   * 主段位
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer mainRanking;

  /**
   * 子段位
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer subRanking;

  /**
   * 进度条当前分值
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  private final Long currExp;

  /**
   * 进度条总分值
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  private final Long maxExp;

  /**
   * 段位描述。例如：白银歌者4段
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String rankingDesc;

  public UserRanking(Integer mainRanking, Integer subRanking, Long currExp, Long maxExp,
      String rankingDesc) {
    this(mainRanking, subRanking, currExp, maxExp, rankingDesc, ByteString.EMPTY);
  }

  public UserRanking(Integer mainRanking, Integer subRanking, Long currExp, Long maxExp,
      String rankingDesc, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.mainRanking = mainRanking;
    this.subRanking = subRanking;
    this.currExp = currExp;
    this.maxExp = maxExp;
    this.rankingDesc = rankingDesc;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.mainRanking = mainRanking;
    builder.subRanking = subRanking;
    builder.currExp = currExp;
    builder.maxExp = maxExp;
    builder.rankingDesc = rankingDesc;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserRanking)) return false;
    UserRanking o = (UserRanking) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(mainRanking, o.mainRanking)
        && Internal.equals(subRanking, o.subRanking)
        && Internal.equals(currExp, o.currExp)
        && Internal.equals(maxExp, o.maxExp)
        && Internal.equals(rankingDesc, o.rankingDesc);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (mainRanking != null ? mainRanking.hashCode() : 0);
      result = result * 37 + (subRanking != null ? subRanking.hashCode() : 0);
      result = result * 37 + (currExp != null ? currExp.hashCode() : 0);
      result = result * 37 + (maxExp != null ? maxExp.hashCode() : 0);
      result = result * 37 + (rankingDesc != null ? rankingDesc.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (mainRanking != null) builder.append(", mainRanking=").append(mainRanking);
    if (subRanking != null) builder.append(", subRanking=").append(subRanking);
    if (currExp != null) builder.append(", currExp=").append(currExp);
    if (maxExp != null) builder.append(", maxExp=").append(maxExp);
    if (rankingDesc != null) builder.append(", rankingDesc=").append(rankingDesc);
    return builder.replace(0, 2, "UserRanking{").append('}').toString();
  }

  public byte[] toByteArray() {
    return UserRanking.ADAPTER.encode(this);
  }

  public static final UserRanking parseFrom(byte[] data) throws IOException {
    UserRanking c = null;
       c = UserRanking.ADAPTER.decode(data);
    return c;
  }

  /**
   * 主段位
   */
  public Integer getMainRanking() {
    if(mainRanking==null){
        return DEFAULT_MAINRANKING;
    }
    return mainRanking;
  }

  /**
   * 子段位
   */
  public Integer getSubRanking() {
    if(subRanking==null){
        return DEFAULT_SUBRANKING;
    }
    return subRanking;
  }

  /**
   * 进度条当前分值
   */
  public Long getCurrExp() {
    if(currExp==null){
        return DEFAULT_CURREXP;
    }
    return currExp;
  }

  /**
   * 进度条总分值
   */
  public Long getMaxExp() {
    if(maxExp==null){
        return DEFAULT_MAXEXP;
    }
    return maxExp;
  }

  /**
   * 段位描述。例如：白银歌者4段
   */
  public String getRankingDesc() {
    if(rankingDesc==null){
        return DEFAULT_RANKINGDESC;
    }
    return rankingDesc;
  }

  /**
   * 主段位
   */
  public boolean hasMainRanking() {
    return mainRanking!=null;
  }

  /**
   * 子段位
   */
  public boolean hasSubRanking() {
    return subRanking!=null;
  }

  /**
   * 进度条当前分值
   */
  public boolean hasCurrExp() {
    return currExp!=null;
  }

  /**
   * 进度条总分值
   */
  public boolean hasMaxExp() {
    return maxExp!=null;
  }

  /**
   * 段位描述。例如：白银歌者4段
   */
  public boolean hasRankingDesc() {
    return rankingDesc!=null;
  }

  public static final class Builder extends Message.Builder<UserRanking, Builder> {
    private Integer mainRanking;

    private Integer subRanking;

    private Long currExp;

    private Long maxExp;

    private String rankingDesc;

    public Builder() {
    }

    /**
     * 主段位
     */
    public Builder setMainRanking(Integer mainRanking) {
      this.mainRanking = mainRanking;
      return this;
    }

    /**
     * 子段位
     */
    public Builder setSubRanking(Integer subRanking) {
      this.subRanking = subRanking;
      return this;
    }

    /**
     * 进度条当前分值
     */
    public Builder setCurrExp(Long currExp) {
      this.currExp = currExp;
      return this;
    }

    /**
     * 进度条总分值
     */
    public Builder setMaxExp(Long maxExp) {
      this.maxExp = maxExp;
      return this;
    }

    /**
     * 段位描述。例如：白银歌者4段
     */
    public Builder setRankingDesc(String rankingDesc) {
      this.rankingDesc = rankingDesc;
      return this;
    }

    @Override
    public UserRanking build() {
      return new UserRanking(mainRanking, subRanking, currExp, maxExp, rankingDesc, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_UserRanking extends ProtoAdapter<UserRanking> {
    public ProtoAdapter_UserRanking() {
      super(FieldEncoding.LENGTH_DELIMITED, UserRanking.class);
    }

    @Override
    public int encodedSize(UserRanking value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.mainRanking)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.subRanking)
          + ProtoAdapter.INT64.encodedSizeWithTag(3, value.currExp)
          + ProtoAdapter.INT64.encodedSizeWithTag(4, value.maxExp)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.rankingDesc)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, UserRanking value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.mainRanking);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.subRanking);
      ProtoAdapter.INT64.encodeWithTag(writer, 3, value.currExp);
      ProtoAdapter.INT64.encodeWithTag(writer, 4, value.maxExp);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.rankingDesc);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public UserRanking decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setMainRanking(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setSubRanking(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setCurrExp(ProtoAdapter.INT64.decode(reader)); break;
          case 4: builder.setMaxExp(ProtoAdapter.INT64.decode(reader)); break;
          case 5: builder.setRankingDesc(ProtoAdapter.STRING.decode(reader)); break;
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
    public UserRanking redact(UserRanking value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
