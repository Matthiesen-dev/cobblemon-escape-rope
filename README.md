# Cobblemon Escape Rope

A lightweight Cobblemon add-on that adds an **Escape Rope** item for returning to the last safe place where you were under open sky.

## What it does

- Adds a custom `Escape Rope` item.
- Records each player's last position in a skylit dimension while they can see the sky.
- On use, teleports the player back to that saved location (same dimension only).
- Applies a configurable use time, cooldown, and item consumption behavior.
- Tries to find a safe nearby landing spot using a configurable search radius.

## Requirements

- [Cobblemon 1.7.3+1.21.1](https://modrinth.com/mod/cobblemon)
- [Architectury API for 1.21.1](https://modrinth.com/mod/architectury-api)

## Usage

- Get the item from the creative tab **Cobblemon Escape Rope**, craft it in survival, or with:
  - `/give @p cobblemon_escape_rope:escape_rope`
- Hold right-click to charge the rope.
- If you are underground/in a valid dimension, you are teleported to your last recorded open-sky position.

### Crafting

Shaped recipe (3x3):

```text
S S S
S E S
S S S
```

- `S` = `minecraft:string`
- `E` = `minecraft:ender_pearl`
- Output: `cobblemon_escape_rope:escape_rope` x1

### Teleport behavior

- Works only if your current dimension matches the saved dimension.
- If you are already under open sky, the rope cancels and notifies you.
- If no safe landing position is found near the saved location, teleport is canceled.
- If your current dimension is in `blacklistedDimensions`, the rope cannot be used.

## Configuration

A config file is generated automatically at:

- `config/cobblemon_escape_rope/config.json`

Default values:

```json
{
  "serverSaveTicks": 20,
  "escapeRopeItem": {
    "cooldownInSeconds": 300,
    "consumeOnUse": true,
    "useTimeInSeconds": 3,
    "teleportSafeSearchRadius": 2,
    "blacklistedDimensions": []
  }
}
```

### Top-level settings:

- `serverSaveTicks`: How often (in ticks) to save player positions to disk. Default: 20 (1 second).

### `escapeRopeItem` settings:

- `cooldownInSeconds`: Cooldown duration after using the rope. Default: 300 (5 minutes).
- `consumeOnUse`: Whether to consume the rope item on use. Default: true.
- `useTimeInSeconds`: Time required to use the rope (hold right-click). Default: 3 seconds.
- `teleportSafeSearchRadius`: Radius to search for a safe landing spot around the saved position. Default: 2 blocks or 5x5 area.
- `blacklistedDimensions` accepts dimension IDs (for example: `minecraft:the_nether`).

Sample:

```json
{
  "escapeRopeItem": {
    "blacklistedDimensions": [
      "minecraft:the_nether",
      "minecraft:the_end"
    ]
  }
}
```

When a dimension is blacklisted:

- Escape Rope use is blocked in that dimension.
- Open-sky return positions are not updated while you are in that dimension.
