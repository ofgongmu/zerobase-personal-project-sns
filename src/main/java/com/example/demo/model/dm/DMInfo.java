package com.example.demo.model.dm;

import com.example.demo.entity.DM;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DMInfo {
  private String senderNickname;
  private String senderProfilePicUrl;
  private String messageText;
  private String messageImageUrl;
  private LocalDateTime sentAt;

  public static DMInfo fromEntity(DM dm) {
    return DMInfo.builder()
        .senderNickname(dm.getAccount().getNickname())
        .senderProfilePicUrl(dm.getAccount().getImageUrl())
        .messageText(dm.getText())
        .messageImageUrl(dm.getImageUrl())
        .sentAt(dm.getCreatedDate())
        .build();
  }
}
