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
