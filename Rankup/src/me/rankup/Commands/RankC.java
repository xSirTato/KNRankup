package me.rankup.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.rankup.KNRankup;
import me.rankup.Configs.Players.Players;
import me.rankup.Messages.Messages;
import me.rankup.Utility.Rank;
import simple.brainsynder.api.ParticleMaker.Particle;

public class RankC implements CommandExecutor {

	private Messages msgs = new Messages();

	public RankC() {
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			return true;
		}
		Player p = (Player) sender;
		String name = p.getName().toLowerCase();
		////////////////////////////////////////
		if (args.length == 0) {
			if (!p.hasPermission("rankup.rank")) {
				KNRankup.getAPI().sendParticle(p, Particle.FLAME);
				KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
						this.msgs.get(p, "Messages.NoPerms"));
				return false;
			}
			String cr = new Players().getCRank(p);
			//////////////////////////////////////
			if (cr == null) {
				p.sendMessage(this.msgs.get(p, "Messages.RankNotFound"));
				return false;
			}
			Rank curentRank = new Rank(cr);
			///////////////////////////////
			if (!curentRank.exists()) {
				Rank.executeRankup(p, curentRank.getDefRank());
				new Players("Players." + name + ".Rank", curentRank.getDefRank()).set();
				p.sendMessage(this.msgs.get(p, "Messages.RankNotFound"));
				return false;
			}
			KNRankup.getAPI().sendParticle(p, Particle.VILLAGER_HAPPY);
			KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
					this.msgs.get(p, "Messages.CurentRank").replace("%rank%", curentRank.getPrefix()));
			return true;
		}
		if (args.length == 1) {
			if (!p.hasPermission("rankup.rank.others")) {
				KNRankup.getAPI().sendParticle(p, Particle.FLAME);
				KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
						this.msgs.get(p, "Messages.NoPerms"));
				return false;
			}
			String t = args[0];
			///////////////////
			if (t == null) {
				return false;
			}
			if (t.isEmpty()) {
				return false;
			}
			Player target = KNRankup.s.getPlayer(t);
			//////////////////////////////////////////////
			if (target == null) {
				KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
						this.msgs.get(target, "Messages.TargetNotFound").replace("%target%", t));
				return false;
			}
			if (!target.isOnline()) {
				KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
						this.msgs.get(target, "Messages.TargetNotFound").replace("%target%", t));
				return false;
			}
			String cr = new Players().getCRank(target);
			String tname = target.getName().toLowerCase();
			//////////////////////////////////////////////
			if (cr == null) {
				target.sendMessage(this.msgs.get(target, "Messages.RankNotFound"));
				p.sendMessage(
						this.msgs.get(p, "Messages.RankNotFoundTarget").replace("%target%", target.getDisplayName()));
				return false;
			}
			Rank curentRank = new Rank(cr);
			///////////////////////////////
			if (!curentRank.exists()) {
				Rank.executeRankup(target, curentRank.getDefRank());
				new Players("Players." + tname + ".Rank", curentRank.getDefRank()).set();
				target.sendMessage(this.msgs.get(target, "Messages.RankNotFound"));
				p.sendMessage(
						this.msgs.get(p, "Messages.RankNotFoundTarget").replace("%target%", target.getDisplayName()));
				return false;
			}
			KNRankup.getAPI().sendParticle(p, Particle.VILLAGER_HAPPY);
			KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
					this.msgs.get(p, "Messages.CurentRankTarget").replace("%rank%", curentRank.getPrefix())
							.replace("%target%", target.getDisplayName()));
			return true;
		}
		if (args.length == 2) {
			if (!p.hasPermission("rankup.rank.set")) {
				KNRankup.getAPI().sendParticle(p, Particle.FLAME);
				KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
						this.msgs.get(p, "Messages.NoPerms"));
				return false;
			}
			String t = args[0];
			String rank = args[1];
			//////////////////////
			if (t == null) {
				return false;
			}
			if (t.isEmpty()) {
				return false;
			}
			if (rank == null) {
				return false;
			}
			if (rank.isEmpty()) {
				return false;
			}
			Player target = KNRankup.s.getPlayer(t);
			//////////////////////////////////////////////
			if (target == null) {
				KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
						this.msgs.get(target, "Messages.TargetNotFound").replace("%target%", t));
				return false;
			}
			if (!target.isOnline()) {
				KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
						this.msgs.get(target, "Messages.TargetNotFound").replace("%target%", t));
				return false;
			}
			Rank r = new Rank(rank);
			String tname = target.getName().toLowerCase();
			//////////////////////////////////////////////
			if (!r.exists()) {
				p.sendMessage(this.msgs.get(p, "Messages.RankNotFoundSet").replace("%rank%", rank));
				return false;
			}
			Rank.executeRankup(target, rank);
			new Players("Players." + tname + ".Rank", rank).set();
			KNRankup.getAPI().sendParticle(p, Particle.CLOUD);
			KNRankup.getAPI().sendParticle(target, Particle.VILLAGER_HAPPY);
			KNRankup.getAPI().sendTitle(target, this.msgs.get(p, "Messages.Title"),
					this.msgs.get(target, "Messages.CurentRankChanged").replace("%rank%", r.getPrefix()));
			KNRankup.getAPI().sendTitle(p, this.msgs.get(p, "Messages.Title"),
					this.msgs.get(p, "Messages.CurentRankSet").replace("%rank%", r.getPrefix()).replace("%target%",
							target.getDisplayName()));
			return true;
		}
		return false;
	}
}