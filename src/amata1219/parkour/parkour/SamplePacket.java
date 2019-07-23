package amata1219.parkour.parkour;

import java.io.IOException;

import org.bukkit.craftbukkit.v1_13_R2.CraftParticle;

import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketDataSerializer;
import net.minecraft.server.v1_13_R2.PacketListenerPlayOut;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.Particle;
import net.minecraft.server.v1_13_R2.ParticleParam;
import net.minecraft.server.v1_13_R2.Particles;

public class SamplePacket implements Packet<PacketListenerPlayOut> {

	public float x;
	public float y;
	public float z;
	public float offsetX_red;
	public float offsetY_green;
	public float offsetZ_blue;
	public float extra;
	public int count;
	public boolean flag = true;
	public ParticleParam particleAndData;

	private float a;
	private float b;
	private float c;
	private float d;
	private float e;
	private float f;
	private float g;
	private int h;
	private boolean i;
	private ParticleParam j;

	public SamplePacket() {
	}

	public <T extends ParticleParam> SamplePacket(T var0, boolean var1, float var2, float var3,
			float var4, float var5, float var6, float var7, float var8, int var9) {
		this.j = var0;//CraftParticle.toNMS(particle, data)
		this.i = var1;//true
		this.a = var2;//x
		this.b = var3;//y
		this.c = var4;//z
		this.d = var5;//offsetX red
		this.e = var6;//offsetY green
		this.f = var7;//offsetZ blue
		this.g = var8;//extra
		this.h = var9;//count CraftPlayer

		/*
		 * new PacketPlayOutWorldParticles(
					CraftParticle.toNMS(particle, data), true, (float) x, (float) y, (float) z, (float) offsetX,
					(float) offsetY, (float) offsetZ, (float) extra, count);
		 */
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void a(PacketDataSerializer var0) throws IOException {
		Object var1 = (Particle) IRegistry.PARTICLE_TYPE.fromId(var0.readInt());
		if (var1 == null) {
			var1 = Particles.c;
		}

		this.i = var0.readBoolean();
		this.a = var0.readFloat();
		this.b = var0.readFloat();
		this.c = var0.readFloat();
		this.d = var0.readFloat();
		this.e = var0.readFloat();
		this.f = var0.readFloat();
		this.g = var0.readFloat();
		this.h = var0.readInt();
		this.j = this.a(var0, (Particle) var1);
	}

	private <T extends ParticleParam> T a(PacketDataSerializer var0, Particle<T> var1) {
		return var1.f().b(var1, var0);
	}

	public void b(PacketDataSerializer var0) throws IOException {
		var0.writeInt(IRegistry.PARTICLE_TYPE.a(this.j.b()));
		var0.writeBoolean(this.i);
		var0.writeFloat(this.a);
		var0.writeFloat(this.b);
		var0.writeFloat(this.c);
		var0.writeFloat(this.d);
		var0.writeFloat(this.e);
		var0.writeFloat(this.f);
		var0.writeFloat(this.g);
		var0.writeInt(this.h);
		this.j.a(var0);
	}

	public void a(PacketListenerPlayOut var0) {
		//var0.a(this);
	}

}
