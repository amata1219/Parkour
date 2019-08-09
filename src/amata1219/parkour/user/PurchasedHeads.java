package amata1219.parkour.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.head.Head;

public class PurchasedHeads {

	private final User user;
	private Set<UUID> heads = new HashSet<>();

	public PurchasedHeads(User user, Yaml yaml){
		this.user = user;

		heads = yaml.getStringList("Purchased heads").stream().map(UUID::fromString).collect(Collectors.toSet());
	}

	public boolean has(Head head){
		return heads.contains(head.uuid);
	}

	public boolean canBuy(Head head){
		return head.value <= user.getCoins();
	}

	public void buy(Head head){
		user.withdrawCoins(head.value);
		heads.add(head.uuid);
	}

	public void save(Yaml yaml){
		yaml.set("Purchased heads", heads.stream().map(UUID::toString).collect(Collectors.toList()));
	}

}
