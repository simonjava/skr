package com.zq.mediaengine.capture.audio;

import com.zq.mediaengine.util.LibraryLoader;

import java.nio.ByteBuffer;

/**
 * Audio recorder with OpenSLES.
 *
 * @hide
 */

public class KSYAudioSLRecord implements IKSYAudioRecord {
    private long mInstance;

    public KSYAudioSLRecord(int sampleRate, int channels, int bufferSamples) {
        mInstance = _init(sampleRate, channels, bufferSamples);
        if (mInstance == 0) {
            throw new IllegalArgumentException("Create OpenGLES record failed!");
        }
    }

    @Override
    public void setVolume(float volume) {
        _setVolume(mInstance, volume);
    }

    @Override
    public int startRecording() {
        return _start(mInstance);
    }

    @Override
    public int stop() {
        return _stop(mInstance);
    }

    @Override
    public void release() {
        _release(mInstance);
    }

    @Override
    public int read(ByteBuffer buffer, int size, long timeout) {
        int ret = _read(mInstance, buffer, size, (int)timeout);
        if (ret > 0) {
            buffer.limit(ret);
            buffer.rewind();
        }
        return ret;
    }

    @Override
    public long getNativeModule() {
        return mInstance;
    }

    @Override
    public void setEnableLatencyTest(boolean enable) {
        _setEnableLatencyTest(mInstance, enable);
    }

    private native long _init(int sampleRate, int channels, int bufferSamples);
    private native void _setVolume(long instance, float volume);
    private native int _start(long instance);
    private native int _stop(long instance);
    private native void _setEnableLatencyTest(long instance, boolean enable);
    private native int _read(long instance, ByteBuffer buffer, int size, int timeout);
    private native void _release(long instance);

    static {
        LibraryLoader.load();
    }
}
