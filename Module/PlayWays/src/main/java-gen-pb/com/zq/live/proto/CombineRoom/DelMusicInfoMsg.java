// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CombineRoom.proto
package com.zq.live.proto.CombineRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class DelMusicInfoMsg extends Message<DelMusicInfoMsg, DelMusicInfoMsg.Builder> {
  public static final ProtoAdapter<DelMusicInfoMsg> ADAPTER = new ProtoAdapter_DelMusicInfoMsg();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_UNIQTAG = "";

  /**
   * 唯一id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String uniqTag;

  public DelMusicInfoMsg(String uniqTag) {
    this(uniqTag, ByteString.EMPTY);
  }

  public DelMusicInfoMsg(String uniqTag, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uniqTag = uniqTag;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uniqTag = uniqTag;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof DelMusicInfoMsg)) return false;
    DelMusicInfoMsg o = (DelMusicInfoMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(uniqTag, o.uniqTag);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (uniqTag != null ? uniqTag.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (uniqTag != null) builder.append(", uniqTag=").append(uniqTag);
    return builder.replace(0, 2, "DelMusicInfoMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return DelMusicInfoMsg.ADAPTER.encode(this);
  }

  public static final DelMusicInfoMsg parseFrom(byte[] data) throws IOException {
    DelMusicInfoMsg c = null;
       c = DelMusicInfoMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 唯一id
   */
  public String getUniqTag() {
    if(uniqTag==null){
        return DEFAULT_UNIQTAG;
    }
    return uniqTag;
  }

  /**
   * 唯一id
   */
  public boolean hasUniqTag() {
    return uniqTag!=null;
  }

  public static final class Builder extends Message.Builder<DelMusicInfoMsg, Builder> {
    private String uniqTag;

    public Builder() {
    }

    /**
     * 唯一id
     */
    public Builder setUniqTag(String uniqTag) {
      this.uniqTag = uniqTag;
      return this;
    }

    @Override
    public DelMusicInfoMsg build() {
      return new DelMusicInfoMsg(uniqTag, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_DelMusicInfoMsg extends ProtoAdapter<DelMusicInfoMsg> {
    public ProtoAdapter_DelMusicInfoMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, DelMusicInfoMsg.class);
    }

    @Override
    public int encodedSize(DelMusicInfoMsg value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.uniqTag)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, DelMusicInfoMsg value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.uniqTag);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public DelMusicInfoMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUniqTag(ProtoAdapter.STRING.decode(reader)); break;
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
    public DelMusicInfoMsg redact(DelMusicInfoMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
