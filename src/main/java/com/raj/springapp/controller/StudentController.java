package com.raj.springapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raj.springapp.model.Student;
import com.raj.springapp.repository.StudentRepository;

@RestController
public class StudentController {

  private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

  @Autowired StudentRepository studentRepository;

  @Autowired private QueueMessagingTemplate queueMessagingTemplate;

  @Value("${cloud.aws.end-point.uri}")
  private String awsSQSStandardQueue;

  @RequestMapping("/sendMessageToSQS")
  public void sendMessage() {
    queueMessagingTemplate.send(
        awsSQSStandardQueue,
        MessageBuilder.withPayload("Hi I'm excited to send message to AWS SQS").build());
  }

  @SqsListener("request.fifo")
  public void getMessage(String message) {
    studentRepository.save(new Student(message));
  }

  @RequestMapping(value = "/all")
  public List<Student> getAll() {
    return studentRepository.findAll();
  }
}
