package it.polimi.ingsw.is25am02.controller.client;

import it.polimi.ingsw.is25am02.utils.Coordinate;
import it.polimi.ingsw.is25am02.utils.enumerations.AliveType;
import it.polimi.ingsw.is25am02.utils.enumerations.BoxType;
import it.polimi.ingsw.is25am02.utils.enumerations.PlayerColor;
import it.polimi.ingsw.is25am02.utils.enumerations.RotationType;
import it.polimi.ingsw.is25am02.model.tiles.Tile;
import it.polimi.ingsw.is25am02.network.ConnectionClient;
import it.polimi.ingsw.is25am02.network.VirtualServer;
import it.polimi.ingsw.is25am02.network.VirtualView;
import it.polimi.ingsw.is25am02.view.modelDuplicateView.GameV;

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

    public ClientController(ConnectionClient connection) {
        this.connection = connection;
        this.menuState = MenuState.LOGIN;
        this.running = true;
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
        if(menuControl(MenuState.GAME)) {
            connection.getServer().flipHourglass(nickname);
        }
    }

    public void takeTile(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().takeTile(nickname);
        }
    }

    public void takeTile(String nickname, Tile tile) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().takeTile(nickname, tile);
        }
    }

    public void takeMiniDeck(String nickname, int index) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().takeMiniDeck(nickname, index);
        }
    }

    public void returnMiniDeck(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().returnMiniDeck(nickname);
        }
    }

    public void bookTile(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().bookTile(nickname);
        }
    }

    public void addBookedTile(String nickname, int index, Coordinate pos, RotationType rotation) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().addBookedTile(nickname, index, pos, rotation);
        }
    }

    public void returnTile(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().returnTile(nickname);
        }
    }

    public void addTile(String nickname, Coordinate pos, RotationType rotation) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().addTile(nickname, pos, rotation);
        }
    }

    public void shipFinished(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().shipFinished(nickname);
        }
    }

    public void checkSpaceship(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().checkSpaceship(nickname);
        }
    }

    public void removeTile(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().removeTile(nickname, pos);
        }
    }

    public void checkWrongSpaceship(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().checkWrongSpaceship(nickname);
        }
    }

    public void addCrew(String nickname, Coordinate pos, AliveType type) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().addCrew(nickname, pos, type);
        }
    }

    public void ready(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().ready(nickname);
        }
    }

    public void playNextCard(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().playNextCard(nickname);
        }
    }

    public void earlyLanding(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().earlyLanding(nickname);
        }
    }

    public void choice(String nickname, boolean choice) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().choice(nickname, choice);
        }
    }

    public void removeCrew(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().removeCrew(nickname, pos);
        }
    }

    public void choiceBox(String nickname, boolean choice) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().choiceBox(nickname, choice);
        }
    }

    public void moveBox(String nickname, Coordinate start, Coordinate end, BoxType boxType, boolean on) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().moveBox(nickname, start, end, boxType, on);
        }
    }

    public void choicePlanet(String nickname, int index) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().choicePlanet(nickname, index);
        }
    }

    public void choiceDoubleMotor(String nickname, List<Coordinate> motors, List<Coordinate> batteries) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().choiceDoubleMotor(nickname, motors, batteries);
        }
    }

    public void choiceDoubleCannon(String nickname, List<Coordinate> cannons, List<Coordinate> batteries) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().choiceDoubleCannon(nickname, cannons, batteries);
        }
    }

    public void choiceCrew(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().choiceCrew(nickname);
        }
    }

    public void removeBox(String nickname, Coordinate pos, BoxType type) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().removeBox(nickname, pos, type);
        }
    }

    public void removeBattery(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().removeBattery(nickname, pos);
        }
    }

    public void rollDice(String nickname) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().rollDice(nickname);
        }
    }

    public void calculateDamage(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
            connection.getServer().calculateDamage(nickname, pos);
        }
    }

    public void keepBlock(String nickname, Coordinate pos) throws RemoteException {
        if(menuControl(MenuState.GAME)) {
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
