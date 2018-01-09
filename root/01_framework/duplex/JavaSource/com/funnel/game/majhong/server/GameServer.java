package com.funnel.game.majhong.server;

import org.apache.log4j.Logger;

import com.duplex.frame.ByteArrayTransfer.Mode;
import com.duplex.frame.handler.ByteArraytProtocolHandler;
import com.funnel.game.majhong.server.protocol.PLATFORM_LAYER_Handler;
import com.funnel.game.majhong.server.protocol.TRANSPORT_LAYER_Handler;
import com.funnel.game.majhong.server.protocol.platform.CONNECT_GAME_PROTOCOL;
import com.funnel.game.majhong.server.protocol.platform.CREATE_ROOM_PROTOCOL;
import com.funnel.game.majhong.server.protocol.platform.DISTRIBUTE_GAME_PROTOCOL;
import com.funnel.game.majhong.server.protocol.platform.EXIT_GAME_Protocol;
import com.funnel.game.majhong.server.protocol.platform.JOIN_ROOM_PROTOCOL;
import com.funnel.game.majhong.server.protocol.platform.LOGIN_GAME_Protocol;
import com.funnel.game.majhong.server.protocol.platform.PLAY_GAME_Protocol;
import com.funnel.game.majhong.server.protocol.platform.QUERY_USERINFO_PROTOCOL;
import com.funnel.game.majhong.server.protocol.platform.READY_GAME_Protocol;

public class GameServer {

	private final static Logger logger = Logger.getLogger(GameServer.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Mode name = null;
		int mode = 1;
		if (args.length > 0) {
			mode = Integer.parseInt(args[0]);
			
		}

		logger.info("GameServer args="+args);
		logger.info("GameServer mode="+mode);
		switch (mode) {
		case 0:
			name = Mode.NONE;
			break;

		case 1:
			name = Mode.UPPER_TO_LOW;
			break;

		default:
			break;
		}

		logger.info("GameServer mode="+name);
		
		TRANSPORT_LAYER_Handler transport_LAYER_Handler = new TRANSPORT_LAYER_Handler();

		PLATFORM_LAYER_Handler platform_LAYER_Handler = new PLATFORM_LAYER_Handler();
		platform_LAYER_Handler.addHandler(new CONNECT_GAME_PROTOCOL());
		platform_LAYER_Handler.addHandler(new CREATE_ROOM_PROTOCOL());
		platform_LAYER_Handler.addHandler(new DISTRIBUTE_GAME_PROTOCOL());
		platform_LAYER_Handler.addHandler(new EXIT_GAME_Protocol());
		platform_LAYER_Handler.addHandler(new JOIN_ROOM_PROTOCOL());
		platform_LAYER_Handler.addHandler(new LOGIN_GAME_Protocol());
		platform_LAYER_Handler.addHandler(new PLAY_GAME_Protocol());
		platform_LAYER_Handler.addHandler(new QUERY_USERINFO_PROTOCOL());
		platform_LAYER_Handler.addHandler(new READY_GAME_Protocol());


		transport_LAYER_Handler.setNextLayerhandler(platform_LAYER_Handler);

		ByteArraytProtocolHandler byteArraytProtocolHandler = new ByteArraytProtocolHandler(
				transport_LAYER_Handler, name);

		GameServerAgent serverJobAgent = new GameServerAgent(11234,
				byteArraytProtocolHandler, name);

		serverJobAgent.start();

	}

}
