package com.github.novotnyr.android.fotoriava

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class GalleryItem(val id: Long = -1) {
    fun getUri(): Uri {
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
    }
}

class GalleryViewModel(val app: Application) : AndroidViewModel(app) {
    val galleryItems = MutableLiveData<List<GalleryItem>>()

    //0. [x] pripravit LiveData s polozkami pre zoznam
    //1. zdrojom dat bude "ContentProvider pre media"
    //2. prepojenie viewmodelu a aktivity

    fun reload() {
        viewModelScope.launch {
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor = app.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val items = ArrayList<GalleryItem>()
                while (it.moveToNext()) {
                    val id = it.getLong(0)
                    items.add(GalleryItem(id))
                }
                // pozor! prehadzujeme data z korutiny do hlavneho vlakna
                galleryItems.postValue(items)
            }
        }
    }

}