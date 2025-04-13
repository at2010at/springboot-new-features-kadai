package com.example.samuraitravel.entity;


import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reviews") //テーブルと紐づけ
@Data //ゲッターやセッターが自動定義される

public class Review {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")//対応させるカラム名
    private Integer id;

    //　表示中の物件と一致するコメントを抽出
    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;    

	//  user_id 編集や削除に使用
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "name")
    private String name;

    @Column(name = "star")
    private Integer star;
    
    @Column(name = "review_date", insertable = false, updatable = false)//アプリ側から更新不可
    private Timestamp reviewDate;

    @Column(name = "review_comment")
    private String reviewComment;
    
}

//@ManyToOne
//@JoinColumn(name = "house_id")
//private House houseId;

//@Column(name = "review_updated_date", insertable = false, updatable = false)
//private Timestamp reviewUpdatedDate;
//private House houseId;
//private User userId;
//会員登録用の本名とレビュー用のニックネームは別でいい？