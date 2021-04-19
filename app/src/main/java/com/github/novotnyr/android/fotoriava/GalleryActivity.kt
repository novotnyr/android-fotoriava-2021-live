package com.github.novotnyr.android.fotoriava

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private val viewModel: GalleryViewModel by viewModels()

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it) viewModel.reload()
        //TODO osetrit pripad, ked sme nedostali povolenie
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = GalleryAdapter().also { adapter ->
            viewModel.galleryItems.observe(this) { items ->
                adapter.submitList(items)
            }
        }
        when(ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)) {
            PERMISSION_GRANTED -> viewModel.reload()
            else -> permissionRequest.launch(READ_EXTERNAL_STORAGE)
        }
    }
}