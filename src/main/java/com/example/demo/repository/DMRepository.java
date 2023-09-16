package com.example.demo.repository;

import com.example.demo.entity.DM;
import com.example.demo.entity.DMRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMRepository extends JpaRepository<DM, Long> {

  Optional<DM> findFirstByDmRoomOrderByCreatedDateDesc(DMRoom dmRoom);
}
