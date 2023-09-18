package com.example.demo.model.dm;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinedDMRoomsResponseDto {
  private List<DMRoomInfo> dmRoomList;
}
