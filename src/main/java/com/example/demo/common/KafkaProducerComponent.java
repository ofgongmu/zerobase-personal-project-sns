package com.example.demo.common;

import com.example.demo.entity.Comment;
import com.example.demo.entity.DM;
import com.example.demo.entity.Post;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducerComponent {
  private final KafkaTemplate<String, String> kafkaTemplate;

  private final String tagTopic = "tagNotification";
  private final String dmTopic = "dmNotification";

  public String sendTagNotification(Post post, String receiverId) {
    String text = post.getAccount().getNickname() + ": " + post.getText();
    ProducerRecord<String, String> record
        = new ProducerRecord<>(tagTopic, receiverId, text);
    kafkaTemplate.send(record);
    return text;
  }

  public String sendTagNotification(Comment comment, String receiverId) {
    String text = comment.getAccount().getNickname() + ": " + comment.getText();
    ProducerRecord<String, String> record
        = new ProducerRecord<>(tagTopic, receiverId, text);
    kafkaTemplate.send(record);
    return text;
  }

  public String sendDMNotification(DM dm, String receiverId) {
    String text = dm.getAccount().getNickname() + ": " + dm.getText();
    ProducerRecord<String, String> record
        = new ProducerRecord<>(dmTopic, receiverId, text);
    kafkaTemplate.send(record);
    return text;
  }
}
