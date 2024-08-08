package com.catis.control;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

import com.catis.control.dao.CategorieTestProduitDao;
import com.catis.control.dao.GieglanFileDao;
import com.catis.control.dao.MesureDao;
import com.catis.control.dao.ProduitDao;
import com.catis.control.entities.CategorieTestProduit;
import com.catis.control.entities.GieglanFile;
import com.catis.control.entities.Mesure;
import com.catis.control.entities.Produit;
import com.catis.control.services.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableJpaAuditing(auditorAwareRef = "treatmentServiceProvider")
public class ControlServiceApplication {

	@Autowired
	private CapelecRapportImpl capelecRapport;

	@Autowired
	private SatelliteRapportImpl satelliteRapport;

	@Autowired
	private RavaglioliRapportImpl ravaglioliRapport;

	@Autowired
	private autovisionRapportImpl autovisionRapport;

	@Autowired
	private boschRapportImpl boschRapport;

	@Autowired
	private EuroSystMachineImpl euroSystMachine;

	@Autowired
	private CategorieTestProduitDao categorieTestProduitDao;

	@Autowired
	private ProduitDao produitDao;

	@Autowired private MesureDao mesureDao;

	public static void main(String[] args) {
		SpringApplication.run(ControlServiceApplication.class, args);

	/** TEST 5
		LocalDateTime localDateTime = LocalDateTime.parse("2019-07-10 14:06");
		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		ZoneId zoneId = ZoneId.of("+02:00");
		ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		System.out.println(zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
	*/

	/**
		String str = "(${myKey1} + ${myKey2})/550";
	    Map<String, String> map = new HashMap<>();
	    map.put("myKey1", "50");
	    map.put("myKey21", "50");
	    for (Map.Entry<String, String> entry : map.entrySet()) {
	        str = str.replace("${" + entry.getKey() + "}", entry.getValue());
	    }
	    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
	    Object expResult1;
	    try {
	    	System.out.println(scriptEngine.eval(str));
	    	expResult1 = scriptEngine.eval(str);
	    	System.out.println("\n\n ++++++"+expResult1);
		} catch (Exception e) {
			System.out.print(e.getMessage());
			expResult1 = null;
		}
	    
	    System.out.println("++++++++++++++++++++++ "+expResult1);
	**/

	/**
	 * TEST  1
		 * 
		Supplier<String> place = () -> "viena";
		Places places = new Places();
		populate(places, place);
		Consumer<Places> consumer = p -> System.out.print(p.location);
	}
	
	static<R> void populate(Places t, Supplier<R> supplier) {
		t.location = String.valueOf(supplier.get());
	}
	
	static class Places {
		String location;
	}*/
	}

	@Bean
	CommandLineRunner start(GieglanFileDao gieglanFileDao) {
		return args -> {
			/*GieglanFile g= new GieglanFile();
			g.setName("grosse tete");
			gieglanFileDao.save(g);*/
				/*List<UUID> produitId = Arrays.asList(UUID.fromString("da4db125-88cd-11ec-8389-e8d8d1fde93d"), UUID.fromString("da4db3b9-88cd-11ec-8389-e8d8d1fde93d"), UUID.fromString("da4db76e-88cd-11ec-8389-e8d8d1fde93d"));
				categorieTestProduitDao.getCatTestProduitsByIdProducts(UUID.fromString("da4db8ee-88cd-11ec-8389-e8d8d1fde93d"), UUID.fromString("145412ea-8d82-11ec-8389-e8d8d1fde93d")).forEach(prod -> {
					System.out.println("test produit id  :"+prod.getId());
					//produitId.forEach(prods -> {
						CategorieTestProduit catP = new CategorieTestProduit();
						catP.setCategorieTest(prod.getCategorieTest());
						catP.setProduit(produitDao.findById(UUID.fromString("da437070-88cd-11ec-8389-e8d8d1fde93d")).get());
						catP.setType(prod.getType());
						catP.setActiveStatus(prod.isActiveStatus());
						catP.setOrganisation(prod.getOrganisation());
						List<Mesure> mesures = mesureDao.findMesureByCategorieTestProduitsId(prod);
						mesures.forEach(mesure -> {
							catP.addMesure(mesure);
							System.out.println(" mesure id  : " + mesure.getIdMesure());
						});
						categorieTestProduitDao.save(catP);
					//});
				});*/

			//conformityService.start();
			/*ravaglioliRapport.start("Ravaglioli.pdf");
			capelecRapport.start("capelec.pdf");
			satelliteRapport.start("satelliteNgono.pdf");
			autovisionRapport.start("AUTOVISION.pdf");
			boschRapport.start("bosch.pdf");
			euroSystMachine.start("euroSystem.pdf");
/*
			Class clazz = ControlServiceApplication.class;
			try {
				InputStream inputStream = clazz.getResourceAsStream("/static/bosch.pdf");
				PDDocument pdDoc  = PDDocument.load(inputStream);
				PDFTextStripper pdfStripper = new PDFTextStripper();
				String parsedText = pdfStripper.getText(pdDoc);
				System.out.println(parsedText);
				File tmpFile = File.createTempFile("test", ".tmp");
				FileWriter writer = new FileWriter(tmpFile);
				writer.write(parsedText);
				writer.close();

				BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
				System.out.println(tmpFile.toURI());
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		};
	}
}

/**
 * Test 3
interface Drawable {
	default void draw() {
		System.out.println("Drawing a drawable");
	}
}

interface GpuAccelerated {
	default void draw() {
		System.out.println("Drawing with Gpu acceration");
	}
}

class Basic2DShape implements GpuAccelerated {}

class Triangle extends Basic2DShape implements GpuAccelerated, Drawable {
	public static void main(String[] args) {
		new Triangle().draw();
	}
}
*/


/** Test 4
@FunctionalInterface
public interface HelloWorld {
	public String sayHello(String name);
	public String toString();
	public boolean equals(Object obj);
}

public class Hello {
	public String  name;
	public String syaBye(String name) {
		return name;
	}
	public static void main(String args[]) {
		HelloWorld hello = (p) -> p;
		Hello h = new Hello();
		BiFunction<String, String, String> f= (part1, part2) -> (part1 +part2);
		final String result = f.apply(h.syaBye("Hello World!"), f.apply("Bye Bye", hello.sayHello("word!")));
		System.out.print(result);
	}
}
*/

/** TEst 6
class One {
	String name = "john";
	public Optional<String> getName() {
		return Optional.of(name);
	}
}

class Two {
	One one = new One();
	public Optional<One> getOne() {
		return Optional.of(one);
	}
}

class Three {
	Two two = new Two();
	public Optional<Two> getTwo() {
		return Optional.ofNullable(two);
	}
}
*/

/** TEST 7
 * 
 * 
 * @author HP
 *
 *
class ListUtil {
	public static String toString(List<Integer> numbers) {
		StringJoiner sj = new StringJoiner(";", "[", "]");
		numbers.forEach((i)-> sj.add(i.toString()));
		return sj.toString();
	}
	
	public String toString(List<Long> numbers) {
		StringJoiner sj = new StringJoiner(",");
		numbers.forEach((i)-> sj.add(i.toString()));
		return sj.toString();
	}
}

public class Numbers extends ListUtil {
	public static List<Integer> numbers = Arrays.asList(1,2,3,4);
	public static void (String args[]) {
		System.out.println(ListUtil.toString(numbers));
	}
}
*/