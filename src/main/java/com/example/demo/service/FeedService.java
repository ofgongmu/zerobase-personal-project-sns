package com.example.demo.service;

import com.example.demo.common.S3Component;
import com.example.demo.constants.ContentType;
import com.example.demo.constants.FollowState;
import com.example.demo.entity.Account;
import com.example.demo.entity.Comment;
import com.example.demo.entity.CommentDocument;
import com.example.demo.entity.Follow;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostDocument;
import com.example.demo.entity.Tag;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.model.feed.CommentResponseDto;
import com.example.demo.model.feed.Content;
import com.example.demo.model.feed.FeedRequestDto;
import com.example.demo.model.feed.FeedResponseDto;
import com.example.demo.model.feed.SearchRequestDto;
import com.example.demo.model.feed.SearchResponseDto;
import com.example.demo.model.feed.SearchResultDto;
import com.example.demo.model.feed.SeeCommentResponseDto;
import com.example.demo.model.feed.SeePostResponseDto;
import com.example.demo.model.feed.SuggestTagResponseDto;
import com.example.demo.model.feed.WriteRequestDto;
import com.example.demo.model.feed.WriteResponseDto;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CommentDocumentRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.PostDocumentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.util.TagUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FeedService {
  private final AccountRepository accountRepository;
  private final FollowRepository followRepository;
  private final PostRepository postRepository;
  private final PostDocumentRepository postDocumentRepository;
  private final CommentRepository commentRepository;
  private final CommentDocumentRepository commentDocumentRepository;
  private final TagRepository tagRepository;

  private final S3Component s3Component;
  private final ElasticsearchOperations elasticsearchOperations;

  public WriteResponseDto writePost(String id, WriteRequestDto request, MultipartFile image) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Post post = Post.builder()
        .account(account)
        .text(request.getText())
        .imageUrl(uploadPostImageIfExists(image))
        .build();
    postRepository.save(post);
    postDocumentRepository.save(PostDocument.fromPost(post));
    extractAndSaveAllTags(post);
    return WriteResponseDto.fromEntity(post);
  }

  public SuggestTagResponseDto suggestTag(String tag) {
    return SuggestTagResponseDto.fromEntities(accountRepository.findByIdStartingWith(tag));
  }

  public void deletePost(String id, Long postNum) {
    Post post = postRepository.findByPostNum(postNum)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_DOES_NOT_EXIST));
    checkIfPostWriter(id, post);
    deleteRelatedTags(post);
    PostDocument postDocument = postDocumentRepository.findByPostNum(postNum)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_DOES_NOT_EXIST));

    List<Comment> relatedComments = commentRepository.findByPost(post);
    for (Comment comment: relatedComments) {
      commentRepository.save(comment.toBuilder()
          .post(null)
          .build());
    }
    List<CommentDocument> relatedCommentDocuments =
        commentDocumentRepository.findByPostDocumentNum(postDocument.getPostNum());
    for (CommentDocument commentDocument: relatedCommentDocuments) {
      commentDocument.setPostDocument(null);
      commentDocumentRepository.save(commentDocument);
    }

    postRepository.delete(post);
    postDocumentRepository.delete(postDocument);
  }

  public SeePostResponseDto seePost(String id, Long postNum) {
    Post post = postRepository.findByPostNum(postNum)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_DOES_NOT_EXIST));

    SeePostResponseDto response = hideProtectedPost(id, post);

    List<Comment> comments = commentRepository.findByPost(post);
    response.setComments(hideProtectedComments(id, comments));

    return response;
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

  public WriteResponseDto writeComment(String id, Long postNum, WriteRequestDto request, MultipartFile image) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Post post = postRepository.findByPostNum(postNum)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_DOES_NOT_EXIST));
    Comment comment = Comment.builder()
        .account(account)
        .post(post)
        .text(request.getText())
        .imageUrl(uploadCommentImageIfExists(image))
        .build();
    commentRepository.save(comment);
    commentDocumentRepository.save(CommentDocument.fromComment(comment));
    extractAndSaveAllTags(comment);
    return WriteResponseDto.fromEntity(comment);
  }

  public void deleteComment(String id, Long commentNum) {
    Comment comment = commentRepository.findByCommentNum(commentNum)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_DOES_NOT_EXIST));
    checkIfCommentWriter(id, comment);
    deleteRelatedTags(comment);
    CommentDocument commentDocument = commentDocumentRepository.findByCommentNum(commentNum)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_DOES_NOT_EXIST));
    commentRepository.delete(comment);
    commentDocumentRepository.delete(commentDocument);
  }

  public SeeCommentResponseDto seeComment (String id, Long commentNum) {
    Comment comment = commentRepository.findByCommentNum(commentNum)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_DOES_NOT_EXIST));

    SeeCommentResponseDto response = hideProtectedComment(id, comment);
    response.setPost(hideProtectedPost(id, comment.getPost()));

    return response;
  }

  public SearchResponseDto search (String id, SearchRequestDto request) {
    Query query = new CriteriaQuery(new Criteria("text").matches(request.getKeyword()))
        .addSort(Sort.by("createdDate").descending());
    SearchHits<Content> searchHits =
        elasticsearchOperations.search(query, Content.class, IndexCoordinates.of("content"));

    List<SearchResultDto> response = new ArrayList<>();
    searchHits.forEach(searchHit -> {
        Content content =
            elasticsearchOperations.getElasticsearchConverter().read(Content.class, Document.from(searchHit.getContent()));
        if (!checkIfHiddenContent(id, content.get("accountId").toString())) {
          response.add(SearchResultDto.fromContent(content));
        }
    });

    return SearchResponseDto.builder()
        .resultList(response)
        .build();
  }

  private String uploadPostImageIfExists(MultipartFile image) {
    if (image == null) {
      return null;
    }
    return s3Component.uploadFile("post/", image);
  }

  private void checkIfPostWriter(String id, Post post) {
    if (!id.equals(post.getAccount().getId())) {
      throw new CustomException(ErrorCode.UNABLE_TO_DELETE_POST);
    }
  }

  private void deleteRelatedTags(Post post) {
    List<Tag> relatedTags
        = tagRepository.findByContentTypeAndContentNum(ContentType.POST, post.getPostNum());
    tagRepository.deleteAll(relatedTags);
  }

  private boolean checkIfHiddenContent(String id, String writerAccountId) {
    Account readerAccount = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Account writerAccount = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return !readerAccount.equals(writerAccount)
        && writerAccount.getIsProtected() && !checkIfFollower(writerAccount, readerAccount);
  }

  private boolean checkIfHiddenPost(String id, Post post) {
    Account readerAccount = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return !readerAccount.equals(post.getAccount())
    && post.getAccount().getIsProtected() && !checkIfFollower(post.getAccount(), readerAccount);
  }

  private boolean checkIfFollower(Account writerAccount, Account readerAccount) {
    return followRepository
        .existsByFollowingAndFollowedAndState(readerAccount, writerAccount, FollowState.ACCEPTED);
  }

  private SeePostResponseDto hideProtectedPost(String id, Post post) {
    SeePostResponseDto response;
    if (checkIfHiddenPost(id, post)) {
      response = SeePostResponseDto.fromEntityClosed(post);
    } else {
      response = SeePostResponseDto.fromEntity(post);
    }
    return response;
  }

  private List<CommentResponseDto> hideProtectedComments(String id, List<Comment> comments) {
    List<CommentResponseDto> commentResponse = new ArrayList<>();
    for (Comment comment : comments) {
      if (checkIfHiddenComment(id, comment)) {
        commentResponse.add(CommentResponseDto.fromEntityClosed(comment));
      } else {
        commentResponse.add(CommentResponseDto.fromEntity(comment));
      }
    }
    return commentResponse;
  }

  private SeeCommentResponseDto hideProtectedComment(String id, Comment comment) {
    SeeCommentResponseDto response;
    if (checkIfHiddenComment(id, comment)) {
      response = SeeCommentResponseDto.fromEntityClosed(comment);
    } else {
      response = SeeCommentResponseDto.fromEntity(comment);
    }
    return response;
  }

  private List<Account> getSelfAndFollowingList(Account account) {
    List<Account> accountList = new ArrayList<>(
        followRepository.findByFollowingAndState(account, FollowState.ACCEPTED)
            .stream().map(Follow::getFollowed).toList());
    accountList.add(account);
    return accountList;
  }

  private void extractAndSaveAllTags(Post post) {
    List<Tag> tags = new ArrayList<>();
    for(String tag: TagUtil.getIds(post.getText())) {
      Account taggedAccount = accountRepository.findById(tag.substring(1))
          .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
      tags.add(Tag.builder()
          .taggedAccount(taggedAccount)
          .contentType(ContentType.POST)
          .contentNum(post.getPostNum())
          .build());
    }
    tagRepository.saveAll(tags);
  }

  private void extractAndSaveAllTags(Comment comment) {
    List<Tag> tags = new ArrayList<>();
    tagOriginalPostWriter(tags, comment);

    for(String tag: TagUtil.getIds(comment.getText())) {
      Account taggedAccount = accountRepository.findById(tag.substring(1))
          .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));

      tags.add(Tag.builder()
          .taggedAccount(taggedAccount)
          .contentType(ContentType.COMMENT)
          .contentNum(comment.getCommentNum())
          .build());
    }
    tagRepository.saveAll(tags);
  }

  private void tagOriginalPostWriter(List<Tag> tags, Comment comment) {
    tags.add(Tag.builder()
        .taggedAccount(comment.getPost().getAccount())
        .contentType(ContentType.COMMENT)
        .contentNum(comment.getCommentNum())
        .build());
  }

  private String uploadCommentImageIfExists(MultipartFile image) {
    if (image == null) {
      return null;
    }
    return s3Component.uploadFile("comment/", image);
  }

  private void checkIfCommentWriter(String id, Comment comment) {
    if (!id.equals(comment.getAccount().getId())) {
      throw new CustomException(ErrorCode.UNABLE_TO_DELETE_COMMENT);
    }
  }

  private void deleteRelatedTags(Comment comment) {
    List<Tag> relatedTags
        = tagRepository.findByContentTypeAndContentNum(ContentType.COMMENT, comment.getCommentNum());
    tagRepository.deleteAll(relatedTags);
  }

  private boolean checkIfHiddenComment(String id, Comment comment) {
    Account readerAccount = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return !readerAccount.equals(comment.getAccount())
        && comment.getAccount().getIsProtected() && !checkIfFollower(comment.getAccount(), readerAccount);
  }

}