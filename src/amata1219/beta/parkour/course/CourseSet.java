package amata1219.beta.parkour.course;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import amata1219.beta.parkour.Parkour;
import amata1219.beta.parkour.region.CheckArea;
import amata1219.beta.parkour.region.VisibleRegion;
import amata1219.parkour.chunk.ChunksToRegionsMap;
import graffiti.Maybe;
import graffiti.Yaml;

public class CourseSet {

	private static CourseSet INSTANCE = new CourseSet();

	public static void load(){
		INSTANCE = new CourseSet();
	}

	public static CourseSet instance(){
		return INSTANCE;
	}

	private final Parkour plugin = Parkour.plugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "Courses");
	private final Map<String, Course> courses = new HashMap<>();
	private final ChunksToRegionsMap<VisibleRegion> chunksToStartLinesMap = new ChunksToRegionsMap<>();
	private final ChunksToRegionsMap<VisibleRegion> chunksToFinishLinesMap = new ChunksToRegionsMap<>();
	private final ChunksToRegionsMap<CheckArea> chunksToCheckAreasMap = new ChunksToRegionsMap<>();

	private CourseSet(){
		if(!folder.exists()) folder.mkdirs();

		Maybe.unit(folder.listFiles())
		.bind(Arrays::stream)
		.ifJust(stream -> stream.map(file -> new Yaml(plugin, file, "course.yml"))
								.map(Course::new)
								.forEach(this::register)
		);
	}

	public void register(Course course){
		if(contains(course)) unregister(course);

		courses.put(course.name, course);

		chunksToStartLinesMap.putAll(course.startLine);
		chunksToFinishLinesMap.putAll(course.finishLine);
		//course.checkAreas.
	}

	public void unregister(Course course){
		unregister(course.name);
	}

	public void unregister(String courseName){
		if(!contains(courseName)) return;


	}

	public boolean contains(Course course){
		return contains(course.name);
	}

	public boolean contains(String courseName){
		return courses.containsKey(courseName);
	}

	public Maybe<Course> get(String courseName){
		return Maybe.unit(courses.get(courseName));
	}

}
