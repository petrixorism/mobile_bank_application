package uz.gita.myapplication.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class ColorRequest(

	@SerializedName("color")
	val color: Int,

	@SerializedName("userCardId")
	val userCardId: Int,
)
