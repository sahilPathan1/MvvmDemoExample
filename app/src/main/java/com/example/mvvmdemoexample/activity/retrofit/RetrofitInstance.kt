package retrofit
import com.example.mvvmdemoexample.activity.retrofit.Service
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://13.233.39.120/trainee/api/"

    //lazy unction that takes a lambda and returns an instance of Lazy<T> , which can serve as a delegate for implementing a lazy property.
    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
    val apiInterface: Service by lazy {

        retrofit.create(Service::class.java)
    }
}
