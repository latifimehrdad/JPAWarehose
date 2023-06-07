package com.zarholding.jpacustomer.view.adapter.holder

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.downloadImage
import com.hoomanholding.applibrary.model.data.response.video.VideoModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemVideoBinding

/**
 * Created by m-latifi on 5/27/2023.
 */

class VideoHolder(
    private val binding: ItemVideoBinding,
    private val click: Click
): RecyclerView.ViewHolder(binding.root) {

    interface Click{
        fun click(position: Int)
    }


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: VideoModel, position: Int) {
        setValueToXml(item)
        setListener(position)
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: VideoModel) {
        binding.textViewTitle.text = item.videoTitle
        binding.textViewDescription.text = item.videoDescription
        binding.imageViewThumbnail.downloadImage(
            url = item.thumbnailNameAddress,
            placeholder = AppCompatResources
                .getDrawable(binding.imageViewThumbnail.context, R.drawable.ic_logo)
        )
    }
    //---------------------------------------------------------------------------------------------- setValueToXml



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(position: Int) {
        binding.root.setOnClickListener {
            click.click(position)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener

}