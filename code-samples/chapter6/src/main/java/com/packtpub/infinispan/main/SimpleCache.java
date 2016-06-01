package com.packtpub.infinispan.main;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import model.Ticket;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import com.packtpub.infinispan.multiconfig.ClusteredCache;

import utils.IOUtils;

@Singleton
public class SimpleCache {

	@Inject
	@ClusteredCache
	DefaultCacheManager manager;
	
	public void start() {
		Cache<Integer, Ticket> cache = manager.getCache();
		String command = "";
		int ticketId = 1;
		
		IOUtils.dumpWelcomeMessage();
		
		while(true) {
			command = IOUtils.readLine("> ");
			if(command.equals("book")) {
				String name = IOUtils.readLine("Enter name ");
				String show = IOUtils.readLine("Enter show ");
				
				Ticket ticket = new Ticket(name, show);
				cache.put(ticketId, ticket);
				log("Booked ticket "+ticket);
				ticketId++;
			} else if(command.equals("pay")) {
				Integer id = new Integer(IOUtils.readLine("Enter ticketId "));
				Ticket ticket = cache.remove(id);
				log("Checked out ticket " + ticket);
			} else if(command.equals("list")) {
				Set<Integer> set = cache.keySet();
				for(Integer ticket : set) {
					log(cache.get(ticket).toString());
				}
			} else if(command.equals("quit")) {
				cache.stop();
				log("Bye");
				break;
			} else {
				log("Unknown command "+command);
				IOUtils.dumpWelcomeMessage();
			}
		}
	}
	
	public static void log(String s){
		System.out.println(s);
	}

	
	public static void main(String[] args) {
		WeldContainer weld = new Weld().initialize();
		SimpleCache simpleCache = weld.instance().select(SimpleCache.class).get();
		simpleCache.start();
	}
}
