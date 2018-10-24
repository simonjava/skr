// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveCommon.proto
package com.wali.live.proto.LiveCommon;

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

/**
 * java -jar -Dfile.encoding=UTF-8 ./proto/wire-compiler-2.3.0-SNAPSHOT-jar-with-dependencies.jar \
 * --proto_path=./proto --java_out=./modulewatch/src/main/java-gen-pb/ LiveCommon.proto
 * wiki地址：http://wiki.n.miui.com/pages/viewpage.action?pageId=18996762
 */
public final class UpStreamUrl extends Message<UpStreamUrl, UpStreamUrl.Builder> {
  public static final ProtoAdapter<UpStreamUrl> ADAPTER = new ProtoAdapter_UpStreamUrl();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_URL = "";

  public static final Integer DEFAULT_WEIGHT = 0;

  /**
   * 完整推流url(带wsHost)
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String url;

  /**
   * 权重
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer weight;

  public UpStreamUrl(String url, Integer weight) {
    this(url, weight, ByteString.EMPTY);
  }

  public UpStreamUrl(String url, Integer weight, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.url = url;
    this.weight = weight;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.url = url;
    builder.weight = weight;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UpStreamUrl)) return false;
    UpStreamUrl o = (UpStreamUrl) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(url, o.url)
        && Internal.equals(weight, o.weight);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (url != null ? url.hashCode() : 0);
      result = result * 37 + (weight != null ? weight.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (url != null) builder.append(", url=").append(url);
    if (weight != null) builder.append(", weight=").append(weight);
    return builder.replace(0, 2, "UpStreamUrl{").append('}').toString();
  }

  public byte[] toByteArray() {
    return UpStreamUrl.ADAPTER.encode(this);
  }

  public static final UpStreamUrl parseFrom(byte[] data) throws IOException {
    UpStreamUrl c = null;
       c = UpStreamUrl.ADAPTER.decode(data);
    return c;
  }

  /**
   * 完整推流url(带wsHost)
   */
  public String getUrl() {
    if(url==null){
        return DEFAULT_URL;
    }
    return url;
  }

  /**
   * 权重
   */
  public Integer getWeight() {
    if(weight==null){
        return DEFAULT_WEIGHT;
    }
    return weight;
  }

  /**
   * 完整推流url(带wsHost)
   */
  public boolean hasUrl() {
    return url!=null;
  }

  /**
   * 权重
   */
  public boolean hasWeight() {
    return weight!=null;
  }

  public static final class Builder extends Message.Builder<UpStreamUrl, Builder> {
    public String url;

    public Integer weight;

    public Builder() {
    }

    /**
     * 完整推流url(带wsHost)
     */
    public Builder setUrl(String url) {
      this.url = url;
      return this;
    }

    /**
     * 权重
     */
    public Builder setWeight(Integer weight) {
      this.weight = weight;
      return this;
    }

    @Override
    public UpStreamUrl build() {
      return new UpStreamUrl(url, weight, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_UpStreamUrl extends ProtoAdapter<UpStreamUrl> {
    public ProtoAdapter_UpStreamUrl() {
      super(FieldEncoding.LENGTH_DELIMITED, UpStreamUrl.class);
    }

    @Override
    public int encodedSize(UpStreamUrl value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.url)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.weight)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, UpStreamUrl value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.url);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.weight);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public UpStreamUrl decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setWeight(ProtoAdapter.UINT32.decode(reader)); break;
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
    public UpStreamUrl redact(UpStreamUrl value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
