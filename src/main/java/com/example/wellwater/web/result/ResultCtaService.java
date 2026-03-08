package com.example.wellwater.web.result;

import com.example.wellwater.decision.model.CtaLink;
import com.example.wellwater.decision.model.DecisionResult;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ResultCtaService {

    public List<RenderableCta> renderableCtas(DecisionResult result, String sessionId, String slug) {
        return result.ctas().stream()
                .map(cta -> toRenderableCta(cta, result, sessionId, slug))
                .toList();
    }

    private RenderableCta toRenderableCta(CtaLink cta, DecisionResult result, String sessionId, String slug) {
        String outboundUrl = UriComponentsBuilder.fromPath("/tool/out")
                .queryParam("target", cta.targetUrl())
                .queryParam("ctaType", cta.type())
                .queryParam("entryMode", result.entryMode().wireValue())
                .queryParam("sessionId", sessionId)
                .queryParam("tier", result.tier().label())
                .queryParam("branch", result.branch().label())
                .queryParam("slug", slug == null ? "" : slug)
                .build()
                .toUriString();
        return new RenderableCta(cta.type(), cta.label(), outboundUrl);
    }
}
