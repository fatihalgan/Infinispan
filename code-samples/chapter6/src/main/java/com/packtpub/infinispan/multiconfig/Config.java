package com.packtpub.infinispan.multiconfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class Config {

	public static final String ADDRESS_FAR_AWAY = "localhost";
	public static final String ADDRESS = "localhost";
	public static final int PORT = 0; 
	
	@Produces
	@ApplicationScoped
	public EmbeddedCacheManager defaultEmbeddedCacheManager() {
		Configuration cfg = new ConfigurationBuilder()
			.eviction().strategy(EvictionStrategy.LRU)
			.maxEntries(150).build();
		return new DefaultCacheManager(cfg);
	}
	
	@Produces
	@ApplicationScoped
	@ClusteredCache //A custom qualifier
	public EmbeddedCacheManager clusteredEmbeddedCacheManager() {
		Configuration cfg = new ConfigurationBuilder()
			.eviction().strategy(EvictionStrategy.LRU)
			.maxEntries(150).build();
		return new DefaultCacheManager(cfg);
	}
	
	@Produces
	@ApplicationScoped
	public RemoteCacheManager defaultRemoteCacheManager() {
		return new RemoteCacheManager(ADDRESS, PORT);
	}
	
	@Produces
	@ApplicationScoped
	@RemoteCacheInDifferentDataCenter //A custom qualifier
	public RemoteCacheManager remoteCacheManagerInDifferentDataCenter() {
		return new RemoteCacheManager(ADDRESS_FAR_AWAY, PORT);
	}
}
