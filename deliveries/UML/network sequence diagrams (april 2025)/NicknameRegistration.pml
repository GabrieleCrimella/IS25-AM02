@startuml

group "Nickname registration [ok & failed due to nickname already exists]"
    ClientA -> Server : nicknameRegistration(playerName)
    rnote over Server #lightgrey : ServerController.nicknameRegistration(playerName)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
    ClientB -> Server : nicknameRegistration(playerName)
  rnote over Server #lightgrey : ServerController.nicknameRegistration(playerNam)
    Server -> ClientB : {status: "failed", message: "nickname already exists"}, signalError
end
note left of ClientB
    ClientB sent the same nickename that ClientA sent.
end note
@enduml