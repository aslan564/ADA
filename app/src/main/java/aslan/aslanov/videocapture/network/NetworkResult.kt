package aslan.aslanov.videocapture.network

data class NetworkResult<R>(val status: Status, val data: R?, val message: String?) {
    companion object {
        fun <R> success(data: R?): NetworkResult<R> {
            return NetworkResult(Status.SUCCESS, data, null)
        }

        fun <R> error(errorMessage: String?): NetworkResult<R> {
            return NetworkResult(Status.ERROR, null, errorMessage)
        }

        fun <R> loading(): NetworkResult<R> {
            return NetworkResult(Status.LOADING, null, null)
        }
    }
}

enum class Status {
    SUCCESS, ERROR, LOADING
}

///{"timestamp":"2021-07-09T16:29:52.323+03:00","status":400,"error":"Bad Request","message":"Beklenen doğum tarihi değerlendirme haftaları arasında değildir. Beklenen doğum tarihi üzerinden 14 hafta geçti ise rapor doğru sonuç veremeyecektir.","path":"/user"}
