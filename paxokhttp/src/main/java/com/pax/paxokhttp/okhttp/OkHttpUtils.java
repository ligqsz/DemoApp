package com.pax.paxokhttp.okhttp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author ligq
 * @date 2018/9/21
 */

@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused", "ConstantConditions"})
public class OkHttpUtils {
    private OkHttpUtils() {
        throw new IllegalArgumentException();
    }

    static final String TAG = "OkHttpUtils";
    private static final String CER_NAME = "";
    private static OkHttpClient okHttpClient;
    public static final int DEFAULT_TIMEOUT = 20;


    public static OkHttpClient getOkHttpClient(Context context, int timeout) {
        if (okHttpClient == null) {
            initClient(context, timeout);
        }
        return okHttpClient;
    }

    public static void initClient(Context context, int timeout) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        if (!TextUtils.isEmpty(CER_NAME)) {
            try (InputStream open = context.getResources().getAssets().open(CER_NAME)) {
                SslParams sslParams = getSslSocketFactory(new InputStream[]{open}, null, null);
                okHttpClient = new OkHttpClient.Builder()
                        .addNetworkInterceptor(interceptor)
                        .addInterceptor(interceptor)
                        .cache(HttpCache.getCache())
                        //设置连接超时
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        //设置读超时
                        .readTimeout(timeout, TimeUnit.SECONDS)
                        //设置写超时
                        .writeTimeout(timeout, TimeUnit.SECONDS)
                        //是否自动重连
                        .retryOnConnectionFailure(false)
                        .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                        .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                        .build();
            } catch (IOException e) {
                Log.e(TAG, "initClient: ", e);
            }
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(interceptor)
                    .addInterceptor(interceptor)
                    .cache(HttpCache.getCache())
                    //设置连接超时
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    //设置读超时
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    //设置写超时
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    //是否自动重连
                    .retryOnConnectionFailure(false)
                    .build();
        }

    }

    public static class SslParams {
        private SSLSocketFactory sSLSocketFactory;
        private X509TrustManager trustManager;
    }

    public static SslParams getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        SslParams sslParams = new SslParams();
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager;
            if (trustManagers != null) {
                trustManager = new MyTrustManager(chooseTrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
            return sslParams;
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new AssertionError(e);
        }
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) {
            return new TrustManager[0];
        }
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
            TrustManagerFactory trustManagerFactory;

            trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            return trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return new TrustManager[0];
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) {
                return new KeyManager[0];
            }

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return new KeyManager[0];
    }

    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d(TAG, "checkClientTrusted");
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            Log.d(TAG, "checkClientTrusted");
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            Log.d(TAG, "checkServerTrusted");
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    }
}
