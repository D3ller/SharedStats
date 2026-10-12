<div align="center">
<h1>SharedStats</h1>
<p>Share your health and food level with the players in your group.</p>
</div>

## Commands

| Command | Description | Permissions |
| --- | --- | --- |
| `/sharedgroup help` | List the available commands | Player |
| `/sharedgroup create` | Create a shared stats group | OP |
| `/sharedgroup invite [player]` | Add a player to your group | OP, group owner |
| `/sharedgroup remove [player]` | Remove a player from your group | OP, group owner |
| `/sharedgroup revoke` | Delete the group and remove all its players | OP, group owner |
| `/sharedgroup start` | Start the game and stat synchronization | OP, group owner |
| `/sharedgroup stop` | Stop an active game without deleting the group | Group owner |
| `/sharedgroup players` | List the online players in your group | Group member |
| `/sharedgroup version` | Display the plugin version | None |

## What remains to be done

- Synchronize players' food levels and saturation.
- Add automated tests for the group lifecycle and stat synchronization.

Made for Minecraft 26.2 with PaperMC.

## License

SharedStats is available under the [MIT License](LICENSE).
