package com.group33.swa.webController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/** IndexController Class */
@Controller
public class IndexController {

  /**
   * Index Mapper on requesting root "/"
   *
   * @return {@link String}
   */
  @RequestMapping("/")
  public String index() {
    System.out.println("Looking in the index controller.........");
    return "index.html";
  }
}
