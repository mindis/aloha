package com.eharmony.aloha.models.tree.decision

import java.util.concurrent.atomic.AtomicBoolean

import com.eharmony.aloha.ModelSerializationTestHelper
import com.eharmony.aloha.factory.{BasicModelParser, ModelFactory, ModelParser, ParserProviderCompanion}
import com.eharmony.aloha.id.ModelId
import com.eharmony.aloha.models.{ErrorModel, BaseModel, ConstantModel, Model}
import com.eharmony.aloha.reflect.RefInfo
import com.eharmony.aloha.score.conversions.ScoreConverter
import com.eharmony.aloha.score.conversions.ScoreConverter.Implicits.NothingScoreConverter
import com.eharmony.aloha.score.conversions.rich.RichScore
import com.eharmony.aloha.semantics.Semantics
import com.eharmony.aloha.semantics.func.{GenAggFunc, GenFunc, GeneratedAccessor}
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import spray.json._
import spray.json.DefaultJsonProtocol._

import scala.collection.JavaConversions.asScalaBuffer

@RunWith(classOf[BlockJUnit4ClassRunner])
class ModelDecisionTreeTest extends ModelSerializationTestHelper {
    import ModelDecisionTreeTest._

    @Test def test_tttt_ee() { success(tttt, ee, missingBothFeatures, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_tttf_ee() { failure(tttf, ee, missingBothFeatures, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ttft_ee() { success(ttft, ee, missingBothFeatures, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ttff_ee() { failure(ttff, ee, missingBothFeatures, errorIndicatesMissingDataInPredicateInInnerModel) }
    @Test def test_tftt_ee() { failure(tftt, ee, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tftf_ee() { failure(tftf, ee, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfft_ee() { failure(tftf, ee, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfff_ee() { failure(tfff, ee, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fttt_ee() { success(fttt, ee, missingBothFeatures, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_fttf_ee() { failure(fttf, ee, missingBothFeatures, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ftft_ee() { success(ftft, ee, missingBothFeatures, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ftff_ee() { failure(ftff, ee, missingBothFeatures, errorIndicatesMissingDataInPredicateInInnerModel) }
    @Test def test_fftt_ee() { failure(fftt, ee, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_fftf_ee() { failure(fftf, ee, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_ffft_ee() { failure(ffft, ee, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_ffff_ee() { failure(ffff, ee, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }

    @Test def test_tttt_en() { success(tttt, en, missingFirstFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_tttf_en() { failure(tttf, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ttft_en() { success(ttft, en, missingFirstFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ttff_en() { failure(ttff, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_tftt_en() { failure(tftt, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tftf_en() { failure(tftf, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfft_en() { failure(tfft, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfff_en() { failure(tfff, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fttt_en() { success(fttt, en, missingFirstFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_fttf_en() { failure(fttf, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ftft_en() { success(ftft, en, missingFirstFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ftff_en() { failure(ftff, en, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_fftt_en() { failure(fftt, en, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_fftf_en() { failure(fftf, en, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_ffft_en() { failure(ffft, en, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_ffff_en() { failure(ffff, en, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }

    @Test def test_tttt_ep() { success(tttt, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_tttf_ep() { success(tttf, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ttft_ep() { success(ttft, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ttff_ep() { success(ttff, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_tftt_ep() { failure(tftt, ep, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tftf_ep() { failure(tftf, ep, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfft_ep() { failure(tfft, ep, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfff_ep() { failure(tfff, ep, missingFirstFeature, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fttt_ep() { success(fttt, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_fttf_ep() { success(fttf, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ftft_ep() { success(ftft, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ftff_ep() { success(ftff, ep, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_fftt_ep() { failure(fftt, ep, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_fftf_ep() { failure(fftf, ep, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_ffft_ep() { failure(ffft, ep, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }
    @Test def test_ffff_ep() { failure(ffff, ep, missingFirstFeature, errorIndicatesMissingDataInPredicateInOuterModel) }


    @Test def test_tttt_ne() { success(tttt, _ne, missingSecondFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_tttf_ne() { failure(tttf, _ne, missingSecondFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ttft_ne() { success(ttft, _ne, missingSecondFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ttff_ne() { failure(ttff, _ne, missingSecondFeature, errorIndicatesMissingDataInPredicateInInnerModel) }
    @Test def test_tftt_ne() { failure(tftt, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tftf_ne() { failure(tftf, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfft_ne() { failure(tfft, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfff_ne() { failure(tfff, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fttt_ne() { success(fttt, _ne, missingSecondFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_fttf_ne() { failure(fttf, _ne, missingSecondFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ftft_ne() { success(ftft, _ne, missingSecondFeature, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ftff_ne() { failure(ftff, _ne, missingSecondFeature, errorIndicatesMissingDataInPredicateInInnerModel) }
    @Test def test_fftt_ne() { failure(fftt, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fftf_ne() { failure(fftf, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_ffft_ne() { failure(ffft, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_ffff_ne() { failure(ffff, _ne, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }

    @Test def test_tttt_nn() { success(tttt, nn, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_tttf_nn() { failure(tttf, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ttft_nn() { success(ttft, nn, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ttff_nn() { failure(ttff, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_tftt_nn() { failure(tftt, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tftf_nn() { failure(tftf, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfft_nn() { failure(tfft, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfff_nn() { failure(tfff, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fttt_nn() { success(fttt, nn, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_fttf_nn() { failure(fttf, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ftft_nn() { success(ftft, nn, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelInterior) }
    @Test def test_ftff_nn() { failure(ftff, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_fftt_nn() { failure(fftt, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fftf_nn() { failure(fftf, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_ffft_nn() { failure(ffft, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_ffff_nn() { failure(ffff, nn, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }

    @Test def test_tttt_np() { success(tttt, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_tttf_np() { success(tttf, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ttft_np() { success(ttft, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ttff_np() { success(ttff, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_tftt_np() { failure(tftt, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tftf_np() { failure(tftf, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfft_np() { failure(tfft, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_tfff_np() { failure(tfff, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fttt_np() { success(fttt, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_fttf_np() { success(fttf, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ftft_np() { success(ftft, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_ftff_np() { success(ftff, np, noneMissing, noErrorMessages, scoreIndicatesFirstInnerModelLeaf) }
    @Test def test_fftt_np() { failure(fftt, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_fftf_np() { failure(fftf, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_ffft_np() { failure(ffft, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }
    @Test def test_ffff_np() { failure(ffff, np, noneMissing, errorIndicatesNoPredicateSatisfiedInOuterModel) }

    @Test def test_tttt_pe() { success(tttt, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_tttf_pe() { failure(tttf, pe, missingSecondFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ttft_pe() { success(ttft, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_ttff_pe() { failure(ttff, pe, missingSecondFeature, errorIndicatesMissingDataInPredicateInInnerModel) }
    @Test def test_tftt_pe() { success(tftt, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_tftf_pe() { failure(tftf, pe, missingSecondFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_tfft_pe() { success(tfft, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_tfff_pe() { failure(tfff, pe, missingSecondFeature, errorIndicatesMissingDataInPredicateInInnerModel) }
    @Test def test_fttt_pe() { success(fttt, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_fttf_pe() { failure(fttf, pe, missingSecondFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ftft_pe() { success(ftft, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_ftff_pe() { failure(ftff, pe, missingSecondFeature, errorIndicatesMissingDataInPredicateInInnerModel) }
    @Test def test_fftt_pe() { success(fftt, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_fftf_pe() { failure(fftf, pe, missingSecondFeature, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ffft_pe() { success(ffft, pe, missingSecondFeature, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_ffff_pe() { failure(ffff, pe, missingSecondFeature, errorIndicatesMissingDataInPredicateInInnerModel) }

    @Test def test_tttt_pn() { success(tttt, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_tttf_pn() { failure(tttf, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ttft_pn() { success(ttft, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_ttff_pn() { failure(ttff, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_tftt_pn() { success(tftt, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_tftf_pn() { failure(tftf, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_tfft_pn() { success(tfft, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_tfff_pn() { failure(tfff, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_fttt_pn() { success(fttt, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_fttf_pn() { failure(fttf, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ftft_pn() { success(ftft, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_ftff_pn() { failure(ftff, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_fftt_pn() { success(fftt, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_fftf_pn() { failure(fftf, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }
    @Test def test_ffft_pn() { success(ffft, pn, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelInterior) }
    @Test def test_ffff_pn() { failure(ffff, pn, noneMissing, errorIndicatesNoPredicateSatisfiedInInnerModel) }

    @Test def test_pp() { models.foreach(m => success(m, pp, noneMissing, noErrorMessages, scoreIndicatesSecondInnerModelLeaf) ) }

    @Test def testSerialization(): Unit = {
        val sub = ErrorModel(ModelId(2, "abc"), Seq("def", "ghi"))
        val m = ModelDecisionTree(ModelId(2, "abc"), root = Leaf(sub), returnBest = true)
        val m1 = serializeDeserializeRoundTrip(m)
        assertEquals(m, m1)
    }

    /**
     * Get a DecisionTreeModel with a full 3-layer binary tree with 7 (2^3^ - 1) nodes.  Call close on the
     * DecisionTreeModel but not the submodels.  Then check that the submodels were closed.
     */
    @Test def testClosingSubmodels(): Unit = {
        val (modelDecisionTree, submodels) = treeAndCheckerModels()
        assertEquals(7, submodels.size)
        modelDecisionTree.close()
        assertTrue(submodels.forall(_.closed))
    }

    def success(m: ModelContainer[Map[String, Double], Int], x: Map[String, Double], missing: Seq[String], errors: Seq[String], exp: Int) {
        val s = m.model.score(x)

        assertTrue("Model should produce a score.", s.hasScore)
        assertEquals("Incorrect score produced", exp, s.relaxed.asInt.get)
        assertEquals("One subscore should be produced. Found none", 1, s.getSubScoresList.size)
        assertEquals("Incorrect sub-score produced", exp, s.getSubScoresList.head.relaxed.asInt.get)

        if (missing.nonEmpty || errors.nonEmpty) {
            assertTrue("An error object should be present.", s.hasError)

            val e = s.getError

            if (missing.nonEmpty)
                assertTrue("Model should produce an error with missing features.", e.hasMissingFeatures)

            assertEquals("Difference in expected missing features: ", missing, e.getMissingFeatures.getNamesList.toSeq)


            if (errors.nonEmpty)
                assertTrue("Model should produce an error with causes.", e.getMessagesCount > 0)

            assertEquals("Difference in expected error messages: ", errors, e.getMessagesList.toSeq)
        }
    }

    def failure(m: ModelContainer[Map[String, Double], Int], x: Map[String, Double], missing: Seq[String], errors: Seq[String]) {
        val s = m.model.score(x)

        if (s.hasScore)
            assertFalse("Model should NOT successfully produce a score.  Found: " + s.relaxed.asInt.get, s.hasScore)

        assertTrue("Model should produce an error.", s.hasError)

        val e = s.getError

        if (missing.nonEmpty)
            assertTrue("Model should produce an error with missing features.", e.hasMissingFeatures)

        assertEquals("Difference in expected missing features: ", missing, e.getMissingFeatures.getNamesList.toSeq)

        if (errors.nonEmpty)
            assertTrue("Model should produce an error with causes.", e.getMessagesCount > 0)

        assertEquals("Difference in expected error messages: ", errors, e.getMessagesList.toSeq)
    }

    // Code used to generate data for tests.
//    @Ignore @Test def test1() {
//        def maps[A](s: Seq[Option[A]], key: String) = s.map(_.map(key -> _).toMap)
//        val vals = Seq(None, Some(-1.0), Some(1.0))
//
//        for {
//            first <- maps(vals, "first_feature")
//            second <- maps(vals, "second_feature")
//            t = first ++ second
//            m <- models
//            s = m.model.score(t)
//            hasScore = s.hasScore
//            scoreVal = s.relaxed.asInt.map(_.toString).getOrElse("")
//            scoreId = if (s.getScore.getModel.hasId) s.getScore.getModel.getId.toString else ""
//            hasError = s.hasError
//            hasMissing = s.getError.hasMissingFeatures
//            missing = s.getError.getMissingFeatures.getNamesList.toSeq.mkString(",")
//            errorMsgs = s.getError.getMessagesList.toSeq.mkString("; ")
//        } println(s"${m.missing1}\t${m.best1}\t${m.missing2}\t${m.best2}\t$t\t$hasScore\t$scoreVal\t$scoreId\t$hasError\t$hasMissing\t$missing\t$errorMsgs")
//    }
}

object ModelDecisionTreeTest {

    case class CloseCheckerModel(modelId: ModelId) extends BaseModel[Any, Nothing] {
        private[this] val c = new AtomicBoolean(false)
        private[this] val Seq(w, wo) = Seq(true, false) map { audit => failure(Seq(getClass.getSimpleName + " always errors"))(audit) }
        private[aloha] def getScore(a: Any)(implicit audit: Boolean) = if (audit) w else wo
        override def close(): Unit = c.set(true)
        def closed = c.get  // always false until close is called, then always true.
    }

    object CloseCheckerModel extends ParserProviderCompanion {
        object Parser extends BasicModelParser {
            val modelType = "CloseChecker"
            def modelJsonReader[A, B: JsonReader : ScoreConverter]: JsonReader[CloseCheckerModel] = new JsonReader[CloseCheckerModel] {
                def read(json: JsValue) = json.convertTo(jsonFormat(CloseCheckerModel(_), "modelId"))
            }
        }
        def parser: ModelParser = Parser
    }

    private[ModelDecisionTreeTest] def treeAndCheckerModels() = {
        import com.eharmony.aloha.score.conversions.ScoreConverter.Implicits.IntScoreConverter
        import spray.json.DefaultJsonProtocol.IntJsonFormat

        // Don't need semantics since no features.  Just reuse any semantics. Only need these 2 parsers.
        val factory = ModelFactory(ModelDecisionTree.parser, CloseCheckerModel.parser).
                        toTypedFactory[Map[String, Double], Int](semantics)

        val json = s"""
                      |{
                      |  "modelType": "ModelDecisionTree",
                      |  "modelId": { "id": 100, "name": "tree" },
                      |  "returnBest": true,
                      |  "missingDataOk": false,
                      |  "nodes": [
                      |    {
                      |      "id": 0,
                      |      "value": { "modelType": "CloseChecker", "modelId": { "id": 0, "name": "checker" } },
                      |      "selector": { "selectorType": "linear", "children": [1, 4], "predicates": ["true", "false"] }
                      |    },
                      |    {
                      |      "id": 1,
                      |      "value": { "modelType": "CloseChecker", "modelId": { "id": 1, "name": "checker" } },
                      |      "selector": { "selectorType": "linear", "children": [2, 3], "predicates": ["true", "false"] }
                      |    },
                      |    { "id": 2, "value": { "modelType": "CloseChecker", "modelId": { "id": 2, "name": "checker" } } },
                      |    { "id": 3, "value": { "modelType": "CloseChecker", "modelId": { "id": 3, "name": "checker" } } },
                      |    {
                      |      "id": 4,
                      |      "value": { "modelType": "CloseChecker", "modelId": { "id": 4, "name": "checker" } },
                      |      "selector": { "selectorType": "linear", "children": [5, 6], "predicates": ["true", "false"] }
                      |    },
                      |    { "id": 5, "value": { "modelType": "CloseChecker", "modelId": { "id": 5, "name": "checker" } } },
                      |    { "id": 6, "value": { "modelType": "CloseChecker", "modelId": { "id": 6, "name": "checker" } } }
                      |  ]
                      |}
                    """.stripMargin.trim

        val attempt = factory.fromString(json)
        val t = attempt.get.asInstanceOf[ModelDecisionTree[Map[String, Double], Int]]
        val cs = t.root.dfs().map{ _._1.value }.collect{ case c: CloseCheckerModel => c }.toVector
        (t, cs)
    }

    case class ModelContainer[A, B](model: Model[A, B], missing1: Boolean, best1: Boolean, missing2: Boolean, best2: Boolean)

    /** This semantics operates on Map[String, Double].  Produces functions that return true when the key exists in
      * the map and the value associated value is non-negative.  The functions return false when the key exists in
      * the map but the values
      */
    private[this] val semantics: Semantics[Map[String, Double]] = new Semantics[Map[String, Double]] {
        def refInfoA = RefInfo[Map[String, Double]]
        def close() {}
        def accessorFunctionNames: Seq[String] = Nil
        def createFunction[B: RefInfo](codeSpec: String, default: Option[B]): Either[Seq[String], GenAggFunc[Map[String, Double], B]] = {
            val cs = codeSpec.trim
            val ga = GeneratedAccessor(cs, (_: Map[String, Double]).get(cs), Option("""(_: Map[String, Double]).get(cs)"""))
            val f = GenFunc.f1(ga)("${"+cs+"} >= 0", _ map {_ >= 0})
            val g = Right(f.asInstanceOf[GenAggFunc[Map[String, Double], B]])
            g
        }
    }

    private[this] def model(missing1: Boolean, best1: Boolean, missing2: Boolean, best2: Boolean) = {
        import com.eharmony.aloha.score.conversions.ScoreConverter.Implicits.IntScoreConverter
        import spray.json.DefaultJsonProtocol.IntJsonFormat

        val f = ModelFactory(ConstantModel.parser, BasicDecisionTree.parser, ModelDecisionTree.parser)
        val factory = f.toTypedFactory[Map[String, Double], Int](semantics)
        val mTry = factory.fromString(json(missing1, best1, missing2, best2)) // So we can see the exception in debugging.
        val m = mTry.get
        ModelContainer(m, missing1, best1, missing2, best2)
    }

    val noneMissing = Seq.empty[String]
    val missingFirstFeature = Seq("first_feature")
    val missingSecondFeature = Seq("second_feature")
    val missingBothFeatures = Seq("first_feature", "second_feature")

    val errorIndicatesMissingDataInPredicateInOuterModel = Seq("Encountered unacceptable missing data in predicate: ${first_feature} >= 0")
    val errorIndicatesMissingDataInPredicateInInnerModel = Seq("Encountered unacceptable missing data in predicate: ${second_feature} >= 0")
    val errorIndicatesNoPredicateSatisfiedInOuterModel = Seq("No decision tree predicate satisfied. Tried: [GenAggFunc((${first_feature}) => ${first_feature} >= 0)]")
    val errorIndicatesNoPredicateSatisfiedInInnerModel = Seq("No decision tree predicate satisfied. Tried: [GenAggFunc((${second_feature}) => ${second_feature} >= 0)]")
    val noErrorMessages = Seq.empty[String]

    val scoreIndicatesFirstInnerModelInterior = 11
    val scoreIndicatesFirstInnerModelLeaf = 21
    val scoreIndicatesSecondInnerModelInterior = 12
    val scoreIndicatesSecondInnerModelLeaf = 22

    val tttt = model(missing1 = true,  best1 = true,  missing2 = true,  best2 = true)
    val tttf = model(missing1 = true,  best1 = true,  missing2 = true,  best2 = false)
    val ttft = model(missing1 = true,  best1 = true,  missing2 = false, best2 = true)
    val ttff = model(missing1 = true,  best1 = true,  missing2 = false, best2 = false)
    val tftt = model(missing1 = true,  best1 = false, missing2 = true,  best2 = true)
    val tftf = model(missing1 = true,  best1 = false, missing2 = true,  best2 = false)
    val tfft = model(missing1 = true,  best1 = false, missing2 = false, best2 = true)
    val tfff = model(missing1 = true,  best1 = false, missing2 = false, best2 = false)
    val fttt = model(missing1 = false, best1 = true,  missing2 = true,  best2 = true)
    val fttf = model(missing1 = false, best1 = true,  missing2 = true,  best2 = false)
    val ftft = model(missing1 = false, best1 = true,  missing2 = false, best2 = true)
    val ftff = model(missing1 = false, best1 = true,  missing2 = false, best2 = false)
    val fftt = model(missing1 = false, best1 = false, missing2 = true,  best2 = true)
    val fftf = model(missing1 = false, best1 = false, missing2 = true,  best2 = false)
    val ffft = model(missing1 = false, best1 = false, missing2 = false, best2 = true)
    val ffff = model(missing1 = false, best1 = false, missing2 = false, best2 = false)
    val models = Seq(tttt, tttf, ttft, ttff, tftt, tftf, tfft, tfff, fttt, fttf, ftft, ftff, fftt, fftf, ffft, ffff)

    val ee = Map.empty[String, Double]
    val en = Map("second_feature" -> -1.0)
    val ep = Map("second_feature" -> 1.0)
    val _ne = Map("first_feature" -> -1.0)
    val nn = Map("first_feature" -> -1.0, "second_feature" -> -1.0)
    val np = Map("first_feature" -> -1.0, "second_feature" -> 1.0)
    val pe = Map("first_feature" -> 1.0)
    val pn = Map("first_feature" -> 1.0, "second_feature" -> -1.0)
    val pp = Map("first_feature" -> 1.0, "second_feature" -> 1.0)
    val inputs: Seq[Map[String, Double]] = Seq(ee, en, ep, _ne, nn, np, pe, pn, pp)

    private[this] def json(missing1: Boolean, best1: Boolean, missing2: Boolean, best2: Boolean) =
        s"""
          |{
          |  "modelType": "ModelDecisionTree",
          |  "modelId": {"id": 0, "name": ""},
          |  "returnBest": $best1,
          |  "missingDataOk": $missing1,
          |  "nodes": [
          |    {
          |      "id": 1,
          |      "value": {
          |        "modelType": "DecisionTree",
          |        "modelId": {"id": 1, "name": ""},
          |        "returnBest": $best2,
          |        "missingDataOk": $missing2,
          |        "nodes": [
          |          {
          |            "id": 0,
          |            "value": 11,
          |            "selector": { "selectorType": "linear", "children": [1], "predicates": ["second_feature"] }
          |          },
          |          { "id": 1, "value": 21 }
          |        ]
          |      },
          |      "selector": { "selectorType": "linear", "children": [2], "predicates": ["first_feature"] }
          |    },
          |    {
          |      "id": 2,
          |      "value": {
          |        "modelType": "DecisionTree",
          |        "modelId": {"id": 2, "name": ""},
          |        "returnBest": $best2,
          |        "missingDataOk": $missing2,
          |        "nodes": [
          |          {
          |            "id": 0,
          |            "value": 12,
          |            "selector": { "selectorType": "linear", "children": [1], "predicates": ["second_feature"] }
          |          },
          |          { "id": 1, "value": 22 }
          |        ]
          |      }
          |    }
          |  ]
          |}
        """.stripMargin.trim
}
