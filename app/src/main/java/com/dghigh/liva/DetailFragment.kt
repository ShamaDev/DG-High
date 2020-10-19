package com.dghigh.liva

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_detail.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener {
            findNavController().popBackStack()
        }

        detailPic.setImageResource(Repository.pic)


        save.setOnClickListener {
            Toast.makeText(context, "Сохранено", Toast.LENGTH_LONG).show()
            savePicture(convertViewToDrawable(detailPic))
        }
    }

    private fun convertViewToDrawable(view: View): Bitmap {
        val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(spec, spec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val b = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        c.translate((-view.scrollX).toFloat(), (-view.scrollY).toFloat())
        view.draw(c)
        return b
    }

    private fun savePicture(bmp: Bitmap) {
        var fOut: OutputStream? = null
        try {
            val dest = File(getGalleryPath() + "TikTakToe")
            dest.mkdirs()
            val file = File(
                getGalleryPath() + "TikTakToe",
                System.currentTimeMillis().toString() + ".jpg"
            ) // создать уникальное имя для файла основываясь на дате сохранения
            fOut = FileOutputStream(file)
            bmp.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                fOut
            ) // сохранять картинку в jpeg-формате с 85% сжатия.
            fOut.flush()
            fOut.close()
            MediaStore.Images.Media.insertImage(
                activity?.contentResolver,
                file.absolutePath,
                file.name,
                file.name
            )
        } catch (e: java.lang.Exception)
        {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun getGalleryPath(): String {
        return Environment.getExternalStorageDirectory().toString() + "/"
    }


}