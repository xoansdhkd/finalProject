package com.team2.backend.domain.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="notice_file")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Builder
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="imageNo")
    private Long imageNo;

    @Column(name="able", columnDefinition = "varchar(1) default 'Y'")
    private String able;

    @ManyToOne(targetEntity = Notice.class)
    @JoinColumn(name="noticeNo", insertable = false, updatable = false)
    private Notice notice;
    @Column(name="noticeNo")
    private Long noticeNo;

    @Column(name="path")
    private String path;

    @Column(name="type")
    private String type;

    @Column(name="imageSize")
    private String imageSize;

    @CreatedDate
    @Column(name="createAt")
    private LocalDateTime createAt;

}
