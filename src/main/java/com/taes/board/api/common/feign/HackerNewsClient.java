package com.taes.board.api.common.feign;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "hacker-news-client", url = "${feign.client.clients.hacker-news.url}")
public interface HackerNewsClient
{
    @GetMapping(value = "/newstories.json")
    List<Integer> getNewStories();


    @GetMapping(value = "/item/{itemId}.json")
    NewsItem getNewsItemById(@PathVariable("itemId") Integer itemId);

    @Getter
    @NoArgsConstructor
    class NewsItem
    {
        private String by;
        private Integer descendants;
        private Integer id;
        private Integer score;
        private Long time;
        private String title;
        private String type;
        private String url;

        public NewsItem(String by, Integer descendants, Integer id, Integer score, Long time, String title, String type, String url)
        {
            this.by = by;
            this.descendants = descendants;
            this.id = id;
            this.score = score;
            this.time = time;
            this.title = title;
            this.type = type;
            this.url = url;
        }
    }

}
