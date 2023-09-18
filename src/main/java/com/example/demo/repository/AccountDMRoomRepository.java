package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.AccountDMRoom;
import com.example.demo.entity.DMRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountDMRoomRepository extends JpaRepository<AccountDMRoom, Long> {
  @Query(value = "SELECT * FROM AccountDMRoom ad "
      + "INNER JOIN DMRoom d ON ad.dm_room_dm_room_num = d.dm_room_num  "
      + "WHERE ad.account_account_num = ?1 ORDER BY d.modified_date", nativeQuery = true)
  List<AccountDMRoom> findByAccountOrderRecent(Long accountNum);
  List<AccountDMRoom> findByDmRoom(DMRoom dmRoom);
  boolean existsByAccountAndDmRoom(Account account, DMRoom dmRoom);
  void deleteByAccountAndDmRoom(Account account, DMRoom dmRoom);
}
