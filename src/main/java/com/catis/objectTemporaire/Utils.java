package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String parseDate(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static final List<ProduitCategorieTest> tests = new ArrayList<ProduitCategorieTest>() {{
        add(new ProduitCategorieTest(5L,
                new ArrayList<TestNew>() {{
                    add(new TestNew(1L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F") );
                    add(new TestNew(2L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(4L,"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(9L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp","G" )  );
                    add(new TestNew(12L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp", "JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(2L,
                new ArrayList<TestNew>() {{
                    add(new TestNew(1L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(2L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(3L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(4L,"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(9L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(12L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(3L,
                new ArrayList<TestNew>() {{
                    add(new TestNew(1L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(2L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(3L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(4L,"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(9L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(12L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(4L,
                new ArrayList<TestNew>() {{
                    add(new TestNew(1L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(2L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(3L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(4L,"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(9L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(12L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
        add(new ProduitCategorieTest(6L,
                new ArrayList<TestNew>() {{
                    add(new TestNew(1L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(2L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"><i class=\"i-Car-2\"></i></span>&nbsp", "R"));
                    add(new TestNew(3L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"><i class=\"i-Jeep-2\"></i></span>&nbsp","S"));
                    add(new TestNew(4L,"<span class=\"badge badge-light\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Reglophare\"><i class=\"i-Flash\"></i></span>&nbsp", "P")  );
                    add(new TestNew(9L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"><i class=\"i-Cloud1\"></i></span>&nbsp", "G")  );
                    add(new TestNew(12L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));add(new ProduitCategorieTest(10L,
                new ArrayList<TestNew>() {{
                    add(new TestNew(1L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp", "F")  );
                    add(new TestNew(12L,"<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures Visuelles\"><i class=\"i-Eye\"></i></span>&nbsp","JSON")  );
                }}
        ));
    }};
}
