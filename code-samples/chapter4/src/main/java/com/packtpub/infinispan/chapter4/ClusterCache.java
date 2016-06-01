package com.packtpub.infinispan.chapter4;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import model.Ticket;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

import utils.IOUtils;

public class ClusterCache {
	
	DefaultCacheManager m = null;
	Cache<Integer, Ticket> cache = null;
	
	public void start() {
		try {
			m = new DefaultCacheManager("cluster.xml");
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		cache = m.getCache("clusteredCache");
		String command = ""; // Line read from standard in
		int ticketid = 1;
		
		IOUtils.dumpWelcomeMessage();
		
		while(true) {
			command = IOUtils.readLine("> ");
			if(command.equals("book")) {
				String name = IOUtils.readLine("Enter name ");
				String show = IOUtils.readLine("Enter show ");
				
				Ticket ticket = new Ticket(name, show);
				cache.put(ticketid, ticket);
				log("Booked ticket " + ticket);
				ticketid++;
			} else if(command.equals("change")) {
				String id = IOUtils.readLine("Enter Ticketid ");
				String show = IOUtils.readLine("Enter show ");
				
				Ticket ticket = cache.get(Integer.parseInt(id));
				ticket.setShow(show);
				
				beginTransaction();
				cache.replace(Integer.parseInt(id), ticket);
				String captcha = UUID.randomUUID().toString().substring(0,4);
				String check = IOUtils.readLine("Enter captcha "+captcha);
				if (captcha.equals(check)) {
					commitTransaction();
					log("Updated ticket "+ticket);
				} else {
					rollbackTransaction();
					log("Updated failed!");
				}
				
			} else if(command.equals("pay")) {
				Integer id = new Integer(IOUtils.readLine("Enter ticketid "));
				Ticket ticket = cache.remove(id);
				log("Checked out ticket " + ticket);
			} else if(command.equals("list")) {
				Set<Integer> keySet = cache.keySet();
				for(Integer key : keySet) {
					log(cache.get(key).toString());
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
	
	public void beginTransaction() {
		try {
			cache.getAdvancedCache().getTransactionManager().begin();
		} catch (Exception e) {
			log("Cannot start transaction: " + e.getMessage());
			e.printStackTrace();
		} 
	}
		
	public void commitTransaction() {
		try {
			cache.getAdvancedCache().getTransactionManager().commit();
		} catch(Exception e) {
			log("Cannot commit transaction: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void rollbackTransaction() {
		try {
			cache.getAdvancedCache().getTransactionManager().rollback();
		} catch(Exception e) {
			log("Cannot rollback transaction: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void log(String s){
		System.out.println(s);
	}

	
	public static void main(String[] args) {
		new ClusterCache().start();
	}
}
