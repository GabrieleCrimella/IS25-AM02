package it.polimi.ingsw.is25am02.controller.client;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.GameV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;

import java.rmi.RemoteException;
import java.util.List;

//PlayerV diventa String nickname
//PlayerColorV diventa int numEnum
//tile in take tile non so come fare!!, id? immagine?
//Coordinate diventa x e y
//RotationTypeV diventa int numEnum
//AliveTypeV diventa int numEnum
//BoxTypeV diventa int numEnum

public class ClientController implements VirtualServer {
    private final ConnectionClient connection;
    private MenuState menuState;
    private boolean running;
    private GameV gameV;
    //todo devo definire gameV una volta che so in che game sono

    public ClientController(ConnectionClient connection) {
        this.connection = connection;
        this.menuState = MenuState.LOGIN;
        this.running = true;
    }

    public void setGameV(GameV gameV) {
        this.gameV = gameV;
    }

    public void setMenuState(MenuState state) {
        menuState = state;
    }

    public VirtualView getVirtualView() {
        return (VirtualView) connection;
    }
    
    public void closeConnect() throws Exception {
        connection.closeConnection();
        running = false;
    }

    private PlayerV getPlayerVFromNickname(String nickname) throws RemoteException {
        for (PlayerV p : gameV.getPlayers()) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        //todo messagio di errore
        //throw new PlayerNotFoundException("Player <" + nickname + "> not found");
        return null; //messo solo per non dare errore
    }

    @SuppressWarnings("all")
    public void ping(String nickname) throws RemoteException {
        Thread pingThread = new Thread(() -> {
            while (running) {
                try {
                    connection.getServer().ping(nickname);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (RemoteException e) {
                    connection.getConsole().displayMessage("error.connection.command", null);
                    break;
                }
            }
        });
        pingThread.start();
    }

    public void nicknameRegistration(String nickname, VirtualView client) throws RemoteException {
        if(menuControl(MenuState.LOGIN)) {
            connection.getServer().nicknameRegistration(nickname, client);
            ping(nickname);
        }
    }

    public void createLobby(VirtualView client, String nickname, int maxPlayers, PlayerColor color, int level) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().createLobby(client, nickname, maxPlayers, color, level);
        }
    }

    public void getLobbies(VirtualView client) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().getLobbies(client);
        }
    }

    public void joinLobby(VirtualView client, int lobbyId, String nickname, PlayerColor color) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().joinLobby(client, lobbyId, nickname, color);
        }
    }

    public void isGameRunning(VirtualView client, int lobbyId) throws RemoteException {
        if(menuControl(MenuState.MENU)) {
            connection.getServer().isGameRunning(client, lobbyId);
        }
    }

    public void flipHourglass(String nickname) throws RemoteException {
        if (menuControl(MenuState.GAME)&&gameV.levelControl() && gameV.buildControl() && (gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))||gameV.stateControl(StateGameType.BUILD, StatePlayerType.FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname)))) {
            connection.getServer().flipHourglass(nickname);
        }
    }

    public void takeTile(String nickname) throws RemoteException {
        if (menuControl(MenuState.GAME)&&gameV.buildControl()&&gameV.stateControl(gameV.getCurrentState().getPhase(), gameV.getCurrentState().getCurrentPlayer().getStatePlayer(), gameV.getCurrentState().getCurrentCard().getStateCard(), getPlayerVFromNickname(nickname) )&&gameV.currentTileControl(getPlayerVFromNickname(nickname))) {
            //todo manca il player in entrambi gli if
            connection.getServer().takeTile(nickname);
        }

    }

    public void takeTile(String nickname, Tile tile) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.buildControl()&&gameV.stateControl(gameV.getCurrentState().getPhase(), gameV.getCurrentState().getCurrentPlayer().getStatePlayer(), gameV.getCurrentState().getCurrentCard().getStateCard(), getPlayerVFromNickname(nickname) )&& gameV.currentTileControl(getPlayerVFromNickname(nickname))) {
            connection.getServer().takeTile(nickname, tile);
        }
    }

    public void takeMiniDeck(String nickname, int index) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.levelControl()&&gameV.buildControl()&&gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))&& gameV.currentTileControl(getPlayerVFromNickname(nickname))&& gameV.deckAllowedControl(getPlayerVFromNickname(nickname))){
            connection.getServer().takeMiniDeck(nickname, index);
        }
    }

    public void returnMiniDeck(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.levelControl()&&gameV.buildControl()&&gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))&& gameV.currentTileControl(getPlayerVFromNickname(nickname))&& gameV.deckAllowedControl(getPlayerVFromNickname(nickname))){
            connection.getServer().returnMiniDeck(nickname);
        }
    }

    public void bookTile(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.levelControl()&& gameV.buildControl()&&gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().bookTile(nickname);
        }
    }

    public void addBookedTile(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.levelControl()&& gameV.buildControl()&&gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().addBookedTile(nickname, index, pos, rotation);
        }
    }

    public void returnTile(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.buildControl()&&gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().returnTile(nickname);
        }

    }

    public void addTile(String nickname, Coordinate pos, RotationType rotation) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.buildControl()&& gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().addTile(nickname, pos, rotation);
        }

    }

    public void shipFinished(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.BUILD, StatePlayerType.NOT_FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().shipFinished(nickname);
        }
    }

    //todo qui puoi controllare che la spaceship vada bene prima di mandarlo al server
    public void checkSpaceship(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.CHECK, StatePlayerType.FINISHED, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().checkSpaceship(nickname);
        }
    }

    public void removeTile(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.CORRECTION, StatePlayerType.WRONG_SHIP, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().removeTile(nickname, pos);
        }
    }

    public void checkWrongSpaceship(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.CORRECTION, StatePlayerType.WRONG_SHIP, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().checkWrongSpaceship(nickname);
        }
    }

    public void addCrew(String nickname, Coordinate pos, AliveType type) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.levelControl()&& gameV.stateControl(StateGameType.INITIALIZATION_SPACESHIP, StatePlayerType.CORRECT_SHIP, StateCardType.FINISH, getPlayerVFromNickname(nickname))&&gameV.cabinControl(getPlayerVFromNickname(nickname),pos)){
            connection.getServer().addCrew(nickname, pos, type);
        }
    }

    public void ready(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.INITIALIZATION_SPACESHIP, StatePlayerType.CORRECT_SHIP, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().ready(nickname);
        }
    }

    public void playNextCard(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.TAKE_CARD, StatePlayerType.IN_GAME, StateCardType.FINISH, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().playNextCard(nickname);
        }
    }

    public void earlyLanding(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.levelControl()&& gameV.stateControl(StateGameType.TAKE_CARD, StatePlayerType.IN_GAME, StateCardType.FINISH, getPlayerVFromNickname(nickname))){
            connection.getServer().earlyLanding(nickname);
        }
    }

    public void choice(String nickname, boolean choice) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.DECISION,  getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().choice(nickname, choice);
        }
    }

    public void removeCrew(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.REMOVE, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))&&gameV.typeControl(getPlayerVFromNickname(nickname), pos, TileType.CABIN)){
            connection.getServer().removeCrew(nickname, pos);
        }
    }

    public void choiceBox(String nickname, boolean choice) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.DECISION, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().choiceBox(nickname, choice);
        }
    }

    public void moveBox(String nickname, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.BOXMANAGEMENT, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().moveBox(nickname, start, end, boxType, on);
        }
    }

    public void choicePlanet(String nickname, int index) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.DECISION,  getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().choicePlanet(nickname, index);
        }
    }

    public void choiceDoubleMotor(String nickname, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().choiceDoubleMotor(nickname, motors, batteries);
        }
    }

    public void choiceDoubleCannon(String nickname, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.CHOICE_ATTRIBUTES, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().choiceDoubleCannon(nickname, cannons, batteries);
        }
    }

    public void choiceCrew(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.CHOICE_ATTRIBUTES, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().choiceCrew(nickname);
        }
    }

    public void removeBox(String nickname, Coordinate pos, BoxType type) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.REMOVE, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().removeBox(nickname, pos, type);
        }
    }

    public void removeBattery(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.REMOVE,getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))&& gameV.typeControl(getPlayerVFromNickname(nickname), pos, TileType.BATTERY)){
            connection.getServer().removeBattery(nickname, pos);
        }
    }

    public void rollDice(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME) && gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.ROLL, getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().rollDice(nickname);
        }
    }

    public void calculateDamage(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME) && gameV.stateControl(StateGameType.EFFECT_ON_PLAYER, StatePlayerType.IN_GAME, StateCardType.CHOICE_ATTRIBUTES,getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().calculateDamage(nickname, pos);
        }
    }

    public void keepBlock(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)&&gameV.stateControl(StateGameType.CORRECTION, StatePlayerType.WRONG_SHIP, StateCardType.FINISH,getPlayerVFromNickname(nickname))&& gameV.currentPlayerControl(getPlayerVFromNickname(nickname))){
            connection.getServer().keepBlock(nickname, pos);
        }
    }

    public void Winners(int lobbyId) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().Winners(lobbyId);
        }
    }

    public void endGame(int lobbyId) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().endGame(lobbyId);
        }
    }

    private boolean menuControl(MenuState state){
        if(menuState != state){
            if(menuState == MenuState.LOGIN){
                connection.getConsole().reportError("error.menu.login.off", null);
            } else if (menuState == MenuState.MENU) {
                if(state == MenuState.LOGIN) {
                    connection.getConsole().reportError("error.menu.login.on", null);
                } else if (state == MenuState.WAITING) {
                    connection.getConsole().reportError("error.menu.wait", null);
                }
            } else if (menuState == MenuState.WAITING) {
                connection.getConsole().reportError("error.menu.wait", null);
            } else if (menuState == MenuState.GAME) {
                connection.getConsole().reportError("error.menu.game", null);
            }
            return false;
        } else { return true; }
    }


}
