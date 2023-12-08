import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

data class News(
    var id : Int = 0,
    var name : String? = null,
    var description : String? = null,
    var price : Int = 0,
    var image : String? = null
)

interface ApiInterface {

    @GET("api/News")
    public fun GetAllNews() : Call<MutableList<News>>

}

object APIConnect {

    val gson = GsonBuilder()
        .setLenient()
        .create();

    val ConnectionString : String = "https://iis.ngknn.ru/NGKNN/МамшеваЮС/MedicMadlab/";

    public fun GetRetrofit() : ApiInterface
    {
        return Retrofit.Builder()
               .baseUrl(ConnectionString)
               .addConverterFactory(GsonConverterFactory.create(gson))
               .build()
               .create(ApiInterface::class.java)
    }

}