@startuml

group "Create Lobby (the owner player wants to start a new game) [ok]"
    ClientA -> Server : createLobby(int maxPlayers, String nickname, PlayerColor color, int level)
    rnote over Server #lightgrey : ServerController.createLobby(int maxPlayers, String nickname, PlayerColor color, int level)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
    end
@enduml