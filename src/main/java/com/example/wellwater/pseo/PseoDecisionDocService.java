package com.example.wellwater.pseo;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PseoDecisionDocService {

    private final Map<String, PseoDecisionDoc> docs = Map.ofEntries(
            Map.entry("rotten-egg-smell", winnerDoc(
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
                    splits(
                            split("Hot water only", "Bias the decision toward heater inspection, heater disinfection, or anode-related troubleshooting before whole-house treatment."),
                            split("Cold and hot throughout the house", "Treat it like a source-water or distribution-wide nuisance pattern and move toward testing plus compare-path narrowing."),
                            split("Odor plus orange or black staining", "Widen the scope to iron, manganese, or overlapping nuisance patterns before choosing a sulfur-specific category.")
                    ),
                    List.of(
                            "Escalate faster if the odor appeared after flooding, repair work, or a long period of inactivity.",
                            "Escalate if the odor suddenly moved from one fixture to the whole house.",
                            "Escalate if odor is paired with broader staining or sediment changes instead of staying an isolated smell problem."
                    ),
                    faq(
                            item("Why does my well water smell like rotten eggs?", "It is often tied to sulfur bacteria, heater-specific behavior, or a wider nuisance pattern, but the location and timing of the smell change the likely cause."),
                            item("Why does only my hot water smell like sulfur?", "Hot-water-only odor often points toward the water heater or anode behavior before it points to a whole-house sulfur problem."),
                            item("Do I need a sulfur filter for well water right away?", "No. First determine whether the smell is hot-water-only, whole-house, or paired with staining because those patterns change the likely fix.")
                    )
            )),
            Map.entry("orange-stains", winnerDoc(
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
                    splits(
                            split("Only one fixture or one branch", "Keep plumbing interaction and fixture-specific corrosion in scope before you jump to whole-house equipment."),
                            split("Stains across multiple fixtures", "Shift toward a broader nuisance-mineral or source-wide maintenance diagnosis."),
                            split("Stains plus metallic taste or blue-green marks", "Move the page toward corrosion logic, low-pH follow-up, and metal verification instead of straight iron shopping.")
                    ),
                    List.of(
                            "Escalate if staining is spreading quickly after a recent plumbing change or low-pH clue.",
                            "Escalate if stains are paired with metallic taste, copper clues, or visible corrosion signs.",
                            "Escalate if the issue changed after treatment and the treatment claim now needs verification."
                    ),
                    faq(
                            item("Why is my well water leaving orange stains?", "Orange stains often suggest iron or related nuisance minerals, but corrosion and plumbing interaction can change the answer."),
                            item("Do orange stains mean I need an iron filter?", "Not automatically. First decide whether the problem is source-wide, corrosion-shaped, or tied to a specific fixture."),
                            item("Could orange stains be from plumbing instead of the well?", "Yes. Taste changes, low pH clues, and where the stains appear can point toward corrosion rather than a simple iron problem.")
                    )
            )),
            Map.entry("black-stains", winnerDoc(
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
                    splits(
                            split("One fixture or one branch", "Keep the page focused on fixture conditions and plumbing interaction before whole-house treatment enters the discussion."),
                            split("Hot-water-linked residue", "Bias the page toward heater or hot-line diagnosis instead of a blanket whole-house assumption."),
                            split("Black staining plus odor or sediment", "Widen the page to include broader nuisance or source-pattern checks before compare-page shopping.")
                    ),
                    List.of(
                            "Escalate if the staining suddenly spreads from one fixture to the whole house.",
                            "Escalate if black staining is paired with odor, sediment, or broader water-quality change.",
                            "Escalate if the page is treating one stain clue like a settled system-wide diagnosis."
                    ),
                    faq(
                            item("Does black staining automatically mean manganese?", "It can point that way, but the pattern still needs confirmation before you treat it like a settled diagnosis."),
                            item("Do I need whole-house equipment immediately?", "Not from the stain alone. First decide whether the source is fixture-specific, hot-water-only, or system-wide."),
                            item("What helps most before buying?", "A clear symptom map and, if needed, targeted testing rather than category shopping from a single clue.")
                    )
            )),
            Map.entry("cloudy-water", winnerDoc(
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
                    splits(
                            split("Clears after standing", "Bias toward disturbance timing, entrained air, or temporary operational change before you buy hardware."),
                            split("Stays cloudy for hours or days", "Treat it as a persistent condition that deserves broader verification and a tighter test path."),
                            split("Started after storm, repair, or power event", "Use a trigger-first frame because event context changes how much old assumptions should still be trusted.")
                    ),
                    List.of(
                            "Escalate if cloudy water persists after the immediate disturbance window.",
                            "Escalate if cloudiness appeared with flood, heavy rain, or repair context.",
                            "Escalate if cloudy water is paired with odor, microbial concern, or a strong change in household use confidence."
                    ),
                    faq(
                            item("Why is my well water cloudy all of a sudden?", "Timing matters most. Cloudiness can follow storms, repairs, power events, or other disturbances that change how much you should trust older assumptions."),
                            item("Is cloudy well water safe to drink?", "Not automatically. Persistent or trigger-linked cloudiness should slow down shopping and raise verification before normal use confidence returns."),
                            item("Do I need a sediment filter for cloudy well water?", "Not until you know whether the cloudiness is persistent, temporary, or tied to a recent event.")
                    )
            )),
            Map.entry("metallic-taste", winnerDoc(
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
                    splits(
                            split("Metallic taste plus low pH or blue-green stains", "Push the page toward corrosion follow-up and plumbing interaction instead of a simple taste fix."),
                            split("Taste after recent plumbing work", "Treat the symptom like a post-change verification problem before comparing treatment categories."),
                            split("Taste across the whole house with no clear plumbing trigger", "Widen the scope to certified testing and source-versus-plumbing separation.")
                    ),
                    List.of(
                            "Escalate if metallic taste appears with vulnerable-household lead exposure concerns.",
                            "Escalate if taste is paired with blue-green staining, low pH, or visible corrosion clues.",
                            "Escalate if the taste began right after plumbing work and does not settle out quickly."
                    ),
                    faq(
                            item("Why does my well water taste metallic?", "A metallic taste can come from corrosion, plumbing interaction, or a broader source-water issue, so the pattern matters before treatment."),
                            item("Does metallic taste mean metal contamination?", "Not always. Plumbing interaction and corrosion can be part of the picture, especially when low pH or blue-green stains show up too."),
                            item("Should I test pH or metals first for metallic taste?", "Low pH, copper or lead follow-up, and whether the symptom is system-wide or tied to certain fixtures are usually the strongest next checks.")
                    )
            )),
            Map.entry("after-flood", winnerDoc(
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
                    splits(
                            split("Wellhead or surrounding area materially affected", "Move immediately into safer temporary water plus flood-specific testing and disinfection guidance."),
                            split("No visible damage but floodwater reached the property", "Lower trust in older assumptions and still treat the page as verification-first."),
                            split("Old lab results are being used to calm the decision", "Treat those results as stale until flood response and follow-up testing restore confidence.")
                    ),
                    List.of(
                            "Escalate if floodwater likely contacted the wellhead, casing, or nearby surface runoff path.",
                            "Escalate if anyone is still using old pre-flood results as the main evidence.",
                            "Escalate if the water looks normal but the flood materially changed the site conditions."
                    ),
                    faq(
                            item("Is well water safe to use after a flood?", "Not until flood response steps and follow-up testing restore confidence in the water."),
                            item("Should I boil well water after a flood?", "Boiling is not the whole answer after a flood. Temporary safer water, local guidance, and the right testing path come first."),
                            item("What should I test after a flood affects my well?", "Use flood-specific testing and disinfection guidance, and do not rely on old pre-flood results as if nothing changed.")
                    )
            )),
            Map.entry("after-repair", winnerDoc(
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
                    splits(
                            split("Symptom changed immediately after repair", "Treat the page as post-repair noise first and avoid interpreting the first change like settled truth."),
                            split("Same issue persists after the system settles", "Raise the chance that the underlying problem remains and needs tighter retesting."),
                            split("Repair changed the sampling path or plumbing conditions", "Lower confidence in older results and rebuild the evidence before shopping.")
                    ),
                    List.of(
                            "Escalate if the repair likely changed sample comparability or plumbing conditions.",
                            "Escalate if the same pattern returns after the initial post-repair settling window.",
                            "Escalate if treatment browsing starts before the repair-versus-underlying-problem question is answered."
                    ),
                    faq(
                            item("Should I compare treatment right after a repair?", "Not until you know whether the repair changed the symptom, the sample quality, or the underlying problem scope."),
                            item("Do old results still count?", "Sometimes, but repairs can reduce the comparability of older results."),
                            item("What is the best next move?", "Document the repair, watch the changed pattern, and retest the most affected signals.")
                    )
            )),
            Map.entry("home-purchase-test", winnerDoc(
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
                    splits(
                            split("Sale panel only", "Treat the page like a transaction checklist, not a full household-safety verdict."),
                            split("Infants, pregnancy, or unusual geology risk", "Expand beyond the minimum sale logic because household vulnerability changes the test scope."),
                            split("State-specific sale rules apply", "Use those rules as a floor, then decide what your real household still needs after closing.")
                    ),
                    List.of(
                            "Escalate if the sale panel is being treated like a lifetime all-clear.",
                            "Escalate if the household includes infants, pregnancy, or high-risk drinking-water use that the sale panel does not cover.",
                            "Escalate if closing pressure is pushing you into treatment shopping before the test scope is clear."
                    ),
                    faq(
                            item("What should I test when buying a house with a well?", "Start with the sale or lender panel, then check whether geology, household risk, or local context means more testing belongs in scope."),
                            item("Is a seller well water test enough before closing?", "Not always. It may satisfy transaction needs without covering every long-term health or geology question."),
                            item("Should I use a certified lab before closing on a house with a well?", "Yes when the decision needs stronger confidence. Certified labs and a clear split between sale requirements and household safety planning make this page more useful.")
                    )
            )),
            Map.entry("retest-after-treatment", winnerDoc(
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
                    splits(
                            split("Water seems better but no new test exists", "Treat the page as proof-gap first and avoid assuming the install solved the verified problem."),
                            split("Retest still shows the problem", "Move the page back into scope, fit, and verification mode instead of doubling down on the same category."),
                            split("Treatment claim was broad or sales-led", "Use retesting to decide whether the claim matched the actual household problem in the first place.")
                    ),
                    List.of(
                            "Escalate if subjective improvement is replacing post-treatment verification.",
                            "Escalate if the retest still looks bad or incomplete after the install.",
                            "Escalate if the treatment category was chosen from weak evidence and now needs stronger proof."
                    ),
                    faq(
                            item("Why retest after treatment if the water already seems better?", "Because subjective improvement is weaker evidence than post-treatment verification."),
                            item("Does every treatment need the same retest timing?", "No. The timeline should fit the treatment type and the original problem."),
                            item("What if the retest is still bad?", "Go back into verification and scope mode instead of doubling down on the same buying assumption.")
                    )
            )),
            Map.entry("nitrate", winnerDoc(
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
                    splits(
                            split("Infants or pregnancy in scope", "Move immediately toward safer drinking water plus certified confirmation instead of routine compare browsing."),
                            split("Agricultural or seasonal nitrate pattern", "Use repeat-screening and timing logic because one sample may not fully describe the risk window."),
                            split("Whole-house treatment assumption", "Slow down and decide whether the real exposure question is drinking-water-only or broader use.")
                    ),
                    List.of(
                            "Escalate if infant feeding or pregnancy is involved right now.",
                            "Escalate if the water looks normal and the household is underreacting to a safety-oriented contaminant.",
                            "Escalate if a treatment purchase is being considered before drinking-water scope is clear."
                    ),
                    faq(
                            item("Is nitrate in well water dangerous for babies?", "Yes, babies, formula use, and pregnancy are why nitrate gets treated as a household safety and exposure question first."),
                            item("Can I boil nitrate out of well water?", "No. Use safer temporary drinking water and certified confirmation instead of assuming boiling fixes nitrate."),
                            item("Do I need reverse osmosis for nitrate in well water?", "Maybe, but not before household scope, certified confirmation, and safer temporary drinking-water decisions are clear.")
                    )
            )),
            Map.entry("coliform", winnerDoc(
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
                    splits(
                            split("Positive after flood, repair, or vacancy", "Treat the result like an event-shaped microbial question and tighten verification before shopping."),
                            split("Repeat positive or broader microbial concern", "Move the page toward corrective action, local guidance, and stronger disinfection-path logic."),
                            split("Weak sample discipline", "Treat the test process itself as part of the problem before trusting any compare page.")
                    ),
                    List.of(
                            "Escalate if the page is about a repeat positive rather than a one-off result.",
                            "Escalate if sample quality or event context makes the positive hard to interpret cleanly.",
                            "Escalate if someone is jumping straight to UV or chlorination without a tighter retest path."
                    ),
                    faq(
                            item("Can I drink well water with a positive coliform result?", "Use caution first. Confirmation, event context, and the corrective pathway still matter before normal use confidence returns."),
                            item("Should I shock chlorinate after a coliform positive?", "Sometimes, but do not skip confirmation, sampling review, and event context on the way to a corrective plan."),
                            item("Do I need to retest after a positive coliform result?", "Yes. Certified retesting, context review, and a clearer view of whether the issue is transient, structural, or confirmed should come before product comparison.")
                    )
            )),
            Map.entry("e-coli", winnerDoc(
                    "E. coli is an immediate exposure-control page first, not a treatment-shopping page.",
                    "This page should act more urgently than a general coliform page because the immediate question is whether anyone should still be drinking or preparing food with the water.",
                    List.of(
                            "Switch to safer water for drinking, cooking, brushing teeth, and infant formula right away.",
                            "Use local guidance plus certified retesting before deciding whether shock treatment, disinfection, or broader correction belongs in scope.",
                            "Treat sampling quality, flooding, repair work, and wellhead conditions as part of the interpretation."
                    ),
                    List.of(
                            "People often treat E. coli like a nuisance issue because the water can still look normal.",
                            "Boiling questions can distract from the bigger issue, which is safe temporary water and a tighter response sequence.",
                            "A product comparison is weak if the page has not yet confirmed the source and corrected the pathway."
                    ),
                    "Retest on the microbial follow-up timeline recommended by local guidance, especially after any disinfection or corrective action.",
                    splits(
                            split("Positive result with flood, repair, or wellhead concern", "Treat the page like an urgent pathway problem and tighten the response sequence before comparing equipment."),
                            split("Positive result with weak sampling confidence", "Recheck the sample process fast, but keep exposure-control logic in place until stronger evidence exists."),
                            split("User jumps straight to product shopping", "Pull the page back toward safe temporary water, confirmation, and corrective-path clarity first.")
                    ),
                    List.of(
                            "Escalate if anyone is still drinking or preparing infant formula with the water.",
                            "Escalate if the page is treating E. coli like a simple one-step shopping problem.",
                            "Escalate if flooding, repair work, or wellhead conditions may still be part of the cause."
                    ),
                    faq(
                            item("Can I drink well water with E. coli?", "No. Use safer water for drinking, cooking, brushing teeth, and infant formula while you follow the corrective path."),
                            item("Does boiling make E. coli well water safe?", "Boiling questions should not replace the larger response plan. Safe temporary water, local guidance, and the right retest sequence still matter."),
                            item("What should I do after an E. coli positive in well water?", "Move into exposure control, local guidance, and certified retesting before any product comparison.")
                    )
            )),
            Map.entry("arsenic", winnerDoc(
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
                    splits(
                            split("Certified result with regional geology context", "Take the page more seriously as a treatment-scope decision and widen the testing plan where geology suggests it."),
                            split("Single result without strong lab confidence", "Keep the page in verification mode rather than collapsing into category shopping."),
                            split("Whole-house assumption from fear alone", "Slow down and decide whether the real risk path is drinking-water exposure or broader household use.")
                    ),
                    List.of(
                            "Escalate if arsenic is being treated like a simple nuisance page because the water looks normal.",
                            "Escalate if regional bedrock context suggests radionuclides or related tests may also belong in scope.",
                            "Escalate if treatment comparison is starting before the result confidence is strong enough."
                    ),
                    faq(
                            item("Is arsenic in well water dangerous?", "Yes, it is a health-oriented page, which is why result confidence and drinking-water scope matter more than shopping speed."),
                            item("Do I need whole-house treatment for arsenic?", "Not automatically. The best scope depends on how the water is used and what the verified exposure question actually is."),
                            item("Should I choose RO or adsorptive media for arsenic first?", "Only after you have enough confidence in the result and in the use-case scope.")
                    )
            )),
            Map.entry("lead", winnerDoc(
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
                    splits(
                            split("Lead plus low pH or corrosion clues", "Bias the page toward corrosion correction and plumbing interaction, not just end-of-line shopping."),
                            split("Fixture-specific or first-draw pattern", "Tighten the exposure path before you treat the whole house like the same problem."),
                            split("Vulnerable household or child exposure concern", "Raise urgency and keep the page in exposure-control mode until follow-up testing is clearer.")
                    ),
                    List.of(
                            "Escalate if infants, children, or pregnancy change the exposure threshold for action.",
                            "Escalate if low pH, blue-green stains, or recent plumbing work suggest corrosion is actively shaping the result.",
                            "Escalate if the page is drifting into simple taste or filter logic before the exposure path is understood."
                    ),
                    faq(
                            item("Does lead always mean the well source is the problem?", "No. Plumbing interaction and corrosivity can be part of the decision path."),
                            item("Should I buy a drinking-water filter immediately?", "Not before the page and the follow-up testing narrow where the risk is entering the system."),
                            item("What changes urgency here?", "Vulnerable households, corrosion clues, and uncertainty about where the lead is coming from.")
                    )
            )),
            Map.entry("pfas", winnerDoc(
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
                    splits(
                            split("Claim scope is vague or vendor-led", "Keep the page in evidence mode and verify what the treatment is actually certified or documented to reduce."),
                            split("Drinking-water exposure only", "Bias the decision toward narrower protection scope before the page becomes a whole-house compare exercise."),
                            split("State or local PFAS concern is active", "Raise confidence requirements and use stronger testing plus source guidance before shopping.")
                    ),
                    List.of(
                            "Escalate if PFAS compare shopping starts before testing confidence and claim scope are clear.",
                            "Escalate if the page is collapsing all PFAS questions into one product category answer.",
                            "Escalate if a whole-house assumption is being made without clarifying the real exposure path."
                    ),
                    faq(
                            item("Is PFAS in well water dangerous?", "PFAS pages should start from testing confidence and exposure scope because the risk question matters more than broad filter marketing."),
                            item("Do carbon filters remove PFAS from well water?", "Sometimes, but the page should not assume that without checking claim scope, certification, and the specific treatment path."),
                            item("Should I test PFAS before buying a filter?", "Yes. Tighten the test interpretation and compare categories only after the exposure scope is clear.")
                    )
            )),
            Map.entry("radon", winnerDoc(
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
                    splits(
                            split("State or geology context is strong", "Use that context to widen the testing plan before narrowing to treatment categories."),
                            split("Home exposure assumptions are mixed together", "Separate drinking-water concern from broader home-air assumptions so the page does not oversimplify the decision."),
                            split("Treatment comparison starts too early", "Move back into scope and radionuclide-context clarification before product logic.")
                    ),
                    List.of(
                            "Escalate if the page is treating radon like a generic nuisance issue.",
                            "Escalate if state or geology context suggests related radionuclide questions are being ignored.",
                            "Escalate if a whole-house treatment path is assumed before the use-case is narrowed."
                    ),
                    faq(
                            item("Is radon in well water dangerous?", "It can be, but the page gets stronger when local context, household use, and related radionuclide testing are clear before treatment comparison begins."),
                            item("Should I test indoor air too if I have radon in water?", "Sometimes yes. Separate drinking-water concern from broader home-air assumptions so the page does not oversimplify the decision."),
                            item("Do I need an aeration system for radon in well water?", "Not automatically. Clarify the local context and the real scope of concern before comparing treatment categories.")
                    )
            )),
            Map.entry("ph", winnerDoc(
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
                    splits(
                            split("Low pH plus corrosion clues", "Push the page toward metals follow-up and plumbing interaction before you treat it like neutralization-only shopping."),
                            split("Low pH without exposure clues", "Keep the page focused on corrosion control and system protection rather than overselling health language."),
                            split("Point-of-use taste fix is being considered", "Slow down and decide whether the actual problem is broader corrosivity rather than localized taste.")
                    ),
                    List.of(
                            "Escalate if low pH is paired with lead, copper, or clear corrosion clues.",
                            "Escalate if the page is turning into a filter comparison before corrosion follow-up is resolved.",
                            "Escalate if household exposure questions are being masked as a simple nuisance problem."
                    ),
                    faq(
                            item("Is low pH in well water dangerous?", "Not always, but it can become an exposure and plumbing issue when corrosion clues are present."),
                            item("Does low pH cause copper or lead problems?", "It can. Low pH plus corrosion clues should push the page toward metals follow-up before shopping."),
                            item("Do I need a neutralizer for low-pH well water?", "Maybe, but verify corrosion implications before turning this into a treatment-category decision.")
                    )
            )),
            Map.entry("blue-green-stains", winnerDoc(
                    "Blue-green stains should push the page toward corrosion logic before hardware shopping.",
                    "This page becomes more useful when it connects staining to low pH, plumbing interaction, and possible metals follow-up instead of treating it like a cosmetic issue only.",
                    List.of(
                            "Check whether metallic taste or low pH clues appear with the staining.",
                            "Decide whether the pattern is fixture-specific or system-wide before comparing treatment paths.",
                            "Use the symptom or lab-result tool before shopping from the stain alone."
                    ),
                    List.of(
                            "People often treat blue-green stains like simple discoloration instead of a corrosion clue.",
                            "A fixture pattern can point to plumbing interaction more than source-water treatment.",
                            "The page gets weaker if it skips lead or copper follow-up where the pattern justifies it."
                    ),
                    "Retest after corrosion correction, plumbing work, or any treatment meant to reduce acidic water interaction.",
                    splits(
                            split("Stains plus metallic taste", "Bias the page toward corrosion and metals follow-up rather than a cosmetic-only reading."),
                            split("One fixture or branch only", "Treat plumbing interaction as a stronger hypothesis before whole-house treatment enters the conversation."),
                            split("Whole-house corrosion pattern", "Widen the scope to low pH, corrosivity, and broader plumbing impact before compare-page shopping.")
                    ),
                    List.of(
                            "Escalate if blue-green stains are being dismissed as cosmetic only.",
                            "Escalate if low pH or metallic taste is reinforcing a corrosion pattern.",
                            "Escalate if vulnerable-household exposure concerns make copper or lead follow-up more urgent."
                    ),
                    faq(
                            item("Why is my well water leaving blue-green stains?", "Blue-green stains often act more like a corrosion clue than a cosmetic-only problem."),
                            item("Do blue-green stains mean copper in well water?", "They can point toward copper and corrosion follow-up, especially when low pH or metallic taste is also present."),
                            item("Should I test pH if I see blue-green stains?", "Yes. Low pH, metallic taste, and whether the pattern is local or whole-house usually matter most.")
                    )
            )),
            Map.entry("sulfur-smell-hot-water", winnerDoc(
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
                    splits(
                            split("Hot water only", "Keep the page focused on heater-linked diagnosis and maintenance before whole-house compare logic."),
                            split("Hot and cold water smell", "Move back toward source-water nuisance diagnosis rather than a heater-only explanation."),
                            split("Odor plus staining", "Widen the page to include iron, manganese, or overlapping nuisance clues before you choose a sulfur category.")
                    ),
                    List.of(
                            "Escalate if a heater-only symptom suddenly becomes whole-house.",
                            "Escalate if sulfur odor is paired with staining or a major event change.",
                            "Escalate if the page is skipping heater service logic and going straight to whole-house equipment."
                    ),
                    faq(
                            item("Why does only my hot water smell like sulfur?", "Hot-water-only sulfur odor often points toward heater-specific behavior before it points to a whole-house sulfur problem."),
                            item("Is the water heater causing the sulfur smell?", "It can be. Knowing whether the smell is hot only, cold only, or both is what makes this page more useful."),
                            item("Do I need a whole-house sulfur filter if cold water smells fine?", "Not automatically. Only compare sulfur systems after the page rules out a heater-specific pattern.")
                    )
            )),
            Map.entry("after-heavy-rain", winnerDoc(
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
                    splits(
                            split("Temporary change right after rain", "Treat the page as timing-sensitive and wait for the system to settle before compare-page shopping."),
                            split("Persistent pattern after multiple storms", "Raise the chance that the event is exposing a structural source issue rather than a one-off disturbance."),
                            split("Older lab results are being used anyway", "Lower trust in those older assumptions until the post-rain system is rechecked.")
                    ),
                    List.of(
                            "Escalate if heavy rain repeatedly changes the same symptom pattern.",
                            "Escalate if the page is assuming rain timing is irrelevant to current confidence.",
                            "Escalate if shopping starts before the event-versus-structure question is answered."
                    ),
                    faq(
                            item("Is well water safe after heavy rain?", "Often you should at least downgrade confidence in older assumptions until the system is rechecked."),
                            item("Why did my well water change after heavy rain?", "Rainfall can reset sampling confidence and expose whether the issue is temporary or tied to a deeper structural problem."),
                            item("What should I test after heavy rain affects my well?", "Event timing and verification come first. Clear timing, repeat patterns, and whether the issue persists once the event passes strengthen the page.")
                    )
            )),
            Map.entry("new-baby-at-home", winnerDoc(
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
                    splits(
                            split("Recent data is weak or stale", "Raise urgency and move toward safer temporary drinking-water logic until stronger evidence exists."),
                            split("Known nitrate or microbial concern exists", "Treat the page as exposure-management first and treatment comparison second."),
                            split("Shopping starts from household status alone", "Slow down and use vulnerability to tighten verification rather than to jump to hardware.")
                    ),
                    List.of(
                            "Escalate if infant drinking-water decisions are still relying on weak or older evidence.",
                            "Escalate if a known nitrate or microbial question is already in scope.",
                            "Escalate if treatment browsing is replacing verification urgency."
                    ),
                    faq(
                            item("Is my well water safe for baby formula?", "Do not assume so from weak or older evidence. A new baby makes the drinking-water decision more cautious right away."),
                            item("What well water tests matter most for a new baby?", "Nitrate, microbial, and other drinking-water questions that change infant exposure should move to the front of the testing plan."),
                            item("Should I switch water sources until I retest?", "Yes when recent evidence is weak or stale. Vulnerable-household logic should raise verification urgency before shopping.")
                    )
            )),
            Map.entry("pregnancy-in-home", winnerDoc(
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
                    splits(
                            split("Evidence is weak or old", "Treat the page as a stronger verification problem before equipment enters the discussion."),
                            split("Known contaminant concern already exists", "Keep the page in safer-drinking-water and certified-testing logic first."),
                            split("One-size-fits-all product assumption appears", "Break the page back into exposure question, evidence quality, and only then compare logic.")
                    ),
                    List.of(
                            "Escalate if pregnancy is changing the acceptable evidence threshold and the page has not caught up.",
                            "Escalate if treatment browsing is happening before the drinking-water path is clearly supported by data.",
                            "Escalate if the page is treating all contaminant questions as if they need the same product answer."
                    ),
                    faq(
                            item("Why does pregnancy change the page logic?", "Because the page should become less tolerant of weak evidence and slower shopping."),
                            item("Is this page mainly about products?", "No. It is mainly about verification posture and safer drinking-water decisions."),
                            item("What should happen next?", "Use the stronger testing or result path before settling on equipment.")
                    )
            )),
            Map.entry("new-hampshire-arsenic-well-water", winnerDoc(
                    "New Hampshire arsenic pages should act like bedrock-risk pages first, not generic arsenic shopping pages.",
                    "This regional page is strongest when it treats New Hampshire arsenic as a local-scope problem: certified follow-up, bedrock context, and related radionuclide questions should enter before product certainty does.",
                    List.of(
                            "Use the New Hampshire arsenic guidance and lab path before comparing treatment categories.",
                            "Check whether bedrock context widens the next panel beyond arsenic alone.",
                            "Keep drinking-water exposure and verification ahead of whole-house shopping assumptions."
                    ),
                    List.of(
                            "People often treat a New Hampshire arsenic page like a generic national arsenic page.",
                            "A bedrock-shaped state page weakens when it assumes one analyte answers the whole risk picture.",
                            "The page gets weaker if it jumps to treatment before the state testing path is clear."
                    ),
                    "Retest on the state-aware cadence that fits the household risk, the sample age, and any wider bedrock panel still being scoped.",
                    splits(
                            split("Result exists but bedrock scope is unclear", "Use the page to widen the testing plan before you narrow the treatment plan."),
                            split("Household wants to shop from one arsenic number", "Slow the decision down and re-anchor it to New Hampshire arsenic guidance plus lab follow-up."),
                            split("Related bedrock risks may still be open", "Treat the page like a broader testing-order decision instead of a one-category purchase page.")
                    ),
                    List.of(
                            "Escalate if the page is treating New Hampshire context like a generic national arsenic page.",
                            "Escalate if bedrock scope questions are still open but shopping has already started.",
                            "Escalate if a stale or weak sample is being used like settled truth."
                    ),
                    faq(
                            item("Why is New Hampshire different for arsenic in private wells?", "Because the page should stay tied to New Hampshire arsenic guidance, local lab pathways, and bedrock-shaped scope decisions before shopping."),
                            item("Should I only test arsenic in New Hampshire?", "Not always. Bedrock context can widen what belongs in the next panel."),
                            item("What should I do before comparing arsenic treatment?", "Use the New Hampshire guidance and a certified lab path first.")
                    )
            )),
            Map.entry("florida-rotten-egg-smell-well-water", winnerDoc(
                    "Florida sulfur-smell pages should separate hot-water-only odor from whole-house nuisance patterns before they behave like filter pages.",
                    "This regional page works when it uses Florida odor and staining guidance to map the pattern first, then moves into testing and compare logic only after the smell behaves like a system-wide issue.",
                    List.of(
                            "Map whether the smell is hot-water-only, cold-water-only, or whole-house before you shop.",
                            "Use Florida odor and staining guidance when sulfur smell overlaps with stains or broader nuisance clues.",
                            "Compare sulfur treatment paths only after the cause pattern is tighter."
                    ),
                    List.of(
                            "People often buy sulfur equipment before they separate heater-only odor from whole-house odor.",
                            "A Florida odor page gets weaker if it ignores staining and overlap clues.",
                            "The page should not treat a smell report like a settled whole-house diagnosis."
                    ),
                    "Re-evaluate after heater service, disinfection, or any change in whether the odor is isolated or system-wide.",
                    splits(
                            split("Hot-water-only odor", "Keep the page focused on heater behavior before whole-house equipment enters the frame."),
                            split("Odor plus staining", "Use the Florida nuisance pattern to widen the testing and compare path before shopping."),
                            split("Whole-house odor after a recent change", "Treat the page as a stronger verification problem before equipment assumptions harden.")
                    ),
                    List.of(
                            "Escalate if odor changed after storm, repair, or long inactivity.",
                            "Escalate if staining or other nuisance clues overlap with the smell.",
                            "Escalate if whole-house treatment shopping starts before the odor map is clear."
                    ),
                    faq(
                            item("Why does rotten egg smell in Florida well water need a different page?", "Because Florida odor and staining context can change the likely cause pattern and the order of testing."),
                            item("Does hot-water-only sulfur smell mean I need whole-house treatment?", "No. Hot-water-only odor should usually slow the decision down and keep heater behavior in scope first."),
                            item("What should I test before comparing sulfur equipment?", "Test the nuisance pattern after you map where the odor shows up.")
                    )
            )),
            Map.entry("iowa-nitrate-baby-well-water", winnerDoc(
                    "Iowa nitrate plus baby-risk pages should behave like safer-drinking-water pages first, not general treatment pages.",
                    "This regional page is strongest when it treats Iowa nitrate risk, infant feeding, and repeat testing as the main decision rather than broad equipment browsing.",
                    List.of(
                            "Move to safer drinking water faster when infant or pregnancy context is active.",
                            "Use the Iowa nitrate guidance and certified lab path before you compare treatment categories.",
                            "Keep the page centered on drinking-water exposure and repeat screening cadence."
                    ),
                    List.of(
                            "People often underreact because nitrate has no smell or visible clue.",
                            "An Iowa nitrate page weakens when it talks like a generic nuisance page.",
                            "The page should not skip from vulnerable-household risk to whole-house shopping."
                    ),
                    "Retest on the cadence that fits the household vulnerability and the local nitrate screening pattern before you assume the risk is settled.",
                    splits(
                            split("Infant feeding is active", "Treat the page as a safer-water and certified-testing page before a product page."),
                            split("Result exists but timing is unclear", "Repeat the screening before you harden the treatment decision."),
                            split("Whole-house shopping starts early", "Pull the page back to drinking-water exposure and household risk first.")
                    ),
                    List.of(
                            "Escalate if infant feeding or pregnancy is active right now.",
                            "Escalate if the page is behaving like nitrate is a cosmetic issue.",
                            "Escalate if one result is being treated like a permanent all-clear or permanent verdict."
                    ),
                    faq(
                            item("Why is Iowa nitrate plus baby context a separate page?", "Because infant-risk logic should change urgency, testing cadence, and the order of next actions."),
                            item("Should I compare treatment immediately for Iowa nitrate?", "Not before safer drinking water and certified follow-up are in place."),
                            item("What matters most on this page?", "Exposure management and repeat screening discipline.")
                    )
            )),
            Map.entry("connecticut-low-ph-blue-green-stains", winnerDoc(
                    "Connecticut low-pH pages should treat corrosion clues like a sequence problem first, not a neutralizer recommendation first.",
                    "This regional page gets stronger when it keeps low pH, blue-green staining, and metal follow-up tied together instead of collapsing into one hardware answer.",
                    List.of(
                            "Use Connecticut private well testing guidance before you shop from blue-green stains alone.",
                            "Keep low pH, corrosion clues, and metal follow-up in the same page logic.",
                            "Compare neutralizer paths only after the corrosion sequence is clearer."
                    ),
                    List.of(
                            "People often jump to neutralizer shopping from staining alone.",
                            "A Connecticut corrosion page gets weaker if it ignores copper or lead follow-up.",
                            "The page should not assume source water and plumbing interaction are already separated."
                    ),
                    "Retest after pH correction, plumbing changes, or any intervention that should change corrosion behavior.",
                    splits(
                            split("Blue-green stains plus metallic clues", "Keep the page in corrosion and metals follow-up before shopping."),
                            split("Low pH result exists but sample context is weak", "Raise method discipline before treatment confidence."),
                            split("User wants one neutralizer answer", "Pull the page back to corrosion sequence, plumbing interaction, and lab follow-up.")
                    ),
                    List.of(
                            "Escalate if corrosion clues are being treated like nuisance-only symptoms.",
                            "Escalate if low pH shopping starts before metals follow-up is scoped.",
                            "Escalate if the page is hiding plumbing interaction behind one product answer."
                    ),
                    faq(
                            item("Why is Connecticut low pH with blue-green stains a separate page?", "Because corrosion sequence and testing order matter more than a one-step neutralizer answer."),
                            item("Should I buy a neutralizer immediately?", "Not before the page separates corrosion clues, plumbing interaction, and metals follow-up."),
                            item("What changes the answer fastest here?", "Whether staining, low pH, and metal clues point toward a broader corrosion path.")
                    )
            )),
            Map.entry("pennsylvania-private-well-radon", winnerDoc(
                    "Pennsylvania radon pages should act like scope and verification pages before they act like category pages.",
                    "This regional page works when it explains how radon testing, related radionuclide questions, and certified lab paths shape the next action before compare logic takes over.",
                    List.of(
                            "Use Pennsylvania private well testing guidance before comparing aeration and GAC.",
                            "Check whether the page still needs broader radionuclide follow-up before treatment narrowing starts.",
                            "Treat radon like a scope question first and a category question second."
                    ),
                    List.of(
                            "People often jump to aeration versus GAC before the testing path is settled.",
                            "A Pennsylvania radon page weakens when it acts like radon scope is already clear.",
                            "The page should not treat one test context like the whole radionuclide picture."
                    ),
                    "Retest or widen the panel when the radon question still overlaps with broader radionuclide uncertainty or weak sample context.",
                    splits(
                            split("Radon result exists but broader scope is open", "Keep the page on testing and scope discipline before equipment narrowing."),
                            split("User wants to compare categories immediately", "Pull the page back to Pennsylvania testing guidance and certified lab fit."),
                            split("Home-sale or property-transfer context overlaps", "Use the page to separate transaction timing from long-term household planning.")
                    ),
                    List.of(
                            "Escalate if the page is narrowing to equipment before the testing path is clear.",
                            "Escalate if broader radionuclide uncertainty still exists.",
                            "Escalate if a transfer-related test is being treated like the whole household answer."
                    ),
                    faq(
                            item("Why is Pennsylvania private well radon a separate page?", "Because the state testing path and radionuclide scope can change what should happen before compare logic."),
                            item("Should I compare radon treatment categories right away?", "Not before the page is clear about testing scope and certified lab follow-up."),
                            item("What should I do first?", "Use Pennsylvania testing guidance and tighten the scope before narrowing to treatment categories.")
                    )
            )),
            Map.entry("new-jersey-pwta-private-well-testing", winnerDoc(
                    "New Jersey PWTA is a transfer-test floor, not a full household-safety verdict.",
                    "This regional page works best when it separates what the PWTA requires for a sale from what a buyer or household may still need to test after looking at geology, family risk, and old evidence quality.",
                    List.of(
                            "Start with the PWTA requirement if a sale or transfer is driving the timeline.",
                            "Use an NJDEP-certified lab and do not swap in a generic national checklist when the sale process is state-shaped.",
                            "Check whether family risk, property history, or local geology means the required panel is still too narrow."
                    ),
                    List.of(
                            "People often treat the PWTA panel like a full all-clear for long-term household safety.",
                            "Buyers can confuse transaction requirements with living-in-the-house decisions.",
                            "A New Jersey page gets weaker if it talks like a generic home-sale article and ignores the state process."
                    ),
                    "Retest after closing when the sale panel was narrow, old, or clearly weaker than what the household actually needs for daily use.",
                    splits(
                            split("Sale is active and timing is tight", "Keep the page anchored to the PWTA process first, then separate what still belongs in the household plan."),
                            split("Household risk is higher than the transfer panel covers", "Widen the testing plan beyond the required panel before treating the sale process like the full answer."),
                            split("User wants to shop from the sale panel alone", "Slow down and split transaction compliance from long-term drinking-water decisions.")
                    ),
                    List.of(
                            "Escalate if the household includes infants, pregnancy, or other high-risk drinking-water use the PWTA panel does not fully answer.",
                            "Escalate if the page is treating a required transfer panel like a lifetime all-clear.",
                            "Escalate if treatment shopping starts before the sale panel and the household panel are clearly separated."
                    ),
                    faq(
                            item("What does the New Jersey PWTA test for?", "It sets a required transfer-testing path, but that required panel should be treated like a floor rather than a full household-safety verdict."),
                            item("Do I need a certified lab for New Jersey PWTA testing?", "Yes. Use an NJDEP-certified lab when the sale or transfer process puts PWTA testing on the clock."),
                            item("Do I need extra testing beyond the PWTA panel?", "Sometimes yes. Family risk, property history, and local geology can make the required panel too narrow for real household decisions.")
                    )
            )),
            Map.entry("how-to-read-a-well-water-lab-report", winnerDoc(
                    "A well-water lab report is only useful if you read the unit, qualifier, sample point, and sample date before reacting to the number.",
                    "This authority page should reduce false certainty. The real question is not just what the result says, but whether the sample and method are strong enough to support the decision you are about to make.",
                    List.of(
                            "Check the analyte name, unit, qualifier, sample date, and whether the sample came from raw water or treated water.",
                            "Compare the number against the right benchmark before turning one printed result into a buying decision.",
                            "Treat stale, mixed-up, or weakly collected samples as a reason to retest instead of forcing interpretation."
                    ),
                    List.of(
                            "People often read the number and ignore the unit or qualifier.",
                            "A report can look precise while still being too old, too weak, or sampled from the wrong place for the decision at hand.",
                            "This page gets weaker if it explains chemistry but does not feed the user back into a better test or scope decision."
                    ),
                    "Retest when the report is stale, the sample point is unclear, the unit reading is ambiguous, or the result does not match the decision stakes.",
                    splits(
                            split("Result is recent and clearly sampled", "Move toward interpretation and next-step narrowing with more confidence."),
                            split("Result is stale or sample context is weak", "Treat the page as a retest and evidence-quality problem before a treatment problem."),
                            split("User wants to shop from a single number", "Pull the page back to units, qualifiers, benchmark fit, and sample quality first.")
                    ),
                    List.of(
                            "Escalate if the report is old but is still being used like current truth.",
                            "Escalate if unit confusion, qualifiers, or treated-versus-raw sampling are being ignored.",
                            "Escalate if the page is skipping from one printed result to a treatment category too fast."
                    ),
                    faq(
                            item("How do I read a well water lab report?", "Start with the analyte name, unit, qualifier, sample date, and whether the sample came from raw or treated water before you interpret the number."),
                            item("What do ppb and ppm mean on a well water test?", "Units change the meaning of the number, which is why a report should not be interpreted until the unit is clear."),
                            item("Does a passing well water test mean my water is safe?", "Not automatically. Sample age, sample quality, and whether the test scope matched the real household risk still matter.")
                    )
            )),
            Map.entry("private-well-home-sale-testing-by-state", winnerDoc(
                    "Home-sale well testing changes by state, but the sale panel is still not the same thing as a full long-term household plan.",
                    "This authority page should help users separate state or lender requirements from the broader question of what a family needs to know before living on the well every day.",
                    List.of(
                            "Start with the state, lender, or transfer requirement that actually applies to the sale.",
                            "Check whether geology, household vulnerability, or property history means the minimum sale panel is not enough.",
                            "Use certified labs and a state-aware checklist before turning a transfer result into a treatment purchase."
                    ),
                    List.of(
                            "People often assume every state handles private well sale testing the same way.",
                            "A passing transfer panel can still leave major household questions unanswered.",
                            "This page gets weaker if it reads like a generic checklist without separating legal minimums from practical household risk."
                    ),
                    "Retest after closing when the sale panel was limited, old, or clearly not matched to the household that will actually use the water.",
                    splits(
                            split("State or lender requirement is explicit", "Use that as the starting point, not the whole answer."),
                            split("Household risk is broader than the transfer panel", "Expand beyond the minimum before treating the sale panel like final evidence."),
                            split("Buyer wants to shop immediately after a passing panel", "Slow down and separate transaction success from long-term water confidence.")
                    ),
                    List.of(
                            "Escalate if the user is assuming every state follows the same sale-testing rules.",
                            "Escalate if the sale panel is being treated like a complete safety plan for the next owner.",
                            "Escalate if treatment shopping starts before state requirements and household needs are split apart."
                    ),
                    faq(
                            item("Is private well testing required when selling a house?", "It depends on the state, lender, and transfer context, which is why this page separates state requirements from generic advice."),
                            item("What well water tests are required for a home sale?", "Start with the state or lender requirement, then check whether geology, family risk, or property history means more testing belongs in scope."),
                            item("Is a passing transfer well test enough to buy the house?", "Not automatically. A transfer panel can satisfy the sale and still leave long-term household questions unanswered.")
                    )
            )),
            Map.entry("test-kit-vs-certified-lab", winnerDoc(
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
                    splits(
                            split("Quick screen question only", "A kit may stay in scope, but the page should still protect against overconfident treatment decisions."),
                            split("Health, sale, or verification decision", "Bias the page strongly toward certified lab evidence and away from convenience-first logic."),
                            split("User is comparing speed and price only", "Pull the page back to confidence quality before cost and turnaround.")
                    ),
                    List.of(
                            "Escalate if a kit result is being used as if it were decision-grade evidence.",
                            "Escalate if sale, health, or treatment-verification stakes are present.",
                            "Escalate if convenience language is outrunning method-fit language on the page."
                    ),
                    faq(
                            item("Is a home kit enough for a treatment decision?", "Not always. The page should separate quick screening from certified decision-grade evidence."),
                            item("When does a certified lab matter most?", "When the page is shaping health, sale, escalation, or treatment-verification decisions."),
                            item("What is the main mistake here?", "Confusing a faster answer with a strong enough answer.")
                    )
            )),
            Map.entry("mail-in-lab-vs-local-certified-lab", winnerDoc(
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
                    splits(
                            split("Strict timing or chain-of-custody matters", "Bias the page toward the lab path that better protects sample integrity and decision confidence."),
                            split("Local rules or sale process apply", "Raise the value of local certified pathways before convenience becomes the main argument."),
                            split("Shipping and handling risk is high", "Treat sample integrity as the decision bottleneck instead of price or turnaround.")
                    ),
                    List.of(
                            "Escalate if convenience is being treated as more important than evidence fit.",
                            "Escalate if state or local requirements may invalidate the easier path.",
                            "Escalate if shipping, handling, or custody doubts would weaken the result's usefulness."
                    ),
                    faq(
                            item("Is local certified always better than mail-in?", "Not automatically, but the page should compare confidence fit, timing, and process risk instead of convenience alone."),
                            item("Why does sampling discipline matter here?", "Because the best lab path still fails if the sample handling does not fit the question."),
                            item("What should happen before shopping from the result?", "Make sure the lab path actually produced decision-grade evidence.")
                    )
            )),
            Map.entry("private-well-sampling-mistakes-that-break-results", winnerDoc(
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
                    splits(
                            split("Timing was poor", "Treat the page as a sampling-confidence problem before you blame treatment or source behavior."),
                            split("Handled or labeled sample is uncertain", "Lower trust in the result and return to certified retesting rather than interpreting noise as truth."),
                            split("Treated versus raw sample is mixed up", "Reframe the page around method fit because the wrong sampling point can break the whole decision.")
                    ),
                    List.of(
                            "Escalate if a printed result is being treated like ground truth without sample review.",
                            "Escalate if the page is blaming treatment failure before checking method quality.",
                            "Escalate if sample timing, handling, or source selection makes the result hard to trust."
                    ),
                    faq(
                            item("Can a sampling mistake really change the treatment decision?", "Yes. It can turn a confident-looking result into weak guidance."),
                            item("When should I stop and retest?", "When sample timing, handling, or source selection makes the result difficult to trust."),
                            item("Why is this an organic winner page?", "Because users often need method clarity before they can trust any later compare or product page.")
                    )
            )),
            Map.entry("iron", winnerDoc(
                    "Iron is usually a nuisance and maintenance problem first, but the page should still separate source-water iron from corrosion or disturbance before treatment shopping.",
                    "Most iron pages lean toward nuisance, staining, and maintenance, but sudden sediment, metallic change, or low-pH clues can make the page less about simple iron equipment and more about broader source or plumbing verification.",
                    List.of(
                            "Check whether the water runs clear and stains later or already looks rusty when it leaves the tap.",
                            "Use a recent untreated-water result and map where staining or sediment appears before comparing filter categories.",
                            "Check manganese, pH, and any recent well or plumbing disturbance before you lock into a softener or iron-filter path."
                    ),
                    List.of(
                            "People often assume every orange stain or rusty particle problem is the same kind of iron issue.",
                            "Clear-water iron and already-oxidized iron can lead to different maintenance and equipment decisions.",
                            "A softener versus iron-filter comparison is weak if the page has not separated iron from corrosion or post-disturbance sediment."
                    ),
                    "Retest after treatment changes, well service, sediment events, or any shift in whether the water runs clear or rusty.",
                    splits(
                            split("Water runs clear then turns orange or leaves stains later", "Bias the page toward dissolved iron behavior and make sure the compare path fits that pattern instead of visible sediment alone."),
                            split("Water is already rusty or carries visible sediment", "Keep oxidation, disturbance, or source instability in scope before treating the problem like a simple nuisance stain."),
                            split("Iron clues plus low pH or metallic taste", "Re-open corrosion and plumbing interaction before you buy source-water equipment from an iron label alone.")
                    ),
                    List.of(
                            "Escalate if iron-like staining or sediment changed suddenly after flood, repair, or pump work.",
                            "Escalate if iron clues travel with black staining, odor, or other nuisance overlap that widens the scope.",
                            "Escalate if sediment, pressure, or clarity changes make the page look more like a disturbance problem than a stable iron pattern."
                    ),
                    faq(
                            item("Does iron in well water always mean I need a filter?", "No. First separate dissolved iron, visible sediment, and corrosion-shaped clues before you treat iron like a settled equipment decision."),
                            item("Why does clear water still leave orange stains later?", "That pattern often points toward dissolved iron that oxidizes after the water stands rather than rust-colored water at the tap."),
                            item("Should I compare a softener and an iron filter right away?", "Not until the page has mapped how the iron shows up and whether manganese, pH, or disturbance changed the problem.")
                    )
            )),
            Map.entry("manganese", winnerDoc(
                    "Manganese can look like a nuisance staining page, but higher levels and drinking-water use can justify a more cautious health and scope check before shopping.",
                    "Black staining and nuisance residue often drive discovery, yet manganese should not be treated as only cosmetic when household drinking-water exposure or infant use is in scope.",
                    List.of(
                            "Confirm the result with a recent certified sample and note whether the concern is staining, taste, or drinking-water use.",
                            "Check iron, pH, and where black residue appears before treating manganese like a single-contaminant equipment decision.",
                            "Decide whether the real scope is whole-house nuisance control, drinking-water exposure control, or both."
                    ),
                    List.of(
                            "People often treat black staining as automatically harmless nuisance without checking the actual lab value.",
                            "Manganese and iron often overlap, which can make a one-category treatment decision too simple.",
                            "The page gets weaker when it assumes every manganese issue deserves the same whole-house answer."
                    ),
                    "Retest after any treatment install, source disturbance, or change in residue pattern or household drinking-water use.",
                    splits(
                            split("Black staining or dark residue is the main clue", "Keep nuisance maintenance in scope, but do not let the staining alone stand in for a current lab result."),
                            split("Higher lab result with infant or formula use in scope", "Move the page toward faster drinking-water verification instead of routine nuisance shopping."),
                            split("Manganese plus iron, odor, or broader discoloration", "Treat the problem like a mixed nuisance pattern before choosing one treatment category.")
                    ),
                    List.of(
                            "Escalate if infant drinking-water use or formula preparation is part of the household decision.",
                            "Escalate if dark residue is paired with taste, odor, or other water-quality changes that suggest a wider scope.",
                            "Escalate if the page is shopping from a stain clue alone without a recent verified result."
                    ),
                    faq(
                            item("Is manganese in well water only a staining problem?", "Not always. Staining is a common clue, but drinking-water use and the actual lab value still matter before treatment decisions."),
                            item("Why do manganese and iron get confused so often?", "Because they can overlap in staining and nuisance symptoms, which is why the page should not force a one-contaminant answer too early."),
                            item("When should I move faster on manganese?", "When the result is high enough to raise drinking-water concern or when infant use makes a cautious exposure check more important.")
                    )
            )),
            Map.entry("after-wildfire", winnerDoc(
                    "After wildfire, treat the well like a changed system until inspection and post-fire testing restore confidence.",
                    "This is not a normal nuisance-odor or routine maintenance page. Fire can damage well components, nearby infrastructure, and confidence in both microbiological and chemical safety.",
                    List.of(
                            "Check for visible damage, loss of pressure, electrical problems, or signs that the wellhead or nearby plumbing was affected by fire.",
                            "Use safer temporary water until local guidance, inspection, and the first post-fire tests support normal use.",
                            "At minimum re-establish baseline bacteria testing and add certified chemical follow-up when fire damage, plastics, smoke, or local contamination are in play."
                    ),
                    List.of(
                            "People often assume clear water means the wildfire did not change the well.",
                            "A quick disinfection step is not the same as a post-fire inspection and testing plan.",
                            "Shopping for treatment too early can replace the harder work of defining what wildfire actually changed."
                    ),
                    "Retest after the initial fire response, again after repairs or disinfection, and again if odor, color, pressure, or local guidance shifts.",
                    splits(
                            split("Direct heat or physical damage reached the well system", "Treat the page like a damaged-infrastructure problem first and restore inspection plus safer water before compare logic."),
                            split("No visible damage but ash, smoke, or evacuation context exists", "Lower trust in old assumptions and widen the page toward certified post-fire testing before normal use confidence returns."),
                            split("Only one strange symptom appeared after the fire", "Do not collapse the page into one nuisance explanation until wildfire context is ruled out as the bigger cause.")
                    ),
                    List.of(
                            "Escalate if there is visible damage, loss of pressure, or signs that the fire affected the well system.",
                            "Escalate if the water has a chemical odor, unusual taste, or other post-fire change that routine nuisance pages do not explain well.",
                            "Escalate if local officials issued fire-related water or contamination guidance affecting the property."
                    ),
                    faq(
                            item("Is well water safe right after a wildfire?", "Not until inspection, local guidance, and post-fire testing rebuild confidence in the system."),
                            item("Does clear well water mean wildfire did not affect it?", "No. Fire can still change confidence in the well even when the water does not look obviously damaged."),
                            item("What should I test after wildfire affects my property?", "Re-establish bacteria testing at a minimum and add certified chemical follow-up when the fire may have damaged components or affected nearby contamination pathways.")
                    )
            )),
            Map.entry("private-well-testing-schedule-by-household", winnerDoc(
                    "A yearly testing schedule is only the floor; household type, location, and recent events should change both cadence and scope.",
                    "Routine household testing is less about nuisance versus health than about not letting a quiet exposure risk hide behind an outdated annual habit.",
                    List.of(
                            "Start with a baseline annual routine and then add state or local risk factors that the generic schedule misses.",
                            "Tighten the schedule when infants, pregnancy, immune sensitivity, or drinking-water-only concerns raise the cost of waiting.",
                            "Reset the clock after floods, repairs, treatment installs, taste changes, or any event that weakens trust in older results."
                    ),
                    List.of(
                            "People often treat one annual panel like a full answer for every household and every year.",
                            "A sale test or old baseline result does not replace routine testing after the household changes.",
                            "The schedule gets weaker if it ignores geography, local contaminants, or vulnerable household use."
                    ),
                    "Retest annually at a minimum and accelerate after household changes, trigger events, or any result that did not fully answer the decision.",
                    splits(
                            split("Infants, pregnancy, or vulnerable use is in scope", "Bias the page toward tighter drinking-water schedules and faster follow-up instead of routine annual comfort."),
                            split("Agriculture, bedrock, or local contaminant alerts shape the property", "Expand beyond the generic checklist because location changes what belongs in the household schedule."),
                            split("A recent event or treatment change reset confidence", "Treat the schedule like a moving plan and restart retesting sooner than the normal annual cycle.")
                    ),
                    List.of(
                            "Escalate if there is no recent test and the household now includes infants or pregnancy.",
                            "Escalate if repeated positives or recurring symptoms are being hidden behind an old annual result.",
                            "Escalate if flood, repair, or treatment changes made the last routine schedule no longer trustworthy."
                    ),
                    faq(
                            item("How often should a private well be tested?", "At least annually for a baseline, but household risk, geography, and trigger events can require a faster schedule."),
                            item("Does a home-sale test replace routine yearly testing?", "No. A sale test answers a transaction question, not every future household risk question."),
                            item("When should the schedule change fastest?", "When infants, pregnancy, local contaminant risk, or major events like flood or repair make the old cadence too slow.")
                    )
            )),
            Map.entry("new-york-pfas-private-wells", winnerDoc(
                    "New York PFAS well pages should be certified-testing and claim-check pages first, not filter-shopping pages.",
                    "PFAS belongs on the health and drinking-water side of the decision. The page is strongest when it separates lab scope, analyte fit, and claim verification from generic filter marketing.",
                    List.of(
                            "Use a certified lab and current New York guidance before treating PFAS as a settled filter purchase.",
                            "Define whether the household decision is drinking-water-only exposure control, broader screening, or follow-up after a local advisory.",
                            "Compare treatment claims only after the tested analytes and product certifications actually match."
                    ),
                    List.of(
                            "People often buy a PFAS filter from national marketing before they have a New York-specific test plan.",
                            "Not every PFAS page should jump from one concern to a whole-house system verdict.",
                            "A carbon or RO claim is weak if it is not tied to the analytes actually tested on the well."
                    ),
                    "Retest after a positive finding, after treatment install, or when local guidance or nearby contamination context changes the lab scope.",
                    splits(
                            split("A local advisory or nearby contamination source is part of the context", "Raise the value of certified lab scope and New York guidance before the page opens treatment shopping."),
                            split("The household mainly needs drinking-water exposure control", "Keep the page focused on exposure fit before it drifts into broader whole-house assumptions."),
                            split("A product claim does not clearly match the tested analytes", "Treat the page as a claim-check problem before any filter category becomes the answer.")
                    ),
                    List.of(
                            "Escalate if infant, pregnancy, or high drinking-water reliance is shaping the PFAS decision right now.",
                            "Escalate if a local PFAS notice or nearby source changes what testing belongs in scope.",
                            "Escalate if treatment shopping is outrunning certified testing and claim verification."
                    ),
                    faq(
                            item("Should I buy a PFAS filter before testing my private well?", "No. Start with certified testing and New York-specific guidance before you treat PFAS as a settled equipment decision."),
                            item("Is carbon or RO automatically the right PFAS answer?", "Not automatically. The page should check analyte fit and certification before either category becomes the recommendation."),
                            item("Why does New York context matter for PFAS?", "Because local guidance and contamination context can change what belongs in the testing scope before treatment claims are compared.")
                    )
            )),
            Map.entry("sulfur-smell-hot-water-vs-whole-house", winnerDoc(
                    "Sulfur smell location changes the likely cause faster than treatment category pages do.",
                    "Most sulfur odor pages start as nuisance diagnosis, but hot-water-only odor often points toward heater behavior while whole-house odor keeps source-water testing and broader nuisance overlap in scope.",
                    List.of(
                            "Map whether the odor is hot-only, cold-only, or whole-house before treating sulfur smell like one settled problem.",
                            "Check water-heater context, recent maintenance, and whether orange or black staining travel with the smell.",
                            "Use targeted testing or the symptom-first flow before buying whole-house sulfur equipment."
                    ),
                    List.of(
                            "People often treat hot-water sulfur odor like automatic proof of a whole-house sulfur problem.",
                            "A heater-specific smell can send the page into the wrong compare path if location is not mapped first.",
                            "The page loses value when it treats odor alone as a full diagnosis without staining or timing context."
                    ),
                    "Retest or re-evaluate after heater service, disinfection, or any change in where the odor appears.",
                    splits(
                            split("Odor is hot water only", "Bias the page toward heater behavior, heater maintenance, or anode-related troubleshooting before whole-house treatment."),
                            split("Odor appears on both hot and cold or across the whole house", "Keep source-water testing and broader nuisance treatment paths in scope."),
                            split("Odor travels with staining or sediment", "Widen the page beyond sulfur smell alone and check iron, manganese, and mixed nuisance patterns.")
                    ),
                    List.of(
                            "Escalate if the smell started after flood, repair, or a long vacancy changed the system.",
                            "Escalate if the odor suddenly moved from one fixture or hot-water line to the whole house.",
                            "Escalate if odor is paired with discoloration, sediment, or other clues that make the scope broader than heater behavior."
                    ),
                    faq(
                            item("Why does only my hot water smell like sulfur?", "Hot-water-only sulfur odor often points toward heater behavior before it points to a whole-house sulfur problem."),
                            item("Should I buy a whole-house sulfur filter if only the hot water smells bad?", "Not before the page checks heater-specific causes and maps where the odor really appears."),
                            item("What changes when the sulfur smell is whole-house instead?", "Whole-house odor keeps source-water testing and broader nuisance overlap in scope instead of narrowing straight to heater troubleshooting.")
                    )
            )),
            Map.entry("hardness", winnerDoc(
                    "Hardness is usually a nuisance and appliance-protection page first, not a health page.",
                    "Scale, soap, and appliance burden drive most hardness decisions, but the page should still separate whole-house comfort problems from unsupported drinking-water claims.",
                    List.of(
                            "Confirm the hardness result and units before you treat scale or soap issues like a settled equipment verdict.",
                            "Map whether the burden is mostly fixtures, appliances, water-heater scale, or whole-house cleaning pain.",
                            "Compare salt, maintenance, and fit only after the page decides whether hardness is the real dominant problem."
                    ),
                    List.of(
                            "People often treat hard water like a health emergency when the real issue is maintenance and appliance wear.",
                            "A bigger softener is not automatically a better answer if the page has not mapped where hardness hurts most.",
                            "Hardness can overlap with iron or manganese nuisance patterns, which weakens a one-cause treatment decision."
                    ),
                    "Retest after softener changes, source-water shifts, or any major change in scaling and soap performance that suggests the old result is stale.",
                    splits(
                            split("Scale and water-heater burden are the main issue", "Keep the page focused on household nuisance and maintenance before it drifts into broader drinking-water claims."),
                            split("Soap, laundry, and cleaning burden affect the whole house", "Bias the page toward whole-house comfort economics rather than a narrow appliance-only fix."),
                            split("Hardness plus staining or sediment is in scope", "Re-open iron, manganese, or mixed nuisance patterns before you lock into a hardness-only answer.")
                    ),
                    List.of(
                            "Escalate if the page is using hardness alone to imply a drinking-water safety problem.",
                            "Escalate if staining, sediment, or odor make the nuisance pattern broader than simple hard water.",
                            "Escalate if a treatment recommendation appears before the page maps where hardness is actually causing burden."
                    ),
                    faq(
                            item("Is hard well water dangerous to drink?", "Hardness is usually a nuisance and maintenance issue first, not a drinking-water emergency."),
                            item("Do I always need a softener for hard well water?", "No. First decide whether the real problem is scale, cleaning burden, appliance wear, or a mixed nuisance pattern."),
                            item("Why should I test before buying for hard water?", "Because the page gets stronger when it separates hardness from other well-water issues instead of treating every scale clue the same way.")
                    )
            )),
            Map.entry("uranium", winnerDoc(
                    "Uranium in a well is a drinking-water verification page first and a treatment page second.",
                    "This belongs on the health and exposure-control side of the decision. The page is strongest when it treats uranium like a certified-testing and scope problem before it becomes a category-shopping page.",
                    List.of(
                            "Use a recent certified result and safer drinking water when the uranium result is new, unclear, or household exposure is still unresolved.",
                            "Check whether arsenic, radium, radon, or bedrock context widen the page beyond one lab line.",
                            "Choose treatment scope only after the page decides whether the real concern is drinking-water exposure, a broader radionuclide cluster, or post-treatment verification."
                    ),
                    List.of(
                            "People often jump from the uranium label straight to equipment without confirming the result and the rest of the radionuclide context.",
                            "A drinking-water exposure page should not drift into whole-house shopping before the scope is actually defined.",
                            "The page gets weaker when it treats uranium like an isolated number and ignores regional geology or related contaminants."
                    ),
                    "Retest after any treatment install, lab confirmation gap, or change in regional context that affects radionuclide follow-up.",
                    splits(
                            split("A new uranium result is the only signal", "Treat the page like a certified-testing and exposure-scope problem before it becomes a treatment comparison."),
                            split("Regional radionuclide or bedrock context is already known", "Widen the page to radium, radon, arsenic, or geology-aware follow-up instead of one-number shopping."),
                            split("Post-treatment verification is the real question", "Bias the page toward retest timing and proof instead of restarting from raw fear.")
                    ),
                    List.of(
                            "Escalate if drinking-water use is continuing without a clear certified result.",
                            "Escalate if uranium appears with radon, radium, or arsenic context that broadens the page beyond a single contaminant.",
                            "Escalate if the page is shopping from one result before exposure scope and verification are settled."
                    ),
                    faq(
                            item("Is uranium in well water a health issue or a nuisance issue?", "Treat it like a drinking-water health and exposure issue first, not a nuisance page."),
                            item("Should I buy treatment right after a uranium result?", "Not before the page confirms the result, checks related radionuclide context, and defines whether the scope is drinking-water-only or broader."),
                            item("Why does regional geology matter for uranium?", "Because uranium often belongs to a wider local pattern that should shape the next testing step before treatment claims are compared.")
                    )
            )),
            Map.entry("nitrite", winnerDoc(
                    "Nitrite is a faster exposure-control page than a routine nuisance or compare page.",
                    "This is a health-first result where infants, pregnancy, and current drinking-water use matter more than category shopping. The page should act with the same caution users bring to nitrate, but with even less patience for delay.",
                    List.of(
                            "Use safer drinking water when infants, pregnancy, or unresolved exposure is in scope.",
                            "Confirm the nitrite result with a certified lab and check whether nitrate, bacteria, or recent events widen the problem definition.",
                            "Choose treatment scope only after the page clarifies whether this is an exposure emergency, a mixed contamination pattern, or a sample-quality problem."
                    ),
                    List.of(
                            "People often treat nitrite like a lab curiosity instead of a current household exposure question.",
                            "A nitrite page gets weaker when it skips nitrate, microbial, or event context that may explain why the result changed.",
                            "Shopping for equipment before a certified confirmation can turn urgency into the wrong kind of action."
                    ),
                    "Retest quickly on a certified timeline and again after any corrective action or event review that changes how the result should be interpreted.",
                    splits(
                            split("Infants or pregnancy are in scope", "Move the page immediately toward safer drinking water and certified confirmation instead of routine compare browsing."),
                            split("Nitrite appears with nitrate or after a recent event", "Treat the page like a wider contamination-pattern problem rather than a single-number verdict."),
                            split("Sample quality or timing is weak", "Lower confidence in the result and return to stronger testing before the page becomes a treatment decision.")
                    ),
                    List.of(
                            "Escalate if infant feeding or pregnancy is part of the current household decision.",
                            "Escalate if nitrite appeared after flood, repair, or another event that weakens older assumptions.",
                            "Escalate if a treatment purchase is being considered before certified confirmation and scope are clear."
                    ),
                    faq(
                            item("Is nitrite in well water urgent?", "Yes. Treat it like a current exposure-control question until certified follow-up makes the risk clearer."),
                            item("Should I think about nitrate when nitrite is present?", "Yes. Nitrite often belongs inside a wider nitrate or contamination pattern that the page should not ignore."),
                            item("Can I start shopping for treatment right away?", "Not before the page confirms the result and defines whether the real question is exposure control, wider contamination, or sample quality.")
                    )
            )),
            Map.entry("copper", winnerDoc(
                    "Copper often behaves like a corrosion and plumbing page first, not a source-water treatment page first.",
                    "This page is strongest when it separates first-draw plumbing exposure, low-pH corrosion clues, and true source-water scope before treatment shopping starts.",
                    List.of(
                            "Check whether the concern is first-draw plumbing exposure, low pH, blue-green staining, or a broader well-water issue.",
                            "Use certified testing and compare first-draw versus flushed context before treating copper like a simple source-water contaminant page.",
                            "Keep infants and drinking-water use in scope when the page has not yet separated plumbing interaction from source-water treatment."
                    ),
                    List.of(
                            "People often treat copper like a source-water equipment problem when the first question is corrosion and plumbing interaction.",
                            "Taste alone is a weak basis for treatment shopping if pH and fixture context are still unresolved.",
                            "The page gets weaker when it jumps to a neutralizer or filter before it checks where the copper is entering the water."
                    ),
                    "Retest after corrosion correction, plumbing changes, or any intervention meant to change first-draw and flushed results.",
                    splits(
                            split("First-draw or plumbing-linked pattern is strongest", "Bias the page toward corrosion and plumbing interaction before you buy source-water equipment."),
                            split("Low pH, blue-green staining, or metallic taste travel with the result", "Keep corrosion follow-up and pH correction ahead of generic copper shopping."),
                            split("The pattern looks system-wide with no clear plumbing trigger", "Use certified testing to decide whether the page should widen beyond corrosion into broader source-water scope.")
                    ),
                    List.of(
                            "Escalate if infant formula or other sensitive drinking-water use is part of the household decision.",
                            "Escalate if corrosion clues are obvious but the page is still shopping before pH and plumbing context are checked.",
                            "Escalate if the user is treating one copper number like proof that a single treatment category will solve the whole problem."
                    ),
                    faq(
                            item("Does copper in well water always mean the well itself is contaminated?", "No. Copper often behaves like a corrosion and plumbing interaction problem first."),
                            item("Why does first-draw versus flushed context matter for copper?", "Because it helps the page separate plumbing exposure from a broader source-water conclusion."),
                            item("Should I buy treatment before checking pH and corrosion clues?", "No. The page is stronger when it settles corrosion context before it becomes a product decision.")
                    )
            )),
            Map.entry("softener-vs-iron-filter", winnerDoc(
                    "Softener versus iron filter is a dominance and scope decision first, not a product-label decision first.",
                    "This compare page works only when it separates hardness-driven comfort problems from iron-driven staining and maintenance problems before anyone shops by category name.",
                    List.of(
                            "Confirm whether hardness, iron, or a mixed nuisance pattern is actually driving the household pain.",
                            "Use current test results and visible symptom mapping before treating the page like a same-day equipment verdict.",
                            "Check whether manganese, pH, or sediment make the compare path more complicated than a one-category choice."
                    ),
                    List.of(
                            "People often compare softeners and iron filters before they know which problem is dominant.",
                            "A mixed hardness-plus-iron house can make a one-category decision look simpler than it really is.",
                            "The page gets weaker when it turns category familiarity into proof of fit."
                    ),
                    "Retest or re-evaluate after any treatment change, symptom shift, or result update that changes whether hardness or iron is really dominant.",
                    splits(
                            split("Scale, soap, and appliance burden dominate", "Bias the page toward hardness logic before it drifts into stain-first decisions."),
                            split("Orange staining, sediment, or iron result dominates", "Bias the page toward iron handling and maintenance fit before it assumes hardness is the core issue."),
                            split("The home shows both strong hardness and strong iron clues", "Treat the page like a mixed nuisance problem and slow down one-category shopping.")
                    ),
                    List.of(
                            "Escalate if the page is comparing categories before a recent hardness and iron result exists.",
                            "Escalate if manganese, pH, or broader nuisance overlap make the comparison more complex than the page admits.",
                            "Escalate if the user wants to buy from a product label before the dominant problem is actually settled."
                    ),
                    faq(
                            item("How do I know whether I need a softener or an iron filter?", "First decide whether hardness, iron, or a mixed nuisance pattern is actually driving the problem."),
                            item("Can one category solve every iron and hardness issue?", "Not automatically. The page should stay cautious when multiple nuisance patterns overlap."),
                            item("Why should I test before comparing softener and iron filter?", "Because the comparison only works when the page knows which burden is dominant and what else overlaps it.")
                    )
            )),
            Map.entry("whole-house-vs-under-sink-ro", winnerDoc(
                    "Whole-house versus under-sink RO is a scope and exposure-route decision, not a bigger-is-better decision.",
                    "This compare page is strongest when it separates drinking-water-only exposure control from whole-house nuisance or contact concerns before cost and installation size take over the conversation.",
                    List.of(
                            "Define whether the real problem is drinking-water exposure only or a broader whole-house issue.",
                            "Use test results and household use patterns before treating RO scope like a lifestyle preference instead of a contaminant-fit decision.",
                            "Check reject-water burden, maintenance, and where the actual risk appears before the page becomes a premium-install assumption."
                    ),
                    List.of(
                            "People often assume whole-house RO is automatically safer because it is bigger and more expensive.",
                            "An under-sink system does not solve a whole-house nuisance or contact problem just because it improves drinking water.",
                            "The page weakens when it compares installation scale before contaminant scope is clear."
                    ),
                    "Retest or reframe the scope after treatment install, changing household use, or any new result that shifts the exposure route question.",
                    splits(
                            split("The real concern is drinking and cooking water only", "Bias the page toward point-of-use logic before it drifts into whole-house system assumptions."),
                            split("The burden affects more than ingestion alone", "Keep whole-house use context in scope instead of treating RO size like a generic upsell."),
                            split("The user is shopping from install size and prestige", "Pull the page back to contaminant fit, exposure route, and maintenance reality.")
                    ),
                    List.of(
                            "Escalate if the page is using system size as a proxy for safety without defining exposure scope.",
                            "Escalate if the household problem affects more than drinking water but the page is still acting like a tap-only decision.",
                            "Escalate if cost and install complexity are leading the decision before contaminant fit is known."
                    ),
                    faq(
                            item("Is whole-house RO better than under-sink RO by default?", "No. The page should decide scope from exposure route and contaminant fit, not size alone."),
                            item("When does under-sink RO make more sense?", "When the real question is drinking-water exposure control rather than whole-house contact or nuisance issues."),
                            item("Why should I avoid comparing RO scope too early?", "Because the compare page gets stronger only after the underlying contaminant and use-case scope are clear.")
                    )
            )),
            Map.entry("carbon-vs-ro", winnerDoc(
                    "Carbon versus RO is a claim-fit and contaminant-fit page first, not a generic better-filter page.",
                    "This compare page works when it separates taste and odor improvement, PFAS and organic claims, and dissolved inorganic contaminant questions instead of treating carbon and RO like universal substitutes.",
                    List.of(
                            "Define the target contaminant before the page compares carbon and RO as if they solve the same problem.",
                            "Check certification language and analyte fit before you let a category label stand in for proof.",
                            "Keep maintenance, pretreatment, and exposure route in scope so the compare path stays tied to the actual household decision."
                    ),
                    List.of(
                            "People often assume carbon and RO are interchangeable because both sound like premium drinking-water options.",
                            "A carbon page gets weaker when it implies broad contaminant coverage without claim fit.",
                            "An RO page gets weaker when it wins by reputation before the household knows what the target actually is."
                    ),
                    "Retest after install, after a claim-based purchase, or whenever the tested analyte set changes what the category should have been chosen to address.",
                    splits(
                            split("The goal is taste, odor, or broad nuisance cleanup", "Keep the page cautious about assuming RO is needed when the problem may not require that level of treatment."),
                            split("The page is comparing PFAS, organics, or claim-sensitive contaminants", "Treat certification and analyte fit as the core decision instead of brand or category reputation."),
                            split("The concern is a dissolved inorganic contaminant", "Bias the page toward contaminant-specific fit before carbon claims are allowed to sound broad.")
                    ),
                    List.of(
                            "Escalate if the page is using carbon as a universal answer without checking the actual analyte.",
                            "Escalate if RO is being chosen by prestige rather than by contaminant fit and household scope.",
                            "Escalate if the compare path is ignoring certification detail, maintenance, or post-treatment verification."
                    ),
                    faq(
                            item("Is carbon better than RO for well water?", "Not by default. The page should decide from contaminant fit and certification, not category reputation."),
                            item("Can carbon and RO solve the same problem?", "Sometimes, but the page gets stronger only after it defines the target analyte and the claim that matters."),
                            item("Why should I verify claims before choosing carbon or RO?", "Because the category name alone is weaker evidence than a certification and analyte match.")
                    )
            )),
            Map.entry("uv-vs-chlorination", winnerDoc(
                    "UV versus chlorination is a microbial follow-up page only after the page confirms a real disinfection need.",
                    "This compare page is strongest when it separates confirmed microbial or corrective-action needs from routine nuisance pages and then compares operational fit, pretreatment fit, and verification burden.",
                    List.of(
                            "Confirm the microbial reason for treatment before you compare UV and chlorination like generic product categories.",
                            "Check pretreatment, water clarity, operator burden, and residual-disinfection needs before the page picks a side.",
                            "Plan post-treatment verification so the comparison ends in proof rather than installation alone."
                    ),
                    List.of(
                            "People often compare UV and chlorination before they know whether the page is about a confirmed microbial problem or just fear after one event.",
                            "UV is not a free pass when pretreatment or maintenance assumptions are weak.",
                            "Chlorination is not the same thing as a one-time shock response or a permanent fit for every household."
                    ),
                    "Retest after installation, corrective action, or any change in microbial context that would change how ongoing disinfection should be judged.",
                    splits(
                            split("The page is about a confirmed ongoing microbial risk", "Compare operation, residual behavior, pretreatment, and proof instead of jumping from fear to a category label."),
                            split("The page is about a one-time corrective response", "Keep corrective-action timing separate from permanent disinfection-system shopping."),
                            split("Pretreatment or maintenance discipline is weak", "Do not let the page recommend a system whose success depends on assumptions the household has not met.")
                    ),
                    List.of(
                            "Escalate if the page is shopping disinfection categories before certified follow-up and source-path review.",
                            "Escalate if maintenance, pretreatment, or post-treatment verification are being ignored.",
                            "Escalate if a one-time microbial event is being treated like automatic proof of a permanent category choice."
                    ),
                    faq(
                            item("Is UV better than chlorination for a well?", "Not automatically. The page should decide from microbial context, pretreatment fit, and ongoing operation needs."),
                            item("Can I compare UV and chlorination before confirming the microbial issue?", "No. The compare page gets stronger only after the actual reason for disinfection is confirmed."),
                            item("Why does post-treatment verification matter here?", "Because installation alone is weaker evidence than proof that the disinfection approach actually solved the problem.")
                    )
            )),
            Map.entry("point-of-entry-vs-point-of-use", winnerDoc(
                    "Point-of-entry versus point-of-use is an exposure-route decision before it is a plumbing-layout decision.",
                    "This compare page is strongest when it separates drinking-water-only needs from whole-house contact, nuisance, or mixed-use problems before system placement becomes the headline.",
                    List.of(
                            "Define whether the real issue is ingestion only, broader household contact, or a nuisance problem that affects more than one use point.",
                            "Check contaminant fit and certification before the page treats system placement like proof of adequacy.",
                            "Use fixture distribution and maintenance burden as secondary factors after the page settles exposure scope."
                    ),
                    List.of(
                            "People often assume point-of-entry is automatically safer because it treats more water.",
                            "A point-of-use system does not solve shower, laundry, or whole-house nuisance burden just because it improves drinking water.",
                            "The page weakens when it chooses placement before contaminant fit and exposure route are defined."
                    ),
                    "Re-evaluate placement after new testing, new household use patterns, or post-install evidence that the original scope guess was wrong.",
                    splits(
                            split("The problem is mainly drinking and cooking water", "Bias the page toward point-of-use logic before it drifts into whole-house assumptions."),
                            split("The problem affects contact, appliances, or whole-house nuisance use", "Keep point-of-entry in scope because the page is broader than ingestion alone."),
                            split("The user is shopping placement by intuition instead of contaminant fit", "Pull the page back to exposure route and certification before a placement verdict is allowed.")
                    ),
                    List.of(
                            "Escalate if the page is choosing placement before the contaminant and use route are clearly mapped.",
                            "Escalate if a drinking-water-only system is being treated like a whole-house solution.",
                            "Escalate if a whole-house system is being sold as automatically better without defining why broader placement is actually needed."
                    ),
                    faq(
                            item("Is point-of-entry always better than point-of-use?", "No. The page should decide from exposure route and contaminant fit, not coverage alone."),
                            item("When does point-of-use make more sense?", "When the real need is drinking and cooking water rather than broader whole-house contact or nuisance control."),
                            item("Why should system placement wait until after testing?", "Because placement only makes sense once the page knows what the target is and where the exposure actually happens.")
                    )
            )),
            Map.entry("california-private-well-owner-guide", winnerDoc(
                    "California private well guidance should act like a risk-reset page when drought, wildfire, land use, or basin conditions change what the next test should be.",
                    "This is not one generic state page. It is strongest when it uses California context to separate wildfire, drought, nitrate, and land-use stress before a household jumps into treatment shopping.",
                    List.of(
                            "Use the California guide to narrow which local stressor is actually shaping the next testing step.",
                            "Check whether drought, wildfire, agricultural land use, or a recent water-quality change is the real reason the page exists.",
                            "Route into the right tool or compare page only after California context changes the order of next actions."
                    ),
                    List.of(
                            "People often treat California like one private-well environment when the real risks vary by local context.",
                            "A drought or wildfire page gets weaker if it jumps straight to equipment before testing scope is rebuilt.",
                            "This page loses value if it uses the state name without showing why California context changes the answer."
                    ),
                    "Retest after wildfire, drought stress, major water-quality change, or any local condition that makes older California assumptions weaker.",
                    splits(
                            split("Wildfire or smoke context is active", "Bias the page toward post-fire testing and safer-water logic before product comparison."),
                            split("Drought, low water levels, or changing source behavior is active", "Treat the page like a source-stress and testing-priority problem before it becomes a treatment page."),
                            split("Agricultural or land-use concern is shaping the property", "Keep nitrate and broader local testing in scope instead of using one generic California answer.")
                    ),
                    List.of(
                            "Escalate if local context changed but the page is still behaving like a static statewide checklist.",
                            "Escalate if wildfire or drought stress is pushing the household toward shopping before testing scope is rebuilt.",
                            "Escalate if the page is naming California without showing which local driver actually changes the next action."
                    ),
                    faq(
                            item("Why does California context matter for a private well page?", "Because drought, wildfire, land use, and local basin conditions can change what belongs in the next test plan."),
                            item("Should I shop for treatment right after drought or wildfire changes my well?", "No. California context should first reset what you test and what evidence still counts."),
                            item("Is one California well guide enough for every household?", "No. The page is strongest when it narrows which local stressor is actually shaping the decision.")
                    )
            )),
            Map.entry("texas-private-well-sampling-testing", winnerDoc(
                    "Texas private well sampling is a sample-quality and lab-path page first, not a treatment page first.",
                    "This regional page matters because heat, timing, handling, and local certified-lab use can change whether the result is decision-grade evidence or just a weak screen.",
                    List.of(
                            "Use Texas sampling guidance to decide how the sample should be taken, handled, and routed before trusting the result.",
                            "Choose certified labs and stronger sample discipline when the page is shaping health, sale, or treatment-verification decisions.",
                            "Keep chain-of-custody, timing, and climate-related handling risk in scope before the page opens category shopping."
                    ),
                    List.of(
                            "People often compare treatment before they have a sample they can actually trust.",
                            "A narrow result set from weak handling can create confident-looking but fragile decisions.",
                            "The page gets weaker when it treats sampling like paperwork instead of part of the evidence hierarchy."
                    ),
                    "Retest whenever timing, handling, or lab-path choices make the original result hard to trust or compare.",
                    splits(
                            split("Heat, travel time, or handling risk are high", "Bias the page toward stronger sample discipline and lab-path choices before the result becomes a household decision."),
                            split("The page supports a health, sale, or verification decision", "Use certified-lab discipline because the evidence threshold is higher than a quick screen."),
                            split("The user wants to compare treatment before fixing the sample path", "Pull the page back to method quality before product comparison.")
                    ),
                    List.of(
                            "Escalate if the result came from weak sampling discipline but is being treated like final truth.",
                            "Escalate if health, sale, or treatment-verification stakes are present and the page is still using a weak lab path.",
                            "Escalate if the page starts shopping before it has a sample and lab process the household can defend."
                    ),
                    faq(
                            item("Why does Texas sampling guidance matter before treatment shopping?", "Because a weak sample can turn a confident-looking page into a weak decision."),
                            item("When should I favor certified lab discipline in Texas?", "When the result is shaping health, sale, or treatment-verification decisions."),
                            item("What is the biggest mistake on this page?", "Treating sample handling like a minor detail instead of part of the evidence itself.")
                    )
            )),
            Map.entry("north-carolina-private-well-water-faqs", winnerDoc(
                    "North Carolina FAQ pages should separate routine well maintenance from flooding, repairs, and storm-driven retesting before they behave like shopping pages.",
                    "This regional page is strongest when it uses North Carolina FAQ and program guidance to decide whether the household is handling baseline testing, post-storm recovery, or concern-specific follow-up before any treatment comparison starts.",
                    List.of(
                            "Use the North Carolina FAQ path first when the household still needs to separate routine testing from flooding, repairs, or storm follow-up.",
                            "Treat event context and sample age like part of the evidence, not like background detail.",
                            "Use the certified lab list before you trust one old screen, one symptom, or one product category as the answer."
                    ),
                    List.of(
                            "People often treat a North Carolina FAQ page like a generic annual-testing article with a state name swapped in.",
                            "A storm or repair question should not be handled like routine baseline upkeep.",
                            "The page gets weaker when product shopping starts before the FAQ path and state-certified lab route are clear."
                    ),
                    "Retest after flooding, repairs, replacement work, or any event that makes the last result weaker than it looks.",
                    splits(
                            split("Routine testing question only", "Keep the page on North Carolina baseline guidance before it widens into compare logic."),
                            split("Flooding, storm, or repair context is active", "Treat the page like an evidence-reset decision before any product category enters the frame."),
                            split("One symptom or one old screen is driving urgency", "Use the certified lab path first so the page does not turn a weak clue into a strong verdict.")
                    ),
                    List.of(
                            "Escalate if flooding, repairs, or storm exposure changed the well after the last test.",
                            "Escalate if infants, pregnancy, or direct drinking-water use are in play but the page still leans on old evidence.",
                            "Escalate if shopping starts before the FAQ path and lab route are settled."
                    ),
                    faq(
                            item("Why does North Carolina need a separate private well FAQ page?", "Because the official FAQ separates routine testing from flood, repair, and storm-driven retesting instead of treating them like the same decision."),
                            item("What should I do before comparing treatment in North Carolina?", "Use the state FAQ and certified-lab route first."),
                            item("What makes this page stronger than a generic testing article?", "It changes the next step when flooding, repairs, or older evidence reset what your last test still proves.")
                    )
            )),
            Map.entry("virginia-private-well-testing-program", winnerDoc(
                    "Virginia private well program pages should act like baseline-and-lab-path pages first, especially when yearly checks, a new well, or a homebuyer question changes the evidence threshold.",
                    "This regional page is strongest when it uses Virginia private well program guidance to separate annual baseline testing from higher-stakes follow-up tied to a new well, a home purchase, or a stale panel before shopping starts.",
                    List.of(
                            "Use Virginia guidance first when the household still needs to separate yearly bacteria and nitrate checks from concern-specific follow-up.",
                            "Treat a new well, homebuyer, or stale-panel question as a stronger evidence problem than routine upkeep.",
                            "Use the Virginia certified-lab route before trusting a convenience-first test path or narrowing into treatment categories."
                    ),
                    List.of(
                            "People often treat an annual Virginia check like a full household plan when it is only a baseline.",
                            "A new well or homebuyer question should widen the page before it narrows into product comparison.",
                            "The page gets weaker when the certified-lab path is treated like optional paperwork."
                    ),
                    "Retest on the yearly baseline that fits Virginia guidance, and move faster when a new well, homebuyer question, or stale panel changes the evidence threshold.",
                    splits(
                            split("Yearly upkeep is the real question", "Keep the page on Virginia baseline guidance before it widens into compare logic."),
                            split("New well or homebuyer stakes are active", "Treat the page like a stronger panel and lab-path decision before any category shopping starts."),
                            split("One convenience test is being used like final proof", "Pull the page back to the Virginia certified-lab route before the household trusts the answer.")
                    ),
                    List.of(
                            "Escalate if a new well or homebuyer decision is being handled with routine baseline logic only.",
                            "Escalate if the last panel is stale but still being used like current truth.",
                            "Escalate if shopping starts before the certified-lab route is settled."
                    ),
                    faq(
                            item("Why does Virginia need its own private well testing program page?", "Because annual baseline checks, new-well questions, and homebuyer stakes do not belong in the same decision bucket."),
                            item("What should I do before comparing treatment in Virginia?", "Use Virginia guidance and the certified-lab path first."),
                            item("What makes this page more helpful than a national guide?", "It changes the order when a yearly baseline question turns into a new-well, homebuyer, or stale-evidence decision.")
                    )
            )),
            Map.entry("indiana-well-water-quality-testing", stateProgramDoc(
                    "Indiana",
                    "Indiana well water quality and testing guidance",
                    "baseline testing, concern-specific follow up, and when certified labs should replace convenience",
                    "baseline schedule changes, new symptoms, or thin lab evidence"
            )),
            Map.entry("georgia-private-well-water-guidance", stateProgramDoc(
                    "Georgia",
                    "Georgia well water guidance",
                    "routine well questions, contamination concerns, and the point where state information should outrun shopping",
                    "new well concerns, stale results, or follow-up uncertainty"
            )),
            Map.entry("south-carolina-well-water-quality-testing", stateProgramDoc(
                    "South Carolina",
                    "South Carolina residential well guidance and testing services",
                    "testing services, residential-well guidance, and certified-lab routing",
                    "service-triggered retesting, new symptoms, or uncertainty about the lab path"
            )),
            Map.entry("oregon-private-well-testing-recommendations", winnerDoc(
                    "Oregon testing-recommendation pages should separate routine owner testing from real-estate transaction panels and broader local well-safety follow-up before they behave like shopping pages.",
                    "This regional page is strongest when it uses Oregon testing recommendations and transaction rules to decide whether the household is doing owner upkeep, seller-panel compliance, or a wider domestic-well safety workup before compare logic starts.",
                    List.of(
                            "Use Oregon testing recommendations first when the household still needs to separate baseline owner testing from a real-estate transaction panel.",
                            "Keep arsenic, nitrate, coliform, and local well-safety questions in scope before the page jumps into treatment shopping.",
                            "Use the Oregon certified-lab route before you trust one sale panel, one screen, or one symptom like the whole answer."
                    ),
                    List.of(
                            "People often treat a sale or transaction panel like a full long-term household plan.",
                            "A routine owner-testing question should not be collapsed into the same decision as transaction compliance.",
                            "The page gets weaker when the Oregon process disappears and only generic product language remains."
                    ),
                    "Retest when a transaction panel is old, when the property context changed, or when local well-safety questions make the first panel too narrow.",
                    splits(
                            split("Real-estate transaction is active", "Keep the page on Oregon transaction rules first, then separate what still belongs in the long-term household plan."),
                            split("Routine owner testing is the real question", "Use Oregon recommendations to decide panel fit before any compare page opens."),
                            split("One sale panel or one symptom is being treated like the whole answer", "Pull the page back to panel choice, local well-safety context, and certified-lab follow-up.")
                    ),
                    List.of(
                            "Escalate if a sale panel is being treated like a lifetime all-clear.",
                            "Escalate if arsenic, nitrate, coliform, or local well-safety questions are still open.",
                            "Escalate if shopping starts before the Oregon panel choice is defended."
                    ),
                    faq(
                            item("Why does Oregon need a separate private well testing recommendations page?", "Because owner testing, real-estate transaction panels, and local well-safety follow-up do not belong in the same decision bucket."),
                            item("What should I do before comparing treatment in Oregon?", "Use Oregon recommendations, transaction rules if relevant, and the certified-lab path first."),
                            item("What makes this page different from a national guide?", "It changes the answer when a sale panel or a local well-safety question changes what belongs in the next panel.")
                    )
            )),
            Map.entry("washington-private-well-water-testing", winnerDoc(
                    "Washington testing-your-water pages should act like targeted-panel pages first, because annual bacteria and nitrate checks do not answer the same question as arsenic or other targeted follow-up.",
                    "This regional page is strongest when it uses Washington testing guidance to separate annual routine testing from targeted arsenic and contaminant follow-up before shopping starts.",
                    List.of(
                            "Use Washington guidance first when the household still needs to separate annual coliform and nitrate checks from targeted arsenic or other contaminant follow-up.",
                            "Treat annual routine testing as baseline, not as proof that every drinking-water question is closed.",
                            "Use the Washington lab route before trusting one old panel, one symptom, or one product page like the answer."
                    ),
                    List.of(
                            "People often treat an annual routine screen like a full long-term well-safety verdict.",
                            "Arsenic and other targeted follow-up questions should not be flattened into the same cadence as baseline upkeep.",
                            "The page gets weaker when category shopping starts before the state testing path is clear."
                    ),
                    "Retest on the annual routine cadence Washington uses for baseline checks, and move faster whenever targeted contaminant follow-up or stale evidence changes the testing question.",
                    splits(
                            split("Annual routine testing is the real question", "Keep the page on Washington baseline guidance before compare logic opens."),
                            split("Arsenic or another targeted contaminant is in scope", "Treat the page like a wider panel and certified-lab decision before any category shopping starts."),
                            split("One old routine panel is being treated like a complete answer", "Pull the page back to the Washington testing path before the household trusts the verdict.")
                    ),
                    List.of(
                            "Escalate if annual routine testing is being mistaken for a full contaminant workup.",
                            "Escalate if arsenic or another targeted concern is active but the page is still acting like a baseline checklist.",
                            "Escalate if shopping starts before the Washington lab route is settled."
                    ),
                    faq(
                            item("Why does Washington need its own private well testing page?", "Because annual baseline testing and targeted contaminant follow-up do not belong in the same decision bucket."),
                            item("What should I do before comparing treatment in Washington?", "Use Washington testing guidance, any targeted arsenic follow-up, and the lab route first."),
                            item("What makes this page more helpful than a generic testing article?", "It changes the next step when an annual routine screen is not enough to answer a targeted contaminant question.")
                    )
            )),
            Map.entry("acid-neutralizer-vs-soda-ash", winnerDoc(
                    "Acid neutralizer versus soda ash is a corrosion-fit and maintenance decision before it is an equipment-style decision.",
                    "This compare page is strongest when low pH, copper, lead, and plumbing interaction are already in view. It gets weaker when media preference or feed-pump preference outruns the actual corrosion evidence.",
                    List.of(
                            "Confirm low pH with current testing and check whether first-draw copper, blue-green staining, or metallic taste widen the page beyond acidity alone.",
                            "Map whether the real need is whole-house pH correction, plumbing follow-up, or drinking-water-only exposure control before comparing equipment styles.",
                            "Compare media replenishment, feed-pump discipline, and post-correction retesting only after the page proves low pH is the main driver."
                    ),
                    List.of(
                            "People often treat neutralizers and soda ash like interchangeable fixes when the real question is corrosion evidence and maintenance fit.",
                            "A tank-based compare page gets weaker if copper or lead follow-up is still unresolved.",
                            "The page loses value when an equipment preference is treated like proof that the household already knows its scope."
                    ),
                    "Retest after any correction install, media or feed adjustment, or plumbing change that could alter pH and corrosion behavior.",
                    splits(
                            split("Low pH is clear and corrosion clues are limited", "Keep the page on maintenance, media behavior, and operating fit instead of reopening every contaminant path."),
                            split("Copper, lead, or first-draw evidence is active", "Bias the page toward corrosion follow-up before it becomes a correction-equipment contest."),
                            split("Maintenance discipline is weak or the system is rarely checked", "Treat the page cautiously because either path can fail when upkeep is assumed but not realistic.")
                    ),
                    List.of(
                            "Escalate if the page is comparing correction methods before low pH has been confirmed on current testing.",
                            "Escalate if copper, lead, or fixture-specific corrosion clues are present but the page is still shopping from equipment labels.",
                            "Escalate if the household is underestimating the maintenance and retest burden that makes either correction path trustworthy."
                    ),
                    faq(
                            item("How do I decide between an acid neutralizer and soda ash?", "First confirm that low pH is the main problem and that corrosion follow-up is not still unresolved."),
                            item("Can I compare these two options before checking copper or lead clues?", "No. The page is stronger when corrosion evidence is settled before it becomes an equipment comparison."),
                            item("Why does maintenance fit matter so much here?", "Because either correction path gets weaker when the household cannot support the refill, feed, or retest discipline it assumes.")
                    )
            )),
            Map.entry("after-boil-water-advisory", winnerDoc(
                    "After a boil water advisory, the next step is evidence recovery first, not automatic return to untreated use.",
                    "This trigger page is strongest when it separates official advisory status, private-well impact, and post-event testing needs before a household assumes normal use can resume.",
                    List.of(
                            "Follow the official advisory instructions first and confirm whether the advisory actually involved your water source or a connected system you use.",
                            "Check whether your private well had its own flood, outage, repair, or contamination path that still requires testing even after the advisory ends.",
                            "Return to untreated use only after the page has sorted flushing, testing, or corrective action from simple advisory language."
                    ),
                    List.of(
                            "People often treat the end of an advisory like automatic proof that every private-well question is closed.",
                            "Boiling helps with microbial risk, but it does not settle chemical contamination or well-specific damage.",
                            "The page gets weaker when it starts comparing treatment before it rebuilds what still needs verification."
                    ),
                    "Retest after any advisory-linked event that affected your private well directly, or whenever water quality changed during the advisory period.",
                    splits(
                            split("The advisory was system-wide but your private well was not directly affected", "Bias the page toward confirming local instructions and returning carefully rather than inventing extra treatment decisions."),
                            split("The advisory overlapped with flooding, repairs, or visible water-quality change", "Treat the page like a private-well testing and corrective-action problem before normal use is assumed."),
                            split("The user wants to restart untreated use immediately", "Pull the page back to the evidence still missing before convenience becomes the decision.")
                    ),
                    List.of(
                            "Escalate if the well itself may have been contaminated or mechanically affected during the event.",
                            "Escalate if taste, odor, color, or sediment changed during the advisory window.",
                            "Escalate if the page is treating boil guidance like proof that no testing or follow-up is needed."
                    ),
                    faq(
                            item("Can I go back to normal water use as soon as the boil advisory ends?", "Not automatically. The page should still check whether your private well had its own event-related risk or testing gap."),
                            item("Does boiling settle every post-advisory well-water question?", "No. Boiling helps with germs, but it does not answer chemical issues or well-specific damage."),
                            item("When should I test after a boil water advisory?", "Test when the advisory overlapped with flooding, repairs, source disruption, or any water-quality change that makes the old evidence weaker.")
                    )
            )),
            Map.entry("air-injection-vs-oxidizing-filter", winnerDoc(
                    "Air injection versus oxidizing filter is a nuisance-pattern and maintenance-fit decision before it is a product-label decision.",
                    "This compare page works only when odor, staining, sediment, and overlap with iron or manganese have already been mapped. It weakens when sulfur treatment is chosen from category familiarity alone.",
                    List.of(
                            "Confirm whether the household is dealing with whole-house sulfur odor, hot-water-only odor, or a mixed nuisance pattern that includes staining or sediment.",
                            "Use current testing and symptom mapping before treating the compare page like a settled treatment verdict.",
                            "Compare maintenance burden, media handling, and pretreatment fit only after the page decides what the nuisance pattern actually is."
                    ),
                    List.of(
                            "People often compare these categories before they know whether sulfur is the only problem in scope.",
                            "A heater-specific smell can make a whole-house compare page look stronger than it really is.",
                            "The page loses value when one treatment category is treated like a universal answer for every odor pattern."
                    ),
                    "Re-evaluate after disinfection, heater work, or any shift in odor timing, staining, or sediment that changes which nuisance pattern is dominant.",
                    splits(
                            split("Whole-house odor with broader nuisance clues is in scope", "Keep the page on mixed nuisance treatment fit instead of narrowing to sulfur alone."),
                            split("Hot-water-only odor is still plausible", "Pull the page back toward heater behavior before whole-house equipment shopping takes over."),
                            split("Maintenance tolerance is low", "Treat the page cautiously because the best-sounding category can still fail if upkeep assumptions are unrealistic.")
                    ),
                    List.of(
                            "Escalate if staining, sediment, iron, or manganese clues make the compare path broader than sulfur smell alone.",
                            "Escalate if the page is choosing a category before untreated-water testing and odor mapping are current.",
                            "Escalate if the household is shopping from marketing language while ignoring maintenance burden and retest discipline."
                    ),
                    faq(
                            item("How do I choose between air injection and an oxidizing filter?", "First decide whether sulfur odor is actually the dominant whole-house problem and whether other nuisance clues are overlapping."),
                            item("What if the smell is only in hot water?", "Then the page should check heater behavior before it turns into a whole-house compare decision."),
                            item("Why does maintenance fit matter on this page?", "Because either category gets weaker when the household cannot support the upkeep that keeps odor control working.")
                    )
            )),
            Map.entry("arsenic-bedrock-testing-checklist", winnerDoc(
                    "An arsenic bedrock checklist is a testing-scope page first and a treatment page second.",
                    "This authority page is strongest when it uses bedrock context to decide whether arsenic alone is enough or whether uranium, radon, or broader geology-aware follow-up belongs in the same plan.",
                    List.of(
                            "Use bedrock context to decide whether the next lab panel should stay arsenic-only or widen to other radionuclide or geology-linked risks.",
                            "Choose certified labs and a decision-grade sample path before this page opens any treatment comparison.",
                            "Treat safer drinking-water steps and household exposure scope as separate from product shopping when the arsenic result is still unresolved."
                    ),
                    List.of(
                            "People often treat bedrock arsenic like one isolated number when the real issue is a wider geology-shaped testing plan.",
                            "A checklist page gets weaker when it jumps to treatment before it decides what else belongs in the panel.",
                            "The page loses value when the word bedrock is used as branding instead of a reason to change the evidence threshold."
                    ),
                    "Retest after a new lab result, after treatment, or whenever bedrock context suggests the first panel was too narrow to defend.",
                    splits(
                            split("Arsenic is the only confirmed signal so far", "Keep the page on certified confirmation and scope discipline before treatment categories appear."),
                            split("Regional geology suggests uranium, radon, or a wider panel", "Widen the checklist so the page does not under-test a bedrock-driven pattern."),
                            split("The user wants treatment before the bedrock panel is clear", "Pull the page back to testing scope and household exposure first.")
                    ),
                    List.of(
                            "Escalate if a bedrock-area well is being treated from one arsenic result without considering the wider panel.",
                            "Escalate if the page is shopping before certified testing and safer drinking-water decisions are settled.",
                            "Escalate if the checklist is too narrow to defend for a health-stakes household decision."
                    ),
                    faq(
                            item("Why does bedrock context change the arsenic checklist?", "Because bedrock risk can justify a wider panel than a simple one-contaminant page would suggest."),
                            item("Should I buy treatment before finishing the bedrock testing plan?", "No. The page is strongest when it locks the panel and exposure scope before treatment comparison starts."),
                            item("What makes this checklist stronger than a generic arsenic page?", "It changes the next test decision based on geology instead of treating every arsenic case like the same workflow.")
                    )
            )),
            Map.entry("home-sale-private-well-testing-checklist", winnerDoc(
                    "A home-sale well checklist is an evidence and timing page first, not a closing-pressure page first.",
                    "This page is strongest when it protects the sample path, certified-lab quality, and scope discipline from the rush of a real-estate timeline.",
                    List.of(
                            "Start with the sale-triggered testing scope required or recommended locally before comparing any treatment response.",
                            "Use certified labs, defensible sampling, and documented timing because the page may need to support negotiation or disclosure decisions.",
                            "Separate immediate sale evidence from longer-term treatment planning so a rushed closing does not distort the testing logic."
                    ),
                    List.of(
                            "People often let contract timing weaken the sample path and then trust the result like final evidence.",
                            "A sale checklist gets weaker when it jumps from one narrow panel to treatment recommendations.",
                            "The page loses value when it treats buyer pressure as a reason to skip certified testing or follow-up."
                    ),
                    "Retest whenever the transaction timeline, corrective action, or sample quality problem makes the original result weaker as sale evidence.",
                    splits(
                            split("The page is supporting negotiation or disclosure", "Raise the evidence bar and keep certified-lab discipline central."),
                            split("The first sample path was weak or rushed", "Treat the page like an evidence-repair problem before it becomes a treatment decision."),
                            split("A treatment fix is being considered during the sale", "Separate what is needed for the transaction from what is needed for a durable household plan.")
                    ),
                    List.of(
                            "Escalate if the result may affect disclosure, pricing, or buyer confidence and the sample path is weak.",
                            "Escalate if a narrow panel is being used like proof that the whole-house issue is understood.",
                            "Escalate if treatment is being purchased to rescue a sale before the evidence is decision-grade."
                    ),
                    faq(
                            item("Why is certified-lab discipline so important in a home sale?", "Because sale decisions often need evidence that can survive negotiation, disclosure, and later scrutiny."),
                            item("Should I buy treatment during the sale process right away?", "Not before the page separates transaction evidence from the longer-term treatment decision."),
                            item("What is the biggest mistake on this checklist?", "Letting closing pressure turn a weak or narrow sample into stronger evidence than it really is.")
                    )
            )),
            Map.entry("low-ph-copper-corrosion-testing-order", winnerDoc(
                    "Low-pH copper corrosion is a testing-sequence page before it is a treatment page.",
                    "This authority page is strongest when it teaches the order of pH, copper, lead, first-draw, and flushed evidence so the household does not buy correction equipment from incomplete corrosion logic.",
                    List.of(
                            "Confirm low pH and collect the plumbing-context evidence that separates first-draw exposure from broader source-water conclusions.",
                            "Check copper, lead, blue-green staining, metallic taste, and fixture pattern before the page opens a treatment comparison.",
                            "Choose correction paths only after the sequence of corrosion evidence is strong enough to defend."
                    ),
                    List.of(
                            "People often test one corrosion clue and then shop like the whole sequence is already known.",
                            "A low-pH page gets weaker when it skips first-draw versus flushed context.",
                            "The page loses value when a neutralizer becomes the answer before metal exposure and plumbing interaction are mapped."
                    ),
                    "Retest after any corrosion correction, plumbing work, or sampling change that should alter the first-draw and flushed pattern.",
                    splits(
                            split("Low pH is confirmed but metal exposure is still unclear", "Keep the page on testing order and plumbing evidence before it turns into a correction purchase."),
                            split("Copper or lead evidence is already active", "Bias the page toward exposure and corrosion-sequence discipline instead of generic low-pH treatment language."),
                            split("The page is being used to justify equipment immediately", "Pull it back to the missing testing steps before the sequence becomes a shopping shortcut.")
                    ),
                    List.of(
                            "Escalate if the page is using pH alone to settle a corrosion and metals problem.",
                            "Escalate if first-draw versus flushed context is missing but the household is ready to buy correction equipment.",
                            "Escalate if lead, copper, or visible corrosion clues are being treated like secondary details."
                    ),
                    faq(
                            item("What should I test first when low pH and copper corrosion are both possible?", "Start with current pH and the plumbing-context evidence that separates first-draw exposure from broader source-water conclusions."),
                            item("Why does first-draw versus flushed testing matter here?", "Because it helps the page decide whether the main problem is plumbing interaction, broader source-water scope, or both."),
                            item("Should I buy a neutralizer before the corrosion sequence is clear?", "No. The page is stronger when the testing order is settled before it becomes a treatment decision.")
                    )
            )),
            Map.entry("nitrate-baby-pregnancy-well-water-checklist", winnerDoc(
                    "A nitrate checklist for babies and pregnancy is an exposure-control page first and a treatment page second.",
                    "This authority page is strongest when it treats infant feeding, pregnancy, and drinking-water use as immediate risk-routing decisions rather than routine panel interpretation.",
                    List.of(
                            "Use safer drinking water now when baby, formula, or pregnancy exposure is still unresolved.",
                            "Confirm the nitrate result with certified testing and check whether repeated screening or seasonal context should change the plan.",
                            "Keep treatment comparison secondary until the page has settled immediate exposure control and test certainty."
                    ),
                    List.of(
                            "People often read nitrate like one more lab line when the household context makes it a same-day decision.",
                            "A checklist page gets weaker when it delays safer-water guidance while it compares treatment categories.",
                            "The page loses value when it ignores how pregnancy or infant feeding changes the urgency threshold."
                    ),
                    "Retest on the certified schedule and again after seasonal change, treatment change, or any event that weakens confidence in the last nitrate result.",
                    splits(
                            split("Infant feeding or pregnancy is current", "Move the page toward safer drinking water and rapid certified confirmation before shopping begins."),
                            split("The household is planning ahead but no vulnerable exposure is current", "Keep the page on prevention, repeat testing, and scope discipline instead of fear-driven buying."),
                            split("Local or seasonal risk may change the result over time", "Treat repeat testing as part of the checklist rather than a footnote.")
                    ),
                    List.of(
                            "Escalate if infant feeding or pregnancy is in scope and the page is still acting like a routine panel explainer.",
                            "Escalate if treatment shopping appears before certified confirmation and exposure control are settled.",
                            "Escalate if seasonal or local nitrate risk makes a one-time result weaker than the page admits."
                    ),
                    faq(
                            item("Why is this nitrate page different when a baby or pregnancy is involved?", "Because vulnerable-household exposure changes the urgency from routine interpretation to immediate risk control."),
                            item("Should I start treatment shopping right away?", "Not before the page settles safer drinking water, certified confirmation, and whether repeat testing belongs in the plan."),
                            item("When does repeat nitrate testing matter most?", "When seasonal, local, or household changes make one result too weak to carry the whole decision.")
                    )
            )),
            Map.entry("pfas-private-well-filter-claim-checklist", winnerDoc(
                    "A PFAS filter claim checklist is a certification-fit page first, not a category-marketing page first.",
                    "This authority page is strongest when it teaches users to match the PFAS question, certification claim, treatment scope, and maintenance burden before trusting a product page or installer pitch.",
                    List.of(
                            "Start by confirming which PFAS concern is actually in scope and whether the household needs drinking-water-only protection or a broader response.",
                            "Check certification language, replacement burden, and product scope before the page treats a PFAS label like proof.",
                            "Keep post-install testing and state guidance in view so the checklist ends in verification instead of marketing confidence."
                    ),
                    List.of(
                            "People often treat any PFAS label like proof that the system matches their exposure question.",
                            "A PFAS checklist gets weaker when it ignores whether the claim is point-of-use, point-of-entry, or contaminant-specific.",
                            "The page loses value when installation is treated like the final proof instead of a step that still needs verification."
                    ),
                    "Re-check the claim whenever the target PFAS question changes, the system scope changes, or no post-install evidence exists yet.",
                    splits(
                            split("The claim is specific and certification-backed", "Treat the page as stronger, but still keep scope, maintenance, and retest discipline in view."),
                            split("The claim is broad or marketing-led", "Turn the page into a claim-risk review before it becomes a buying decision."),
                            split("The system is already installed but unverified", "Pull the page back to proof because PFAS branding is weaker than post-install evidence.")
                    ),
                    List.of(
                            "Escalate if the page is using PFAS marketing language as stronger evidence than the actual certification claim.",
                            "Escalate if point-of-use and point-of-entry scope are being blurred into one answer.",
                            "Escalate if the household is trusting the install without a retest or state-guidance check."
                    ),
                    faq(
                            item("How do I verify a PFAS filter claim?", "Check the actual certification, the treatment scope, the maintenance burden, and the retest plan instead of trusting the PFAS label alone."),
                            item("Is any PFAS filter label enough?", "No. The page is stronger only when the claim matches the real exposure question and system scope."),
                            item("Why does post-install testing matter on PFAS pages?", "Because the strongest claim is still weaker than proof that the installed system reduced the PFAS risk you actually have.")
                    )
            )),
            Map.entry("ro-vs-adsorptive-media-for-arsenic", winnerDoc(
                    "RO versus adsorptive media for arsenic is a chemistry-fit and scope decision before it is a category-preference decision.",
                    "This compare page is strongest when it separates drinking-water-only scope, arsenic chemistry, waste and maintenance burden, and verification fit before anyone shops by treatment reputation.",
                    List.of(
                            "Confirm the arsenic result and check whether water chemistry, treatment scope, or broader radionuclide context change which category is even plausible.",
                            "Decide whether the real need is point-of-use drinking-water protection or a broader system response before comparing technologies.",
                            "Compare maintenance, waste, media replacement, and post-treatment verification only after the page knows what kind of arsenic problem it is solving."
                    ),
                    List.of(
                            "People often compare RO and adsorptive media like a generic premium-versus-premium choice when the real driver is chemistry and scope.",
                            "A page gets weaker when it assumes whole-house treatment is needed for a drinking-water-only arsenic problem.",
                            "The compare logic breaks when category reputation is stronger than current testing and verification plans."
                    ),
                    "Retest after installation, after any pretreatment change, or whenever arsenic chemistry and result confidence make the original comparison weaker.",
                    splits(
                            split("The need is mainly drinking and cooking water", "Keep point-of-use logic central before whole-house assumptions start driving the compare page."),
                            split("Arsenic chemistry or water-quality interactions are still unclear", "Pull the page back to testing and treatment-fit evidence before category shopping."),
                            split("The user is choosing from reputation alone", "Return the page to scope, maintenance, and verification because the category name is weaker than the evidence.")
                    ),
                    List.of(
                            "Escalate if the compare path ignores whether the arsenic question is drinking-water-only or broader.",
                            "Escalate if water chemistry or arsenic form may affect performance but the page is still shopping from labels.",
                            "Escalate if post-treatment verification is not part of the decision."
                    ),
                    faq(
                            item("How do I choose between RO and adsorptive media for arsenic?", "First settle treatment scope, water chemistry fit, and how the result will be verified after install."),
                            item("Is whole-house treatment always necessary for arsenic?", "No. The page is often stronger when it starts with whether the real need is drinking and cooking water only."),
                            item("Why is verification so important on this compare page?", "Because category reputation is weaker evidence than proof that the chosen system reduced the arsenic problem you tested.")
                    )
            )),
            Map.entry("shock-vs-continuous-chlorination", winnerDoc(
                    "Shock versus continuous chlorination is an event-response versus recurring-risk decision before it is a chemical-brand decision.",
                    "This compare page is strongest when it separates one-time corrective action from ongoing microbial management and keeps follow-up testing central to both paths.",
                    List.of(
                            "Confirm whether the page is responding to a one-time event, a persistent microbial pattern, or an unresolved source problem.",
                            "Use certified follow-up testing and source-path review before the page treats chlorination mode like the whole answer.",
                            "Compare operator burden, recurrence risk, and proof-of-success only after the page has defined the actual contamination pattern."
                    ),
                    List.of(
                            "People often treat shock and continuous chlorination like simple strong versus stronger options when they answer different questions.",
                            "A one-time event page gets weaker when it drifts into permanent-treatment language too quickly.",
                            "The compare path breaks when ongoing source problems are hidden behind a disinfection choice."
                    ),
                    "Retest after shock treatment, after any continuous-chlorination change, or whenever the contamination pattern no longer looks like the original decision.",
                    splits(
                            split("The page is about a one-time contamination event", "Keep the page on corrective response and proof instead of making a permanent-system leap."),
                            split("The page is about recurring microbial risk", "Bias the comparison toward ongoing management, maintenance, and verification burden."),
                            split("The source pathway is still unresolved", "Pull the page back to root-cause review before disinfection mode becomes the headline.")
                    ),
                    List.of(
                            "Escalate if the page is choosing continuous chlorination before recurring risk is established.",
                            "Escalate if shock treatment is being treated like proof that the source problem is solved.",
                            "Escalate if follow-up testing is missing from either side of the comparison."
                    ),
                    faq(
                            item("When is shock chlorination the right lens?", "When the page is dealing with a one-time event or corrective response rather than proven recurring contamination."),
                            item("When does continuous chlorination make more sense?", "When the page has evidence of recurring microbial risk and can support the ongoing operating burden."),
                            item("Why is follow-up testing central to both options?", "Because chlorination mode is weaker evidence than proof that the contamination pattern was actually controlled.")
                    )
            )),
            Map.entry("uv-vs-ro", winnerDoc(
                    "UV versus RO is a contaminant-type decision before it is a premium-treatment decision.",
                    "This compare page is strongest when it separates microbial treatment questions from dissolved-chemical treatment questions and refuses to compare the two categories as if they solve the same target by default.",
                    List.of(
                            "Define whether the page is addressing germs, dissolved chemicals, or a mixed problem before it compares UV and RO.",
                            "Use current testing and exposure scope so the page does not let premium treatment language outrun contaminant fit.",
                            "Keep pretreatment, reject water, maintenance, and post-install verification in view after the target is defined."
                    ),
                    List.of(
                            "People often compare UV and RO like two versions of the same protection level when they answer different contaminant questions.",
                            "A page gets weaker when it treats UV like a chemical solution or RO like a stand-in for microbial control without defining the target.",
                            "Premium reputation can make the compare path sound stronger than the evidence behind it."
                    ),
                    "Retest or re-evaluate after new testing, post-install verification, or any change in the target contaminant that alters which category belongs in scope.",
                    splits(
                            split("The target is microbial contamination", "Bias the page toward disinfection logic and keep RO reputation from hijacking the decision."),
                            split("The target is dissolved chemicals or salts", "Bias the page toward contaminant-fit and scope rather than letting UV sound like broad protection."),
                            split("The target is still vague or mixed", "Pull the page back to testing because the categories should not be compared until the problem is defined.")
                    ),
                    List.of(
                            "Escalate if the compare page is treating UV and RO like interchangeable categories.",
                            "Escalate if the target contaminant has not been defined but the household is already shopping.",
                            "Escalate if maintenance, pretreatment, or post-install verification are being ignored."
                    ),
                    faq(
                            item("How do I choose between UV and RO for a well?", "First define whether the problem is germs, dissolved chemicals, or a mixed issue because the categories do not answer the same target by default."),
                            item("Can UV and RO be compared like two versions of the same product?", "No. The page is stronger only when it compares them from contaminant fit rather than treatment prestige."),
                            item("Why should testing come before this compare page?", "Because the categories should not compete until the page knows what problem they are actually supposed to solve.")
                    )
            )),
            Map.entry("when-not-to-buy-treatment-yet", winnerDoc(
                    "Not buying treatment yet is often the highest-quality decision when the evidence stack is still weak.",
                    "This authority page is strongest when it teaches users how to recognize missing scope, weak samples, unresolved exposure, or generic marketing pressure before money turns uncertainty into the wrong install.",
                    List.of(
                            "Check whether the page is missing current testing, certified-lab evidence, clear contaminant scope, or a real household-use definition.",
                            "Separate urgent exposure-control steps from treatment shopping so the household can act safely without pretending it already knows the right system.",
                            "Use claim verification and retest planning to decide when the page has finally earned a buying decision."
                    ),
                    List.of(
                            "People often confuse urgency with certainty and buy treatment before the problem is actually defined.",
                            "A delay page gets weaker if it sounds passive when the real goal is to improve the evidence quality.",
                            "The page loses value when it says wait without teaching what proof should come next."
                    ),
                    "Move from waiting to buying only after testing, scope, claim fit, and retest logic are strong enough that the install can be defended.",
                    splits(
                            split("Exposure control is urgent but the treatment answer is unclear", "Keep the page on safer interim action while the evidence stack is rebuilt."),
                            split("The household has weak or stale evidence", "Turn the page into a testing and claim-verification plan before any buying starts."),
                            split("Marketing pressure is ahead of certified proof", "Use the page to slow the decision until the claim and scope actually match the problem.")
                    ),
                    List.of(
                            "Escalate if the page is drifting into product comparison without current testing or clear contaminant scope.",
                            "Escalate if the household is treating a vague claim as stronger evidence than the lab path.",
                            "Escalate if the page says wait but fails to define the next proof needed to move forward."
                    ),
                    faq(
                            item("When should I not buy treatment yet?", "When current testing, contaminant scope, claim fit, or retest logic are still too weak to defend the install."),
                            item("Does waiting mean doing nothing?", "No. The page should replace premature buying with safer interim steps and stronger evidence gathering."),
                            item("What tells me the page is finally ready for a purchase?", "A purchase becomes defensible when testing, scope, certification fit, and verification are all stronger than the sales pressure around them.")
                    )
            )),
            Map.entry("how-to-verify-water-treatment-claims", winnerDoc(
                    "A treatment claim should match the contaminant, certification, maintenance reality, and retest plan before it earns trust.",
                    "This authority page is strongest when it teaches users to separate marketing language from proof, especially in YMYL well-water pages where category labels can outrun evidence.",
                    List.of(
                            "Start by identifying the exact contaminant or problem the claim is supposed to address.",
                            "Check certification language, capacity, maintenance, and use-case fit before treating the claim like proof.",
                            "Keep post-install retesting in scope so the page ends in verification rather than ad copy."
                    ),
                    List.of(
                            "People often treat a contaminant keyword on the box like proof that the system fits the household.",
                            "A certification badge is weaker when the page has not checked what it actually covers and under what conditions.",
                            "The page loses value when marketing claims are allowed to outrun retest logic and maintenance burden."
                    ),
                    "Re-check claims whenever the contaminant target changes, the system context changes, or the install still has not been verified by follow-up testing.",
                    splits(
                            split("The claim names the contaminant and a relevant certification clearly", "Treat the page as stronger, but still keep capacity, use-case fit, and retest in scope."),
                            split("The claim is broad, vague, or category-led", "Treat the page like a marketing-risk problem before it becomes a buying decision."),
                            split("The system is installed but no retest exists", "Pull the page back to verification because installation alone is weaker than proof.")
                    ),
                    List.of(
                            "Escalate if the page is using marketing language as stronger evidence than testing and certification detail.",
                            "Escalate if the claim sounds broad but the page has not checked what contaminants or conditions it actually covers.",
                            "Escalate if the household is ready to buy or trust the install without a retest plan."
                    ),
                    faq(
                            item("How do I know whether a water-treatment claim is real?", "Check the exact contaminant, certification, use-case fit, maintenance requirements, and retest plan instead of trusting category language alone."),
                            item("Is a certification badge enough by itself?", "Not always. The page should still check what the certification covers and whether it matches the actual household problem."),
                            item("Why does retesting matter when verifying treatment claims?", "Because the strongest claim is still weaker than proof that the installed system solved the verified problem.")
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
        return new PseoDecisionDoc(oneLineVerdict, healthVsNuisance, nextSteps, commonConfusions, retestTiming, List.of(), List.of(), faqs);
    }

    private PseoDecisionDoc winnerDoc(
            String oneLineVerdict,
            String healthVsNuisance,
            List<String> nextSteps,
            List<String> commonConfusions,
            String retestTiming,
            List<PseoDecisionSplit> decisionSplits,
            List<String> escalationSignals,
            List<PseoFaqItem> faqs
    ) {
        return new PseoDecisionDoc(oneLineVerdict, healthVsNuisance, nextSteps, commonConfusions, retestTiming, decisionSplits, escalationSignals, faqs);
    }

    private PseoDecisionDoc stateProgramDoc(
            String stateName,
            String guidanceLabel,
            String localDifference,
            String retestTriggerFocus
    ) {
        return winnerDoc(
                stateName + " private well pages should act like state-process and certified-lab pages first, not generic shopping pages.",
                "This regional page is strongest when it uses " + guidanceLabel + " to separate " + localDifference + " before treatment shopping starts.",
                List.of(
                        "Use " + stateName + " guidance first when the household still needs to narrow the testing path.",
                        "Move into certified-lab routing before you trust a thin panel or one weak clue.",
                        "Keep compare pages downstream from the state process instead of letting product shopping outrun the evidence."
                ),
                List.of(
                        "People often treat a " + stateName + " page like a generic national checklist with a state name swapped in.",
                        "The page gets weaker when it hides the state process and jumps into product categories too fast.",
                        "Certified-lab routing should behave like part of the answer, not like an optional footnote."
                ),
                "Retest whenever " + retestTriggerFocus + " or any evidence-quality gap makes the current state-guided answer weaker than it looks.",
                splits(
                        split("Routine testing and the state process are still unclear", "Keep the page on " + stateName + " guidance and lab-path discipline before compare logic opens."),
                        split("The household wants to shop from one old result or one symptom", "Pull the page back to state process, certified follow-up, and stronger evidence first."),
                        split("A local event or property change changed the testing question", "Use the " + stateName + " page to rebuild the testing path before narrowing to treatment.")
                ),
                List.of(
                        "Escalate if the page is using a state name without changing the testing order.",
                        "Escalate if certified-lab routing or official guidance still belongs upstream of any product decision.",
                        "Escalate if one stale screen or one symptom is being treated like a final answer."
                ),
                faq(
                        item("Why does " + stateName + " need its own private well page?", "Because the official guidance and lab path change what belongs upstream of treatment shopping."),
                        item("What should I do before comparing treatment in " + stateName + "?", "Use the state guidance and certified-lab route first."),
                        item("What makes this page different from a national guide?", "It changes the testing order with " + localDifference + " instead of giving the same generic answer.")
                )
        );
    }

    private List<PseoFaqItem> faq(PseoFaqItem... items) {
        return List.of(items);
    }

    private List<PseoDecisionSplit> splits(PseoDecisionSplit... items) {
        return List.of(items);
    }

    private PseoFaqItem item(String question, String answer) {
        return new PseoFaqItem(question, answer);
    }

    private PseoDecisionSplit split(String signal, String meaning) {
        return new PseoDecisionSplit(signal, meaning);
    }
}
