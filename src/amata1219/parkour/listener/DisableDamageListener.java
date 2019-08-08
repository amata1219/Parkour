package amata1219.parkour.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DisableDamageListener implements Listener {

	@EventHandler
	public void disableBlockDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		//ダメージを受けたエンティティがプレイヤーでなければ戻る
		if(!(entity instanceof Player)) return;

		switch(event.getCause()){
		case DROWNING:
		case ENTITY_ATTACK:
		case FALL:
		case STARVATION:
			event.setCancelled(true);
			return;
		case FIRE:
		case FIRE_TICK:
		case LAVA:
			entity.setFireTicks(0);
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

	@EventHandler
	public void disableFireEffect(EntityCombustEvent event){
		event.setCancelled(true);
	}

}
