package com.listapp.API_Utility.AsyncTask_Utility;

import android.net.SSLCertificateSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ClientSSLSocketFactory extends SSLCertificateSocketFactory {
    static private SSLContext sslContext;
    static private int time;

    /**
     * @param handshakeTimeoutMillis
     * @deprecated
     */
    public ClientSSLSocketFactory(int handshakeTimeoutMillis) {
        super(handshakeTimeoutMillis);
    }

    public ClientSSLSocketFactory(int handshakeTimeoutMillis, SSLContext sslContext) {
        super(handshakeTimeoutMillis);
        this.sslContext = sslContext;
    }

    public static SSLSocketFactory getSocketFactory() {
        try {
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{tm}, null);
            // Install the all-trusting trust manager
            //  final SSLContext sslContext = SSLContext.getInstance("SSL");
            ///  sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            //  SSLSocketFactory ssf = ClientSSLSocketFactory.getDefault(10000,new SSLSessionCache());

            //   return ssf;
            return sslContext.getSocketFactory();

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}