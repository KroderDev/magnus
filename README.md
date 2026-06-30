# Magnus (Server-Side)

![Build Status](https://img.shields.io/github/actions/workflow/status/KroderDev/magnus/build.yml?branch=main&style=flat-square&label=Build)
![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1--1.21.11-brown?style=flat-square&logo=minecraft)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue?style=flat-square&logo=fabric)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?style=flat-square&logo=kotlin)
![GitHub Release](https://img.shields.io/github/v/release/KroderDev/magnus?style=flat-square&label=Release&color=green)
[![Modrinth Version](https://img.shields.io/modrinth/v/magnus?style=flat-square&logo=modrinth&color=00AF5C&label=Modrinth)](https://modrinth.com/mod/magnus)
[![CurseForge Version](https://img.shields.io/curseforge/v/1448049?style=flat-square&logo=curseforge&color=F16436&label=CurseForge)](https://www.curseforge.com/minecraft/mc-mods/magnus)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)
![Trivy](https://img.shields.io/badge/security-Trivy-1904DA?style=flat-square&logo=trivy)
![License](https://img.shields.io/badge/License-LGPL_v3-blue?style=flat-square)


A modular, high-performance, and resilient synchronization suite for Minecraft Fabric servers. Features real-time inventory sync, cross-server chat, and global player management.

## Features

### Core Sync
*   **Multi-Server Sync**: Syncs Inventory, Ender Chest, Health, Hunger, XP, Potion Effects.
*   **High Performance**: Uses **Redis** for sub-millisecond data retrieval.
*   **Persistence**: Uses **PostgreSQL** for permanent reliable storage.
*   **Fault Tolerance**:
    *   **Circuit Breaker**: Detects database failures automatically.
    *   **Local Fallback**: Saves data to disk if the database goes down.
    *   **Auto-Recovery**: "The Janitor" service automatically restores data when the database comes back online.

### Modules

| Module | Description |
|--------|-------------|
| **Inventory Sync** | Core synchronization of inventory, ender chest, health, hunger, XP, and potion effects. **Enabled by default.** |
| **Global Chat** | Syncs chat messages across all servers via Redis pub/sub. Raw text format for compatibility with LuckPerms, Stylist, etc. |
| **Global Player List** | Maintains a global player count with heartbeat. Provides `/glist` command. |
| **Session Lock** | Prevents concurrent logins by locking player sessions during sync. Players are held in queue until their data is safely transferred. |

### Security

| Feature | Default | Description |
|---------|---------|-------------|
| **Message Signing** | ✅ Enabled | HMAC-SHA256 prevents message injection attacks |
| **Auto-Generated Secret** | ✅ | 32-byte cryptographic secret generated on first run |
| **Replay Prevention** | 30 sec | Messages older than 30 seconds are rejected |
| **Payload Size Limits** | 64KB | Prevents DoS via oversized messages |
| **SSL/TLS Support** | Optional | Encrypt Redis connections |

## Configuration

Magnus requires a **PostgreSQL** database and a **Redis** instance to function.

1.  **Generate Config**: Start your server once to generate the default configuration file at `config/magnus.json`.
2.  **Edit Config**: Stop the server and open `config/magnus.json`:

```json
{
    "postgresUrl": "jdbc:postgresql://your-db-host:5432/magnus",
    "postgresUser": "your_user",
    "postgresPass": "your_password",
    "redisHost": "your-redis-host",
    "redisPort": 6379,
    "redisPass": null,
    "serverName": "survival",
    "enableInventorySync": true,
    "enableGlobalChat": false,
    "enableGlobalPlayerList": false,
    "enableSessionLock": false,
    "enableMessageSigning": true,
    "messageSigningSecret": "auto-generated-on-first-run",
    "redisSsl": false
}
```

3.  **Launch**: Restart your server. Magnus will automatically verify the connection and create the necessary database schema.

> [!IMPORTANT]
> **Multi-Server Setup**: Copy the `messageSigningSecret` from the first server to all other servers. All servers must use the same secret.

### Configuration Options

| Option | Default | Description |
|--------|---------|-------------|
| `serverName` | `"default"` | Unique identifier for this server (e.g., `"survival"`, `"lobby"`) |
| `enableInventorySync` | `true` | Enable inventory/player data synchronization. Disable for lobby servers. |
| `enableGlobalChat` | `false` | Enable cross-server chat synchronization |
| `enableGlobalPlayerList` | `false` | Enable global player list and `/glist` command |
| `enableSessionLock` | `false` | Enable session locking to prevent concurrent logins |
| `enableMessageSigning` | `true` | Enable HMAC message signing (recommended) |
| `redisSsl` | `false` | Enable SSL/TLS for Redis connections |

## Commands

| Command | Description |
|---------|-------------|
| `/glist` | Shows total player count and players grouped by server (requires `enableGlobalPlayerList`) |

## Development

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

All development tasks run inside a Docker container. You don't need Java, Gradle, PostgreSQL, or Redis installed on your host.

### Commands

| Command | Description |
|---------|-------------|
| `make build` | Compile and package the mod |
| `make test` | Run all tests (unit + integration) |
| `make lint` | Run Detekt static analysis |
| `make shell` | Open a shell in the dev container |
| `make up` | Start PostgreSQL and Redis for manual testing |
| `make down` | Stop all services |

### First Run

```bash
make build
```

The first build downloads all dependencies (including Minecraft assets) and caches them in a persistent Docker volume. Subsequent builds are significantly faster.

### Running Tests

```bash
make test
```

Integration tests use [Testcontainers](https://testcontainers.com/) to spin up isolated PostgreSQL and Redis instances automatically.

### Code Quality

```bash
make lint
```

This project uses [Detekt](https://detekt.dev/) for Kotlin static analysis. Configuration is in `detekt.yml`.

## Architecture

For detailed flow diagrams and behavior documentation:
- [Core Sync Flow](docs/core_sync_flow.md) - Data synchronization and fault tolerance
- [Global Chat Flow](docs/global_chat_flow.md) - Cross-server chat messaging
- [Global Player List Flow](docs/global_playerlist_flow.md) - Player list heartbeat and `/glist`
- [Session Lock Flow](docs/session_lock_flow.md) - Login queue and session locking
- [Message Security Flow](docs/message_security_flow.md) - HMAC signing and replay prevention
- [Server Lifecycle Flow](docs/server_lifecycle_flow.md) - Startup and shutdown sequence
