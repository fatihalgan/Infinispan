package com.packtpub.infinispan.chapter2.listener;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStarted;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStopped;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStoppedEvent;

@Listener
public class SimpleListener {

	@CacheEntryCreated
	public void dataAdded(CacheEntryCreatedEvent evt) {
		if(evt.isPre()) {
			System.out.println("Going to add new entry " + evt.getKey() + " created in the cache");
		} else {
			System.out.println("Added new entry " + evt.getKey() + " to the cache"); 
		}
	}
	
	@CacheEntryRemoved
	public void dataRemoved(CacheEntryRemovedEvent evt) {
		if(evt.isPre()) {
			System.out.println("Going to remove entry " + evt.getKey() + " created in the cache");
		} else {
			System.out.println("Removed entry " + evt.getKey() + " from the cache"); 
		}
	}
	
	@CacheStarted
	public void cacheStarted(CacheStartedEvent evt) {
		System.out.println("Cache Started");
	}
	
	@CacheStopped
	public void cacheStopped(CacheStoppedEvent evt) {
		System.out.println("Cache Stopped");
	}
}
