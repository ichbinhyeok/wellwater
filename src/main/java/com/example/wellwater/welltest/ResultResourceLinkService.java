package com.example.wellwater.welltest;

import com.example.wellwater.decision.registry.StateResourceRegistryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class ResultResourceLinkService {

    private static final Set<String> ALLOWED_KINDS = Set.of("official_guidance", "certified_lab");
    private static final Set<String> ALLOWED_FAMILIES = Set.of("baseline", "expanded", "certified_urgent", "transaction");
    private final String siteBaseUrl;
    private final Set<String> allowedOutboundHosts;

    public ResultResourceLinkService(
            @Value("${app.site.base-url:https://waterverdict.com}") String siteBaseUrl,
            StateResourceRegistryService stateResourceRegistryService
    ) {
        this.siteBaseUrl = trimTrailingSlash(siteBaseUrl);
        LinkedHashSet<String> hosts = new LinkedHashSet<>(stateResourceRegistryService.allowedOutboundHosts());
        hosts.add("www.epa.gov");
        this.allowedOutboundHosts = Set.copyOf(hosts);
    }

    public PlanResourceLink tracked(PlanResourceLink target, String kind, String channel, String resultFamily) {
        if (!ALLOWED_KINDS.contains(kind)) {
            return target;
        }
        String targetUrl = target == null ? "" : target.url();
        boolean usedFallback = false;
        if (targetUrl.isBlank() || resolve(encode(targetUrl)).isEmpty()) {
            targetUrl = fallback(kind);
            usedFallback = true;
        }
        String label = usedFallback || target == null || target.label() == null || target.label().isBlank()
                ? ("certified_lab".equals(kind) ? "Find a certified drinking-water lab" : "EPA private-well guidance")
                : target.label();
        String safeChannel = "chatgpt".equalsIgnoreCase(channel) ? "chatgpt" : "web";
        String safeFamily = ALLOWED_FAMILIES.contains(resultFamily) ? resultFamily : "baseline";
        String url = siteBaseUrl + "/out/resource/" + kind + "/" + encode(targetUrl)
                + "?source=" + safeChannel + "&family=" + safeFamily;
        return new PlanResourceLink(label, url);
    }

    public Optional<String> resolve(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            URI uri = URI.create(decoded);
            String host = uri.getHost();
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || host == null
                    || host.isBlank()
                    || uri.getRawUserInfo() != null
                    || (uri.getPort() != -1 && uri.getPort() != 443)
                    || !allowedOutboundHosts.contains(host.toLowerCase(Locale.ROOT))) {
                return Optional.empty();
            }
            return Optional.of(uri.toString());
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    public boolean isAllowedKind(String kind) {
        return ALLOWED_KINDS.contains(kind);
    }

    public String safeFamily(String family) {
        return ALLOWED_FAMILIES.contains(family) ? family : "baseline";
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String fallback(String kind) {
        return "certified_lab".equals(kind)
                ? "https://www.epa.gov/dwlabcert/contact-information-certification-programs-and-certified-laboratories-drinking-water"
                : "https://www.epa.gov/privatewells";
    }

    private String trimTrailingSlash(String value) {
        String normalized = value == null || value.isBlank() ? "https://waterverdict.com" : value.trim();
        try {
            URI uri = URI.create(normalized);
            if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null || uri.getHost().isBlank()) {
                normalized = "https://waterverdict.com";
            }
        } catch (IllegalArgumentException ignored) {
            normalized = "https://waterverdict.com";
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
