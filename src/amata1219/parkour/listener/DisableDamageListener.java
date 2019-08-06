package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DisableDamageListener implements Listener {

	@EventHandler
	public void disableBlockDamage(EntityDamageEvent event){
		//ダメージを受けたエンティティがプレイヤーでなければ戻る
		if(!(event.getEntity() instanceof Player)) return;

		switch(event.getCause()){
		case DROWNING:
		case ENTITY_ATTACK:
		case FALL:
		case FIRE:
		case FIRE_TICK:
		case LAVA:
		case STARVATION:
			event.setCancelled(true);
			return;
		case CONTACT:
		case HOT_FLOOR:
			event.setDamage(0);
			return;
		default:
			return;
		}
	}

}
