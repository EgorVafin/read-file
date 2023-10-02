package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.WordStatEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UrlParser {
    private String text;

    public String parse(String url) {
        //как правильно проверять плохие URL?
        try {
            Document doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000).get();

            text = doc.body().text();
            //как выводить сообщения об ошибке?
        } catch (IOException e) {
            System.out.println("НЕ удалось прочитать данные по указанному url");
            return null;
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("НЕ удалось прочитать данные по указанному url");
            return null;
        }

        return text;
    }
}
