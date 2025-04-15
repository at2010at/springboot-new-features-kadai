package com.example.samuraitravel.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewForm {
    @NotNull(message = "評価を選択してください")
    @Min(1)
    @Max(5)
    private Integer star;

    @NotBlank(message = "コメントを入力してください")
    private String reviewComment;

    // getter / setter
}
