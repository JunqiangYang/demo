package com.zz.startup.security;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListener implements org.apache.shiro.session.SessionListener {

	private static Logger logger = LoggerFactory.getLogger(SessionListener.class);
	
	public void onStart(Session session) {
		logger.info("on start:{}" + session.getId());
	}

	public void onStop(Session session) {
		logger.info("on stop:{}" + session.getId());
	}

	public void onExpiration(Session session) {
		logger.info("on expiration:{}" + session.getId());
	}

}
