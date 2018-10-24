// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveMessage.proto
package com.wali.live.proto.LiveMessage;

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
 * 拉取未读消息请求
 */
public final class SyncUnreadRequest extends Message<SyncUnreadRequest, SyncUnreadRequest.Builder> {
  public static final ProtoAdapter<SyncUnreadRequest> ADAPTER = new ProtoAdapter_SyncUnreadRequest();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_FROM_USER = 0L;

  public static final Long DEFAULT_CID = 0L;

  public static final String DEFAULT_PAGE_ID = "";

  public static final Integer DEFAULT_LIMIT = 0;

  public static final Integer DEFAULT_FOLLOW_TYPE = 0;

  /**
   * 拉取未读消息的发送者
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long from_user;

  /**
   * 客户端消息ID
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long cid;

  /**
   * 第一次传"",上一次拉取unread的sync_id
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String page_id;

  /**
   * 拉取数量
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer limit;

  /**
   * 1:未关注 0:已关注
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer follow_type;

  public SyncUnreadRequest(Long from_user, Long cid, String page_id, Integer limit,
      Integer follow_type) {
    this(from_user, cid, page_id, limit, follow_type, ByteString.EMPTY);
  }

  public SyncUnreadRequest(Long from_user, Long cid, String page_id, Integer limit,
      Integer follow_type, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.from_user = from_user;
    this.cid = cid;
    this.page_id = page_id;
    this.limit = limit;
    this.follow_type = follow_type;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.from_user = from_user;
    builder.cid = cid;
    builder.page_id = page_id;
    builder.limit = limit;
    builder.follow_type = follow_type;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SyncUnreadRequest)) return false;
    SyncUnreadRequest o = (SyncUnreadRequest) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(from_user, o.from_user)
        && Internal.equals(cid, o.cid)
        && Internal.equals(page_id, o.page_id)
        && Internal.equals(limit, o.limit)
        && Internal.equals(follow_type, o.follow_type);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (from_user != null ? from_user.hashCode() : 0);
      result = result * 37 + (cid != null ? cid.hashCode() : 0);
      result = result * 37 + (page_id != null ? page_id.hashCode() : 0);
      result = result * 37 + (limit != null ? limit.hashCode() : 0);
      result = result * 37 + (follow_type != null ? follow_type.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (from_user != null) builder.append(", from_user=").append(from_user);
    if (cid != null) builder.append(", cid=").append(cid);
    if (page_id != null) builder.append(", page_id=").append(page_id);
    if (limit != null) builder.append(", limit=").append(limit);
    if (follow_type != null) builder.append(", follow_type=").append(follow_type);
    return builder.replace(0, 2, "SyncUnreadRequest{").append('}').toString();
  }

  public byte[] toByteArray() {
    return SyncUnreadRequest.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<SyncUnreadRequest, Builder> {
    public Long from_user;

    public Long cid;

    public String page_id;

    public Integer limit;

    public Integer follow_type;

    public Builder() {
    }

    /**
     * 拉取未读消息的发送者
     */
    public Builder setFromUser(Long from_user) {
      this.from_user = from_user;
      return this;
    }

    /**
     * 客户端消息ID
     */
    public Builder setCid(Long cid) {
      this.cid = cid;
      return this;
    }

    /**
     * 第一次传"",上一次拉取unread的sync_id
     */
    public Builder setPageId(String page_id) {
      this.page_id = page_id;
      return this;
    }

    /**
     * 拉取数量
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * 1:未关注 0:已关注
     */
    public Builder setFollowType(Integer follow_type) {
      this.follow_type = follow_type;
      return this;
    }

    @Override
    public SyncUnreadRequest build() {
      return new SyncUnreadRequest(from_user, cid, page_id, limit, follow_type, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_SyncUnreadRequest extends ProtoAdapter<SyncUnreadRequest> {
    public ProtoAdapter_SyncUnreadRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, SyncUnreadRequest.class);
    }

    @Override
    public int encodedSize(SyncUnreadRequest value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.from_user)
          + ProtoAdapter.UINT64.encodedSizeWithTag(2, value.cid)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.page_id)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.limit)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.follow_type)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, SyncUnreadRequest value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.from_user);
      ProtoAdapter.UINT64.encodeWithTag(writer, 2, value.cid);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.page_id);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.limit);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.follow_type);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public SyncUnreadRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFromUser(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setCid(ProtoAdapter.UINT64.decode(reader)); break;
          case 3: builder.setPageId(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setLimit(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setFollowType(ProtoAdapter.UINT32.decode(reader)); break;
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
    public SyncUnreadRequest redact(SyncUnreadRequest value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
