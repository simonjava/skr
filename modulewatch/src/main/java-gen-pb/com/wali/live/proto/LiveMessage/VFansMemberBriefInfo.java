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
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 600 宠爱团简要信息
 */
public final class VFansMemberBriefInfo extends Message<VFansMemberBriefInfo, VFansMemberBriefInfo.Builder> {
  public static final ProtoAdapter<VFansMemberBriefInfo> ADAPTER = new ProtoAdapter_VFansMemberBriefInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_PET_LEVEL = 0;

  public static final String DEFAULT_MEDAL_VALUE = "";

  public static final Boolean DEFAULT_IS_USE_MEDAL = false;

  public static final Boolean DEFAULT_IS_VIP_EXPIRE = false;

  public static final String DEFAULT_BARRAGE_COLOR = "";

  /**
   * 宠爱等级
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer pet_level;

  /**
   * 宠爱等级对应的勋章
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String medal_value;

  /**
   * 是否佩戴勋章
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_use_medal;

  /**
   * 宠爱团会员过期时间 默认0,绝对时间戳
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_vip_expire;

  /**
   * 彩色弹幕
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String barrage_color;

  public VFansMemberBriefInfo(Integer pet_level, String medal_value, Boolean is_use_medal,
      Boolean is_vip_expire, String barrage_color) {
    this(pet_level, medal_value, is_use_medal, is_vip_expire, barrage_color, ByteString.EMPTY);
  }

  public VFansMemberBriefInfo(Integer pet_level, String medal_value, Boolean is_use_medal,
      Boolean is_vip_expire, String barrage_color, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.pet_level = pet_level;
    this.medal_value = medal_value;
    this.is_use_medal = is_use_medal;
    this.is_vip_expire = is_vip_expire;
    this.barrage_color = barrage_color;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.pet_level = pet_level;
    builder.medal_value = medal_value;
    builder.is_use_medal = is_use_medal;
    builder.is_vip_expire = is_vip_expire;
    builder.barrage_color = barrage_color;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof VFansMemberBriefInfo)) return false;
    VFansMemberBriefInfo o = (VFansMemberBriefInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(pet_level, o.pet_level)
        && Internal.equals(medal_value, o.medal_value)
        && Internal.equals(is_use_medal, o.is_use_medal)
        && Internal.equals(is_vip_expire, o.is_vip_expire)
        && Internal.equals(barrage_color, o.barrage_color);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (pet_level != null ? pet_level.hashCode() : 0);
      result = result * 37 + (medal_value != null ? medal_value.hashCode() : 0);
      result = result * 37 + (is_use_medal != null ? is_use_medal.hashCode() : 0);
      result = result * 37 + (is_vip_expire != null ? is_vip_expire.hashCode() : 0);
      result = result * 37 + (barrage_color != null ? barrage_color.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (pet_level != null) builder.append(", pet_level=").append(pet_level);
    if (medal_value != null) builder.append(", medal_value=").append(medal_value);
    if (is_use_medal != null) builder.append(", is_use_medal=").append(is_use_medal);
    if (is_vip_expire != null) builder.append(", is_vip_expire=").append(is_vip_expire);
    if (barrage_color != null) builder.append(", barrage_color=").append(barrage_color);
    return builder.replace(0, 2, "VFansMemberBriefInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return VFansMemberBriefInfo.ADAPTER.encode(this);
  }

  public static final VFansMemberBriefInfo parseFrom(byte[] data) throws IOException {
    VFansMemberBriefInfo c = null;
       c = VFansMemberBriefInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 宠爱等级
   */
  public Integer getPetLevel() {
    if(pet_level==null){
        return DEFAULT_PET_LEVEL;
    }
    return pet_level;
  }

  /**
   * 宠爱等级对应的勋章
   */
  public String getMedalValue() {
    if(medal_value==null){
        return DEFAULT_MEDAL_VALUE;
    }
    return medal_value;
  }

  /**
   * 是否佩戴勋章
   */
  public Boolean getIsUseMedal() {
    if(is_use_medal==null){
        return DEFAULT_IS_USE_MEDAL;
    }
    return is_use_medal;
  }

  /**
   * 宠爱团会员过期时间 默认0,绝对时间戳
   */
  public Boolean getIsVipExpire() {
    if(is_vip_expire==null){
        return DEFAULT_IS_VIP_EXPIRE;
    }
    return is_vip_expire;
  }

  /**
   * 彩色弹幕
   */
  public String getBarrageColor() {
    if(barrage_color==null){
        return DEFAULT_BARRAGE_COLOR;
    }
    return barrage_color;
  }

  /**
   * 宠爱等级
   */
  public boolean hasPetLevel() {
    return pet_level!=null;
  }

  /**
   * 宠爱等级对应的勋章
   */
  public boolean hasMedalValue() {
    return medal_value!=null;
  }

  /**
   * 是否佩戴勋章
   */
  public boolean hasIsUseMedal() {
    return is_use_medal!=null;
  }

  /**
   * 宠爱团会员过期时间 默认0,绝对时间戳
   */
  public boolean hasIsVipExpire() {
    return is_vip_expire!=null;
  }

  /**
   * 彩色弹幕
   */
  public boolean hasBarrageColor() {
    return barrage_color!=null;
  }

  public static final class Builder extends Message.Builder<VFansMemberBriefInfo, Builder> {
    public Integer pet_level;

    public String medal_value;

    public Boolean is_use_medal;

    public Boolean is_vip_expire;

    public String barrage_color;

    public Builder() {
    }

    /**
     * 宠爱等级
     */
    public Builder setPetLevel(Integer pet_level) {
      this.pet_level = pet_level;
      return this;
    }

    /**
     * 宠爱等级对应的勋章
     */
    public Builder setMedalValue(String medal_value) {
      this.medal_value = medal_value;
      return this;
    }

    /**
     * 是否佩戴勋章
     */
    public Builder setIsUseMedal(Boolean is_use_medal) {
      this.is_use_medal = is_use_medal;
      return this;
    }

    /**
     * 宠爱团会员过期时间 默认0,绝对时间戳
     */
    public Builder setIsVipExpire(Boolean is_vip_expire) {
      this.is_vip_expire = is_vip_expire;
      return this;
    }

    /**
     * 彩色弹幕
     */
    public Builder setBarrageColor(String barrage_color) {
      this.barrage_color = barrage_color;
      return this;
    }

    @Override
    public VFansMemberBriefInfo build() {
      return new VFansMemberBriefInfo(pet_level, medal_value, is_use_medal, is_vip_expire, barrage_color, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_VFansMemberBriefInfo extends ProtoAdapter<VFansMemberBriefInfo> {
    public ProtoAdapter_VFansMemberBriefInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, VFansMemberBriefInfo.class);
    }

    @Override
    public int encodedSize(VFansMemberBriefInfo value) {
      return ProtoAdapter.INT32.encodedSizeWithTag(1, value.pet_level)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.medal_value)
          + ProtoAdapter.BOOL.encodedSizeWithTag(3, value.is_use_medal)
          + ProtoAdapter.BOOL.encodedSizeWithTag(4, value.is_vip_expire)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.barrage_color)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, VFansMemberBriefInfo value) throws IOException {
      ProtoAdapter.INT32.encodeWithTag(writer, 1, value.pet_level);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.medal_value);
      ProtoAdapter.BOOL.encodeWithTag(writer, 3, value.is_use_medal);
      ProtoAdapter.BOOL.encodeWithTag(writer, 4, value.is_vip_expire);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.barrage_color);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public VFansMemberBriefInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setPetLevel(ProtoAdapter.INT32.decode(reader)); break;
          case 2: builder.setMedalValue(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setIsUseMedal(ProtoAdapter.BOOL.decode(reader)); break;
          case 4: builder.setIsVipExpire(ProtoAdapter.BOOL.decode(reader)); break;
          case 5: builder.setBarrageColor(ProtoAdapter.STRING.decode(reader)); break;
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
    public VFansMemberBriefInfo redact(VFansMemberBriefInfo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
