package in.tech_camp.pictweet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.pictweet.custom_user.CustomUserDetail;
import in.tech_camp.pictweet.entity.TweetEntity;
import in.tech_camp.pictweet.form.TweetForm;
import in.tech_camp.pictweet.repository.TweetRepository;
import in.tech_camp.pictweet.validation.ValidationOrder;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class TweetController {

  private final TweetRepository tweetRepository;

  @GetMapping("/")
  public String showIndex(Model model) {
        List<TweetEntity> tweets = tweetRepository.findAll();
        model.addAttribute("tweets", tweets);
        return "tweets/index";
  }

  @GetMapping("/tweets/new")
  public String showTweetNew(Model model){
    model.addAttribute("tweetForm", new TweetForm());
    return "tweets/new";
  }

 @PostMapping("/tweets")
  public String createTweet(@ModelAttribute("tweetForm") @Validated(ValidationOrder.class) TweetForm tweetForm,
                            BindingResult result, 
                            @AuthenticationPrincipal CustomUserDetail currentUser,
                            Model model) {

    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("tweetForm", tweetForm);
      return "tweets/new";
    }

    TweetEntity tweet = new TweetEntity();
    tweet.setUserId(currentUser.getId());
    tweet.setName(tweetForm.getName());
    tweet.setText(tweetForm.getText());
    tweet.setImage(tweetForm.getImage());
      
    try {
      tweetRepository.insert(tweet);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }

    return "redirect:/";
  }

  @PostMapping("/tweets/{tweetId}/delete")
  public String deleteTweet(@PathVariable("tweetId") Integer tweetId) {
    try {
      tweetRepository.deleteById(tweetId);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }
    return "redirect:/";
  }
  @GetMapping("/tweets/{tweetId}/edit")
  public String editTweet(@PathVariable("tweetId") Integer tweetId, Model model) {
    TweetEntity tweet = tweetRepository.findById(tweetId);

    TweetForm tweetForm = new TweetForm();
    tweetForm.setName(tweet.getName());
    tweetForm.setText(tweet.getText());
    tweetForm.setImage(tweet.getImage());

    model.addAttribute("tweetForm", tweetForm);
    model.addAttribute("tweetId", tweetId);
    return "tweets/edit";
  }

  @PostMapping("/tweets/{tweetId}/update")
  public String updateTweet(@ModelAttribute("tweetForm") @Validated TweetForm tweetForm,
                            BindingResult result,
                            @PathVariable("tweetId") Integer tweetId,
                            Model model) {

    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);

      model.addAttribute("tweetForm", tweetForm);
      model.addAttribute("tweetId", tweetId);
      return "tweets/edit";
    }

    TweetEntity tweet = tweetRepository.findById(tweetId);
    tweet.setName(tweetForm.getName());
    tweet.setText(tweetForm.getText());
    tweet.setImage(tweetForm.getImage());

    try {
      tweetRepository.update(tweet);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }

    return "redirect:/";
  }

  @GetMapping("/tweets/{tweetId}")
  public String showTweetDetail(@PathVariable("tweetId") Integer tweetId, Model model) {
      TweetEntity tweet = tweetRepository.findById(tweetId);
      model.addAttribute("tweet", tweet);
      return "tweets/detail";
  }
}

