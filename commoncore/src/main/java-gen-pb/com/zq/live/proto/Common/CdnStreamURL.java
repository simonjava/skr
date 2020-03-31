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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class CdnStreamURL extends Message<CdnStreamURL, CdnStreamURL.Builder> {
  public static final ProtoAdapter<CdnStreamURL> ADAPTER = new ProtoAdapter_CdnStreamURL();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_TAG = "";

  public static final String DEFAULT_URL = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String tag;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String url;

  public CdnStreamURL(String tag, String url) {
    this(tag, url, ByteString.EMPTY);
  }

  public CdnStreamURL(String tag, String url, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.tag = tag;
    this.url = url;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.tag = tag;
    builder.url = url;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CdnStreamURL)) return false;
    CdnStreamURL o = (CdnStreamURL) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(tag, o.tag)
        && Internal.equals(url, o.url);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (tag != null ? tag.hashCode() : 0);
      result = result * 37 + (url != null ? url.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (tag != null) builder.append(", tag=").append(tag);
    if (url != null) builder.append(", url=").append(url);
    return builder.replace(0, 2, "CdnStreamURL{").append('}').toString();
  }

  public byte[] toByteArray() {
    return CdnStreamURL.ADAPTER.encode(this);
  }

  public static final CdnStreamURL parseFrom(byte[] data) throws IOException {
    CdnStreamURL c = null;
       c = CdnStreamURL.ADAPTER.decode(data);
    return c;
  }

  public String getTag() {
    if(tag==null){
        return DEFAULT_TAG;
    }
    return tag;
  }

  public String getUrl() {
    if(url==null){
        return DEFAULT_URL;
    }
    return url;
  }

  public boolean hasTag() {
    return tag!=null;
  }

  public boolean hasUrl() {
    return url!=null;
  }

  public static final class Builder extends Message.Builder<CdnStreamURL, Builder> {
    private String tag;

    private String url;

    public Builder() {
    }

    public Builder setTag(String tag) {
      this.tag = tag;
      return this;
    }

    public Builder setUrl(String url) {
      this.url = url;
      return this;
    }

    @Override
    public CdnStreamURL build() {
      return new CdnStreamURL(tag, url, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_CdnStreamURL extends ProtoAdapter<CdnStreamURL> {
    public ProtoAdapter_CdnStreamURL() {
      super(FieldEncoding.LENGTH_DELIMITED, CdnStreamURL.class);
    }

    @Override
    public int encodedSize(CdnStreamURL value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.tag)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.url)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, CdnStreamURL value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.tag);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.url);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public CdnStreamURL decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTag(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setUrl(ProtoAdapter.STRING.decode(reader)); break;
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
    public CdnStreamURL redact(CdnStreamURL value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
