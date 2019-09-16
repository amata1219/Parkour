package amata1219.beta.parkour.util;

import static java.lang.Math.*;
import static org.bukkit.ChatColor.*;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;

import com.google.common.collect.ImmutableMap;

public class ApproximateChatColorFinder {

	private static final Map<ChatColor, Color> COLORS = ImmutableMap.<ChatColor, Color>builder()
			.put(BLACK, from(0, 0, 0))
			.put(DARK_BLUE, from(0, 0, 170))
			.put(DARK_GREEN, from(0, 170, 0))
			.put(DARK_AQUA, from(0, 170, 170))
			.put(DARK_RED, from(170, 0, 0))
			.put(DARK_PURPLE, from(170, 0, 170))
			.put(GOLD, from(255, 170, 0))
			.put(GRAY, from(170, 170, 170))
			.put(DARK_GRAY, from(85, 85, 85))
			.put(BLUE, from(85, 85, 255))
			.put(GREEN, from(85, 255, 85))
			.put(AQUA, from(85, 255, 255))
			.put(RED, from(255, 85, 85))
			.put(LIGHT_PURPLE, from(255, 85, 255))
			.put(YELLOW, from(255, 255, 85))
			.put(WHITE, from(255, 255, 255))
			.build();

	private static Color from(int r, int g, int b){
		return new Color(r, g, b);
	}

	public static ChatColor find(amata1219.beta.parkour.util.Color color){
		TreeMap<Integer, ChatColor> distanceMap = new TreeMap<>();
		COLORS.forEach((key, value) -> distanceMap.put(value.distance(color), key));
		return distanceMap.firstEntry().getValue();

	}

	private static class Color {

		public final int r, g, b;

		public Color(int r, int g, int b){
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public int distance(amata1219.beta.parkour.util.Color color){
			int rd = abs(r - color.red);
			int gd = abs(g - color.green);
			int bd = abs(b - color.blue);
			return (int) Math.sqrt(rd * rd + gd * gd + bd * bd);
		}

	}

}
