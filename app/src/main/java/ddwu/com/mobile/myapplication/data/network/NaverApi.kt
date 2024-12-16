package ddwu.com.mobile.myapplication.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.Response

interface NaverApi {
    @Headers(
        "X-Naver-Client-Id: dEFMa7myuB3LSkAVdjl7",
        "X-Naver-Client-Secret: _KokTILI0u"
    )
    @GET("v1/search/local.json")
    suspend fun searchLocal(
        @Query("query") query: String,
        @Query("display") display: Int = 20,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "random"
    ): Response<NaverSearchResponse>
}
