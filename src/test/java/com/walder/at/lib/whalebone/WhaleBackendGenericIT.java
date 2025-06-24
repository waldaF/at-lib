package com.walder.at.lib.whalebone;

import com.walder.at.lib.auth.UnAuthProvider;
import com.walder.at.lib.client.rest_api.Client;
import com.walder.at.lib.listener.TestNgListener;
import com.walder.at.lib.provider.KeyProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners(TestNgListener.class)
class WhaleBackendGenericIT {
	protected Client whaleBoneClientUnauth;

	@BeforeClass
	public void init() {
		final String petStoreUrl = KeyProvider.loadProperty("com.whalebone.backend.url");
		this.whaleBoneClientUnauth = new Client(UnAuthProvider.builder()
				.url(petStoreUrl)
				.build()
		);
	}
}
