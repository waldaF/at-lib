package com.walder.at.lib.petshop;

import com.walder.at.lib.auth.BasicAuthProvider;
import com.walder.at.lib.auth.UnAuthProvider;
import com.walder.at.lib.client.Client;
import com.walder.at.lib.listener.TestNgListener;
import com.walder.at.lib.provider.KeyProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners(TestNgListener.class)
class PetStoreGenericIT {
	protected Client petStoreClient;
	protected Client anotherBaseAuthClient;
	@BeforeClass
	public void init() {

		final String petStoreUrl = KeyProvider.loadProperty("com.petstore.url");

		this.petStoreClient = new Client(UnAuthProvider.builder()
				.url(petStoreUrl)
				.build()
		);

		this.anotherBaseAuthClient = new Client(BasicAuthProvider.builder()
				.url(petStoreUrl)
				.username(KeyProvider.loadProperty("com.petstore.username"))
				.password(KeyProvider.loadProperty("com.petstore.password"))
				.build());
	}
}
