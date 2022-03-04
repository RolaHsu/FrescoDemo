package com.rolahsu.frescodemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Animatable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io

class MainActivity2 : AppCompatActivity() {
    lateinit var simpleImage: SimpleDraweeView
    val resourceUri = Uri.parse("https://dev.streetvoice.com/asset_stage/images/default/img-profile-head-default.png?x-oss-process=image/resize,m_fill,h_300,w_300,limit_0/interlace,1/quality,q_95/sharpen,80/format,jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cover)
        simpleImage = findViewById(R.id.simpleImage)
        Log.d("xxx", "main thread")

        snap(this)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("xxx", "success")
            }, {
                Log.d("xxx", "fail")
            })


    }

    private fun snap(context: Context) = Single.create<String> {
        val outputBitmap = Bitmap.createBitmap(context.resources.getDimensionPixelOffset(R.dimen.clap_card_4x_width), context.resources.getDimensionPixelOffset(R.dimen.clap_card_4x_height), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)

        val view = LayoutInflater.from(context).inflate(R.layout.cover, null)
        val simpleImage2 = view.findViewById<SimpleDraweeView>(R.id.simpleImage)

        val downloadListener = object : BaseControllerListener<ImageInfo>() {
            override fun onSubmit(id: String?, callerContext: Any?) {
                super.onSubmit(id, callerContext)
                Log.d("xxx", "onSubmit")
            }

            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                super.onFinalImageSet(id, imageInfo, animatable)
                Log.d("xxx", "onFinalImageSet")
                it.onSuccess("123")
            }

            override fun onFailure(id: String?, throwable: Throwable?) {
                super.onFailure(id, throwable)
                Log.e("xxx", "$throwable")
            }
        }

        Log.d("xxx", "set uri")
        val controller = Fresco.newDraweeControllerBuilder()
            .setUri(resourceUri)
            .setControllerListener(downloadListener)
            .build()
        simpleImage.controller = controller
    }
}