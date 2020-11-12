package com.example.mvvmappapplication.di;

import com.example.mvvmappapplication.data.CameraService;
import com.example.mvvmappapplication.data.CommentService;
import com.example.mvvmappapplication.data.PostService;
import com.example.mvvmappapplication.data.UserServerService;
import com.example.mvvmappapplication.data.UserService;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {
    /**
     * - GsonConverter : 대부분의 서버에서는 응답으로 json을 주로 이용합니다.
     *   retrofit은 이러한 응답을 간단하게 변환할 수 있도록, 다양한 Converter을 제공하고 있습니다.
     */
    @Provides
    @Singleton
    @Named("json")
    Retrofit provideJsonRetrofit() {
        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder().addInterceptor(
                        new HttpLoggingInterceptor().setLevel(
                                HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
    @Provides
    @Singleton
    @Named("server")
    Retrofit provideServerRetrofit() {
        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder().addInterceptor(
                        new HttpLoggingInterceptor().setLevel(
                                HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl("http://ec2-13-209-5-246.ap-northeast-2.compute.amazonaws.com:8072/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
    @Provides
    @Singleton
    @Named("image")
    Retrofit provideImageRetrofit() {
        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .readTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5,TimeUnit.SECONDS)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(
                                HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl("http://ec2-3-35-166-249.ap-northeast-2.compute.amazonaws.com:8072/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
    @Provides
    @Reusable
    PostService providePostService(@Named("json") Retrofit retrofit) {
        return retrofit.create(PostService.class);
    }

    @Provides
    @Reusable
    UserService provideUserService(@Named("json") Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }
    @Provides
    @Reusable
    UserServerService provideUserServerService(@Named("server") Retrofit retrofit) {
        return retrofit.create(UserServerService.class);
    }
    @Provides
    @Reusable
    CommentService provideCommentService(@Named("json") Retrofit retrofit) {
        return retrofit.create(CommentService.class);
    }

    @Provides
    @Reusable
    CameraService provideCameraService(@Named("image") Retrofit retrofit) {
        return retrofit.create(CameraService.class);
    }


    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

