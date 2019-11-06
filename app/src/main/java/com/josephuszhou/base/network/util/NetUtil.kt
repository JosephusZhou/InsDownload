package com.josephuszhou.base.network.util

import android.annotation.SuppressLint
import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @author senfeng.zhou
 * @date 2019-07-11
 * @desc
 */
class NetUtil {

    companion object {

        /**
         * 全局自签名证书
         */
        class SSLParams {
            var sSLSocketFactory: SSLSocketFactory? = null
            var trustManager: X509TrustManager? = null
        }

        fun getSSLSocketFactory(
            bksFile: InputStream?,
            password: String?,
            certificates: Array<InputStream>?
        ): SSLParams {
            val sslParams = SSLParams()
            try {
                val keyManagers = prepareKeyManager(bksFile, password)
                val trustManagers = prepareTrustManager(certificates)
                val sslContext = SSLContext.getInstance("TLS")
                val trustManager: X509TrustManager = if (trustManagers != null) {
                    MyTrustManager(
                        chooseTrustManager(trustManagers)
                    )
                } else {
                    UnSafeTrustManager()
                }
                sslContext.init(keyManagers, arrayOf<TrustManager>(trustManager), null)
                sslParams.sSLSocketFactory = sslContext.socketFactory
                sslParams.trustManager = trustManager
                return sslParams
            } catch (e: NoSuchAlgorithmException) {
                throw AssertionError(e)
            } catch (e: KeyManagementException) {
                throw AssertionError(e)
            } catch (e: KeyStoreException) {
                throw AssertionError(e)
            }
        }

        private fun prepareKeyManager(bksFile: InputStream?, password: String?): Array<KeyManager>? {
            try {
                if (bksFile == null || password == null) return null
                val clientKeyStore = KeyStore.getInstance("BKS")
                clientKeyStore.load(bksFile, password.toCharArray())
                val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                keyManagerFactory.init(clientKeyStore, password.toCharArray())
                return keyManagerFactory.keyManagers
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        private fun prepareTrustManager(certificates: Array<InputStream>?): Array<TrustManager>? {
            if (certificates == null || certificates.isEmpty())
                return null

            try {
                val certificateFactory = CertificateFactory.getInstance("X.509")
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null)
                for ((index, certificate) in certificates.withIndex()) {
                    val certificateAlias = index.toString()
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate))
                    try {
                        certificate.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                val trustManagerFactory: TrustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(keyStore)
                return trustManagerFactory.trustManagers
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
            for (trustManager in trustManagers) {
                if (trustManager is X509TrustManager) {
                    return trustManager
                }
            }
            return null
        }

        @SuppressLint("TrustAllX509TrustManager")
        private class UnSafeTrustManager : X509TrustManager {

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

        @SuppressLint("TrustAllX509TrustManager")
        private class MyTrustManager @Throws(NoSuchAlgorithmException::class, KeyStoreException::class)
        constructor(private val localTrustManager: X509TrustManager?) : X509TrustManager {

            private val defaultTrustManager: X509TrustManager?

            init {
                val var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                var4.init(null as KeyStore?)
                defaultTrustManager =
                    chooseTrustManager(var4.trustManagers)
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                try {
                    defaultTrustManager?.checkServerTrusted(chain, authType)
                } catch (ce: CertificateException) {
                    localTrustManager?.checkServerTrusted(chain, authType)
                }

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    /**
     * 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
     * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
     * 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true
     */
    @SuppressLint("BadHostnameVerifier")
    class DefaultHostnameVerifier : HostnameVerifier {

        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

}