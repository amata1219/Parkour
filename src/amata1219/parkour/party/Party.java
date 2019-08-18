package amata1219.parkour.party;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party {

	public UUID owner;
	public final Set<UUID> members = new HashSet<>();

}
