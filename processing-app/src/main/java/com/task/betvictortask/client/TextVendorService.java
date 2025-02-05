package com.task.betvictortask.client;

import com.task.betvictortask.aop.Timed;
import com.task.betvictortask.constants.ParagraphSize;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TextVendorService {

    //TODO improve this, add exception handler, stop the process and return error if any fetch failed
    private final RestTemplate restTemplate = new RestTemplate();

    @Timed
    public List<String> fetchTexts(int count, ParagraphSize size) {
        log.info("Starting text fetch with count:'{}' and paragraph size:'{}'", count, size);
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(count, 10));

        try {
            List<CompletableFuture<String>> futures = new ArrayList<>();

            for (int i = 1; i <= count; i++) {
                int finalI = i;
                futures.add(CompletableFuture.supplyAsync(() -> {
                    log.debug("Fetching text '{}' from API", finalI);
                    return fetchText(finalI, size);
                }, executor));
            }

            List<String> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            log.info("Successfully fetched '{}' texts", results.size());
            return results;
        } catch (Exception e) {
            log.error("Error occurred while fetching texts", e);
            throw e;
        } finally {
            executor.shutdown();
            log.debug("Executor service is shut down");
        }
    }

    private String fetchText(int id, ParagraphSize size) {
        String url = String.format("https://loripsum.net/api/%d/%s", id, size.name());
        log.debug("Fetching text from URL:'{}'", url);

        try {
            String response = restTemplate.getForObject(url, String.class);
            log.debug("Successfully fetched text '{}'", id);
            return response;
        } catch (RestClientException e) {
            log.error("Failed to fetch text '{}' from API.", id, e);
            return "Failed to fetch for endpoint " + id;
        }
    }
}