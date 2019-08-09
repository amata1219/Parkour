package amata1219.parkour.head;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Heads {

	public final static Map<UUID, Head> HEADS = new HashMap<>();

	static{
		initializeWithPlayerHeads(
			"58becc44-c5b7-420f-8800-15ba88820973,ledlaggazi,1000000",
			"82669f11-f1e5-402c-9642-75aff8a47613,YukiLeafX,500000",
			"7daf21e7-b275-43dd-bc0d-4762c73d6199,siloneco,500000",
			"4edeba6b-0f32-4383-9e74-5e7988a7cf7e,Mori01231,500000",
			"54e4dd55-fd3f-46d4-8ef9-ff3b6957e3b0,amata1219,500000",
			"aa81b3a6-dce8-4b1c-99d5-164632dd4da3,Mizinkobusters,500000",
			"84a08980-1f9b-4cf7-98a1-839592834d4d,Bounen057,500000",
			"199798ad-2966-47ba-932d-c0a735144cf1,Ad_mira,300000",
			"836da165-253a-48c1-8c94-15ec42ca3a1a,SC_Yazuki,300000",
			"b521e99d-e229-4390-8c21-ae883a83c163,Akiakibird_japan,300000",
			"deae598c-d3f8-4421-9528-1d0bcb72d599,p_maikura,150000",
			"de68717a-7aa1-4a4a-b150-cc55b686ac44,_Z_soda__,150000",
			"49ffabd4-0b9d-455c-bcc1-621c880efff9,Dall0125,100000",
			"d15e4e82-2a07-4472-92da-aa010020d209,BlackAlice1694,100000",
			"64c0d71e-5307-4fdc-886b-6c763ea0c775,P0_KI,100000",
			"a61f7017-f780-4a6c-8b0c-461bd12602fc,Sasterisk24,100000",
			"516ad1d3-6cd4-47f0-8fc7-a89c09f26f5a,ard_Riri,100000",
			"4c171dce-e6dd-4b11-a46c-9739a218a27f,kyoujyu88,100000",
			"f375550e-eb44-4673-9e9c-a5bb36810a95,red_akairo,100000",
			"94fcaad2-b68a-4779-a3c8-b22193d5f3a7,yugomegane,100000",
			"dbe1ac1e-e259-4bbd-ae1e-eebcc9c19cc0,taiki7,100000",
			"0c7f42fb-4a8a-466d-9f1e-3acf1a4558b3,iThank,100000",
			"38c5d35c-8bb1-46a1-a9e7-3e3e61582e21,Suppp4,100000",
			"bfb4982e-02b8-42ed-b7f9-59f1531d983c,NR_3,100000",
			"595351ac-7618-4671-921d-a0c3c33bbf90,papiyonnn,100000",
			"b38eda99-33e0-4bee-bd65-00e4b1dc5a1d,_regretDees_,100000",
			"15ca310d-750b-466b-ba2a-e38ad96a266f,JapanALL,100000",
			"5b221142-e795-4fe5-8432-913450c422a6,KUCHITSU,100000",
			"13d57593-0a92-40ac-860a-73b6fd91a661,SHAURA517,100000",
			"81624b24-8fd1-4453-a386-f9be35165b51,Tomoya,100000",
			"c08d7e99-4912-47ea-9c66-326f8dacf2a5,FlowSounds,100000",
			"6e773e5e-b914-41d7-b6b9-2beab7b0646e,toufu_toufu,100000",
			"b967b9de-8ac8-4f17-aadf-8ce39e8b99fb,SAPONIN9000,100000",
			"db2e9a55-c2f6-4ff6-878b-aafaf0311ac9,mikage255,100000",
			"563ccfc5-6bac-48e1-917b-dacac709b093,Ayaka_1031,100000",
			"05e9fb08-8e0b-4dad-b611-ae13625b051f,mi_0711,100000",
			"06e9b34d-6fb3-4d34-ba05-f2b258f75b50,Uekibachi,100000"
		);

		initializeWithCustomHeads(

		);
	}

	private static void initializeWithPlayerHeads(String... texts){
		Arrays.stream(texts)
		.map(text -> text.split(","))
		.map(parts -> new Head(UUID.fromString(parts[0]), parts[1], Integer.parseInt(parts[2])))
		.forEach(head -> HEADS.put(head.uuid, head));

		HEADS.forEach((k, v) -> System.out.println(v.name));
	}

	private static void initializeWithCustomHeads(String... texts){
		Arrays.stream(texts)
		.map(text -> text.split(","))
		.map(parts -> new Head(UUID.fromString(parts[0]), parts[1], Integer.parseInt(parts[2]), parts[3]))
		.forEach(head -> HEADS.put(head.uuid, head));
	}

}
