package com.catis.control.dao.custom;

import java.util.Map;
import java.util.UUID;

public interface RapportDeVisiteRepositoryCustom {

    Map<String, String> getResultVisiteId(UUID id);
}
