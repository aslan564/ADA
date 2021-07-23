package aslan.aslanov.videocapture.model.registerModel


import com.squareup.moshi.Json

data class Child(
    @Json(name = "doctorName") val doctorName: String,
    @Json(name = "estimatedBirthDate") val estimatedBirthDate: String,
    @Json(name = "grams") val grams: Int,
    @Json(name = "name") val name: String,
    @Json(name = "realBirthDate") val realBirthDate: String,
    @Json(name = "sexuality") val sexuality: String
)