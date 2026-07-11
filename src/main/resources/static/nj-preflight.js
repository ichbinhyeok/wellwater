(function () {
    var root = document.querySelector("[data-nj-preflight-root]");
    if (!root) {
        return;
    }

    var started = false;
    var form = root.querySelector('form[action="/nj-well-preflight/result"]');

    function payload(eventName) {
        var params = new URLSearchParams();
        params.set("eventName", eventName);
        params.set("channel", root.dataset.channel || "direct");
        params.set("source", root.dataset.source || "main");
        params.set("partnerSlug", root.dataset.partnerSlug || "");
        params.set("municipalitySlug", root.dataset.municipalitySlug || "");
        return params;
    }

    function send(eventName) {
        var params = payload(eventName);
        if (navigator.sendBeacon) {
            navigator.sendBeacon("/nj-well-preflight/event", params);
            return;
        }
        fetch("/nj-well-preflight/event", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" },
            body: params.toString(),
            keepalive: true,
            credentials: "same-origin"
        }).catch(function () {});
    }

    function markStarted() {
        if (started) {
            return;
        }
        started = true;
        send("tool_started");
    }

    send("landing_view");
    if (form) {
        form.addEventListener("focusin", markStarted, { once: true });
        form.addEventListener("change", markStarted, { once: true });
    }
})();
