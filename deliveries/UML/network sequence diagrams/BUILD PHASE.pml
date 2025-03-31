@startuml
group "Example of Build Method [ok]"
    ClientA -> Server : takeTile(Player player)
    rnote over Server #lightgrey : ServerController.getGame().takeTile(Player player)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
end

group "Example of Build Method [failed due to invalid state of the game]"
    ClientA -> Server : playNextCard(Player player)
    rnote over Server #lightgrey : ServerController.getGame().playNextCard(Player player)
    Server -> ClientA : {status: "failed"}, message: "invalid state of the game"}
end

group "Example of Build Method [failed due to the player is not found in the game]"
    ClientA -> Server : playNextCard(Player player)
    Server -> ClientA : {status: "failed"}, message: "player not found in the game"}
end
@enduml