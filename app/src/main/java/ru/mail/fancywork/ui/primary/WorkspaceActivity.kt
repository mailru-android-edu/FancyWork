package ru.mail.fancywork.ui.primary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import ru.mail.fancywork.R
import ru.mail.fancywork.controller.Controller
import ru.mail.fancywork.model.datatype.Fancywork
import ru.mail.fancywork.model.repo.AuthRepository
import ru.mail.fancywork.model.repo.CloudStorageRepository
import ru.mail.fancywork.model.repo.FirestoreRepository
import ru.mail.fancywork.model.repo.PixelizationRepository
import ru.mail.fancywork.ui.secondary.ColorGridView

class WorkspaceActivity : AppCompatActivity() {
    private val controller = Controller()

    private lateinit var originalBitmap: Bitmap
    private lateinit var pixelatedBitmap: Bitmap
    private lateinit var colorGridView: ColorGridView
    private lateinit var scaleSlider: Slider
    private lateinit var colorSlider: Slider
    private var scale = 25
    private var colors = 5
    private var isDirty = true

    fun save(view: View) {
        pixelate()
        // todo prompt for name
        controller.addEmbroidery(pixelatedBitmap, colors)
        finish()
    }

    fun process(view: View) {
        pixelate()
    }

    private fun pixelate() {
        if(!isDirty)return
        isDirty = false

        val ratio = originalBitmap.width / originalBitmap.height.toFloat()
        val isVertical = ratio > 1.0
        val width = if (isVertical) (scale * ratio).toInt() else scale
        val height = if (isVertical) scale else (scale / ratio).toInt()

        pixelatedBitmap = controller.pixelate(originalBitmap, width, height, colors)
        colorGridView.setImage(pixelatedBitmap, scale)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pixelspace)

        val uri = intent.getParcelableExtra<Uri>(MainActivity.BITMAP_MESSAGE)!!
        val inputStream = this.applicationContext.contentResolver.openInputStream(uri)
        originalBitmap = BitmapFactory.decodeStream(inputStream)

        colorGridView = findViewById(R.id.color_grid_view)
        scaleSlider = findViewById(R.id.scaleSlider)
        colorSlider = findViewById(R.id.colorSlider)

        scaleSlider.value = scale.toFloat()
        colorSlider.value = colors.toFloat()

        scaleSlider.addOnChangeListener { _, value, _ ->
            scale = value.toInt()
            isDirty = true
        }
        colorSlider.addOnChangeListener { _, value, _ ->
            colors = value.toInt()
            isDirty = true
        }

        pixelate()
    }
}
