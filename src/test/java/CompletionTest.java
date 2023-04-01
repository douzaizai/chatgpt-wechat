import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CompletionTest {
  String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiI4Mzg3ODgyNDBAcXEuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWV9LCJodHRwczovL2FwaS5vcGVuYWkuY29tL2F1dGgiOnsidXNlcl9pZCI6InVzZXItd2Z1ZEpXWHJXQ3docFhuTTNYUnQ0RklUIn0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJhdXRoMHw2M2UxYzFhZTAxNzY1NDdhY2RmZGI3ZTQiLCJhdWQiOlsiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS92MSIsImh0dHBzOi8vb3BlbmFpLm9wZW5haS5hdXRoMGFwcC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNjgwMzU2MjQ0LCJleHAiOjE2ODE1NjU4NDQsImF6cCI6IlRkSkljYmUxNldvVEh0Tjk1bnl5d2g1RTR5T282SXRHIiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBtb2RlbC5yZWFkIG1vZGVsLnJlcXVlc3Qgb3JnYW5pemF0aW9uLnJlYWQgb2ZmbGluZV9hY2Nlc3MifQ.zUFIxxCpSWf-Laj7MSmJB9N-gKinwGmGrV-67ftCMht4nWfDwB2SEx05KN34tNwZz_gZ3Zscwj4imDOdFCFAcLnKwWzOgYcP93X6BN7xKq0Kj-ETp4EkrcBVUj14G5KHHDjby-8k33FlUuei1P1wzalLmUAwxieZIcT1URAjB0IxtJEwgB9nJijNEk4DYkIS3GIFdFttVyZe1A9yQcZ1h70Eq10i5cwQ00IeQY1R8NfXAXVZ_67U0OmFVUgFI2lminEwGROfDfHnhOurtIu-_Vm_FH-7hKztZEv7xWSyDzi4vweoMmzHnA4-KhCriko4dxIla9649jBjTNxvh1jFUA";

  OpenAiService service = new OpenAiService(token);

  @Test
  void createCompletion() {
    CompletionRequest completionRequest = CompletionRequest.builder()
            .model("text-davinci-003")
            .prompt("what is the day today")
            .temperature(0.9)
            //.echo(true)
            .n(1)
            .maxTokens(200)
            .user("ricky")
            //.logitBias(new HashMap<>())
            .build();

    List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
    assertEquals(1, choices.size());
  }
}
