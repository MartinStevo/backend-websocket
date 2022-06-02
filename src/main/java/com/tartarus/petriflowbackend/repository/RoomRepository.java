package com.tartarus.petriflowbackend.repository;

import com.tartarus.petriflowbackend.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    boolean existsByUrlPassword(String urlPassword);

    RoomEntity getByUrlPassword(String urlPassword);
}
