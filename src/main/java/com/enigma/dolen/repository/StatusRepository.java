package com.enigma.dolen.repository;

import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.entity.Order;
import com.enigma.dolen.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, String> {
    Status getStatusByOrder_Id(String id);
    List<Status> getStatusesByStatusAndOrder_Id(EStatus status, String order_id);

}
