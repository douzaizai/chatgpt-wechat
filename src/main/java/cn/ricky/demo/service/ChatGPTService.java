package cn.ricky.demo.service;

import cn.ricky.demo.configuration.MyConfig;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "caffeineCacheManager")
public class ChatGPTService {

  @Autowired
  private MyConfig myConfig;

  private OpenAiService service;

  @PostConstruct
  public void init() {
    service = new OpenAiService(myConfig.getOpenAIToken(), Duration.ofSeconds(myConfig.getOpenAITimeout()));
  }

  public String chat(String model, String prompt, String user) {
    StringBuilder result = new StringBuilder();
    try {
      CompletionRequest completionRequest = CompletionRequest.builder()
              .model(model)
              .prompt(prompt)
              .temperature(myConfig.getOpenAITemperature())
              .maxTokens(myConfig.getGptMaxTokens())
              .n(1)
              //.stream(true)
              //.frequencyPenalty(0.0)
              //.echo(true)
              .user(user).build();
      List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
      for (CompletionChoice choice : choices) {
        result.append(choice.getText().replaceAll("\n\n", ""));
        log.info("choice:{}", choice);
      }
    } catch (Exception e) {
      log.error("chat failed", e);
      result.append("Oops,ChatGPT累了,你也休息一下吧");
    }
    return result.toString();
  }

  @Cacheable(key = "#prompt+':'+#user")
  public String chat(String prompt, String user) {
    return this.chat(myConfig.getOpenAIModel(), prompt, user);
  }
}
