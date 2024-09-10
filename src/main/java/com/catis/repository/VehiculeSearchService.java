// package com.catis.repository;

// import java.util.List;
// import java.util.UUID;

// import org.springframework.data.elasticsearch.annotations.Query;
// import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

// import com.catis.objectTemporaire.VehicleSearch;

// public interface VehiculeSearchService extends ElasticsearchRepository<VehicleSearch, UUID>{


//     @Query("{\"bool\":{\"should\": [{\"regexp\": {\"chassis.keyword\": {\"value\": \"?0.*\", \"flags\": \"ALL\", \"case_insensitive\": true, \"max_determinized_states\": 10000}}}]}}")
//     List<VehicleSearch> findByChassisStartsWithIgnoreCase(String chassis);
    
// }
