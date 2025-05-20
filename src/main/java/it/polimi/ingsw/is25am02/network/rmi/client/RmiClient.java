package it.polimi.ingsw.is25am02.network.rmi.client;

import it.polimi.ingsw.is25am02.controller.client.MenuState;
import it.polimi.ingsw.is25am02.model.Lobby;
import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.LobbyView;
import it.polimi.ingsw.is25am02.utils.enumerations.*;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.PlayerV;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.*;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.ConsoleClient;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.tile.TileV;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class RmiClient extends UnicastRemoteObject implements VirtualView, ConnectionClient {
    VirtualServer server;
    ConsoleClient console;
    GameV gameV;

    public RmiClient() throws RemoteException {
        super();
    }

    public void startConnection(String ip) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ip);
        System.out.println("Tentativo di lookup...");
        server = (VirtualServer) registry.lookup("RMIServer");
        System.out.println("Lookup riuscito...");

        System.out.println(">> RMI Client ready.");
    }

    public void closeConnection() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
        System.out.println(">> RMI Client stopped.");
    }

    public void setView(ConsoleClient choice) {
        console = choice;
    }

    public void setNickname(String nickname) {
        console.setNickname(nickname);
    }

    public VirtualServer getServer() {
        return server;
    }

    public ConsoleClient getConsole() {
        return console;
    }

    public void setGameV(GameV gameV) {
        this.gameV = gameV;
    }

    public GameV getGameV() {
        return gameV;
    }

    @Override
    public void spaceshipBrokenUpdate(String details, Coordinate[][] ships) throws RemoteException {
        console.spaceshipBrokenUpdate(details, ships);
    }

    @Override
    public void reportError(String keys, Map<String, String> params) throws RemoteException {
        console.reportError(keys, params);
    }

    @Override
    public void displayMessage(String keys, Map<String, String> params) throws RemoteException {
        console.displayMessage(keys, params);
    }

    @Override
    public void setLobbiesView(Map<Integer, LobbyView> lobbies) throws RemoteException {
        console.setLobbiesView(lobbies);
    }

    @Override
    public void showTileRemoval(Coordinate coordinate, String nickname) { //tolgo dal player p la tile in posizione coordinate
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()) {
                    if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getType().equals(TileType.CABIN)) {
                        showCrewRemoval(coordinate, nickname);
                    } else if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getType().equals(TileType.BATTERY)) {
                        showBatteryRemoval(coordinate, nickname, playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumBattery() - 1);
                    }
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()] = Optional.empty();
                }
            }
        }

        if (console.getNickname().equals(nickname)){
            printOnConsole();
        }
    }

    @Override
    public void showBatteryRemoval(Coordinate coordinate, String nickname, int numBattery) {
        //voglio mettere nella tile in coordinate nella spaceship copiata una batteria in meno
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()) {
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBattery(numBattery);
                }
            }
        }

        if (console.getNickname().equals(nickname)){
            printOnConsole();
        }
    }
    //todo questo rimuove in automatica, sarebbe meglio che si prendesse la lista di alive, vedesse il tipo e quanti ce ne sono e lo settasse corretto.
    @Override
    public void showCrewRemoval(Coordinate coordinate, String nickname) {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()) {
                    if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumBAliens() > 0) {
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBAliens(0);
                    } else if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumPAliens() > 0) {
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumPAliens(0);
                    } else {
                        playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumHumans(playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().getNumHumans() - 1);
                    }
                }
            }
        }
    }

    @Override
    public void showBoxUpdate(Coordinate coordinate, String nickname, List<BoxType> boxList) {
        int numred = 0;
        int numyellow = 0;
        int numblue = 0;
        int numgreen = 0;
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if (playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].isPresent()) {
                    for (BoxType box : boxList) {
                        if (box.equals(BoxType.RED)) {
                            numred++;
                        } else if (box.equals(BoxType.YELLOW)) {
                            numyellow++;
                        } else if (box.equals(BoxType.GREEN)) {
                            numgreen++;
                        } else {
                            numblue++;
                        }
                    }
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumYellowBox(numyellow);
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumGreenBox(numgreen);
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumBlueBox(numblue);
                    playerv.getSpaceshipBoard()[coordinate.x()][coordinate.y()].get().setNumRedBox(numred);
                }
            }
        }
        if (console.getNickname().equals(nickname)){
            printOnConsole();
        }
    }

    @Override
    public void showCreditUpdate(String nickname, int cosmicCredits) {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                playerv.setCredits(cosmicCredits);
            }
        }
        if (console.getNickname().equals(nickname)){
            printOnConsole();
        }
    }

    //todo cosa deve fare?
    @Override
    public void showUpdatedOthers() {

    }

    @Override
    public void showPositionUpdate(String nickname, int position) {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                gameV.getGlobalBoard().setPosition(playerv, position);
            }
        }
    }

    //viene chiamato quando viene girata la clessidra
    @Override
    public void showHourglassUpdate(long timeLeft) {
        //gameV.getHourglass().flip();
        gameV.getHourglass().setTimeLeft(timeLeft);
    }

    @Override
    public void showDiceUpdate(String nickname, int result) {
        gameV.getDiceV().setResult(result);
    }

    @Override
    public void showVisibilityUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox) { //todo voglio aggiungere una tile all'heaptile
        TileV tileV = new TileV(tType, connectors, rotationType, true, imagepath, maxBattery, maxBox);
        gameV.getHeapTilesV().addToHeapTile(tileV);
    }

    // tolgo un tile che è stata messa in una nave
    @Override
    public void showTileRemovalFromHeapTile(String imagepath) {
        for (TileV tileV : gameV.getHeapTilesV().getListTileV()) {
            if (tileV.getImagePath().equals(imagepath)) {
                gameV.getHeapTilesV().removeTile(tileV);
                return;
            }
        }
    }

    @Override
    public void showDeckAllowUpdate(String player) throws RemoteException {
        for (PlayerV playerV : gameV.getPlayers()) {
            if (player.equals(playerV.getNickname())) {
                playerV.setDeckAllowed();
                return;
            }
        }
    }

    @Override
    public void setMenuState(MenuState state) throws RemoteException {
        console.getController().setMenuState(state);
    }

    @Override
    public void showMinideckUpdate(String nickname, int deck) {
        for (PlayerV playerV : gameV.getPlayers()) {
            if (nickname.equals(playerV.getNickname())) {
                playerV.setNumDeck(deck);
                return;
            }
        }
        if (console.getNickname().equals(nickname) && deck != -1) {
            console.getPrinter().printDeck(deck);
        }

    }

    //todo in base a come scegliamo di fare la current card questa potrebbe dover essere rivista
    @Override
    public void showCurrentCardUpdate(String imagepath, StateCardType stateCard, CardType cardType, String comment) {
        CardV card = new CardV(stateCard, imagepath, cardType, comment);
        gameV.getCurrentState().setCurrentCard(card);
        printOnConsole();
    }

    @Override
    public void showCurrentTileUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname) {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                for (TileV tileV : gameV.getHeapTilesV().getListTileV()) {
                    if (tileV.getImagePath().equals(imagepath)) {
                        playerv.setCurrentTile(Optional.of(tileV));
                    }
                } //se non trovo la tile nell'heap tile la devo creare
                TileV tileV = new TileV(tType, connectors, rotationType, true, imagepath, maxBattery, maxBox);
                playerv.setCurrentTile(Optional.of(tileV));
            }
        }
        if (console.getNickname().equals(nickname)){
            printOnConsole();
        }
    }

    @Override
    public void showCurrentTileNullityUpdate(String nickname) {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname) && playerv.getCurrentTile().isPresent()) {
                playerv.setCurrentTile(Optional.empty());
                if (console.getNickname().equals(nickname)){
                    printOnConsole();
                }
            }
        }
    }

    //una tile viene aggiunta ad una nave, potrebbe essere che è nel set di tile oppure che devo crearla
    @Override
    public void showTileAdditionUpdate(String imagepath, ConnectorType[] connectors, RotationType rotationType, TileType tType, int maxBattery, int maxBox, String nickname, Coordinate coordinate) {
        for (PlayerV playerv : gameV.getPlayers()) {
            for (TileV tileV : gameV.getHeapTilesV().getListTileV()) {
                if (tileV.getImagePath().equals(imagepath)) {
                    gameV.getHeapTilesV().removeFromHeapTile(tileV);
                    if (playerv.getNickname().equals(nickname) ) {
                        tileV.setRotationType(rotationType);
                        if(tType == TileType.BATTERY) tileV.setNumBattery(maxBattery);
                        playerv.setSpaceshipBoardTile(tileV, coordinate);
                    }
                }
            } //se non trovo la tile nell'heap tile la devo creare
            if (playerv.getNickname().equals(nickname)) {
                TileV tileV = new TileV(tType, connectors, rotationType, true, imagepath, maxBattery, maxBox);
                if(tType == TileType.BATTERY) tileV.setNumBattery(maxBattery);
                playerv.setSpaceshipBoardTile(tileV, coordinate);
            }

        }
    }

    @Override
    public void showUpdateEverything(int level, HashMap<String, PlayerColor> playercolors, String currentCardImage, StateCardType stateCard, CardType cardType, String currentComment, StateGameType stateGame, String currentPlayer, boolean[][] mask, int[] startingpositions, HashMap<Integer, List<List<Object>>> deck) throws RemoteException {
        ArrayList<PlayerV> players = new ArrayList<>();
        CardV currentCardV = new CardV(stateCard, currentCardImage, cardType, currentComment);
        PlayerV currentPlayerV = null;

        for (String p : playercolors.keySet()) {
            PlayerV playerV = new PlayerV(p, playercolors.get(p), mask);
            if (p.equals(currentPlayer)) currentPlayerV = playerV;
            players.add(playerV);
        }

        HashMap<Integer, List<CardV>> deckCardV = new HashMap<>();
        for (Map.Entry<Integer, List<List<Object>>> entry : deck.entrySet()) {
            Integer key = entry.getKey();
            List<List<Object>> cardDataList = entry.getValue();

            List<CardV> cardVList = new ArrayList<>();

            for (List<Object> cardData : cardDataList) {
                StateCardType stateCardV = (StateCardType) cardData.get(0);
                String imagePath = (String) cardData.get(1);
                String comment = (String) cardData.get(2);
                CardType cardTypeV = (CardType) cardData.get(3);

                // Corretto l'ordine per il costruttore
                CardV cardV = new CardV(stateCardV, imagePath, cardTypeV, comment);
                cardVList.add(cardV);
            }

            deckCardV.put(key, cardVList);
        }
        CardDeckV cardDeckV = new CardDeckV(deckCardV);

        StateV stateV = new StateV(currentCardV, currentPlayerV, stateGame);
        GameboardV gameboardV = new GameboardV(startingpositions, level, players);
        GameV game = new GameV(players, level, gameboardV, stateV, false, console, cardDeckV);

        setGameV(game);
        console.getPrinter().setGame(gameV);
        console.getController().setGameV(game);
        console.startCountdown();
        printOnConsole();
    }

    @Override
    public void showGameStateUpdate(StateGameType newGamestate) throws RemoteException {
        gameV.getCurrentState().setPhase(newGamestate);
        printOnConsole();
    }

    @Override
    public void showPlayerStateUpdate(String nickname, StatePlayerType newPlayerstate) throws RemoteException {
        for (PlayerV playerV : gameV.getPlayers()) {
            if (playerV.getNickname().equals(nickname)) {
                playerV.setStatePlayer(newPlayerstate);
            }
        }
    }

    @Override
    public void showCardStateUpdate(StateCardType newCardstate) throws RemoteException {
        gameV.getCurrentCard().setStateCard(newCardstate);
        printOnConsole();
    }

    @Override
    public void showCurrentPlayerUpdate(String nickname) throws RemoteException {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                gameV.getCurrentState().setCurrentPlayer(playerv);
            }
        }
        printOnConsole();
    }

    @Override
    public void showBookTileUpdate(String nickname) throws RemoteException {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if (playerv.getBookedTiles().get(1) == null) {
                    playerv.setBookedTiles(1, playerv.getCurrentTile().get());
                } else if (playerv.getBookedTiles().get(2) == null) {
                    playerv.setBookedTiles(2, playerv.getCurrentTile().get());
                }
            }

        }
    }

    @Override
    public void showAddCrewUpdate(String nickname, Coordinate pos, AliveType type, int num) throws RemoteException {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                if (type.equals(AliveType.BROWN_ALIEN)) {
                    playerv.getSpaceshipBoard()[pos.x()][pos.y()].get().setNumBAliens(num);
                } else if (type.equals(AliveType.PURPLE_ALIEN)) {
                    playerv.getSpaceshipBoard()[pos.x()][pos.y()].get().setNumPAliens(num);
                } else {
                    playerv.getSpaceshipBoard()[pos.x()][pos.y()].get().setNumHumans(num);
                }
            }
        }
        if(console.getNickname().equals(nickname)){
            printOnConsole();
        }
    }

    @Override
    public void showBookedTileNullityUpdate(String nickname, int index, Coordinate pos) throws RemoteException {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                playerv.setSpaceshipBoardTile(playerv.getBookedTiles().get(index), pos);
                playerv.getBookedTiles().put(index, null);
            }
        }
        if(console.getNickname().equals(nickname)){
           printOnConsole();
        }
    }

    @Override
    public void showEarlyLandingUpdate(String nickname) throws RemoteException {
        for (PlayerV playerv : gameV.getPlayers()) {
            if (playerv.getNickname().equals(nickname)) {
                gameV.getGlobalBoard().getPositions().remove(playerv);
            }
        }
    }

    @Override
    public void showBuildTimeIsOverUpdate() throws RemoteException {
        gameV.setBuildTimeIsOver(true);
    }

    private void printOnConsole() {
        console.getPrinter().print();
    }
}
