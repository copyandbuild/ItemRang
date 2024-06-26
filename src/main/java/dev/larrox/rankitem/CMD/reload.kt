package dev.larrox.rankitem.CMD

import dev.larrox.rankitem.RankItem
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class reload(private val plugin: RankItem) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        plugin.reloadConfig()
        plugin.customItemCMD?.loadConfig()
        sender.sendMessage("§econfig.yml §7neugeladen...")
        return true
    }
}
