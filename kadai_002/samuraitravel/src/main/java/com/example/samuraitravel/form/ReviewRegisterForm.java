package com.example.samuraitravel.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRegisterForm {
    @NotBlank(message = "評価を入力してください。")
    @Range(min = 1, max = 5, message = "評価は1～5のいずれかを選択してください。")
    private Integer star;
        
//    private MultipartFile imageFile;
    
    @NotBlank(message = "コメントを入力してください。（300文字まで）")
    @Length(max = 300, message = "コメントは300文字以内で入力してください。")
    private String reviewComment;   
    
//    @NotNull(message = "宿泊料金を入力してください。")
//    @Min(value = 1, message = "宿泊料金は1円以上に設定してください。")
//    private Integer price;  
//    
//    @NotNull(message = "定員を入力してください。")
//    @Min(value = 1, message = "定員は1人以上に設定してください。")
//    private Integer capacity;     
//    
//    @NotBlank(message = "郵便番号を入力してください。")
//    private String postalCode;
//    
//    @NotBlank(message = "住所を入力してください。")
//    private String address;
//    
//    @NotBlank(message = "電話番号を入力してください。")
//    private String phoneNumber;
}
