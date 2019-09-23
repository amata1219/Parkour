package amata1219.beta.parkour.region;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.beta.parkour.location.ImmutableLocation;
import amata1219.beta.parkour.location.Location;
import amata1219.beta.parkour.serialize.Deserializer;
import amata1219.beta.parkour.serialize.Serializer;

public class Region {

	public final World world;
	public final ImmutableLocation min, max;

	public static Region deserialize(String text){
		return Deserializer.stream(text)
		.map(Bukkit::getWorld, 0)
		.map(Double::parseDouble, double.class, 1, 6)
		.deserializeTo(Region.class);
	}

	public Region(ImmutableLocation lesserBoundaryCorner, ImmutableLocation greaterBoundaryCorner){
		this.world = lesserBoundaryCorner.world;
		this.min = lesserBoundaryCorner;
		this.max = greaterBoundaryCorner;
	}

	public Region(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
		this.world = world;
		min = new ImmutableLocation(world, minX, minY, minZ);
		max = new ImmutableLocation(world, maxX, maxY, maxZ);
	}

	public boolean isIn(Location location){
		return isIn(location.getWorld(), location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public boolean isIn(World world, double x, double y, double z){
		return min.x <= x && x <= max.x
				&& min.y <= y && y <= max.y
				&& min.z <= z && z <= max.z
				&& world.equals(world);
	}

	public double getLength(){
		return max.getIntX() - min.getIntX();
	}

	public double getHeight(){
		return max.getIntY() - min.getIntY();
	}

	public double getWidth(){
		return max.getIntZ() - min.getIntZ();
	}

	public double getArea(){
		return getLength() * getWidth();
	}

	public double getVolume(){
		return getArea() * getHeight();
	}

	public Region extend(double x, double y, double z){
		return new Region(min.add(Math.min(x, 0), Math.min(y, 0), Math.min(z, 0)), max.add(Math.max(x, 0), Math.max(y, 0), Math.max(z, 0)));
	}

	public Region add(double x, double y, double z){
		return new Region(min.add(x, y, z), max.add(x, y, z));
	}

	public Region add(Location location){
		return add(location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public Region sub(double x, double y, double z){
		return new Region(min.sub(x, y, z), max.sub(x, y, z));
	}

	public Region sub(Location location){
		return sub(location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public Region relative(double x, double y, double z){
		return new Region(world, min.x - x, min.y - y, min.z - z, max.x - x, max.y - y, max.z - z);
	}

	public Region relative(Location location){
		return relative(location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public String serialize(){
		return Serializer.serialize(world.getName(), min.x, min.y, min.z,
				max.x, max.y, max.z);
	}

}
