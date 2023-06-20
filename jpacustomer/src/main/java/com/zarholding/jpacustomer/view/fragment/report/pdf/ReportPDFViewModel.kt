package com.zarholding.jpacustomer.view.fragment.report.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.PDFModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by m-latifi on 6/18/2023.
 */

@HiltViewModel
class ReportPDFViewModel @Inject constructor(

): JpaViewModel() {

    val a4Height = 1754
    val a4With = 1240
    val mutableLiveData = MutableLiveData<Boolean>()
    var destinationFile: File? = null

    //______________________________________________________________________________________________ createPdf
    fun createPdf(v: View) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val convertWidth: Int
            var differentPercent = if (a4With > v.width) {
                convertWidth = v.width
                (v.width * 100) / a4With
            } else {
                convertWidth = a4With
                (a4With * 100) / v.width
            }

            if (differentPercent >= 100 )
                differentPercent -= 100
            else
                differentPercent = 100 - differentPercent

            val convertHeight = if (a4With > v.width) {
                v.height + ((v.height * differentPercent) / 100)
            } else {
                v.height - ((v.height * differentPercent) / 100)
            }

            val origin = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(origin)
            c.drawColor(Color.WHITE)
            v.draw(c)

            val b = Bitmap.createScaledBitmap(origin, convertWidth, convertHeight, true)

            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(a4With, a4Height, 1).create()
            val bitmaps = splitBitmap(b, convertHeight)
            for (bitmap in bitmaps) {
                val page = document.startPage(pageInfo)
                val bit = Bitmap.createScaledBitmap(bitmap.bitmap, a4With, bitmap.height, true)
                page.canvas.drawBitmap(bit, 0f, 0f, null)
                document.finishPage(page)
            }

            val downloadFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val destinationDir = File(downloadFolder.absolutePath, "Jpa")
            if (!destinationDir.exists())
                destinationDir.mkdir()
            destinationFile = File(
                destinationDir.absolutePath,
                "${getNewFileName()}.pdf"
            )
            try {
                withContext(IO) {
                    document.writeTo(FileOutputStream(destinationFile))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                mutableLiveData.postValue(false)
            }
            document.close()
            mutableLiveData.postValue(true)
        }
    }
    //______________________________________________________________________________________________ createPdf



    //______________________________________________________________________________________________ splitBitmap
    private fun splitBitmap(src: Bitmap, height: Int): MutableList<PDFModel> {
        var pageNumber = height / a4Height
        val rem = height % a4Height
        if (rem > 0)
            pageNumber++
        val bitmaps = mutableListOf<PDFModel>()
        for (i in 0 until pageNumber) {
            if (i + 1 < pageNumber){
                val bit = Bitmap.createBitmap(src, 0, i * a4Height, src.width, a4Height)
                bitmaps.add(PDFModel(a4Height, src.width, bit))
            } else {
                val bit = Bitmap.createBitmap(src, 0, i * a4Height, src.width, rem)
                bitmaps.add(PDFModel(rem, src.width, bit))
            }
        }
        return bitmaps
    }
    //______________________________________________________________________________________________ splitBitmap




    //______________________________________________________________________________________________ getNewFileName
    private fun getNewFileName(): String? {
        return SimpleDateFormat("yyyy_MM_dd__HH_mm_ss", Locale.getDefault()).format(Date())
    }
    //______________________________________________________________________________________________ getNewFileName

}