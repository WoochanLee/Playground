import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

const val NETWORK_TIME_OUT = 1000L

fun makeOkHttpClient(
    isLogging: Boolean = true
): OkHttpClient {
    return OkHttpClient.Builder()

        //setup timeout
        .connectTimeout(NETWORK_TIME_OUT, java.util.concurrent.TimeUnit.MILLISECONDS)
        .readTimeout(NETWORK_TIME_OUT, java.util.concurrent.TimeUnit.MILLISECONDS)
        .writeTimeout(NETWORK_TIME_OUT, java.util.concurrent.TimeUnit.MILLISECONDS)

        //setup cache
        .cache(
            Cache(
                // directory = File(application.cacheDir, "http_cache") // for android
                directory = File("/test_cache", "http_cache"),
                maxSize = 50L * 1024L * 1024L // 50 MB
            )
        )

        //setup logging interceptor
        .run {
            if (isLogging) {
                addInterceptor(run {
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                })
            } else {
                this
            }
        }

        .build()
}

fun OkHttpClient.clearCache() {
    cache?.delete()
}

fun OkHttpClient.removeCache(hostUrl: String) {
    val urlIterator = cache?.urls()
    while (urlIterator?.hasNext() == true) {
        if (urlIterator.next().startsWith(hostUrl)) {
            urlIterator.remove()
        }
    }
}