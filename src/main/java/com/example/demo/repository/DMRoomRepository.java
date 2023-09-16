package com.example.demo.repository;

import com.example.demo.entity.DMRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMRoomRepository extends JpaRepository<DMRoom, Long> {
  Optional<DMRoom> findByDmRoomNum(Long dmRoomNum);
}
