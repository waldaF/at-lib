package com.walder.at.lib.client;

import com.walder.at.lib.exceptions.HostnameVerifierUtilsException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

class HostNameVerifier {
	private static final String SSL = "SSL";

	TrustManager[] getTrustedCerts() {
		return new TrustManager[]{new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
				// without checking client certs
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
				// without checking server certs
			}
		}};
	}

	SSLContext getSSLInstant() {
		try {
			return SSLContext.getInstance(SSL);
		} catch (NoSuchAlgorithmException e) {
			throw new HostnameVerifierUtilsException("Error during deserialize SSL instant", e);
		}
	}

	SSLContext initSSL() {
		final SSLContext context = getSSLInstant();
		final TrustManager[] trustedCerts = getTrustedCerts();
		try {
			context.init(null, trustedCerts, new java.security.SecureRandom());
			return context;
		} catch (KeyManagementException e) {
			throw new HostnameVerifierUtilsException("Error during init SSL context", e);
		}
	}
}