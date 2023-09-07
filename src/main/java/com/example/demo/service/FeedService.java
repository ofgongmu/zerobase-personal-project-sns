package com.example.demo.service;

import com.example.demo.common.S3Component;
import com.example.demo.constants.FollowState;
import com.example.demo.entity.Account;
import com.example.demo.entity.Follow;
import com.example.demo.entity.Post;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.model.feed.FeedRequestDto;
import com.example.demo.model.feed.FeedResponseDto;
import com.example.demo.model.feed.SeePostResponseDto;
import com.example.demo.model.feed.WritePostRequestDto;
import com.example.demo.model.feed.WritePostResponseDto;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FeedService {
  private final AccountRepository accountRepository;
  private final FollowRepository followRepository;
  private final PostRepository postRepository;

  private final S3Component s3Component;

  public WritePostResponseDto writePost(String id, WritePostRequestDto request, MultipartFile image) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Post post = Post.builder()
        .account(account)
        .text(request.getText())
        .createdDate(LocalDateTime.now())
        .build();
    postRepository.save(post);
    uploadPostImageIfExists(image, post);
    return WritePostResponseDto.fromEntity(post);
  }

  public void deletePost(String id, Long postNum) {
    Post post = postRepository.findByPostNum(postNum)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_DOES_NOT_EXIST));
    checkIfPostWriter(id, post);
    postRepository.delete(post);
  }

  public SeePostResponseDto seePost(String id, Long postNum) {
    Post post = postRepository.findByPostNum(postNum)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_DOES_NOT_EXIST));
    checkIfSeeablePost(id, post);
    return SeePostResponseDto.fromEntity(post);
  }

  public FeedResponseDto seeFeed(String id, FeedRequestDto request) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    // 스크롤이 아래 끝에 도달할 때마다 프론트엔드 측에서 가장 오래된 포스트의 번호를 받아와, 해당 번호로부터 20개를 추가적으로 로드하는 로직
    // 첫 진입 시 lastPostNum의 기본값은 Long.MAX_VALUE로 상정
    return FeedResponseDto.fromEntities(
        postRepository.findByAccountIsInAndPostNumLessThanOrderByPostNumDesc(
            getSelfAndFollowingList(account), request.getLastPostNum(), PageRequest.of(0, 20)));
  }

  private void uploadPostImageIfExists(MultipartFile image, Post post) {
    if (image != null) {
      postRepository.save(post.toBuilder()
          .imageUrl(s3Component.uploadFile("post/", image))
          .build());
    }
  }

  private void checkIfPostWriter(String id, Post post) {
    if (!id.equals(post.getAccount().getId())) {
      throw new CustomException(ErrorCode.UNABLE_TO_DELETE_POST);
    }
  }

  private void checkIfSeeablePost(String id, Post post) {
    Account readerAccount = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    if (!readerAccount.equals(post.getAccount())
    && post.getAccount().getIsProtected() && !checkIfFollower(post.getAccount(), readerAccount)) {
      throw new CustomException(ErrorCode.UNABLE_TO_SEE_POST);
    }
  }

  private boolean checkIfFollower(Account writerAccount, Account readerAccount) {
    return followRepository
        .existsByFollowingAndFollowedAndState(readerAccount, writerAccount, FollowState.ACCEPTED);
  }

  private List<Account> getSelfAndFollowingList(Account account) {
    List<Account> accountList = new ArrayList<>(
        followRepository.findByFollowingAndState(account, FollowState.ACCEPTED)
            .stream().map(Follow::getFollowed).toList());
    accountList.add(account);
    return accountList;
  }

}
