<div align="center">
<h1>SharedStats</h1>
<p>Share your health and food level with the players in your group.</p>
<p>
  <a href="https://www.curseforge.com/minecraft/bukkit-plugins/sharedstats/preview"><img src="https://wsrv.nl/?url=https%3A%2F%2Fcdn.jsdelivr.net%2Fnpm%2F%40intergrav%2Fdevins-badges%403%2Fassets%2Fcozy%2Favailable%2Fcurseforge_vector.svg&amp;n=-1" alt="Available on CurseForge"></a>
  <a href="https://modrinth.com/plugin/sharedstats"><img src="https://wsrv.nl/?url=https%3A%2F%2Fcdn.jsdelivr.net%2Fnpm%2F%40intergrav%2Fdevins-badges%403%2Fassets%2Fcozy%2Favailable%2Fmodrinth_vector.svg&amp;n=-1" alt="Available on Modrinth"></a>
</p>
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
| `/sharedgroup leaderboard` | Show the group's death leaderboard during a game | Group member |
| `/sharedgroup enable <health\|food\|potions>` | Enable synchronization for a stat, outside a game | OP |
| `/sharedgroup disable <health\|food\|potions>` | Disable synchronization for a stat, outside a game | OP |
| `/sharedgroup configuration` | Open the synchronization settings menu, outside a game | OP, group owner |
| `/sharedgroup version` | Display the plugin version | None |

## Food synchronization

The food level is shared across the group, but saturation is not: each player keeps their own saturation. This is a deliberate choice. Every player's hunger drains the shared food bar, so food goes down much faster than in solo play.

## What remains to be done

- Add automated tests for the group lifecycle and stat synchronization.

Made for Minecraft 26.2 with PaperMC.

## License

SharedStats is available under the [MIT License](LICENSE).
