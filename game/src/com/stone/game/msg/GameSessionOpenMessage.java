package com.stone.game.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stone.core.msg.MessageParseException;
import com.stone.game.session.GamePlayerSession;

/**
 * Game session open message;
 * 
 * @author crazyjohn
 *
 */
public class GameSessionOpenMessage extends BaseCGMessage {
	private Logger logger = LoggerFactory.getLogger(GameSessionCloseMessage.class);

	public GameSessionOpenMessage(GamePlayerSession sessionInfo) {
		this.session = sessionInfo;
	}

	@Override
	public void execute() throws MessageParseException {
		logger.info(String.format("Session opened: %s", this.session));
	}

	@Override
	protected boolean readBody() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean writeBody() {
		throw new UnsupportedOperationException();
	}

}
