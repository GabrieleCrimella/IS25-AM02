@startuml
group "Example of Card Method [ok]"
    ClientA -> Server : choiceBox(Player player, boolean choice)
    rnote over Server #lightgrey : ServerController.getGame().choiceBox(Player player, boolean choice)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
end

group "Example of Card Method [failed due to invalid state of the game]"
    ClientA -> Server : choiceBox(Player player, boolean choice)
    rnote over Server #lightgrey : ServerController.getGame().choiceBox(Player player, boolean choice)
    Server -> ClientA : {status: "failed"}, message: "invalid state of the game"}
end

group "Example of Card Method [failed due to the player is not found in the game]"
    ClientA -> Server : choiceBox(Player player, boolean choice)
    Server -> ClientA : {status: "failed"}, message: "player not found in the game"}
end

group "Example of Card Method [failed due to the player is not the current one]"
    ClientA -> Server : choiceBox(Player player, boolean choice)
    rnote over Server #lightgrey : ServerController.getGame().choiceBox(Player player, boolean choice)
    Server -> ClientA : {status: "failed"}, message: "player is not the current one"}
end
@enduml