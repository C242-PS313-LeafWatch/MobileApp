package com.example.capstone.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LoginResponse(

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("msg")
	var msg: String = "Success",

	@field:SerializedName("accessToken")
	val accessToken: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)

data class ProfileResponse(

	@field:SerializedName("msg")
	val msg: String = "Success",

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class User(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("birth_date")
	val birthDate: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)
