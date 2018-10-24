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
import com.wali.live.proto.LiveCommon.Viewer;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * 离开房间
 */
public final class LeaveRoomMessage extends Message<LeaveRoomMessage, LeaveRoomMessage.Builder> {
  public static final ProtoAdapter<LeaveRoomMessage> ADAPTER = new ProtoAdapter_LeaveRoomMessage();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VIEWER_COUNT = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer viewer_count;

  /**
   * 观众
   */
  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.LiveCommon.Viewer#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<Viewer> viewers;

  public LeaveRoomMessage(Integer viewer_count, List<Viewer> viewers) {
    this(viewer_count, viewers, ByteString.EMPTY);
  }

  public LeaveRoomMessage(Integer viewer_count, List<Viewer> viewers, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.viewer_count = viewer_count;
    this.viewers = Internal.immutableCopyOf("viewers", viewers);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.viewer_count = viewer_count;
    builder.viewers = Internal.copyOf("viewers", viewers);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof LeaveRoomMessage)) return false;
    LeaveRoomMessage o = (LeaveRoomMessage) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(viewer_count, o.viewer_count)
        && viewers.equals(o.viewers);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (viewer_count != null ? viewer_count.hashCode() : 0);
      result = result * 37 + viewers.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (viewer_count != null) builder.append(", viewer_count=").append(viewer_count);
    if (!viewers.isEmpty()) builder.append(", viewers=").append(viewers);
    return builder.replace(0, 2, "LeaveRoomMessage{").append('}').toString();
  }

  public byte[] toByteArray() {
    return LeaveRoomMessage.ADAPTER.encode(this);
  }

  public static final LeaveRoomMessage parseFrom(byte[] data) throws IOException {
    LeaveRoomMessage c = null;
       c = LeaveRoomMessage.ADAPTER.decode(data);
    return c;
  }

  public Integer getViewerCount() {
    if(viewer_count==null){
        return DEFAULT_VIEWER_COUNT;
    }
    return viewer_count;
  }

  /**
   * 观众
   */
  public List<Viewer> getViewersList() {
    if(viewers==null){
        return new java.util.ArrayList<Viewer>();
    }
    return viewers;
  }

  public boolean hasViewerCount() {
    return viewer_count!=null;
  }

  /**
   * 观众
   */
  public boolean hasViewersList() {
    return viewers!=null;
  }

  public static final class Builder extends Message.Builder<LeaveRoomMessage, Builder> {
    public Integer viewer_count;

    public List<Viewer> viewers;

    public Builder() {
      viewers = Internal.newMutableList();
    }

    public Builder setViewerCount(Integer viewer_count) {
      this.viewer_count = viewer_count;
      return this;
    }

    /**
     * 观众
     */
    public Builder addAllViewers(List<Viewer> viewers) {
      Internal.checkElementsNotNull(viewers);
      this.viewers = viewers;
      return this;
    }

    @Override
    public LeaveRoomMessage build() {
      return new LeaveRoomMessage(viewer_count, viewers, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_LeaveRoomMessage extends ProtoAdapter<LeaveRoomMessage> {
    public ProtoAdapter_LeaveRoomMessage() {
      super(FieldEncoding.LENGTH_DELIMITED, LeaveRoomMessage.class);
    }

    @Override
    public int encodedSize(LeaveRoomMessage value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.viewer_count)
          + Viewer.ADAPTER.asRepeated().encodedSizeWithTag(2, value.viewers)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, LeaveRoomMessage value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.viewer_count);
      Viewer.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.viewers);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public LeaveRoomMessage decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setViewerCount(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.viewers.add(Viewer.ADAPTER.decode(reader)); break;
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
    public LeaveRoomMessage redact(LeaveRoomMessage value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.viewers, Viewer.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
