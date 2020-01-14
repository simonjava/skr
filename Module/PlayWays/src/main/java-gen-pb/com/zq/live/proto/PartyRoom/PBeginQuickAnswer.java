// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: party_room.proto
package com.zq.live.proto.PartyRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PBeginQuickAnswer extends Message<PBeginQuickAnswer, PBeginQuickAnswer.Builder> {
  public static final ProtoAdapter<PBeginQuickAnswer> ADAPTER = new ProtoAdapter_PBeginQuickAnswer();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_QUICKANSWERTAG = "";

  public static final Long DEFAULT_BEGINTIMEMS = 0L;

  public static final Long DEFAULT_ENDTIMEMS = 0L;

  public static final Long DEFAULT_CREATETIMEMS = 0L;

  /**
   * 抢答标识
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String quickAnswerTag;

  /**
   * 开始时间戳，绝对时间
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long beginTimeMs;

  /**
   * 结束时间戳，绝对时间
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long endTimeMs;

  /**
   * 创建时间戳，绝对时间(创建后，经过倒计时(beginTimeMs-createTimeMs)，再开始抢答)
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long createTimeMs;

  public PBeginQuickAnswer(String quickAnswerTag, Long beginTimeMs, Long endTimeMs,
      Long createTimeMs) {
    this(quickAnswerTag, beginTimeMs, endTimeMs, createTimeMs, ByteString.EMPTY);
  }

  public PBeginQuickAnswer(String quickAnswerTag, Long beginTimeMs, Long endTimeMs,
      Long createTimeMs, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.quickAnswerTag = quickAnswerTag;
    this.beginTimeMs = beginTimeMs;
    this.endTimeMs = endTimeMs;
    this.createTimeMs = createTimeMs;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.quickAnswerTag = quickAnswerTag;
    builder.beginTimeMs = beginTimeMs;
    builder.endTimeMs = endTimeMs;
    builder.createTimeMs = createTimeMs;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PBeginQuickAnswer)) return false;
    PBeginQuickAnswer o = (PBeginQuickAnswer) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(quickAnswerTag, o.quickAnswerTag)
        && Internal.equals(beginTimeMs, o.beginTimeMs)
        && Internal.equals(endTimeMs, o.endTimeMs)
        && Internal.equals(createTimeMs, o.createTimeMs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (quickAnswerTag != null ? quickAnswerTag.hashCode() : 0);
      result = result * 37 + (beginTimeMs != null ? beginTimeMs.hashCode() : 0);
      result = result * 37 + (endTimeMs != null ? endTimeMs.hashCode() : 0);
      result = result * 37 + (createTimeMs != null ? createTimeMs.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (quickAnswerTag != null) builder.append(", quickAnswerTag=").append(quickAnswerTag);
    if (beginTimeMs != null) builder.append(", beginTimeMs=").append(beginTimeMs);
    if (endTimeMs != null) builder.append(", endTimeMs=").append(endTimeMs);
    if (createTimeMs != null) builder.append(", createTimeMs=").append(createTimeMs);
    return builder.replace(0, 2, "PBeginQuickAnswer{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PBeginQuickAnswer.ADAPTER.encode(this);
  }

  public static final PBeginQuickAnswer parseFrom(byte[] data) throws IOException {
    PBeginQuickAnswer c = null;
       c = PBeginQuickAnswer.ADAPTER.decode(data);
    return c;
  }

  /**
   * 抢答标识
   */
  public String getQuickAnswerTag() {
    if(quickAnswerTag==null){
        return DEFAULT_QUICKANSWERTAG;
    }
    return quickAnswerTag;
  }

  /**
   * 开始时间戳，绝对时间
   */
  public Long getBeginTimeMs() {
    if(beginTimeMs==null){
        return DEFAULT_BEGINTIMEMS;
    }
    return beginTimeMs;
  }

  /**
   * 结束时间戳，绝对时间
   */
  public Long getEndTimeMs() {
    if(endTimeMs==null){
        return DEFAULT_ENDTIMEMS;
    }
    return endTimeMs;
  }

  /**
   * 创建时间戳，绝对时间(创建后，经过倒计时(beginTimeMs-createTimeMs)，再开始抢答)
   */
  public Long getCreateTimeMs() {
    if(createTimeMs==null){
        return DEFAULT_CREATETIMEMS;
    }
    return createTimeMs;
  }

  /**
   * 抢答标识
   */
  public boolean hasQuickAnswerTag() {
    return quickAnswerTag!=null;
  }

  /**
   * 开始时间戳，绝对时间
   */
  public boolean hasBeginTimeMs() {
    return beginTimeMs!=null;
  }

  /**
   * 结束时间戳，绝对时间
   */
  public boolean hasEndTimeMs() {
    return endTimeMs!=null;
  }

  /**
   * 创建时间戳，绝对时间(创建后，经过倒计时(beginTimeMs-createTimeMs)，再开始抢答)
   */
  public boolean hasCreateTimeMs() {
    return createTimeMs!=null;
  }

  public static final class Builder extends Message.Builder<PBeginQuickAnswer, Builder> {
    private String quickAnswerTag;

    private Long beginTimeMs;

    private Long endTimeMs;

    private Long createTimeMs;

    public Builder() {
    }

    /**
     * 抢答标识
     */
    public Builder setQuickAnswerTag(String quickAnswerTag) {
      this.quickAnswerTag = quickAnswerTag;
      return this;
    }

    /**
     * 开始时间戳，绝对时间
     */
    public Builder setBeginTimeMs(Long beginTimeMs) {
      this.beginTimeMs = beginTimeMs;
      return this;
    }

    /**
     * 结束时间戳，绝对时间
     */
    public Builder setEndTimeMs(Long endTimeMs) {
      this.endTimeMs = endTimeMs;
      return this;
    }

    /**
     * 创建时间戳，绝对时间(创建后，经过倒计时(beginTimeMs-createTimeMs)，再开始抢答)
     */
    public Builder setCreateTimeMs(Long createTimeMs) {
      this.createTimeMs = createTimeMs;
      return this;
    }

    @Override
    public PBeginQuickAnswer build() {
      return new PBeginQuickAnswer(quickAnswerTag, beginTimeMs, endTimeMs, createTimeMs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PBeginQuickAnswer extends ProtoAdapter<PBeginQuickAnswer> {
    public ProtoAdapter_PBeginQuickAnswer() {
      super(FieldEncoding.LENGTH_DELIMITED, PBeginQuickAnswer.class);
    }

    @Override
    public int encodedSize(PBeginQuickAnswer value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.quickAnswerTag)
          + ProtoAdapter.SINT64.encodedSizeWithTag(2, value.beginTimeMs)
          + ProtoAdapter.SINT64.encodedSizeWithTag(3, value.endTimeMs)
          + ProtoAdapter.SINT64.encodedSizeWithTag(4, value.createTimeMs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PBeginQuickAnswer value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.quickAnswerTag);
      ProtoAdapter.SINT64.encodeWithTag(writer, 2, value.beginTimeMs);
      ProtoAdapter.SINT64.encodeWithTag(writer, 3, value.endTimeMs);
      ProtoAdapter.SINT64.encodeWithTag(writer, 4, value.createTimeMs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PBeginQuickAnswer decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setQuickAnswerTag(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setBeginTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 3: builder.setEndTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 4: builder.setCreateTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
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
    public PBeginQuickAnswer redact(PBeginQuickAnswer value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
