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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class MedalConfigMessage extends Message<MedalConfigMessage, MedalConfigMessage.Builder> {
  public static final ProtoAdapter<MedalConfigMessage> ADAPTER = new ProtoAdapter_MedalConfigMessage();

  private static final long serialVersionUID = 0L;

  @WireField(
      tag = 1,
      adapter = "com.wali.live.proto.LiveMessage.InnerMedalConfig#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<InnerMedalConfig> before_nickname_config;

  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.LiveMessage.InnerMedalConfig#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<InnerMedalConfig> after_nickname_config;

  @WireField(
      tag = 3,
      adapter = "com.wali.live.proto.LiveMessage.InnerMedalConfig#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<InnerMedalConfig> before_content_config;

  @WireField(
      tag = 4,
      adapter = "com.wali.live.proto.LiveMessage.InnerMedalConfig#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<InnerMedalConfig> after_content_config;

  @WireField(
      tag = 5,
      adapter = "com.wali.live.proto.LiveMessage.InnerMedalConfig#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<InnerMedalConfig> effect_config;

  public MedalConfigMessage(List<InnerMedalConfig> before_nickname_config,
      List<InnerMedalConfig> after_nickname_config, List<InnerMedalConfig> before_content_config,
      List<InnerMedalConfig> after_content_config, List<InnerMedalConfig> effect_config) {
    this(before_nickname_config, after_nickname_config, before_content_config, after_content_config, effect_config, ByteString.EMPTY);
  }

  public MedalConfigMessage(List<InnerMedalConfig> before_nickname_config,
      List<InnerMedalConfig> after_nickname_config, List<InnerMedalConfig> before_content_config,
      List<InnerMedalConfig> after_content_config, List<InnerMedalConfig> effect_config,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.before_nickname_config = Internal.immutableCopyOf("before_nickname_config", before_nickname_config);
    this.after_nickname_config = Internal.immutableCopyOf("after_nickname_config", after_nickname_config);
    this.before_content_config = Internal.immutableCopyOf("before_content_config", before_content_config);
    this.after_content_config = Internal.immutableCopyOf("after_content_config", after_content_config);
    this.effect_config = Internal.immutableCopyOf("effect_config", effect_config);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.before_nickname_config = Internal.copyOf("before_nickname_config", before_nickname_config);
    builder.after_nickname_config = Internal.copyOf("after_nickname_config", after_nickname_config);
    builder.before_content_config = Internal.copyOf("before_content_config", before_content_config);
    builder.after_content_config = Internal.copyOf("after_content_config", after_content_config);
    builder.effect_config = Internal.copyOf("effect_config", effect_config);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MedalConfigMessage)) return false;
    MedalConfigMessage o = (MedalConfigMessage) other;
    return unknownFields().equals(o.unknownFields())
        && before_nickname_config.equals(o.before_nickname_config)
        && after_nickname_config.equals(o.after_nickname_config)
        && before_content_config.equals(o.before_content_config)
        && after_content_config.equals(o.after_content_config)
        && effect_config.equals(o.effect_config);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + before_nickname_config.hashCode();
      result = result * 37 + after_nickname_config.hashCode();
      result = result * 37 + before_content_config.hashCode();
      result = result * 37 + after_content_config.hashCode();
      result = result * 37 + effect_config.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (!before_nickname_config.isEmpty()) builder.append(", before_nickname_config=").append(before_nickname_config);
    if (!after_nickname_config.isEmpty()) builder.append(", after_nickname_config=").append(after_nickname_config);
    if (!before_content_config.isEmpty()) builder.append(", before_content_config=").append(before_content_config);
    if (!after_content_config.isEmpty()) builder.append(", after_content_config=").append(after_content_config);
    if (!effect_config.isEmpty()) builder.append(", effect_config=").append(effect_config);
    return builder.replace(0, 2, "MedalConfigMessage{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MedalConfigMessage.ADAPTER.encode(this);
  }

  public static final MedalConfigMessage parseFrom(byte[] data) throws IOException {
    MedalConfigMessage c = null;
       c = MedalConfigMessage.ADAPTER.decode(data);
    return c;
  }

  public List<InnerMedalConfig> getBeforeNicknameConfigList() {
    if(before_nickname_config==null){
        return new java.util.ArrayList<InnerMedalConfig>();
    }
    return before_nickname_config;
  }

  public List<InnerMedalConfig> getAfterNicknameConfigList() {
    if(after_nickname_config==null){
        return new java.util.ArrayList<InnerMedalConfig>();
    }
    return after_nickname_config;
  }

  public List<InnerMedalConfig> getBeforeContentConfigList() {
    if(before_content_config==null){
        return new java.util.ArrayList<InnerMedalConfig>();
    }
    return before_content_config;
  }

  public List<InnerMedalConfig> getAfterContentConfigList() {
    if(after_content_config==null){
        return new java.util.ArrayList<InnerMedalConfig>();
    }
    return after_content_config;
  }

  public List<InnerMedalConfig> getEffectConfigList() {
    if(effect_config==null){
        return new java.util.ArrayList<InnerMedalConfig>();
    }
    return effect_config;
  }

  public boolean hasBeforeNicknameConfigList() {
    return before_nickname_config!=null;
  }

  public boolean hasAfterNicknameConfigList() {
    return after_nickname_config!=null;
  }

  public boolean hasBeforeContentConfigList() {
    return before_content_config!=null;
  }

  public boolean hasAfterContentConfigList() {
    return after_content_config!=null;
  }

  public boolean hasEffectConfigList() {
    return effect_config!=null;
  }

  public static final class Builder extends Message.Builder<MedalConfigMessage, Builder> {
    public List<InnerMedalConfig> before_nickname_config;

    public List<InnerMedalConfig> after_nickname_config;

    public List<InnerMedalConfig> before_content_config;

    public List<InnerMedalConfig> after_content_config;

    public List<InnerMedalConfig> effect_config;

    public Builder() {
      before_nickname_config = Internal.newMutableList();
      after_nickname_config = Internal.newMutableList();
      before_content_config = Internal.newMutableList();
      after_content_config = Internal.newMutableList();
      effect_config = Internal.newMutableList();
    }

    public Builder addAllBeforeNicknameConfig(List<InnerMedalConfig> before_nickname_config) {
      Internal.checkElementsNotNull(before_nickname_config);
      this.before_nickname_config = before_nickname_config;
      return this;
    }

    public Builder addAllAfterNicknameConfig(List<InnerMedalConfig> after_nickname_config) {
      Internal.checkElementsNotNull(after_nickname_config);
      this.after_nickname_config = after_nickname_config;
      return this;
    }

    public Builder addAllBeforeContentConfig(List<InnerMedalConfig> before_content_config) {
      Internal.checkElementsNotNull(before_content_config);
      this.before_content_config = before_content_config;
      return this;
    }

    public Builder addAllAfterContentConfig(List<InnerMedalConfig> after_content_config) {
      Internal.checkElementsNotNull(after_content_config);
      this.after_content_config = after_content_config;
      return this;
    }

    public Builder addAllEffectConfig(List<InnerMedalConfig> effect_config) {
      Internal.checkElementsNotNull(effect_config);
      this.effect_config = effect_config;
      return this;
    }

    @Override
    public MedalConfigMessage build() {
      return new MedalConfigMessage(before_nickname_config, after_nickname_config, before_content_config, after_content_config, effect_config, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MedalConfigMessage extends ProtoAdapter<MedalConfigMessage> {
    public ProtoAdapter_MedalConfigMessage() {
      super(FieldEncoding.LENGTH_DELIMITED, MedalConfigMessage.class);
    }

    @Override
    public int encodedSize(MedalConfigMessage value) {
      return InnerMedalConfig.ADAPTER.asRepeated().encodedSizeWithTag(1, value.before_nickname_config)
          + InnerMedalConfig.ADAPTER.asRepeated().encodedSizeWithTag(2, value.after_nickname_config)
          + InnerMedalConfig.ADAPTER.asRepeated().encodedSizeWithTag(3, value.before_content_config)
          + InnerMedalConfig.ADAPTER.asRepeated().encodedSizeWithTag(4, value.after_content_config)
          + InnerMedalConfig.ADAPTER.asRepeated().encodedSizeWithTag(5, value.effect_config)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MedalConfigMessage value) throws IOException {
      InnerMedalConfig.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.before_nickname_config);
      InnerMedalConfig.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.after_nickname_config);
      InnerMedalConfig.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.before_content_config);
      InnerMedalConfig.ADAPTER.asRepeated().encodeWithTag(writer, 4, value.after_content_config);
      InnerMedalConfig.ADAPTER.asRepeated().encodeWithTag(writer, 5, value.effect_config);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MedalConfigMessage decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.before_nickname_config.add(InnerMedalConfig.ADAPTER.decode(reader)); break;
          case 2: builder.after_nickname_config.add(InnerMedalConfig.ADAPTER.decode(reader)); break;
          case 3: builder.before_content_config.add(InnerMedalConfig.ADAPTER.decode(reader)); break;
          case 4: builder.after_content_config.add(InnerMedalConfig.ADAPTER.decode(reader)); break;
          case 5: builder.effect_config.add(InnerMedalConfig.ADAPTER.decode(reader)); break;
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
    public MedalConfigMessage redact(MedalConfigMessage value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.before_nickname_config, InnerMedalConfig.ADAPTER);
      Internal.redactElements(builder.after_nickname_config, InnerMedalConfig.ADAPTER);
      Internal.redactElements(builder.before_content_config, InnerMedalConfig.ADAPTER);
      Internal.redactElements(builder.after_content_config, InnerMedalConfig.ADAPTER);
      Internal.redactElements(builder.effect_config, InnerMedalConfig.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
