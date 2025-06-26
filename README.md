# Galaxy Trucker - Java Implementation

## Project Overview

This project is a comprehensive Java-based implementation of the popular board game Galaxy Trucker, developed at Politecnico di Milano for the final exam of Software Engineering (Ingegneria del Software).

Galaxy Trucker is a strategic board game where players build spaceships from tiles and then fly them through dangerous space, facing various challenges like meteors, pirates, and equipment failures. This digital implementation faithfully recreates the complete game experience with both network multiplayer support and multiple user interface options.

### Team Members
- Gabriele Crimella
- Anna Cordioli
- Erica Donno
- Davide Faccini

## Features

The implementation includes:
- **Complete game rules** with all original mechanics
- **Network multiplayer support** via both RMI and Socket connections
- **Dual interface options**: Textual User Interface (TUI) and Graphical User Interface (GUI)
- **Two additional functionalities**: Test Flight mode and Multiple Games support
- **Cross-platform compatibility** through Java

## How to Run

The project consists of two main JAR files:
- `IS25-AM02-1.0-server.jar` - Game Server
- `IS25-AM02-1.0-client.jar` - Game Client

### Starting the Server

```bash
java -jar IS25-AM02-1.0-server.jar
```

### Starting the Client

```bash
java -jar IS25-AM02-1.0-client.jar [network-protocol] [interface-type] [server-ip]
```

**Parameters:**
- **network-protocol**: Connection type
    - `rmi` - RMI (Remote Method Invocation) connection
    - `socket` - TCP Socket connection
- **interface-type**: User interface mode
    - `tui` - Textual User Interface (command-line based)
    - `gui` - Graphical User Interface (JavaFX based)
- **server-ip**: IP address of the game server

**Example usage:**
```bash
# Connect with GUI via RMI to localhost
java -jar IS25-AM02-1.0-client.jar rmi gui 127.0.0.1

# Connect with TUI via Socket to remote server
java -jar IS25-AM02-1.0-client.jar socket tui 192.168.1.100

# Connect with GUI via Socket to localhost
java -jar IS25-AM02-1.0-client.jar socket gui localhost
```

## Network Configuration

The application supports two network protocols:

### RMI (Remote Method Invocation)
- **Parameter**: `rmi`
- Java's built-in distributed computing mechanism
- Suitable for Java-to-Java communication

### Socket Connection
- **Parameter**: `socket`
- TCP-based communication
- More universal and firewall-friendly

## User Interface Options

### Textual User Interface (TUI)
- **Parameter**: `tui`
- Command-line based interface
- Lightweight and suitable for headless environments
- Full game functionality through text commands

### Graphical User Interface (GUI)
- **Parameter**: `gui`
- JavaFX-based graphical interface
- Intuitive visual gameplay
- Enhanced user experience with animations and visual feedback

## Implementation Status

| Functionality     | Status |
|-------------------|--------|
| Basic rules       | ✅     |
| Complete rules    | ✅     |
| Socket connection | ✅     |
| RMI connection    | ✅     |
| TUI               | ✅     |
| GUI               | ✅     |
| Multiple games    | ✅     |
| Test flight       | ✅     |
| Persistence       | ⬜     |
| Resilience        | ⬜     |

**Legend:**
- ✅ = Completed
- 🟧 = In progress
- ⬜ = Not yet implemented

## Additional Features

Beyond the core game implementation, we have successfully developed:

1. **Complete Rules Implementation** - Full faithful recreation of the original Galaxy Trucker board game mechanics
2. **Dual Interface Support** - Both TUI and GUI options for different user preferences
3. **Network Connectivity** - Both RMI and Socket-based multiplayer support
4. **Test Flight Mode** - Practice mode for players to familiarize themselves with ship building
5. **Multiple Games Support** - Ability to host and manage multiple concurrent game sessions

## Requirements

- Java Runtime Environment (JRE) 11 or higher
- Network connectivity for multiplayer gameplay
- For GUI mode: JavaFX-compatible system

## Development

This project demonstrates advanced software engineering concepts including:
- Network programming (RMI and Socket)
- GUI development with JavaFX
- Model-View-Controller (MVC) architecture
- Multithreading and concurrent programming
- Object-oriented design patterns
- Client-server architecture