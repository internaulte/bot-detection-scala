package modules.trafficanalysis.jobs

import modules.trafficanalysis.jobs.interfaces.TrafficAnalysisJobs
import modules.trafficanalysis.jobs.preprocessing.TrafficAnalysisPreprocessingImpl
import modules.trafficanalysis.jobs.preprocessing.interfaces.TrafficAnalysisPreprocessing
import modules.trafficanalysis.jobs.reading.TrafficAnalysisReadingImpl
import modules.trafficanalysis.jobs.reading.interfaces.TrafficAnalysisReading
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TrafficAnalysisJobsImpl(
    private val trafficAnalysisReading: TrafficAnalysisReading = new TrafficAnalysisReadingImpl,
    private val trafficAnalysisPreprocessing: TrafficAnalysisPreprocessing = new TrafficAnalysisPreprocessingImpl
) extends TrafficAnalysisJobs {
  private implicit val flinkEnv: StreamExecutionEnvironment = TrafficAnalysisUtils.flinkEnv

  override def startTrafficAnalysis: Future[Unit] = {
    Future {
      val httpTrafficStream = trafficAnalysisReading.readHttpTrafficStream
      val preprocessedHttpTrafficStream = trafficAnalysisPreprocessing.preprocessHttpTrafficLogData(httpTrafficStream)

    }
  }
}
