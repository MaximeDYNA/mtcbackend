package com.catis.repository.nativeQueries;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.NewListView;

// flemming implimented 
public interface VisiteNativeQueryRepository {
  List<Visite> LastVisiteWithTestIsOkDirectQuery(UUID controlId, UUID visiteId);
  Page<NewListView> MainlistParStatus(int status, UUID orgId, Pageable pageable);
  Page<NewListView> searchedVisitMainList(String search, UUID orgId, Pageable pageable);
  Page<NewListView> searchedVisitMainListstatus(String search, UUID orgId, int status, Pageable pageable);
  Page<NewListView> endedMainVisitList(UUID organisationId, Pageable pageable);

}
