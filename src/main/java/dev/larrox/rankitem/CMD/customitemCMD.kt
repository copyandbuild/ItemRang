package dev.larrox.rankitem.CMD

import dev.larrox.rankitem.RankItem
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class customitemCMD(private val plugin: RankItem) : CommandExecutor, Listener {
    private val guiname = "§c§kmm §7RangItem §c§kmm"
    private var rankItem: ItemStack? = null
    private var rankName: String? = null
    private var commandToExecute: String? = null

    init {
        loadConfig()
    }

    fun loadConfig() {
        val config = plugin.config
        val materialStr = config.getString("rankitem.material")
        val itemNameStr = config.getString("rankitem.name")
        val rankStr = config.getString("rankitem.rank")
        val commandStr = config.getString("rankitem.command")

        if (materialStr != null && itemNameStr != null && rankStr != null && commandStr != null) {
            val material = Material.matchMaterial(materialStr)
            if (material != null) {
                rankItem = createItem(material, ChatColor.translateAlternateColorCodes('&', itemNameStr))
                rankName = rankStr
                commandToExecute = commandStr
            } else {
                plugin.logger.warning("Invalid material '${materialStr}' for rank item in config!")
            }
        } else {
            plugin.logger.warning("Missing required configuration values for rank item!")
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§7Gui wird geöffnet..."))
            ItemGUI(player)
        }
        return true
    }

    private fun ItemGUI(player: Player) {
        val gui = Bukkit.createInventory(null, 9, guiname)
        rankItem?.let { gui.setItem(4, it) }
        player.openInventory(gui)
    }

    private fun createItem(material: Material, name: String): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        if (meta != null) {
            meta.setDisplayName(name)
            item.itemMeta = meta
        }
        return item
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val view = event.view
        if (view.title != guiname) return

        val player = event.whoClicked as? Player ?: return
        event.isCancelled = false
        val currentItem = event.currentItem
        if (currentItem != null && currentItem.isSimilar(rankItem)) {
            player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent("§7Du hast das §e$rankName §7Rang Item erhalten.")
            )
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item
        if (item != null && item.isSimilar(rankItem)) {
            player.spigot()
                .sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§7Du hast den §e$rankName §7Rang erhalten."))
            executeCommand(player)
            item.amount = item.amount - 1
        }
    }

    private fun executeCommand(player: Player) {
        val command = commandToExecute?.replace("%player%", player.name)
        if (!command.isNullOrBlank()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
        }
    }
}
