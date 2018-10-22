// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Banner.proto
package com.wali.live.proto.Banner;

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
import java.util.List;
import okio.ByteString;

public final class SyncBannerRsp extends Message<SyncBannerRsp, SyncBannerRsp.Builder> {
  public static final ProtoAdapter<SyncBannerRsp> ADAPTER = new ProtoAdapter_SyncBannerRsp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RETCODE = 0;

  public static final Long DEFAULT_LASTUPDATETS = 0L;

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
   * 广告位列表
   */
  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.Banner.Banner#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<Banner> banner;

  /**
   * 上一次拉取的最大banner修改时间
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long lastUpdateTs;

  public SyncBannerRsp(Integer retCode, List<Banner> banner, Long lastUpdateTs) {
    this(retCode, banner, lastUpdateTs, ByteString.EMPTY);
  }

  public SyncBannerRsp(Integer retCode, List<Banner> banner, Long lastUpdateTs,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.retCode = retCode;
    this.banner = Internal.immutableCopyOf("banner", banner);
    this.lastUpdateTs = lastUpdateTs;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.retCode = retCode;
    builder.banner = Internal.copyOf("banner", banner);
    builder.lastUpdateTs = lastUpdateTs;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SyncBannerRsp)) return false;
    SyncBannerRsp o = (SyncBannerRsp) other;
    return unknownFields().equals(o.unknownFields())
        && retCode.equals(o.retCode)
        && banner.equals(o.banner)
        && Internal.equals(lastUpdateTs, o.lastUpdateTs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + retCode.hashCode();
      result = result * 37 + banner.hashCode();
      result = result * 37 + (lastUpdateTs != null ? lastUpdateTs.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", retCode=").append(retCode);
    if (!banner.isEmpty()) builder.append(", banner=").append(banner);
    if (lastUpdateTs != null) builder.append(", lastUpdateTs=").append(lastUpdateTs);
    return builder.replace(0, 2, "SyncBannerRsp{").append('}').toString();
  }

  public static final SyncBannerRsp parseFrom(byte[] data) throws IOException {
    SyncBannerRsp c = null;
       c = SyncBannerRsp.ADAPTER.decode(data);
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
   * 广告位列表
   */
  public List<Banner> getBannerList() {
    if(banner==null){
        return new java.util.ArrayList<Banner>();
    }
    return banner;
  }

  /**
   * 上一次拉取的最大banner修改时间
   */
  public Long getLastUpdateTs() {
    if(lastUpdateTs==null){
        return DEFAULT_LASTUPDATETS;
    }
    return lastUpdateTs;
  }

  /**
   * 0:表示成功
   */
  public boolean hasRetCode() {
    return retCode!=null;
  }

  /**
   * 广告位列表
   */
  public boolean hasBannerList() {
    return banner!=null;
  }

  /**
   * 上一次拉取的最大banner修改时间
   */
  public boolean hasLastUpdateTs() {
    return lastUpdateTs!=null;
  }

  public static final class Builder extends Message.Builder<SyncBannerRsp, Builder> {
    public Integer retCode;

    public List<Banner> banner;

    public Long lastUpdateTs;

    public Builder() {
      banner = Internal.newMutableList();
    }

    /**
     * 0:表示成功
     */
    public Builder setRetCode(Integer retCode) {
      this.retCode = retCode;
      return this;
    }

    /**
     * 广告位列表
     */
    public Builder addAllBanner(List<Banner> banner) {
      Internal.checkElementsNotNull(banner);
      this.banner = banner;
      return this;
    }

    /**
     * 上一次拉取的最大banner修改时间
     */
    public Builder setLastUpdateTs(Long lastUpdateTs) {
      this.lastUpdateTs = lastUpdateTs;
      return this;
    }

    @Override
    public SyncBannerRsp build() {
      return new SyncBannerRsp(retCode, banner, lastUpdateTs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_SyncBannerRsp extends ProtoAdapter<SyncBannerRsp> {
    public ProtoAdapter_SyncBannerRsp() {
      super(FieldEncoding.LENGTH_DELIMITED, SyncBannerRsp.class);
    }

    @Override
    public int encodedSize(SyncBannerRsp value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.retCode)
          + Banner.ADAPTER.asRepeated().encodedSizeWithTag(2, value.banner)
          + ProtoAdapter.UINT64.encodedSizeWithTag(3, value.lastUpdateTs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, SyncBannerRsp value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.retCode);
      Banner.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.banner);
      ProtoAdapter.UINT64.encodeWithTag(writer, 3, value.lastUpdateTs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public SyncBannerRsp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRetCode(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.banner.add(Banner.ADAPTER.decode(reader)); break;
          case 3: builder.setLastUpdateTs(ProtoAdapter.UINT64.decode(reader)); break;
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
    public SyncBannerRsp redact(SyncBannerRsp value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.banner, Banner.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
