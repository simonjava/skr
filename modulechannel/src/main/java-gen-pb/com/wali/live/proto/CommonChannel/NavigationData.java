// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CommonChannel.proto
package com.wali.live.proto.CommonChannel;

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
 * type=20, 27, 30（圈子新增）
 */
public final class NavigationData extends Message<NavigationData, NavigationData.Builder> {
  public static final ProtoAdapter<NavigationData> ADAPTER = new ProtoAdapter_NavigationData();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_NAME = "";

  public static final String DEFAULT_BGIMGURL = "";

  public static final String DEFAULT_ICONURL = "";

  public static final String DEFAULT_JUMPSCHEMEURI = "";

  public static final Integer DEFAULT_TEXTCOLOR = 0;

  public static final String DEFAULT_HEXCOLORCODE = "";

  public static final String DEFAULT_TEXT1 = "";

  public static final String DEFAULT_APKNAME = "";

  /**
   * 导航标签名 话题
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String name;

  /**
   * 背景图
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String bgImgUrl;

  /**
   * 小图标
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String iconUrl;

  /**
   * 跳转scheme 话题跳转uri(考虑需求经常变，这里可配置其他类型uri，如电视台，h5等)
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String jumpSchemeUri;

  /**
   * 文本颜色[话题用到],0=客户端默认颜色,1=金色
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer textColor;

  /**
   * 十六进制颜色码，如#FFFFFF
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String hexColorCode;

  /**
   * 导航新增text1
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String text1;

  /**
   * apk包名
   */
  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String apkName;

  public NavigationData(String name, String bgImgUrl, String iconUrl, String jumpSchemeUri,
      Integer textColor, String hexColorCode, String text1, String apkName) {
    this(name, bgImgUrl, iconUrl, jumpSchemeUri, textColor, hexColorCode, text1, apkName, ByteString.EMPTY);
  }

  public NavigationData(String name, String bgImgUrl, String iconUrl, String jumpSchemeUri,
      Integer textColor, String hexColorCode, String text1, String apkName,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.name = name;
    this.bgImgUrl = bgImgUrl;
    this.iconUrl = iconUrl;
    this.jumpSchemeUri = jumpSchemeUri;
    this.textColor = textColor;
    this.hexColorCode = hexColorCode;
    this.text1 = text1;
    this.apkName = apkName;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.name = name;
    builder.bgImgUrl = bgImgUrl;
    builder.iconUrl = iconUrl;
    builder.jumpSchemeUri = jumpSchemeUri;
    builder.textColor = textColor;
    builder.hexColorCode = hexColorCode;
    builder.text1 = text1;
    builder.apkName = apkName;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof NavigationData)) return false;
    NavigationData o = (NavigationData) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(name, o.name)
        && Internal.equals(bgImgUrl, o.bgImgUrl)
        && Internal.equals(iconUrl, o.iconUrl)
        && Internal.equals(jumpSchemeUri, o.jumpSchemeUri)
        && Internal.equals(textColor, o.textColor)
        && Internal.equals(hexColorCode, o.hexColorCode)
        && Internal.equals(text1, o.text1)
        && Internal.equals(apkName, o.apkName);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (bgImgUrl != null ? bgImgUrl.hashCode() : 0);
      result = result * 37 + (iconUrl != null ? iconUrl.hashCode() : 0);
      result = result * 37 + (jumpSchemeUri != null ? jumpSchemeUri.hashCode() : 0);
      result = result * 37 + (textColor != null ? textColor.hashCode() : 0);
      result = result * 37 + (hexColorCode != null ? hexColorCode.hashCode() : 0);
      result = result * 37 + (text1 != null ? text1.hashCode() : 0);
      result = result * 37 + (apkName != null ? apkName.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (name != null) builder.append(", name=").append(name);
    if (bgImgUrl != null) builder.append(", bgImgUrl=").append(bgImgUrl);
    if (iconUrl != null) builder.append(", iconUrl=").append(iconUrl);
    if (jumpSchemeUri != null) builder.append(", jumpSchemeUri=").append(jumpSchemeUri);
    if (textColor != null) builder.append(", textColor=").append(textColor);
    if (hexColorCode != null) builder.append(", hexColorCode=").append(hexColorCode);
    if (text1 != null) builder.append(", text1=").append(text1);
    if (apkName != null) builder.append(", apkName=").append(apkName);
    return builder.replace(0, 2, "NavigationData{").append('}').toString();
  }

  public byte[] toByteArray() {
    return NavigationData.ADAPTER.encode(this);
  }

  public static final NavigationData parseFrom(byte[] data) throws IOException {
    NavigationData c = null;
       c = NavigationData.ADAPTER.decode(data);
    return c;
  }

  /**
   * 导航标签名 话题
   */
  public String getName() {
    if(name==null){
        return DEFAULT_NAME;
    }
    return name;
  }

  /**
   * 背景图
   */
  public String getBgImgUrl() {
    if(bgImgUrl==null){
        return DEFAULT_BGIMGURL;
    }
    return bgImgUrl;
  }

  /**
   * 小图标
   */
  public String getIconUrl() {
    if(iconUrl==null){
        return DEFAULT_ICONURL;
    }
    return iconUrl;
  }

  /**
   * 跳转scheme 话题跳转uri(考虑需求经常变，这里可配置其他类型uri，如电视台，h5等)
   */
  public String getJumpSchemeUri() {
    if(jumpSchemeUri==null){
        return DEFAULT_JUMPSCHEMEURI;
    }
    return jumpSchemeUri;
  }

  /**
   * 文本颜色[话题用到],0=客户端默认颜色,1=金色
   */
  public Integer getTextColor() {
    if(textColor==null){
        return DEFAULT_TEXTCOLOR;
    }
    return textColor;
  }

  /**
   * 十六进制颜色码，如#FFFFFF
   */
  public String getHexColorCode() {
    if(hexColorCode==null){
        return DEFAULT_HEXCOLORCODE;
    }
    return hexColorCode;
  }

  /**
   * 导航新增text1
   */
  public String getText1() {
    if(text1==null){
        return DEFAULT_TEXT1;
    }
    return text1;
  }

  /**
   * apk包名
   */
  public String getApkName() {
    if(apkName==null){
        return DEFAULT_APKNAME;
    }
    return apkName;
  }

  /**
   * 导航标签名 话题
   */
  public boolean hasName() {
    return name!=null;
  }

  /**
   * 背景图
   */
  public boolean hasBgImgUrl() {
    return bgImgUrl!=null;
  }

  /**
   * 小图标
   */
  public boolean hasIconUrl() {
    return iconUrl!=null;
  }

  /**
   * 跳转scheme 话题跳转uri(考虑需求经常变，这里可配置其他类型uri，如电视台，h5等)
   */
  public boolean hasJumpSchemeUri() {
    return jumpSchemeUri!=null;
  }

  /**
   * 文本颜色[话题用到],0=客户端默认颜色,1=金色
   */
  public boolean hasTextColor() {
    return textColor!=null;
  }

  /**
   * 十六进制颜色码，如#FFFFFF
   */
  public boolean hasHexColorCode() {
    return hexColorCode!=null;
  }

  /**
   * 导航新增text1
   */
  public boolean hasText1() {
    return text1!=null;
  }

  /**
   * apk包名
   */
  public boolean hasApkName() {
    return apkName!=null;
  }

  public static final class Builder extends Message.Builder<NavigationData, Builder> {
    public String name;

    public String bgImgUrl;

    public String iconUrl;

    public String jumpSchemeUri;

    public Integer textColor;

    public String hexColorCode;

    public String text1;

    public String apkName;

    public Builder() {
    }

    /**
     * 导航标签名 话题
     */
    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * 背景图
     */
    public Builder setBgImgUrl(String bgImgUrl) {
      this.bgImgUrl = bgImgUrl;
      return this;
    }

    /**
     * 小图标
     */
    public Builder setIconUrl(String iconUrl) {
      this.iconUrl = iconUrl;
      return this;
    }

    /**
     * 跳转scheme 话题跳转uri(考虑需求经常变，这里可配置其他类型uri，如电视台，h5等)
     */
    public Builder setJumpSchemeUri(String jumpSchemeUri) {
      this.jumpSchemeUri = jumpSchemeUri;
      return this;
    }

    /**
     * 文本颜色[话题用到],0=客户端默认颜色,1=金色
     */
    public Builder setTextColor(Integer textColor) {
      this.textColor = textColor;
      return this;
    }

    /**
     * 十六进制颜色码，如#FFFFFF
     */
    public Builder setHexColorCode(String hexColorCode) {
      this.hexColorCode = hexColorCode;
      return this;
    }

    /**
     * 导航新增text1
     */
    public Builder setText1(String text1) {
      this.text1 = text1;
      return this;
    }

    /**
     * apk包名
     */
    public Builder setApkName(String apkName) {
      this.apkName = apkName;
      return this;
    }

    @Override
    public NavigationData build() {
      return new NavigationData(name, bgImgUrl, iconUrl, jumpSchemeUri, textColor, hexColorCode, text1, apkName, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_NavigationData extends ProtoAdapter<NavigationData> {
    public ProtoAdapter_NavigationData() {
      super(FieldEncoding.LENGTH_DELIMITED, NavigationData.class);
    }

    @Override
    public int encodedSize(NavigationData value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.name)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.bgImgUrl)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.iconUrl)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.jumpSchemeUri)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.textColor)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.hexColorCode)
          + ProtoAdapter.STRING.encodedSizeWithTag(7, value.text1)
          + ProtoAdapter.STRING.encodedSizeWithTag(8, value.apkName)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, NavigationData value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.name);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.bgImgUrl);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.iconUrl);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.jumpSchemeUri);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.textColor);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.hexColorCode);
      ProtoAdapter.STRING.encodeWithTag(writer, 7, value.text1);
      ProtoAdapter.STRING.encodeWithTag(writer, 8, value.apkName);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public NavigationData decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setName(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setBgImgUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setIconUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setJumpSchemeUri(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.setTextColor(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.setHexColorCode(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setText1(ProtoAdapter.STRING.decode(reader)); break;
          case 8: builder.setApkName(ProtoAdapter.STRING.decode(reader)); break;
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
    public NavigationData redact(NavigationData value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
