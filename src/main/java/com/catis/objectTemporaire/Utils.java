package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {
    public static String parseDate(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static final List<ProduitCategorieTest> tests = new ArrayList<ProduitCategorieTest>() {{
        add(new ProduitCategorieTest(UUID.fromString("da4db60d-88cd-11ec-8389-e8d8d1fde93d"),
                new ArrayList<TestNew>() {{
                    add(new TestNew(UUID.fromString("f818383d-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F") );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(UUID.fromString("f820c03a-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(UUID.fromString("f823f2a4-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp","G" )  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp", "JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(UUID.fromString("da4db8ee-88cd-11ec-8389-e8d8d1fde93d"),
                new ArrayList<TestNew>() {{
                    add(new TestNew(UUID.fromString("f818383d-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F") );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(UUID.fromString("f820c03a-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(UUID.fromString("f823f2a4-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp","G" )  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp", "JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(UUID.fromString("da4dadfb-88cd-11ec-8389-e8d8d1fde93d"),
                new ArrayList<TestNew>() {{
                    add(new TestNew(UUID.fromString("f818383d-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(UUID.fromString("f820bf9b-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(UUID.fromString("f820c03a-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(UUID.fromString("f823f2a4-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(UUID.fromString("da4db125-88cd-11ec-8389-e8d8d1fde93d"),
                new ArrayList<TestNew>() {{
                    add(new TestNew(UUID.fromString("f818383d-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(UUID.fromString("f820bf9b-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(UUID.fromString("f820c03a-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(UUID.fromString("f823f2a4-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(UUID.fromString("da4db3b9-88cd-11ec-8389-e8d8d1fde93d"),
                new ArrayList<TestNew>() {{
                    add(new TestNew(UUID.fromString("f818383d-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(UUID.fromString("f820bf9b-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(UUID.fromString("f820c03a-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(UUID.fromString("f823f2a4-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(UUID.fromString("da4db76e-88cd-11ec-8389-e8d8d1fde93d"),
                new ArrayList<TestNew>() {{
                    add(new TestNew(UUID.fromString("f818383d-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(UUID.fromString("f820bf9b-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(UUID.fromString("f820c03a-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(UUID.fromString("f823f2a4-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));add(new ProduitCategorieTest(UUID.fromString("da4dbb4e-88cd-11ec-8389-e8d8d1fde93d"),
                new ArrayList<TestNew>() {{
                    add(new TestNew(UUID.fromString("f818383d-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(UUID.fromString("f820bdbe-882f-11ec-8389-e8d8d1fde93d"),"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
    }};
}
