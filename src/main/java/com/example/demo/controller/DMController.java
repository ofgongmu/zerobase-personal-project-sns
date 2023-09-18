package com.example.demo.controller;

import com.example.demo.model.dm.DMRoomInviteRequestDto;
import com.example.demo.model.dm.SendDMRequestDto;
import com.example.demo.service.DMService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DMController {
  private final DMService dmService;

  @PostMapping("/rooms")
  public ResponseEntity<?> createDMRoom(@AuthenticationPrincipal String id,
                                        @RequestBody @Valid DMRoomInviteRequestDto request) {
    return ResponseEntity.ok(dmService.createDMRoom(id, request));
  }

  @GetMapping("/rooms")
  public ResponseEntity<?> seeJoinedDmRooms(@AuthenticationPrincipal String id) {
    return ResponseEntity.ok(dmService.seeJoinedDmRooms(id));
  }

  @PostMapping("/room/{dmRoomNum}/invite")
  public ResponseEntity<?> inviteToDMRoom(@AuthenticationPrincipal String id, @PathVariable Long dmRoomNum,
                                          @RequestBody @Valid DMRoomInviteRequestDto request) {
    return ResponseEntity.ok(dmService.inviteToDMRoom(id, dmRoomNum, request));
  }

  @MessageMapping("/room/{dmRoomNum}")
  public ResponseEntity<?> sendDM(@AuthenticationPrincipal String id, @DestinationVariable @PathVariable Long dmRoomNum,
                                  @RequestPart(value = "request") SendDMRequestDto request,
                                  @RequestPart(value = "image", required = false) MultipartFile image) {
    return ResponseEntity.ok(dmService.sendDM(id, dmRoomNum, request, image));
  }

  @GetMapping("/room/{dmRoomNum}")
  public ResponseEntity<?> seeDMRoom(@AuthenticationPrincipal String id, @PathVariable Long dmRoomNum) {
    return ResponseEntity.ok(dmService.seeDmRoom(id, dmRoomNum));
  }

  @DeleteMapping("/room/{dmRoomNum}")
  public void leaveDMRoom(@AuthenticationPrincipal String id, @PathVariable Long dmRoomNum) {
    dmService.leaveDMRoom(id, dmRoomNum);
  }
}
