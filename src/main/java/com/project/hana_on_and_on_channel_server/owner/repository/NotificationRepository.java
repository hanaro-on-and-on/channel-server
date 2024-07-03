package com.project.hana_on_and_on_channel_server.owner.repository;

import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByWorkPlaceWorkPlaceId(Long workPlaceId);
    List<Notification> findByWorkPlaceOrderByCreatedAtDesc(WorkPlace workPlace);
    Optional<Notification> findTop1ByWorkPlaceWorkPlaceIdOrderByCreatedAtDesc(Long workPlaceId);
}
