package app.com.retrofitwithrecyclerviewkotlin

import com.vijaysrikanth.fd.api.Response.GetContentList
import com.vijaysrikanth.fd.api.Response.GetLayoutList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("layout/getLayoutList")
    fun getLayoutList() : Call<List<GetLayoutList>>


    @GET("library/getContentDetailsById/cnt00001")
    fun getContentList() : Call<List<GetContentList>>

    @GET("library/getContentDetailsById/{content_id}")
    fun getContentList1(@Path("content_id") input: String?): Call<List<GetContentList>>?


    companion object {

        var BASE_URL = "http://3.17.129.226:8080/api/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}