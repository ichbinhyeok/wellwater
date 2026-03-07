package com.example.wellwater.pseo;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PseoDecisionDocService {

    private final Map<String, PseoDecisionDoc> docs = Map.ofEntries(
            Map.entry("rotten-egg-smell", doc(
                    "Treat rotten egg odor as a pattern-matching problem first, not a same-day equipment verdict.",
                    "Hot-only odor often points toward a water-heater issue or sulfur bacteria pattern, while whole-house odor pushes you toward source-water diagnosis and broader nuisance testing.",
                    List.of(
                            "Map where the odor appears: hot only, cold only, or throughout the house.",
                            "Check whether odor travels with orange or black staining before comparing sulfur treatment categories.",
                            "Use the symptom-first tool or targeted testing before buying whole-house equipment."
                    ),
                    List.of(
                            "People often mistake a water-heater-only smell for a whole-house sulfur problem.",
                            "Sulfur odor can overlap with iron or manganese symptom patterns, especially when staining is present.",
                            "A product comparison is premature if you have not mapped when and where the odor appears."
                    ),
                    "Retest or re-evaluate after heater service, disinfection, or any change in where the odor shows up.",
                    faq(
                            item("Is rotten egg smell always dangerous?", "Not automatically. It is often a nuisance and maintenance problem, but it still needs pattern-based diagnosis before treatment."),
                            item("Should I buy a sulfur filter first?", "No. First determine whether the smell is hot-water-only, whole-house, or paired with staining because those patterns change the likely fix."),
                            item("What data matters most?", "Location of the odor, timing, staining, and whether a recent maintenance event changed the pattern.")
                    )
            )),
            Map.entry("orange-stains", doc(
                    "Orange stains usually point toward nuisance minerals or corrosion clues, but the scope still needs sorting before you buy hardware.",
                    "If the issue is mostly staining without broader exposure concerns, it is often nuisance or plumbing related; if metallic taste, low pH, or copper clues appear too, the decision becomes more about corrosion and verification.",
                    List.of(
                            "Check whether the staining is paired with metallic taste, low pH clues, or blue-green staining.",
                            "Decide whether the issue is fixture-specific or system-wide before comparing iron or softening paths.",
                            "Use a result-first or symptom-first workflow before choosing between filter categories."
                    ),
                    List.of(
                            "People often confuse iron staining with every orange or brown mark in the home.",
                            "Corrosion and old plumbing can create a different decision path from nuisance mineral staining.",
                            "A softener versus iron-filter decision is weak if you have not separated staining from corrosion context."
                    ),
                    "Retest after plumbing changes, pH correction, or any treatment install that is supposed to change staining behavior.",
                    faq(
                            item("Are orange stains always an iron problem?", "Not always. They often suggest iron or related nuisance issues, but corrosion and plumbing interaction can change the answer."),
                            item("Should I compare filters right away?", "Only after you know whether the problem is source-wide, corrosion-shaped, or tied to a specific fixture."),
                            item("What is the most useful next signal?", "Whether the stains travel with taste, pH clues, or only show up in certain locations.")
                    )
            )),
            Map.entry("black-stains", doc(
                    "Black staining is a clue, not a diagnosis. Separate nuisance minerals, fixture conditions, and source patterns before buying treatment.",
                    "If black residue is isolated or cosmetic, it often behaves more like a nuisance problem; if it moves with taste, odor, or broader water changes, it needs a more disciplined scope check.",
                    List.of(
                            "Map where the staining appears and whether it is paired with odor, taste, or sediment changes.",
                            "Check whether only certain fixtures or hot-water lines are involved.",
                            "Use the symptom-first tool before comparing treatment categories."
                    ),
                    List.of(
                            "People often assume black staining means a single contaminant when the source could be broader or more mechanical.",
                            "Fixture-specific residue can mislead you into buying system-wide treatment.",
                            "A weak symptom map creates bad compare-page decisions."
                    ),
                    "Re-evaluate after fixture service, water-heater work, or any event that changes where the staining shows up.",
                    faq(
                            item("Does black staining automatically mean manganese?", "It can point that way, but the pattern still needs confirmation before you treat it like a settled diagnosis."),
                            item("Do I need whole-house equipment immediately?", "Not from the stain alone. First decide whether the source is fixture-specific, hot-water-only, or system-wide."),
                            item("What helps most before buying?", "A clear symptom map and, if needed, targeted testing rather than category shopping from a single clue.")
                    )
            )),
            Map.entry("cloudy-water", doc(
                    "Cloudy well water is a timing and pattern problem first. Do not buy sediment gear until you know whether the change is temporary, trigger-driven, or persistent.",
                    "Short-lived cloudiness can behave like an operational or disturbance issue, while persistent change after storms, repairs, or source stress deserves a more cautious safety and retest path.",
                    List.of(
                            "Check whether the cloudiness followed a storm, repair, power event, or long vacancy.",
                            "Note whether it clears after standing or stays consistently murky.",
                            "Use the trigger-first or symptom-first flow before selecting filtration hardware."
                    ),
                    List.of(
                            "People often buy sediment equipment before checking whether the change was temporary and event-driven.",
                            "A cloudy sample after disturbance can say more about timing than long-term source behavior.",
                            "Persistent cloudiness and temporary cloudiness should not be treated like the same problem."
                    ),
                    "Retest after the disturbance settles and again if the cloudiness persists beyond the immediate event window.",
                    faq(
                            item("Is cloudy water always dangerous?", "Not automatically, but persistent or trigger-linked cloudiness should slow down shopping and raise verification."),
                            item("Should I buy a sediment filter right away?", "Not until you know whether the cloudiness is persistent, temporary, or tied to a recent event."),
                            item("What matters most?", "Whether the cloudiness clears, when it started, and what changed around the well or plumbing system.")
                    )
            )),
            Map.entry("metallic-taste", doc(
                    "Metallic taste often needs corrosion logic before treatment logic.",
                    "When metallic taste travels with low pH, blue-green staining, or plumbing clues, the issue leans toward corrosion and metal follow-up rather than a generic nuisance filter purchase.",
                    List.of(
                            "Check for low pH, blue-green stains, or recent plumbing work.",
                            "Separate source-water contaminants from plumbing interaction before choosing equipment.",
                            "Use the symptom-first or result-first path to tighten the corrosion sequence."
                    ),
                    List.of(
                            "People often mistake corrosion clues for a simple taste problem.",
                            "Taste alone is a weak basis for category shopping when plumbing interaction may still be unresolved.",
                            "Under-sink fixes can hide a broader corrosion problem instead of solving it."
                    ),
                    "Retest after pH correction, plumbing changes, or any intervention meant to reduce corrosion or metal release.",
                    faq(
                            item("Does metallic taste mean the well itself is contaminated?", "Not always. Plumbing interaction and corrosion can be part of the picture."),
                            item("Should I buy taste-improvement filtration first?", "No. First decide whether low pH and metal exposure need a broader test sequence."),
                            item("What is the strongest next check?", "Low pH, copper or lead follow-up, and whether the symptom is system-wide or tied to certain fixtures.")
                    )
            )),
            Map.entry("after-flood", doc(
                    "After a flood, treat older assumptions as stale and move into verification mode before product mode.",
                    "This is a safety-critical trigger first. Even if the visible problem looks minor, flood context raises the chance that normal nuisance logic is no longer enough.",
                    List.of(
                            "Use safer temporary water if the well or surrounding area was materially affected.",
                            "Follow flood-specific testing and disinfection guidance before browsing treatment categories.",
                            "Do not trust pre-flood results to define the current risk picture."
                    ),
                    List.of(
                            "People often jump straight to equipment when the immediate need is verification and safe temporary water.",
                            "A flood changes the confidence of older lab results even if the water seems normal afterward.",
                            "Post-flood treatment shopping can become a substitute for proper retesting if the sequence is wrong."
                    ),
                    "Retest after the initial flood response and again when local guidance or well conditions suggest the system has stabilized.",
                    faq(
                            item("Should I rely on old test results after a flood?", "No. Flood context can make old assumptions and older results much less reliable."),
                            item("Is this a time to compare filters?", "Usually not first. Verification, temporary safer water, and flood response steps come first."),
                            item("What is the right posture after flooding?", "Verification-first, safety-first, and slower commerce.")
                    )
            )),
            Map.entry("after-repair", doc(
                    "After well repair, the question is not just what changed. It is whether the repair reset what you should test and trust.",
                    "Some post-repair changes are temporary operational noise, while others mean the system should be retested before you trust either old data or new treatment assumptions.",
                    List.of(
                            "Write down what was repaired and what changed afterward.",
                            "Retest the signals most likely to have been affected by the repair or disturbance.",
                            "Avoid shopping from a post-repair symptom until the repair-linked noise is separated from the underlying issue."
                    ),
                    List.of(
                            "People often assume a repair solved the original problem without rechecking the evidence.",
                            "Temporary post-repair changes can trigger bad buying decisions if treated like stable system behavior.",
                            "A repair may change sampling confidence and what counts as a comparable result."
                    ),
                    "Retest after the system settles from the repair and again if the same pattern persists beyond the immediate post-repair window.",
                    faq(
                            item("Should I compare treatment right after a repair?", "Not until you know whether the repair changed the symptom, the sample quality, or the underlying problem scope."),
                            item("Do old results still count?", "Sometimes, but repairs can reduce the comparability of older results."),
                            item("What is the best next move?", "Document the repair, watch the changed pattern, and retest the most affected signals.")
                    )
            )),
            Map.entry("home-purchase-test", doc(
                    "A home-purchase panel is a timing-specific decision, not a complete lifetime safety verdict.",
                    "Home-purchase testing can be high-stakes because the transaction timeline is short, but it still should not be confused with a full long-term household risk plan.",
                    List.of(
                            "Separate required or customary sale testing from the broader tests your household may still need.",
                            "Use certified labs and state-specific sale context where it applies.",
                            "Do not convert a limited sale panel into a broad equipment purchase without checking scope gaps."
                    ),
                    List.of(
                            "People often treat a sale panel as if it answers every health, geology, and future maintenance question.",
                            "Short transaction timelines can pressure buyers into shopping before the scope is clear.",
                            "Local rules and household composition can change what counts as an adequate panel."
                    ),
                    "Retest after closing if the sale panel was narrow, state-limited, or missing issues tied to your actual household risk.",
                    faq(
                            item("Is a home-sale well test enough?", "Not always. It may satisfy transaction needs without covering every long-term health or geology question."),
                            item("Should I buy treatment before closing?", "Only if the scope is genuinely clear. Otherwise, separate the sale panel from longer-term decision-making."),
                            item("What improves this page's value?", "State context, certified labs, and a clear split between sale requirements and household safety planning.")
                    )
            )),
            Map.entry("retest-after-treatment", doc(
                    "Retesting after treatment is how you verify the fix instead of assuming the equipment claim was enough.",
                    "This is less about health versus nuisance and more about proof versus assumption: if the intervention changed the system, you still need to verify the outcome on the right timeline.",
                    List.of(
                            "Match the retest timing to the treatment type and the problem it was supposed to solve.",
                            "Do not stop at installation photos, marketing claims, or a one-time impression of better water.",
                            "Use the result-first path again if the retest shows the issue is not actually resolved."
                    ),
                    List.of(
                            "People often mistake installation for verified resolution.",
                            "A treatment category can be appropriate in theory but still poorly fitted, poorly maintained, or not actually solving the verified issue.",
                            "Skipping retesting weakens both trust and repeat revenue opportunities."
                    ),
                    "Retest according to the treatment category, the contaminant or symptom being monitored, and any state or lab guidance linked to the issue.",
                    faq(
                            item("Why retest after treatment if the water already seems better?", "Because subjective improvement is weaker evidence than post-treatment verification."),
                            item("Does every treatment need the same retest timing?", "No. The timeline should fit the treatment type and the original problem."),
                            item("What if the retest is still bad?", "Go back into verification and scope mode instead of doubling down on the same buying assumption.")
                    )
            )),
            Map.entry("nitrate", doc(
                    "Nitrate is a verify-and-protect issue, especially when infants or pregnancy are in scope.",
                    "This is not a cosmetic nuisance problem. Vulnerable households move the page into safer-drinking-water and certified-testing logic first.",
                    List.of(
                            "If infant feeding or pregnancy is involved, prioritize safer drinking water while the result is being verified and scoped.",
                            "Use certified testing and a repeat-screening mindset instead of treating nitrate like one isolated lab line.",
                            "Only compare treatment categories after the household risk and use case are clear."
                    ),
                    List.of(
                            "People often underreact because the water looks and tastes normal.",
                            "A single nitrate result should not instantly become a hardware verdict without checking vulnerable-household context.",
                            "Whole-house versus drinking-water-only scope matters and should not be guessed from fear."
                    ),
                    "Retest according to household vulnerability, regional risk, seasonality, and any official local guidance affecting nitrate screening cadence.",
                    faq(
                            item("Why is nitrate treated differently from nuisance issues?", "Because the main problem is household safety and exposure management, not appearance or maintenance."),
                            item("Should I buy a filter right away?", "Not before household scope, certified confirmation, and safer temporary drinking-water decisions are clear."),
                            item("What makes nitrate urgent?", "Infants, pregnancy, and uncertainty about the current drinking-water source.")
                    )
            )),
            Map.entry("coliform", doc(
                    "A total coliform positive is a verification and pathway question first, not a generic filter-shopping prompt.",
                    "This leans toward health and microbial caution because the main job is to confirm, interpret context, and decide whether disinfection or broader follow-up belongs in scope.",
                    List.of(
                            "Treat the result as a cue to tighten sampling, confirmation, and local guidance rather than jump into routine product browsing.",
                            "Check whether the result followed flooding, repair, long vacancy, or sample-quality problems.",
                            "Use certified retesting and microbial follow-up logic before narrowing to UV or chlorination choices."
                    ),
                    List.of(
                            "People often buy equipment before confirming whether the sample or event context changed the result.",
                            "A coliform positive is not the same as a settled final treatment verdict.",
                            "Microbial interpretation gets weaker fast when sampling discipline is poor."
                    ),
                    "Retest on the schedule appropriate for microbial follow-up and any event context, especially after disinfection or corrective action.",
                    faq(
                            item("Does a total coliform positive mean I should buy UV immediately?", "Not automatically. Confirmation, event context, and the corrective pathway still matter."),
                            item("Why does sample quality matter so much here?", "Because microbial decisions become weaker and more expensive when the sample itself is suspect."),
                            item("What should happen before product comparison?", "Certified retesting, context review, and a clearer view of whether the issue is transient, structural, or confirmed.")
                    )
            )),
            Map.entry("arsenic", doc(
                    "Arsenic is a health-and-scope issue first. Treatment comparison belongs after lab confidence and household scope are clear.",
                    "This is not a nuisance page. The key question is whether the result is confirmed, what the household actually drinks, and whether regional geology changes the testing path.",
                    List.of(
                            "Use certified lab confidence and state or geology context before settling on a treatment path.",
                            "Decide whether the main question is drinking-water protection or whole-house scope.",
                            "Compare arsenic treatment categories only after the evidence and certification requirements are clear."
                    ),
                    List.of(
                            "People often jump straight to product category shopping because arsenic feels inherently urgent.",
                            "Regional bedrock context can widen the testing conversation to radionuclides or other related issues.",
                            "An arsenic result still needs scope discipline; fear is not the same thing as a good whole-house decision."
                    ),
                    "Retest according to certified lab guidance, any treatment install, and the regional risk profile that shaped the original scope decision.",
                    faq(
                            item("Is arsenic a whole-house problem by default?", "Not automatically. The best scope depends on how the water is used and what the verified exposure question actually is."),
                            item("Should I compare RO and adsorptive media immediately?", "Only after you have enough confidence in the result and in the use-case scope."),
                            item("Why does regional context matter so much?", "Because geology can change what else belongs in the test plan and how the result should be interpreted.")
                    )
            )),
            Map.entry("lead", doc(
                    "Lead belongs in a corrosion-and-exposure workflow first, not a generic taste or filter workflow.",
                    "Lead often rides with plumbing interaction, low pH, or fixture-specific clues, which means the page should tighten testing and scope before anyone treats it like a normal nuisance problem.",
                    List.of(
                            "Check whether low pH, blue-green stains, or recent plumbing work are part of the picture.",
                            "Use certified follow-up and decide whether the question is fixture-specific exposure or broader household scope.",
                            "Do not collapse lead into a simple under-sink shopping decision until the exposure path is clearer."
                    ),
                    List.of(
                            "People often treat lead like a stand-alone contaminant without checking corrosion context.",
                            "A plumbing interaction problem can look simpler than it really is if the page jumps to product category too quickly.",
                            "Taste and appearance are weak signals for lead exposure on their own."
                    ),
                    "Retest after corrosion correction, plumbing changes, or any intervention meant to reduce exposure risk.",
                    faq(
                            item("Does lead always mean the well source is the problem?", "No. Plumbing interaction and corrosivity can be part of the decision path."),
                            item("Should I buy a drinking-water filter immediately?", "Not before the page and the follow-up testing narrow where the risk is entering the system."),
                            item("What changes urgency here?", "Vulnerable households, corrosion clues, and uncertainty about where the lead is coming from.")
                    )
            )),
            Map.entry("pfas", doc(
                    "PFAS is a claim-check and scope discipline problem before it becomes a product shortlist.",
                    "This is a health-oriented page where testing confidence, exposure scope, and treatment claims all matter more than broad filter marketing.",
                    List.of(
                            "Use lab confidence and local context before narrowing to a treatment category.",
                            "Separate drinking-water protection from whole-house assumptions before comparing categories.",
                            "Verify what the treatment claim actually covers before treating the page like a product verdict."
                    ),
                    List.of(
                            "People often treat PFAS like one chemical and one filter answer when the claim scope may vary.",
                            "Category shopping gets ahead of the evidence very quickly on PFAS pages.",
                            "Whole-house and point-of-use choices are not interchangeable just because the contaminant label is the same."
                    ),
                    "Retest after any treatment install and on the cadence that fits the verified PFAS concern and local testing path.",
                    faq(
                            item("Is carbon always enough for PFAS?", "The page should not assume that. Claim scope, test fit, and the specific treatment path still matter."),
                            item("Should I compare products from the start?", "No. PFAS pages should move through testing confidence and claim verification first."),
                            item("What is the strongest next step?", "Tighten the test interpretation and compare categories only after the exposure scope is clear.")
                    )
            )),
            Map.entry("radon", doc(
                    "Radon in well water is a state-and-scope issue first, not a generic nuisance problem.",
                    "This page gets stronger when regional context, household use, and related radionuclide testing are visible before treatment comparison begins.",
                    List.of(
                            "Use state or geology context to decide whether broader radionuclide testing belongs in scope.",
                            "Separate drinking-water concern from broader home exposure assumptions before comparing treatment categories.",
                            "Move into compare pages only after the page clarifies the testing and household-use path."
                    ),
                    List.of(
                            "People often treat radon pages like ordinary filter pages even though the local context matters more.",
                            "Regional geology can widen the right next test beyond one number on one page.",
                            "A compare page is weak if the page has not narrowed the actual use-case."
                    ),
                    "Retest according to local guidance, any treatment install, and the radionuclide context that shaped the original plan.",
                    faq(
                            item("Should I compare treatment categories right away?", "Only after the page clarifies the local context and the scope of the concern."),
                            item("Why is state context important here?", "Because geology and local testing pathways can materially change what the next step should be."),
                            item("Is radon always a whole-house treatment question?", "Not automatically. Scope and household use still need to be defined.")
                    )
            )),
            Map.entry("ph", doc(
                    "Low pH is a corrosion workflow first. Do not treat it like a simple filter-shopping problem.",
                    "This page gets stronger when it separates source-water acidity, plumbing interaction, and follow-up metals testing before category comparison.",
                    List.of(
                            "Check whether metallic taste, blue-green stains, or lead and copper follow-up belong in scope.",
                            "Decide whether the page is mainly about plumbing protection, exposure verification, or both.",
                            "Compare correction categories only after the page tightens the corrosion sequence."
                    ),
                    List.of(
                            "People often shop for neutralization before deciding whether corrosion follow-up is incomplete.",
                            "A low-pH page can hide lead or copper implications if it is treated like a simple nuisance page.",
                            "Whole-house correction and point-of-use taste fixes are not the same decision."
                    ),
                    "Retest after pH correction, plumbing changes, or any intervention meant to stabilize corrosivity.",
                    faq(
                            item("Is low pH mainly a nuisance issue?", "Not always. It can become an exposure and plumbing issue when corrosion clues are present."),
                            item("Should I compare treatment categories first?", "No. The page should verify corrosion implications before it becomes a compare-page decision."),
                            item("What strengthens the decision here?", "Low pH, paired corrosion clues, and follow-up metals testing.")
                    )
            )),
            Map.entry("blue-green-stains", doc(
                    "Blue-green stains should push the page toward corrosion logic before hardware shopping.",
                    "This page becomes more useful when it connects staining to low pH, plumbing interaction, and possible metals follow-up instead of treating it like a cosmetic issue only.",
                    List.of(
                            "Check whether metallic taste or low pH clues appear with the staining.",
                            "Decide whether the pattern is fixture-specific or system-wide before comparing treatment paths.",
                            "Use a symptom-first or result-first handoff before shopping from the stain alone."
                    ),
                    List.of(
                            "People often treat blue-green stains like simple discoloration instead of a corrosion clue.",
                            "A fixture pattern can point to plumbing interaction more than source-water treatment.",
                            "The page gets weaker if it skips lead or copper follow-up where the pattern justifies it."
                    ),
                    "Retest after corrosion correction, plumbing work, or any treatment meant to reduce acidic water interaction.",
                    faq(
                            item("Are blue-green stains just cosmetic?", "Not necessarily. The page should treat them as a corrosion clue until proven otherwise."),
                            item("Should I buy treatment from the stain alone?", "No. The page should narrow the corrosion path first."),
                            item("What usually matters most?", "Low pH, metallic taste, and whether the pattern is local or whole-house.")
                    )
            )),
            Map.entry("sulfur-smell-hot-water", doc(
                    "Hot-water sulfur odor is usually a pattern-separation job before it becomes a whole-house treatment decision.",
                    "This page is strongest when it keeps the user from confusing a heater-linked odor with a source-wide sulfur diagnosis.",
                    List.of(
                            "Confirm whether the odor is hot-water-only before comparing whole-house treatment categories.",
                            "Check for paired staining or cold-water odor that would change the likely cause pattern.",
                            "Use the symptom-first tool if the page still cannot cleanly separate heater behavior from source-water behavior."
                    ),
                    List.of(
                            "People often turn a heater symptom into a whole-house purchase too quickly.",
                            "Hot-water-only odor and whole-house odor should not share the same first compare path.",
                            "A sulfur page gets weaker if it ignores heater maintenance and timing clues."
                    ),
                    "Re-evaluate after heater service, disinfection, or any change in whether the odor stays hot-water-only.",
                    faq(
                            item("Does hot-water sulfur smell mean I need a whole-house filter?", "Not automatically. The page should first separate heater-specific behavior from source-water odor."),
                            item("What makes this page more useful?", "Knowing whether the smell is hot only, cold only, or both."),
                            item("Should I compare sulfur systems immediately?", "Only if the page already rules out a heater-specific pattern.")
                    )
            )),
            Map.entry("after-heavy-rain", doc(
                    "After heavy rain, the page should assume the timing changed the confidence of older assumptions.",
                    "This trigger page is strongest when it treats rainfall as a reset to sampling confidence, symptom interpretation, and retest order rather than a generic shopping event.",
                    List.of(
                            "Record what changed after the rain and whether the shift was temporary or persistent.",
                            "Use testing or inspection logic before turning the page into a treatment comparison.",
                            "Treat older results as weaker if the rainfall event plausibly changed the system."
                    ),
                    List.of(
                            "People often shop from the post-rain symptom before deciding whether the event was temporary or structural.",
                            "A rain trigger is not automatically the same as a flood trigger, but it still changes confidence.",
                            "The page loses value if it treats event timing like background noise."
                    ),
                    "Retest after the event settles and again if the same pattern keeps appearing after later rain events.",
                    faq(
                            item("Is heavy rain enough to change what I should trust?", "Often yes. The page should at least downgrade confidence in older assumptions until the system is rechecked."),
                            item("Should I compare filters after rain?", "Not first. Event timing and verification belong first."),
                            item("What strengthens this page?", "Clear timing, repeat patterns, and whether the issue persists once the event passes.")
                    )
            )),
            Map.entry("new-baby-at-home", doc(
                    "A new baby in the home should push the page toward safer-drinking-water logic before any treatment shopping.",
                    "This trigger page gets value from narrowing exposure and testing urgency, not from pretending the right product can be picked from household status alone.",
                    List.of(
                            "Check whether nitrate, microbial, or other drinking-water questions now need faster verification.",
                            "Use safer drinking-water logic if the page still lacks strong recent data.",
                            "Treat compare pages as secondary until the vulnerable-household question is tighter."
                    ),
                    List.of(
                            "People often keep the same testing urgency even though the household risk changed.",
                            "A new baby does not tell you the contaminant, but it absolutely changes how cautious the page should be.",
                            "The page gets weaker if it turns vulnerability into instant product shopping."
                    ),
                    "Retest according to the drinking-water risks that matter most for infants and the current quality of the evidence on the page.",
                    faq(
                            item("Does having a new baby change the next step even without a new lab result?", "Yes. It can make a weak or older evidence set less acceptable for drinking-water decisions."),
                            item("Should I start by shopping for treatment?", "No. Vulnerable-household logic should raise verification urgency first."),
                            item("What does this page do best?", "It changes the urgency and caution level of the next action.")
                    )
            )),
            Map.entry("pregnancy-in-home", doc(
                    "Pregnancy in the home should move the page toward narrower, more cautious drinking-water decisions first.",
                    "This trigger page earns trust when it raises verification discipline and exposure management before it raises shopping intent.",
                    List.of(
                            "Recheck whether the current drinking-water path is actually supported by recent evidence.",
                            "Use certified testing or better data if the page still relies on weak or old signals.",
                            "Keep compare pages downstream from the vulnerable-household question."
                    ),
                    List.of(
                            "People often leave the page on a normal urgency setting even though the household context changed.",
                            "Pregnancy context should narrow tolerance for weak evidence, not broaden shopping activity.",
                            "The page gets weaker if it assumes one product path fits every contaminant concern."
                    ),
                    "Retest on the schedule that fits the strongest health question in scope and the current quality of the evidence.",
                    faq(
                            item("Why does pregnancy change the page logic?", "Because the page should become less tolerant of weak evidence and slower shopping."),
                            item("Is this page mainly about products?", "No. It is mainly about verification posture and safer drinking-water decisions."),
                            item("What should happen next?", "Use the stronger testing or result path before settling on equipment.")
                    )
            )),
            Map.entry("test-kit-vs-certified-lab", doc(
                    "Test kit versus certified lab is a method and confidence decision first, not a convenience decision only.",
                    "This compare page works when it shows which questions can tolerate weaker evidence and which ones need certified sampling and lab pathways.",
                    List.of(
                            "Define whether the page is trying to answer a quick screen question or a high-confidence household decision.",
                            "Use certified labs when the page touches health, sale, or treatment-verification questions that need stronger evidence.",
                            "Treat convenience as secondary to confidence and scope."
                    ),
                    List.of(
                            "People often compare speed and price without comparing evidence quality.",
                            "A quick screen is not the same thing as a partner-ready or treatment-ready result.",
                            "The page loses value if it treats health and nuisance questions like they need the same test confidence."
                    ),
                    "Repeat or escalate testing when the kit result still leaves the actual household decision unresolved.",
                    faq(
                            item("Is a home kit enough for a treatment decision?", "Not always. The page should separate quick screening from certified decision-grade evidence."),
                            item("When does a certified lab matter most?", "When the page is shaping health, sale, escalation, or treatment-verification decisions."),
                            item("What is the main mistake here?", "Confusing a faster answer with a strong enough answer.")
                    )
            )),
            Map.entry("mail-in-lab-vs-local-certified-lab", doc(
                    "Mail-in versus local certified lab is a sampling-discipline and turnaround question, not just a convenience question.",
                    "This compare page is strongest when it explains where method fit, shipping, chain-of-custody, and local context change the confidence of the result.",
                    List.of(
                            "Check whether the page depends on timing, local handling, or a strict certified path.",
                            "Use the stronger lab path when the result needs to support health, sale, or treatment verification decisions.",
                            "Read the sampling-mistake page before treating mail-in convenience as the only relevant factor."
                    ),
                    List.of(
                            "People often compare price and speed before checking whether the result still fits the actual decision.",
                            "Sampling and handling errors can make a lab comparison look simpler than it is.",
                            "The page weakens if it ignores state or local process requirements."
                    ),
                    "Repeat or escalate testing if the chosen path introduced timing, sampling, or custody doubts that affect the household decision.",
                    faq(
                            item("Is local certified always better than mail-in?", "Not automatically, but the page should compare confidence fit, timing, and process risk instead of convenience alone."),
                            item("Why does sampling discipline matter here?", "Because the best lab path still fails if the sample handling does not fit the question."),
                            item("What should happen before shopping from the result?", "Make sure the lab path actually produced decision-grade evidence.")
                    )
            )),
            Map.entry("private-well-sampling-mistakes-that-break-results", doc(
                    "Sampling mistakes can quietly break the value of an otherwise good page, test, or treatment decision.",
                    "This authority page matters because a bad sample can make every later step look precise while still being wrong for the household.",
                    List.of(
                            "Use this page before relying on a weak, inconsistent, or event-shaped result.",
                            "Check whether timing, container handling, or treated-versus-raw sampling changed the meaning of the result.",
                            "Go back to certified retesting when the sample process undercuts confidence."
                    ),
                    List.of(
                            "People often treat a printed result like truth without checking how the sample was taken.",
                            "A sampling error can look like a treatment problem when the page skips method review.",
                            "This page loses value if it stays abstract instead of feeding the user back into a stronger test path."
                    ),
                    "Retest whenever the sample process itself makes the result hard to trust, compare, or act on.",
                    faq(
                            item("Can a sampling mistake really change the treatment decision?", "Yes. It can turn a confident-looking result into weak guidance."),
                            item("When should I stop and retest?", "When sample timing, handling, or source selection makes the result difficult to trust."),
                            item("Why is this an organic winner page?", "Because users often need method clarity before they can trust any later compare or product page.")
                    )
            ))
    );

    public Optional<PseoDecisionDoc> findBySlug(String slug) {
        return Optional.ofNullable(docs.get(slug));
    }

    private PseoDecisionDoc doc(
            String oneLineVerdict,
            String healthVsNuisance,
            List<String> nextSteps,
            List<String> commonConfusions,
            String retestTiming,
            List<PseoFaqItem> faqs
    ) {
        return new PseoDecisionDoc(oneLineVerdict, healthVsNuisance, nextSteps, commonConfusions, retestTiming, faqs);
    }

    private List<PseoFaqItem> faq(PseoFaqItem... items) {
        return List.of(items);
    }

    private PseoFaqItem item(String question, String answer) {
        return new PseoFaqItem(question, answer);
    }
}
