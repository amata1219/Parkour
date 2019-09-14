package amata1219.beta.parkour.region;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.beta.parkour.location.ImmutableLocation;
import amata1219.beta.parkour.location.Location;
import amata1219.beta.parkour.serialize.Deserializer;
import amata1219.beta.parkour.serialize.Serializer;

public class Region {

	public final World world;
	public final ImmutableLocation lesserBoundaryCorner, greaterBoundaryCorner;

	public static Region deserialize(String text){
		return Deserializer.stream(text)
		.map(Bukkit::getWorld, 0)
		.map(Double::parseDouble, double.class, 1, 6)
		.deserializeTo(Region.class);
	}

	public Region(ImmutableLocation lesserBoundaryCorner, ImmutableLocation greaterBoundaryCorner){
		this.world = lesserBoundaryCorner.world;
		this.lesserBoundaryCorner = lesserBoundaryCorner;
		this.greaterBoundaryCorner = greaterBoundaryCorner;
	}

	public Region(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
		this.world = world;
		lesserBoundaryCorner = new ImmutableLocation(world, minX, minY, minZ);
		greaterBoundaryCorner = new ImmutableLocation(world, maxX, maxY, maxZ);
	}

	public boolean isIn(Location location){
		return isIn(location.getWorld(), location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public boolean isIn(World world, double x, double y, double z){
		return lesserBoundaryCorner.x <= x && x <= greaterBoundaryCorner.x
				&& lesserBoundaryCorner.y <= y && y <= greaterBoundaryCorner.y
				&& lesserBoundaryCorner.z <= z && z <= greaterBoundaryCorner.z
				&& world.equals(world);
	}

	public double getLength(){
		return greaterBoundaryCorner.getIntX() - lesserBoundaryCorner.getIntX();
	}

	public double getHeight(){
		return greaterBoundaryCorner.getIntY() - lesserBoundaryCorner.getIntY();
	}

	public double getWidth(){
		return greaterBoundaryCorner.getIntZ() - lesserBoundaryCorner.getIntZ();
	}

	public double getArea(){
		return getLength() * getWidth();
	}

	public double getVolume(){
		return getArea() * getHeight();
	}

	public Region extend(double x, double y, double z){
		return new Region(lesserBoundaryCorner.add(Math.min(x, 0), Math.min(y, 0), Math.min(z, 0)), greaterBoundaryCorner.add(Math.max(x, 0), Math.max(y, 0), Math.max(z, 0)));
	}

	public Region add(double x, double y, double z){
		return new Region(lesserBoundaryCorner.add(x, y, z), greaterBoundaryCorner.add(x, y, z));
	}

	public Region add(Location location){
		return add(location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public Region sub(double x, double y, double z){
		return new Region(lesserBoundaryCorner.sub(x, y, z), greaterBoundaryCorner.sub(x, y, z));
	}

	public Region sub(Location location){
		return sub(location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public Region relative(double x, double y, double z){
		return new Region(world, lesserBoundaryCorner.x - x, lesserBoundaryCorner.y - y, lesserBoundaryCorner.z - z, greaterBoundaryCorner.x - x, greaterBoundaryCorner.y - y, greaterBoundaryCorner.z - z);
	}

	public Region relative(Location location){
		return relative(location.getIntX(), location.getIntY(), location.getIntZ());
	}

	public String serialize(){
		return Serializer.serialize(world.getName(), lesserBoundaryCorner.x, lesserBoundaryCorner.y, lesserBoundaryCorner.z,
				greaterBoundaryCorner.x, greaterBoundaryCorner.y, greaterBoundaryCorner.z);
	}

}
