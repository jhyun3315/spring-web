package kr.ac.cnu.web.service;

import kr.ac.cnu.web.games.blackjack.Deck;
import kr.ac.cnu.web.games.blackjack.GameRoom;
import kr.ac.cnu.web.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rokim on 2018. 5. 26..
 */
@Service
public class BlackjackService {
    private final int DECK_NUMBER = 1;
    private final Map<String, GameRoom> gameRoomMap = new HashMap<>();

    public GameRoom createGameRoom(User user) {
        Deck deck = new Deck(DECK_NUMBER);

        GameRoom gameRoom = new GameRoom(deck);
        gameRoom.addPlayer(user.getName(), user.getAccount());

        gameRoomMap.put(gameRoom.getRoomId(), gameRoom);

        return gameRoom;
    }

    public GameRoom joinGameRoom(String roomId, User user) {
        // multi player Game 이 아니므로 필요가 없다.
        return null;
    }

    public void leaveGameRoom(String roomId, User user) {
        gameRoomMap.get(roomId).removePlayer(user.getName());
    }

    public GameRoom getGameRoom(String roomId) {
        return gameRoomMap.get(roomId);
    }

    public GameRoom bet(String roomId, User user, long bet) {
        GameRoom gameRoom = gameRoomMap.get(roomId);

        gameRoom.reset();
        gameRoom.bet(user.getName(), bet);
        gameRoom.deal();

        return gameRoom;
    }

    public GameRoom hit(String roomId, User user) {
        GameRoom gameRoom = gameRoomMap.get(roomId);

        gameRoom.hit(user.getName());
        if(gameRoom.getPlayerList().get(user.getName()).getHand().getCardSum() >= 21){
            gameRoom.getPlayerList().get(user.getName()).setIsPlaying(false);
            gameRoom.playDealer_FinCase();
        }
        else{
            gameRoom.playDealer_OnCase();
            if(gameRoom.getDealer().getHand().getCardSum() >= 21){
                gameRoom.getPlayerList().get(user.getName()).setIsPlaying(false);
            }
        }
        return gameRoom;
    }

    public GameRoom stand(String roomId, User user) {
        GameRoom gameRoom = gameRoomMap.get(roomId);

        gameRoom.stand(user.getName());
        gameRoom.playDealer();

        return gameRoom;
    }

    public GameRoom doubledown(String roomId, User user) {
        GameRoom gameRoom = gameRoomMap.get(roomId);
        gameRoom.getPlayerList().get(user.getName()).placeBet(gameRoom.getPlayerList().get(user.getName()).getCurrentBet());
        gameRoom.getPlayerList().get(user.getName()).setCurrentBet(gameRoom.getPlayerList().get(user.getName()).getCurrentBet() *2);

        gameRoom.hit(user.getName());

        gameRoom.getPlayerList().get(user.getName()).setIsPlaying(false);
        gameRoom.playDealer_FinCase();


        return gameRoom;
    }

        public long getPlayerAccount(String roomId, String name) {
        GameRoom gameRoom = gameRoomMap.get(roomId);
        return gameRoom.getPlayerAccount(name);
    }
}
