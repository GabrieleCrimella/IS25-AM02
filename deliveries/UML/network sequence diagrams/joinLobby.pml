@startuml

note right of ClientA
    After adding the ClientA to that lobby,
    the game doesn't start yet because the lobby is not full.
end note
group "Join Lobby (a player wants to take part to a game) [ok]"
    ClientA -> Server : joinLobby(int lobbyId, String nickname, PlayerColor color)
    rnote over Server #lightgrey : ServerController.joinLobby(int lobbyId, String nickname, PlayerColor color)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
end

note right of ClientA
    After adding the ClientA to that lobby,
    the game starts because the lobby is full.
end note
group "Join Lobby (a player wants to take part to a game) [ok]"
    ClientA -> Server : joinLobby(int lobbyId, String nickname, PlayerColor color)
    rnote over Server #lightgrey : ServerController.joinLobby(int lobbyId, String nickname, PlayerColor color)
    rnote over Server #lightgrey : ServerController.startGame(lobby)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
end

note right of ClientA
    The lobby can't be fount because the lobbyId is wrong or invalid.
end note
group "Join Lobby (a player wants to take part to a game) [failed due to lobby not found]"
    ClientA -> Server : joinLobby(int lobbyId, String nickname, PlayerColor color)
    rnote over Server #lightgrey : ServerController.joinLobby(int lobbyId, String nickname, PlayerColor color)
    Server -> ClientA : {status: "failed"}, message: "lobby not found"}, signalError
end
@enduml