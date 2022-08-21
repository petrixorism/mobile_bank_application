package uz.gita.myapplication.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class IgnoreBalanceRequest(

	@SerializedName("userCardId")
	val userCardId: Int,

	@SerializedName("ignoreBalance")
	val ignoreBalance: Boolean,
)
