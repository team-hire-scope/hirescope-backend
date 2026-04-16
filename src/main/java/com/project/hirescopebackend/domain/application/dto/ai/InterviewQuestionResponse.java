package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterviewQuestionResponse {

    private String question;
    private String intent;

    @JsonProperty("answer_guide")
    private String answerGuide;
}
