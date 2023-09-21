package org.example.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WordAndCountDto {

    private long id;

    private String word;

    private int count;
}
