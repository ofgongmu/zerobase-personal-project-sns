# 단문형 SNS 구현 프로젝트 

다수의 유저들이 서로 짧게 생각과 일상을 나눌 수 있는 SNS 서비스입니다. 

## 프로젝트 기능 및 설계 

### 사용자
    - 회원 가입 
        - 이메일, 아이디, 비밀번호를 통해 회원가입을 진행한다. 
        - 이메일 인증을 거쳐 계정을 활성화시킨다. 
    - 로그인 
        - 이메일 혹은 아이디와 비밀번호를 받아 로그인하고, 성공 시 JWT 토큰을 발급한다.
    - 계정 설정 
        - 가입한 사용자는 프로필 사진, 닉네임, 아이디, 바이오를 설정할 수 있다. 
        - 팔로우 여부와 무관하게 포스트를 누구나 볼 수 있는 공개 계정과 팔로우된 사용자만 포스트를 열람할 수 있는 비공개 계정 여부를 설정할 수 있다. 
    - 계정간 팔로우 및 팔로우 해제
        - 타 사용자의 계정을 팔로우(구독)하거나, 팔로우를 해제할 수 있다. 
    - 계정 검색 
        - 아이디와 닉네임을 통해 계정을 검색할 수 있다. 
    - 복수 계정 연결
        - 한 사용자는 여러 개의 계정을 생성할 수 있다.
        - 해당 계정들은 연동되며, 사용자는 계정들 사이를 자유롭게 교체할 수 있다. 
### 피드
    - 피드
        - 기본적으로 팔로우된 사용자만의 포스트를 시간 역순 조회한다.
        - 한 번에 보여지는 포스트의 수를 제한하기 위해 페이징 처리한다.
        - 대용량 트래픽 발생 시 이를 처리하기 위한 방안을 마련한다. 
    - 포스트 
        - 포스트는 텍스트 150자로 제한한다. 
        - 포스트는 작성하거나 삭제할 수 있다.
    - 댓글
        - 댓글은 텍스트 150자로 제한한다. 
        - 포스트에 대해 댓글을 작성하거나 삭제할 수 있다. 
    - 이미지 첨부 [Amazon S3]
      - 포스트 혹은 댓글에는 최대 한 장의 이미지를 첨부할 수 있다.
    - 링크 첨부
        - 포스트 혹은 댓글에는 링크를 첨부할 수 있다.
        - @사용자 형식의 태그 기능 또한 포함되어, 해당 태그를 누르면 계정 페이지로 이동한다.
        - 댓글의 경우 포스트의 작성자를 자동으로 태그하게 된다. 
    - 알림 [Kafka]
        - 자신을 태그한 포스트 혹은 댓글이 있을 경우 즉각적인 알림이 발생한다. 
    - 포스트 검색 
        - 키워드가 포함된 포스트를 검색할 수 있다. [Elasticsearch]
### 디엠
    - 메시지 [Websocket]
        - 타인의 피드에 나타나지 않는, 독립된 공간에서 주고받는 메시지를 의미한다.
        - 메시지는 작성, 조회(시간 역순)할 수 있다. 
        - 각 디엠방에는 계정을 최대 10개까지 초대할 수 있다.
        - 각 계정은 자유롭게 디엠방을 퇴장할 수 있다. 
    - 이미지 첨부 [Amazon S3]
        - 디엠을 통해 이미지를 전송할 수 있다. 
    - 링크 첨부
        - 디엠을 통해 링크를 전송할 수 있다. 
        - 단, 포스트와는 다르게 태그를 하더라도 알림이 발생하지 않는다.
    - 알림 [Kafka]
        - 디엠을 수신하게 될 경우 즉각적인 알림이 발생한다. 
### 광고
    - 광고 계정
        - 광고 계정의 경우 그 여부를 설정해야 한다. 
        - 광고 계정으로 설정하기를 원할 경우, 지불 수단을 연결해야 한다. [PortOne]
    - 광고 계정 포스트의 임의 노출 
        - 광고 계정으로 등록된 계정의 포스트는 팔로우되지 않았더라도 피드에 임의 노출된다. 

## ERD
![sns.png](doc%2Fsns.png)

## 트러블 슈팅
트러블 슈팅 내역은 [이 블로그](velog.io/@ofgongmu)에 작성합니다. 

## 기술 스택
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)