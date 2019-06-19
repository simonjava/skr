package com.module.playways.grab.room.songmanager.event;

import com.module.playways.grab.room.songmanager.model.GrabWishSongModel;

public class AddSuggestSongEvent {
    GrabWishSongModel mGrabWishSongModel;

    public AddSuggestSongEvent(GrabWishSongModel model) {
        mGrabWishSongModel = model;
    }

    public GrabWishSongModel getGrabWishSongModel() {
        return mGrabWishSongModel;
    }

    public void setGrabWishSongModel(GrabWishSongModel grabWishSongModel) {
        mGrabWishSongModel = grabWishSongModel;
    }
}
