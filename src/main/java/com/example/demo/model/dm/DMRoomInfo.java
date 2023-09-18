package com.example.demo.model.dm;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DMRoomInfo {
  private List<String> joinedNicknames;
  private String mostRecentDMText;
}
