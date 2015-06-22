package com.eharmony.matching.featureSpecExtractor.cli

import com.eharmony.matching.aloha.FileLocations
import com.eharmony.matching.aloha.io.StringReadable
import com.eharmony.matching.testhelp.io.{TestWithIoCapture, IoCaptureCompanion}
import org.apache.commons.io.IOUtils
import org.apache.commons.vfs2.VFS
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

/**
 * Created by rdeak on 6/19/15.
 */
@RunWith(classOf[BlockJUnit4ClassRunner])
object DatasetCliTest extends IoCaptureCompanion
class DatasetCliTest extends TestWithIoCapture(DatasetCliTest) {
    @Test def cliTest1(): Unit = {

        val input = Seq(
            "MALE,205,stuff|things",
            "FEMALE,115,"
        ).mkString("\n")

        val expected = Seq(
            "| gender=MALE weight=200 num_likes=14",
            "| gender=FEMALE weight=110 num_likes=0"
        ).mkString("\n")

        val tmpFile = "tmp:out.vw"

        val args = Array[String](
            "-s", "res:com/eharmony/matching/featureSpecExtractor/cli/csv_spec1.js",
            "-c", "res:com/eharmony/matching/featureSpecExtractor/cli/csv_types1.js",
            "--cachedir", FileLocations.testGeneratedClasses.getCanonicalPath,
            "--vw", tmpFile
        )

        val in = System.in
        System.setIn(IOUtils.toInputStream(input))
        DatasetCli.main(args)
        System.setIn(in)
        val out = StringReadable.fromVfs2(VFS.getManager.resolveFile(tmpFile)).trim
        assertEquals(expected, out)
    }
}