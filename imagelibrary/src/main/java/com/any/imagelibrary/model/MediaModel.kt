package com.any.imagelibrary.model

import android.os.Parcel
import android.os.Parcelable

/**
 *
 * @author any
 * @time 2019/05/21 11.25
 * @details
 */
class MediaModel constructor() : Parcelable {

    var path: String? = null

    var compressPath: String? = null

    constructor(path: String) : this() {
        this.path = path
    }

    constructor(path: String,compressPath: String) : this() {
        this.path = path
        this.compressPath = compressPath
    }

    constructor(parcel: Parcel) : this() {
        path = parcel.readString()
        compressPath = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(compressPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaModel> {
        override fun createFromParcel(parcel: Parcel): MediaModel {
            return MediaModel(parcel)
        }

        override fun newArray(size: Int): Array<MediaModel?> {
            return arrayOfNulls(size)
        }
    }
}