package com.christmas.letter.sender.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController {

  @GetMapping("/text")
  public String sample() {
    return "sample";
  }

}
