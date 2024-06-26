package dev.larrox.rankitem

import dev.larrox.rankitem.CMD.customitemCMD
import dev.larrox.rankitem.CMD.reload
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class RankItem : JavaPlugin() {
    var customItemCMD: customitemCMD? = null
        private set

    override fun onLoad() {
        val console = Bukkit.getConsoleSender()
        console.sendMessage("${ChatColor.RED}[${ChatColor.GREEN}CustomItem${ChatColor.RED}] ${ChatColor.GOLD}Plugin loaded...")
    }

    override fun onEnable() {
        saveDefaultConfig()

        customItemCMD = customitemCMD(this)
        getCommand("rankitem")?.setExecutor(customItemCMD)
        server.pluginManager.registerEvents(customItemCMD!!, this)

        getCommand("reloadconfig")?.setExecutor(reload(this))

        val console = Bukkit.getConsoleSender()
        console.sendMessage("${ChatColor.RED}[${ChatColor.GREEN}CustomItem${ChatColor.RED}] ${ChatColor.GREEN}Plugin enabled, ready for use")
    }

    override fun onDisable() {
        val console = Bukkit.getConsoleSender()
        console.sendMessage("${ChatColor.RED}[${ChatColor.GREEN}CustomItem${ChatColor.RED}] ${ChatColor.RED}Plugin disabled...")
    }
}
