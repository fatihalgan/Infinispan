package com.packtpub.infinispan.main;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.infinispan.Cache;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

@Singleton
public class GreetingService {

	@Inject
    private Cache<String, String> cache;
 
    public String greet(String user) {
        String cachedValue = cache.get(user);
        if (cachedValue == null) {
            cachedValue = "Hello " + user;
            cache.put(user, cachedValue);
        }
        return cachedValue;
    }
    
    public static void main(String[] args) {
    	WeldContainer weld = new Weld().initialize();
    	GreetingService greeting = weld.instance().select(GreetingService.class).get();
    	greeting.greet(args[0]);
    }
}
