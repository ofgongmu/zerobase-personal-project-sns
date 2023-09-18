package com.example.demo.model.dm;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class DMRoomInviteRequestDto {
  @NotEmpty(message = "DM방에는 한 명 이상의 인원이 초대되어야 합니다.")
  private List<String> accountIds;
}
