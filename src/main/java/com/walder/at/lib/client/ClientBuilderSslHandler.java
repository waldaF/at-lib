package com.walder.at.lib.client;

import jakarta.ws.rs.client.ClientBuilder;
import lombok.NoArgsConstructor;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

@NoArgsConstructor
class ClientBuilderSslHandler {
	 ClientBuilder withFakeSslContext() {

		final SSLContext sslContext = new HostNameVerifier().initSSL();
		final HostnameVerifier hostnameVerifier = (hostname, session) -> true;

		return ClientBuilder.newBuilder()
				.sslContext(sslContext)
				.hostnameVerifier(hostnameVerifier);
	}
}
