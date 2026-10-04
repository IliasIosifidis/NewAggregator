package io.github.iliasIosifidis.newsingestor.source.hackerNews;

record HackerNewsItem(
        Long id,
        Long time,
        String title,
        String type,
        String url,
        Boolean deleted,
        Boolean dead) {}
