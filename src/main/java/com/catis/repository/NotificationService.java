package com.catis.repository;

import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.EventDto;

public interface NotificationService {

    void sendNotification(String memberId, EventDto event);

    void dipatchVisiteToMember(String memberId, Visite visite, boolean edited);
}
