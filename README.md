# Cobblemon Escape Rope

<div>
  <img src="https://mods.matthiesen.dev/badges/matthiesenLibAPI.svg" alt="Matthiesen Lib API">
  <img src="https://mods.matthiesen.dev/badges/matthiesenLib.svg" alt="Matthiesen Lib">
  <img src="https://mods.matthiesen.dev/badges/cobblemon.svg" alt="Cobblemon">
</div>

A lightweight Cobblemon add-on that adds an **Escape Rope** item for returning to the last safe place where you were under open sky.

## What it does

- Adds a custom `Escape Rope` item.
- Records each player's last position in a skylit dimension while they can see the sky.
- On use, teleports the player back to that saved location (same dimension only).
- Applies a configurable use time, cooldown, and item consumption behavior.
- Tries to find a safe nearby landing spot using a configurable search radius.

## Requirements

- [Cobblemon 1.7.3+1.21.1](https://modrinth.com/mod/cobblemon)
- [Matthiesen Lib API](https://modrinth.com/mod/matthiesen-lib-api)
- [Matthiesen Lib](https://modrinth.com/mod/matthiesen-lib)

## Docs

Documentation for this mod can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/cobblemon-escape-rope/)

## Version Compatibility

| Minecraft Version | Mod Version |
|-------------------|-------------|
| 1.21.1            | 1.x.x       |

## FastStats Metrics

This mod uses [FastStats](https://faststats.dev) to collect anonymous usage statistics. This helps the developer understand
how this mod is being used and improve it over time. You can learn more about the data collected and how it is used by visiting
[FastStats: Information](https://faststats.dev/info).

You can also view the data collected by this mod on the [FastStats: Cobblemon Escape Rope](https://faststats.dev/project/cobblemon-escape-rope) page.

To opt out of this data collection, set the `enabled` property to `false` in the `<game_directory>/config/matthiesen_lib_api/metrics.properties` file.

## License

MIT - see `LICENSE`.