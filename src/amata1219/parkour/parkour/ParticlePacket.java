package amata1219.parkour.parkour;

import java.io.IOException;
import java.util.Random;

import org.bukkit.craftbukkit.v1_13_R2.CraftParticle;

import amata1219.amalib.color.RGB;
import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketDataSerializer;
import net.minecraft.server.v1_13_R2.PacketListenerPlayOut;
import net.minecraft.server.v1_13_R2.ParticleParam;

public class ParticlePacket implements Packet<PacketListenerPlayOut> {

	private static final Random RANDOM = new Random();

	public static final float EXTRA = 1;
	public static final int COUNT = 0;
	public static final boolean FLAG = true;
	public static final ParticleParam PARTICLE = CraftParticle.toNMS(org.bukkit.Particle.REDSTONE, null);

	public final float x;
	public final float y;
	public final float z;
	public final float red;
	public final float green;
	public final float blue;

	public ParticlePacket(float x, float y, float z, RGB based) {
		this.x = x;
		this.y = y;
		this.z = z;

		RGB color = new RGB(mixColor(based.red), mixColor(based.green), mixColor(based.blue));
		red = color.parcentageOfRed;
		green = color.parcentageOfGreen;
		blue = color.parcentageOfBlue;
	}

	private int mixColor(int color){
		return color + RANDOM.nextInt(30) - 14;
	}

	public void a(PacketDataSerializer var0) throws IOException {

	}

	public void b(PacketDataSerializer serializer) throws IOException {
		serializer.writeInt(IRegistry.PARTICLE_TYPE.a(PARTICLE.b()));
		serializer.writeBoolean(FLAG);
		serializer.writeFloat(x);
		serializer.writeFloat(y);
		serializer.writeFloat(z);
		serializer.writeFloat(red);
		serializer.writeFloat(green);
		serializer.writeFloat(blue);
		serializer.writeFloat(EXTRA);
		serializer.writeInt(COUNT);
		PARTICLE.a(serializer);
	}

	public void a(PacketListenerPlayOut listener) {
		System.out.println("debug@custompacket");
	}

}
