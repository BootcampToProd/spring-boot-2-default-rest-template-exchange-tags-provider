package com.bootcamptoprod.defaultresttemplateexchangetagsprovider.config;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.metrics.web.client.DefaultRestTemplateExchangeTagsProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Bootcamp to prod extended rest template exchange tags provider.
 * Useful for adding our own additional tags in rest template metrics
 */
@Component
public class BootcampToProdExtendedRestTemplateExchangeTagsProvider extends DefaultRestTemplateExchangeTagsProvider {
    @Override
    public Iterable<Tag> getTags(String urlTemplate, HttpRequest request, ClientHttpResponse response) {
        Iterable<Tag> defaultTags = super.getTags(urlTemplate, request, response);

        Tags tags = Tags.of(defaultTags);

        // Extracting query parameters from URI
        String uri = request.getURI().toString();
        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

        // Optional tag which will be present in metrics only when the condition is evaluated to true
        if (parameters.containsKey("id")) {
            tags = tags.and(Tag.of("userId", parameters.get("id").get(0)));
        }

        // Custom tag which will be present in all the controller metrics
        tags = tags.and(Tag.of("tag", "value"));

        return tags;
    }

}
