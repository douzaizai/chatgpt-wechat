package cn.ricky.demo.service;

import cn.ricky.demo.configuration.MyConfig;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class ChatGPTService {

  @Autowired
  private MyConfig myConfig;

  public String chat(String model, String prompt, String user) {
    StringBuilder result = new StringBuilder();
    try {
      OpenAiService service = new OpenAiService(myConfig.getOpenAIToken(), Duration.ofSeconds(60000));
      CompletionRequest completionRequest = CompletionRequest.builder()
              .model(model)
              .prompt(prompt)
              .temperature(0.9)
              .maxTokens(myConfig.getGptMaxTokens())
              .n(1)
              //.stream(true)
              //.frequencyPenalty(0.0)
              //.echo(true)
              .user(user).build();
      List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
      for (CompletionChoice choice : choices) {
        result.append(choice.getText()).append("\n");
        log.info("choice:" + choice);
      }
    } catch (Exception e) {
      log.error("chat failed", e);
      result.append("Oops,ChatGPT累了,你也休息一下吧");
    }
    return result.toString();
  }

  public String chat(String prompt, String user) {
    return this.chat(myConfig.getOpenAIModel(), prompt, user);
  }
}
