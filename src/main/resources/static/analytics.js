(function () {
    function sanitizeValue(value) {
        if (value === null || value === undefined) {
            return undefined;
        }
        if (typeof value === "number") {
            return Number.isFinite(value) ? value : undefined;
        }
        if (typeof value === "boolean") {
            return value ? "true" : "false";
        }

        var stringValue = String(value).trim();
        if (!stringValue) {
            return undefined;
        }
        return stringValue.substring(0, 100);
    }

    function sanitizeParams(params) {
        var sanitized = {};
        if (!params) {
            return sanitized;
        }

        Object.keys(params).forEach(function (key) {
            var value = sanitizeValue(params[key]);
            if (value !== undefined) {
                sanitized[key] = value;
            }
        });

        return sanitized;
    }

    function hasGtag() {
        return typeof window.gtag === "function";
    }

    function track(eventName, params, options) {
        var sanitizedName = sanitizeValue(eventName);
        var callback = options && typeof options.onDone === "function" ? options.onDone : null;

        if (!sanitizedName) {
            if (callback) {
                callback();
            }
            return;
        }

        var payload = sanitizeParams(params);
        if (!hasGtag()) {
            if (callback) {
                callback();
            }
            return;
        }

        var completed = false;
        var done = function () {
            if (completed) {
                return;
            }
            completed = true;
            if (callback) {
                callback();
            }
        };

        if (callback) {
            payload.event_callback = done;
        }

        window.gtag("event", sanitizedName, payload);

        if (callback) {
            window.setTimeout(done, options && typeof options.timeoutMs === "number" ? options.timeoutMs : 350);
        }
    }

    function currentPath() {
        return window.location.pathname || "/";
    }

    function surfaceFromPath(pathname) {
        if (!pathname || pathname === "/") {
            return "home";
        }
        if (pathname.indexOf("/result/saved/") === 0) {
            return "saved_result";
        }
        if (pathname.indexOf("/tool/") === 0) {
            return pathname === "/tool/result" ? "tool_result" : "tool_entry";
        }
        if (pathname.indexOf("/well-water/family/") === 0) {
            return "pseo_family";
        }
        if (pathname.indexOf("/well-water/") === 0) {
            return "pseo_detail";
        }
        if (pathname.indexOf("/trust") === 0) {
            return "trust";
        }
        return "page";
    }

    function entryModeFromPath(pathname) {
        if (!pathname) {
            return "";
        }
        if (pathname.indexOf("/tool/result-first") === 0) {
            return "result-first";
        }
        if (pathname.indexOf("/tool/symptom-first") === 0) {
            return "symptom-first";
        }
        if (pathname.indexOf("/tool/trigger-first") === 0) {
            return "trigger-first";
        }
        return "";
    }

    function readValue(form, name) {
        var field = form.querySelector('[name="' + name + '"]');
        if (!field) {
            return "";
        }
        return (field.value || "").trim();
    }

    function firstNonEmpty(values) {
        for (var i = 0; i < values.length; i += 1) {
            if (values[i]) {
                return values[i];
            }
        }
        return "";
    }

    function hasCheckedValues(form, name) {
        return form.querySelectorAll('[name="' + name + '"]:checked').length > 0;
    }

    function hasCompanionReportLines(form) {
        var fields = form.querySelectorAll('[name^="companionLines["]');
        for (var i = 0; i < fields.length; i += 1) {
            if ((fields[i].value || "").trim()) {
                return true;
            }
        }
        return false;
    }

    function buildToolFormParams(form) {
        var analyte = readValue(form, "analyteName");
        var symptom = readValue(form, "symptomFlag");
        var trigger = readValue(form, "triggerFlag");
        var entryMode = readValue(form, "entryMode") || entryModeFromPath(currentPath());
        var signalType = "";

        if (analyte) {
            signalType = "analyte";
        } else if (symptom) {
            signalType = "symptom";
        } else if (trigger) {
            signalType = "trigger";
        }

        return {
            entry_mode: entryMode,
            signal_type: signalType,
            signal_value: firstNonEmpty([analyte, symptom, trigger]),
            state_selected: readValue(form, "state"),
            use_scope: readValue(form, "useScope"),
            sample_source: readValue(form, "sampleSource"),
            lab_certified: readValue(form, "labCertified"),
            has_existing_treatment: hasCheckedValues(form, "existingTreatments"),
            has_supporting_signals: hasCheckedValues(form, "supportingSignals"),
            has_companion_lines: hasCompanionReportLines(form),
            source_surface: surfaceFromPath(currentPath())
        };
    }

    function buildLeadFormParams(form) {
        return {
            source_type: readValue(form, "sourceType"),
            source_family: readValue(form, "sourceFamily"),
            state_selected: readValue(form, "state"),
            has_note: !!readValue(form, "note"),
            source_surface: surfaceFromPath(currentPath())
        };
    }

    function getResultContext() {
        var root = document.querySelector("[data-analytics-result-root]");
        if (!root) {
            return null;
        }

        return sanitizeParams({
            entry_mode: root.dataset.entryMode,
            result_branch: root.dataset.branch,
            result_tier: root.dataset.tier,
            problem_type: root.dataset.problemType,
            shared_view: root.dataset.sharedView,
            source_surface: surfaceFromPath(currentPath())
        });
    }

    function safeUrl(href) {
        try {
            return new URL(href, window.location.origin);
        } catch (error) {
            return null;
        }
    }

    function isPlainLeftClick(event) {
        return event.button === 0 && !event.metaKey && !event.ctrlKey && !event.shiftKey && !event.altKey;
    }

    function targetHost(target) {
        if (!target) {
            return "";
        }
        var url = safeUrl(target);
        return url ? url.hostname : "";
    }

    function initToolEntryViews() {
        var forms = document.querySelectorAll('form[action="/tool/result"]');
        if (forms.length === 0) {
            return;
        }

        var form = forms[0];
        track("tool_entry_viewed", buildToolFormParams(form));
    }

    function initResultView() {
        var resultContext = getResultContext();
        if (!resultContext) {
            return;
        }

        track("decision_result_viewed", resultContext);
    }

    function initLeadFeedbackView() {
        var searchParams = new URLSearchParams(window.location.search);
        var leadStatus = searchParams.get("lead");
        if (!leadStatus) {
            return;
        }

        track("lead_submission_feedback_viewed", {
            lead_status: leadStatus,
            source_surface: surfaceFromPath(currentPath())
        });
    }

    function initFormTracking() {
        var startedForms = new WeakSet();

        function markStarted(form, eventName, builder) {
            if (!form || startedForms.has(form)) {
                return;
            }
            startedForms.add(form);
            track(eventName, builder(form));
        }

        document.addEventListener("focusin", function (event) {
            var toolForm = event.target.closest('form[action="/tool/result"]');
            if (toolForm) {
                markStarted(toolForm, "tool_form_started", buildToolFormParams);
                return;
            }

            var leadForm = event.target.closest('form[action="/lead/submit"]');
            if (leadForm) {
                markStarted(leadForm, "lead_form_started", buildLeadFormParams);
            }
        });

        document.addEventListener("change", function (event) {
            var toolForm = event.target.closest('form[action="/tool/result"]');
            if (toolForm) {
                markStarted(toolForm, "tool_form_started", buildToolFormParams);
                return;
            }

            var leadForm = event.target.closest('form[action="/lead/submit"]');
            if (leadForm) {
                markStarted(leadForm, "lead_form_started", buildLeadFormParams);
            }
        });

        document.addEventListener("submit", function (event) {
            var form = event.target;
            if (!(form instanceof HTMLFormElement)) {
                return;
            }
            if (form.dataset.analyticsSubmitting === "true") {
                return;
            }

            if (form.matches('form[action="/tool/result"]')) {
                event.preventDefault();
                form.dataset.analyticsSubmitting = "true";
                track("tool_form_submitted", buildToolFormParams(form), {
                    onDone: function () {
                        HTMLFormElement.prototype.submit.call(form);
                    }
                });
                return;
            }

            if (form.matches('form[action="/lead/submit"]')) {
                event.preventDefault();
                form.dataset.analyticsSubmitting = "true";
                track("generate_lead", buildLeadFormParams(form), {
                    onDone: function () {
                        HTMLFormElement.prototype.submit.call(form);
                    }
                });
            }
        });
    }

    function initLinkTracking() {
        document.addEventListener("click", function (event) {
            var link = event.target.closest("a[href]");
            if (!link) {
                return;
            }

            var href = link.getAttribute("href");
            if (!href || href.indexOf("#") === 0) {
                return;
            }

            var url = safeUrl(href);
            if (!url) {
                return;
            }

            if (url.pathname.indexOf("/tool/") === 0 && url.pathname !== "/tool/out") {
                var entryMode = entryModeFromPath(url.pathname);
                if (entryMode) {
                    track("tool_entry_clicked", {
                        entry_mode: entryMode,
                        source_surface: surfaceFromPath(currentPath())
                    });
                }
                return;
            }

            var resultContext = getResultContext();

            if (url.pathname === "/tool/out") {
                var params = url.searchParams;
                var ctaParams = {
                    entry_mode: params.get("entryMode"),
                    result_tier: params.get("tier"),
                    result_branch: params.get("branch"),
                    cta_type: params.get("ctaType"),
                    destination_host: targetHost(params.get("target")),
                    shared_view: resultContext ? resultContext.shared_view : ""
                };

                if (!isPlainLeftClick(event) || link.target === "_blank") {
                    track("result_cta_clicked", ctaParams);
                    return;
                }

                event.preventDefault();
                track("result_cta_clicked", ctaParams, {
                    onDone: function () {
                        window.location.assign(link.href);
                    }
                });
                return;
            }

            if (!resultContext) {
                return;
            }

            if (url.pathname.indexOf("/result/saved/") === 0 && url.pathname.lastIndexOf(".pdf") !== url.pathname.length - 4) {
                track("saved_result_open_clicked", resultContext);
                return;
            }

            if (url.pathname.lastIndexOf(".pdf") === url.pathname.length - 4) {
                track("file_download", {
                    entry_mode: resultContext.entry_mode,
                    result_tier: resultContext.result_tier,
                    result_branch: resultContext.result_branch,
                    problem_type: resultContext.problem_type,
                    shared_view: resultContext.shared_view,
                    content_type: "saved_result",
                    file_extension: "pdf"
                });
            }
        });
    }

    window.wvAnalytics = {
        track: track,
        resultContext: getResultContext
    };

    initToolEntryViews();
    initResultView();
    initLeadFeedbackView();
    initFormTracking();
    initLinkTracking();
})();
