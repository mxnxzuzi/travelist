package ddwu.com.mobile.myapplication.data.network

data class NaverSearchResponse(
    val items: List<Place>
)

data class Place(
    val title: String,
    val link: String,
    val category: String,
    val description: String,
    val telephone: String,
    val address: String,
    val roadAddress: String,
    val mapx: Int,
    val mapy: Int
)
