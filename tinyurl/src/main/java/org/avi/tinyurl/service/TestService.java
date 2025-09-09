package org.avi.tinyurl.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UrlShortenerService service;

    @Scheduled(fixedRate = 10000)
    public void test() {

    }

    @Transactional()
    public void myTransaction() {

        // Trasctional 1

        // Trasaction2

        newtrasaction();
    }

    @Transactional(propagation = Transactional.Propagation.REQUIRES_NEW)
    public void newtrasaction() {

    }
}
